package com.opcoach.genmodeladdon.handlers;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.opcoach.genmodeladdon.core.GenerateAntFileForCodeGeneration;

public class GenerateAntFileHandler extends GenerateParentHandler
{


	@Execute
	@Override
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell,
			@Named(IServiceConstants.ACTIVE_SELECTION) GenModel gm)
	{
		GenerateAntFileForCodeGeneration gen = new GenerateAntFileForCodeGeneration();

		File f = gen.getAntFile(gm);
		boolean createFile = true;
		if (f.exists())
		{
			createFile = MessageDialog.openConfirm(parentShell, "This ant file exists",
					"Do you really want to override this file " + GenerateAntFileForCodeGeneration.ANT_FILENAME + " ?");
		}

		if (createFile)
		{

			String s = gm.eResource().toString();
			int pos = s.lastIndexOf(File.separator);
			String modelName = s.substring(pos + 1);
			pos = modelName.indexOf(".genmodel");
			modelName = modelName.substring(0, pos);

			try
			{
				gen.generateAntFile(gm);
				
			} catch (IOException e)
			{
				MessageDialog.openWarning(
						parentShell,
						"Ant file could not be generated",
						"It was not possible to generate the file : " + GenerateAntFileForCodeGeneration.ANT_FILENAME + "\n\n The reason is : \n\n"
								+ e.getMessage());
			} catch (CoreException e)
			{
				MessageDialog.openWarning(parentShell, "Could not refresh the workspace",
						"It was not possible refresh the workspace. You should do it by hand");
			}
		}

	}

	



}
