package com.opcoach.genmodeladdon.core

import java.io.File
import java.io.FileWriter
import java.io.IOException
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.CoreException

class GenerateAntFileForCodeGeneration {

	public final static String ANT_FILENAME = "generateEMFCode.xml";

	def generateAntFileContent(String modelName) '''
<?xml version="1.0" encoding="UTF-8"?>

<!--  Dont forget to launch this ant file in the same JRE than your Eclipse -->

<project name="project" default="generateCode">
	<description> Build the javacode from ecore model   </description>
	<target name="generateCode" description="description">
		<emf.Ecore2Java genModel="model/«modelName».genmodel" 
			model="model/«modelName».ecore" 
			generatemodelproject="true" 
			generateeditorproject="false" 
			generateeditproject="false" 
			reconcilegenmodel="reload" />
	</target>
</project>
	'''

	def File getAntFile(IProject proj) {
		val location = proj.getLocation()
		val srcAbsolutePath = location.toOSString() + File.separator + ANT_FILENAME;
		val f = new File(srcAbsolutePath);
		return f;
	}

	def generateAntFile(String modelName, IProject proj) throws IOException, CoreException {
		val f = getAntFile(proj);

		if (!f.exists)
			f.createNewFile;
		val fw = new FileWriter(f);
		fw.write(generateAntFileContent(modelName).toString);
		fw.flush;
		fw.close;

		// Add a refresh
		proj.refreshLocal(IResource.DEPTH_ONE, null);
		
		return f
		
	}

}
