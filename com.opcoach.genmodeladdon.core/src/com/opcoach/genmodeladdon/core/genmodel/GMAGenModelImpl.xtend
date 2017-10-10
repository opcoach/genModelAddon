package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelImpl
import org.eclipse.emf.codegen.util.ImportManager

class GMAGenModelImpl extends GenModelImpl {

	GMATransform gmaTransform = null

	ImportManager delegatedImportManager

	def getGMATransform() {
		if (gmaTransform === null)
			gmaTransform = new GMATransform(this)

		gmaTransform.init()

		return gmaTransform

	}


	override setImportManager(ImportManager im) {
		val dim = new GMAImportManager(im, this, "")
		super.setImportManager(dim)
	}

	override getImportManager() {
		if (delegatedImportManager == null) {
			delegatedImportManager = new GMAImportManager(super.getImportManager(), this, "")
		}
		return delegatedImportManager
	}

}
