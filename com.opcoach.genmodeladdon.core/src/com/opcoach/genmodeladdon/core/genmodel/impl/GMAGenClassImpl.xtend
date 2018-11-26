package com.opcoach.genmodeladdon.core.genmodel.impl

import com.opcoach.genmodeladdon.core.genmodel.GMATransform
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenClassImpl
import org.eclipse.emf.common.util.UniqueEList

class GMAGenClassImpl extends GenClassImpl {

	override getClassExtends() {
		val ext = super.classExtends
		GMATransform.replaceDevName(this, ext)
	}
	
	override getClassImplementsList() {
		var result = new UniqueEList<String>
		 
		for (String s : super.classImplementsList)
			result.add(GMATransform.replaceDevName(this, s))
						
	    result
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
