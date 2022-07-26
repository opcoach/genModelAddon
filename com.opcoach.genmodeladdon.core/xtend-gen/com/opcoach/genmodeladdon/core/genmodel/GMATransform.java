package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.GMAConstants;
import com.opcoach.genmodeladdon.core.GMAHelper;
import com.opcoach.genmodeladdon.core.GenerateCommon;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.eclipse.emf.codegen.ecore.genmodel.GenBase;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.InputOutput;

/**
 * This class computes the new names for generated classes according to pattern name matching
 */
@SuppressWarnings("all")
public class GMATransform implements GMAConstants {
  protected Map<String, String> devNames = new TreeMap<String, String>();

  private String devInterfaceNamePattern = GMAConstants.DEFAULT_SRC_INTERFACE_PATTERN;

  private String genInterfaceNamePattern = GMAConstants.DEFAULT_GEN_INTERFACE_PATTERN;

  private String devClassNamePattern = GMAConstants.DEFAULT_SRC_CLASS_IMPL_PATTERN;

  private String genClassNamePattern = GMAConstants.DEFAULT_GEN_CLASS_IMPL_PATTERN;

  private GMAGenModel gm;

  private boolean isInit = false;

  public GMATransform(final GMAGenModel gm) {
    this.gm = gm;
    this.initPatterns();
  }

  private GMATransform initPatterns() {
    GMATransform _xblockexpression = null;
    {
      String _xifexpression = null;
      String _classNamePattern = this.gm.getClassNamePattern();
      boolean _tripleEquals = (_classNamePattern == null);
      if (_tripleEquals) {
        _xifexpression = GMAConstants.DEFAULT_GEN_CLASS_IMPL_PATTERN;
      } else {
        _xifexpression = this.gm.getClassNamePattern();
      }
      this.genClassNamePattern = _xifexpression;
      String _xifexpression_1 = null;
      String _interfaceNamePattern = this.gm.getInterfaceNamePattern();
      boolean _tripleEquals_1 = (_interfaceNamePattern == null);
      if (_tripleEquals_1) {
        _xifexpression_1 = GMAConstants.DEFAULT_GEN_INTERFACE_PATTERN;
      } else {
        _xifexpression_1 = this.gm.getInterfaceNamePattern();
      }
      this.genInterfaceNamePattern = _xifexpression_1;
      this.devClassNamePattern = this.gm.getDevClassPattern();
      this.devInterfaceNamePattern = this.gm.getDevInterfacePattern();
      _xblockexpression = InputOutput.<GMATransform>println(this);
    }
    return _xblockexpression;
  }

  @Override
  public synchronized String toString() {
    String _xblockexpression = null;
    {
      Object _xifexpression = null;
      Resource _eResource = this.gm.getGenModel().eResource();
      boolean _tripleEquals = (_eResource == null);
      if (_tripleEquals) {
        _xifexpression = this.gm.getGenModel().toString();
      } else {
        _xifexpression = this.gm.getGenModel().eResource().getURI();
      }
      final Object title = _xifexpression;
      InputOutput.<String>println(("GmaTransform for : " + title));
      InputOutput.<String>println(("   -> genClassNamePattern : " + this.genClassNamePattern));
      InputOutput.<String>println(("   -> genInterfaceNamePattern : " + this.genInterfaceNamePattern));
      InputOutput.<String>println(("   -> devClassNamePattern : " + this.devClassNamePattern));
      _xblockexpression = InputOutput.<String>println(("   -> devInterfaceNamePattern : " + this.devInterfaceNamePattern));
    }
    return _xblockexpression;
  }

  public boolean init() {
    boolean _xifexpression = false;
    if ((!this.isInit)) {
      boolean _xblockexpression = false;
      {
        EList<GenPackage> _genPackages = this.gm.getGenPackages();
        for (final GenPackage gp : _genPackages) {
          this.computeNames(gp.getEcorePackage());
        }
        EList<GenPackage> _usedGenPackages = this.gm.getUsedGenPackages();
        for (final GenPackage gp_1 : _usedGenPackages) {
          this.computeNames(gp_1.getEcorePackage());
        }
        _xblockexpression = this.isInit = true;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }

  public boolean clear() {
    boolean _xblockexpression = false;
    {
      this.devNames.clear();
      _xblockexpression = this.isInit = false;
    }
    return _xblockexpression;
  }

  public void computeNames(final EPackage p) {
    boolean _equals = EcorePackage.eNS_URI.equals(p.getNsURI());
    boolean _not = (!_equals);
    if (_not) {
      EList<EClassifier> _eClassifiers = p.getEClassifiers();
      for (final EClassifier c : _eClassifiers) {
        if ((((c instanceof EClass) && (!c.getName().endsWith("Package"))) && (!GenerateCommon.isMapType(c)))) {
          final String devIntName = MessageFormat.format(this.devInterfaceNamePattern, c.getName());
          final String genIntName = MessageFormat.format(this.genInterfaceNamePattern, c.getName());
          this.devNames.put(genIntName, devIntName);
          final String genClassName = MessageFormat.format(this.genClassNamePattern, c.getName());
          final String devClassName = MessageFormat.format(this.devClassNamePattern, c.getName());
          this.devNames.put(genClassName, devClassName);
        }
      }
      EList<EPackage> _eSubpackages = p.getESubpackages();
      for (final EPackage childPackage : _eSubpackages) {
        this.computeNames(childPackage);
      }
    }
  }

  /**
   * Replace the development name (a key in the map) as many times as necessary.
   * But it must be replaced only if the key is found with not a letter before or after.
   *  Examples :
   * <blockquote>     public class MProject  -->   public class Project  </blockquote>
   *   <blockquote>   public class MyMProject extends MProject  --> public class MyMProject extends Project </blockquote>
   *   <blockquote>   public class MyMProject extends MProject implements YourMProject  --> public class MyMProject extends Project implements YourMProject  </blockquote>
   */
  public String replaceDevName(final String stringToTranslate) {
    StringBuffer res = new StringBuffer(stringToTranslate);
    Set<Map.Entry<String, String>> _entrySet = this.devNames.entrySet();
    for (final Map.Entry<String, String> entry : _entrySet) {
      {
        final String key = entry.getKey();
        final String value = entry.getValue();
        int s = 0;
        while ((res.indexOf(key, s) != (-1))) {
          {
            s = res.indexOf(key, s);
            int _length = key.length();
            final int e = (s + _length);
            final boolean startIsOk = ((s == 0) || (!Character.isLetterOrDigit(res.charAt((s - 1)))));
            final boolean endIsOk = ((e == res.length()) || (!Character.isLetterOrDigit(res.charAt(e))));
            if ((startIsOk && endIsOk)) {
              res = res.replace(s, e, value);
            }
            s = e;
          }
        }
      }
    }
    return res.toString();
  }

  /**
   * Transform a String with default computed names with the new names
   */
  public static String replaceDevName(final GenBase base, final String stringToTranslate) {
    String _xblockexpression = null;
    {
      boolean _GMACompliant = GMAHelper.GMACompliant(base);
      boolean _not = (!_GMACompliant);
      if (_not) {
        return stringToTranslate;
      }
      final GenModel genModel = base.getGenModel();
      GMATransform dt = null;
      if ((genModel instanceof GMAGenModel)) {
        final GMAGenModel gm = ((GMAGenModel) genModel);
        dt = gm.getGMATransform();
      }
      String _xifexpression = null;
      if ((dt != null)) {
        _xifexpression = dt.replaceDevName(stringToTranslate);
      } else {
        return stringToTranslate;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
