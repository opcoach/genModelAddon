 
package com.opcoach.genmodeladdon.handlers;

import java.util.Iterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.opcoach.genmodeladdon.nature.GMANature;

public class AddRemoveGMANature extends AbstractHandler {
	
	static Logger logger = LogManager.getLogger(AddRemoveGMANature.class);
			
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection isel = HandlerUtil.getCurrentSelection(event);
		if (isel instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) isel).iterator(); it.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
				}
				if (project != null) {
					try {
						GMANature.toggleNature(project);
					} catch (CoreException e) {
						logger.error(e.getMessage());
					}
				}
			}
		}

		return null;
	}
}