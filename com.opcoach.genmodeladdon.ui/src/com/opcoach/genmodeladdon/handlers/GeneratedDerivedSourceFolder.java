package com.opcoach.genmodeladdon.handlers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.opcoach.genmodeladdon.core.EMFPatternExtractor;
import com.opcoach.genmodeladdon.core.GenerateDevStructure;
import com.opcoach.genmodeladdon.ui.dialog.ConfirmFileSelectionDialog;
import com.opcoach.genmodeladdon.ui.dialog.DerivedSourceParametersDialog;

public class GeneratedDerivedSourceFolder extends GenerateParentHandler
{

	private Shell parentShell;

	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Named(IServiceConstants.ACTIVE_SELECTION) GenModel gm)
	{
		parentShell = shell;

		Shell s = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		s.setText("Enter the parameter for the development structure generation");
		DerivedSourceParametersDialog dial = new DerivedSourceParametersDialog(s);
		dial.setGenModel(gm);

		int status = dial.open();
		if (status == Dialog.OK)
		{
			String ip = dial.getDevInterfacePattern();
			String cp = dial.getDevClassPattern();
			String src = dial.getSrcDir();

			// Check the genModel dynamic templates.
			if (!checkGenModelTemplates(gm))
				return ;

			// Try to generate to check the files that could be
			// overridden
			GenerateDevStructure gds = new GenerateDevStructure(gm, cp, ip, src);
			gds.generateDevStructure(false);

			Map<String, Object> filesNotGenerated = gds.filesNotGenerated;
			StringBuffer filesInError = new StringBuffer();

			int confirmCode = ConfirmFileSelectionDialog.OK;
			ConfirmFileSelectionDialog cfs = null;

			if (!filesNotGenerated.isEmpty())
			{
				cfs = new ConfirmFileSelectionDialog(parentShell, filesNotGenerated, src);
				confirmCode = cfs.open();
			}

			if (confirmCode == ConfirmFileSelectionDialog.OK)
			{
				// Now can generate really the files.
				gds.generateDevStructure(true);

				// Must ask if the not generated files can override
				// existing files...
				if (cfs != null)
				{
					for (String fn : cfs.getFilesToBeGenerated())
					{
						// Open the file and generate contents
						try
						{
							FileWriter fw = new FileWriter(fn);
							fw.write(filesNotGenerated.get(fn).toString());
							fw.flush();
							fw.close();
						} catch (IOException e)
						{
							Bundle bundle = FrameworkUtil.getBundle(this.getClass());
							ILog log = Platform.getLog(bundle);
							String mess = "Unable to generate the file : " + fn;
							Status st = new Status(Status.WARNING, bundle.getSymbolicName(), Status.OK, mess, e);
							log.log(st);
							int pos = fn.indexOf(src);
							filesInError.append(fn.substring(pos)).append("\n");
						}

					}
				}

				// Refresh the workspace.
				IProject proj = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(getProjectName(gm));
				// Extract EMF templates to modify the way to inherit
				// from ancestor
				EMFPatternExtractor extractor = new EMFPatternExtractor(proj, cp, ip);
				extractor.run();
				try
				{
					proj.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e)
				{
				}

				// Display a sum up dialog
				if (filesInError.length() == 0)
					MessageDialog.openInformation(parentShell, "Files generated",
							"Files have been generated in this directory : \n\n" + gds.getSrcAbsolutePath());
				else
				{
					MessageDialog.openWarning(
							parentShell,
							"Files generated with errors",
							"Some errors occured during the generation.\n\n These files were not generated : \n"
									+ filesInError
									+ "\n\nThe other files have been generated without any error here : \n"
									+ gds.getSrcAbsolutePath()
									+ "\n\n---------------------------------------------------\nCheck the logs in : "
									+ Platform.getLogFileLocation().toPortableString());
				}
			}

		}
	}

	/**
	 * This method checks if the genModel has a dynamic templates property and a
	 * template directory set to projectName/templates
	 */
	private boolean checkGenModelTemplates(GenModel gm)
	{
		StringBuffer changes = new StringBuffer();
		boolean result = true;

		if (!gm.isDynamicTemplates())
		{
			gm.setDynamicTemplates(true);
			changes.append("The dynamic template property must be set to true");
		}

		String expectedTemplateDir = "/" + getProjectName(gm) + "/templates";
		String currentTemplateDir = gm.getTemplateDirectory();
		if (!expectedTemplateDir.equals(currentTemplateDir))
		{
			gm.setTemplateDirectory(expectedTemplateDir);
			if ((currentTemplateDir != null) && (currentTemplateDir.length() > 0))
			{
				changes.append("\nThe  template directory must be changed :  \n");
				changes.append("\n   Previous value was : " + currentTemplateDir);
				changes.append("\n   New value is       : " + expectedTemplateDir);

			} else
			{
				changes.append("The template directory has been set to : " + expectedTemplateDir);
			}
		}

		// Inform user of changes and save the file.
		if (changes.length() > 0)
		{
			if (result = MessageDialog.openConfirm(parentShell, "Your genModel file must be updated",
					"Do you confirm the following changes on your gen model : \n\n" + changes))
			{
				final Map<Object, Object> opt = new HashMap<Object, Object>();
				opt.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
				opt.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);
				try
				{
					gm.eResource().save(opt);
				} catch (IOException e)
				{
					MessageDialog.openInformation(parentShell, "genModel could not be saved",
							"The genmodel could not be saved.\nReason is : " + e.getMessage());
					Bundle bundle = FrameworkUtil.getBundle(this.getClass());
					ILog logger = Platform.getLog(bundle);
					logger.log(new Status(IStatus.WARNING, bundle.getSymbolicName(),
							"Unable to save the genModel in : " + gm.eResource(), e));
				}
			}

		}

		return result;

	}


}
