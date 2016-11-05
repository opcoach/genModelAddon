/**
 */
package com.opcoach.project.util;

import com.opcoach.project.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.opcoach.project.MProjectPackage
 * @generated
 */
public class ProjectAdapterFactory extends AdapterFactoryImpl
{
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MProjectPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProjectAdapterFactory()
	{
		if (modelPackage == null)
		{
			modelPackage = MProjectPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object)
	{
		if (object == modelPackage)
		{
			return true;
		}
		if (object instanceof EObject)
		{
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProjectSwitch<Adapter> modelSwitch =
		new ProjectSwitch<Adapter>()
		{
			@Override
			public Adapter casePerson(MPerson object)
			{
				return createPersonAdapter();
			}
			@Override
			public Adapter caseProject(MProject object)
			{
				return createProjectAdapter();
			}
			@Override
			public Adapter caseTask(MTask object)
			{
				return createTaskAdapter();
			}
			@Override
			public Adapter caseCompany(MCompany object)
			{
				return createCompanyAdapter();
			}
			@Override
			public <T> Adapter caseFolder(MFolder<T> object)
			{
				return createFolderAdapter();
			}
			@Override
			public Adapter caseTaskFolder(MTaskFolder object)
			{
				return createTaskFolderAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object)
			{
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target)
	{
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link com.opcoach.project.MPerson <em>Person</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.opcoach.project.MPerson
	 * @generated
	 */
	public Adapter createPersonAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.opcoach.project.MProject <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.opcoach.project.MProject
	 * @generated
	 */
	public Adapter createProjectAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.opcoach.project.MTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.opcoach.project.MTask
	 * @generated
	 */
	public Adapter createTaskAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.opcoach.project.MCompany <em>Company</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.opcoach.project.MCompany
	 * @generated
	 */
	public Adapter createCompanyAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.opcoach.project.MFolder <em>Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.opcoach.project.MFolder
	 * @generated
	 */
	public Adapter createFolderAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.opcoach.project.MTaskFolder <em>Task Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.opcoach.project.MTaskFolder
	 * @generated
	 */
	public Adapter createTaskFolderAdapter()
	{
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter()
	{
		return null;
	}

} //ProjectAdapterFactory