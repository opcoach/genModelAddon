package com.opcoach.genmodeladdon.core.genmodel

import com.opcoach.genmodeladdon.core.GMAConstants
import com.opcoach.genmodeladdon.core.GenerateCommon
import java.text.MessageFormat
import java.util.Map
import java.util.TreeMap
import org.eclipse.emf.codegen.ecore.genmodel.GenBase
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EcorePackage
import com.opcoach.genmodeladdon.core.GMAHelper

/** This class computes the new names for generated classes according to pattern name matching */
class GMATransform implements GMAConstants {

// The map of devnames : key = implementation name, value = devName
	protected Map<String, String> devNames = new TreeMap<String, String>()

	String devInterfaceNamePattern = DEFAULT_SRC_INTERFACE_PATTERN
	String genInterfaceNamePattern = DEFAULT_GEN_INTERFACE_PATTERN
	String devClassNamePattern = DEFAULT_SRC_CLASS_IMPL_PATTERN
	String genClassNamePattern = DEFAULT_GEN_CLASS_IMPL_PATTERN

	GMAGenModel gm
	boolean isInit = false

	new(GMAGenModel gm) {
		this.gm = gm

		// Try to get the dev interface and class values set in properties
		initPatterns
	}


	private def initPatterns() {

		genClassNamePattern = (gm.classNamePattern === null) ? DEFAULT_GEN_CLASS_IMPL_PATTERN : gm.classNamePattern
		genInterfaceNamePattern = (gm.interfaceNamePattern === null)
			? DEFAULT_GEN_INTERFACE_PATTERN
			: gm.interfaceNamePattern

		devClassNamePattern = gm.devClassPattern
		devInterfaceNamePattern = gm.devInterfacePattern

		println(this)

	}

	override synchronized toString() {
		val title = gm.genModel.eResource === null ? gm.genModel.toString : gm.genModel.eResource.URI
		println("GmaTransform for : " + title)
		println("   -> genClassNamePattern : " + genClassNamePattern)
		println("   -> genInterfaceNamePattern : " + genInterfaceNamePattern)
		println("   -> devClassNamePattern : " + devClassNamePattern)
		println("   -> devInterfaceNamePattern : " + devInterfaceNamePattern)
	}

	def init() {
		if (!isInit) {

			for (gp : gm.getGenPackages())
				computeNames(gp.getEcorePackage())

			for (gp : gm.getUsedGenPackages())
				computeNames(gp.getEcorePackage())

			isInit = true
		}
	}

	def clear() {
		devNames.clear
		isInit = false
	}

	def void computeNames(EPackage p) {
		// Do not compute names for emf2002Ecore
		if (!EcorePackage::eNS_URI.equals(p.nsURI)) {
			for (c : p.getEClassifiers()) {
				if ((c instanceof EClass) && !c.name.endsWith("Package") && !GenerateCommon.isMapType(c)) {
					val devIntName = MessageFormat.format(devInterfaceNamePattern, c.name)
					val genIntName = MessageFormat.format(genInterfaceNamePattern, c.name)
					// println(
					// "Put : " + genIntName + "," + devIntName + " (used format : " + devInterfaceNamePattern + ")");
					devNames.put(genIntName, devIntName)

					val genClassName = MessageFormat.format(genClassNamePattern, c.name)
					val devClassName = MessageFormat.format(devClassNamePattern, c.name)
					// println("Put : " + genClassName + "," + devClassName);
					devNames.put(genClassName, devClassName)
				}
			}
			for (EPackage childPackage : p.getESubpackages())
				computeNames(childPackage)
		}

	}

	/**
	 * Replace the development name (a key in the map) as many times as necessary. 
	 * But it must be replaced only if the key is found with not a letter before or after. 
	 *  Examples :
	 * <blockquote>     public class MProject  -->   public class Project  </blockquote>
	 *   <blockquote>   public class MyMProject extends MProject  --> public class MyMProject extends Project </blockquote>
	 *   <blockquote>   public class MyMProject extends MProject implements YourMProject  --> public class MyMProject extends Project implements YourMProject  </blockquote>
	 * 
	 */
	def replaceDevName(String stringToTranslate) {
		var res = new StringBuffer(stringToTranslate) // Current copy recreated in the loop
		for (entry : devNames.entrySet()) {

			val key = entry.key
			val value = entry.value

			var s = 0
			while (res.indexOf(key, s) != -1) {
				s = res.indexOf(key, s)
				val e = s + key.length
				// Must change the string only if it is a real single word : 
				// of if it is at the beginning or at the end of the string.
				val startIsOk = (s == 0) || !Character.isLetterOrDigit(res.charAt(s - 1))
				val endIsOk = (e == res.length) || !Character.isLetterOrDigit(res.charAt(e))

				if (startIsOk && endIsOk)
					res = res.replace(s, e, value)

				// Must continue so search after the end...
				s = e
			}

		} // end for entry
		return res.toString
	}

	/** Transform a String with default computed names with the new names */
	static def replaceDevName(GenBase base, String stringToTranslate) {
		// Do not translate anything if project is not compliant with GMA
		if(!GMAHelper.GMACompliant(base))
			return stringToTranslate
		// Check it base has a GenModelImpl with its own devtransform
		val genModel = base.genModel
		var GMATransform dt = null
		if (genModel instanceof GMAGenModel) {
			val gm = genModel as GMAGenModel
			dt = gm.GMATransform
		}

		if (dt !== null)
			dt.replaceDevName(stringToTranslate)
		else
			return stringToTranslate
	}

}
