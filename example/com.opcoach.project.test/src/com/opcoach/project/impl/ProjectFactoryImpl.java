package com.opcoach.project.impl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.opcoach.project.Person;
import com.opcoach.project.Project;
import com.opcoach.project.Task;
import com.opcoach.project.Company;
import com.opcoach.project.Folder;
import com.opcoach.project.TaskFolder;
import com.opcoach.project.ProjectFactory;


// This factory  overrides the generated factory and returns the new generated interfaces
public class ProjectFactoryImpl extends MProjectFactoryImpl implements ProjectFactory
{
	
	public static ProjectFactory init() {
		
		try {
			Object fact = MProjectFactoryImpl.init();
			if ((fact != null) && (fact instanceof ProjectFactory))
					return (ProjectFactory) fact;
			}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ProjectFactoryImpl(); 
		 }
	
	public Person createPerson()
	{
		Person result = new PersonImpl();
		return result;
	}
	public Project createProject()
	{
		Project result = new ProjectImpl();
		return result;
	}
	public Task createTask()
	{
		Task result = new TaskImpl();
		return result;
	}
	public Company createCompany()
	{
		Company result = new CompanyImpl();
		return result;
	}
	public <T> Folder<T> createFolder()
	{
		Folder<T> result = new FolderImpl<T>();
		return result;
	}
	public TaskFolder createTaskFolder()
	{
		TaskFolder result = new TaskFolderImpl();
		return result;
	}
}
