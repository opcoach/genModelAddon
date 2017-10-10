package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenParameterImpl
import org.eclipse.emf.codegen.ecore.genmodel.GenClass

class GMAGenParameterImpl extends GenParameterImpl {
	
	override getObjectType(GenClass context) {
		GMATransform.replaceDevName(this,super.getObjectType(context))
	}
	
	
}