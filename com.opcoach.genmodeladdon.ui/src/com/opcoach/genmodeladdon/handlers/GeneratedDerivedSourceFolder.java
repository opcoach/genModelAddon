package com.opcoach.genmodeladdon.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

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

			GenerateDevStructure gds = new GenerateDevStructure(gm, cp, ip, src);

			// Check the genModel dynamic templates.
			String changes = gds.setGenModelTemplates(gm, false);
			if (changes.length() > 0)
			{
				if (!confirmSaveGenModelTemplates(gm, changes))
					return;
			}
						

			// Try to generate to check the files that could be
			// overridden
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

		
				// Display a sum up dialog
				if (filesInError.length() == 0)
					MessageDialog.openInformation(parentShell, "Files generated",
							"Files have been generated in this directory : \n\n" + gds.getSrcAbsolutePath());
				else
				{
					MessageDialog.openWarning(parentShell, "Files generated with errors",
							"Some errors occured during the generation.\n\n These files were not generated : \n"
									+ filesInError
									+ "\n\nThe other files have been generated without any error here : \n"
									+ gds.getSrcAbsolutePath()
									+ "\n\n---------------------------------------------------\nCheck the logs in : "
									+ Platform.getLogFileLocation().toPortableString());
				}
				
				// Generate the next step : ant file and EMF code
				if (MessageDialog.openConfirm(parentShell, "Confirm next step", 
						"The EMF code must be regenerated to take this generation code into account. \n"
						+ " Do you want to do it ? (you will have to do it by hand anyway). "))
				{
					// Generate the ant file and call it with the ant Runner
					// Generate the ant file to generate emf code
					File antFile = gds.generateAntFile();

					// Once dev structure is generated and ant file too, can call it !
					gds.generateGenModelCode(antFile);
					
					// Then should reorganize imports in the project in package explorer view ! 
					
				}
			}

		}
	}

	
	/**
	 * This method checks if the genModel has a dynamic templates property and a
	 * template directory set to projectName/templates
	 */
	private boolean confirmSaveGenModelTemplates(GenModel gm, String changes)
	{
		boolean result = false;

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
