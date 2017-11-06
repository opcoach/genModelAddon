package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.impl.GenTypeParameterImpl

class GMAGenTypeParameterImpl extends GenTypeParameterImpl {
	
	
	override getQualifiedModelInfo() {
		GMATransform.replaceDevName(this,super.getQualifiedModelInfo())
	}
	
}