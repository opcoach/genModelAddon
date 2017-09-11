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
			val lastSlash = s.indexOf("/");
			return s.substring(0, lastSlash);
			
		}
		// Search for this segment in the genModelUri
		val segments = genModelUri.segmentsList
		val lastIndex = segments.lastIndexOf(lastSegOfRootUri)
		return segments.get(lastIndex+1)
		
	/* 	val gmUriStr = genModelUri.toString()
		val s1 = genModelUri.toPlatformString(false)
		val s2 = genModelUri.toPlatformString(true)
		val s3 = genModelUri.toFileString
		val dev = genModelUri.device
		val devPath = genModelUri.devicePath
		val lastSeg = genModelUri.lastSegment
		val seg = genModelUri.resolve(rootUri)
		
		for (sg : genModelUri.segments)
		   println("Segment : " + sg)

		println("\n" + s1 + "\n" + s2 + "\n" + s3)

		if (genModelUri.isPlatformResource) {
			val p = gmUriStr.replaceFirst("platform:/resource/", "");

			val pos = p.indexOf("/");
			return p.substring(0, pos);
		} else if (genModelUri.isFile) {
			val root = ResourcesPlugin.workspace.root
			val wsloc = root.locationURI
			val p = gmUriStr.replaceFirst(wsloc.toString + File.separator, "")
			val pos = p.indexOf("/");
			return p.substring(0, pos);
		} else if (genModelUri.isHierarchical) {
			val root = ResourcesPlugin.workspace.root
			val wsloc = root.locationURI
			val p = gmUriStr.replaceFirst(wsloc.toString + File.separator, "")
			val pos = p.indexOf("/");
			return p.substring(0, pos);

		}
		// Unknown ?? 
		return null
		
		*/
	}

	/** Find the project from a genmodel */
	def static IProject getProject(GenModel gm) {
		val projectName = getProjectName(gm)
		ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	}

	/** Find the model name from the genmodel */
	def static getModelName(GenModel gm) {
		val uri = gm.eResource.URI
		val s = uri.toString;
		var pos = s.lastIndexOf("/");
		var modelName = s.substring(pos + 1);
		pos = modelName.indexOf(".genmodel");
		modelName = modelName.substring(0, pos);
		return modelName
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
		val lastSlashPos = uri.lastIndexOf("/");
		
		var modelDir = "." 
		if (pathPos < lastSlashPos) 
		    modelDir =  uri.substring(pathPos, lastSlashPos)

		// Path is between projectName and model Name ! 
		return modelDir
	}
}
