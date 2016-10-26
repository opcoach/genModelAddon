package com.opcoach.project.documentation;

import com.opcoach.project.documentation.impl.DocumentationFactoryImpl;

/** This factory  overrides the generated factory and returns the new generated interfaces */
public interface DocumentationFactory extends MDocumentationFactory 
{
	
	/** Specialize the eINSTANCE initialization with the new interface type 
	  * (overridden in the override_factory extension)
	*/
	DocumentationFactory eINSTANCE = DocumentationFactoryImpl.init();
				
	public DocumentationProject createDocumentationProject();
	public DocumentationTask createDocumentationTask();
}
