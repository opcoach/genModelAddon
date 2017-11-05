package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenFeatureImpl

class GMAGenFeatureImpl extends GenFeatureImpl {
	
	override getImportedInternalType(GenClass context) {
		GMATransform.replaceDevName(this, super.getImportedInternalType(context))
	}
	
	override getImportedType(GenClass context) {
		GMATransform.replaceDevName(this, super.getImportedType(context))
	}
	
	override getListItemType(GenClass context) {
		GMATransform.replaceDevName(this, super.getListItemType(context))
	}
	
	override getObjectType(GenClass context) {
		GMATransform.replaceDevName(this,super.getObjectType(context))
	}
	
	override getNonEObjectInternalTypeCast(GenClass context) {
		GMATransform.replaceDevName(this,super.getNonEObjectInternalTypeCast(context))
	}
	
	override getInternalTypeCast() {
		GMATransform.replaceDevName(this,super.getInternalTypeCast())
	}
	
	override getType(GenClass context) {
		GMATransform.replaceDevName(this,super.getType(context))
	}
	
}