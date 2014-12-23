package com.opcoach.genmodeladdon.core

import java.io.FileWriter
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage

class GenerateDevStructure {

	String classPattern
	String interfacePattern
	GenModel genModel

	/** Build the generator with 2 parameters
	 * @param cpattern : the class name pattern used for generation ({0}Impl for instance)
	 * @param ipattern : the interface name pattern used for generation ({0} for instance)
	 */
	new(GenModel gm, String cPattern, String iPattern) {
		genModel = gm
		classPattern = cPattern
		interfacePattern = iPattern
	}

	new(GenModel gm) {
		this(gm, "{0}ExtImpl", "{0}Ext")
	}

	def generateDevStructure() {
		for (p : genModel.genPackages) {
			generateDevStructure(p)
		}
	}

	def generateDevStructure(GenPackage gp) {
		for (c : gp.genClasses) {
			generateOverridenClass(c, "/tmp/test/")
			generateOverridenInterface(c, "/tmp/test/")
		}
	}

	def generateOverridenClass(GenClass gc, String path) {

		// Open the file and generate contents
		val fw = new FileWriter(path + gc.computeClassname + ".java")
		fw.write(gc.generateClassContent.toString)
		fw.flush
		fw.close
	}

	def generateOverridenInterface(GenClass gc, String path) {

		// Open the file and generate contents
		val fw = new FileWriter(path + gc.computeInterfaceName + ".java")
		fw.write(gc.generateInterfaceContent.toString)
		fw.flush
		fw.close
	}

	def generateClassContent(GenClass gc) '''
		package «gc.genPackage.computePackageNameForClasses»;
		
		// This class can override the generated class and will be instantiated by factory
		class «gc.computeClassname» extends «gc.computeGeneratedClassName()» implements «gc.computeInterfaceName»
		{
		
		}
	'''

	def generateInterfaceContent(GenClass gc) '''
		package «gc.genPackage.computePackageNameForInterfaces»;
		
		// This interface can override the generated interface and will be returned by factory
		public interface «gc.computeInterfaceName» extends «gc.computeGeneratedInterfaceName()»
		{
		
		}
	'''

	def generateFactoryContent(GenClass gc) '''
		package «gc.genPackage.computePackageNameForClasses»;
		
		// This class can override the generated class and will be instantiated by factory
		class «gc.computeClassname» extends «gc.computeGeneratedClassName()» implements «gc.computeInterfaceName»
		{
		
		}
	'''

	/** Compute the class name to be generated */
	def computeClassname(GenClass gc) {
		classPattern.replace("{0}", gc.ecoreClass.name)
	}

	/** Compute the interface name to be generated */
	def computeInterfaceName(GenClass gc) {
		interfacePattern.replace("{0}", gc.ecoreClass.name)
	}

	/** Compute the package name for class */
	def computePackageNameForClasses(GenPackage gp) {
		val basePackage = if(gp.basePackage == null) "" else gp.basePackage
		val packSuffix = if (gp.classPackageSuffix == null) "" else "."+gp.classPackageSuffix
		basePackage + "." + gp.packageName.toLowerCase + packSuffix
	}

	/** Compute the package name for interfaces */
	def computePackageNameForInterfaces(GenPackage gp) {
		val basePackage = if(gp.basePackage == null) "" else gp.basePackage +"."
		val intSuffix = if(gp.classPackageSuffix == null) "" else "."+gp.interfacePackageSuffix 

		basePackage + gp.packageName.toLowerCase + intSuffix
	}

	/** Compute the generated class name depending on classpattern. */
	def computeGeneratedClassName(GenClass c) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val classPattern = c.genPackage.genModel.classNamePattern

		if (classPattern != null)
			classPattern.replace("{0}", c.ecoreClass.name)
		else
			c.ecoreClass.name + "Impl"
	}

	/** Compute the generated interface name depending on interfacePattern. */
	def computeGeneratedInterfaceName(GenClass c) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val interfaceNamePattern = c.genPackage.genModel.interfaceNamePattern

		if (interfaceNamePattern != null)
			interfaceNamePattern.replace("{0}", c.ecoreClass.name)
		else
			c.ecoreClass.name
	}
}
