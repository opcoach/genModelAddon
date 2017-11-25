package com.opcoach.genmodeladdon.core

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Path
import org.eclipse.core.runtime.Platform
import org.eclipse.core.runtime.QualifiedName
import org.eclipse.core.runtime.Status
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClassifier
import org.osgi.framework.FrameworkUtil

/** A class to provide some generation common methods */
class GenerateCommon implements GMAConstants {

	/** Extract the project name from the genmodel resource */
	def static String getProjectName(GenModel gm) {
		return getProjectNameFromURI(gm.eResource().getURI())
	}

	def static String getProjectNameFromURI(URI genModelUri) {
		// Project name is always the segment after the last segment of workspace root. 
		val rootUri = URI.createURI(ResourcesPlugin.workspace.root.locationURI.toString)
		val lastSegOfRootUri = rootUri.lastSegment
		val genModelUriStr = genModelUri.toString

		if (genModelUriStr.startsWith("platform:/resource/")) {
			// For this URI, project name is just after resource/
			val s = genModelUriStr.replace("platform:/resource/", "")
			val lastSlash = s.indexOf("/")
			return s.substring(0, lastSlash)

		}
		// Search for this segment in the genModelUri
		val segments = genModelUri.segmentsList
		val lastIndex = segments.lastIndexOf(lastSegOfRootUri)
		return segments.get(lastIndex + 1)

	}

	/** Find the project from a genmodel */
	def static IProject getProject(GenModel gm) {
		val projectName = getProjectName(gm)
		ResourcesPlugin.getWorkspace().getRoot().getProject(projectName)
	}

	/** Find the IFile from a genmodel */
	def static IFile getModelFile(GenModel gm) {

		if (gm.eResource !== null) {
			val genModelUri = gm.eResource.URI
			val p = new Path(genModelUri.toString.replaceFirst("platform:/resource", ""))
			val ws = ResourcesPlugin.workspace.root
			return ws.getFile(p)
		}
		return null
	}

	def static String getProperty(IFile f, QualifiedName qn) {
		var String result = null
		try {
			result = f.getPersistentProperty(qn)
		} catch (Exception e) {
			// There is no property file... no value.
		}
		return result
	}

	def static setProperty(IFile f, QualifiedName qn, String value) {
		try {
			f.setPersistentProperty(qn, value)
		} catch (Exception e) {
			val bndl = FrameworkUtil.getBundle(GenerateCommon)
			val logger = Platform.getLog(bndl);
			logger.log(new Status(IStatus.WARNING, PLUGIN_ID, "Unable to store the property : " + qn, e))
		}
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
			modelDir = uri.substring(pathPos, lastSlashPos)

		// Path is between projectName and model Name ! 
		return modelDir
	}
	
	
	def static isMapType(EClassifier c) {
		// See :  GenBaseImpl::isJavaUtilMapEntry(String name)
		val name = c.instanceClassName
		return "java.util.Map.Entry".equals(name) || "java.util.Map$Entry".equals(name)
	}
	
	def static isMapType(GenClass c) {
		// See :  GenBaseImpl::isJavaUtilMapEntry(String name)
		isMapType(c.ecoreClass)
	}
}
