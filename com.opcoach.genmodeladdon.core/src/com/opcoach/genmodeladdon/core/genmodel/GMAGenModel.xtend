package com.opcoach.genmodeladdon.core.genmodel

import org.eclipse.emf.codegen.ecore.genmodel.GenModel

/** This interface manages the devClassPattern and devInterfacePattern */
interface GMAGenModel extends GenModel {
	
	def void setDevClassPattern(String cpattern)
	def void setDevInterfacePattern(String ipattern)
	

	def String getDevClassPattern()
	def String getDevInterfacePattern()
	
	def GMATransform getGMATransform()
	
	
	
}