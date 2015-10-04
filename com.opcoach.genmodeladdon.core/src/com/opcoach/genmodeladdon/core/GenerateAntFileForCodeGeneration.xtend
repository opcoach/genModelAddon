package com.opcoach.genmodeladdon.core

import java.io.File
import java.io.FileWriter
import java.io.IOException
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.CoreException
import org.eclipse.emf.codegen.ecore.genmodel.GenModel

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

	def File getAntFile(GenModel gm) {
		val proj = GenerateCommon.getProject(gm);
		val location = proj.getLocation()
		val srcAbsolutePath = location.toOSString() + File.separator + ANT_FILENAME;
		val f = new File(srcAbsolutePath);
		return f;
	}

	def generateAntFile(GenModel gm) throws IOException, CoreException {
		val s = gm.eResource().toString();
		var pos = s.lastIndexOf(File.separator);
		var modelName = s.substring(pos + 1);
		pos = modelName.indexOf(".genmodel");
		modelName = modelName.substring(0, pos);

		val f = gm.getAntFile;

		if (!f.exists())
			f.createNewFile();
		val fw = new FileWriter(f);
		fw.write(generateAntFileContent(modelName).toString());
		fw.flush();
		fw.close();

		// Add a refresh
		val proj = GenerateCommon.getProject(gm);

		proj.refreshLocal(IResource.DEPTH_ONE, null);
		
		return f
	}

}
