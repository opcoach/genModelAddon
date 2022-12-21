package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.GenModel

/** This interface manages the devClassPattern and devInterfacePattern */
interface GMAGenModel extends GenModel {

	def void setDevClassPattern(String cpattern)

	def void setDevInterfacePattern(String ipattern)
	
	def void setSrcDir(String dir)
	
	def void setGenerateEMFCode(boolean gen)

	def void setGenerateOverridenImplAsXtend(boolean gen)

	def String getDevClassPattern()

	def String getDevInterfacePattern()
	
	def String getSrcDir()
	
	def boolean mustGenerateEMF()

	def boolean mustGenerateOverridenImplAsXtendCode()

	def GMATransform getGMATransform()

}
