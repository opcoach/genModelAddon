package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

import com.opcoach.genmodeladdon.core.GenerateDevStructure;

public class TestInterfaceGeneration extends GenModelAddonTestCase
{

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

	@Test
	public void testGenerateInterfaces()
	{
		assertFileContains("src/com/opcoach/project/Project.java", "public interface Project extends MProject");
	}

}
