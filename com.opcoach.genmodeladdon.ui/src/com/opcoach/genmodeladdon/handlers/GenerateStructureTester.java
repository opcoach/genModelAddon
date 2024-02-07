
package com.opcoach.genmodeladdon.handlers;

import org.eclipse.e4.core.di.annotations.Evaluate;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.opcoach.genmodeladdon.core.GMAHelper;

import jakarta.inject.Named;

public class GenerateStructureTester
{
	@Evaluate
	public boolean evaluate(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection)
	{
		// Show Generate structure command only if project is compliant with GMA
		if (!selection.isEmpty())
		{
			Object first = selection.getFirstElement();
			if (first instanceof GenModel)
			{
				GenModel genModel = (GenModel) first;
				return GMAHelper.GMACompliant(genModel);
			}
		}
		return false;
	}
}
