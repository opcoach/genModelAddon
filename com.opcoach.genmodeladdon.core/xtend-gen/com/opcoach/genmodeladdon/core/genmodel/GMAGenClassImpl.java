package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenClassImpl;

@SuppressWarnings("all")
public class GMAGenClassImpl extends GenClassImpl {
  @Override
  public String getClassExtends() {
    String _xblockexpression = null;
    {
      final String ext = super.getClassExtends();
      _xblockexpression = GMATransform.replaceDevName(this, ext);
    }
    return _xblockexpression;
  }
  
  @Override
  public String getClassImplements() {
    return GMATransform.replaceDevName(this, super.getClassImplements());
  }
  
  @Override
  public String getInterfaceExtends() {
    return GMATransform.replaceDevName(this, super.getInterfaceExtends());
  }
  
  @Override
  public String getListConstructor(final GenFeature genFeature) {
    return GMATransform.replaceDevName(this, super.getListConstructor(genFeature));
  }
}
