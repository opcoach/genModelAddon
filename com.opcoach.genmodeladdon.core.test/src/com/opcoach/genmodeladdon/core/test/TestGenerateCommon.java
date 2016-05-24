package com.opcoach.genmodeladdon.core.test;


import static org.junit.Assert.*;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import com.opcoach.genmodeladdon.core.GenerateCommon;

public class TestGenerateCommon extends GenModelAddonTestCase
{
	private static final String PROJECT_SAMPLE_NAME = "com.opcoach.genmodeladdon.sample";

	@Test
	public void testGetProjectNameFromLinuxURI()
	{
		URI u = URI.createURI("/Users/olivier/Documents/OPCoach/workspaces/junit-genmodeladdon/com.opcoach.genmodeladdon.sample/model/project.genmodel");
		String s = GenerateCommon.getProjectNameFromURI(u);
		assertEquals("Project name must be " + PROJECT_SAMPLE_NAME, PROJECT_SAMPLE_NAME, s);
	}

	@Test
	public void testGetProjectNameFromFileURI()
	{
		URI u = URI.createURI("file:/Users/olivier/Documents/OPCoach/workspaces/junit-genmodeladdon/com.opcoach.genmodeladdon.sample/model/project.genmodel");
		String s = GenerateCommon.getProjectNameFromURI(u);
		assertEquals("Project name must be " + PROJECT_SAMPLE_NAME, PROJECT_SAMPLE_NAME, s);
	}
	
	
	@Test
	public void testGetProjectNameFromWindowsFileURI()
	{
		URI u = URI.createURI("file:/C:/Users/olivier/Documents/OPCoach/workspaces/junit-genmodeladdon/com.opcoach.genmodeladdon.sample/model/project.genmodel");
		String s = GenerateCommon.getProjectNameFromURI(u);
		assertEquals("Project name must be " + PROJECT_SAMPLE_NAME, PROJECT_SAMPLE_NAME, s);
	}

	@Test
	public void testGetProjectNameFromWindowsPathURI()
	{
		URI u = URI.createURI("C:/Users/olivier/Documents/OPCoach/workspaces/junit-genmodeladdon/com.opcoach.genmodeladdon.sample/model/project.genmodel");
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
	

}
