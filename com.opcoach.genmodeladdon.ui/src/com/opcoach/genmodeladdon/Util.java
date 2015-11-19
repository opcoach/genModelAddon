package com.opcoach.genmodeladdon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class Util
{
	public static void saveGenModel(GenModel gm, Shell parentShell)
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
			Bundle bundle = FrameworkUtil.getBundle(Util.class);
			ILog logger = Platform.getLog(bundle);
			logger.log(new Status(IStatus.WARNING, bundle.getSymbolicName(),
					"Unable to save the genModel in : " + gm.eResource(), e));
		}
	}
}
