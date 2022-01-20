package com.opcoach.genmodeladdon.core;

@SuppressWarnings("all")
public interface GMAConstants {
  static final String PLUGIN_ID = "com.opcoach.genmodeladdon.core";
  
  static final String PROP_DEV_INTERFACE_PATTERN = "interfacePattern";
  
  static final String PROP_DEV_CLASS_PATTERN = "classPattern";
  
  static final String PROP_SRCDIR = "srcDir";
  
  static final String PARAM_DEBUG_MODE = "-gmaDebug";
  
  static final String PROP_GENEMFCODE = "generateEMFCodeAfter";
  
  static final String GMA_EXT = ".gma";
  
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
  
  static final String NATURE_ID = "com.opcoach.genmodeladdon.core.GMANature";
}
