package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.EClass;
import org.junit.Test;

import com.opcoach.genmodeladdon.core.GenerateCommon;

/** This test case is for the model2 folder containing specific genmodel generator parameters
 *  It has specific classPatterns : 
 *   IG{0} and G{0}impl, for src-gen 
 *   G{0} and {0}impl, for src
 * see bug #76
 * @author olivier
 *
 */
public class TestModel2 extends GenModelAddonTestCase
{

	private String copyright = "OPCoach 2018";

	

	// ----------------------------------------------------------------------------------------------------------
	// ------------------- Test dev interfaces extends gen interfaces or parent
	// dev interface ------------------
	// ----------------------------------------------------------------------------------------------------------

	@Test
	public void interfaceGProjectMustExtendsIGProject()
	{
		assertFileContains("src/com/opcoach2/project/GProject.java", "public interface GProject extends IGProject");
	}


	@Test
	public void interfaceIGDocumentationProjectMustExtendsGProject()
	{
		assertFileContains("src-gen/com/opcoach2/project/documentation/IGDocumentationProject.java",
				"interface IGDocumentationProject extends GProject");
	}

	@Test
	public void interfaceGDocumentationProjectMustExtendsIGDocumentationProject()
	{
		assertFileContains("src/com/opcoach2/project/documentation/GDocumentationProject.java",
				"interface GDocumentationProject extends IGDocumentationProject");
	}

	// ----------------------------------------------------------------------------------------------------------
	// ------------------- Test dev implementations extends gen implementations
	// or parent dev implementations --
	// ----------------------------------------------------------------------------------------------------------
	@Test
	public void implCompanyMustExtendGCompanyImplAndImplementsGCompany()
	{

		assertFileContains("src/com/opcoach2/project/impl/CompanyImpl.java",
				"class CompanyImpl extends GCompanyImpl implements GCompany");
	}

	@Test
	public void implTaskMustExtendGTaskImplAndImplementsGTask()
	{

		assertFileContains("src/com/opcoach2/project/impl/TaskImpl.java",
				"class TaskImpl extends GTaskImpl implements GTask");
	}


	@Test
	public void implDocProjectMustExtendGDocProjectImpAndImplementsGDocProject()
	{
		assertFileContains("src/com/opcoach2/project/documentation/impl/DocumentationProjectImpl.java",
				"class DocumentationProjectImpl extends GDocumentationProjectImpl implements GDocumentationProject");
	}


	// ----------------------------------------------------------------------------------------------------------
	// ------------------- Test gen implementations extends gen implementations
	// or parent dev implementations --
	// ----------------------------------------------------------------------------------------------------------
	@Test
	public void implGDocProjectImplMustExtendGProjectImplAndImplementsGDocProject()
	{

		assertFileContains("src-gen/com/opcoach2/project/documentation/impl/GDocumentationProjectImpl.java",
				"class GDocumentationProjectImpl extends ProjectImpl implements GDocumentationProject");
	}

	// -----------------------------------------------------------------
	// ------------------- Test composition getters ------------------
	// -----------------------------------------------------------------

	@Test
	public void getTaskOnGenProjectInterfaceMustReturnListOfGTasks()
	{
		assertFileContains("src-gen/com/opcoach2/project/IGProject.java", "EList<GTask> getTasks();");
	}

	@Test
	public void getMainTaskOnGenProjectInterfaceMustReturnGTask()
	{
		assertFileContains("src-gen/com/opcoach2/project/IGProject.java", "GTask getMainTask();");
		assertFileContains("src-gen/com/opcoach2/project/IGProject.java", "void setMainTask(GTask value);");
	}

	@Test
	public void getMainTaskOnGenProjectClassMustReturnGTask()
	{
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectImpl.java", "protected GTask mainTask;");
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectImpl.java", "public GTask getMainTask()");
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectImpl.java", "public void setMainTask(GTask newMainTask)");
	}


	@Test
	public void getProjectOnCompanyMustReturnListOfGProjects()
	{
		assertFileContains("src-gen/com/opcoach2/project/IGCompany.java", "EList<GProject> getProjects();");
	}

	
	// -----------------------------------------------------------------
	// ------------------- Test factory API ------------------
	// -----------------------------------------------------------------

	@Test
	public void theGProjectFactoryMustExtendsIGProjectFactory()
	{
		assertFileContains("src/com/opcoach2/project/GProjectFactory.java",
				"public interface GProjectFactory extends IGProjectFactory");
	}

	@Test
	public void theEMFGeneratedProjectFactoryMustCreateGPerson()
	{
		assertFileContains("src-gen/com/opcoach2/project/IGProjectFactory.java", "GPerson createPerson();");
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectFactoryImpl.java", "new PersonImpl();");
	}

	@Test
	public void theEMFGenereatedProjectFactoryMustCreateGProject()
	{
		assertFileContains("src-gen/com/opcoach2/project/IGProjectFactory.java", "GProject createProject()");
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectFactoryImpl.java", "new ProjectImpl();");
	}


	// -----------------------------------------------------------------------
	// ------------------- Test gen operations must return the dev types --
	// -----------------------------------------------------------------------

	@Test
	public void theFindTaskMethodMustReturnGTask()
	{
		assertFileContains("src-gen/com/opcoach2/project/IGProject.java", "GTask findFirstTask");
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectImpl.java", "public GTask findFirstTask");

	}

	
	@Test
	public void theFindTaskMethodMustReceiveAGPerson()
	{
		assertFileContains("src-gen/com/opcoach2/project/IGProject.java", "findFirstTask(GPerson p);");
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectImpl.java", "findFirstTask(GPerson p)");
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectImpl.java",
				"return findFirstTask((GPerson)arguments.get(0))");

	}
	
	// -----------------------------------------------------------------------
	// ------------------- EClass defined as interface  --
	// -----------------------------------------------------------------------
	@Test
	public void anEclassAsInterfaceMustNotGenerateImplementation2()
	{
		assertFileExists("src/com/opcoach2/project/GClassAsInterface.java");
		assertFileNotExists("src/com/opcoach2/project/impl/ClassAsInterfaceImpl.java");
	}
	
	
	// -----------------------------------------------------------------------
	// ------------------- Enum type must be cast in the eSet method -
	// -----------------------------------------------------------------------
	@Test
	public void enumTypeMustBeCastCorrectlyInESetMethod()
	{
		assertFileContains("src-gen/com/opcoach2/project/impl/GPersonImpl.java", "setType((TypePerson)newValue);");
		assertFileContains("src-gen/com/opcoach2/project/impl/GProjectImpl.java", "setType((TypeProject)newValue);");
	}
	
	
	// -----------------------------------------------------------------------
	// ------------------- Tests for issue #66 / https://github.com/opcoach/genModelAddon/issues/66
	// -----------------------------------------------------------------------
	@Test
	public void classWithMapInstanceNameMustNotBeGenerated2()
	{
		GenModel gm = getGenModel(PROJECT2_GENMODEL);
		// Check EClass IntToDoubleMap exists. 
		GenClass gc = findGenClass(gm, "IntToDoubleMap");
		EClass c = (gc == null) ? null : gc.getEcoreClass();
		
		assertNotNull("The EClass IntToDoubleMap is present in test model", c);
		assertTrue("The IntToDoubleMap instance type name must be java.util.Map$Entry", GenerateCommon.isMapType(c));
		
		// Now the file for this class must not be generated...
		assertFileNotExists("src/com/opcoach2/project/GIntToDoubleMap.java");
		assertFileNotExists("src/com/opcoach2/project/impl/IntToDoubleMapImpl.java");
		
		// Check that factory generated file does not contain the IntToDoubleMap class
		assertFileDoesNotContain("src/com/opcoach2/project/GProjectFactory.java", "IntToDoubleMap");
		assertFileDoesNotContain("src/com/opcoach2/project/impl/ProjectFactoryImpl.java", "IntToDoubleMap");

	}

}
