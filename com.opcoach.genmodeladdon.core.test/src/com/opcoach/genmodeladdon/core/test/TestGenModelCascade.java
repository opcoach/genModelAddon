package com.opcoach.genmodeladdon.core.test;

import org.junit.Test;

/**
 * This unit test is used to check if cascaded genmodel generate the good code.
 * It is based on the fannoise model
 */
public class TestGenModelCascade extends GenModelAddonTestCase
{

	// -------------------------------------------------------------
	// ------------------- Test gen classes for generic types --
	// -------------------------------------------------------------
	@Test
	public void theFannoiseProjectMustManageWorkflow()
	{
		assertFileContains("src-gen/fannoise/MFanNoiseProject.java", "\tWorkflow getWorkflow()");

	}
	
	@Test
	public void theFannoiseProjectMustImportWorkflow()
	{
		assertFileContains("src-gen/fannoise/MFanNoiseProject.java", "import ananax.Workflow");
	}


}
