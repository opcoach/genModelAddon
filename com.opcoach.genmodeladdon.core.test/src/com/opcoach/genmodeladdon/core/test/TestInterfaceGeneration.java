package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.EClass;
import org.junit.Test;

import com.opcoach.genmodeladdon.core.GenerateCommon;

public class TestInterfaceGeneration extends GenModelAddonTestCase
{

	private String copyright = "OPCoach 2016";

	

	// ----------------------------------------------------------------------------------------------------------
	// ------------------- Test dev interfaces extends gen interfaces or parent
	// dev interface ------------------
	// ----------------------------------------------------------------------------------------------------------

	@Test
	public void interfaceProjectMustExtendsMProject()
	{
		assertFileContains("src/com/opcoach/project/Project.java", "interface Project extends MProject");
	}

	@Test
	public void interfaceTaskMustExtendsMTask()
	{
		assertFileContains("src/com/opcoach/project/Task.java", "interface Task extends MTask");
	}

	@Test
	public void interfaceCompanyMustExtendsMCompany()
	{
		assertFileContains("src/com/opcoach/project/Company.java", "interface Company extends MCompany");
	}

	@Test
	public void interfacePersonMustExtendsMPerson()
	{
		assertFileContains("src/com/opcoach/project/Person.java", "interface Person extends MPerson");
	}

	@Test
	public void interfaceMDocumentationProjectMustExtendsProject()
	{
		assertFileContains("src-gen/com/opcoach/project/documentation/MDocumentationProject.java",
				"interface MDocumentationProject extends Project");
	}

	@Test
	public void interfaceDocumentationProjectMustExtendsMDocumentationProject()
	{
		assertFileContains("src/com/opcoach/project/documentation/DocumentationProject.java",
				"interface DocumentationProject extends MDocumentationProject");
	}

	// ----------------------------------------------------------------------------------------------------------
	// ------------------- Test dev implementations extends gen implementations
	// or parent dev implementations --
	// ----------------------------------------------------------------------------------------------------------
	@Test
	public void implCompanyMustExtendMCompanyImplAndImplementsCompany()
	{

		assertFileContains("src/com/opcoach/project/impl/CompanyImpl.java",
				"class CompanyImpl extends MCompanyImpl implements Company");
	}

	@Test
	public void implTaskMustExtendMTaskImplAndImplementsTask()
	{

		assertFileContains("src/com/opcoach/project/impl/TaskImpl.java",
				"class TaskImpl extends MTaskImpl implements Task");
	}

	@Test
	public void implPersonMustExtendMPersonImplAndImplementsPerson()
	{

		assertFileContains("src/com/opcoach/project/impl/PersonImpl.java",
				"class PersonImpl extends MPersonImpl implements Person");
	}

	@Test
	public void implProjectMustExtendMProjectImpAndImplementsProject()
	{

		assertFileContains("src/com/opcoach/project/impl/ProjectImpl.java",
				"class ProjectImpl extends MProjectImpl implements Project");
	}

	@Test
	public void implDocProjectMustExtendMDocProjectImpAndImplementsDocProject()
	{
		assertFileContains("src/com/opcoach/project/documentation/impl/DocumentationProjectImpl.java",
				"class DocumentationProjectImpl extends MDocumentationProjectImpl implements DocumentationProject");
	}

	@Test
	public void implDocTaskMustExtendMDocTaskImpAndImplementsDocTask()
	{
		assertFileContains("src/com/opcoach/project/documentation/impl/DocumentationTaskImpl.java",
				"class DocumentationTaskImpl extends MDocumentationTaskImpl implements DocumentationTask");
	}

	// ----------------------------------------------------------------------------------------------------------
	// ------------------- Test gen implementations extends gen implementations
	// or parent dev implementations --
	// ----------------------------------------------------------------------------------------------------------
	@Test
	public void implMDocProjectImplMustExtendProjectImplAndImplementsMDocProject()
	{

		assertFileContains("src-gen/com/opcoach/project/documentation/impl/MDocumentationProjectImpl.java",
				"class MDocumentationProjectImpl extends ProjectImpl implements DocumentationProject");
	}

	// -----------------------------------------------------------------
	// ------------------- Test composition getters ------------------
	// -----------------------------------------------------------------

	@Test
	public void getTaskOnGenProjectInterfaceMustReturnListOfTask()
	{
		assertFileContains("src-gen/com/opcoach/project/MProject.java", "EList<Task> getTasks();");
	}

	@Test
	public void getMainTaskOnGenProjectInterfaceMustReturnTask()
	{
		assertFileContains("src-gen/com/opcoach/project/MProject.java", "Task getMainTask();");
		assertFileContains("src-gen/com/opcoach/project/MProject.java", "void setMainTask(Task value);");
	}

	@Test
	public void getMainTaskOnGenProjectClassMustReturnTask()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "protected Task mainTask;");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "public Task getMainTask()");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "public void setMainTask(Task newMainTask)");
	}


	@Test
	public void getProjectOnCompanyMustReturnListOfProjects()
	{
		assertFileContains("src-gen/com/opcoach/project/MCompany.java", "EList<Project> getProjects();");
	}

	@Test
	public void getEmployeesOnCompanyMustReturnListOfPerson()
	{
		assertFileContains("src-gen/com/opcoach/project/MCompany.java", "EList<Person> getEmployees();");
	}

	// -----------------------------------------------------------------
	// ------------------- Test factory API ------------------
	// -----------------------------------------------------------------

	@Test
	public void theProjectFactoryMustExtendsMProjectFactory()
	{
		assertFileContains("src/com/opcoach/project/ProjectFactory.java",
				"public interface ProjectFactory extends MProjectFactory");
	}

	@Test
	public void theEMFGeneratedProjectFactoryMustCreatePerson()
	{
		assertFileContains("src-gen/com/opcoach/project/MProjectFactory.java", "Person createPerson();");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectFactoryImpl.java", "new PersonImpl();");
	}

	@Test
	public void theEMFGenereatedProjectFactoryMustCreateProject()
	{
		assertFileContains("src-gen/com/opcoach/project/MProjectFactory.java", "Project createProject()");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectFactoryImpl.java", "new ProjectImpl();");
	}

	@Test
	public void theEMFGenereatedProjectFactoryMustCreateTask()
	{
		assertFileContains("src-gen/com/opcoach/project/MProjectFactory.java", "Task createTask();");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectFactoryImpl.java", "new TaskImpl();");
	}

	@Test
	public void theEMFGeneratedProjectFactoryMustCreateCompany()
	{
		assertFileContains("src-gen/com/opcoach/project/MProjectFactory.java", "Company createCompany();");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectFactoryImpl.java", "new CompanyImpl();");
	}

	@Test
	public void filesMustContainCopyright()
	{
		assertFileContains("src/com/opcoach/project/Project.java", copyright);
		assertFileContains("src/com/opcoach/project/ProjectFactory.java", copyright);
		assertFileContains("src/com/opcoach/project/ProjectPackage.java", copyright);
		assertFileContains("src/com/opcoach/project/impl/ProjectImpl.java", copyright);
		assertFileContains("src/com/opcoach/project/impl/ProjectFactoryImpl.java", copyright);
	}

	// -----------------------------------------------------------------------
	// ------------------- Test gen operations must return the dev types --
	// -----------------------------------------------------------------------

	@Test
	public void theFindTaskMethodMustReturnTask()
	{
		assertFileContains("src-gen/com/opcoach/project/MProject.java", "Task findFirstTask");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "public Task findFirstTask");

	}

	@Test
	public void theFindTaskMethodMustReceiveAPerson()
	{
		assertFileContains("src-gen/com/opcoach/project/MProject.java", "findFirstTask(Person p);");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "findFirstTask(Person p)");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java",
				"return findFirstTask((Person)arguments.get(0))");

	}
	
	// -----------------------------------------------------------------------
	// ------------------- EClass defined as interface  --
	// -----------------------------------------------------------------------
	@Test
	public void anEclassAsInterfaceMustNotGenerateImplementation()
	{
		assertFileExists("src/com/opcoach/project/ClassAsInterface.java");
		assertFileNotExists("src/com/opcoach/project/impl/ClassAsInterfaceImpl.java");
	}
	
	
	// -----------------------------------------------------------------------
	// ------------------- Enum type must be cast in the eSet method -
	// -----------------------------------------------------------------------
	@Test
	public void enumTypeMustBeCastInESetMethod()
	{
		assertFileContains("src-gen/com/opcoach/project/impl/MPersonImpl.java", "setType((TypePerson)newValue);");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "setType((TypeProject)newValue);");
	}
	
	
	// -----------------------------------------------------------------------
	// ------------------- Tests for issue #66 / https://github.com/opcoach/genModelAddon/issues/66
	// -----------------------------------------------------------------------
	@Test
	public void classWithMapInstanceNameMustNotBeGenerated()
	{
		GenModel gm = getGenModel(PROJECT_GENMODEL);
		// Check EClass IntToDoubleMap exists. 
		GenClass gc = findGenClass(gm, "IntToDoubleMap");
		EClass c = (gc == null) ? null : gc.getEcoreClass();
		
		assertNotNull("The EClass IntToDoubleMap is present in test model", c);
		assertTrue("The IntToDoubleMap instance type name must be java.util.Map$Entry", GenerateCommon.isMapType(c));
		
		// Now the file for this class must not be generated...
		assertFileNotExists("src/com/opcoach/project/IntToDoubleMap.java");
		assertFileNotExists("src/com/opcoach/project/impl/IntToDoubleMapImpl.java");
		
		// Check that factory generated file does not contain the IntToDoubleMap class
		assertFileDoesNotContain("src/com/opcoach/project/ProjectFactory.java", "IntToDoubleMap");
		assertFileDoesNotContain("src/com/opcoach/project/impl/ProjectFactoryImpl.java", "IntToDoubleMap");

	}

	


}
