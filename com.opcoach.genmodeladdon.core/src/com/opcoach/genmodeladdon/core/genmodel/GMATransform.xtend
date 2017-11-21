package com.opcoach.genmodeladdon.core.genmodel

import com.opcoach.genmodeladdon.core.GMAConstants
import com.opcoach.genmodeladdon.core.GenerateCommon
import java.text.MessageFormat
import java.util.Map
import java.util.TreeMap
import java.util.function.Consumer
import org.eclipse.emf.codegen.ecore.genmodel.GenBase
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EPackage

/** This class computes the new names for generated classes according to pattern name matching */
class GMATransform implements GMAConstants {

// The map of devnames : key = implementation name, value = devName
	protected Map<String, String> devNames = new TreeMap<String, String>()

	String devInterfaceNamePattern = ADVISED_DEV_INTERFACE_PATTERN
	String genInterfaceNamePattern = ADVISED_GEN_INTERFACE_PATTERN
	String devClassNamePattern = ADVISED_DEV_CLASS_IMPL_PATTERN
	String genClassNamePattern = ADVISED_GEN_CLASS_IMPL_PATTERN

	GenModel gm
	boolean isInit = false

	new(GenModel gm) {
		this(gm, true)
	}

	private new(GenModel gm, boolean mustInitNames) {

		this.gm = gm

		// Try to get the dev interface and class values set in properties
		if (mustInitNames) {
			initNames
		}
	}

	new(GenModel gm, String devIntNamePattern, String genIntNamePattern, String devClassNamePattern,
		String genClassNamePattern) {

		this(gm, false)

		this.devInterfaceNamePattern = devIntNamePattern
		this.genInterfaceNamePattern = genIntNamePattern
		this.devClassNamePattern = devClassNamePattern
		this.genClassNamePattern = genClassNamePattern

	}

// Set the value in the consumer if not null
	private def initValue(String v, Consumer<String> consumer) {
		if (v !== null)
			consumer.accept(v)
	}

	private def initNames() {
		initValue(gm.classNamePattern, [v|this.genClassNamePattern = v])
		initValue(gm.interfaceNamePattern, [v|this.genInterfaceNamePattern = v])

		val f = GenerateCommon.getModelFile(gm)
		if (f !== null) {
			initValue(GenerateCommon.getProperty(f, PROP_CLASS_PATTERN), [v|devClassNamePattern = v])
			initValue(GenerateCommon.getProperty(f, PROP_INTERFACE_PATTERN), [v|devInterfaceNamePattern = v])
		}
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
		for (c : p.getEClassifiers()) {
			if ((c instanceof EClass) && !c.name.endsWith("Package")) {
				val devIntName = MessageFormat.format(devInterfaceNamePattern, c.name)
				val genIntName = MessageFormat.format(genInterfaceNamePattern, c.name)
				// System.out.println("Put : " + genIntName + "," + devIntName);
				devNames.put(genIntName, devIntName)

				val genClassName = MessageFormat.format(genClassNamePattern, c.name)
				val devClassName = MessageFormat.format(devClassNamePattern, c.name)
				// System.out.println("Put : " + genClassName + "," + devClassName);
				devNames.put(genClassName, devClassName)
			}
		}
		for (EPackage childPackage : p.getESubpackages())
			computeNames(childPackage)
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
			    s = res.indexOf(key,s)
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
	
	def static void main(String[] args) {
		println("pour . " + Character.isLetterOrDigit('.') )
		println("pour < " + Character.isLetterOrDigit('<') )
		println("pour > " + Character.isLetterOrDigit('>') )
	}

	/** Transform a String with default computed names with the new names */
	static def replaceDevName(GenBase base, String stringToTranslate) {
		// Check it base has a GenModelImpl with its own devtransform
		val genModel = base.genModel
		var GMATransform dt = null
		if (genModel instanceof GMAGenModelImpl) {
			val gm = genModel as GMAGenModelImpl
			dt = gm.GMATransform
		}

		if (dt !== null)
			dt.replaceDevName(stringToTranslate)
		else
			return stringToTranslate
	}

}
