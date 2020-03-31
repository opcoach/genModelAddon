package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;

/**
 * This interface manages the devClassPattern and devInterfacePattern
 */
@SuppressWarnings("all")
public interface GMAGenModel extends GenModel {
  void setDevClassPattern(final String cpattern);
  
  void setDevInterfacePattern(final String ipattern);
  
  String getDevClassPattern();
  
  String getDevInterfacePattern();
  
  GMATransform getGMATransform();
}
