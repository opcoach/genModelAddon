package com.opcoach.genmodeladdon.core.genmodel;

import com.google.common.base.Objects;
import com.opcoach.genmodeladdon.core.GMAConstants;
import com.opcoach.genmodeladdon.core.GenerateCommon;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.codegen.ecore.genmodel.GenBase;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.xbase.lib.InputOutput;

/**
 * This class computes the new names for generated classes according to pattern name matching
 */
@SuppressWarnings("all")
public class GMATransform implements GMAConstants {
  protected Map<String, String> devNames = new TreeMap<String, String>();
  
  private String devInterfaceNamePattern = GMAConstants.ADVISED_DEV_INTERFACE_PATTERN;
  
  private String genInterfaceNamePattern = GMAConstants.ADVISED_GEN_INTERFACE_PATTERN;
  
  private String devClassNamePattern = GMAConstants.ADVISED_DEV_CLASS_IMPL_PATTERN;
  
  private String genClassNamePattern = GMAConstants.ADVISED_GEN_CLASS_IMPL_PATTERN;
  
  private GMAGenModel gm;
  
  private boolean isInit = false;
  
  public GMATransform(final GMAGenModel gm) {
    this.gm = gm;
    String _devInterfacePattern = gm.getDevInterfacePattern();
    boolean _notEquals = (!Objects.equal(_devInterfacePattern, null));
    if (_notEquals) {
      this.devInterfaceNamePattern = gm.getDevInterfacePattern();
    }
    String _devClassPattern = gm.getDevClassPattern();
    boolean _notEquals_1 = (!Objects.equal(_devClassPattern, null));
    if (_notEquals_1) {
      this.devClassNamePattern = gm.getDevClassPattern();
    }
    String _interfaceNamePattern = gm.getInterfaceNamePattern();
    boolean _notEquals_2 = (!Objects.equal(_interfaceNamePattern, null));
    if (_notEquals_2) {
      this.genInterfaceNamePattern = gm.getInterfaceNamePattern();
    }
    String _classNamePattern = gm.getClassNamePattern();
    boolean _notEquals_3 = (!Objects.equal(_classNamePattern, null));
    if (_notEquals_3) {
      this.genClassNamePattern = gm.getClassNamePattern();
    }
  }
  
  private GMATransform(final GMAGenModel gm, final boolean mustInitNames) {
    this(gm);
    if (mustInitNames) {
      this.initNames();
    }
  }
  
  private void initValue(final String v, final Consumer<String> consumer) {
    if (((v != null) && (v.length() > 0))) {
      consumer.accept(v);
    }
  }
  
  private void initNames() {
    final Consumer<String> _function = (String v) -> {
      this.genClassNamePattern = v;
    };
    this.initValue(this.gm.getClassNamePattern(), _function);
    final Consumer<String> _function_1 = (String v) -> {
      this.genInterfaceNamePattern = v;
    };
    this.initValue(this.gm.getInterfaceNamePattern(), _function_1);
    final IFile f = GenerateCommon.getModelFile(this.gm);
    if ((f != null)) {
      final Consumer<String> _function_2 = (String v) -> {
        this.devClassNamePattern = v;
      };
      this.initValue(GenerateCommon.getProperty(f, GMAConstants.PROP_CLASS_PATTERN), _function_2);
      final Consumer<String> _function_3 = (String v) -> {
        this.devInterfaceNamePattern = v;
      };
      this.initValue(GenerateCommon.getProperty(f, GMAConstants.PROP_INTERFACE_PATTERN), _function_3);
    }
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
          InputOutput.<String>println(
            (((((("Put : " + genIntName) + ",") + devIntName) + " (used format : ") + this.devInterfaceNamePattern) + ")"));
          this.devNames.put(genIntName, devIntName);
          final String genClassName = MessageFormat.format(this.genClassNamePattern, c.getName());
          final String devClassName = MessageFormat.format(this.devClassNamePattern, c.getName());
          InputOutput.<String>println(((("Put : " + genClassName) + ",") + devClassName));
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
