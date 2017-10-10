package com.opcoach.genmodeladdon.core.genmodel;

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
}
