package com.opcoach.genmodeladdon.core.genmodel

import java.text.MessageFormat
import java.util.Map
import java.util.TreeMap
import org.eclipse.emf.codegen.ecore.genmodel.GenBase
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EPackage

/** This class computes the new names for generated classes according to pattern name matching */
class GMATransform {

	// The map of devnames : key = implementation name, value = devName
	protected Map<String, String> devNames
	String devInterfaceNamePattern
	String genInterfaceNamePattern
	String devClassNamePattern
	String genClassNamePattern
	GenModel gm 
	boolean isInit = false

	new (GenModel gm)
	{
		this(gm, "{0}", "M{0}", "{0}Impl", "M{0}Impl")
		
	}
	new(GenModel gm, String devInterfaceNamePattern, String genInterfaceNamePattern, String devClassNamePattern,
		String genClassNamePattern) {
		super()
		this.devInterfaceNamePattern = devInterfaceNamePattern
		this.genInterfaceNamePattern = genInterfaceNamePattern
		this.devClassNamePattern = devClassNamePattern
		this.genClassNamePattern = genClassNamePattern
		this.gm = gm
		devNames = new TreeMap<String, String>()

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
	
	def clear()
	{
		devNames.clear
		isInit = false
	}

	def void computeNames(EPackage p) {
		for (c : p.getEClassifiers()) {
			if ((c instanceof EClass) && !c.name.endsWith("Package")) {
				val devIntName = MessageFormat.format(devInterfaceNamePattern, c.getName())
				val genIntName = MessageFormat.format(genInterfaceNamePattern, c.getName())
				// System.out.println("Put : " + genIntName + "," + devIntName);
				devNames.put(genIntName, devIntName)

				val genClassName = MessageFormat.format(genClassNamePattern, c.getName())
				val devClassName = MessageFormat.format(devClassNamePattern, c.getName())
				// System.out.println("Put : " + genClassName + "," + devClassName);
				devNames.put(genClassName, devClassName)
			}
		}
		for (EPackage childPackage : p.getESubpackages())
			computeNames(childPackage)
	}

	def replaceDevName(String stringToTranslate) {
		var res = stringToTranslate
		// Search for the genName string in stringToTransalte
		for (String key : devNames.keySet()) {
			if (stringToTranslate.contains(key)) {
				// System.out.println("String : " + stringToTranslate + " contains " + key + " will replace with " + devNames.get(key) );
				res = res.replaceAll(key, devNames.get(key))
			}
		}

		return res
	}
	
	/** Transform a String with default computed names with the new names */
	static def replaceDevName(GenBase base, String stringToTranslate)
	{
		// Check it base has a GenModelImpl with its own devtransform
		val genModel = base.genModel
		var GMATransform dt = null
		if (genModel instanceof GMAGenModelImpl)
		{
			val gm = genModel as GMAGenModelImpl
			dt = gm.GMATransform
		}
		   
		if (dt !== null)
		{
		    val after = dt.replaceDevName(stringToTranslate)
		    return after
		    }
		 else 
		    return stringToTranslate
	}
}
