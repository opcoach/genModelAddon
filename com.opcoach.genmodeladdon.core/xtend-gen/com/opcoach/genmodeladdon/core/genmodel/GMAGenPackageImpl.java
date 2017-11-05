package com.opcoach.genmodeladdon.core.genmodel;

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
  
  @Override
  public String getFactoryClassName() {
    return GMATransform.replaceDevName(this, super.getFactoryClassName());
  }
  
  @Override
  public String getFactoryInstanceName() {
    return GMATransform.replaceDevName(this, super.getFactoryInstanceName());
  }
}
