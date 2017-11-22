package com.opcoach.genmodeladdon.core;

import org.eclipse.core.runtime.QualifiedName;

@SuppressWarnings("all")
public interface GMAConstants {
  public final static String PLUGIN_ID = "com.opcoach.genmodeladdon.core";
  
  public final static QualifiedName PROP_INTERFACE_PATTERN = new QualifiedName(GMAConstants.PLUGIN_ID, "interfacePattern");
  
  public final static QualifiedName PROP_CLASS_PATTERN = new QualifiedName(GMAConstants.PLUGIN_ID, "classPattern");
  
  public final static QualifiedName PROP_SRCDIR = new QualifiedName(GMAConstants.PLUGIN_ID, "srcDir");
  
  public final static QualifiedName PROP_GENEMFCODE = new QualifiedName(GMAConstants.PLUGIN_ID, "generateEMFCodeAfter");
  
  public final static QualifiedName PROP_GMA = new QualifiedName(GMAConstants.PLUGIN_ID, "genModelAddon");
  
  public final static String ADVISED_GEN_INTERFACE_PATTERN = "M{0}";
  
  public final static String ADVISED_GEN_CLASS_IMPL_PATTERN = "M{0}Impl";
  
  public final static String ADVISED_DEV_INTERFACE_PATTERN = "{0}";
  
  public final static String ADVISED_DEV_CLASS_IMPL_PATTERN = "{0}Impl";
  
  public final static String ADVISED_GEN_SRC_DIR = "src-gen";
  
  public final static String DEFAULT_SRC_DEV = "src";
  
  public final static String DEFAULT_DEV_INTERFACE_PATTERN = "{0}";
  
  public final static String DEFAULT_DEV_CLASS_IMPL_PATTERN = "{0}Impl";
}
