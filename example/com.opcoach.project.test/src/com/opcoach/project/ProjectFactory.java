package com.opcoach.project;

import com.opcoach.project.impl.ProjectFactoryImpl;

/** This factory  overrides the generated factory and returns the new generated interfaces */
public interface ProjectFactory extends MProjectFactory 
{
	
	/** Specialize the eINSTANCE initialization with the new interface type 
	  * (overridden in the override_factory extension)
	*/
	ProjectFactory eINSTANCE = ProjectFactoryImpl.init();
				
	public Person createPerson();
	public Project createProject();
	public Task createTask();
	public Company createCompany();
	public <T> Folder<T> createFolder();
	public TaskFolder createTaskFolder();
}
