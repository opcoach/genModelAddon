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
  private static boolean isAvailable = false;
  
  public static boolean setAvailable(final boolean avail) {
    return GMAFactoryImpl.isAvailable = avail;
  }
  
  @Override
  public GenFeature createGenFeature() {
    GenFeature _xifexpression = null;
    if (GMAFactoryImpl.isAvailable) {
      _xifexpression = new GMAGenFeatureImpl();
    } else {
      _xifexpression = super.createGenFeature();
    }
    return _xifexpression;
  }
  
  @Override
  public GenClass createGenClass() {
    GenClass _xifexpression = null;
    if (GMAFactoryImpl.isAvailable) {
      _xifexpression = new GMAGenClassImpl();
    } else {
      _xifexpression = super.createGenClass();
    }
    return _xifexpression;
  }
  
  @Override
  public GenModel createGenModel() {
    GenModel _xifexpression = null;
    if (GMAFactoryImpl.isAvailable) {
      _xifexpression = new GMAGenModelImpl();
    } else {
      _xifexpression = super.createGenModel();
    }
    return _xifexpression;
  }
  
  @Override
  public GenOperation createGenOperation() {
    GenOperation _xifexpression = null;
    if (GMAFactoryImpl.isAvailable) {
      _xifexpression = new GMAGenOperationImpl();
    } else {
      _xifexpression = super.createGenOperation();
    }
    return _xifexpression;
  }
  
  @Override
  public GenPackage createGenPackage() {
    GenPackage _xifexpression = null;
    if (GMAFactoryImpl.isAvailable) {
      _xifexpression = new GMAGenPackageImpl();
    } else {
      _xifexpression = super.createGenPackage();
    }
    return _xifexpression;
  }
  
  @Override
  public GenParameter createGenParameter() {
    GenParameter _xifexpression = null;
    if (GMAFactoryImpl.isAvailable) {
      _xifexpression = new GMAGenParameterImpl();
    } else {
      _xifexpression = super.createGenParameter();
    }
    return _xifexpression;
  }
  
  @Override
  public GenTypeParameter createGenTypeParameter() {
    GenTypeParameter _xifexpression = null;
    if (GMAFactoryImpl.isAvailable) {
      _xifexpression = new GMAGenTypeParameterImpl();
    } else {
      _xifexpression = super.createGenTypeParameter();
    }
    return _xifexpression;
  }
}
