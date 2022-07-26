package com.opcoach.genmodeladdon.core.genmodel.impl;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenPackageImpl;

@SuppressWarnings("all")
public class GMAGenPackageImpl extends GenPackageImpl {
  @Override
  public String getImportedPackageClassName() {
    return GMATransform.replaceDevName(this, super.getImportedPackageClassName());
  }

  @Override
  public String getImportedPackageInterfaceName() {
    return GMATransform.replaceDevName(this, super.getImportedPackageInterfaceName());
  }
}
