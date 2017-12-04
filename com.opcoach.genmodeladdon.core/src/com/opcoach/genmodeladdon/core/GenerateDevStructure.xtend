package com.opcoach.genmodeladdon.core

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap
import java.util.Map
import org.eclipse.ant.core.AntRunner
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.core.runtime.Platform
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage
import org.eclipse.emf.ecore.EClass
import org.eclipse.jdt.core.IClasspathEntry
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.JavaCore

/** This class is used to proceed the different steps to generate the development structure
 * A method is defined for each step :
 * generateDevStructure : generate the development structure
 * generateAntFile : generate the ant file to generate the code (usefull for automatic builder)
 * generateGenModelCode : generate the EMF code using templates (calls the ant file)
 */
class GenerateDevStructure {

	String classPattern
	String interfacePattern
	String srcDevDirectory
	var generateFiles = false
	// Current project for generation
	IProject project

	String projectName
	GenModel genModel
	var copyright = ""
	
	boolean debug = false   // Initialized with argument -gmaDebug
	
	public Map<String, Object> filesNotGenerated = new HashMap
	
	String modelName
	String modelDir
	
	// Maps to store for each modelUri the package and factory class names
	// To manage the extensions for override factory and generated package
	// Key is Uri and value is class name
	 Map<String, String> factories = new HashMap
	 Map<String, String> packages = new HashMap

	/** Build the generator with 4 parameters
	 * @param cpattern : the class name pattern used for generation ({0}Impl for instance)
	 * @param ipattern : the interface name pattern used for generation ({0} for instance)
	 * @param srcDir : the source directory (relative path) in project
	 */
	new(GenModel gm, String cPattern, String iPattern, String srcDir) {
		
		if (Platform.applicationArgs.contains(GMAConstants.PARAM_DEBUG_MODE))
		{
		  debug = true
	    }
		  
		genModel = gm
		if (gm.copyrightText !== null)
			copyright = computeCopyrightComment.toString
		classPattern = cPattern
		interfacePattern = iPattern
		srcDevDirectory = srcDir
		project = GenerateCommon.getProject(gm)
		projectName = project.name
		modelName = GenerateCommon.getModelName(gm)
		modelDir = GenerateCommon.getModelPath(gm)
		
		project.open(null)
		var status = "closed"
		if (project.isOpen) status = "closed"
		// println("Project " + projectName + " is " + status  + " when creating devStructure for " + modelName)
		

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
		
		project.refreshLocal(IResource.DEPTH_INFINITE, null)
		
		
	}

	def void generateDevStructure(GenPackage gp) {

		val root = ResourcesPlugin.workspace.root
		project = root.getProject(projectName)

		// Add the srcDir as source folder if it is not yet the case
		setFolderAsSourceFolder(project, srcDevDirectory)

		// Then create inside the package directory if not exists
		val srcFolder = project.getFolder(srcDevDirectory + "/" + gp.computePackageNameForClasses.replace(".", "/"))
		val srcAbsolutePath = srcFolder.location.toOSString + "/"
		val f = new File(srcAbsolutePath)
		if(!f.exists) f.mkdirs

		val interfaceFolder = project.getFolder(
			srcDevDirectory + "/" + gp.computePackageNameForInterfaces.replace(".", "/"))
		val interfaceAbsolutePath = interfaceFolder.location.toOSString + "/"
		val f2 = new File(interfaceAbsolutePath)
		if(!f2.exists) f.mkdirs

	//	println("Generate classes in    : " + srcAbsolutePath)
	//	println("Generate interfaces in : " + interfaceAbsolutePath)

		for (c : gp.genClasses.filter[!isDynamic].filter[p | !GenerateCommon.isMapType(p)]) {
			if (!c.isInterface)
				generateOverriddenClass(c, srcAbsolutePath) // Can still generate abstract classes
			generateOverriddenInterface(c, interfaceAbsolutePath)
		}

		// Generate factory interface and implementation
		gp.generateOverriddenFactoryInterface(interfaceAbsolutePath)
		gp.generateOverriddenFactoryClass(srcAbsolutePath)

		// Generate  package interface (used to have a dev interface compliant with generated code)
		gp.generateOverriddenPackageInterface(interfaceAbsolutePath)

		// remember of factory and package classes for this uri. 
	    val factoryClassName = gp.computePackageNameForClasses + "." + gp.computeFactoryClassName
		val packageClassName = gp.qualifiedPackageInterfaceName
		
		factories.put(gp.getEcorePackage.nsURI, factoryClassName)
		//println("Added this factory in list : " + factoryClassName)
		packages.put(gp.getEcorePackage.nsURI, packageClassName)

		// Iterate on subpackages 
		for (sp : gp.subGenPackages)
			sp.generateDevStructure
	}
	
	

	/** add the srcDir as a source directory in the java project, if it is not yet added */
	def private setFolderAsSourceFolder(IProject proj, String srcDir) {
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
				jvp.setRawClasspath(newClassPath, new NullProgressMonitor)
			}
		}

	}

	/**
	 * This method initializes the genModel with convenient values
	 */
	def public void initializeGenModelConvenientProperties() {
		
		// By default organize imports in genmodel
		genModel.importOrganizing = true
		
	}

	/** Generate the ant file and return it (or null.  */
	def generateAntFile() {
		generateAntFile(GenerateAntFileForCodeGeneration.ANT_FILENAME)
	}
	
	/** Generate the ant file and return it (or null.  */
	def generateAntFile(String antFilename) {
		// println("Generate the ant file : " + antFilename)
		refreshWorkspace
		val gen = new GenerateAntFileForCodeGeneration()
		try {
			val antFile = gen.generateAntFile(modelDir, modelName, project, antFilename)
			project.refreshLocal(1, null)
			return antFile

		} catch (IOException e) {
			e.printStackTrace
		} catch (CoreException e) {
			e.printStackTrace
		}
		return null
	}
	

	/** generate the source code using the ant generated task 
	 * @param f : the ant file to be called */
	def void generateGenModelCode(File f, IProgressMonitor monitor) {

        
		val runner = new AntRunner
		runner.setBuildFileLocation(f.absolutePath)
	
	    // Add traces during ant generation if debug mode (-gmaDebug) 
	    if (debug)
	    {
	      	runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
		    runner.arguments = "-verbose -debug"
		}
		
		try {
			if (debug)
			   println("  --> Generate the EMF Code using the ant file : " +  f.absolutePath);
			
			runner.run(monitor)
			refreshWorkspace

		} catch (CoreException e) {
			e.printStackTrace
		}
		

	}
	
	// Generate or update extensions...
	def generateExtensions()
	{
		val gfoe = new GenerateExtensions(project)
		gfoe.generateOrUpdateExtensions(factories, packages)
		
	}
	
	
	// This method do the global process in the right order 
	def generateAll(String antFilename)
	{
		// set some genModel convenient properties.
		initializeGenModelConvenientProperties()

		// Generate the dev structure...
		generateDevStructure(true)

		// Generate the ant file to generate emf code
		val antFile = generateAntFile(antFilename)

		// Once dev structure is generated and ant file too, can call it !
		generateGenModelCode(antFile, new NullProgressMonitor)

		
		// Must generate or update extensions in plugin.xml file
		// BUT AFTER THE EMF CODE GENERATION !!
		generateExtensions
		
		refreshWorkspace
	
	}

	def refreshWorkspace() {
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null)
			//println("Waiting for refresh ")
			Thread.sleep(2000);  // Wait for refresh (important). MUST NOT BE LESS THAN 2 seconds for tycho build

		} catch (CoreException e) {
			e.printStackTrace
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

		generateFile(path + gc.computeClassFilename + ".java", gc.generateClassContent)
	}

	def generateOverriddenInterface(GenClass gc, String path) {

		generateFile(path + gc.computeInterfaceFilename + ".java", gc.generateInterfaceContent)
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
		«copyright»
		package «gc.genPackage.computePackageNameForClasses»;
		
		import «gc.genPackage.computePackageNameForInterfaces».«gc.computeInterfaceFilename»;
		
		// This class overrides the generated class and will be instantiated by factory
		public class «gc.computeClassname» extends «gc.computeGeneratedClassName()» implements «gc.computeInterfaceName»
		{
		
		}
	'''

	def generateInterfaceContent(GenClass gc) '''
		«copyright»
		package «gc.genPackage.computePackageNameForInterfaces»;
		
		// This interface overrides the generated interface and will be returned by factory
		public interface «gc.computeInterfaceName» extends «gc.computeGeneratedInterfaceName()»
		{
			// You can write additional methods using an empty default java 8 notation 
			// because the generated implemented class extends this interface and is not abstract
			//
			// For instance : 
			// default public void addSomething(Something foo) {} ;
		
		}
	'''

	def generateInterfaceFactoryContent(GenPackage gp) '''
		«copyright»
		package «gp.computePackageNameForInterfaces»;
		
		import «gp.computePackageNameForClasses».«gp.computeFactoryClassName»;
		
		/** This factory  overrides the generated factory and returns the new generated interfaces */
		public interface «gp.computeFactoryInterfaceName» extends «gp.factoryInterfaceName» 
		{
			
			/** Specialize the eINSTANCE initialization with the new interface type 
			  * (overridden in the override_factory extension)
			*/
			«gp.computeFactoryInterfaceName» eINSTANCE = «gp.computeFactoryClassName».init();
						
		}
	'''

	def generateInterfacePackageContent(GenPackage gp) '''
		«copyright»
		package «gp.computePackageNameForInterfaces»;
				
		/** This package interface extends  the generated package interface 
		    It is necessary because its name is used in the EMF generated code) 
		*/
		public interface «gp.computePackageInterfaceName» extends «gp.packageInterfaceName» 
		{
			
		}
	'''

	def generateFactoryDef(GenClass gc) '''
		public «gc.computeInterfaceName.extractGenericTypes»«gc.computeInterfaceName» create«gc.ecoreClass.name»();
	'''
	
	/** This method extracts the generic types found at the end of a class name, like Folder<T> or Folder<T,U>
	 * it returns <T> or <T,U> if the interfaceName is Folder<T> or Folder<T,U>
	 * it returns an empty string if there is no generics
	 */
	def extractGenericTypes(String s) {
		val pos = s.indexOf('<');
		if (pos > 0)
		  s.substring(pos) + " " 
		  else ""
	}

	def generateClassFactoryContent(GenPackage gp) '''
		«copyright»
		package «gp.computePackageNameForClasses»;
		
		import org.eclipse.emf.ecore.plugin.EcorePlugin;
		
		import «gp.computePackageNameForInterfaces».«gp.computeFactoryInterfaceName»;
		
		
		// This factory  renames the generated factory interface to use it as an overriden factory
		public class «gp.computeFactoryClassName» extends «gp.factoryClassName» implements «gp.
			computeFactoryInterfaceName»
		{
			
			public static «gp.computeFactoryInterfaceName» init() {
				
				try {
					Object fact = «gp.factoryClassName».init();
					if ((fact != null) && (fact instanceof «gp.computeFactoryInterfaceName»))
							return («gp.computeFactoryInterfaceName») fact;
					}
				catch (Exception exception) {
					EcorePlugin.INSTANCE.log(exception);
				}
				return new «gp.computeFactoryClassName»(); 
				 }
			
		
		}
	'''


	
	def computeCopyrightComment() '''
	«IF genModel.copyrightText !== null && genModel.copyrightText.length > 0»
/**
  * «genModel.copyrightText»
*/
«ELSE»«ENDIF»
	'''

	/** Compute the class name to be generated */
	def computeClassFilename(GenClass gc) {
		classPattern.replace("{0}", gc.ecoreClass.name) 
	}

	/** Compute the interface name to be generated */
	def computeInterfaceFilename(GenClass gc) {
		interfacePattern.replace("{0}", gc.ecoreClass.name) 
	}

	/** Compute the class name to be generated */
	def computeClassname(GenClass gc) {
		gc.computeClassFilename + gc.ecoreClass.computeGenericTypes
	}

	/** Compute the interface name to be generated */
	def computeInterfaceName(GenClass gc) {
		gc.computeInterfaceFilename + gc.ecoreClass.computeGenericTypes
	}
	
	def computeGenericTypes(EClass c) {
		if (c.ETypeParameters.isEmpty) return ""
		var sb = new StringBuffer("<")
		var  sep = ""
		for (pt : c.ETypeParameters)
		{
			sb.append(sep).append(pt.name)
			sep = ","
		}
		sb.append(">")
		return sb
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
		val basePackage = if(gp.basePackage === null) "" else gp.basePackage + "."
		val packSuffix = if(gp.classPackageSuffix === null) "" else "." + gp.classPackageSuffix
		basePackage + gp.packageName + packSuffix
	}

	/** Compute the package name for interfaces */
	private def computePackageNameForInterfaces(GenPackage gp) {
		val basePackage = if(gp.basePackage === null) "" else gp.basePackage + "."
		val intSuffix = if (gp.interfacePackageSuffix === null || gp.interfacePackageSuffix.length == 0)
				""
			else
				"." + gp.interfacePackageSuffix

		basePackage + gp.packageName + intSuffix
	}

	/** Compute the generated class name depending on classpattern. */
	def computeGeneratedClassName(GenClass c) {
		
		c.className + c.ecoreClass.computeGenericTypes
	}

	/** Compute the generated interface name depending on interfacePattern. */
	def computeGeneratedInterfaceName(GenClass c) {

		c.interfaceName + c.ecoreClass.computeGenericTypes
		
	}


}
