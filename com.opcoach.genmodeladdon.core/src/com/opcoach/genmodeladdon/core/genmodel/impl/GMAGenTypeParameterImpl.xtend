package com.opcoach.genmodeladdon.core.genmodel.impl

import com.opcoach.genmodeladdon.core.genmodel.GMATransform
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenTypeParameterImpl

class GMAGenTypeParameterImpl extends GenTypeParameterImpl {
	
	override getQualifiedModelInfo() {
		GMATransform.replaceDevName(this,super.getQualifiedModelInfo())
	}
	
}