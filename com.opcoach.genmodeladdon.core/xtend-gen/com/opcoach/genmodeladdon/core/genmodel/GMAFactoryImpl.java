package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMAGenClassImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenFeatureImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModelImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenOperationImpl;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenOperation;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelFactoryImpl;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class GMAFactoryImpl extends GenModelFactoryImpl {
  public GMAFactoryImpl() {
    InputOutput.<String>println("Create the GMA Factory");
  }
  
  @Override
  public GenFeature createGenFeature() {
    GMAGenFeatureImpl _xblockexpression = null;
    {
      InputOutput.<String>println("Create a GMAGenFeatureImpl ");
      _xblockexpression = new GMAGenFeatureImpl();
    }
    return _xblockexpression;
  }
  
  @Override
  public GenClass createGenClass() {
    GMAGenClassImpl _xblockexpression = null;
    {
      InputOutput.<String>println("Create a GMAGenClassIMpl ");
      _xblockexpression = new GMAGenClassImpl();
    }
    return _xblockexpression;
  }
  
  @Override
  public GenModel createGenModel() {
    InputOutput.<String>println("Create a GMAGenModelImpl ");
    final GMAGenModelImpl res = new GMAGenModelImpl();
    return res;
  }
  
  @Override
  public GenOperation createGenOperation() {
    GMAGenOperationImpl _xblockexpression = null;
    {
      InputOutput.<String>println("Create a GMAGenOperationImpl ");
      _xblockexpression = new GMAGenOperationImpl();
    }
    return _xblockexpression;
  }
}
