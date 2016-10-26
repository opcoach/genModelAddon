/**
 */
package com.opcoach.project.impl;

import com.opcoach.project.ProjectPackage;
import com.opcoach.project.MProjectPackage;
import com.opcoach.project.Task;
import com.opcoach.project.TaskFolder;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Task Folder</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
 public class MTaskFolderImpl extends FolderImpl<Task> implements TaskFolder
{
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MTaskFolderImpl()
	{
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass()
	{
		return MProjectPackage.Literals.TASK_FOLDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * This is specialized for the more specific element type known in this context.
	 * @generated
	 */
	@Override
	public EList<Task> getContent()
	{
		if (content == null)
		{
			content = new EObjectContainmentEList<Task>(Task.class, this, ProjectPackage.TASK_FOLDER__CONTENT);
		}
		return content;
	}

} //MTaskFolderImpl
