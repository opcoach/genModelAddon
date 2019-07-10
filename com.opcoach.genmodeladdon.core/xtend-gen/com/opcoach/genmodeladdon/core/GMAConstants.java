package com.opcoach.genmodeladdon.core;

import org.eclipse.core.runtime.QualifiedName;

@SuppressWarnings("all")
public interface GMAConstants {
  public static final String PLUGIN_ID = "com.opcoach.genmodeladdon.core";
  
  public static final QualifiedName PROP_INTERFACE_PATTERN = new QualifiedName(GMAConstants.PLUGIN_ID, "interfacePattern");
  
  public static final QualifiedName PROP_CLASS_PATTERN = new QualifiedName(GMAConstants.PLUGIN_ID, "classPattern");
  
  public static final QualifiedName PROP_SRCDIR = new QualifiedName(GMAConstants.PLUGIN_ID, "srcDir");
  
  public static final String PARAM_DEBUG_MODE = "-gmaDebug";
  
  public static final QualifiedName PROP_GENEMFCODE = new QualifiedName(GMAConstants.PLUGIN_ID, "generateEMFCodeAfter");
  
  public static final QualifiedName PROP_GMA = new QualifiedName(GMAConstants.PLUGIN_ID, "genModelAddon");
  
  public static final String ADVISED_GEN_INTERFACE_PATTERN = "M{0}";
  
  public static final String ADVISED_GEN_CLASS_IMPL_PATTERN = "M{0}Impl";
  
  public static final String ADVISED_DEV_INTERFACE_PATTERN = "{0}";
  
  public static final String ADVISED_DEV_CLASS_IMPL_PATTERN = "{0}Impl";
  
  public static final String ADVISED_GEN_SRC_DIR = "src-gen";
  
  public static final String DEFAULT_SRC_DEV = "src";
  
  public static final String DEFAULT_GEN_INTERFACE_PATTERN = "{0}";
  
  public static final String DEFAULT_GEN_CLASS_IMPL_PATTERN = "{0}Impl";
  
  public static final String DEFAULT_SRC_INTERFACE_PATTERN = "{0}Dev";
  
  public static final String DEFAULT_SRC_CLASS_IMPL_PATTERN = "{0}DevImpl";
}
