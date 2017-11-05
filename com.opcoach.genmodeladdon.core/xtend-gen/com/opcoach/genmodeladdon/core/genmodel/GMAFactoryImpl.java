package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMAGenClassImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenFeatureImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModelImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenOperationImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenPackageImpl;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenOperation;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelFactoryImpl;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class GMAFactoryImpl extends GenModelFactoryImpl {
  public GMAFactoryImpl() {
    InputOutput.<String>println("Create the GMA Factory");
  }
  
  @Override
  public GenFeature createGenFeature() {
    return new GMAGenFeatureImpl();
  }
  
  @Override
  public GenClass createGenClass() {
    return new GMAGenClassImpl();
  }
  
  @Override
  public GenModel createGenModel() {
    final GMAGenModelImpl res = new GMAGenModelImpl();
    return res;
  }
  
  @Override
  public GenOperation createGenOperation() {
    return new GMAGenOperationImpl();
  }
  
  @Override
  public GenPackage createGenPackage() {
    return new GMAGenPackageImpl();
  }
}
