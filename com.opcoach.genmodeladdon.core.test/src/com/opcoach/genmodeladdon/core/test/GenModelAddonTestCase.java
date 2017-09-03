package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import com.opcoach.genmodeladdon.core.GenerateDevStructure;

public class GenModelAddonTestCase
{

	protected static final String SRC_SAMPLE_PROJECT = "com.opcoach.genmodeladdon.sample";
	protected static final String DEST_SAMPLE_PROJECT = "com.opcoach.genmodeladdon.destsample";

	protected static final String FANOISE_ANT_FILE = "generateEMFCode_fanoise.xml";
	protected static final String PROJECT_ANT_FILE = "generateEMFCode_project.xml";
	public static final String PROJECT_GENMODEL = "/" + DEST_SAMPLE_PROJECT + "/model/project.genmodel";
	public static final String FANNOISE_GENMODEL = "/" + DEST_SAMPLE_PROJECT + "/model_fannoise/fannoise.genmodel";


	protected static Map<String, GenModel> gmMap = new HashMap<String, GenModel>();
	protected static Map<String, GenerateDevStructure> genMap = new HashMap<String, GenerateDevStructure>();

	protected static IWorkspaceRoot root;

	protected static IProject sampleProject;
	
	public static boolean projectCreated = false;

	static
	{
		// Start manually the jdt.ui and catch the bad exception..
		// Actually we don't need this plugin (or may ben ant.launching needs it ?) but it appears with dependencies
		Bundle jdtUI = Platform.getBundle("org.eclipse.jdt.ui");
		try
		{
			if (jdtUI != null)
				jdtUI.start();
		} catch (BundleException e)
		{
		}

	} 
	
	
	@BeforeClass
	public static void createProjectAndGenerate()
	{
		System.out.println("******  Creating the projects ");
		if (!projectCreated)
		{
			try
			{
				// Copy the sample project in the runtime workspace
				root = initWorkspace();
				initGenModel(PROJECT_GENMODEL, PROJECT_ANT_FILE);
				initGenModel(FANNOISE_GENMODEL, FANOISE_ANT_FILE);
			} catch (Exception ex)
			{

				ex.printStackTrace();
			}
			projectCreated = true;
		}
	}
	

	public static void initGenModel(String genModelName, String antFilename) throws IOException
	{

		// Read the genModel
		GenModel gm = readSampleGenModel(root, genModelName);
		gmMap.put(genModelName, gm);

		// Create the generator.
		GenerateDevStructure gen = new GenerateDevStructure(gm, "{0}Impl", "{0}", "src");
		genMap.put(genModelName, gen);

		// Remember of sample project
		sampleProject = root.getProject(DEST_SAMPLE_PROJECT);
		try
		{
			sampleProject.open(new NullProgressMonitor());
		} catch (CoreException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Install the templates
		gen.setGenModelTemplates(gm, true);

		// Generate the dev structure...
		gen.generateDevStructure(true);

		// Generate the ant file to generate emf code
		File antFile = gen.generateAntFile(antFilename);

		// Once dev structure is generated and ant file too, can call it !
		gen.generateGenModelCode(antFile, new NullProgressMonitor());

		try
		{
			sampleProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e)
		{
			e.printStackTrace();
		}

	}

	protected GenModel getGenModel(String name)
	{
		return gmMap.get(name);
	}

	protected GenerateDevStructure getGenDevStructure(String name)
	{
		return genMap.get(name);
	}

	/**
	 * Read the sample gen model located in com.opcoach.genmodeladdon.sample
	 * project
	 */
	private static GenModel readSampleGenModel(IWorkspaceRoot root, String pathToGenModel)
	{
		// Read the sample gen model in temporary workspace
		String path = root.getLocation().toString() + pathToGenModel;

		ResourceSet rset = new ResourceSetImpl();
		// rset.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap(true));

		// Get the resource
		Resource resource = rset.getResource(URI.createURI("file:/" + path), true);
		return (GenModel) resource.getContents().get(0);

	}

	/** This method initialize the test workspace with a sample project */
	private static IWorkspaceRoot initWorkspace() throws IOException
	{
		// Get the zipped file to extract
		Bundle b = Platform.getBundle(SRC_SAMPLE_PROJECT);
		URL url = b.getEntry("sampleProject.zip");
		String fileURL = FileLocator.toFileURL(url).toString();

		// Create a sample empty project in workspace root
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject proj = root.getProject(DEST_SAMPLE_PROJECT);

		NullProgressMonitor npm = new NullProgressMonitor();
		try
		{
			if (proj.exists())
			{
				// remove previous project version. 
				System.out.println("Deleting existing project : " + DEST_SAMPLE_PROJECT);
				proj.delete(true, npm);
			}

			proj.create(npm);

		} catch (CoreException e1)
		{
		     e1.printStackTrace();
		}

		// Then get the ant file to run to copy the template project
		AntRunner runner = new AntRunner();
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("wsRoot", root.getLocation().toOSString() + File.separator + DEST_SAMPLE_PROJECT);
		properties.put("zipFile", fileURL.replace("file:", ""));
		properties.put("destProjectName", DEST_SAMPLE_PROJECT);
		properties.put("srcProjectName", SRC_SAMPLE_PROJECT);
		runner.addUserProperties(properties);
		runner.setBuildFileLocation("prepareTestWorkspace.xml");
		runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
		try
		{
			runner.run();
			root.refreshLocal(IResource.DEPTH_INFINITE, null);

			// Now close the project and reopen it
			proj.close(npm);
		    proj = root.getProject(DEST_SAMPLE_PROJECT);
			proj.open(npm);

		} catch (CoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root;
	}

	/**
	 * This method checks if the specified content is in the file
	 * 
	 * @param path
	 *            the path expressed relative to project :
	 *            "src/impl/ProjectImpl.java" for instance
	 * @param content
	 *            a string like : "ProjectImpl extends MProjectImpl"
	 * @return
	 */
	void assertFileContains(String path, String content)
	{
		boolean found = false;
		BufferedReader fr = null;

		try
		{
			// Gets native File using EFS
			File javaFile = getFileFromIFile(sampleProject.getFile(new Path(path)));

			fr = new BufferedReader(new FileReader(javaFile));
			String line = fr.readLine();
			while ((line != null) && !found)
			{
				found = line.contains(content);
				line = fr.readLine();
			}

		} catch (CoreException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (fr != null)
					fr.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		if (!found)
			fail("The file '" + path + "' should contain the string '" + content + "' but it was not found.");

	}

	/**
	 * This method checks a file exists
	 * 
	 * @param path
	 *            the path expressed relative to project :
	 *            "src/impl/ProjectImpl.java" for instance
	 * 
	 * @return
	 */
	void assertFileExists(String path)
	{
		try
		{
			// Gets native File using EFS
			File javaFile = getFileFromIFile(sampleProject.getFile(new Path(path)));
			if (javaFile.exists())
				return;

		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		fail("The file '" + path + "' does not exists but it should exists.");

	}

	/**
	 * This method checks a file does not exists
	 * 
	 * @param path
	 *            the path expressed relative to project :
	 *            "src/impl/ProjectImpl.java" for instance
	 * 
	 * @return
	 */
	void assertFileNotExists(String path)
	{
		try
		{
			// Gets native File using EFS
			File javaFile = getFileFromIFile(sampleProject.getFile(new Path(path)));
			if (!javaFile.exists())
				return;

		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		fail("The file '" + path + "' exists but it should not exists !");

	}

	private File getFileFromIFile(IFile file) throws CoreException
	{
		// gets URI for EFS.
		java.net.URI uri = file.getLocationURI();

		// what if file is a link, resolve it.
		if (file.isLinked())
		{
			uri = file.getRawLocationURI();
		}

		return EFS.getStore(uri).toLocalFile(0, new NullProgressMonitor());

	}

	GenClass findGenClass(GenModel gm, String name)
	{
		return findGenClassInGenPackageList(gm.getGenPackages(), name);
	}

	GenClass findGenClassInGenPackageList(Collection<GenPackage> packages, String name)
	{
		for (GenPackage gp : packages)
		{
			for (GenClass gc : gp.getGenClasses())
				if (name.equals(gc.getEcoreClass().getName()))
					return gc;

			GenClass subResult = findGenClassInGenPackageList(gp.getSubGenPackages(), name);
			if (subResult != null)
				return subResult;
		}
		return null;
	}

	/** Search for genpackage at first level */
	GenPackage findGenPackage(GenModel gm, String name)
	{
		return findGenPackageInGenPackageList(gm.getGenPackages(), name);
	}

	/** Search for genpackage at first level */
	GenPackage findGenPackageInGenPackageList(Collection<GenPackage> packages, String name)
	{
		for (GenPackage gp : packages)
		{
			if (name.equals(gp.getEcorePackage().getName()))
				return gp;
			GenPackage subResult = findGenPackageInGenPackageList(gp.getSubGenPackages(), name);
			if (subResult != null)
				return subResult;
		}

		return null;
	}

}
