package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelImpl
import org.eclipse.emf.codegen.util.ImportManager
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage

class GMAGenModelImpl extends GenModelImpl {

	GMATransform gmaTransform = null

	def GMATransform getGMATransform() {

		if (gmaTransform === null) {
			gmaTransform = new GMATransform(this)
			gmaTransform.init()

			// Must also set it on all subGenModels and dependencies.. (code like super.setImportManager)
			// We also need to set it on any GenModels holding any used or static packages that may be refered to.
			//
			for (GenPackage genPackage : getUsedGenPackages()) {
				val gm = genPackage.getGenModel()
				if (gm instanceof GMAGenModelImpl)
					gm.setGMATransform(gmaTransform);
			}

			for (GenPackage genPackage : getStaticGenPackages()) {
				val gm = genPackage.getGenModel()
				if (gm instanceof GMAGenModelImpl)
					gm.setGMATransform(gmaTransform);
			}

			// And we need to set it on any cached GenModels holding the special Ecore and XML packages.
			//
			val ecore = getEcoreGenPackage();
			if (ecore !== null && ecore.getGenModel() instanceof GMAGenModelImpl) {
				val gm = ecore.getGenModel() as GMAGenModelImpl
				if (gm.GMATransform !== gmaTransform)
					gm.setGMATransform(gmaTransform);
			}

			val xmlType = getXMLTypeGenPackage();
			if (xmlType !== null && xmlType.getGenModel() instanceof GMAGenModelImpl) {
				val gm = xmlType.getGenModel() as GMAGenModelImpl
				if (gm.GMATransform !== gmaTransform)
					gm.setGMATransform(gmaTransform);
			}

			val xmlNamespace = getXMLNamespaceGenPackage();
			if (xmlNamespace !== null && xmlNamespace.getGenModel() instanceof GMAGenModelImpl) {
				val gm = xmlNamespace.getGenModel() as GMAGenModelImpl
				if (gm.GMATransform !== gmaTransform)
					gm.setGMATransform(gmaTransform);
			}

		}

		return gmaTransform

	}

	def setGMATransform(GMATransform gmat) {
		gmaTransform = gmat
	}

	override emitSortedImports() {
		super.emitSortedImports()
	}

	override setImportManager(ImportManager im) {
		if (im === null)
			return;

		if (getImportManager() !== im) {
			if (im instanceof GMAImportManager)
				super.setImportManager(im)
			else {
				val delegatedImportManager = new GMAImportManager(im, this, "")
				super.setImportManager(delegatedImportManager)
			}

		}

	}

}
