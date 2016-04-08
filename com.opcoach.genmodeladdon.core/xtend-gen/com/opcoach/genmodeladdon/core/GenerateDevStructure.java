package com.opcoach.genmodeladdon.core;

import com.google.common.base.Objects;
import com.opcoach.genmodeladdon.core.EMFPatternExtractor;
import com.opcoach.genmodeladdon.core.GenerateAntFileForCodeGeneration;
import com.opcoach.genmodeladdon.core.GenerateCommon;
import com.opcoach.genmodeladdon.core.GenerateFactoryOverrideExtension;
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
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
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
import org.eclipse.emf.ecore.EPackage;
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
import org.eclipse.xtext.xbase.lib.StringExtensions;
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
  
  /**
   * Build the generator with 4 parameters
   * @param cpattern : the class name pattern used for generation ({0}Impl for instance)
   * @param ipattern : the interface name pattern used for generation ({0} for instance)
   * @param srcDir : the source directory (relative path) in project
   */
  public GenerateDevStructure(final GenModel gm, final String cPattern, final String iPattern, final String srcDir) {
    this.genModel = gm;
    String _copyrightText = gm.getCopyrightText();
    boolean _notEquals = (!Objects.equal(_copyrightText, null));
    if (_notEquals) {
      CharSequence _computeCopyrightComment = this.computeCopyrightComment();
      String _string = _computeCopyrightComment.toString();
      this.copyright = _string;
    }
    this.classPattern = cPattern;
    this.interfacePattern = iPattern;
    this.srcDevDirectory = srcDir;
    IProject _project = GenerateCommon.getProject(gm);
    this.project = _project;
    String _name = this.project.getName();
    this.projectName = _name;
    String _modelName = GenerateCommon.getModelName(gm);
    this.modelName = _modelName;
    this.filesNotGenerated.clear();
  }
  
  public GenerateDevStructure(final GenModel gm) {
    this(gm, "{0}ExtImpl", "{0}Ext", "src");
  }
  
  /**
   * Generate the file structure. If genFiles is false just compute the files to be generated
   */
  public void generateDevStructure(final boolean genFiles) {
    this.generateFiles = genFiles;
    EList<GenPackage> _genPackages = this.genModel.getGenPackages();
    for (final GenPackage p : _genPackages) {
      this.generateDevStructure(p);
    }
  }
  
  public void generateDevStructure(final GenPackage gp) {
    try {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      final IWorkspaceRoot root = _workspace.getRoot();
      IProject _project = root.getProject(this.projectName);
      this.project = _project;
      this.setFolderAsSourceFolder(this.project, this.srcDevDirectory);
      String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
      String _replace = _computePackageNameForClasses.replace(".", "/");
      String _plus = ((this.srcDevDirectory + "/") + _replace);
      final IFolder srcFolder = this.project.getFolder(_plus);
      IPath _location = srcFolder.getLocation();
      String _oSString = _location.toOSString();
      final String srcAbsolutePath = (_oSString + "/");
      final File f = new File(srcAbsolutePath);
      boolean _exists = f.exists();
      boolean _not = (!_exists);
      if (_not) {
        f.mkdirs();
      }
      String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
      String _replace_1 = _computePackageNameForInterfaces.replace(".", "/");
      String _plus_1 = ((this.srcDevDirectory + "/") + _replace_1);
      final IFolder interfaceFolder = this.project.getFolder(_plus_1);
      IPath _location_1 = interfaceFolder.getLocation();
      String _oSString_1 = _location_1.toOSString();
      final String interfaceAbsolutePath = (_oSString_1 + "/");
      final File f2 = new File(interfaceAbsolutePath);
      boolean _exists_1 = f2.exists();
      boolean _not_1 = (!_exists_1);
      if (_not_1) {
        f.mkdirs();
      }
      InputOutput.<String>println(("Generate classes in    : " + srcAbsolutePath));
      InputOutput.<String>println(("Generate interfaces in : " + interfaceAbsolutePath));
      EList<GenClass> _genClasses = gp.getGenClasses();
      final Function1<GenClass, Boolean> _function = new Function1<GenClass, Boolean>() {
        @Override
        public Boolean apply(final GenClass it) {
          boolean _isDynamic = it.isDynamic();
          return Boolean.valueOf((!_isDynamic));
        }
      };
      Iterable<GenClass> _filter = IterableExtensions.<GenClass>filter(_genClasses, _function);
      for (final GenClass c : _filter) {
        {
          this.generateOverriddenClass(c, srcAbsolutePath);
          this.generateOverriddenInterface(c, interfaceAbsolutePath);
        }
      }
      this.generateOverriddenFactoryInterface(gp, interfaceAbsolutePath);
      this.generateOverriddenFactoryClass(gp, srcAbsolutePath);
      this.generateOverriddenPackageInterface(gp, interfaceAbsolutePath);
      this.project.refreshLocal(IResource.DEPTH_INFINITE, null);
      final GenerateFactoryOverrideExtension gfoe = new GenerateFactoryOverrideExtension(this.projectName);
      EPackage _ecorePackage = gp.getEcorePackage();
      String _nsURI = _ecorePackage.getNsURI();
      String _computePackageNameForClasses_1 = this.computePackageNameForClasses(gp);
      String _plus_2 = (_computePackageNameForClasses_1 + ".");
      String _computeFactoryClassName = this.computeFactoryClassName(gp);
      String _plus_3 = (_plus_2 + _computeFactoryClassName);
      gfoe.generateOverideExtension(_nsURI, _plus_3);
      List<GenPackage> _subGenPackages = gp.getSubGenPackages();
      for (final GenPackage sp : _subGenPackages) {
        this.generateDevStructure(sp);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
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
          boolean _and = false;
          if (!(!found)) {
            _and = false;
          } else {
            IPath _path = cpe.getPath();
            String _string = _path.toString();
            boolean _equals = expectedSrcDir.equals(_string);
            _and = _equals;
          }
          if (_and) {
            int _entryKind = cpe.getEntryKind();
            boolean _equals_1 = (_entryKind == IClasspathEntry.CPE_SOURCE);
            found = _equals_1;
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
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(currentTemplateDir, null));
      if (!_notEquals) {
        _and = false;
      } else {
        int _length = currentTemplateDir.length();
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      if (_and) {
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
    boolean _and_1 = false;
    int _length_1 = changes.length();
    boolean _greaterThan_1 = (_length_1 > 0);
    if (!_greaterThan_1) {
      _and_1 = false;
    } else {
      _and_1 = forceSave;
    }
    if (_and_1) {
      final Map<Object, Object> opt = new HashMap<Object, Object>();
      opt.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
      opt.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);
      try {
        Resource _eResource = gm.eResource();
        _eResource.save(opt);
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException e = (IOException)_t;
          Class<? extends GenerateDevStructure> _class = this.getClass();
          final Bundle bundle = FrameworkUtil.getBundle(_class);
          final ILog logger = Platform.getLog(bundle);
          String _symbolicName = bundle.getSymbolicName();
          Resource _eResource_1 = gm.eResource();
          String _plus = ("Unable to save the genModel in : " + _eResource_1);
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
    InputOutput.<String>println("-------> GENERATE THE ANT FILE -----");
    this.refreshWorkspace();
    final GenerateAntFileForCodeGeneration gen = new GenerateAntFileForCodeGeneration();
    try {
      final File antFile = gen.generateAntFile(this.modelName, this.project);
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
    InputOutput.<String>println("-------> END GENERATE THE ANT FILE -----");
    return null;
  }
  
  /**
   * generate the source code using the ant generated task
   * @param f : the ant file to be called
   */
  public void generateGenModelCode(final File f, final IProgressMonitor monitor) {
    InputOutput.<String>println("--------- START GENERATE THE EMF CODE -------------");
    String _absolutePath = f.getAbsolutePath();
    String _plus = ("on : " + _absolutePath);
    InputOutput.<String>println(_plus);
    final AntRunner runner = new AntRunner();
    String _absolutePath_1 = f.getAbsolutePath();
    runner.setBuildFileLocation(_absolutePath_1);
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
    InputOutput.<String>println("--------- END GENERATE THE EMF CODE -------------");
  }
  
  public void refreshWorkspace() {
    try {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      _root.refreshLocal(IResource.DEPTH_INFINITE, null);
    } catch (final Throwable _t) {
      if (_t instanceof CoreException) {
        final CoreException e = (CoreException)_t;
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
      CharSequence _generateInterfaceFactoryContent = this.generateInterfaceFactoryContent(gp);
      _xblockexpression = this.generateFile(filename, _generateInterfaceFactoryContent);
    }
    return _xblockexpression;
  }
  
  public Object generateOverriddenFactoryClass(final GenPackage gp, final String path) {
    Object _xblockexpression = null;
    {
      String _computeFactoryClassName = this.computeFactoryClassName(gp);
      String _plus = (path + _computeFactoryClassName);
      final String filename = (_plus + ".java");
      CharSequence _generateClassFactoryContent = this.generateClassFactoryContent(gp);
      _xblockexpression = this.generateFile(filename, _generateClassFactoryContent);
    }
    return _xblockexpression;
  }
  
  public Object generateOverriddenPackageInterface(final GenPackage gp, final String path) {
    Object _xblockexpression = null;
    {
      String _computePackageInterfaceName = this.computePackageInterfaceName(gp);
      String _plus = (path + _computePackageInterfaceName);
      final String filename = (_plus + ".java");
      CharSequence _generateInterfacePackageContent = this.generateInterfacePackageContent(gp);
      _xblockexpression = this.generateFile(filename, _generateInterfacePackageContent);
    }
    return _xblockexpression;
  }
  
  public Object generateOverriddenClass(final GenClass gc, final String path) {
    String _computeClassFilename = this.computeClassFilename(gc);
    String _plus = (path + _computeClassFilename);
    String _plus_1 = (_plus + ".java");
    CharSequence _generateClassContent = this.generateClassContent(gc);
    return this.generateFile(_plus_1, _generateClassContent);
  }
  
  public Object generateOverriddenInterface(final GenClass gc, final String path) {
    String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
    String _plus = (path + _computeInterfaceFilename);
    String _plus_1 = (_plus + ".java");
    CharSequence _generateInterfaceContent = this.generateInterfaceContent(gc);
    return this.generateFile(_plus_1, _generateInterfaceContent);
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
            String _string = contents.toString();
            fw.write(_string);
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
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      final IWorkspaceRoot root = _workspace.getRoot();
      final IProject proj = root.getProject(this.projectName);
      final IFolder srcFolder = proj.getFolder((this.srcDevDirectory + "/"));
      IPath _location = srcFolder.getLocation();
      String _oSString = _location.toOSString();
      _xblockexpression = (_oSString + "/");
    }
    return _xblockexpression;
  }
  
  public CharSequence generateClassContent(final GenClass gc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.copyright, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    GenPackage _genPackage = gc.getGenPackage();
    String _computePackageNameForClasses = this.computePackageNameForClasses(_genPackage);
    _builder.append(_computePackageNameForClasses, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import ");
    GenPackage _genPackage_1 = gc.getGenPackage();
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(_genPackage_1);
    _builder.append(_computePackageNameForInterfaces, "");
    _builder.append(".");
    String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
    _builder.append(_computeInterfaceFilename, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("// This class overrides the generated class and will be instantiated by factory");
    _builder.newLine();
    _builder.append("public class ");
    String _computeClassname = this.computeClassname(gc);
    _builder.append(_computeClassname, "");
    _builder.append(" extends ");
    String _computeGeneratedClassName = this.computeGeneratedClassName(gc);
    _builder.append(_computeGeneratedClassName, "");
    _builder.append(" implements ");
    String _computeInterfaceName = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName, "");
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
    _builder.append(this.copyright, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    GenPackage _genPackage = gc.getGenPackage();
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(_genPackage);
    _builder.append(_computePackageNameForInterfaces, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("// This interface overrides the generated interface and will be returned by factory");
    _builder.newLine();
    _builder.append("public interface ");
    String _computeInterfaceName = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName, "");
    _builder.append(" extends ");
    String _computeGeneratedInterfaceName = this.computeGeneratedInterfaceName(gc);
    _builder.append(_computeGeneratedInterfaceName, "");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateInterfaceFactoryContent(final GenPackage gp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.copyright, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
    _builder.append(_computePackageNameForInterfaces, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import ");
    String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
    _builder.append(_computePackageNameForClasses, "");
    _builder.append(".");
    String _computeFactoryClassName = this.computeFactoryClassName(gp);
    _builder.append(_computeFactoryClassName, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/** This factory  overrides the generated factory and returns the new generated interfaces */");
    _builder.newLine();
    _builder.append("public interface ");
    String _computeFactoryInterfaceName = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName, "");
    _builder.append(" extends ");
    String _computeGeneratedFactoryInterfaceName = this.computeGeneratedFactoryInterfaceName(gp);
    _builder.append(_computeGeneratedFactoryInterfaceName, "");
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
      EList<GenClass> _genClasses = gp.getGenClasses();
      final Function1<GenClass, Boolean> _function = new Function1<GenClass, Boolean>() {
        @Override
        public Boolean apply(final GenClass it) {
          boolean _isDynamic = it.isDynamic();
          return Boolean.valueOf((!_isDynamic));
        }
      };
      Iterable<GenClass> _filter = IterableExtensions.<GenClass>filter(_genClasses, _function);
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
    _builder.append(this.copyright, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
    _builder.append(_computePackageNameForInterfaces, "");
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
    _builder.append(_computePackageInterfaceName, "");
    _builder.append(" extends ");
    String _computeGeneratedPackageInterfaceName = this.computeGeneratedPackageInterfaceName(gp);
    _builder.append(_computeGeneratedPackageInterfaceName, "");
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
    String _computeInterfaceName = this.computeInterfaceName(gc);
    String _extractGenericTypes = this.extractGenericTypes(_computeInterfaceName);
    _builder.append(_extractGenericTypes, "");
    String _computeInterfaceName_1 = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName_1, "");
    _builder.append(" create");
    EClass _ecoreClass = gc.getEcoreClass();
    String _name = _ecoreClass.getName();
    _builder.append(_name, "");
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
    _builder.append(this.copyright, "");
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
    _builder.append(_computePackageNameForClasses, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import org.eclipse.emf.ecore.plugin.EcorePlugin;");
    _builder.newLine();
    _builder.newLine();
    {
      EList<GenClass> _genClasses = gp.getGenClasses();
      final Function1<GenClass, Boolean> _function = new Function1<GenClass, Boolean>() {
        @Override
        public Boolean apply(final GenClass it) {
          boolean _isDynamic = it.isDynamic();
          return Boolean.valueOf((!_isDynamic));
        }
      };
      Iterable<GenClass> _filter = IterableExtensions.<GenClass>filter(_genClasses, _function);
      for(final GenClass gc : _filter) {
        _builder.append("import ");
        String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
        _builder.append(_computePackageNameForInterfaces, "");
        _builder.append(".");
        String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
        _builder.append(_computeInterfaceFilename, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("import ");
    String _computePackageNameForInterfaces_1 = this.computePackageNameForInterfaces(gp);
    _builder.append(_computePackageNameForInterfaces_1, "");
    _builder.append(".");
    String _computeFactoryInterfaceName = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.newLine();
    _builder.append("// This factory  overrides the generated factory and returns the new generated interfaces");
    _builder.newLine();
    _builder.append("public class ");
    String _computeFactoryClassName = this.computeFactoryClassName(gp);
    _builder.append(_computeFactoryClassName, "");
    _builder.append(" extends ");
    String _computeGeneratedFactoryClassName = this.computeGeneratedFactoryClassName(gp);
    _builder.append(_computeGeneratedFactoryClassName, "");
    _builder.append(" implements ");
    String _computeFactoryInterfaceName_1 = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName_1, "");
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
      EList<GenClass> _genClasses_1 = gp.getGenClasses();
      final Function1<GenClass, Boolean> _function_1 = new Function1<GenClass, Boolean>() {
        @Override
        public Boolean apply(final GenClass it) {
          boolean _isDynamic = it.isDynamic();
          return Boolean.valueOf((!_isDynamic));
        }
      };
      Iterable<GenClass> _filter_1 = IterableExtensions.<GenClass>filter(_genClasses_1, _function_1);
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
    String _computeInterfaceName = this.computeInterfaceName(gc);
    String _extractGenericTypes = this.extractGenericTypes(_computeInterfaceName);
    _builder.append(_extractGenericTypes, "");
    String _computeInterfaceName_1 = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName_1, "");
    _builder.append(" create");
    EClass _ecoreClass = gc.getEcoreClass();
    String _name = _ecoreClass.getName();
    _builder.append(_name, "");
    _builder.append("()");
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    String _computeInterfaceName_2 = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName_2, "\t");
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
      boolean _and = false;
      String _copyrightText = this.genModel.getCopyrightText();
      boolean _notEquals = (!Objects.equal(_copyrightText, null));
      if (!_notEquals) {
        _and = false;
      } else {
        String _copyrightText_1 = this.genModel.getCopyrightText();
        int _length = _copyrightText_1.length();
        boolean _greaterThan = (_length > 0);
        _and = _greaterThan;
      }
      if (_and) {
        _builder.append("/**");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("* ");
        String _copyrightText_2 = this.genModel.getCopyrightText();
        _builder.append(_copyrightText_2, "  ");
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
    EClass _ecoreClass = gc.getEcoreClass();
    String _name = _ecoreClass.getName();
    return this.classPattern.replace("{0}", _name);
  }
  
  /**
   * Compute the interface name to be generated
   */
  public String computeInterfaceFilename(final GenClass gc) {
    EClass _ecoreClass = gc.getEcoreClass();
    String _name = _ecoreClass.getName();
    return this.interfacePattern.replace("{0}", _name);
  }
  
  /**
   * Compute the class name to be generated
   */
  public String computeClassname(final GenClass gc) {
    String _computeClassFilename = this.computeClassFilename(gc);
    EClass _ecoreClass = gc.getEcoreClass();
    Object _computeGenericTypes = this.computeGenericTypes(_ecoreClass);
    return (_computeClassFilename + _computeGenericTypes);
  }
  
  /**
   * Compute the interface name to be generated
   */
  public String computeInterfaceName(final GenClass gc) {
    String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
    EClass _ecoreClass = gc.getEcoreClass();
    Object _computeGenericTypes = this.computeGenericTypes(_ecoreClass);
    return (_computeInterfaceFilename + _computeGenericTypes);
  }
  
  public Object computeGenericTypes(final EClass c) {
    EList<ETypeParameter> _eTypeParameters = c.getETypeParameters();
    boolean _isEmpty = _eTypeParameters.isEmpty();
    if (_isEmpty) {
      return "";
    }
    StringBuffer sb = new StringBuffer("<");
    String sep = "";
    EList<ETypeParameter> _eTypeParameters_1 = c.getETypeParameters();
    for (final ETypeParameter pt : _eTypeParameters_1) {
      {
        String _name = pt.getName();
        StringBuffer _append = sb.append(_name);
        _append.append(sep);
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
      String _lowerCase = _packageName.toLowerCase();
      String _plus = (basePackage + _lowerCase);
      _xblockexpression = (_plus + packSuffix);
    }
    return _xblockexpression;
  }
  
  /**
   * Compute the package name for interfaces
   */
  public String computePackageNameForInterfaces(final GenPackage gp) {
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
      boolean _or = false;
      String _interfacePackageSuffix = gp.getInterfacePackageSuffix();
      boolean _equals_1 = Objects.equal(_interfacePackageSuffix, null);
      if (_equals_1) {
        _or = true;
      } else {
        String _interfacePackageSuffix_1 = gp.getInterfacePackageSuffix();
        int _length = _interfacePackageSuffix_1.length();
        boolean _equals_2 = (_length == 0);
        _or = _equals_2;
      }
      if (_or) {
        _xifexpression_1 = "";
      } else {
        String _interfacePackageSuffix_2 = gp.getInterfacePackageSuffix();
        _xifexpression_1 = ("." + _interfacePackageSuffix_2);
      }
      final String intSuffix = _xifexpression_1;
      String _packageName = gp.getPackageName();
      String _lowerCase = _packageName.toLowerCase();
      String _plus = (basePackage + _lowerCase);
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
      GenPackage _genPackage = c.getGenPackage();
      GenModel _genModel = _genPackage.getGenModel();
      final String classPattern = _genModel.getClassNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(classPattern, null));
      if (_notEquals) {
        EClass _ecoreClass = c.getEcoreClass();
        String _name = _ecoreClass.getName();
        String _replace = classPattern.replace("{0}", _name);
        EClass _ecoreClass_1 = c.getEcoreClass();
        Object _computeGenericTypes = this.computeGenericTypes(_ecoreClass_1);
        _xifexpression = (_replace + _computeGenericTypes);
      } else {
        EClass _ecoreClass_2 = c.getEcoreClass();
        String _name_1 = _ecoreClass_2.getName();
        String _plus = (_name_1 + "Impl");
        EClass _ecoreClass_3 = c.getEcoreClass();
        Object _computeGenericTypes_1 = this.computeGenericTypes(_ecoreClass_3);
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
      GenPackage _genPackage = c.getGenPackage();
      GenModel _genModel = _genPackage.getGenModel();
      final String interfaceNamePattern = _genModel.getInterfaceNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(interfaceNamePattern, null));
      if (_notEquals) {
        EClass _ecoreClass = c.getEcoreClass();
        String _name = _ecoreClass.getName();
        String _replace = interfaceNamePattern.replace("{0}", _name);
        EClass _ecoreClass_1 = c.getEcoreClass();
        Object _computeGenericTypes = this.computeGenericTypes(_ecoreClass_1);
        _xifexpression = (_replace + _computeGenericTypes);
      } else {
        EClass _ecoreClass_2 = c.getEcoreClass();
        String _name_1 = _ecoreClass_2.getName();
        EClass _ecoreClass_3 = c.getEcoreClass();
        Object _computeGenericTypes_1 = this.computeGenericTypes(_ecoreClass_3);
        _xifexpression = (_name_1 + _computeGenericTypes_1);
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
      GenModel _genModel = gp.getGenModel();
      final String classPattern = _genModel.getClassNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(classPattern, null));
      if (_notEquals) {
        String _packageName = gp.getPackageName();
        String _firstUpper = StringExtensions.toFirstUpper(_packageName);
        String _plus = (_firstUpper + "Factory");
        _xifexpression = classPattern.replace("{0}", _plus);
      } else {
        String _packageName_1 = gp.getPackageName();
        String _firstUpper_1 = StringExtensions.toFirstUpper(_packageName_1);
        _xifexpression = (_firstUpper_1 + "FactoryImpl");
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
      GenModel _genModel = gp.getGenModel();
      final String interfacePattern = _genModel.getInterfaceNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(interfacePattern, null));
      if (_notEquals) {
        String _packageName = gp.getPackageName();
        String _firstUpper = StringExtensions.toFirstUpper(_packageName);
        String _plus = (_firstUpper + "Factory");
        _xifexpression = interfacePattern.replace("{0}", _plus);
      } else {
        String _packageName_1 = gp.getPackageName();
        String _firstUpper_1 = StringExtensions.toFirstUpper(_packageName_1);
        _xifexpression = (_firstUpper_1 + "Factory");
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
      GenModel _genModel = gp.getGenModel();
      final String interfacePattern = _genModel.getInterfaceNamePattern();
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(interfacePattern, null));
      if (_notEquals) {
        String _packageName = gp.getPackageName();
        String _firstUpper = StringExtensions.toFirstUpper(_packageName);
        String _plus = (_firstUpper + "Package");
        _xifexpression = interfacePattern.replace("{0}", _plus);
      } else {
        String _packageName_1 = gp.getPackageName();
        String _firstUpper_1 = StringExtensions.toFirstUpper(_packageName_1);
        _xifexpression = (_firstUpper_1 + "Package");
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
