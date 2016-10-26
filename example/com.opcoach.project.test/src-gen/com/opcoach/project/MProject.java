/**
 */
package com.opcoach.project;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.opcoach.project.MProject#getName <em>Name</em>}</li>
 *   <li>{@link com.opcoach.project.MProject#getTasks <em>Tasks</em>}</li>
 *   <li>{@link com.opcoach.project.MProject#getChief <em>Chief</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.opcoach.project.MProjectPackage#getProject()
 * @model
 * @generated
 */
public interface MProject extends EObject
{
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.opcoach.project.MProjectPackage#getProject_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.opcoach.project.MProject#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Tasks</b></em>' containment reference list.

	 
	 * The list contents are of type {@link com.opcoach.project.MTask}. 
	
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tasks</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tasks</em>' containment reference list.
	 * @see com.opcoach.project.MProjectPackage#getProject_Tasks()
	 * @model containment="true"
	 * @generated
	 */
	EList<Task> getTasks();

	/**
	 * Returns the value of the '<em><b>Chief</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Chief</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Chief</em>' reference.
	 * @see #setChief(MPerson)
	 * @see com.opcoach.project.MProjectPackage#getProject_Chief()
	 * @model
	 * @generated
	 */
	Person getChief();

	/**
	 * Sets the value of the '{@link com.opcoach.project.MProject#getChief <em>Chief</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Chief</em>' reference.
	 * @see #getChief()
	 * @generated
	 */
	void setChief(Person value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	Task findFirstTask(Person p);

} // MProject
