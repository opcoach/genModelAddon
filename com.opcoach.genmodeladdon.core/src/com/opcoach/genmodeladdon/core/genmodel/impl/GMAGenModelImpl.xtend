package com.opcoach.genmodeladdon.core.genmodel.impl

import com.opcoach.genmodeladdon.core.GMAConstants
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel
import com.opcoach.genmodeladdon.core.genmodel.GMAImportManager
import com.opcoach.genmodeladdon.core.genmodel.GMATransform
import java.io.FileReader
import java.io.FileWriter
import java.net.URL
import java.util.Properties
import org.eclipse.core.runtime.FileLocator
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelImpl
import org.eclipse.emf.codegen.util.ImportManager

class GMAGenModelImpl extends GenModelImpl implements GMAGenModel, GMAConstants {

	GMATransform gmaTransform = null

// Generation properties (stored in a file)
	Properties properties
// Name of property file
	String propertyFileName = null

	String cPattern
	String iPattern
	String srcDir
	Boolean genEMFCode

	override GMATransform getGMATransform() {

		if (gmaTransform === null) {
			gmaTransform = new GMATransform(this)
			gmaTransform.init()

			// Must also set it on all subGenModels and dependencies.. (code like super.setImportManager)
			// We also need to set it on any GenModels holding any used or static packages that may be refered to.
			//
			for (GenPackage genPackage : getUsedGenPackages()) {
				val gm = genPackage.getGenModel()
				if (gm instanceof GMAGenModelImpl)
					gm.setGMATransform(gmaTransform);
			}

			for (GenPackage genPackage : getStaticGenPackages()) {
				val gm = genPackage.getGenModel()
				if (gm instanceof GMAGenModelImpl)
					gm.setGMATransform(gmaTransform);
			}

			// And we need to set it on any cached GenModels holding the special Ecore and XML packages.
			//
			val ecore = getEcoreGenPackage();
			if (ecore !== null && ecore.getGenModel() instanceof GMAGenModelImpl) {
				val gm = ecore.getGenModel() as GMAGenModelImpl
				if (gm.GMATransform !== gmaTransform)
					gm.setGMATransform(gmaTransform);
			}

			val xmlType = getXMLTypeGenPackage();
			if (xmlType !== null && xmlType.getGenModel() instanceof GMAGenModelImpl) {
				val gm = xmlType.getGenModel() as GMAGenModelImpl
				if (gm.GMATransform !== gmaTransform)
					gm.setGMATransform(gmaTransform);
			}

			val xmlNamespace = getXMLNamespaceGenPackage();
			if (xmlNamespace !== null && xmlNamespace.getGenModel() instanceof GMAGenModelImpl) {
				val gm = xmlNamespace.getGenModel() as GMAGenModelImpl
				if (gm.GMATransform !== gmaTransform)
					gm.setGMATransform(gmaTransform);
			}

		}

		return gmaTransform

	}

	def setGMATransform(GMATransform gmat) {
		gmaTransform = gmat
	}

	override emitSortedImports() {
		super.emitSortedImports()
	}

	override setImportManager(ImportManager im) {
		if (im === null)
			return;

		if (getImportManager() !== im) {
			if (im instanceof GMAImportManager)
				super.setImportManager(im)
			else {
				val delegatedImportManager = new GMAImportManager(im, this, "")
				super.setImportManager(delegatedImportManager)
			}

		}

	}

	override getImportedName(String qualifiedName) {
		val pname = super.getImportedName(qualifiedName)
		val result = GMATransform.replaceDevName(pname)
		if (result != pname)
			println("--->> genModelImportedName convert " + pname + " into " + result)
		result
	}

	override setDevClassPattern(String cpattern) {
		this.cPattern = cpattern
		// Must store this value in properties
		setProperty(PROP_DEV_CLASS_PATTERN, cpattern)
	}

	override setDevInterfacePattern(String ipattern) {
		this.iPattern = ipattern
		setProperty(PROP_DEV_INTERFACE_PATTERN, ipattern)

	}

	override getDevClassPattern() {

		if (cPattern === null) {
			// Try to get it from properties
			cPattern = getProperty(PROP_DEV_CLASS_PATTERN)
			if (cPattern === null) {
				// If this is still null, return default or advised value depending on current genclasspattern
				cPattern = (classNamePattern === null || 
					DEFAULT_GEN_CLASS_IMPL_PATTERN.equals(classNamePattern)) ? DEFAULT_SRC_CLASS_IMPL_PATTERN : ADVISED_DEV_CLASS_IMPL_PATTERN
			}
		}
		cPattern
	}

	override getDevInterfacePattern() {
 		if (iPattern === null) {
			// Try to get it from properties
			iPattern = getProperty(PROP_DEV_INTERFACE_PATTERN)
			if (iPattern === null) {
				// If this is still null, return default or advised value depending on current geninterfacepattern
				iPattern = (interfaceNamePattern === null || 
					DEFAULT_GEN_INTERFACE_PATTERN.equals(interfaceNamePattern)) ? DEFAULT_SRC_INTERFACE_PATTERN : ADVISED_DEV_INTERFACE_PATTERN
			}
		}
		iPattern

	}

// Fix issue 84 : https://github.com/opcoach/genModelAddon/issues/84
// Eclipse's Properties are not usable for this project, because properties must be read either from 
// a selection in the workspace OR from an absolute path (when the ant task is called)
// So there must be a property file beside the model file containing the generation properties, 
// so as to be sure to use them with the ant file... 
// Find the properties for a given file ... It get the files named genmodel.gma
	def readProperties() {
		if (properties === null) {

			properties = new Properties
			try {
				val fr = new FileReader(propertyFilename)
				properties.load(fr)
			} catch (Exception e) {
				// If no properties, return empty properties...
			}
		}

	}

	def saveProperties() {
		val comment = ''' This file contains the parameters for genModelAddon generation (gma) 
 It is automaticaly generated when gma is used
 Store this file in your scm repository (git, svn ...)'''

		val fw = new FileWriter(propertyFilename)
		properties.store(fw, comment)
	}

	def getPropertyFilename() {
		if (propertyFileName === null) {

			if (eResource !== null) {
				val gmafn = eResource.URI + GMA_EXT
				if (gmafn.startsWith("file:"))
				    propertyFileName = gmafn.replaceFirst("file:", "")
				    else if (gmafn.startsWith("platform:/resource/"))
				    {
				    	val u = FileLocator.resolve(new URL(gmafn))
				    	propertyFileName = u.toString.replaceFirst("file:", "")
				    }
				// println("gma properties file " + propertyFileName)
			}
		}
		return propertyFileName
	}

	def private String getProperty(String qn) {
		readProperties();
		val v = properties.get(qn)
		(v !== null) ? v.toString : null
	}

	def private void setProperty(String qn, String value) {
		readProperties()

		properties.setProperty(qn, value)
		saveProperties()
	}

	override setSrcDir(String dir) {
		srcDir = dir
		setProperty(PROP_SRCDIR, dir)
	}

	override setGenerateEMFCode(boolean gen) {
		genEMFCode = gen
		setProperty(PROP_GENEMFCODE, gen.toString)
	}

	override getSrcDir() {
		if (srcDir === null) {
			srcDir = getProperty(PROP_SRCDIR)
			if (srcDir === null)
				srcDir = DEFAULT_SRC_DEV
		}
		return srcDir
	}

	override mustGenerateEMF() {
		if (genEMFCode === null) {
			genEMFCode = new Boolean(getProperty(PROP_GENEMFCODE))
		}
		return genEMFCode.booleanValue
	}

}
