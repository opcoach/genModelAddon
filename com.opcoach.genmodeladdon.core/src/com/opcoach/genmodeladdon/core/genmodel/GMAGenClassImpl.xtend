package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenClassImpl
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature

class GMAGenClassImpl extends GenClassImpl {

	override getClassExtends() {
		val ext = super.classExtends
		GMATransform.replaceDevName(this, ext)
	}

	override getClassImplements() {
		GMATransform.replaceDevName(this, super.classImplements)
	}
	
	override getClassTypeArguments() {
		GMATransform.replaceDevName(this, super.getClassTypeArguments())
	}
	
	override getTypeParameters() {
		GMATransform.replaceDevName(this, super.getTypeParameters())
	}

	override getInterfaceExtends() {
		GMATransform.replaceDevName(this, super.interfaceExtends)
	}

	override getListConstructor(GenFeature genFeature) {
		GMATransform.replaceDevName(this, super.getListConstructor(genFeature))

	}
	
	
	
	

}
