package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;

import com.opcoach.genmodeladdon.core.GenerateDevStructure;

/**
 * This class is the base class for all testcase. It provides methods to check
 * if a text is present in a file
 * 
 * @author olivier
 *
 */
public class GenModelAddonTestCase implements ProjectConstants
{

	private String workspaceHome = null;

	private IProject sampleProject = null;

	protected GenModel getGenModel(String name)
	{
		return WorkspaceConfigurator.getDefault().getGenModel(name);
	}

	public GenerateDevStructure getGenDevStructure(String name)
	{
		return WorkspaceConfigurator.getDefault().getGenDevStructure(name);
	}

	protected String getWsHome()
	{
		if (workspaceHome == null)
			workspaceHome = WorkspaceConfigurator.getDefault().getWorkspaceRoot().getLocation().toString();

		return workspaceHome;
	}

	protected IProject getSampleProject()
	{
		if (sampleProject == null)
			sampleProject = WorkspaceConfigurator.getDefault().getSampleProject();

		return sampleProject;
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
			File javaFile = getFileFromIFile(getSampleProject().getFile(new Path(path)));

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
			File javaFile = getFileFromIFile(getSampleProject().getFile(new Path(path)));
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
			File javaFile = getFileFromIFile(getSampleProject().getFile(new Path(path)));
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
