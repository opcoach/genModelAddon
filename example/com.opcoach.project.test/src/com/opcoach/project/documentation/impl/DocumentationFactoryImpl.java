package com.opcoach.project.documentation.impl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.opcoach.project.documentation.DocumentationProject;
import com.opcoach.project.documentation.DocumentationTask;
import com.opcoach.project.documentation.DocumentationFactory;


// This factory  overrides the generated factory and returns the new generated interfaces
public class DocumentationFactoryImpl extends MDocumentationFactoryImpl implements DocumentationFactory
{
	
	public static DocumentationFactory init() {
		
		try {
			Object fact = MDocumentationFactoryImpl.init();
			if ((fact != null) && (fact instanceof DocumentationFactory))
					return (DocumentationFactory) fact;
			}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DocumentationFactoryImpl(); 
		 }
	
	public DocumentationProject createDocumentationProject()
	{
		DocumentationProject result = new DocumentationProjectImpl();
		return result;
	}
	public DocumentationTask createDocumentationTask()
	{
		DocumentationTask result = new DocumentationTaskImpl();
		return result;
	}
}
