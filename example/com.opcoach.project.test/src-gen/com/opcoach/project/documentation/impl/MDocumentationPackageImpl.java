/**
 */
package com.opcoach.project.documentation.impl;

import com.opcoach.project.MProjectPackage;

import com.opcoach.project.documentation.MDocumentationFactory;
import com.opcoach.project.documentation.MDocumentationPackage;
import com.opcoach.project.documentation.MDocumentationProject;
import com.opcoach.project.documentation.MDocumentationTask;

import com.opcoach.project.impl.MProjectPackageImpl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MDocumentationPackageImpl extends EPackageImpl implements MDocumentationPackage
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentationProjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentationTaskEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.opcoach.project.documentation.MDocumentationPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MDocumentationPackageImpl()
	{
		super(eNS_URI, MDocumentationFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MDocumentationPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MDocumentationPackage init()
	{
		if (isInited) return (MDocumentationPackage)EPackage.Registry.INSTANCE.getEPackage(MDocumentationPackage.eNS_URI);

		// Obtain or create and register package
		MDocumentationPackageImpl theDocumentationPackage = (MDocumentationPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MDocumentationPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MDocumentationPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		MProjectPackageImpl theProjectPackage = (MProjectPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MProjectPackage.eNS_URI) instanceof MProjectPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MProjectPackage.eNS_URI) : MProjectPackage.eINSTANCE);

		// Create package meta-data objects
		theDocumentationPackage.createPackageContents();
		theProjectPackage.createPackageContents();

		// Initialize created meta-data
		theDocumentationPackage.initializePackageContents();
		theProjectPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDocumentationPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MDocumentationPackage.eNS_URI, theDocumentationPackage);
		return theDocumentationPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDocumentationProject()
	{
		return documentationProjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDocumentationTask()
	{
		return documentationTaskEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MDocumentationFactory getDocumentationFactory()
	{
		return (MDocumentationFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents()
	{
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		documentationProjectEClass = createEClass(DOCUMENTATION_PROJECT);

		documentationTaskEClass = createEClass(DOCUMENTATION_TASK);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents()
	{
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MProjectPackage theProjectPackage = (MProjectPackage)EPackage.Registry.INSTANCE.getEPackage(MProjectPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		documentationProjectEClass.getESuperTypes().add(theProjectPackage.getProject());
		documentationTaskEClass.getESuperTypes().add(theProjectPackage.getTask());

		// Initialize classes, features, and operations; add parameters
		initEClass(documentationProjectEClass, MDocumentationProject.class, "DocumentationProject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(documentationTaskEClass, MDocumentationTask.class, "DocumentationTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
	}

} //MDocumentationPackageImpl
