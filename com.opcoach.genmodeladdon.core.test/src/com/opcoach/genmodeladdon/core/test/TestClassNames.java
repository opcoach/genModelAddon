package com.opcoach.genmodeladdon.core.test;


import static org.junit.Assert.assertEquals;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.junit.Test;

import com.opcoach.genmodeladdon.core.GenerateDevStructure;

 /** this test case controls the good name computation for packages classes like : 
  *  MFanNoisePackage, FanNoisePackage, MFanNoiseFactory, FanNoiseFactory
  *  when the Prefix is set in the genmodel. 
  *  It has been written to fix the issue #51 : https://github.com/opcoach/genModelAddon/issues/51
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

}
