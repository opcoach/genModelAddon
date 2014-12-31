package com.opcoach.genmodeladdon.core

import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.pde.core.plugin.IPluginElement
import org.eclipse.pde.core.plugin.IPluginExtension
import org.eclipse.pde.core.plugin.IPluginModelBase
import org.eclipse.pde.core.plugin.IPluginObject
import org.eclipse.pde.core.plugin.PluginRegistry
import org.eclipse.pde.internal.core.bundle.WorkspaceBundleFragmentModel
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel
import org.eclipse.pde.internal.core.ibundle.IBundlePluginModelBase
import org.eclipse.pde.internal.core.project.PDEProject

// From class PointSelectionPage
class GenerateFactoryOverrideExtension {

	IPluginModelBase fModel;
	String modelUri
	String factoryClassname

	new(String projectName, String pModelUri, String pFactoryClassname) {

		modelUri = pModelUri
		factoryClassname = pFactoryClassname

		val root = ResourcesPlugin.workspace.root
		val project = root.getProject(projectName)

		// This code comes from NewProjectCreationOPeration...
		val fragmentXml = PDEProject.getFragmentXml(project);
		val pluginXml = PDEProject.getPluginXml(project);
		val manifest = PDEProject.getManifest(project);
		if (fragmentXml.exists()) {
			fModel = new WorkspaceBundleFragmentModel(manifest, fragmentXml);
		} else {
			val registryModel = PluginRegistry.findModel(projectName)
			fModel = new WorkspaceBundlePluginModel(manifest, pluginXml);

			// The registry Model is not modifiable but contains some existing extensions. 
			// Must copy them in the new created fModel 
			for (e : registryModel.getPluginBase().extensions) {
				println("Extension in current workspace plugin " + e)
				val clonedExtens = e.copyExtension
				fModel.getPluginBase().add(clonedExtens);
			}

		}

	}

	def copyExtension(IPluginExtension ext) {
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

	def IPluginElement copyExtensionElement(IPluginElement elt, IPluginObject parent) {
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

	def finish() {
		val point = "org.eclipse.emf.ecore.factory_override"

		val ext = fModel.getFactory().createExtension()

		// Set the point attribute
		// The point value overrides an auto-generated value
		ext.setPoint(point)
		for (e : fModel.getPluginBase().extensions) {
			println("Extension in current plugin " + e)
		}

		fModel.getPluginBase().add(ext);
		val IPluginElement elt = fModel.factory.createElement(ext)
		elt.name = "factory"

		elt.setAttribute("class", "toto")
		elt.setAttribute("uri", "titi")

		ext.add(elt)

		// The fModel is actually a BundlePluginModelBase  having the save method
		val wbpm = fModel as IBundlePluginModelBase
		wbpm.save()

		return true;
	}

}
