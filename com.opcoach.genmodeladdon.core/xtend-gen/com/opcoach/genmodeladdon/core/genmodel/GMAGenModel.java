package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;

/**
 * This interface manages the devClassPattern and devInterfacePattern
 */
@SuppressWarnings("all")
public interface GMAGenModel extends GenModel {
  public abstract void setDevClassPattern(final String cpattern);
  
  public abstract void setDevInterfacePattern(final String ipattern);
  
  public abstract String getDevClassPattern();
  
  public abstract String getDevInterfacePattern();
  
  public abstract GMATransform getGMATransform();
}
