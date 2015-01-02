package com.opcoach.genmodeladdon.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.opcoach.genmodeladdon.core.GenerateDevStructure;
import com.opcoach.genmodeladdon.ui.dialog.DerivedSourceParametersDialog;

public class GeneratedDerivedSourceFolder extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell parentShell = HandlerUtil.getActiveShell(event);
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof IStructuredSelection)
		{
			Object selected = ((IStructuredSelection) sel).getFirstElement();
			if (selected instanceof GenModel)
			{
				GenModel gm = (GenModel) selected;
				DerivedSourceParametersDialog dial = new DerivedSourceParametersDialog(parentShell);
				dial.setGenModel(gm);
				
				int status = dial.open();
				if (status == Dialog.OK)
				{
					String ip = dial.getDevInterfacePattern();
					String cp = dial.getDevClassPattern();
					
				  GenerateDevStructure gds = new GenerateDevStructure(gm, cp, ip);
				  gds.generateDevStructure();
				}
			}
		}
		
		return null;
	}

}
