package com.opcoach.genmodeladdon.core;

import org.eclipse.core.runtime.QualifiedName;

@SuppressWarnings("all")
public interface GMAConstants {
  static final String PLUGIN_ID = "com.opcoach.genmodeladdon.core";
  
  static final QualifiedName PROP_INTERFACE_PATTERN = new QualifiedName(GMAConstants.PLUGIN_ID, "interfacePattern");
  
  static final QualifiedName PROP_CLASS_PATTERN = new QualifiedName(GMAConstants.PLUGIN_ID, "classPattern");
  
  static final QualifiedName PROP_SRCDIR = new QualifiedName(GMAConstants.PLUGIN_ID, "srcDir");
  
  static final String PARAM_DEBUG_MODE = "-gmaDebug";
  
  static final QualifiedName PROP_GENEMFCODE = new QualifiedName(GMAConstants.PLUGIN_ID, "generateEMFCodeAfter");
  
  static final QualifiedName PROP_GMA = new QualifiedName(GMAConstants.PLUGIN_ID, "genModelAddon");
  
  static final String ADVISED_GEN_INTERFACE_PATTERN = "M{0}";
  
  static final String ADVISED_GEN_CLASS_IMPL_PATTERN = "M{0}Impl";
  
  static final String ADVISED_DEV_INTERFACE_PATTERN = "{0}";
  
  static final String ADVISED_DEV_CLASS_IMPL_PATTERN = "{0}Impl";
  
  static final String ADVISED_GEN_SRC_DIR = "src-gen";
  
  static final String DEFAULT_SRC_DEV = "src";
  
  static final String DEFAULT_GEN_INTERFACE_PATTERN = "{0}";
  
  static final String DEFAULT_GEN_CLASS_IMPL_PATTERN = "{0}Impl";
  
  static final String DEFAULT_SRC_INTERFACE_PATTERN = "{0}Dev";
  
  static final String DEFAULT_SRC_CLASS_IMPL_PATTERN = "{0}DevImpl";
}
