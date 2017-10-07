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
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.core.runtime.Platform
import org.eclipse.core.runtime.Status
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenModel
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.jdt.core.IClasspathEntry
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.JavaCore
import org.osgi.framework.FrameworkUtil

/** This class is used to proceed the different steps to generate the development structure
 * A method is defined for each step :
 * setGenModelTemplates : will set the dynamic templates and import the Class.javajet if not preset
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
		println("Project " + projectName + " is " + status  + " when creating devStructure for " + modelName)
		

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

		println("Generate classes in    : " + srcAbsolutePath)
		println("Generate interfaces in : " + interfaceAbsolutePath)

		for (c : gp.genClasses.filter[!isDynamic]) {
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
		println("Added this factory in list : " + factoryClassName)
		packages.put(gp.getEcorePackage.nsURI, packageClassName)

		// Iterate on subpackages 
		for (sp : gp.subGenPackages)
			sp.generateDevStructure()
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
				jvp.setRawClasspath(newClassPath, new NullProgressMonitor);
			}
		}

	}

	/**
	 * This method checks if the genModel has a dynamic templates property and a
	 * template directory set to projectName/templates
	 * It also copies the ClassJava.jet from the core project.
	 * it returns the a String containing the changes that has been done on genmodel.
	 */
	def public String setGenModelTemplates(GenModel gm, boolean forceSave) {
		val changes = new StringBuffer();

		if (!gm.isDynamicTemplates()) {
			gm.setDynamicTemplates(true);
			changes.append("The dynamic template property must be set to true");
		}

		gm.importOrganizing = true;

		val expectedTemplateDir = "/" + projectName + "/templates";
		val currentTemplateDir = gm.getTemplateDirectory();
		if (!expectedTemplateDir.equals(currentTemplateDir)) {
			gm.setTemplateDirectory(expectedTemplateDir);
			if ((currentTemplateDir !== null) && (currentTemplateDir.length() > 0)) {
				changes.append("\nThe  template directory must be changed :  \n");
				changes.append("\n   Previous value was : " + currentTemplateDir);
				changes.append("\n   New value is       : " + expectedTemplateDir);

			} else {
				changes.append("The template directory has been set to : " + expectedTemplateDir);
			}
		}

		// Extract EMF templates to modify the way to inherit from ancestor
		val classJavajet = project.getFile(expectedTemplateDir + "/model/Class.javajet")
		if (!classJavajet.exists) {
			val extractor = new EMFPatternExtractor(project, classPattern, interfacePattern)
			extractor.run
			refreshWorkspace
			changes.append("\nThe Class.javajet has been installed")
		}

		// Inform user of changes and save the file.
		if ((changes.length() > 0) && forceSave) {
			val Map<Object, Object> opt = new HashMap
			opt.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER)
			opt.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED)
			try {
				gm.eResource().save(opt)
			} catch (IOException e) {
				val bundle = FrameworkUtil.getBundle(this.getClass())
				val logger = Platform.getLog(bundle)
				logger.log(new Status(IStatus.WARNING, bundle.getSymbolicName(),
					"Unable to save the genModel in : " + gm.eResource(), e))
			}

		}

		return changes.toString;

	}

	/** Generate the ant file and return it (or null.  */
	def generateAntFile() {
		generateAntFile(GenerateAntFileForCodeGeneration.ANT_FILENAME)
	}
	
	/** Generate the ant file and return it (or null.  */
	def generateAntFile(String antFilename) {
		println("Generate the ant file : " + antFilename);
		refreshWorkspace
		val gen = new GenerateAntFileForCodeGeneration();
		try {
			val antFile = gen.generateAntFile(modelDir, modelName, project, antFilename);
			project.refreshLocal(1, null)
			return antFile

		} catch (IOException e) {
			e.printStackTrace;
		} catch (CoreException e) {
			e.printStackTrace;
		}
		return null;
	}
	

	/** generate the source code using the ant generated task 
	 * @param f : the ant file to be called */
	def void generateGenModelCode(File f, IProgressMonitor monitor) {

		println("Generate the EMF Code using the ant file : " +  f.absolutePath);

		val runner = new AntRunner
		runner.setBuildFileLocation(f.absolutePath);
	
	    // Uncomment the 2 following lines to display the traces when running 
	    // the EMF code generation ! 
	   /* 	runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
		runner.arguments = "-verbose -debug"
		*/
		// Bundle b = FrameworkUtil.getBundle(GenModelAddonTestCase.class);
		// runner.setCustomClasspath(new URL[] { b.getEntry("ant_tasks/importer.ecore.tasks.jar")});
		try {
			runner.run(monitor);
			refreshWorkspace

		} catch (CoreException e) {
			e.printStackTrace;
		}

	}
	
	// Generate or update extensions...
	def generateExtensions()
	{
		val gfoe = new GenerateExtensions(project);
		gfoe.generateOrUpdateExtensions(factories, packages);
		
	}
	
	
	// This method do the global process in the right order 
	def generateAll(String antFilename)
	{
		// Install the templates
		setGenModelTemplates(genModel, true);

		// Generate the dev structure...
		generateDevStructure(true);

		// Generate the ant file to generate emf code
		val antFile = generateAntFile(antFilename);

		// Once dev structure is generated and ant file too, can call it !
		generateGenModelCode(antFile, new NullProgressMonitor);

		
		// Must generate or update extensions in plugin.xml file
		// BUT AFTER THE EMF CODE GENERATION !!
		generateExtensions();
		
		refreshWorkspace
	
	}

	def refreshWorkspace() {
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
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
		public interface «gp.computeFactoryInterfaceName» extends «gp.computeGeneratedFactoryInterfaceName» 
		{
			
			/** Specialize the eINSTANCE initialization with the new interface type 
			  * (overridden in the override_factory extension)
			*/
			«gp.computeFactoryInterfaceName» eINSTANCE = «gp.computeFactoryClassName».init();
						
			«FOR gc : gp.genClasses.filter[!isDynamic].filter[!isAbstract]»
				«gc.generateFactoryDef»
			«ENDFOR»
		}
	'''

	def generateInterfacePackageContent(GenPackage gp) '''
		«copyright»
		package «gp.computePackageNameForInterfaces»;
				
		/** This package interface extends  the generated package interface 
		    It is necessary because its name is used in the EMF generated code) 
		*/
		public interface «gp.computePackageInterfaceName» extends «gp.computeGeneratedPackageInterfaceName» 
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
		
		«FOR gc : gp.genClasses.filter[!isDynamic].filter[!isAbstract]»
			import «gp.computePackageNameForInterfaces».«gc.computeInterfaceFilename»;
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
			
			«FOR gc : gp.genClasses.filter[!isDynamic].filter[!isAbstract]»
				«gc.generateCreateMethod»
			«ENDFOR»
		}
	'''

	private def generateCreateMethod(GenClass gc) '''
		public «gc.computeInterfaceName.extractGenericTypes»«gc.computeInterfaceName» create«gc.ecoreClass.name»()
		{
			«gc.computeInterfaceName» result = new «gc.computeClassname»();
			return result;
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
		gc.computeClassFilename() + gc.ecoreClass.computeGenericTypes
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

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val classPattern = c.genPackage.genModel.classNamePattern

		if (classPattern !== null)
			classPattern.replace("{0}", c.ecoreClass.name) + c.ecoreClass.computeGenericTypes
		else
			c.ecoreClass.name + "Impl" + c.ecoreClass.computeGenericTypes
	}

	/** Compute the generated interface name depending on interfacePattern. */
	def computeGeneratedInterfaceName(GenClass c) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val interfaceNamePattern = c.genPackage.genModel.interfaceNamePattern

		if (interfaceNamePattern !== null)
			interfaceNamePattern.replace("{0}", c.ecoreClass.name) + c.ecoreClass.computeGenericTypes
		else
			c.ecoreClass.name + c.ecoreClass.computeGenericTypes
	}

	/** Compute the generated factory class name depending on classpattern. */
	def computeGeneratedFactoryClassName(GenPackage gp) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val classPattern = gp.genModel.classNamePattern

		if (classPattern !== null)
			classPattern.replace("{0}", gp.prefix + "Factory")
		else
			gp.prefix + "FactoryImpl"
	}

	/** Compute the generated factory interface name depending on interface. */
	def computeGeneratedFactoryInterfaceName(GenPackage gp) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val interfacePattern = gp.genModel.interfaceNamePattern

		if (interfacePattern !== null)
			interfacePattern.replace("{0}", gp.prefix + "Factory")
		else
			gp.prefix + "Factory"
	}

	/** Compute the generated package interface name depending on interface. */
	def computeGeneratedPackageInterfaceName(GenPackage gp) {

		// Get the class pattern defined in genmodel (if none, this is {0}Impl)
		val interfacePattern = gp.genModel.interfaceNamePattern

		if (interfacePattern !== null)
			interfacePattern.replace("{0}", gp.prefix + "Package")
		else
			gp.prefix + "Package"
	}

}
