package com.opcoach.genmodeladdon.core;

import com.google.common.base.Objects;
import com.opcoach.genmodeladdon.core.GenerateFactoryOverrideExtension;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class GenerateDevStructure {
  private String classPattern;
  
  private String interfacePattern;
  
  private String srcDevDirectory;
  
  private String projectName;
  
  private GenModel genModel;
  
  public Map<String, Object> filesNotGenerated = new HashMap<String, Object>();
  
  /**
   * Build the generator with 2 parameters
   * @param cpattern : the class name pattern used for generation ({0}Impl for instance)
   * @param ipattern : the interface name pattern used for generation ({0} for instance)
   */
  public GenerateDevStructure(final GenModel gm, final String cPattern, final String iPattern, final String srcDir) {
    this.genModel = gm;
    this.classPattern = cPattern;
    this.interfacePattern = iPattern;
    this.srcDevDirectory = srcDir;
    String _extractProjectName = this.extractProjectName(gm);
    this.projectName = _extractProjectName;
    this.filesNotGenerated.clear();
  }
  
  public GenerateDevStructure(final GenModel gm) {
    this(gm, "{0}ExtImpl", "{0}Ext", "src");
  }
  
  public void generateDevStructure() {
    EList<GenPackage> _genPackages = this.genModel.getGenPackages();
    for (final GenPackage p : _genPackages) {
      this.generateDevStructure(p);
    }
  }
  
  public void generateDevStructure(final GenPackage gp) {
    try {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      final IWorkspaceRoot root = _workspace.getRoot();
      final IProject proj = root.getProject(this.projectName);
      String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
      String _replace = _computePackageNameForClasses.replace(".", "/");
      String _plus = ((this.srcDevDirectory + "/") + _replace);
      final IFolder srcFolder = proj.getFolder(_plus);
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
      final IFolder interfaceFolder = proj.getFolder(_plus_1);
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
        public Boolean apply(final GenClass it) {
          boolean _isDynamic = it.isDynamic();
          return Boolean.valueOf((!_isDynamic));
        }
      };
      Iterable<GenClass> _filter = IterableExtensions.<GenClass>filter(_genClasses, _function);
      for (final GenClass c : _filter) {
        {
          this.generateOverridenClass(c, srcAbsolutePath);
          this.generateOverridenInterface(c, interfaceAbsolutePath);
        }
      }
      this.generateOverridenFactoryInterface(gp, interfaceAbsolutePath);
      this.generateOverridenFactoryClass(gp, srcAbsolutePath);
      proj.refreshLocal(IResource.DEPTH_INFINITE, null);
      final GenerateFactoryOverrideExtension gfoe = new GenerateFactoryOverrideExtension(this.projectName);
      EPackage _ecorePackage = gp.getEcorePackage();
      String _nsURI = _ecorePackage.getNsURI();
      String _computePackageNameForClasses_1 = this.computePackageNameForClasses(gp);
      String _plus_2 = (_computePackageNameForClasses_1 + ".");
      String _computeFactoryClassName = this.computeFactoryClassName(gp);
      String _plus_3 = (_plus_2 + _computeFactoryClassName);
      gfoe.generateOverideExtension(_nsURI, _plus_3);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public Object generateOverridenFactoryInterface(final GenPackage gp, final String path) {
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
  
  public Object generateOverridenFactoryClass(final GenPackage gp, final String path) {
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
  
  public Object generateOverridenClass(final GenClass gc, final String path) {
    String _computeClassname = this.computeClassname(gc);
    String _plus = (path + _computeClassname);
    String _plus_1 = (_plus + ".java");
    CharSequence _generateClassContent = this.generateClassContent(gc);
    return this.generateFile(_plus_1, _generateClassContent);
  }
  
  public Object generateOverridenInterface(final GenClass gc, final String path) {
    String _computeInterfaceName = this.computeInterfaceName(gc);
    String _plus = (path + _computeInterfaceName);
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
          final FileWriter fw = new FileWriter(filename);
          String _string = contents.toString();
          fw.write(_string);
          fw.flush();
          fw.close();
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
    String _computeInterfaceName = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("// This class can override the generated class and will be instantiated by factory");
    _builder.newLine();
    _builder.append("public class ");
    String _computeClassname = this.computeClassname(gc);
    _builder.append(_computeClassname, "");
    _builder.append(" extends ");
    String _computeGeneratedClassName = this.computeGeneratedClassName(gc);
    _builder.append(_computeGeneratedClassName, "");
    _builder.append(" implements ");
    String _computeInterfaceName_1 = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName_1, "");
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
    _builder.append("package ");
    GenPackage _genPackage = gc.getGenPackage();
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(_genPackage);
    _builder.append(_computePackageNameForInterfaces, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("// This interface can override the generated interface and will be returned by factory");
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
    _builder.append("package ");
    String _computePackageNameForInterfaces = this.computePackageNameForInterfaces(gp);
    _builder.append(_computePackageNameForInterfaces, "");
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
    _builder.append("/** Provide a getInstance method to get the factory in the correct type.");
    _builder.newLine();
    _builder.append("\t  ");
    _builder.append("* The eINSTANCE has been overriden with the correct type declared ");
    _builder.newLine();
    _builder.append("\t  ");
    _builder.append("* in the override_factory extension ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static ");
    String _computeFactoryInterfaceName_1 = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName_1, "\t");
    _builder.append(" getInstance() { return (");
    String _computeFactoryInterfaceName_2 = this.computeFactoryInterfaceName(gp);
    _builder.append(_computeFactoryInterfaceName_2, "\t");
    _builder.append(") eINSTANCE; }");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    {
      EList<GenClass> _genClasses = gp.getGenClasses();
      final Function1<GenClass, Boolean> _function = new Function1<GenClass, Boolean>() {
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
  
  public CharSequence generateFactoryDef(final GenClass gc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    String _computeInterfaceName = this.computeInterfaceName(gc);
    _builder.append(_computeInterfaceName, "");
    _builder.append(" create");
    EClass _ecoreClass = gc.getEcoreClass();
    String _name = _ecoreClass.getName();
    _builder.append(_name, "");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence generateClassFactoryContent(final GenPackage gp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _computePackageNameForClasses = this.computePackageNameForClasses(gp);
    _builder.append(_computePackageNameForClasses, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      EList<GenClass> _genClasses = gp.getGenClasses();
      final Function1<GenClass, Boolean> _function = new Function1<GenClass, Boolean>() {
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
        String _computeInterfaceName = this.computeInterfaceName(gc);
        _builder.append(_computeInterfaceName, "");
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
    {
      EList<GenClass> _genClasses_1 = gp.getGenClasses();
      final Function1<GenClass, Boolean> _function_1 = new Function1<GenClass, Boolean>() {
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
    _builder.append(_computeInterfaceName, "");
    _builder.append(" create");
    EClass _ecoreClass = gc.getEcoreClass();
    String _name = _ecoreClass.getName();
    _builder.append(_name, "");
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
  
  /**
   * Compute the class name to be generated
   */
  public String computeClassname(final GenClass gc) {
    EClass _ecoreClass = gc.getEcoreClass();
    String _name = _ecoreClass.getName();
    return this.classPattern.replace("{0}", _name);
  }
  
  /**
   * Compute the interface name to be generated
   */
  public String computeInterfaceName(final GenClass gc) {
    EClass _ecoreClass = gc.getEcoreClass();
    String _name = _ecoreClass.getName();
    return this.interfacePattern.replace("{0}", _name);
  }
  
  /**
   * Compute the factory interface name to be generated
   */
  public String computeFactoryInterfaceName(final GenPackage gp) {
    String _prefix = gp.getPrefix();
    return (_prefix + "Factory");
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
        _xifexpression = classPattern.replace("{0}", _name);
      } else {
        EClass _ecoreClass_1 = c.getEcoreClass();
        String _name_1 = _ecoreClass_1.getName();
        _xifexpression = (_name_1 + "Impl");
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
        _xifexpression = interfaceNamePattern.replace("{0}", _name);
      } else {
        EClass _ecoreClass_1 = c.getEcoreClass();
        _xifexpression = _ecoreClass_1.getName();
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
  
  private String extractProjectName(final GenModel gm) {
    Resource _eResource = gm.eResource();
    URI _uRI = _eResource.getURI();
    String _plus = ("URI of genmodel : " + _uRI);
    InputOutput.<String>println(_plus);
    Resource _eResource_1 = gm.eResource();
    URI _uRI_1 = _eResource_1.getURI();
    final String genModelUri = _uRI_1.toString();
    final String nameStartingWithProjectName = genModelUri.replace("platform:/resource/", "");
    final int pos = nameStartingWithProjectName.indexOf("/");
    return nameStartingWithProjectName.substring(0, pos);
  }
}
