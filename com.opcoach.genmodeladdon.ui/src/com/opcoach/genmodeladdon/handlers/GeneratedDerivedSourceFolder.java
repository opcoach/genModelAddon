package com.opcoach.genmodeladdon.handlers;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.opcoach.genmodeladdon.core.GenerateDevStructure;
import com.opcoach.genmodeladdon.ui.dialog.DerivedSourceParametersDialog;

public class GeneratedDerivedSourceFolder extends AbstractHandler
{

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
					gds.generateDevStructure();

					Map<String, Object> filesNotGenerated = gds.filesNotGenerated;
					if (!filesNotGenerated.isEmpty())
					{
						// Must ask if the not generated files can override
						// existing files...
						for (String fn : filesNotGenerated.keySet())
						{
							boolean confirm = MessageDialog.openConfirm(parentShell, "Override existing file",
									"The file " + fn + " already exists. \n\nDo you really want to override it ? ");
							if (confirm)
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
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
					
					// Display a dialog to explain where files have been generated.
					MessageDialog.openInformation(parentShell, "Files generated", "Files have been generated in this directory : \n\n" + gds.getSrcAbsolutePath());
				}
			}
		}

		return null;
	}
}
