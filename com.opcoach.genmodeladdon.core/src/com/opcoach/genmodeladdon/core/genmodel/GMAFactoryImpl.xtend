package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelFactoryImpl

class GMAFactoryImpl extends GenModelFactoryImpl {
	new() {
		println("Create the GMA Factory")
	}

	override GenFeature createGenFeature() {
		// GenFeatureImpl genFeature = ;
		new GMAGenFeatureImpl
	}

	override GenClass createGenClass() {
		new GMAGenClassImpl
	}

	override GenModel createGenModel() {

		val res = new GMAGenModelImpl
		return res
	}

	override createGenOperation() {
		new GMAGenOperationImpl
	}

	override createGenPackage() {
		new GMAGenPackageImpl
	}
	
	override createGenParameter() {
		new GMAGenParameterImpl
	}

	override createGenTypeParameter() {
		new GMAGenTypeParameterImpl
	}
	
}
