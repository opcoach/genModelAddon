package com.opcoach.genmodeladdon.core

import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.emf.codegen.ecore.genmodel.GenModel

/** A class to provide some generation common methods */
 class  GenerateCommon {
 	 	
	
	
	/** Extract the project name from the resource of genmodel */
	def static String getProjectName(GenModel gm)
	{
		val genModelUri = gm.eResource().getURI();
		val p = genModelUri.toString().replaceFirst("platform:/resource/", "");
		val pos = p.indexOf("/");
		return p.substring(0, pos);
	}
	
	/** Find the project from a genmodel */
	def static IProject getProject(GenModel gm)
	{
	    ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName(gm));
	}
}