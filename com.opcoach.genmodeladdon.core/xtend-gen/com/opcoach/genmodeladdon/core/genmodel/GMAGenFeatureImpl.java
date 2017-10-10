package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenFeatureImpl;

@SuppressWarnings("all")
public class GMAGenFeatureImpl extends GenFeatureImpl {
  @Override
  public String getImportedInternalType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getImportedInternalType(context));
  }
  
  @Override
  public String getImportedType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getImportedType(context));
  }
  
  @Override
  public String getListItemType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getListItemType(context));
  }
  
  @Override
  public String getObjectType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getObjectType(context));
  }
  
  @Override
  public String getNonEObjectInternalTypeCast(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getNonEObjectInternalTypeCast(context));
  }
  
  @Override
  public String getInternalTypeCast() {
    return GMATransform.replaceDevName(this, super.getInternalTypeCast());
  }
}
