package com.opcoach.genmodeladdon.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.opcoach.genmodeladdon.core.GenerateDevStructure;

public class GeneratedDerivedSourceFolder extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof IStructuredSelection)
		{
			Object selected = ((IStructuredSelection) sel).getFirstElement();
			if (selected instanceof GenModel)
			{
				GenerateDevStructure gds = new GenerateDevStructure();
				gds.generateDevStructure((GenModel) selected);
			}
		}
		
		return null;
	}

}
