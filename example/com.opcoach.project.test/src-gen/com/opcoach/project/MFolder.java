/**
 */
package com.opcoach.project;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Folder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.opcoach.project.MFolder#getContent <em>Content</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.opcoach.project.MProjectPackage#getFolder()
 * @model
 * @generated
 */
public interface MFolder<T> extends EObject
{
	/**
	 * Returns the value of the '<em><b>Content</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content</em>' containment reference list.
	 * @see com.opcoach.project.MProjectPackage#getFolder_Content()
	 * @model kind="reference" containment="true"
	 * @generated
	 */
	EList<T> getContent();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	T getFirst();

} // MFolder
