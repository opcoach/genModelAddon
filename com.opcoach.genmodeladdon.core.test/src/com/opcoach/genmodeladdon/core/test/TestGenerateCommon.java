package com.opcoach.genmodeladdon.core.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import com.opcoach.genmodeladdon.core.EMFPatternExtractor;
import com.opcoach.genmodeladdon.core.GenerateCommon;

public class TestGenerateCommon extends GenModelAddonTestCase
{
	private static final String PROJECT_SAMPLE_NAME = "com.opcoach.genmodeladdon.sample";

	private String getWsHome()
	{
		return root.getLocation().toString();
	}
	@Test
	public void testGetProjectNameFromSimpleURI()
	{
		URI u = URI.createURI(getWsHome() + "/com.opcoach.genmodeladdon.sample/model/project.genmodel");
		String s = GenerateCommon.getProjectNameFromURI(u);
		assertEquals("Project name must be " + PROJECT_SAMPLE_NAME, PROJECT_SAMPLE_NAME, s);
	}
	
	@Test
	public void testGetProjectNameFromWindowsFileURI()
	{
		URI u = URI.createURI("file:/" + getWsHome() + "/com.opcoach.genmodeladdon.sample/model/project.genmodel");
		String s = GenerateCommon.getProjectNameFromURI(u);
		assertEquals("Project name must be " + PROJECT_SAMPLE_NAME, PROJECT_SAMPLE_NAME, s);
	}


	@Test
	public void testGetProjectNameFromProjectGenModel()
	{
		GenModel gm = getGenModel(PROJECT_GENMODEL);
		String s = GenerateCommon.getProjectName(gm);
		assertEquals("Project name must be " + PROJECT_SAMPLE_NAME, PROJECT_SAMPLE_NAME, s);
	}
	
	@Test
	public void testGetProjectNameFromFannoiseGenModel()
	{
		GenModel gm = getGenModel(FANNOISE_GENMODEL);
		String s = GenerateCommon.getProjectName(gm);
		assertEquals("Project name must be " + PROJECT_SAMPLE_NAME, PROJECT_SAMPLE_NAME, s);
	}
	
	@Test
	public void testGetProjectNameFromPlatformResourceURI()
	{
		GenModel gm = getGenModel(FANNOISE_GENMODEL);
		String s = GenerateCommon.getProjectNameFromURI(URI.createPlatformResourceURI("/projectName/model/project.genmodel", true));
		assertEquals("Project name must be projectName", "projectName", s);
	}
	
	
	
	
	@Test
	public void testCreateSourceDirectory()
	{
		EMFPatternExtractor epe = new EMFPatternExtractor(sampleProject, "{0}Impl", "{0}");
		IFolder f = epe.createSourceDirectoryStructure();
		assertNotNull("SourceDirectory must not be null", f);

	}
	
	

}