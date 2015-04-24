package com.opcoach.genmodeladdon.core

import java.io.FileInputStream
import java.io.FileOutputStream
import org.apache.commons.io.IOUtils
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.FileLocator
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.core.runtime.Platform

class EMFPatternExtractor implements Runnable {
	static final String EMF_CODEGEN_PLUGIN_SN = "org.eclipse.emf.codegen.ecore"
	static final String EMF_CODEGEN_CLASSGEN_PATH = "/templates/model/Class.javajet"
	static final String TARGET_SOURCE_PATH = "templates"
	static final String TARGET_MODEL_PATH = "model"
	static final String TARGET_CLASS_TEMPLATE_FILE = "Class.javajet"
	static final String REPLACED = "public<%if \\(genClass\\.isAbstract\\(\\)\\) \\{%> abstract<%\\}%> class <%=genClass\\.getClassName\\(\\)%><%=genClass\\.getTypeParameters\\(\\)\\.trim\\(\\)%><%=genClass\\.getClassExtends\\(\\)%><%=genClass\\.getClassImplements\\(\\)%>"
	static final String DECLARATION_REPLACED = "final GenModel genModel=genPackage.getGenModel();"

	final IProject targetProject

	final String replacement
	
	new(IProject targetProject, String replacement) {
		this.targetProject = targetProject
		this.replacement = replacement
	}

	def extractClassTemplateIncurrentPlugin() {
		val codegenBundle = Platform.getBundle(EMF_CODEGEN_PLUGIN_SN)
		val path = new Path(EMF_CODEGEN_CLASSGEN_PATH);
		val jETClassFile = FileLocator::openStream(codegenBundle, path, false)
		jETClassFile
	}

	override run() {
		val sourceJetFile = extractClassTemplateIncurrentPlugin()
		val templateFolder = createSourceDirectoryStructure()
		val file = templateFolder.getFile(TARGET_CLASS_TEMPLATE_FILE)
		if (!file.exists()) {
			file.create(sourceJetFile, true, new NullProgressMonitor)
		}

		var content = IOUtils.toString(new FileInputStream(file.location.toFile), ResourcesPlugin.getEncoding());
		val replacementSplit = replacement.split(
			"\\{0\\}"
		)
				
		
		
		val lineReplacement = "<% final String devClassPattern= \"" + this.replacement +"\";%>\npublic<%if (genClass.isAbstract()) {%> abstract<%}%> class 
			<%=genClass.getClassName()%><%=genClass.getTypeParameters().trim()%><% if (!genClass.getClassExtends().contains(\"MinimalEObjectImpl.Container\")){%>" +
			" extends <%=devClassPattern.replaceFirst(\"\\\\\\\\{0\\\\\\\\}\",genClass.getClassExtendsGenClass().getEcoreClass().getName())%>" +
			//replacementSplit.get(0) + "<%=genClass.getClassExtends().replace(\"Impl\",\"\")%>" +
			//replacementSplit.get(1) 
			 "<%}else{%><%=genClass.getClassExtends()%><%}%><%=genClass.getClassImplements()%>"
		content = content.replaceFirst(REPLACED, lineReplacement)
		IOUtils.write(content, new FileOutputStream(file.location.toFile), ResourcesPlugin.getEncoding());

	}

	def createSourceDirectoryStructure() {
		if (targetProject instanceof IProject) {
			var tgtSourcePath = null as IPath
			val javaTargetProject = targetProject as IProject
			val sourcePath = javaTargetProject.getFolder(TARGET_SOURCE_PATH)
			if (!sourcePath.exists) {
				sourcePath.create(true, true, new NullProgressMonitor())
			}
			tgtSourcePath = sourcePath.fullPath
			if (tgtSourcePath != null) {
				val p = new Path(TARGET_SOURCE_PATH + "/" + TARGET_MODEL_PATH);
				val modelFolder = targetProject.getFolder(p)
				if (!modelFolder.exists) {
					modelFolder.create(true, true, new NullProgressMonitor())
				}
				return modelFolder
			}
			return null

		}
	}

}