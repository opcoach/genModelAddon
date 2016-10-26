/**
 */
package com.opcoach.project;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Company</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.opcoach.project.MCompany#getProjects <em>Projects</em>}</li>
 *   <li>{@link com.opcoach.project.MCompany#getEmployees <em>Employees</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.opcoach.project.MProjectPackage#getCompany()
 * @model
 * @generated
 */
public interface MCompany extends EObject
{
	/**
	 * Returns the value of the '<em><b>Projects</b></em>' containment reference list.

	 
	 * The list contents are of type {@link com.opcoach.project.MProject}. 
	
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Projects</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Projects</em>' containment reference list.
	 * @see com.opcoach.project.MProjectPackage#getCompany_Projects()
	 * @model containment="true"
	 * @generated
	 */
	EList<Project> getProjects();

	/**
	 * Returns the value of the '<em><b>Employees</b></em>' containment reference list.

	 
	 * The list contents are of type {@link com.opcoach.project.MPerson}. 
	
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Employees</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Employees</em>' containment reference list.
	 * @see com.opcoach.project.MProjectPackage#getCompany_Employees()
	 * @model containment="true"
	 * @generated
	 */
	EList<Person> getEmployees();

} // MCompany
