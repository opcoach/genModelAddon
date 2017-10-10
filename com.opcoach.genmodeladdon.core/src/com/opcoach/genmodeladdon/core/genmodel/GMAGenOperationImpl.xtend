package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenOperationImpl
import org.eclipse.emf.codegen.ecore.genmodel.GenClass

class GMAGenOperationImpl extends GenOperationImpl {
	
	override getImportedType(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedType(context))
	}
	
	override getParameters(GenClass context) {
		GMATransform.replaceDevName(this,super.getParameters(context))
	}
	
	override getParameters(boolean isImplementation, GenClass context) {
		GMATransform.replaceDevName(this,super.getParameters(isImplementation, context))
	}
	
}