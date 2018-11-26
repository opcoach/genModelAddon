package com.opcoach.genmodeladdon.core.genmodel.impl

import com.opcoach.genmodeladdon.core.genmodel.GMATransform
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenParameterImpl

class GMAGenParameterImpl extends GenParameterImpl {
	
	override getType(GenClass context) {
		GMATransform.replaceDevName(this,super.getType(context))
	}
	
	override getObjectType(GenClass context) {
		GMATransform.replaceDevName(this,super.getObjectType(context))
	}
	
	override getNonEObjectInternalTypeCast(GenClass context) {
		GMATransform.replaceDevName(this,super.getNonEObjectInternalTypeCast(context))
	}
	
	override getArrayItemType(GenClass context) {
		GMATransform.replaceDevName(this,super.getArrayItemType(context))
	}
	
	override getImportedType(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedType(context))
	}
	
	override getImportedInternalType(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedInternalType(context))
	}
	
	override getImportedMapKeyType(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedMapKeyType(context))
	}
	
	override getImportedMapValueType(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedMapValueType(context))
	}
	
	override getImportedMapTemplateArguments(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedMapTemplateArguments(context))
	}
	
	override getListItemType(GenClass context) {
		GMATransform.replaceDevName(this,super.getListItemType(context))
	}
	
	override getListTemplateArguments(GenClass context) {
		GMATransform.replaceDevName(this,super.getListTemplateArguments(context))
	}
	
	override getQualifiedListItemType(GenClass context) {
		GMATransform.replaceDevName(this,super.getQualifiedListItemType(context))
	}
	
	override getQualifiedObjectType(GenClass context) {
		GMATransform.replaceDevName(this,super.getQualifiedObjectType(context))
	}
	
}