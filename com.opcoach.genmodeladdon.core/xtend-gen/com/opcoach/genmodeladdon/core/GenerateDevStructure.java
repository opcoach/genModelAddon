package com.opcoach.genmodeladdon.core;

import com.google.common.base.Objects;
import com.opcoach.genmodeladdon.core.EMFPatternExtractor;
import com.opcoach.genmodeladdon.core.GenerateAntFileForCodeGeneration;
import com.opcoach.genmodeladdon.core.GenerateCommon;
import com.opcoach.genmodeladdon.core.GenerateExtensions;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This class is used to proceed the different steps to generate the development structure
 * A method is defined for each step :
 * setGenModelTemplates : will set the dynamic templates and import the Class.javajet if not preset
 * generateDevStructure : generate the development structure
 * generateAntFile : generate the ant file to generate the code (usefull for automatic builder)
 * generateGenModelCode : generate the EMF code using templates (calls the ant file)
 */
@SuppressWarnings("all")
public class GenerateDevStructure {
  private String classPattern;
  
  private String interfacePattern;
  
  private String srcDevDirectory;
  
  private boolean generateFiles = false;
  
  private IProject project;
  
  private String projectName;
  
  private GenModel genModel;
  
  private String copyright = "";
  
  public Map<String, Object> filesNotGenerated = new HashMap<String, Object>();
  
  private String modelName;
  
  private String modelDir;
  
  private Map<String, String> factories = new HashMap<String, String>();
  
  private Map<String, String> packages = new HashMap<String, String>();
  
  /**
   * Build the generator with 4 parameters
   * @param cpattern : the class name pattern used for generation ({0}Impl for instance)
   * @param ipattern : the interface name pattern used for generation ({0} for instance)
   * @param srcDir : the source directory (relative path) in project
   */
  public GenerateDevStructure(final GenModel gm, final String cPattern, final String iPattern, final String srcDir) {
    try {
      this.genModel = gm;
      String _copyrightText = gm.getCopyrightText();
      boolean _tripleNotEquals = (_copyrightText != null);
      if (_tripleNotEquals) {
        this.copyright = this.computeCopyrightComment().toString();
      }
      this.classPattern = cPattern;
      this.interfacePattern = iPattern;
      this.srcDevDirectory = srcDir;
      this.project = GenerateCommon.getProject(gm);
      this.projectName = this.project.getName();
      this.modelName = GenerateCommon.getModelName(gm);
      this.modelDir = GenerateCommon.getModelPath(gm);
      this.project.open(null);
      String status = "closed";
      boolean _isOpen = this.project.isOpen();
      if (_isOpen) {
        status = "closed";
      }
      InputOutput.<String>println(((((("Project " + this.projectName) + " is ") + status) + " when creating devStructure for ") + this.modelName));
      this.filesNotGenerated.clear();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public GenerateDevStructure(final GenModel gm) {
    this(gm, "{0}ExtImpl", "{0}Ext", "src");
  }
  
  /**
   * Generate the file structure. If genFiles is false just compute the files to be generated
   */
  public void generateDevStructure(final boolean genFiles) {
    try {
      this.generateFiles = genFiles;
      EList<GenPackage> _genPackages = this.genModel.getGenPackages();
      for (final GenPackage p : _genPackages) {
        this.generateDevStructure(p);
      }
      final GenerateExtensions gfoe = new GenerateExtensions(this.project);
      gfoe.generateOrUpdateExtensions(this.factories, this.packages);
      this.project.refreshLocal(IResource.DEPTH_INFINITE, null);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void generateDevStructure(final GenPackage gp) {
    final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    this.project = root.getProject(this.projectName);
    this.setFolderAsSourceFolder(this.project, this.srcDevDirectory);
    String _replace = this.computePackageNameForClasses(gp).replace(".", "/");
    String _plus = ((this.srcDevDirectory + "/") + _replace);
    final IFolder srcFolder = this.project.getFolder(_plus);
    String _oSString = srcFolder.getLocation().toOSString();
    final String srcAbsolutePath = (_oSString + "/");
    final File f = new File(srcAbsolutePath);
    boolean _exists = f.exists();
    boolean _not = (!_exists);
    if (_not) {
      f.mkdirs();
    }
    String _replace_1 = this.computePackageNameForInterfaces(gp).replace(".", "/");
    String _plus_1 = ((this.srcDevDirectory + "/") + _replace_1);
    final IFolder interfaceFolder = this.project.getFolder(_plus_1);
    String _oSString_1 = interfaceFolder.getLocation().toOSString();
    final String interfaceAbsolutePath = (_oSString_1 + "/");
    final File f2 = new File(interfaceAbsolutePath);
    boolean _exists_1 = f2.exists();
    boolean _not_1 = (!_exists_1);
    if (_not_1) {
      f.mkdirs();
    }
    InputOutput.<String>println(("Generate classes in    : " + srcAbsolutePath));
    InputOutput.<String>println(("Generate interfaces in : " + interfaceAbsolutePath));
    final Function1<GenClass, Boolean> _function = (GenClass it) -> {
      boolean _isDynamic = it.isDynamic();
      return Boolean.valueOf((!_isDynamic));
    };
    Iterable<GenClass> _filter = IterableExtensions.<GenClass>filter(gp.getGenClasses(), _function);
    for (final GenClass c : _filter) {
      {
        boolean _isInterface = c.isInterface();
        boolean _not_2 = (!_isInterface);
        if (_not_2) {
          this.generateOverriddenClass(c, srcAbsolutePath);
        }
        this.generateOverriddenInterface(c, interfaceAbsolutePath);
      }
    }
    this.generateOverriddenFactoryInterface(gp, interfaceAbsolutePath);
    this.generateOverriddenFactoryClass(gp, srcAbsolutePath);
    this.generateOverriddenPackageInterface(gp, interfaceAbsolutePath);
    String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
    String _plus_2 = (_computePackageNameForClasses + ".");
    String _computeFactoryClassName = this.computeFactoryClassName(gp);
    final String factoryClassName = (_plus_2 + _computeFactoryClassName);
    final String packageClassName = gp.getQualifiedPackageInterfaceName();
    this.factories.put(gp.getEcorePackage().getNsURI(), factoryClassName);
    InputOutput.<String>println(("Added this factory in list : " + factoryClassName));
    this.packages.put(gp.getEcorePackage().getNsURI(), packageClassName);
    List<GenPackage> _subGenPackages = gp.getSubGenPackages();
    for (final GenPackage sp : _subGenPackages) {
      this.generateDevStructure(sp);
    }
  }
  
  /**
   * add the srcDir as a source directory in the java project, if it is not yet added
   */
  private void setFolderAsSourceFolder(final IProject proj, final String srcDir) {
    try {
      String _name = proj.getName();
      String _plus = ("/" + _name);
      String _plus_1 = (_plus + "/");
      final String expectedSrcDir = (_plus_1 + srcDir);
      final IProjectNature nat = proj.getNature(JavaCore.NATURE_ID);
      if ((nat instanceof IJavaProject)) {
        boolean found = false;
        final IJavaProject jvp = ((IJavaProject) nat);
        IClasspathEntry[] _resolvedClasspath = jvp.getResolvedClasspath(true);
        for (final IClasspathEntry cpe : _resolvedClasspath) {
          if (((!found) && expectedSrcDir.equals(cpe.getPath().toString()))) {
            int _entryKind = cpe.getEntryKind();
            boolean _equals = (_entryKind == IClasspathEntry.CPE_SOURCE);
            found = _equals;
          }
        }
        if ((!found)) {
          final Path path = new Path(expectedSrcDir);
          final IClasspathEntry srcEntry = JavaCore.newSourceEntry(path);
          IClasspathEntry[] _rawClasspath = jvp.getRawClasspath();
          final ArrayList<IClasspathEntry> newClassPath = new ArrayList<IClasspathEntry>((Collection<? extends IClasspathEntry>)Conversions.doWrapArray(_rawClasspath));
          newClassPath.add(srcEntry);
          NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
          jvp.setRawClasspath(((IClasspathEntry[])Conversions.unwrapArray(newClassPath, IClasspathEntry.class)), _nullProgressMonitor);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * This method checks if the genModel has a dynamic templates property and a
   * template directory set to projectName/templates
   * It also copies the ClassJava.jet from the core project.
   * it returns the a String containing the changes that has been done on genmodel.
   */
  public String setGenModelTemplates(final GenModel gm, final boolean forceSave) {
    final StringBuffer changes = new StringBuffer();
    boolean _isDynamicTemplates = gm.isDynamicTemplates();
    boolean _not = (!_isDynamicTemplates);
    if (_not) {
      gm.setDynamicTemplates(true);
      changes.append("The dynamic template property must be set to true");
    }
    gm.setImportOrganizing(true);
    final String expectedTemplateDir = (("/" + this.projectName) + "/templates");
    final String currentTemplateDir = gm.getTemplateDirectory();
    boolean _equals = expectedTemplateDir.equals(currentTemplateDir);
    boolean _not_1 = (!_equals);
    if (_not_1) {
      gm.setTemplateDirectory(expectedTemplateDir);
      if (((!Objects.equal(currentTemplateDir, null)) && (currentTemplateDir.length() > 0))) {
        changes.append("\nThe  template directory must be changed :  \n");
        changes.append(("\n   Previous value was : " + currentTemplateDir));
        changes.append(("\n   New value is       : " + expectedTemplateDir));
      } else {
        changes.append(("The template directory has been set to : " + expectedTemplateDir));
      }
    }
    final IFile classJavajet = this.project.getFile((expectedTemplateDir + "/model/Class.javajet"));
    boolean _exists = classJavajet.exists();
    boolean _not_2 = (!_exists);
    if (_not_2) {
      final EMFPatternExtractor extractor = new EMFPatternExtractor(this.project, this.classPattern, this.interfacePattern);
      extractor.run();
      this.refreshWorkspace();
      changes.append("\nThe Class.javajet has been installed");
    }
    if (((changes.length() > 0) && forceSave)) {
      final Map<Object, Object> opt = new HashMap<Object, Object>();
      opt.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
      opt.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);
      try {
        gm.eResource().save(opt);
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException e = (IOException)_t;
          final Bundle bundle = FrameworkUtil.getBundle(this.getClass());
          final ILog logger = Platform.getLog(bundle);
          String _symbolicName = bundle.getSymbolicName();
          Resource _eResource = gm.eResource();
          String _plus = ("Unable to save the genModel in : " + _eResource);
          Status _status = new Status(IStatus.WARNING, _symbolicName, _plus, e);
          logger.log(_status);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    return changes.toString();
  }
  
  /**
   * Generate the ant file and return it (or null.
   */
  public File generateAntFile() {
    return this.generateAntFile(GenerateAntFileForCodeGeneration.ANT_FILENAME);
  }
  
  /**
   * Generate the ant file and return it (or null.
   */
  public File generateAntFile(final String antFilename) {
    InputOutput.<String>println(("Generate the ant file : " + antFilename));
    this.refreshWorkspace();
    final GenerateAntFileForCodeGeneration gen = new GenerateAntFileForCodeGeneration();
    try {
      final File antFile = gen.generateAntFile(this.modelDir, this.modelName, this.project, antFilename);
      this.project.refreshLocal(1, null);
      return antFile;
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        final IOException e = (IOException)_t;
        e.printStackTrace();
      } else if (_t instanceof CoreException) {
        final CoreException e_1 = (CoreException)_t;
        e_1.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return null;
  }
  
  /**
   * generate the source code using the ant generated task
   * @param f : the ant file to be called
   */
  public void generateGenModelCode(final File f, final IProgressMonitor monitor) {
    String _absolutePath = f.getAbsolutePath();
    String _plus = ("Generate the EMF Code using the ant file : " + _absolutePath);
    InputOutput.<String>println(_plus);
    final AntRunner runner = new AntRunner();
    runner.setBuildFileLocation(f.getAbsolutePath());
    try {
      runner.run(monitor);
      this.refreshWorkspace();
    } catch (final Throwable _t) {
      if (_t instanceof CoreException) {
        final CoreException e = (CoreException)_t;
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public void refreshWorkspace() {
    try {
      ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
    } catch (final Throwable _t) {
      if (_t instanceof CoreException) {
        final CoreException e = (CoreException)_t;
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public Object generateOverriddenFactoryInterface(final GenPackage gp, final String path) {
    Object _xblockexpression = null;
    {
      String _computeFactoryInterfaceName = this.computeFactoryInterfaceName(gp);
      String _plus = (path + _computeFactoryInterfaceName);
      final String filename = (_plus + ".java");
      _xblockexpression = this.generateFile(filename, this.generateInterfaceFactoryContent(gp));
    }
    return _xblockexpression;
  }
  
  public Object generateOverriddenFactoryClass(final GenPackage gp, final String path) {
    Object _xblockexpression = null;
    {
      String _computeFactoryClassName = this.computeFactoryClassName(gp);
      String _plus = (path + _computeFactoryClassName);
      final String filename = (_plus + ".java");
      _xblockexpression = this.generateFile(filename, this.generateClassFactoryContent(gp));
    }
    return _xblockexpression;
  }
  
  public Object generateOverriddenPackageInterface(final GenPackage gp, final String path) {
    Object _xblockexpression = null;
    {
      String _computePackageInterfaceName = this.computePackageInterfaceName(gp);
      String _plus = (path + _computePackageInterfaceName);
      final String filename = (_plus + ".java");
      _xblockexpression = this.generateFile(filename, this.generateInterfacePackageContent(gp));
    }
    return _xblockexpression;
  }
  
  public Object generateOverriddenClass(final GenClass gc, final String path) {
    String _computeClassFilename = this.computeClassFilename(gc);
    String _plus = (path + _computeClassFilename);
    String _plus_1 = (_plus + ".java");
    return this.generateFile(_plus_1, this.generateClassContent(gc));
  }
  
  public Object generateOverriddenInterface(final GenClass gc, final String path) {
    String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
    String _plus = (path + _computeInterfaceFilename);
    String _plus_1 = (_plus + ".java");
    return this.generateFile(_plus_1, this.generateInterfaceContent(gc));
  }
  
  public Object generateFile(final String filename, final Object contents) {
    try {
      Object _xblockexpression = null;
      {
        final File f = new File(filename);
        Object _xifexpression = null;
        boolean _exists = f.exists();
        if (_exists) {
          _xifexpression = this.filesNotGenerated.put(filename, contents);
        } else {
          if (this.generateFiles) {
            final FileWriter fw = new FileWriter(filename);
            fw.write(contents.toString());
            fw.flush();
            fw.close();
          }
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String getSrcAbsolutePath() {
    String _xblockexpression = null;
    {
      final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      final IProject proj = root.getProject(this.projectName);
      final IFolder srcFolder = proj.getFolder((this.srcDevDirectory + "/"));
      String _oSString = srcFolder.getLocation().toOSString();
      _xblockexpression = (_oSString + "/");
    }
    return _xblockexpression;
  }
  
  public CharSequence generateClassContent(final GenClass gc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.copyright);
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _computePackageNameForClasses = this.computePackageNameForClasses(gc.getGenPackage());
    _builder.append(_computePackageNameForClasses);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import ");
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gc.getGenPackage());
    _builder.append(_computePackageNameForInterfaces);
    _builder.append(".");
    String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
    _builder.append(_computeInterfaceFilename);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("// This class overrides the generated class and will be instantiated by factory");
    _builder.newLine();
    _builder.append("public class ");
    String _computeClassname = this.computeClassname(gc);
    _builder.append(_computeClassname);
    _builder.append(" extends ");
    String _computeGeneratedClassName = this.computeGeneratedClassName(gc);
    _builder.append(_computeGeneratedClassName);
    _builder.append(" implements ");
    String _computeInterfaceName = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName);
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateInterfaceContent(final GenClass gc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.copyright);
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gc.getGenPackage());
    _builder.append(_computePackageNameForInterfaces);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("// This interface overrides the generated interface and will be returned by factory");
    _builder.newLine();
    _builder.append("public interface ");
    String _computeInterfaceName = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName);
    _builder.append(" extends ");
    String _computeGeneratedInterfaceName = this.computeGeneratedInterfaceName(gc);
    _builder.append(_computeGeneratedInterfaceName);
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// You can write additional methods using an empty default java 8 notation ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// because the generated implemented class extends this interface and is not abstract");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// For instance : ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// default public void addSomething(Something foo) {} ;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateInterfaceFactoryContent(final GenPackage gp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.copyright);
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
    _builder.append(_computePackageNameForInterfaces);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import ");
    String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
    _builder.append(_computePackageNameForClasses);
    _builder.append(".");
    String _computeFactoryClassName = this.computeFactoryClassName(gp);
    _builder.append(_computeFactoryClassName);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/** This factory  overrides the generated factory and returns the new generated interfaces */");
    _builder.newLine();
    _builder.append("public interface ");
    String _computeFactoryInterfaceName = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName);
    _builder.append(" extends ");
    String _computeGeneratedFactoryInterfaceName = this.computeGeneratedFactoryInterfaceName(gp);
    _builder.append(_computeGeneratedFactoryInterfaceName);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("/** Specialize the eINSTANCE initialization with the new interface type ");
    _builder.newLine();
    _builder.append("\t  ");
    _builder.append("* (overridden in the override_factory extension)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("\t");
    String _computeFactoryInterfaceName_1 = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName_1, "\t");
    _builder.append(" eINSTANCE = ");
    String _computeFactoryClassName_1 = this.computeFactoryClassName(gp);
    _builder.append(_computeFactoryClassName_1, "\t");
    _builder.append(".init();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t");
    _builder.newLine();
    {
      final Function1<GenClass, Boolean> _function = (GenClass it) -> {
        boolean _isDynamic = it.isDynamic();
        return Boolean.valueOf((!_isDynamic));
      };
      final Function1<GenClass, Boolean> _function_1 = (GenClass it) -> {
        boolean _isAbstract = it.isAbstract();
        return Boolean.valueOf((!_isAbstract));
      };
      Iterable<GenClass> _filter = IterableExtensions.<GenClass>filter(IterableExtensions.<GenClass>filter(gp.getGenClasses(), _function), _function_1);
      for(final GenClass gc : _filter) {
        _builder.append("\t");
        CharSequence _generateFactoryDef = this.generateFactoryDef(gc);
        _builder.append(_generateFactoryDef, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateInterfacePackageContent(final GenPackage gp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.copyright);
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
    _builder.append(_computePackageNameForInterfaces);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("/** This package interface extends  the generated package interface ");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("It is necessary because its name is used in the EMF generated code) ");
    _builder.newLine();
    _builder.append("*/");
    _builder.newLine();
    _builder.append("public interface ");
    String _computePackageInterfaceName = this.computePackageInterfaceName(gp);
    _builder.append(_computePackageInterfaceName);
    _builder.append(" extends ");
    String _computeGeneratedPackageInterfaceName = this.computeGeneratedPackageInterfaceName(gp);
    _builder.append(_computeGeneratedPackageInterfaceName);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateFactoryDef(final GenClass gc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    String _extractGenericTypes = this.extractGenericTypes(this.computeInterfaceName(gc));
    _builder.append(_extractGenericTypes);
    String _computeInterfaceName = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName);
    _builder.append(" create");
    String _name = gc.getEcoreClass().getName();
    _builder.append(_name);
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * This method extracts the generic types found at the end of a class name, like Folder<T> or Folder<T,U>
   * it returns <T> or <T,U> if the interfaceName is Folder<T> or Folder<T,U>
   * it returns an empty string if there is no generics
   */
  public String extractGenericTypes(final String s) {
    String _xblockexpression = null;
    {
      final int pos = s.indexOf("<");
      String _xifexpression = null;
      if ((pos > 0)) {
        String _substring = s.substring(pos);
        _xifexpression = (_substring + " ");
      } else {
        _xifexpression = "";
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public CharSequence generateClassFactoryContent(final GenPackage gp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.copyright);
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
    _builder.append(_computePackageNameForClasses);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import org.eclipse.emf.ecore.plugin.EcorePlugin;");
    _builder.newLine();
    _builder.newLine();
    {
      final Function1<GenClass, Boolean> _function = (GenClass it) -> {
        boolean _isDynamic = it.isDynamic();
        return Boolean.valueOf((!_isDynamic));
      };
      final Function1<GenClass, Boolean> _function_1 = (GenClass it) -> {
        boolean _isAbstract = it.isAbstract();
        return Boolean.valueOf((!_isAbstract));
      };
      Iterable<GenClass> _filter = IterableExtensions.<GenClass>filter(IterableExtensions.<GenClass>filter(gp.getGenClasses(), _function), _function_1);
      for(final GenClass gc : _filter) {
        _builder.append("import ");
        String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
        _builder.append(_computePackageNameForInterfaces);
        _builder.append(".");
        String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
        _builder.append(_computeInterfaceFilename);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("import ");
    String _computePackageNameForInterfaces_1 = this.computePackageNameForInterfaces(gp);
    _builder.append(_computePackageNameForInterfaces_1);
    _builder.append(".");
    String _computeFactoryInterfaceName = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.newLine();
    _builder.append("// This factory  overrides the generated factory and returns the new generated interfaces");
    _builder.newLine();
    _builder.append("public class ");
    String _computeFactoryClassName = this.computeFactoryClassName(gp);
    _builder.append(_computeFactoryClassName);
    _builder.append(" extends ");
    String _computeGeneratedFactoryClassName = this.computeGeneratedFactoryClassName(gp);
    _builder.append(_computeGeneratedFactoryClassName);
    _builder.append(" implements ");
    String _computeFactoryInterfaceName_1 = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName_1);
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static ");
    String _computeFactoryInterfaceName_2 = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName_2, "\t");
    _builder.append(" init() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("try {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("Object fact = ");
    String _computeGeneratedFactoryClassName_1 = this.computeGeneratedFactoryClassName(gp);
    _builder.append(_computeGeneratedFactoryClassName_1, "\t\t\t");
    _builder.append(".init();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("if ((fact != null) && (fact instanceof ");
    String _computeFactoryInterfaceName_3 = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName_3, "\t\t\t");
    _builder.append("))");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t\t");
    _builder.append("return (");
    String _computeFactoryInterfaceName_4 = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName_4, "\t\t\t\t\t");
    _builder.append(") fact;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("catch (Exception exception) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("EcorePlugin.INSTANCE.log(exception);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return new ");
    String _computeFactoryClassName_1 = this.computeFactoryClassName(gp);
    _builder.append(_computeFactoryClassName_1, "\t\t");
    _builder.append("(); ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      final Function1<GenClass, Boolean> _function_2 = (GenClass it) -> {
        boolean _isDynamic = it.isDynamic();
        return Boolean.valueOf((!_isDynamic));
      };
      final Function1<GenClass, Boolean> _function_3 = (GenClass it) -> {
        boolean _isAbstract = it.isAbstract();
        return Boolean.valueOf((!_isAbstract));
      };
      Iterable<GenClass> _filter_1 = IterableExtensions.<GenClass>filter(IterableExtensions.<GenClass>filter(gp.getGenClasses(), _function_2), _function_3);
      for(final GenClass gc_1 : _filter_1) {
        _builder.append("\t");
        CharSequence _generateCreateMethod = this.generateCreateMethod(gc_1);
        _builder.append(_generateCreateMethod, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateCreateMethod(final GenClass gc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    String _extractGenericTypes = this.extractGenericTypes(this.computeInterfaceName(gc));
    _builder.append(_extractGenericTypes);
    String _computeInterfaceName = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName);
    _builder.append(" create");
    String _name = gc.getEcoreClass().getName();
    _builder.append(_name);
    _builder.append("()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    String _computeInterfaceName_1 = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName_1, "\t");
    _builder.append(" result = new ");
    String _computeClassname = this.computeClassname(gc);
    _builder.append(_computeClassname, "\t");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return result;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence computeCopyrightComment() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (((!Objects.equal(this.genModel.getCopyrightText(), null)) && (this.genModel.getCopyrightText().length() > 0))) {
        _builder.append("/**");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("* ");
        String _copyrightText = this.genModel.getCopyrightText();
        _builder.append(_copyrightText, "  ");
        _builder.newLineIfNotEmpty();
        _builder.append("*/");
        _builder.newLine();
      } else {
      }
    }
    return _builder;
  }
  
  /**
   * Compute the class name to be generated
   */
  public String computeClassFilename(final GenClass gc) {
    return this.classPattern.replace("{0}", gc.getEcoreClass().getName());
  }
  
  /**
   * Compute the interface name to be generated
   */
  public String computeInterfaceFilename(final GenClass gc) {
    return this.interfacePattern.replace("{0}", gc.getEcoreClass().getName());
  }
  
  /**
   * Compute the class name to be generated
   */
  public String computeClassname(final GenClass gc) {
    String _computeClassFilename = this.computeClassFilename(gc);
    Object _computeGenericTypes = this.computeGenericTypes(gc.getEcoreClass());
    return (_computeClassFilename + _computeGenericTypes);
  }
  
  /**
   * Compute the interface name to be generated
   */
  public String computeInterfaceName(final GenClass gc) {
    String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
    Object _computeGenericTypes = this.computeGenericTypes(gc.getEcoreClass());
    return (_computeInterfaceFilename + _computeGenericTypes);
  }
  
  public Object computeGenericTypes(final EClass c) {
    boolean _isEmpty = c.getETypeParameters().isEmpty();
    if (_isEmpty) {
      return "";
    }
    StringBuffer sb = new StringBuffer("<");
    String sep = "";
    EList<ETypeParameter> _eTypeParameters = c.getETypeParameters();
    for (final ETypeParameter pt : _eTypeParameters) {
      {
        sb.append(sep).append(pt.getName());
        sep = ",";
      }
    }
    sb.append(">");
    return sb;
  }
  
  /**
   * Compute the factory interface name to be generated
   */
  public String computeFactoryInterfaceName(final GenPackage gp) {
    String _prefix = gp.getPrefix();
    return (_prefix + "Factory");
  }
  
  /**
   * Compute the factory interface name to be generated
   */
  public String computePackageInterfaceName(final GenPackage gp) {
    String _prefix = gp.getPrefix();
    return (_prefix + "Package");
  }
  
  /**
   * Compute the factory class name to be generated
   */
  public String computeFactoryClassName(final GenPackage gp) {
    String _prefix = gp.getPrefix();
    String _replace = this.classPattern.replace("{0}", "Factory");
    return (_prefix + _replace);
  }
  
  /**
   * Compute the package name for class
   */
  public String computePackageNameForClasses(final GenPackage gp) {
    String _xblockexpression = null;
    {
      String _xifexpression = null;
      String _basePackage = gp.getBasePackage();
      boolean _equals = Objects.equal(_basePackage, null);
      if (_equals) {
        _xifexpression = "";
      } else {
        String _basePackage_1 = gp.getBasePackage();
        _xifexpression = (_basePackage_1 + ".");
      }
      final String basePackage = _xifexpression;
      String _xifexpression_1 = null;
      String _classPackageSuffix = gp.getClassPackageSuffix();
      boolean _equals_1 = Objects.equal(_classPackageSuffix, null);
      if (_equals_1) {
        _xifexpression_1 = "";
      } else {
        String _classPackageSuffix_1 = gp.getClassPackageSuffix();
        _xifexpression_1 = ("." + _classPackageSuffix_1);
      }
      final String packSuffix = _xifexpression_1;
      String _packageName = gp.getPackageName();
      String _plus = (basePackage + _packageName);
      _xblockexpression = (_plus + packSuffix);
    }
    return _xblockexpression;
  }
  
  /**
   * Compute the package name for interfaces
   */
  private String computePackageNameForInterfaces(final GenPackage gp) {
    String _xblockexpression = null;
    {
      String _xifexpression = null;
      String _basePackage = gp.getBasePackage();
      boolean _equals = Objects.equal(_basePackage, null);
      if (_equals) {
        _xifexpression = "";
      } else {
        String _basePackage_1 = gp.getBasePackage();
        _xifexpression = (_basePackage_1 + ".");
      }
      final String basePackage = _xifexpression;
      String _xifexpression_1 = null;
      if ((Objects.equal(gp.getInterfacePackageSuffix(), null) || (gp.getInterfacePackageSuffix().length() == 0))) {
        _xifexpression_1 = "";
      } else {
        String _interfacePackageSuffix = gp.getInterfacePackageSuffix();
        _xifexpression_1 = ("." + _interfacePackageSuffix);
      }
      final String intSuffix = _xifexpression_1;
      String _packageName = gp.getPackageName();
      String _plus = (basePackage + _packageName);
      _xblockexpression = (_plus + intSuffix);
    }
    return _xblockexpression;
  }
  
  /**
   * Compute the generated class name depending on classpattern.
   */
  public String computeGeneratedClassName(final GenClass c) {
    String _xblockexpression = null;
    {
      final String classPattern = c.getGenPackage().getGenModel().getClassNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(classPattern, null));
      if (_notEquals) {
        String _replace = classPattern.replace("{0}", c.getEcoreClass().getName());
        Object _computeGenericTypes = this.computeGenericTypes(c.getEcoreClass());
        _xifexpression = (_replace + _computeGenericTypes);
      } else {
        String _name = c.getEcoreClass().getName();
        String _plus = (_name + "Impl");
        Object _computeGenericTypes_1 = this.computeGenericTypes(c.getEcoreClass());
        _xifexpression = (_plus + _computeGenericTypes_1);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Compute the generated interface name depending on interfacePattern.
   */
  public String computeGeneratedInterfaceName(final GenClass c) {
    String _xblockexpression = null;
    {
      final String interfaceNamePattern = c.getGenPackage().getGenModel().getInterfaceNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(interfaceNamePattern, null));
      if (_notEquals) {
        String _replace = interfaceNamePattern.replace("{0}", c.getEcoreClass().getName());
        Object _computeGenericTypes = this.computeGenericTypes(c.getEcoreClass());
        _xifexpression = (_replace + _computeGenericTypes);
      } else {
        String _name = c.getEcoreClass().getName();
        Object _computeGenericTypes_1 = this.computeGenericTypes(c.getEcoreClass());
        _xifexpression = (_name + _computeGenericTypes_1);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Compute the generated factory class name depending on classpattern.
   */
  public String computeGeneratedFactoryClassName(final GenPackage gp) {
    String _xblockexpression = null;
    {
      final String classPattern = gp.getGenModel().getClassNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(classPattern, null));
      if (_notEquals) {
        String _prefix = gp.getPrefix();
        String _plus = (_prefix + "Factory");
        _xifexpression = classPattern.replace("{0}", _plus);
      } else {
        String _prefix_1 = gp.getPrefix();
        _xifexpression = (_prefix_1 + "FactoryImpl");
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Compute the generated factory interface name depending on interface.
   */
  public String computeGeneratedFactoryInterfaceName(final GenPackage gp) {
    String _xblockexpression = null;
    {
      final String interfacePattern = gp.getGenModel().getInterfaceNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(interfacePattern, null));
      if (_notEquals) {
        String _prefix = gp.getPrefix();
        String _plus = (_prefix + "Factory");
        _xifexpression = interfacePattern.replace("{0}", _plus);
      } else {
        String _prefix_1 = gp.getPrefix();
        _xifexpression = (_prefix_1 + "Factory");
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Compute the generated package interface name depending on interface.
   */
  public String computeGeneratedPackageInterfaceName(final GenPackage gp) {
    String _xblockexpression = null;
    {
      final String interfacePattern = gp.getGenModel().getInterfaceNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(interfacePattern, null));
      if (_notEquals) {
        String _prefix = gp.getPrefix();
        String _plus = (_prefix + "Package");
        _xifexpression = interfacePattern.replace("{0}", _plus);
      } else {
        String _prefix_1 = gp.getPrefix();
        _xifexpression = (_prefix_1 + "Package");
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
