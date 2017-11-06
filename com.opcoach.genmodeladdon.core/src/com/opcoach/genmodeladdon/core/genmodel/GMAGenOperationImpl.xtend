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
	
	override getType(GenClass context) {
		GMATransform.replaceDevName(this,super.getType(context))
	}
	
	override getArrayItemType(GenClass context) {
		GMATransform.replaceDevName(this,super.getArrayItemType(context))
	}
	
	override getImportedInternalType(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedInternalType(context))
	}
	
	override getImportedMapKeyType(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedMapKeyType(context))
	}
	
	override getImportedMapTemplateArguments(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedMapTemplateArguments(context))
	}
	
	override getImportedMapValueType(GenClass context) {
		GMATransform.replaceDevName(this,super.getImportedMapValueType(context))
	}
	
	override getListItemType(GenClass context) {
		GMATransform.replaceDevName(this,super.getListItemType(context))
	}
	
	override getListTemplateArguments(GenClass context) {
		GMATransform.replaceDevName(this,super.getListTemplateArguments(context))
	}
	
	override getNonEObjectInternalTypeCast(GenClass context) {
		GMATransform.replaceDevName(this,super.getNonEObjectInternalTypeCast(context))
	}
	
	override getObjectType(GenClass context) {
		GMATransform.replaceDevName(this,super.getObjectType(context))
	}
	
	override getParametersArray(GenClass context) {
		GMATransform.replaceDevName(this,super.getParametersArray(context))
	}
	
	override getQualifiedListItemType(GenClass context) {
		GMATransform.replaceDevName(this,super.getQualifiedListItemType(context))
	}
	
	override getQualifiedObjectType(GenClass context) {
		GMATransform.replaceDevName(this,super.getQualifiedObjectType(context))
	}
	
	override getRawListItemType(GenClass context) {
		GMATransform.replaceDevName(this,super.getRawListItemType(context))
	}
	
	override getThrows(GenClass context) {
		GMATransform.replaceDevName(this,super.getThrows(context))
	}

	override getTypeParameters(GenClass context) {
		GMATransform.replaceDevName(this,super.getTypeParameters(context))
	}
	
}