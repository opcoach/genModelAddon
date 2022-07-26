package com.opcoach.genmodeladdon.core.genmodel.impl;

import com.google.common.base.Objects;
import com.opcoach.genmodeladdon.core.GMAConstants;
import com.opcoach.genmodeladdon.core.GMAHelper;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;
import com.opcoach.genmodeladdon.core.genmodel.GMAImportManager;
import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.Properties;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelImpl;
import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class GMAGenModelImpl extends GenModelImpl implements GMAGenModel, GMAConstants {
  private GMATransform gmaTransform = null;

  private Properties properties;

  private String propertyFileName = null;

  private String cPattern;

  private String iPattern;

  private String srcDir;

  private Boolean genEMFCode;

  @Override
  public GMATransform getGMATransform() {
    if ((this.gmaTransform == null)) {
      GMATransform _gMATransform = new GMATransform(this);
      this.gmaTransform = _gMATransform;
      this.gmaTransform.init();
      EList<GenPackage> _usedGenPackages = this.getUsedGenPackages();
      for (final GenPackage genPackage : _usedGenPackages) {
        {
          final GenModel gm = genPackage.getGenModel();
          if ((gm instanceof GMAGenModelImpl)) {
            ((GMAGenModelImpl)gm).setGMATransform(this.gmaTransform);
          }
        }
      }
      EList<GenPackage> _staticGenPackages = this.getStaticGenPackages();
      for (final GenPackage genPackage_1 : _staticGenPackages) {
        {
          final GenModel gm = genPackage_1.getGenModel();
          if ((gm instanceof GMAGenModelImpl)) {
            ((GMAGenModelImpl)gm).setGMATransform(this.gmaTransform);
          }
        }
      }
      final GenPackage ecore = this.getEcoreGenPackage();
      if (((ecore != null) && (ecore.getGenModel() instanceof GMAGenModelImpl))) {
        GenModel _genModel = ecore.getGenModel();
        final GMAGenModelImpl gm = ((GMAGenModelImpl) _genModel);
        GMATransform _gMATransform_1 = gm.getGMATransform();
        boolean _tripleNotEquals = (_gMATransform_1 != this.gmaTransform);
        if (_tripleNotEquals) {
          gm.setGMATransform(this.gmaTransform);
        }
      }
      final GenPackage xmlType = this.getXMLTypeGenPackage();
      if (((xmlType != null) && (xmlType.getGenModel() instanceof GMAGenModelImpl))) {
        GenModel _genModel_1 = xmlType.getGenModel();
        final GMAGenModelImpl gm_1 = ((GMAGenModelImpl) _genModel_1);
        GMATransform _gMATransform_2 = gm_1.getGMATransform();
        boolean _tripleNotEquals_1 = (_gMATransform_2 != this.gmaTransform);
        if (_tripleNotEquals_1) {
          gm_1.setGMATransform(this.gmaTransform);
        }
      }
      final GenPackage xmlNamespace = this.getXMLNamespaceGenPackage();
      if (((xmlNamespace != null) && (xmlNamespace.getGenModel() instanceof GMAGenModelImpl))) {
        GenModel _genModel_2 = xmlNamespace.getGenModel();
        final GMAGenModelImpl gm_2 = ((GMAGenModelImpl) _genModel_2);
        GMATransform _gMATransform_3 = gm_2.getGMATransform();
        boolean _tripleNotEquals_2 = (_gMATransform_3 != this.gmaTransform);
        if (_tripleNotEquals_2) {
          gm_2.setGMATransform(this.gmaTransform);
        }
      }
    }
    return this.gmaTransform;
  }

  public GMATransform setGMATransform(final GMATransform gmat) {
    return this.gmaTransform = gmat;
  }

  @Override
  public void emitSortedImports() {
    super.emitSortedImports();
  }

  @Override
  public void setImportManager(final ImportManager im) {
    if ((im == null)) {
      return;
    }
    ImportManager _importManager = this.getImportManager();
    boolean _tripleNotEquals = (_importManager != im);
    if (_tripleNotEquals) {
      if ((im instanceof GMAImportManager)) {
        super.setImportManager(im);
      } else {
        final GMAImportManager delegatedImportManager = new GMAImportManager(im, this, "");
        super.setImportManager(delegatedImportManager);
      }
    }
  }

  @Override
  public String getImportedName(final String qualifiedName) {
    String _xblockexpression = null;
    {
      final String pname = super.getImportedName(qualifiedName);
      boolean _GMACompliant = GMAHelper.GMACompliant(this);
      boolean _not = (!_GMACompliant);
      if (_not) {
        return pname;
      }
      final String result = this.getGMATransform().replaceDevName(pname);
      boolean _notEquals = (!Objects.equal(result, pname));
      if (_notEquals) {
        InputOutput.<String>println(((("--->> genModelImportedName convert " + pname) + " into ") + result));
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }

  @Override
  public void setDevClassPattern(final String cpattern) {
    this.cPattern = cpattern;
    this.setProperty(GMAConstants.PROP_DEV_CLASS_PATTERN, cpattern);
  }

  @Override
  public void setDevInterfacePattern(final String ipattern) {
    this.iPattern = ipattern;
    this.setProperty(GMAConstants.PROP_DEV_INTERFACE_PATTERN, ipattern);
  }

  @Override
  public String getDevClassPattern() {
    String _xblockexpression = null;
    {
      if ((this.cPattern == null)) {
        this.cPattern = this.getProperty(GMAConstants.PROP_DEV_CLASS_PATTERN);
        if ((this.cPattern == null)) {
          String _xifexpression = null;
          if (((this.classNamePattern == null) || 
            GMAConstants.DEFAULT_GEN_CLASS_IMPL_PATTERN.equals(this.classNamePattern))) {
            _xifexpression = GMAConstants.DEFAULT_SRC_CLASS_IMPL_PATTERN;
          } else {
            _xifexpression = GMAConstants.ADVISED_DEV_CLASS_IMPL_PATTERN;
          }
          this.cPattern = _xifexpression;
        }
      }
      _xblockexpression = this.cPattern;
    }
    return _xblockexpression;
  }

  @Override
  public String getDevInterfacePattern() {
    String _xblockexpression = null;
    {
      if ((this.iPattern == null)) {
        this.iPattern = this.getProperty(GMAConstants.PROP_DEV_INTERFACE_PATTERN);
        if ((this.iPattern == null)) {
          String _xifexpression = null;
          if (((this.interfaceNamePattern == null) || 
            GMAConstants.DEFAULT_GEN_INTERFACE_PATTERN.equals(this.interfaceNamePattern))) {
            _xifexpression = GMAConstants.DEFAULT_SRC_INTERFACE_PATTERN;
          } else {
            _xifexpression = GMAConstants.ADVISED_DEV_INTERFACE_PATTERN;
          }
          this.iPattern = _xifexpression;
        }
      }
      _xblockexpression = this.iPattern;
    }
    return _xblockexpression;
  }

  public Object readProperties() {
    Object _xifexpression = null;
    if ((this.properties == null)) {
      Object _xblockexpression = null;
      {
        Properties _properties = new Properties();
        this.properties = _properties;
        Object _xtrycatchfinallyexpression = null;
        try {
          String _propertyFilename = this.getPropertyFilename();
          final FileReader fr = new FileReader(_propertyFilename);
          this.properties.load(fr);
        } catch (final Throwable _t) {
          if (_t instanceof Exception) {
            _xtrycatchfinallyexpression = null;
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        _xblockexpression = _xtrycatchfinallyexpression;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }

  public void saveProperties() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(" ");
      _builder.append("This file contains the parameters for genModelAddon generation (gma) ");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("It is automaticaly generated when gma is used");
      _builder.newLine();
      _builder.append(" ");
      _builder.append("Store this file in your scm repository (git, svn ...)");
      final String comment = _builder.toString();
      String _propertyFilename = this.getPropertyFilename();
      final FileWriter fw = new FileWriter(_propertyFilename);
      this.properties.store(fw, comment);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  public String getPropertyFilename() {
    try {
      if ((this.propertyFileName == null)) {
        Resource _eResource = this.eResource();
        boolean _tripleNotEquals = (_eResource != null);
        if (_tripleNotEquals) {
          URI _uRI = this.eResource().getURI();
          final String gmafn = (_uRI + GMAConstants.GMA_EXT);
          boolean _startsWith = gmafn.startsWith("file:");
          if (_startsWith) {
            this.propertyFileName = gmafn.replaceFirst("file:", "");
          } else {
            boolean _startsWith_1 = gmafn.startsWith("platform:/resource/");
            if (_startsWith_1) {
              URL _uRL = new URL(gmafn);
              final URL u = FileLocator.resolve(_uRL);
              this.propertyFileName = u.toString().replaceFirst("file:", "");
            }
          }
        }
      }
      return this.propertyFileName;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  private String getProperty(final String qn) {
    String _xblockexpression = null;
    {
      this.readProperties();
      final Object v = this.properties.get(qn);
      String _xifexpression = null;
      if ((v != null)) {
        _xifexpression = v.toString();
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }

  private void setProperty(final String qn, final String value) {
    this.readProperties();
    this.properties.setProperty(qn, value);
    this.saveProperties();
  }

  @Override
  public void setSrcDir(final String dir) {
    this.srcDir = dir;
    this.setProperty(GMAConstants.PROP_SRCDIR, dir);
  }

  @Override
  public void setGenerateEMFCode(final boolean gen) {
    this.genEMFCode = Boolean.valueOf(gen);
    this.setProperty(GMAConstants.PROP_GENEMFCODE, Boolean.valueOf(gen).toString());
  }

  @Override
  public String getSrcDir() {
    if ((this.srcDir == null)) {
      this.srcDir = this.getProperty(GMAConstants.PROP_SRCDIR);
      if ((this.srcDir == null)) {
        this.srcDir = GMAConstants.DEFAULT_SRC_DEV;
      }
    }
    return this.srcDir;
  }

  @Override
  public boolean mustGenerateEMF() {
    if ((this.genEMFCode == null)) {
      String _property = this.getProperty(GMAConstants.PROP_GENEMFCODE);
      Boolean _boolean = new Boolean(_property);
      this.genEMFCode = _boolean;
    }
    return this.genEMFCode.booleanValue();
  }
}
