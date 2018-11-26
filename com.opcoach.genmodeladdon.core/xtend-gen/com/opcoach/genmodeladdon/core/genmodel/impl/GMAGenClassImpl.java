package com.opcoach.genmodeladdon.core.genmodel.impl;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import java.util.List;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenClassImpl;
import org.eclipse.emf.common.util.UniqueEList;

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
  public List<String> getClassImplementsList() {
    UniqueEList<String> _xblockexpression = null;
    {
      UniqueEList<String> result = new UniqueEList<String>();
      List<String> _classImplementsList = super.getClassImplementsList();
      for (final String s : _classImplementsList) {
        result.add(GMATransform.replaceDevName(this, s));
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  @Override
  public String getClassTypeArguments() {
    return GMATransform.replaceDevName(this, super.getClassTypeArguments());
  }
  
  @Override
  public String getTypeParameters() {
    return GMATransform.replaceDevName(this, super.getTypeParameters());
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
