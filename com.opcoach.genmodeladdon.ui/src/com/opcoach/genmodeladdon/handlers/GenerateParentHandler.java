 
package com.opcoach.genmodeladdon.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

public abstract class GenerateParentHandler {
	
	/** With compatibility layer, must manage also IStructuredSelection ! */
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection isel)
	{
		if (isel.getFirstElement() instanceof GenModel)
			execute(shell, (GenModel) isel.getFirstElement());
	}
	
	@CanExecute
	public boolean canExecuteWithISelection(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection isel)
	{
		return isel.getFirstElement() instanceof GenModel;
	}
	
	public boolean canExecuteWithObject(@Named(IServiceConstants.ACTIVE_SELECTION) GenModel gm)
	{
		return true;
	}

	abstract public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell,
			@Named(IServiceConstants.ACTIVE_SELECTION) GenModel gm);

	/** Extract the project name from the resource of genmodel */
	protected  String getProjectName(GenModel gm)
	{
		URI genModelUri = gm.eResource().getURI();
		String p = genModelUri.toString().replaceFirst("platform:/resource/", "");
		int pos = p.indexOf("/");
		return p.substring(0, pos);
	}
	

}