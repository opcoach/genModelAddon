package com.opcoach.genmodeladdon.core.genmodel.impl;

import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenOperation;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.GenParameter;
import org.eclipse.emf.codegen.ecore.genmodel.GenTypeParameter;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelFactoryImpl;

/**
 * This factory overrides the default GenModel Factory, but it is must be setAvailable to use it
 * else, it will create the default GenModel Objects.
 */
@SuppressWarnings("all")
public class GMAFactoryImpl extends GenModelFactoryImpl {
  @Override
  public GenFeature createGenFeature() {
    return new GMAGenFeatureImpl();
  }

  @Override
  public GenClass createGenClass() {
    return new GMAGenClassImpl();
  }

  @Override
  public GMAGenModel createGenModel() {
    return new GMAGenModelImpl();
  }

  @Override
  public GenOperation createGenOperation() {
    return new GMAGenOperationImpl();
  }

  @Override
  public GenPackage createGenPackage() {
    return new GMAGenPackageImpl();
  }

  @Override
  public GenParameter createGenParameter() {
    return new GMAGenParameterImpl();
  }

  @Override
  public GenTypeParameter createGenTypeParameter() {
    return new GMAGenTypeParameterImpl();
  }
}
