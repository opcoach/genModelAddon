/**
 */
package com.opcoach.project.impl;

import com.opcoach.project.*;

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
public class MProjectFactoryImpl extends EFactoryImpl implements MProjectFactory
{
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MProjectFactory init()
	{
		try
		{
			MProjectFactory theProjectFactory = (MProjectFactory)EPackage.Registry.INSTANCE.getEFactory(MProjectPackage.eNS_URI);
			if (theProjectFactory != null)
			{
				return theProjectFactory;
			}
		}
		catch (Exception exception)
		{
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MProjectFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MProjectFactoryImpl()
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
			case MProjectPackage.PERSON: return createPerson();
			case MProjectPackage.PROJECT: return createProject();
			case MProjectPackage.TASK: return createTask();
			case MProjectPackage.COMPANY: return createCompany();
			case MProjectPackage.FOLDER: return createFolder();
			case MProjectPackage.TASK_FOLDER: return createTaskFolder();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPerson createPerson()
	{
		MPersonImpl person = new MPersonImpl();
		return person;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MProject createProject()
	{
		MProjectImpl project = new MProjectImpl();
		return project;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MTask createTask()
	{
		MTaskImpl task = new MTaskImpl();
		return task;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MCompany createCompany()
	{
		MCompanyImpl company = new MCompanyImpl();
		return company;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T> MFolder<T> createFolder()
	{
		MFolderImpl<T> folder = new MFolderImpl<T>();
		return folder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MTaskFolder createTaskFolder()
	{
		MTaskFolderImpl taskFolder = new MTaskFolderImpl();
		return taskFolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MProjectPackage getProjectPackage()
	{
		return (MProjectPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MProjectPackage getPackage()
	{
		return MProjectPackage.eINSTANCE;
	}

} //MProjectFactoryImpl
