 
package com.opcoach.genmodeladdon.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import com.opcoach.genmodeladdon.core.GenerateCommon;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;

import jakarta.inject.Named;

public abstract class GenerateParentHandler {
	
	/** With compatibility layer, must manage also IStructuredSelection ! */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection isel)
	{
		if (isel.getFirstElement() instanceof GMAGenModel)
			execute(shell, (GMAGenModel) isel.getFirstElement());
	}
	
	@CanExecute
	public boolean canExecuteWithISelection(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection isel)
	{
		return isel.getFirstElement() instanceof GMAGenModel;
	}
	
	@CanExecute
	public boolean canExecuteWithObject(@Named(IServiceConstants.ACTIVE_SELECTION) GMAGenModel gm)
	{
		return true;
	}

	abstract public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Named(IServiceConstants.ACTIVE_SELECTION) GMAGenModel gm);

	/** Extract the project name from the resource of genmodel */
	protected  String getProjectName(GMAGenModel gm)
	{
		return GenerateCommon.getProjectName(gm);
	}
	

}