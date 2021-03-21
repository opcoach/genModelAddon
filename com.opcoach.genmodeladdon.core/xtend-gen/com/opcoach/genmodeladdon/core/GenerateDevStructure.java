package com.opcoach.genmodeladdon.core;

import com.google.common.collect.Iterables;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ArrayExtensions;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * This class is used to proceed the different steps to generate the development structure
 * A method is defined for each step :
 * generateDevStructure : generate the development structure
 * generateAntFile : generate the ant file to generate the code (usefull for automatic builder)
 * generateGenModelCode : generate the EMF code using templates (calls the ant file)
 */
@SuppressWarnings("all")
public class GenerateDevStructure implements IResourceChangeListener {
  private String classPattern;
  
  private String interfacePattern;
  
  private String srcDevDirectory;
  
  private boolean generateFiles = false;
  
  private IProject project;
  
  private String projectName;
  
  private GMAGenModel gmaGenModel;
  
  private String copyright = "";
  
  private boolean debug = false;
  
  public Map<String, Object> filesNotGenerated = new HashMap<String, Object>();
  
  private String modelName;
  
  private String modelDir;
  
  private Map<String, String> factories = new HashMap<String, String>();
  
  private Map<String, String> packages = new HashMap<String, String>();
  
  /**
   * Build the generator with 4 parameters
   * gm : the GMAGenModelImpl correctly initialized with cPattern, iPattern and srcDir
   */
  public GenerateDevStructure(final GMAGenModel gm) {
    try {
      boolean _contains = ArrayExtensions.contains(Platform.getApplicationArgs(), GMAConstants.PARAM_DEBUG_MODE);
      if (_contains) {
        this.debug = true;
      }
      this.gmaGenModel = gm;
      String _copyrightText = gm.getCopyrightText();
      boolean _tripleNotEquals = (_copyrightText != null);
      if (_tripleNotEquals) {
        this.copyright = this.computeCopyrightComment().toString();
      }
      this.classPattern = gm.getDevClassPattern();
      this.interfacePattern = gm.getDevInterfacePattern();
      this.srcDevDirectory = gm.getSrcDir();
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
      this.filesNotGenerated.clear();
      ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_BUILD);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Generate the file structure. If genFiles is false just compute the files to be generated
   */
  public void generateDevStructure(final boolean genFiles) {
    try {
      this.generateFiles = genFiles;
      EList<GenPackage> _genPackages = this.gmaGenModel.getGenPackages();
      for (final GenPackage p : _genPackages) {
        this.generateDevStructure(p);
      }
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
    final Function1<GenClass, Boolean> _function = (GenClass it) -> {
      boolean _isDynamic = it.isDynamic();
      return Boolean.valueOf((!_isDynamic));
    };
    final Function1<GenClass, Boolean> _function_1 = (GenClass p) -> {
      boolean _isMapType = GenerateCommon.isMapType(p);
      return Boolean.valueOf((!_isMapType));
    };
    Iterable<GenClass> _filter = IterableExtensions.<GenClass>filter(IterableExtensions.<GenClass>filter(gp.getGenClasses(), _function), _function_1);
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
    final int nbClasses = IterableExtensions.size(Iterables.<EClass>filter(gp.getEcorePackage().getEClassifiers(), EClass.class));
    if ((nbClasses > 0)) {
      this.generateOverriddenFactoryInterface(gp, interfaceAbsolutePath);
      this.generateOverriddenFactoryClass(gp, srcAbsolutePath);
      this.generateOverriddenPackageInterface(gp, interfaceAbsolutePath);
      String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
      String _plus_2 = (_computePackageNameForClasses + ".");
      String _computeFactoryClassName = this.computeFactoryClassName(gp);
      final String factoryClassName = (_plus_2 + _computeFactoryClassName);
      final String packageClassName = gp.getQualifiedPackageInterfaceName();
      this.factories.put(gp.getEcorePackage().getNsURI(), factoryClassName);
      this.packages.put(gp.getEcorePackage().getNsURI(), packageClassName);
    }
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
   * This method initializes the genModel with convenient values
   */
  public void initializeGenModelConvenientProperties() {
    this.gmaGenModel.setImportOrganizing(true);
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
    final AntRunner runner = new AntRunner();
    runner.setBuildFileLocation(f.getAbsolutePath());
    if (this.debug) {
      runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
      runner.setArguments("-verbose -debug");
    }
    try {
      if (this.debug) {
        String _absolutePath = f.getAbsolutePath();
        String _plus = ("  --> Generate the EMF Code using the ant file : " + _absolutePath);
        InputOutput.<String>println(_plus);
      }
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
  
  public void generateExtensions() {
    final GenerateExtensions gfoe = new GenerateExtensions(this.project);
    gfoe.generateOrUpdateExtensions(this.factories, this.packages);
  }
  
  public void generateAll(final String antFilename) {
    this.initializeGenModelConvenientProperties();
    this.generateDevStructure(true);
    final File antFile = this.generateAntFile(antFilename);
    final NullProgressMonitor npm = new NullProgressMonitor();
    this.generateGenModelCode(antFile, npm);
    this.generateExtensions();
    this.refreshWorkspace();
  }
  
  public String setClassPattern(final String cp) {
    return this.classPattern = cp;
  }
  
  public String getClassPattern() {
    return this.classPattern;
  }
  
  public String setInterfacePattern(final String ip) {
    return this.interfacePattern = ip;
  }
  
  public String getInterfacePattern() {
    return this.interfacePattern;
  }
  
  private boolean waitingForRefresh = false;
  
  public void refreshWorkspace() {
    try {
      try {
        this.waitingForRefresh = true;
        ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
        int nbWait = 0;
        while ((this.waitingForRefresh && (nbWait < 3))) {
          {
            Thread.sleep(2000);
            nbWait = (nbWait + 1);
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof CoreException) {
          final CoreException e = (CoreException)_t;
          e.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      } finally {
        PDECore.getDefault().getModelManager().bundleRootChanged(this.project);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public void resourceChanged(final IResourceChangeEvent event) {
    this.waitingForRefresh = false;
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
    {
      String[] _usedGenericInterfaceNames = this.getUsedGenericInterfaceNames(this.computeClassname(gc));
      for(final String name : _usedGenericInterfaceNames) {
        _builder.append("import ");
        String _computePackageNameForInterfaces_1 = this.computePackageNameForInterfaces(gc.getGenPackage());
        _builder.append(_computePackageNameForInterfaces_1);
        _builder.append(".");
        _builder.append(name);
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("// This class overrides the generated class and will be instantiated by factory");
    _builder.newLine();
    _builder.append("public class ");
    String _computeClassname = this.computeClassname(gc);
    _builder.append(_computeClassname);
    _builder.append(" extends ");
    String _computeGeneratedClassName = this.computeGeneratedClassName(gc, false);
    _builder.append(_computeGeneratedClassName);
    _builder.append(" implements ");
    String _computeInterfaceName = this.computeInterfaceName(gc, false);
    _builder.append(_computeInterfaceName);
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * return the list of interface names found in a generic class name
   * for 'Project' -> returns empty array.
   * for 'Project<T> -> returns empty array
   * for 'ProjectFolder<T extends Project> returns Project
   * for 'ProjectFolder<T extends Project1 & Project2> returns { "Project1", "Project2"}
   */
  public String[] getUsedGenericInterfaceNames(final String name) {
    List<String> _xblockexpression = null;
    {
      final int pos = name.indexOf(" extends ");
      if ((pos == (-1))) {
        return ((String[])Conversions.unwrapArray(Collections.<String>emptyList(), String.class));
      }
      int _length = " extends ".length();
      int _plus = (pos + _length);
      final String afterExtends = name.substring(_plus).replace(">", "");
      final Function<String, String> _function = (String it) -> {
        return it.trim();
      };
      _xblockexpression = ((List<String>)Conversions.doWrapArray(afterExtends.split("&"))).stream().<String>map(_function).collect(Collectors.<String>toList());
    }
    return ((String[])Conversions.unwrapArray(_xblockexpression, String.class));
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
    String _factoryInterfaceName = gp.getFactoryInterfaceName();
    _builder.append(_factoryInterfaceName);
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
    String _packageInterfaceName = gp.getPackageInterfaceName();
    _builder.append(_packageInterfaceName);
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
    _builder.append("import ");
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
    _builder.append(_computePackageNameForInterfaces);
    _builder.append(".");
    String _computeFactoryInterfaceName = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.newLine();
    _builder.append("// This factory  renames the generated factory interface to use it as an overriden factory");
    _builder.newLine();
    _builder.append("public class ");
    String _computeFactoryClassName = this.computeFactoryClassName(gp);
    _builder.append(_computeFactoryClassName);
    _builder.append(" extends ");
    String _factoryClassName = gp.getFactoryClassName();
    _builder.append(_factoryClassName);
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
    String _factoryClassName_1 = gp.getFactoryClassName();
    _builder.append(_factoryClassName_1, "\t\t\t");
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
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence computeCopyrightComment() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (((this.gmaGenModel.getCopyrightText() != null) && (this.gmaGenModel.getCopyrightText().length() > 0))) {
        _builder.append("/**");
        _builder.newLine();
        _builder.append("\t ");
        _builder.append("* ");
        String _copyrightText = this.gmaGenModel.getCopyrightText();
        _builder.append(_copyrightText, "\t ");
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
    return MessageFormat.format(this.classPattern, gc.getEcoreClass().getName());
  }
  
  /**
   * Compute the interface name to be generated
   */
  public String computeInterfaceFilename(final GenClass gc) {
    return MessageFormat.format(this.interfacePattern, gc.getEcoreClass().getName());
  }
  
  /**
   * Compute the class name to be generated
   */
  public String computeClassname(final GenClass gc) {
    String _computeClassFilename = this.computeClassFilename(gc);
    Object _computeGenericTypes = this.computeGenericTypes(gc.getEcoreClass());
    return (_computeClassFilename + _computeGenericTypes);
  }
  
  public String computeInterfaceName(final GenClass gc) {
    return this.computeInterfaceName(gc, true);
  }
  
  /**
   * Compute the interface name to be generated
   */
  public String computeInterfaceName(final GenClass gc, final boolean addExtend) {
    String _computeInterfaceFilename = this.computeInterfaceFilename(gc);
    Object _computeGenericTypes = this.computeGenericTypes(gc.getEcoreClass(), addExtend);
    return (_computeInterfaceFilename + _computeGenericTypes);
  }
  
  public Object computeGenericTypes(final EClass c) {
    return this.computeGenericTypes(c, true);
  }
  
  public Object computeGenericTypes(final EClass c, final boolean addExtends) {
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
        if (addExtends) {
          String prefix = " extends ";
          EList<EGenericType> _eBounds = pt.getEBounds();
          for (final EGenericType gb : _eBounds) {
            {
              sb.append(prefix).append(gb.getEClassifier().getName());
              prefix = " & ";
            }
          }
        }
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
    return MessageFormat.format(this.interfacePattern, gp.getFactoryName());
  }
  
  /**
   * Compute the factory interface name to be generated
   */
  public String computePackageInterfaceName(final GenPackage gp) {
    return MessageFormat.format(this.interfacePattern, gp.getBasicPackageName());
  }
  
  /**
   * Compute the factory class name to be generated
   */
  public String computeFactoryClassName(final GenPackage gp) {
    return MessageFormat.format(this.classPattern, gp.getFactoryName());
  }
  
  /**
   * Compute the package name for class
   */
  public String computePackageNameForClasses(final GenPackage gp) {
    String _xblockexpression = null;
    {
      String _xifexpression = null;
      String _basePackage = gp.getBasePackage();
      boolean _tripleEquals = (_basePackage == null);
      if (_tripleEquals) {
        _xifexpression = "";
      } else {
        String _basePackage_1 = gp.getBasePackage();
        _xifexpression = (_basePackage_1 + ".");
      }
      final String basePackage = _xifexpression;
      String _xifexpression_1 = null;
      String _classPackageSuffix = gp.getClassPackageSuffix();
      boolean _tripleEquals_1 = (_classPackageSuffix == null);
      if (_tripleEquals_1) {
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
      boolean _tripleEquals = (_basePackage == null);
      if (_tripleEquals) {
        _xifexpression = "";
      } else {
        String _basePackage_1 = gp.getBasePackage();
        _xifexpression = (_basePackage_1 + ".");
      }
      final String basePackage = _xifexpression;
      String _xifexpression_1 = null;
      if (((gp.getInterfacePackageSuffix() == null) || (gp.getInterfacePackageSuffix().length() == 0))) {
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
  public String computeGeneratedClassName(final GenClass gc, final boolean addExtend) {
    String _className = gc.getClassName();
    Object _computeGenericTypes = this.computeGenericTypes(gc.getEcoreClass(), addExtend);
    return (_className + _computeGenericTypes);
  }
  
  public String computeGeneratedClassName(final GenClass gc) {
    return this.computeGeneratedClassName(gc, true);
  }
  
  /**
   * Compute the generated interface name depending on interfacePattern.
   */
  public String computeGeneratedInterfaceName(final GenClass c) {
    String _interfaceName = c.getInterfaceName();
    Object _computeGenericTypes = this.computeGenericTypes(c.getEcoreClass(), false);
    return (_interfaceName + _computeGenericTypes);
  }
}
