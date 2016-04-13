package com.opcoach.genmodeladdon.core.test;

import org.junit.Test;

public class TestAntGeneration extends GenModelAddonTestCase
{


	// ----------------------------------------------------
	// ------------------- Test the ant generated file   --
	// ----------------------------------------------------
	@Test
	public void antGeneratedFileMustContainGenModel()
	{
		assertFileContains(PROJECT_ANT_FILE, "emf.Ecore2Java genModel=\"model/project.genmodel\"");
	}

	@Test
	public void antGeneratedFileMustContainEcoreModel()
	{
		assertFileContains(PROJECT_ANT_FILE, "model=\"model/project.ecore\"");
	}

	

}
