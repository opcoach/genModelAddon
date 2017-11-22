package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMAGenClassImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenFeatureImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModelImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenOperationImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenPackageImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenParameterImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenTypeParameterImpl;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
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
  public GenModel createGenModel() {
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
