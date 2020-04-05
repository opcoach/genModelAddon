package com.opcoach.genmodeladdon.core

interface GMAConstants {
	
	public static val String PLUGIN_ID = "com.opcoach.genmodeladdon.core"

	// Define the properties to be stored in the project for dev interface and class patterns
	public static val PROP_DEV_INTERFACE_PATTERN = "interfacePattern"
	public static val PROP_DEV_CLASS_PATTERN = "classPattern"
	public static val PROP_SRCDIR = "srcDir"
	
	// Debug parameter to set on application
	public static val PARAM_DEBUG_MODE = "-gmaDebug"
	
	// Store the EMF generate property after dev code generation
	public static val PROP_GENEMFCODE = "generateEMFCodeAfter"
		
	// Extension for the property file to store the data... 
	public static val GMA_EXT = ".gma"
	
	// Constants for the default name patterns and directories
	public static val String ADVISED_GEN_INTERFACE_PATTERN = "M{0}"
	public static val String ADVISED_GEN_CLASS_IMPL_PATTERN = "M{0}Impl"
	public static val String ADVISED_DEV_INTERFACE_PATTERN = "{0}"
	public static val String ADVISED_DEV_CLASS_IMPL_PATTERN = "{0}Impl"
	public static val String ADVISED_GEN_SRC_DIR = "src-gen"

	public static val String DEFAULT_SRC_DEV = "src"
	
	// Default values used by genmodel to generate class names
	public static val String DEFAULT_GEN_INTERFACE_PATTERN = "{0}"
	public static val String DEFAULT_GEN_CLASS_IMPL_PATTERN = "{0}Impl"
	public static val String DEFAULT_SRC_INTERFACE_PATTERN = "{0}Dev"
	public static val String DEFAULT_SRC_CLASS_IMPL_PATTERN = "{0}DevImpl"
	
	
}