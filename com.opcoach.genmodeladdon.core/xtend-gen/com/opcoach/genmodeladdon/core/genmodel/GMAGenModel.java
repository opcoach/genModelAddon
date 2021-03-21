package com.opcoach.genmodeladdon.core.genmodel;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;

/**
 * This interface manages the devClassPattern and devInterfacePattern
 */
@SuppressWarnings("all")
public interface GMAGenModel extends GenModel {
  void setDevClassPattern(final String cpattern);
  
  void setDevInterfacePattern(final String ipattern);
  
  void setSrcDir(final String dir);
  
  void setGenerateEMFCode(final boolean gen);
  
  String getDevClassPattern();
  
  String getDevInterfacePattern();
  
  String getSrcDir();
  
  boolean mustGenerateEMF();
  
  GMATransform getGMATransform();
}
