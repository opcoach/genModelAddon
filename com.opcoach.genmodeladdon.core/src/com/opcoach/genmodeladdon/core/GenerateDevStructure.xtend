package com.opcoach.genmodeladdon.core

import java.io.FileWriter
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage

class GenerateDevStructure {

	def generateDevStructure(GenModel gm) {
		for (p : gm.genPackages) {
			generateDevStructure(p, gm.classNamePattern)
		}
	}

	def generateDevStructure(GenPackage gp, String classPattern) {
		for (c : gp.genClasses)
		{
			generateOverridenClass(c, classPattern, "/tmp/")
		}
	}
	
	def generateOverridenClass(GenClass gc, String classpattern, String path) {
		// Open the file and generate contents
		val fw = new FileWriter(path + gc.ecoreClass.name + ".java")
		fw.write(gc.generateClassContent(classpattern).toString)
		fw.flush
		fw.close
	}
	
	def generateClassContent(GenClass gc, String classpattern) '''
	  package xxxx;
	  class «gc.ecoreClass.name»Impl extends «gc.computeClassName(classpattern)»
	  {
	  	
	  }
	'''
	
	def computeClassName(GenClass c, String classPattern )
	{
		if (classPattern != null)
		classPattern.replace("{0}", c.ecoreClass.name)
		else
		  c.ecoreClass.name + "Impl"
	}
}
