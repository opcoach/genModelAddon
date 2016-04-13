package com.opcoach.genmodeladdon.core

import java.io.File
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.emf.codegen.ecore.genmodel.GenModel

/** A class to provide some generation common methods */
class GenerateCommon {

	/** Extract the project name from the genmodel resource */
	def static String getProjectName(GenModel gm) {
		val genModelUri = gm.eResource().getURI();
		val gmUriStr = genModelUri.toString()
		if (gmUriStr.startsWith("platform:/resource/")) {
			val p = gmUriStr.replaceFirst("platform:/resource/", "");
			val pos = p.indexOf("/");
			return p.substring(0, pos);
		} else if (gmUriStr.startsWith("file:")) {
			val root = ResourcesPlugin.workspace.root
			val wsloc = root.locationURI
			val p = gmUriStr.replaceFirst(wsloc.toString + File.separator, "")
			val pos = p.indexOf("/");
			return p.substring(0, pos);
		}
		// Unknown ?? 
		return null
	}

	/** Find the project from a genmodel */
	def static IProject getProject(GenModel gm) {
		val projectName = getProjectName(gm)
		ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	}
	
	/** Find the model name from the genmodel */
	def static getModelName(GenModel gm)
	{
		val uri = gm.eResource.URI
		val s = uri.toString;
		var pos = s.lastIndexOf("/");
		var modelName = s.substring(pos + 1);
		pos = modelName.indexOf(".genmodel");
		modelName = modelName.substring(0, pos);
		return modelName
	}
	
		/** Find the model directory in its project */
	def static getModelDirectory(GenModel gm)
	{
		val uri = gm.eResource.URI
		val s = uri.toString;
		
		println("Model uri is : " + s)
		
		val projectName = gm.getProject.name
		val pathPos = s.lastIndexOf(projectName) + projectName.length + 1
		val lastSlashPos = s.lastIndexOf("/");
		val modelDir = s.substring(pathPos, lastSlashPos)
		
		// Path is between projectName and model Name ! 
		
		return modelDir
	}
}