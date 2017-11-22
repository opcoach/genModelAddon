package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelFactoryImpl

/** This factory overrides the default GenModel Factory, but it is must be setAvailable to use it
 * else, it will create the default GenModel Objects.
 */
class GMAFactoryImpl extends GenModelFactoryImpl {

	static boolean isAvailable = false

	def static setAvailable(boolean avail) {
		isAvailable = avail
	}

	override GenFeature createGenFeature() {
		if(isAvailable) new GMAGenFeatureImpl else super.createGenFeature
	}

	override GenClass createGenClass() {
		if(isAvailable) new GMAGenClassImpl else super.createGenClass
	}

	override GenModel createGenModel() {
		if(isAvailable) new GMAGenModelImpl else super.createGenModel
	}

	override createGenOperation() {
		if(isAvailable) new GMAGenOperationImpl else super.createGenOperation
	}

	override createGenPackage() {
		if(isAvailable) new GMAGenPackageImpl else super.createGenPackage
	}

	override createGenParameter() {
		if(isAvailable) new GMAGenParameterImpl else super.createGenParameter
	}

	override createGenTypeParameter() {
		if(isAvailable) new GMAGenTypeParameterImpl else super.createGenTypeParameter
	}

}
