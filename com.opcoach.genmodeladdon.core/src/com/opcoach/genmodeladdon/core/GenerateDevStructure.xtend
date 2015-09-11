package com.opcoach.genmodeladdon.core

import java.io.File
import java.io.FileWriter
import java.util.ArrayList
import java.util.HashMap
import java.util.Map
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage
import org.eclipse.jdt.core.IClasspathEntry
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.JavaCore
import org.eclipse.core.runtime.NullProgressMonitor

class GenerateDevStructure {

	String classPattern
	String interfacePattern
	String srcDevDirectory
	var generateFiles = false

	String projectName
	GenModel genModel
	public Map<String, Object> filesNotGenerated = new HashMap()

	/** Build the generator with 2 parameters
	 * @param cpattern : the class name pattern used for generation ({0}Impl for instance)
	 * @param ipattern : the interface name pattern used for generation ({0} for instance)
	 * @param srcDir : the source directory (relative path) in project
	 */
	new(GenModel gm, String cPattern, String iPattern, String srcDir) {
		this(gm, cPattern, iPattern, srcDir, gm.extractProjectName)

	}

	/** Build the generator with 4 parameters
	 * @param cpattern : the class name pattern used for generation ({0}Impl for instance)
	 * @param ipattern : the interface name pattern used for generation ({0} for instance)
	 * @param srcDir : the source directory (relative path) in project
	 * @param pname : the project name.
	 */
	new(GenModel gm, String cPattern, String iPattern, String srcDir, String pName) {
		genModel = gm
		classPattern = cPattern
		interfacePattern = iPattern
		srcDevDirectory = srcDir
		projectName = pName

		// Reset the files not generated... (they are kept to ask if they must override existing files)
		filesNotGenerated.clear
	}

	new(GenModel gm) {
		this(gm, "{0}ExtImpl", "{0}Ext", "src")
	}

	/** Generate the file structure. If genFiles is false just compute the files to be generated */
	def generateDevStructure(boolean genFiles) {
		generateFiles = genFiles
		for (p : genModel.genPackages) {
			p.generateDevStructure()

		}
	}

	def void generateDevStructure(GenPackage gp) {

		val root = ResourcesPlugin.workspace.root
		val proj = root.getProject(projectName)

		// Add the srcDir as source folder if it is not yet the case
		setFolderAsSourceFolder(proj, srcDevDirectory)

		// Then create inside the package directory if not exists
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

		// Generate  package interface (used to have a dev interface compliant with generated code)
		gp.generateOverriddenPackageInterface(interfaceAbsolutePath)

		proj.refreshLocal(IResource.DEPTH_INFINITE, null)

		// Add the factory override extension
		val gfoe = new GenerateFactoryOverrideExtension(projectName)
		gfoe.generateOverideExtension(gp.getEcorePackage().nsURI,
			gp.computePackageNameForClasses + "." + gp.computeFactoryClassName)

		// Iterate on subpackages 
		for (sp : gp.subGenPackages)
			sp.generateDevStructure()
	}

	/** add the srcDir as a source directory in the java project, if it is not yet added */
	def setFolderAsSourceFolder(IProject proj, String srcDir) {
		val expectedSrcDir = "/" + proj.name + "/" + srcDir
		val nat = proj.getNature(JavaCore::NATURE_ID)

		if (nat instanceof IJavaProject) {
			var found = false;

			val jvp = nat as IJavaProject
			for (cpe : jvp.getResolvedClasspath(true)) {
				if (!found && expectedSrcDir.equals(cpe.path.toString())) {
					found = cpe.entryKind == IClasspathEntry::CPE_SOURCE
				}
			}

			// Add the path if not still added ! 
			if (!found) {
				val path = new Path(expectedSrcDir)
				val srcEntry = JavaCore::newSourceEntry(path)
				val newClassPath = new ArrayList<IClasspathEntry>(jvp.rawClasspath)
				newClassPath.add(srcEntry)
				jvp.setRawClasspath(newClassPath, new NullProgressMonitor);
			}
		}

	}

	def generateOverriddenFactoryInterface(GenPackage gp, String path) {
		val filename = path + gp.computeFactoryInterfaceName + ".java"
		generateFile(filename, gp.generateInterfaceFactoryContent)
	}

	def generateOverriddenFactoryClass(GenPackage gp, String path) {
		val filename = path + gp.computeFactoryClassName + ".java"
		generateFile(filename, gp.generateClassFactoryContent)
	}

	def generateOverriddenPackageInterface(GenPackage gp, String path) {
		val filename = path + gp.computePackageInterfaceName + ".java"
		generateFile(filename, gp.generateInterfacePackageContent)
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
			if (generateFiles) {

				// Open the file and generate contents
				val fw = new FileWriter(filename)
				fw.write(contents.toString)
				fw.flush
				fw.close
			}
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
		
		import «gp.computePackageNameForClasses».«gp.computeFactoryClassName»;
		
		/** This factory  overrides the generated factory and returns the new generated interfaces */
		public interface «gp.computeFactoryInterfaceName» extends «gp.computeGeneratedFactoryInterfaceName» 
		{
			
			/** Specialize the eINSTANCE initialization with the new interface type 
			  * (overridden in the override_factory extension)
			*/
			«gp.computeFactoryInterfaceName» eINSTANCE = «gp.computeFactoryClassName».init();
						
			«FOR gc : gp.genClasses.filter[!isDynamic]»
				«gc.generateFactoryDef»
			«ENDFOR»
		}
	'''

	def generateInterfacePackageContent(GenPackage gp) '''
		package «gp.computePackageNameForInterfaces»;
				
		/** This package interface extends  the generated package interface 
		    It is necessary because its name is used in the EMF generated code) 
		*/
		public interface «gp.computePackageInterfaceName» extends «gp.computeGeneratedPackageInterfaceName» 
		{
			
		}
	'''

	def generateFactoryDef(GenClass gc) '''
		public «gc.computeInterfaceName» create«gc.ecoreClass.name»();
	'''

	def generateClassFactoryContent(GenPackage gp) '''
		package «gp.computePackageNameForClasses»;
		
		import org.eclipse.emf.ecore.plugin.EcorePlugin;
		
		«FOR gc : gp.genClasses.filter[!isDynamic]»
			import «gp.computePackageNameForInterfaces».«gc.computeInterfaceName»;
		«ENDFOR»
		import «gp.computePackageNameForInterfaces».«gp.computeFactoryInterfaceName»;
		
		
		// This factory  overrides the generated factory and returns the new generated interfaces
		public class «gp.computeFactoryClassName» extends «gp.computeGeneratedFactoryClassName» implements «gp.
			computeFactoryInterfaceName»
		{
			
			public static «gp.computeFactoryInterfaceName» init() {
				
				try {
					Object fact = «gp.computeGeneratedFactoryClassName».init();
					if ((fact != null) && (fact instanceof «gp.computeFactoryInterfaceName»))
							return («gp.computeFactoryInterfaceName») fact;
					}
				catch (Exception exception) {
					EcorePlugin.INSTANCE.log(exception);
				}
				return new «gp.computeFactoryClassName»(); 
				 }
			
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

	/** Compute the factory interface name to be generated */
	def computePackageInterfaceName(GenPackage gp) {
		gp.prefix + "Package"
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

	/** Compute the generated package interface name depending on interface. */
	def computeGeneratedPackageInterfaceName(GenPackage gp) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val interfacePattern = gp.genModel.interfaceNamePattern

		if (interfacePattern != null)
			interfacePattern.replace("{0}", gp.packageName.toFirstUpper + "Package")
		else
			gp.packageName.toFirstUpper + "Package"
	}

	private static def extractProjectName(GenModel gm) {
		println("URI of genmodel : " + gm.eResource.URI)
		val genModelUri = gm.eResource.URI.toString
		val nameStartingWithProjectName = genModelUri.replace("platform:/resource/", "")
		val pos = nameStartingWithProjectName.indexOf("/")
		return nameStartingWithProjectName.substring(0, pos)

	}

}
