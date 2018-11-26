package com.opcoach.genmodeladdon.core.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;
import com.opcoach.genmodeladdon.core.genmodel.GMATransform;

public class TestGMATransform extends GenModelAddonTestCase
{
	
	
	class GMATransformStub extends GMATransform
	{

		public GMATransformStub(GMAGenModel gm)     
		{
			super(gm);
			init();
			// Add other strings to replace... just for tests.
			devNames.put("MWorkflow", "Workflow");
			devNames.put("Satellite", "DevSatellite");
			devNames.put("System", "DevSystem");
		}
	
	}
	
	 GMATransformStub gmat = null;

	@Before
	public  void setup() throws Exception
	{
		// Create the GMATransform with some additionals strings inside
		GMAGenModel gm = getGenModel(PROJECT_GENMODEL);

		gmat = new GMATransformStub(gm);
	}

	@Test
	public void testSimpleReplaceWithBlank()
	{
		String source = "public class MProject ";
		String expected = "public class Project ";
		assertEquals(expected, gmat.replaceDevName(source));
	}
	
	@Test
	public void testSimpleReplaceWithCR()
	{
		String source = "public class MProject";
		String expected = "public class Project";
		assertEquals(expected, gmat.replaceDevName(source));
	}
	
	@Test
	public void testSimpleReplaceWithNothingAround()
	{
		String source = "MProject";
		String expected = "Project";
		assertEquals(expected, gmat.replaceDevName(source));
	}
	
	@Test
	public void testInnerNoReplace()
	{
		String source = "public class MyMProject";
		String expected = source;
		assertEquals(expected, gmat.replaceDevName(source));
	}

	@Test
	public void testContinuousEnd()
	{
		String source = "public class MProject1";
		String expected = source;
		assertEquals(expected, gmat.replaceDevName(source));
	}
	
	@Test
	public void testForMethodRename()
	{
		String source = "MWorkflow getWorkflow();";
		String expected = "Workflow getWorkflow();";
		assertEquals(expected, gmat.replaceDevName(source));
	}

	
	@Test
	public void fullTestWithAllCases()
	{
		String source   = "MWorkflow MWorkflowTest setMWorkflow(MWorkflow mf) WorkflowFactory MWorkflowFactory MWorkflow .MWorkflow <MWorkflow> EList<MWorkflow> Map<MWorkflow,String> Map<MWorkflow ,String>  .MWorkflow;";
		String expected = "Workflow MWorkflowTest setMWorkflow(Workflow mf) WorkflowFactory MWorkflowFactory Workflow .Workflow <Workflow> EList<Workflow> Map<Workflow,String> Map<Workflow ,String>  .Workflow;";
		assertEquals(expected, gmat.replaceDevName(source));
	}

	@Test
	public void testWithKeyShorterThanValue()
	{
		String source   = "Satellite SatelliteTest setSatellite(Satellite s) DevSatelliteFactory SatelliteFactory System .Satellite <Satellite> EList<Satellite> Map<Satellite,String> Map<Satellite ,String>  .System;";
		String expected = "DevSatellite SatelliteTest setSatellite(DevSatellite s) DevSatelliteFactory SatelliteFactory DevSystem .DevSatellite <DevSatellite> EList<DevSatellite> Map<DevSatellite,String> Map<DevSatellite ,String>  .DevSystem;";
		assertEquals(expected, gmat.replaceDevName(source));
	}

	



}
