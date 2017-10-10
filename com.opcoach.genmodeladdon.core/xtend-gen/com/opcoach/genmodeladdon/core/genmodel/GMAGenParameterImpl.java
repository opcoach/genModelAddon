package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenParameterImpl;

@SuppressWarnings("all")
public class GMAGenParameterImpl extends GenParameterImpl {
  @Override
  public String getObjectType(final GenClass context) {
    return GMATransform.replaceDevName(this, super.getObjectType(context));
  }
}
