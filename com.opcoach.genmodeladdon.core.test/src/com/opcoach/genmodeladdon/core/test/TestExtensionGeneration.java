package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.PDEExtensionRegistry;
import org.eclipse.pde.internal.core.PluginModelManager;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("restriction")
public class TestExtensionGeneration extends GenModelAddonTestCase
{

	static String EMF_GENERATED_PACKAGE = "org.eclipse.emf.ecore.generated_package";
	static String FACTORY_OVERRIDE = "org.eclipse.emf.ecore.factory_override";
	static String PACKAGE_ELT = "package";
	static String FACTORY_ELT = "factory";
	static String URI_ATTR = "uri";
	static String CLASS_NAME = "class";

	/**
	 * Keep the project extension to check (overridden generatedPackage and
	 * factory)
	 */
	private static Collection<IPluginExtension> extensionsToBeChecked = null;

	// ----------------------------------------------------
	// ------------------- Test emf generated package ----
	// ----------------------------------------------------
	@BeforeClass
	public static void prepareTest()
	{
		// Initialize extensions of sample project
		extensionsToBeChecked = new ArrayList<IPluginExtension>();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(SAMPLE_PROJECT);
		
		PluginModelManager pm = PluginModelManager.getInstance();
		IPluginModelBase base = pm.findModel(project);
		PDEExtensionRegistry pdeReg = PDECore.getDefault().getExtensionsRegistry();

		for (IPluginExtension e : base.getExtensions().getExtensions())
		{
			if (e.getPoint().equals(FACTORY_OVERRIDE))
			{
				extensionsToBeChecked.add(e);
			} else if (e.getPoint().equals(EMF_GENERATED_PACKAGE))
			{
				extensionsToBeChecked.add(e);
			}

		}

	}

	@Test
	public void checkOnlyOneEmfGeneratedPackageForProject()
	{
		String uriToCheck = "http://com.opcoach.project/1.0";
		int nbExt = countEltWithUriAttribute(EMF_GENERATED_PACKAGE, PACKAGE_ELT, uriToCheck);
		assertEquals("There must be only one genPackage extension for " + uriToCheck, 1, nbExt);
	}

	@Test
	public void checkOnlyOneEmfGeneratedPackageForDocumentationProject()
	{
		String uriToCheck = "http://www.opcoach.com/project/documentation/1.0";
		int nbExt = countEltWithUriAttribute(EMF_GENERATED_PACKAGE, PACKAGE_ELT, uriToCheck);
		assertEquals("There must be only one genPackage extension for " + uriToCheck, 1, nbExt);
	}

	@Test
	public void checkOnlyOneEmfGeneratedPackageForFanoise()
	{
		String uriToCheck = "http://www.airbus.com.generic/fannoise";
		int nbExt = countEltWithUriAttribute(EMF_GENERATED_PACKAGE, PACKAGE_ELT, uriToCheck);
		assertEquals("There must be only one genPackage extension for " + uriToCheck, 1, nbExt);
	}

	@Test
	public void checkOnlyOneFactoryOverrideForProject()
	{
		String uriToCheck = "http://com.opcoach.project/1.0";
		int nbExt = countEltWithUriAttribute(FACTORY_OVERRIDE, FACTORY_ELT, uriToCheck);
		assertEquals("There must be only one factory override extension for " + uriToCheck, 1, nbExt);
	}

	@Test
	public void checkFactoryOverrideClassnameForProject()
	{
		String uriToCheck = "http://com.opcoach.project/1.0";
		String obtainedName = getClassNameFor(FACTORY_OVERRIDE, FACTORY_ELT, uriToCheck);
		String expectedFactoryClassname = "com.opcoach.project.impl.ProjectFactoryImpl";
		assertEquals("The expected classname for factory of project must be " + expectedFactoryClassname,
				expectedFactoryClassname, obtainedName);
	}

	@Test
	public void checkFactoryOverrideClassnameForDocumentationProject()
	{
		String uriToCheck = "http://www.opcoach.com/project/documentation/1.0";
		String obtainedName = getClassNameFor(FACTORY_OVERRIDE, FACTORY_ELT, uriToCheck);
		String expectedFactoryClassname = "com.opcoach.project.documentation.impl.DocumentationFactoryImpl";
		assertEquals("The expected classname for factory of documentation project must be " + expectedFactoryClassname,
				expectedFactoryClassname, obtainedName);
	}

	@Test
	public void checkOnlyOneFactoryOverrideForDocumentationProject()
	{
		String uriToCheck = "http://www.opcoach.com/project/documentation/1.0";
		int nbExt = countEltWithUriAttribute(FACTORY_OVERRIDE, FACTORY_ELT, uriToCheck);
		assertEquals("There must be only one factory override extension for " + uriToCheck, 1, nbExt);
	}

	@Test
	public void checkOnlyOneFactoryOverrideForFanoise()
	{
		String uriToCheck = "http://www.airbus.com.generic/fannoise";
		int nbExt = countEltWithUriAttribute(FACTORY_OVERRIDE, FACTORY_ELT, uriToCheck);
		assertEquals("There must be only one factory override extension for " + uriToCheck, 1, nbExt);
	}
	
	
	@Test
	public void checkFactoryOverrideClassnameForFannoiseProject()
	{
		String uriToCheck = "http://www.airbus.com.generic/fannoise";
		String obtainedName = getClassNameFor(FACTORY_OVERRIDE, FACTORY_ELT, uriToCheck);
		String expectedFactoryClassname = "fannoise.impl.FanNoiseFactoryImpl";
		assertEquals("The expected classname for factory of fannoise project must be " + expectedFactoryClassname,
				expectedFactoryClassname, obtainedName);
	}


	@Test
	public void checkApplicationExtensionStillThere()
	{
		PDEExtensionRegistry pdeReg = PDECore.getDefault().getExtensionsRegistry();

		IPluginModelBase base = PluginRegistry.getWorkspaceModels()[0];
		String id = base.getBundleDescription().getSymbolicName();
		Collection<IPluginExtension> appliExt = new ArrayList<IPluginExtension>();

		for (IPluginExtension e : pdeReg.findExtensionsForPlugin(base))
		{
			if (e.getPoint().equals("org.eclipse.core.runtime.applications"))
				appliExt.add(e);

		}

		assertEquals("The application extension must be still there and only once", 1, appliExt.size());

	}

	/**
	 * Count number of extensions containing the modelUri in the Uri extension
	 * attribute for a given eltName
	 */

	private int countEltWithUriAttribute(String extPoint, String eltName, String modelUri)
	{
		int result = 0;
		for (IPluginExtension e : extensionsToBeChecked)
		{
			if (e.getPoint().equals(extPoint))
			{
				// Check if there is already an element with this modelUri
				// attribute...
				for (IPluginObject elt : e.getChildren())
				{
					if (elt instanceof IPluginElement)
					{
						IPluginElement ipe = (IPluginElement) elt;
						if (ipe.getName().equals(eltName))
						{
							String uriAtt = ipe.getAttribute(URI_ATTR).getValue();
							if (modelUri.equals(uriAtt))
							{
								result++;
							}
						}
					}
				}
			}
		}

		return result;
	}

	private String getClassNameFor(String extPoint, String eltName, String modelUri)
	{

		for (IPluginExtension e : extensionsToBeChecked)
		{
			if (e.getPoint().equals(extPoint))
			{
				// Check if there is already an element with this modelUri
				// attribute...
				for (IPluginObject elt : e.getChildren())
				{
					if (elt instanceof IPluginElement)
					{
						IPluginElement ipe = (IPluginElement) elt;
						String uriAttr = ipe.getAttribute(URI_ATTR).getValue();
						if (ipe.getName().equals(eltName) && uriAttr.equals(modelUri))
						{
							return ipe.getAttribute(CLASS_NAME).getValue();

						}
					}
				}
			}
		}

		return null;
	}

}
