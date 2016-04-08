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
		assertFileContains("generateEMFCode.xml", "emf.Ecore2Java genModel=\"model/project.genmodel\"");
	}

	@Test
	public void antGeneratedFileMustContainEcoreModel()
	{
		assertFileContains("generateEMFCode.xml", "model=\"model/project.ecore\"");
	}

	

}
