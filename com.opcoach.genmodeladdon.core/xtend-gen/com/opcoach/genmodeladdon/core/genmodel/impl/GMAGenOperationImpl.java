package com.opcoach.genmodeladdon.core.genmodel.impl;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenOperationImpl;

@SuppressWarnings("all")
public class GMAGenOperationImpl extends GenOperationImpl {
  @Override
  public String getImportedType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getImportedType(context));
  }
  
  @Override
  public String getParameters(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getParameters(context));
  }
  
  @Override
  public String getParameters(final boolean isImplementation, final GenClass context) {
    return GMATransform.replaceDevName(this, super.getParameters(isImplementation, context));
  }
  
  @Override
  public String getType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getType(context));
  }
  
  @Override
  public String getArrayItemType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getArrayItemType(context));
  }
  
  @Override
  public String getImportedInternalType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getImportedInternalType(context));
  }
  
  @Override
  public String getImportedMapKeyType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getImportedMapKeyType(context));
  }
  
  @Override
  public String getImportedMapTemplateArguments(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getImportedMapTemplateArguments(context));
  }
  
  @Override
  public String getImportedMapValueType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getImportedMapValueType(context));
  }
  
  @Override
  public String getListItemType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getListItemType(context));
  }
  
  @Override
  public String getListTemplateArguments(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getListTemplateArguments(context));
  }
  
  @Override
  public String getNonEObjectInternalTypeCast(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getNonEObjectInternalTypeCast(context));
  }
  
  @Override
  public String getObjectType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getObjectType(context));
  }
  
  @Override
  public String getParametersArray(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getParametersArray(context));
  }
  
  @Override
  public String getQualifiedListItemType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getQualifiedListItemType(context));
  }
  
  @Override
  public String getQualifiedObjectType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getQualifiedObjectType(context));
  }
  
  @Override
  public String getRawListItemType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getRawListItemType(context));
  }
  
  @Override
  public String getThrows(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getThrows(context));
  }
  
  @Override
  public String getTypeParameters(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getTypeParameters(context));
  }
}
