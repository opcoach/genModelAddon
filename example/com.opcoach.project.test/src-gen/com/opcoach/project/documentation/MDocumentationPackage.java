/**
 */
package com.opcoach.project.documentation;

import com.opcoach.project.MProjectPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.opcoach.project.documentation.MDocumentationFactory
 * @model kind="package"
 * @generated
 */
public interface MDocumentationPackage extends EPackage
{
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "documentation";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.opcoach.com/project/documentation/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "doc";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MDocumentationPackage eINSTANCE = com.opcoach.project.documentation.impl.MDocumentationPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.opcoach.project.documentation.impl.MDocumentationProjectImpl <em>Project</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.opcoach.project.documentation.impl.MDocumentationProjectImpl
	 * @see com.opcoach.project.documentation.impl.MDocumentationPackageImpl#getDocumentationProject()
	 * @generated
	 */
	int DOCUMENTATION_PROJECT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_PROJECT__NAME = MProjectPackage.PROJECT__NAME;

	/**
	 * The feature id for the '<em><b>Tasks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_PROJECT__TASKS = MProjectPackage.PROJECT__TASKS;

	/**
	 * The feature id for the '<em><b>Chief</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_PROJECT__CHIEF = MProjectPackage.PROJECT__CHIEF;

	/**
	 * The number of structural features of the '<em>Project</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_PROJECT_FEATURE_COUNT = MProjectPackage.PROJECT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Find First Task</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_PROJECT___FIND_FIRST_TASK__MPERSON = MProjectPackage.PROJECT___FIND_FIRST_TASK__MPERSON;

	/**
	 * The number of operations of the '<em>Project</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_PROJECT_OPERATION_COUNT = MProjectPackage.PROJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.opcoach.project.documentation.impl.MDocumentationTaskImpl <em>Task</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.opcoach.project.documentation.impl.MDocumentationTaskImpl
	 * @see com.opcoach.project.documentation.impl.MDocumentationPackageImpl#getDocumentationTask()
	 * @generated
	 */
	int DOCUMENTATION_TASK = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_TASK__NAME = MProjectPackage.TASK__NAME;

	/**
	 * The feature id for the '<em><b>Sub Tasks</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_TASK__SUB_TASKS = MProjectPackage.TASK__SUB_TASKS;

	/**
	 * The feature id for the '<em><b>Responsable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_TASK__RESPONSABLE = MProjectPackage.TASK__RESPONSABLE;

	/**
	 * The number of structural features of the '<em>Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_TASK_FEATURE_COUNT = MProjectPackage.TASK_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Task</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENTATION_TASK_OPERATION_COUNT = MProjectPackage.TASK_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link com.opcoach.project.documentation.MDocumentationProject <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Project</em>'.
	 * @see com.opcoach.project.documentation.MDocumentationProject
	 * @generated
	 */
	EClass getDocumentationProject();

	/**
	 * Returns the meta object for class '{@link com.opcoach.project.documentation.MDocumentationTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Task</em>'.
	 * @see com.opcoach.project.documentation.MDocumentationTask
	 * @generated
	 */
	EClass getDocumentationTask();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MDocumentationFactory getDocumentationFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals
	{
		/**
		 * The meta object literal for the '{@link com.opcoach.project.documentation.impl.MDocumentationProjectImpl <em>Project</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.opcoach.project.documentation.impl.MDocumentationProjectImpl
		 * @see com.opcoach.project.documentation.impl.MDocumentationPackageImpl#getDocumentationProject()
		 * @generated
		 */
		EClass DOCUMENTATION_PROJECT = eINSTANCE.getDocumentationProject();

		/**
		 * The meta object literal for the '{@link com.opcoach.project.documentation.impl.MDocumentationTaskImpl <em>Task</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.opcoach.project.documentation.impl.MDocumentationTaskImpl
		 * @see com.opcoach.project.documentation.impl.MDocumentationPackageImpl#getDocumentationTask()
		 * @generated
		 */
		EClass DOCUMENTATION_TASK = eINSTANCE.getDocumentationTask();

	}

} //MDocumentationPackage
