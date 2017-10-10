package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenClassImpl
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature

class GMAGenClassImpl extends GenClassImpl {

	override getClassExtends() {
		GMATransform.replaceDevName(this, super.classExtends)
	}

	override getClassImplements() {
		GMATransform.replaceDevName(this, super.classImplements)
	}

	override getInterfaceExtends() {
		GMATransform.replaceDevName(this, super.interfaceExtends)
	}

	override getListConstructor(GenFeature genFeature) {
		GMATransform.replaceDevName(this, super.getListConstructor(genFeature))

	}
	

}
