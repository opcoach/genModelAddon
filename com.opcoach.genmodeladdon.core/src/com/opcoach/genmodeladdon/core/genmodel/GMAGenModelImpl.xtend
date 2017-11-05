package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelImpl
import org.eclipse.emf.codegen.util.ImportManager

class GMAGenModelImpl extends GenModelImpl {

	GMATransform gmaTransform = null

	ImportManager delegatedImportManager

	def getGMATransform() {

		println("Enter in getGMATransform ")

		if (gmaTransform === null)
			gmaTransform = new GMATransform(this)

		gmaTransform.init()

		return gmaTransform

	}

	override setImportManager(ImportManager im) {
		if (delegatedImportManager === null) {
			if (im instanceof GMAImportManager)
				delegatedImportManager = im
			else
				delegatedImportManager = new GMAImportManager(im, this, "")
			// super.setImportManager(delegatedImportManager)
		} 
	}

	override getImportManager() {
		if (delegatedImportManager === null) {
			println("Create a GMAImportManager")
			delegatedImportManager = new GMAImportManager(super.getImportManager(), this, "")
		}
		return delegatedImportManager
	}

}
