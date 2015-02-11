package com.opcoach.genmodeladdon.core

import java.io.File
import java.io.FileWriter
import java.util.HashMap
import java.util.Map
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage

class GenerateDevStructure {

	String classPattern
	String interfacePattern
	String srcDevDirectory

	String projectName
	GenModel genModel
	public Map<String, Object> filesNotGenerated = new HashMap()

	/** Build the generator with 2 parameters
	 * @param cpattern : the class name pattern used for generation ({0}Impl for instance)
	 * @param ipattern : the interface name pattern used for generation ({0} for instance)
	 */
	new(GenModel gm, String cPattern, String iPattern, String srcDir) {
		genModel = gm
		classPattern = cPattern
		interfacePattern = iPattern
		srcDevDirectory = srcDir
		projectName = gm.extractProjectName

		// Reset the files not generated... (they are kept to ask if they must override existing files)
		filesNotGenerated.clear
	}

	new(GenModel gm) {
		this(gm, "{0}ExtImpl", "{0}Ext", "src")
	}

	def generateDevStructure() {
		for (p : genModel.genPackages) {
			generateDevStructure(p)
		}
	}

	def generateDevStructure(GenPackage gp) {

		val root = ResourcesPlugin.workspace.root
		val proj = root.getProject(projectName)
		val srcFolder = proj.getFolder(srcDevDirectory + "/" + gp.computePackageNameForClasses.replace(".", "/"))
		val srcAbsolutePath = srcFolder.location.toOSString + "/"
		val f = new File(srcAbsolutePath)
		if(!f.exists) f.mkdirs

		val interfaceFolder = proj.getFolder(
			srcDevDirectory + "/" + gp.computePackageNameForInterfaces.replace(".", "/"))
		val interfaceAbsolutePath = interfaceFolder.location.toOSString + "/"
		val f2 = new File(interfaceAbsolutePath)
		if(!f2.exists) f.mkdirs

		println("Generate classes in    : " + srcAbsolutePath)
		println("Generate interfaces in : " + interfaceAbsolutePath)

		for (c : gp.genClasses.filter[!isDynamic]) {
				generateOverriddenClass(c, srcAbsolutePath)
				generateOverriddenInterface(c, interfaceAbsolutePath)
		}

		// Generate factory interface and implementation
		gp.generateOverriddenFactoryInterface(interfaceAbsolutePath)
		gp.generateOverriddenFactoryClass(srcAbsolutePath)

		proj.refreshLocal(IResource.DEPTH_INFINITE, null)

		// Add the factory override extension
		val gfoe = new GenerateFactoryOverrideExtension(projectName)
		gfoe.generateOverideExtension(gp.getEcorePackage().nsURI,
			gp.computePackageNameForClasses + "." + gp.computeFactoryClassName)
	}

	def generateOverriddenFactoryInterface(GenPackage gp, String path) {
		val filename = path + gp.computeFactoryInterfaceName + ".java"
		generateFile(filename, gp.generateInterfaceFactoryContent)
	}

	def generateOverriddenFactoryClass(GenPackage gp, String path) {
		val filename = path + gp.computeFactoryClassName + ".java"
		generateFile(filename, gp.generateClassFactoryContent)
	}

	def generateOverriddenClass(GenClass gc, String path) {

		generateFile(path + gc.computeClassname + ".java", gc.generateClassContent)
	}

	def generateOverriddenInterface(GenClass gc, String path) {

		generateFile(path + gc.computeInterfaceName + ".java", gc.generateInterfaceContent)
	}

	// Generate the file only if it does not exists.. If it exists keep the content and ask confirmation later
	def generateFile(String filename, Object contents) {

		// Check if file already exists...
		val f = new File(filename)
		if (f.exists()) {
			filesNotGenerated.put(filename, contents)
		} else {

			// Open the file and generate contents
			val fw = new FileWriter(filename)
			fw.write(contents.toString)
			fw.flush
			fw.close
		}
	}

	def getSrcAbsolutePath() {
		val root = ResourcesPlugin.workspace.root
		val proj = root.getProject(projectName)
		val srcFolder = proj.getFolder(srcDevDirectory + "/")
		srcFolder.location.toOSString + "/"
	}

	def generateClassContent(GenClass gc) '''
		package «gc.genPackage.computePackageNameForClasses»;
		
		import «gc.genPackage.computePackageNameForInterfaces».«gc.computeInterfaceName»;
		
		// This class can override the generated class and will be instantiated by factory
		public class «gc.computeClassname» extends «gc.computeGeneratedClassName()» implements «gc.computeInterfaceName»
		{
		
		}
	'''

	def generateInterfaceContent(GenClass gc) '''
		package «gc.genPackage.computePackageNameForInterfaces»;
		
		// This interface can override the generated interface and will be returned by factory
		public interface «gc.computeInterfaceName» extends «gc.computeGeneratedInterfaceName()»
		{
		
		}
	'''

	def generateInterfaceFactoryContent(GenPackage gp) '''
		package «gp.computePackageNameForInterfaces»;
		
		/** This factory  overrides the generated factory and returns the new generated interfaces */
		public interface «gp.computeFactoryInterfaceName» extends «gp.computeGeneratedFactoryInterfaceName» 
		{
			
			/** Provide a getInstance method to get the factory in the correct type.
			  * The eINSTANCE has been overridden with the correct type declared 
			  * in the override_factory extension 
			*/
			public static «gp.computeFactoryInterfaceName» getInstance() { return («gp.computeFactoryInterfaceName») eINSTANCE; }
			
			«FOR gc : gp.genClasses.filter[!isDynamic]»
				«gc.generateFactoryDef»
			«ENDFOR»
		}
	'''

	def generateFactoryDef(GenClass gc) '''
		public «gc.computeInterfaceName» create«gc.ecoreClass.name»();
	'''

	def generateClassFactoryContent(GenPackage gp) '''
		package «gp.computePackageNameForClasses»;
		
		«FOR gc : gp.genClasses.filter[!isDynamic]»
		import «gp.computePackageNameForInterfaces».«gc.computeInterfaceName»;
		«ENDFOR»
		import «gp.computePackageNameForInterfaces».«gp.computeFactoryInterfaceName»;
		
		// This factory  overrides the generated factory and returns the new generated interfaces
		public class «gp.computeFactoryClassName» extends «gp.computeGeneratedFactoryClassName» implements «gp.
			computeFactoryInterfaceName»
		{
			«FOR gc : gp.genClasses.filter[!isDynamic]»
				«gc.generateCreateMethod»
			«ENDFOR»
		}
	'''

	def generateCreateMethod(GenClass gc) '''
		public «gc.computeInterfaceName» create«gc.ecoreClass.name»()
		{
			«gc.computeInterfaceName» result = new «gc.computeClassname»();
			return result;
		}
	'''

	/** Compute the class name to be generated */
	def computeClassname(GenClass gc) {
		classPattern.replace("{0}", gc.ecoreClass.name)
	}

	/** Compute the interface name to be generated */
	def computeInterfaceName(GenClass gc) {

		interfacePattern.replace("{0}", gc.ecoreClass.name)
	}

	/** Compute the factory interface name to be generated */
	def computeFactoryInterfaceName(GenPackage gp) {
		gp.prefix + "Factory"
	}

	/** Compute the factory class name to be generated */
	def computeFactoryClassName(GenPackage gp) {
		gp.prefix + classPattern.replace("{0}", "Factory")
	}

	/** Compute the package name for class */
	def computePackageNameForClasses(GenPackage gp) {
		val basePackage = if(gp.basePackage == null) "" else gp.basePackage + "."
		val packSuffix = if(gp.classPackageSuffix == null) "" else "." + gp.classPackageSuffix
		basePackage + gp.packageName.toLowerCase + packSuffix
	}

	/** Compute the package name for interfaces */
	def computePackageNameForInterfaces(GenPackage gp) {
		val basePackage = if(gp.basePackage == null) "" else gp.basePackage + "."
		val intSuffix = if (gp.interfacePackageSuffix == null || gp.interfacePackageSuffix.length == 0)
				""
			else
				"." + gp.interfacePackageSuffix

		basePackage + gp.packageName.toLowerCase + intSuffix
	}

	/** Compute the generated class name depending on classpattern. */
	def computeGeneratedClassName(GenClass c) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val classPattern = c.genPackage.genModel.classNamePattern

		if (classPattern != null)
			classPattern.replace("{0}", c.ecoreClass.name)
		else
			c.ecoreClass.name + "Impl"
	}

	/** Compute the generated interface name depending on interfacePattern. */
	def computeGeneratedInterfaceName(GenClass c) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val interfaceNamePattern = c.genPackage.genModel.interfaceNamePattern

		if (interfaceNamePattern != null)
			interfaceNamePattern.replace("{0}", c.ecoreClass.name)
		else
			c.ecoreClass.name
	}

	/** Compute the generated factory class name depending on classpattern. */
	def computeGeneratedFactoryClassName(GenPackage gp) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val classPattern = gp.genModel.classNamePattern

		if (classPattern != null)
			classPattern.replace("{0}", gp.packageName.toFirstUpper + "Factory")
		else
			gp.packageName.toFirstUpper + "FactoryImpl"
	}

	/** Compute the generated factory interface name depending on interface. */
	def computeGeneratedFactoryInterfaceName(GenPackage gp) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val interfacePattern = gp.genModel.interfaceNamePattern

		if (interfacePattern != null)
			interfacePattern.replace("{0}", gp.packageName.toFirstUpper + "Factory")
		else
			gp.packageName.toFirstUpper + "Factory"
	}

	private def extractProjectName(GenModel gm) {
		println("URI of genmodel : " + gm.eResource.URI)
		val genModelUri = gm.eResource.URI.toString
		val nameStartingWithProjectName = genModelUri.replace("platform:/resource/", "")
		val pos = nameStartingWithProjectName.indexOf("/")
		return nameStartingWithProjectName.substring(0, pos)
	}

}
