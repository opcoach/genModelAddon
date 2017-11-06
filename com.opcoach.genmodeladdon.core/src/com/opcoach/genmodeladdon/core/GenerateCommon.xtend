package com.opcoach.genmodeladdon.core

import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.common.util.URI

/** A class to provide some generation common methods */
class GenerateCommon {

	/** Extract the project name from the genmodel resource */
	def static String getProjectName(GenModel gm) {
		return getProjectNameFromURI(gm.eResource().getURI())
	}

	def static String getProjectNameFromURI(URI genModelUri) {
		// Project name is always the segment after the last segment of workspace root. 
		val rootUri = URI.createURI(ResourcesPlugin.workspace.root.locationURI.toString)
		val lastSegOfRootUri = rootUri.lastSegment
		val genModelUriStr = genModelUri.toString
		
		if (genModelUriStr.startsWith("platform:/resource/"))
		{
			// For this URI, project name is just after resource/
			val s = genModelUriStr.replace("platform:/resource/", "")
			val lastSlash = s.indexOf("/")
			return s.substring(0, lastSlash)
			
		}
		// Search for this segment in the genModelUri
		val segments = genModelUri.segmentsList
		val lastIndex = segments.lastIndexOf(lastSegOfRootUri)
		return segments.get(lastIndex+1)
		
	}

	/** Find the project from a genmodel */
	def static IProject getProject(GenModel gm) {
		val projectName = getProjectName(gm)
		ResourcesPlugin.getWorkspace().getRoot().getProject(projectName)
	}

	/** Find the model name from the genmodel */
	def static getModelName(GenModel gm) {
		val uri = gm.eResource.URI
		val s = uri.toString
		var pos = s.lastIndexOf("/")
		var modelName = s.substring(pos + 1)
		pos = modelName.indexOf(".genmodel")
		modelName.substring(0, pos)
	}

	/** Find the model directory in its project */
	def static getModelPath(GenModel gm) {
		val uri = gm.eResource.URI
		val projectName = gm.getProject.name
		
		return getModelPathFromStringURI(projectName, uri.toString)

	}
	
		/** Find the model directory in its project */
	def static getModelPathFromStringURI(String projectName, String uri) {
		
		val pathPos = uri.lastIndexOf(projectName) + projectName.length + 1
		val lastSlashPos = uri.lastIndexOf("/")
		
		var modelDir = "." 
		if (pathPos < lastSlashPos) 
		    modelDir =  uri.substring(pathPos, lastSlashPos)

		// Path is between projectName and model Name ! 
		return modelDir
	}
}
