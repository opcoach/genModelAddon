package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.junit.Test;

public class TestInterfaceGeneration extends GenModelAddonTestCase
{

	private String copyright = "© OPCoach 2016";
	
	@Test
	public void testClassNames()
	{
		GenClass gc = findGenClass(gm, "Project");
		// Check interface and class names
		assertEquals("The dev interface name must be Project", "Project", gen.computeInterfaceName(gc));
		assertEquals("The dev class  name must be ProjectImpl", "ProjectImpl", gen.computeClassname(gc));
		assertEquals("The gen interface  name must be MProject", "MProject", gen.computeGeneratedInterfaceName(gc));
		assertEquals("The gen class  name must be MProjectImpl", "MProjectImpl", gen.computeGeneratedClassName(gc));

		// Check for packages names
		GenPackage gp = findGenPackage(gm, "project");
		assertEquals("The dev factory interface  name must be ProjectFactory", "ProjectFactory",
				gen.computeFactoryInterfaceName(gp));
		assertEquals("The dev factory class  name must be ProjectFactoryImpl", "ProjectFactoryImpl",
				gen.computeFactoryClassName(gp));
		assertEquals("The gen factory interface  name must be MProjectFactory", "MProjectFactory",
				gen.computeGeneratedFactoryInterfaceName(gp));
		assertEquals("The gen factory class  name must be MProjectFactoryImpl", "MProjectFactoryImpl",
				gen.computeGeneratedFactoryClassName(gp));
	}
	
	//----------------------------------------------------------------------------------------------------------
	// ------------------- Test dev interfaces extends gen interfaces or parent dev interface ------------------
	//----------------------------------------------------------------------------------------------------------

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
		assertFileContains("src-gen/com/opcoach/project/documentation/MDocumentationProject.java", "interface MDocumentationProject extends Project");
	}

	@Test
	public void interfaceDocumentationProjectMustExtendsMDocumentationProject()
	{
		assertFileContains("src/com/opcoach/project/documentation/DocumentationProject.java", "interface DocumentationProject extends MDocumentationProject");
	}
	
	//----------------------------------------------------------------------------------------------------------
	// ------------------- Test dev implementations extends gen implementations or parent dev implementations --
	//----------------------------------------------------------------------------------------------------------
	@Test
	public void implCompanyMustExtendMCompanyImplAndImplementsCompany()
	{

		assertFileContains("src/com/opcoach/project/impl/CompanyImpl.java", "class CompanyImpl extends MCompanyImpl implements Company");
	}
	
	@Test
	public void implTaskMustExtendMTaskImplAndImplementsTask()
	{

		assertFileContains("src/com/opcoach/project/impl/TaskImpl.java", "class TaskImpl extends MTaskImpl implements Task");
	}
	
	@Test
	public void implPersonMustExtendMPersonImplAndImplementsPerson()
	{

		assertFileContains("src/com/opcoach/project/impl/PersonImpl.java", "class PersonImpl extends MPersonImpl implements Person");
	}
	
	@Test
	public void implProjectMustExtendMProjectImpAndImplementsProject()
	{

		assertFileContains("src/com/opcoach/project/impl/ProjectImpl.java", "class ProjectImpl extends MProjectImpl implements Project");
	}
	
	@Test
	public void implDocProjectMustExtendMDocProjectImpAndImplementsDocProject()
	{
		assertFileContains("src/com/opcoach/project/documentation/impl/DocumentationProjectImpl.java", "class DocumentationProjectImpl extends MDocumentationProjectImpl implements DocumentationProject");
	}
	
	@Test
	public void implDocTaskMustExtendMDocTaskImpAndImplementsDocTask()
	{
		assertFileContains("src/com/opcoach/project/documentation/impl/DocumentationTaskImpl.java", "class DocumentationTaskImpl extends MDocumentationTaskImpl implements DocumentationTask");
	}
	
	
	
	//----------------------------------------------------------------------------------------------------------
	// ------------------- Test gen implementations extends gen implementations or parent dev implementations --
	//----------------------------------------------------------------------------------------------------------
	@Test
	public void implMDocProjectImplMustExtendProjectImplAndImplementsMDocProject()
	{

		assertFileContains("src-gen/com/opcoach/project/documentation/impl/MDocumentationProjectImpl.java", "class MDocumentationProjectImpl extends ProjectImpl implements MDocumentationProject");
	}
	

	
	
	
	
	
	//-----------------------------------------------------------------
	// ------------------- Test composition getters  ------------------
	//-----------------------------------------------------------------

	@Test public void getTaskOnProjectMustReturnListOfTask()
	{
		assertFileContains("src-gen/com/opcoach/project/MProject.java", "EList<Task> getTasks();");
	}

	
	@Test public void getProjectOnCompanyMustReturnListOfProjects()
	{
		assertFileContains("src-gen/com/opcoach/project/MCompany.java", "EList<Project> getProjects();");
	}

	@Test public void getEmployeesOnCompanyMustReturnListOfPerson()
	{
		assertFileContains("src-gen/com/opcoach/project/MCompany.java", "EList<Person> getEmployees();");
	}

	
	
	//-----------------------------------------------------------------
	// ------------------- Test factory API          ------------------
	//-----------------------------------------------------------------

	@Test public void theProjectFactoryMustExtendsMProjectFactory()
	{
		assertFileContains("src/com/opcoach/project/ProjectFactory.java", "public interface ProjectFactory extends MProjectFactory");
	}

	@Test public void theProjectFactoryMustCreatePerson()
	{
		assertFileContains("src/com/opcoach/project/ProjectFactory.java", "public Person createPerson();");
		assertFileContains("src/com/opcoach/project/impl/ProjectFactoryImpl.java", "new PersonImpl();");
	}

	@Test public void theProjectFactoryMustCreateProject()
	{
		assertFileContains("src/com/opcoach/project/ProjectFactory.java", "public Project createProject()");
		assertFileContains("src/com/opcoach/project/impl/ProjectFactoryImpl.java", "new ProjectImpl();");
	}

	@Test public void theProjectFactoryMustCreateTask()
	{
		assertFileContains("src/com/opcoach/project/ProjectFactory.java", "public Task createTask();");
		assertFileContains("src/com/opcoach/project/impl/ProjectFactoryImpl.java", "new TaskImpl();");
	}

	@Test public void theProjectFactoryMustCreateCompany()
	{
		assertFileContains("src/com/opcoach/project/ProjectFactory.java", "public Company createCompany();");
		assertFileContains("src/com/opcoach/project/impl/ProjectFactoryImpl.java", "new CompanyImpl();");
	}

	
	@Test public void filesMustContainCopyright()
	{
		assertFileContains("src/com/opcoach/project/Project.java", copyright);
		assertFileContains("src/com/opcoach/project/ProjectFactory.java", copyright);
		assertFileContains("src/com/opcoach/project/ProjectPackage.java", copyright);
		assertFileContains("src/com/opcoach/project/impl/ProjectImpl.java", copyright);
		assertFileContains("src/com/opcoach/project/impl/ProjectFactoryImpl.java", copyright);
	}
	
	//-----------------------------------------------------------------------
	// ------------------- Test gen operations must return the dev types   --
	//-----------------------------------------------------------------------

	@Test public void theFindTaskMethodMustReturnTask()
	{
		assertFileContains("src-gen/com/opcoach/project/MProject.java", "Task findFirstTask");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "public Task findFirstTask");
		

	}

	@Test public void theFindTaskMethodMustReceiveAPerson()
	{
		assertFileContains("src-gen/com/opcoach/project/MProject.java", "findFirstTask(Person p);");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "findFirstTask(Person p)");
		assertFileContains("src-gen/com/opcoach/project/impl/MProjectImpl.java", "return findFirstTask((Person)arguments.get(0))");
		
	}

	
	
}
