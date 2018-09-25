package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.junit.Test;

import com.opcoach.genmodeladdon.core.GenerateDevStructure;

/**
 * this test case controls the good name computation for packages classes like :
 * MFanNoisePackage, FanNoisePackage, MFanNoiseFactory, FanNoiseFactory when the
 * Prefix is set in the genmodel. It has been written to fix the issue #51 :
 * https://github.com/opcoach/genModelAddon/issues/51
 * 
 * @author olivier
 *
 */
public class TestClassNames extends GenModelAddonTestCase
{

	@Test
	public void test_GenPackageClassName_WithPrefix()
	{
		GenModel gm = getGenModel(FANNOISE_GENMODEL);
		GenerateDevStructure gen = getGenDevStructure(FANNOISE_GENMODEL);

		GenPackage gp = findGenPackage(gm, "fannoise");
		assertEquals("The dev factory interface  name must be FanNoiseFactory", "FanNoiseFactory",
				gen.computeFactoryInterfaceName(gp));
		assertEquals("The dev factory class  name must be FanNoiseFactoryImpl", "FanNoiseFactoryImpl",
				gen.computeFactoryClassName(gp));
		assertEquals("The gen factory interface  name must be MFanNoiseFactory", "MFanNoiseFactory",
				gp.getFactoryInterfaceName());
		assertEquals("The gen factory class  name must be MFanNoiseFactoryImpl", "MFanNoiseFactoryImpl",
				gp.getFactoryClassName());
	}

	@Test
	public void testClassNames()
	{
		GenModel gm = getGenModel(PROJECT_GENMODEL);
		GenerateDevStructure gen = getGenDevStructure(PROJECT_GENMODEL);

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
				gp.getFactoryInterfaceName());
		assertEquals("The gen factory class  name must be MProjectFactoryImpl", "MProjectFactoryImpl",
				gp.getFactoryClassName());
	}

	@Test
	public void testUsedGenericInterfaces()
	{
		GenModel gm = getGenModel(PROJECT_GENMODEL);
		GenerateDevStructure gen = getGenDevStructure(PROJECT_GENMODEL);

		// Test that for a class name interfaces used in the generic part are
		// found.

		// For Project must return an empty list
		assertEquals("'Project' has no generic used interfaces", gen.getUsedGenericInterfaceNames("Project").length, 0);

		// For 'Project<T> must return also an empty list
		assertEquals("'Project<T>' has no generic used interfaces", gen.getUsedGenericInterfaceNames("Project<T>").length, 0);

		// For 'ProjectFolder<T extends Project> must return an array with only
		// Project
		String[] result1 = gen.getUsedGenericInterfaceNames("ProjectFolder<T extends Project>");
		assertEquals("'ProjectFolder<T extends Project>' must have 1 generic interface (Project)", result1.length, 1);
		assertEquals("'ProjectFolder<T extends Project>' must have 1 generic interface (Project)", result1[0], "Project");

		// For 'ProjectFolder<T extends Project1 & Project2> must return an
		// array with Project1 and Project2
		String[] result2 = gen.getUsedGenericInterfaceNames("ProjectFolder<T extends Project1 & Project2>");
		assertEquals("'ProjectFolder<T extends Project1 & Project2>' must have 2 generic interfaces (Project1, Project2)", result2.length, 2);
		assertEquals("'ProjectFolder<T extends Project1 & Project2>' must have 2 generic interfaces (Project1, Project2)", result2[0], "Project1");
		assertEquals("'ProjectFolder<T extends Project1 & Project2>' must have 2 generic interfaces (Project1, Project2)", result2[1], "Project2");

	}

}
