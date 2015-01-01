package com.opcoach.genmodeladdon.core

import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.pde.core.plugin.IPluginElement
import org.eclipse.pde.core.plugin.IPluginExtension
import org.eclipse.pde.core.plugin.IPluginObject
import org.eclipse.pde.core.plugin.PluginRegistry
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel
import org.eclipse.pde.internal.core.project.PDEProject

// From class PointSelectionPage
class GenerateFactoryOverrideExtension {

	WorkspaceBundlePluginModel fModel;

	new(String projectName) {

		val root = ResourcesPlugin.workspace.root
		val project = root.getProject(projectName)

		// This code comes from NewProjectCreationOPeration...
		val pluginXml = PDEProject.getPluginXml(project);
		val manifest = PDEProject.getManifest(project);

		val registryModel = PluginRegistry.findModel(projectName)
		fModel = new WorkspaceBundlePluginModel(manifest, pluginXml);

		// The registry Model is not modifiable but contains some existing extensions. 
		// Must copy them in the new created fModel 
		for (e : registryModel.getPluginBase().extensions) {
			val clonedExtens = e.copyExtension
			fModel.getPluginBase().add(clonedExtens);
		}

	}

	// Copy an existing extension 
	private def copyExtension(IPluginExtension ext) {
		val clonedExt = fModel.factory.createExtension()
		clonedExt.point = ext.point
		for (elt : ext.children) {
			if (elt instanceof IPluginElement) {
				val ipe = elt as IPluginElement
				val IPluginElement clonedElt = ipe.copyExtensionElement(ext)
				clonedExt.add(clonedElt)
			}
		}

		return clonedExt

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
				clonedElt.add(copyExtensionElement(ipe, clonedElt))
			}
		}

		return clonedElt
	}

	// Generate the override extension if it does not exist yet 
	def generateOverideExtension(String modelUri, String factoryClassname) {

		val point = "org.eclipse.emf.ecore.factory_override"

		for (e : fModel.getPluginBase().extensions) {
			if (e.point.equals(point)) {

				// Check if there is already an element with class and uri attribute...
				for (elt : e.children) {
					if (elt instanceof IPluginElement) {
						val ipe = elt as IPluginElement
						val classAtt = ipe.getAttribute("class").value
						val uriAtt = ipe.getAttribute("uri").value
						if (factoryClassname.equals(ipe.getAttribute("class").value) &&
							modelUri.equals(ipe.getAttribute("uri").value)) {

							// Extension already exists... Do nothing.. return
							return
						}
					}
				}
			}
		}

		// Extension not created yet... add it in the file...
		val ext = fModel.getFactory().createExtension()
		ext.point = point

		fModel.getPluginBase().add(ext);
		val IPluginElement elt = fModel.factory.createElement(ext)
		elt.name = "factory"
		elt.setAttribute("class", factoryClassname)
		elt.setAttribute("uri", modelUri)
		ext.add(elt)

		// The fModel is actually a BundlePluginModelBase having the save method
		fModel.save

	}

}
