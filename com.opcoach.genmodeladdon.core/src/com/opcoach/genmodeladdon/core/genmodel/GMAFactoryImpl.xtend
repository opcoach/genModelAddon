package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelFactoryImpl

class GMAFactoryImpl extends GenModelFactoryImpl {
   new()
	{
		println("Create the GMA Factory")
	}
	
	override GenFeature createGenFeature()
	{
		println("Create a GMAGenFeatureImpl ")

	   // GenFeatureImpl genFeature = ;

	    new GMAGenFeatureImpl
	}
	
	override GenClass createGenClass()
	{
		println("Create a GMAGenClassIMpl ")
		 new GMAGenClassImpl
	}
	
	override GenModel createGenModel()
	{
		println("Create a GMAGenModelImpl ")

		val res = new GMAGenModelImpl
		return res
	}
	
	override createGenOperation() {
		println("Create a GMAGenOperationImpl ")
		new GMAGenOperationImpl
	}
	
}