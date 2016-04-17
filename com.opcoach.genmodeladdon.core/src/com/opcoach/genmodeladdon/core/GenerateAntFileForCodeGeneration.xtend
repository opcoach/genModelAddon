package com.opcoach.genmodeladdon.core

import java.io.File
import java.io.FileWriter
import java.io.IOException
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.CoreException

class GenerateAntFileForCodeGeneration {

	public final static String ANT_FILENAME = "generateEMFCode.xml";

	private def generateAntFileContent(String modelDir, String modelName) '''
<?xml version="1.0" encoding="UTF-8"?>

<!--  Dont forget to launch this ant file in the same JRE than your Eclipse -->

<project name="project" default="generateCode">
	<description> Build the javacode from ecore model   </description>
	<target name="generateCode" description="description">
		<emf.Ecore2Java genModel="«modelDir»/«modelName».genmodel" 
			model="«modelDir»/«modelName».ecore" 
			generatemodelproject="true" 
			generateeditorproject="false" 
			generateeditproject="false" 
			reconcilegenmodel="reload" />
	</target>
</project>
	'''

	def File getAntFile(IProject proj) {
		return getAntFile(proj, ANT_FILENAME)
	}

	def File getAntFile(IProject proj, String antFileName) {
		val location = proj.getLocation()
		val srcAbsolutePath = location.toOSString() + File.separator + antFileName;
		val f = new File(srcAbsolutePath);
		return f;
	}

	def File generateAntFile(String modelDir, String modelName, IProject proj) throws IOException, CoreException {
		return generateAntFile(modelDir, modelName, proj, ANT_FILENAME)

	}

	def generateAntFile(String modelDir, String modelName, IProject proj, String antFileName) throws IOException, CoreException {
		val f = getAntFile(proj, antFileName);

		if (!f.exists)
			f.createNewFile;
		val fw = new FileWriter(f);
		fw.write(generateAntFileContent(modelDir, modelName).toString);
		fw.flush;
		fw.close;

		// Add a refresh
		proj.refreshLocal(IResource.DEPTH_ONE, null);

		return f

	}

}
