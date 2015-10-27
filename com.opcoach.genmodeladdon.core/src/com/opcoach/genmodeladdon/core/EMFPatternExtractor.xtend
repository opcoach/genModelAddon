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
	static final String EMF_CODEGEN_PLUGIN_SN = "com.opcoach.genmodeladdon.core"
	static final String EMF_CODEGEN_CLASSGEN_PATH = "/templates/model/Class.javajet"
	static final String TARGET_SOURCE_PATH = "templates"
	static final String TARGET_MODEL_PATH = "model"
	static final String TARGET_CLASS_TEMPLATE_FILE = "Class.javajet"

  static final String DEV_CLASS_PATTERN = "%DEV_CLASS_PATTERN%";
  static final String DEV_INTERFACE_PATTERN = "%DEV_INTERFACE_PATTERN%";
 
	final IProject targetProject

	final String devClassPattern
	final String devInterfacePattern

	new(IProject targetProject, String cp, String ip) {
		this.targetProject = targetProject
		this.devClassPattern = cp
		this.devInterfacePattern = ip
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
		
		content = content.replaceFirst(DEV_CLASS_PATTERN, devClassPattern);
		content = content.replaceFirst(DEV_INTERFACE_PATTERN, devInterfacePattern);
		
			IOUtils.write(content, new FileOutputStream(file.location.toFile), ResourcesPlugin.getEncoding());

		}

		def createSourceDirectoryStructure() {
			if (targetProject instanceof IProject) {
				var tgtSourcePath = null as IPath
				val javaTargetProject = targetProject as IProject
				val sourcePath = javaTargetProject.getFolder(TARGET_SOURCE_PATH)
				println("The sourcePath is : "+ sourcePath)
				if (!sourcePath.exists) {
					try {
					sourcePath.create(true, true, new NullProgressMonitor())
					}
					catch (Exception e)
					{
						println("Unable to create the file :  " + sourcePath);
						e.printStackTrace
					}
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