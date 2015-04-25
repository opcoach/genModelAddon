package com.opcoach.genmodeladdon.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.opcoach.genmodeladdon.core.GenerateAntFileForCodeGeneration;

public class GenerateAntFileHandler extends AbstractHandler
{

	private final static String ANT_FILENAME = "generateEMFCode.xml";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		Shell parentShell = HandlerUtil.getActiveShell(event);
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof IStructuredSelection)
		{
			Object selected = ((IStructuredSelection) sel).getFirstElement();
			if (selected instanceof GenModel)
			{
				GenModel gm = (GenModel) selected;

				File f = getAntFile(gm);
				boolean createFile = true;
				if (f.exists())
				{
					createFile = MessageDialog.openConfirm(parentShell, "This ant file exists",
							"Do you really want to override this file " + ANT_FILENAME + " ?");
				}
				
				if (createFile)
				{

					GenerateAntFileForCodeGeneration gen = new GenerateAntFileForCodeGeneration();
					String s = gm.eResource().toString();
					int pos = s.lastIndexOf(File.separator);
					String modelName = s.substring(pos+1);
					pos = modelName.indexOf(".genmodel");
					modelName = modelName.substring(0,pos);
					

					try
					{
						if (!f.exists())
							f.createNewFile();
						FileWriter fw = new FileWriter(f);
						fw.write(gen.generateAntFileContent(modelName).toString());
						fw.flush();
						fw.close();
						
						// Add a refresh
						IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject(extractProjectName(gm));
						
						proj.refreshLocal(IResource.DEPTH_ONE, null);
						
					} catch (IOException e)
					{
						MessageDialog.openWarning(parentShell, "Ant file could not be generated",
								"It was not possible to generate the file : " + ANT_FILENAME
										+ "\n\n The reason is : \n\n" + e.getMessage());
					} catch (CoreException e)
					{
						MessageDialog.openWarning(parentShell, "Could not refresh the workspace",
								"It was not possible refresh the workspace. You should do it by hand");
					}
				}

			}
		}
		return null;

	}

	private File getAntFile(GenModel gm)
	{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject proj = root.getProject(extractProjectName(gm));
		String srcAbsolutePath = proj.getLocation().toOSString() + File.separator + ANT_FILENAME;
		File f = new File(srcAbsolutePath);
		return f;
	}

	static String extractProjectName(GenModel gm)
	{
		String genModelUri = gm.eResource().getURI().toString();
		String nameStartingWithProjectName = genModelUri.replace("platform:/resource/", "");
		int pos = nameStartingWithProjectName.indexOf("/");
		return nameStartingWithProjectName.substring(0, pos);
	}

}
