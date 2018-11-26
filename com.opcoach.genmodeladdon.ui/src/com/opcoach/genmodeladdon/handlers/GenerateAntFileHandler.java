package com.opcoach.genmodeladdon.handlers;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.opcoach.genmodeladdon.core.GenerateAntFileForCodeGeneration;
import com.opcoach.genmodeladdon.core.GenerateCommon;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;

public class GenerateAntFileHandler extends GenerateParentHandler
{


	@Execute
	@Override
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell,
			@Named(IServiceConstants.ACTIVE_SELECTION) GMAGenModel gm)
	{
		GenerateAntFileForCodeGeneration gen = new GenerateAntFileForCodeGeneration();
		IProject proj = GenerateCommon.getProject(gm);
		File f = gen.getAntFile(proj);
		boolean createFile = true;
		if (f.exists())
		{
			createFile = MessageDialog.openConfirm(parentShell, "This ant file exists",
					"Do you really want to override this file " + GenerateAntFileForCodeGeneration.ANT_FILENAME + " ?");
		}

		if (createFile)
		{
			String modelName = GenerateCommon.getModelName(gm);
			String modelPath = GenerateCommon.getModelPath(gm);

			try
			{
				gen.generateAntFile(modelPath, modelName, proj);
				
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
