/**
 */
package com.opcoach.project.documentation.impl;

import com.opcoach.project.documentation.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MDocumentationFactoryImpl extends EFactoryImpl implements MDocumentationFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MDocumentationFactory init()
	{
		try
		{
			MDocumentationFactory theDocumentationFactory = (MDocumentationFactory)EPackage.Registry.INSTANCE.getEFactory(MDocumentationPackage.eNS_URI);
			if (theDocumentationFactory != null)
			{
				return theDocumentationFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MDocumentationFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDocumentationFactoryImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass)
	{
		switch (eClass.getClassifierID())
		{
			case MDocumentationPackage.DOCUMENTATION_PROJECT: return createDocumentationProject();
			case MDocumentationPackage.DOCUMENTATION_TASK: return createDocumentationTask();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDocumentationProject createDocumentationProject()
	{
		MDocumentationProjectImpl documentationProject = new MDocumentationProjectImpl();
		return documentationProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDocumentationTask createDocumentationTask()
	{
		MDocumentationTaskImpl documentationTask = new MDocumentationTaskImpl();
		return documentationTask;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDocumentationPackage getDocumentationPackage()
	{
		return (MDocumentationPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MDocumentationPackage getPackage()
	{
		return MDocumentationPackage.eINSTANCE;
	}

} //MDocumentationFactoryImpl
