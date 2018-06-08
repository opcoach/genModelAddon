package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.GenBase
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelFactoryImpl

/** This factory overrides the default GenModel Factory, but it is must be setAvailable to use it
 * else, it will create the default GenModel Objects.
 */
class GMAFactoryImpl extends GenModelFactoryImpl {

	// This method check if the current object is hosted in a GMA project (ie : that has the GMAProject property set to true)
	def static isInGMAProject(GenBase gm)
	{
		// Must check if the eResource of associated genmodel object contains the GMA project property
	//	val f = ResourcesPlugin.getResource()gm.eResource.URI
		
		 true
	}

	override GenFeature createGenFeature() {
		new GMAGenFeatureImpl
	}

	override GenClass createGenClass() {
		new GMAGenClassImpl
	}

	override GenModel createGenModel() {
		new GMAGenModelImpl
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
