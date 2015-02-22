package com.opcoach.genmodeladdon.core

class GenerateAntFileForCodeGeneration {


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

}
