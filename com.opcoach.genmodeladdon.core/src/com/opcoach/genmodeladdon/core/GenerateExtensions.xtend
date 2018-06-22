package com.opcoach.genmodeladdon.core

import java.util.Map
import org.eclipse.core.resources.IProject
import org.eclipse.pde.core.plugin.IPluginElement
import org.eclipse.pde.core.plugin.IPluginExtension
import org.eclipse.pde.core.plugin.IPluginExtensionPoint
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.eclipse.pde.core.plugin.IPluginObject
import org.eclipse.pde.core.plugin.PluginRegistry
import org.eclipse.pde.internal.core.PDECore
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel
import org.eclipse.pde.internal.core.project.PDEProject

/**
 * This class updates extensions in the current project. 
 * Actually some of extension points must be updated if they already exists
 * and the factory_override extension must be added.
 * 
 * Namely : 
 *  - if there is already an emf.ecore.generated_package it must be updated with the new package class
 *  - there must be a new emf.ecore.factory_override
 * 
 * Some code is inspired from class PointSelectionPage in org.eclipse.pde.internal.ui.wizards.extension
 */
package class GenerateExtensions {

	// The 2 extension points to manage with their elements and attributes
	static val EMF_GENERATED_PACKAGE = "org.eclipse.emf.ecore.generated_package"
	static val FACTORY_OVERRIDE = "org.eclipse.emf.ecore.factory_override"
	static val PACKAGE_ELT = "package"
	static val FACTORY_ELT = "factory"
	static val URI_ATTR = "uri"
	static val CLASS_ATTR = "class"

	// The new plugin model that will be generated from current project
	WorkspaceBundlePluginModel fModel;
	
	// The input project that contains the extensions to be updated/added 
	IProject project

	new(IProject p) {

		// Constructor will remove the previous generated package or factory override extension
		// For this model URI
		
		project = p
		// This code comes from NewProjectCreationOperation...
		val pluginXml = PDEProject.getPluginXml(project)
		val manifest = PDEProject.getManifest(project)
		fModel = new WorkspaceBundlePluginModel(manifest, pluginXml)

		// Must copy the existing extensions in the new created WorkspaceBundelPluginModel
		// But these extensions can be found using the workspace models
		// Search for the sourceModel from the project name
		var IPluginModelBase sourceModel
		for (m : PluginRegistry.getWorkspaceModels()) {
			if (m.bundleDescription !== null && project.name.equals(m.bundleDescription.symbolicName))
				sourceModel = m
		}

		// Copy current extension points in the new plugin model.
		for (IPluginExtensionPoint ept : sourceModel.extensions.extensionPoints) 
			fModel.pluginBase.add(ept.copyExtensionPoint)

		// Copy current extensions from the sourceModel into new plugin model
		for (IPluginExtension e : sourceModel.extensions.extensions) {
			fModel.pluginBase.add(e.copyExtension)
		}

		// Must inform the modelManager that project should be updated (don't remove this line)
		PDECore.^default.modelManager.bundleRootChanged(project)

	}

	// Used only for debug
	 def printExtension(IPluginExtension ext) {
		 println("<extension point=\"" + ext.point + "\">")
		for (elt : ext.children) {
			if (elt instanceof IPluginElement) {
				val ipe = elt as IPluginElement
				print("<" + ipe.name + " ")
				for (a : ipe.attributes)
					print(a.name + "='" + a.value + "' ")
				println("/>")
			}
		}
		println("</extension>")
	}

	// Copy an existing extension 
	private def copyExtension(IPluginExtension ext) {
		val clonedExt = fModel.factory.createExtension()
		clonedExt.point = ext.point
		if (ext.name.length > 0)
			clonedExt.name = ext.name
		clonedExt.id = ext.id
		for (elt : ext.children) {
			if (elt instanceof IPluginElement) {
				val ipe = elt as IPluginElement
				val IPluginElement clonedElt = ipe.copyExtensionElement(ext)
				clonedExt.add(clonedElt)
			}
		}

		return clonedExt

	}

	// Copy an existing extension  point
	private def copyExtensionPoint(IPluginExtensionPoint extPt) {
		val clonedExtPt = fModel.factory.createExtensionPoint()
		clonedExtPt.id = extPt.id
		clonedExtPt.name = extPt.name
		clonedExtPt.schema = extPt.schema

		return clonedExtPt

	}

	// Copy the extension element (recursive)
	private def IPluginElement copyExtensionElement(IPluginElement elt, IPluginObject parent) {
		val clonedElt = fModel.factory.createElement(parent)
		clonedElt.name = elt.name

		// Copy attributes
		for (a : elt.attributes)
			clonedElt.setAttribute(a.name, a.value)

		// Copy the content if any child element
		for (e : elt.children) {
			if (e instanceof IPluginElement) {
				val ipe = e as IPluginElement
				clonedElt.add(ipe.copyExtensionElement(clonedElt))
			}
		}

		return clonedElt
	}

	/** Check if factory_override or generated_package are correct in the plugin.xml
	 *  Must be checked in case the class names have changed between 2 generations
	 * factories : map<model uri, classForFactory>
	 * packages : map<model uri, classForPackage>
	 */
	def generateOrUpdateExtensions(Map<String, String> factories, Map<String, String> packages) {
		for (entry : factories.entrySet)
			generateOrUpdateExtension(FACTORY_OVERRIDE, entry.key, FACTORY_ELT, entry.value)

		fModel.save   // Fix #72
		
		for (entry : packages.entrySet)
			generateOrUpdateExtension(EMF_GENERATED_PACKAGE, entry.key, PACKAGE_ELT, entry.value)

		// Can store the new fModel to override the previous plugin.xml file.
		fModel.save
		
		/// Don't forget to force a refresh of extension/extension points...
		PDECore.^default.modelManager.bundleRootChanged(project)

	}

	// Generate the overridden extension if it does not exist yet 
	def private generateOrUpdateExtension(String extName, String modelURI, String nodeName, String classname) {

		// search for the extName extension if it exists
		// and for its nodeName with the same modelURI
		// If found, remove it to recreate it
		var IPluginExtension factoryExt
		do {
			factoryExt = findPluginElement(extName, modelURI, nodeName)

			if (factoryExt !== null) {
				// remove it from current plugin.xml to recreate it with correct values
				fModel.getPluginBase().remove(factoryExt)
			}

		} while (factoryExt !== null)

		// Can create a new updated Extension 
		val updatedExtension = fModel.factory.createExtension()
		updatedExtension.point = extName

		val factoryElement = fModel.factory.createElement(updatedExtension)
		factoryElement.name = nodeName
		factoryElement.setAttribute(URI_ATTR, modelURI)
		factoryElement.setAttribute(CLASS_ATTR, classname)

		updatedExtension.add(factoryElement)
		fModel.getPluginBase().add(updatedExtension)

	}

	/**
	 * Search for a plugin element 'factory' for factoryoverride extension
	 * or 'package' for the emf_generatedPackage extension
	 */
	def private findPluginElement(String extPoint, String modelURI, String nodeName) {

		for (e : fModel.getPluginBase().extensions) {
			if (e.point.equals(extPoint)) {
				// Check if there is already an element with this modelUri attribute...
				for (elt : e.children) {
					if (elt instanceof IPluginElement) {
						val ipe = elt as IPluginElement
						if (ipe.name.equals(nodeName) && modelURI.equals(ipe.getAttribute(URI_ATTR).value))
							return e

					}
				}
			}
		}
		return null
	}

}
