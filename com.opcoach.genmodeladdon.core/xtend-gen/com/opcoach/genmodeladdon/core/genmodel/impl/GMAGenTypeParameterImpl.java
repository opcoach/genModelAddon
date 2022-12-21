package com.opcoach.genmodeladdon.core.genmodel.impl;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenTypeParameterImpl;

@SuppressWarnings("all")
public class GMAGenTypeParameterImpl extends GenTypeParameterImpl {
  @Override
  public String getQualifiedModelInfo() {
    return GMATransform.replaceDevName(this, super.getQualifiedModelInfo());
  }
}
