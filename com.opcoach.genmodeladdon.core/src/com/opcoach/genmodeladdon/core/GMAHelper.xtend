package com.opcoach.genmodeladdon.core

import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.emf.codegen.ecore.genmodel.GenBase

class GMAHelper {
	static def boolean GMACompliant(GenBase context) {
		val genModel = context.getGenModel()
		val path = genModel.getModelProjectDirectory()
		val workspace = ResourcesPlugin.getWorkspace()
		val modelProject = workspace.getRoot().getProject(path)
		try {
			return modelProject.hasNature(GMAConstants.NATURE_ID)
		} catch (CoreException e) {
			return false
		}
	}
	
}