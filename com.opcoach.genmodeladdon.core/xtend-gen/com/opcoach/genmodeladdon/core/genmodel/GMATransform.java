package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.GMAConstants;
import com.opcoach.genmodeladdon.core.GenerateCommon;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModelImpl;
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
  
  private GenModel gm;
  
  private boolean isInit = false;
  
  public GMATransform(final GenModel gm) {
    this(gm, true);
  }
  
  private GMATransform(final GenModel gm, final boolean mustInitNames) {
    this.gm = gm;
    if (mustInitNames) {
      this.initNames();
    }
  }
  
  public GMATransform(final GenModel gm, final String devIntNamePattern, final String genIntNamePattern, final String devClassNamePattern, final String genClassNamePattern) {
    this(gm, false);
    this.devInterfaceNamePattern = devIntNamePattern;
    this.genInterfaceNamePattern = genIntNamePattern;
    this.devClassNamePattern = devClassNamePattern;
    this.genClassNamePattern = genClassNamePattern;
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
        if (((c instanceof EClass) && (!c.getName().endsWith("Package")))) {
          final String devIntName = MessageFormat.format(this.devInterfaceNamePattern, c.getName());
          final String genIntName = MessageFormat.format(this.genInterfaceNamePattern, c.getName());
          int _length = genIntName.length();
          boolean _equals_1 = (_length == 0);
          if (_equals_1) {
            InputOutput.<String>println(("Found an empty string for key for this devName " + devIntName));
          }
          InputOutput.<String>println(((("Put : " + genIntName) + ",") + devIntName));
          this.devNames.put(genIntName, devIntName);
          final String genClassName = MessageFormat.format(this.genClassNamePattern, c.getName());
          final String devClassName = MessageFormat.format(this.devClassNamePattern, c.getName());
          int _length_1 = genClassName.length();
          boolean _equals_2 = (_length_1 == 0);
          if (_equals_2) {
            InputOutput.<String>println(("Found an empty string for key for this devName " + this.devClassNamePattern));
          }
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
        int _length = key.length();
        boolean _equals = (_length == 0);
        if (_equals) {
          InputOutput.<String>println(("Found an empty key for this value :  " + value));
        } else {
          int s = 0;
          while ((res.indexOf(key, s) != (-1))) {
            {
              s = res.indexOf(key, s);
              int _length_1 = key.length();
              final int e = (s + _length_1);
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
    }
    return res.toString();
  }
  
  public static void main(final String[] args) {
    boolean _isLetterOrDigit = Character.isLetterOrDigit('.');
    String _plus = ("pour . " + Boolean.valueOf(_isLetterOrDigit));
    InputOutput.<String>println(_plus);
    boolean _isLetterOrDigit_1 = Character.isLetterOrDigit('<');
    String _plus_1 = ("pour < " + Boolean.valueOf(_isLetterOrDigit_1));
    InputOutput.<String>println(_plus_1);
    boolean _isLetterOrDigit_2 = Character.isLetterOrDigit('>');
    String _plus_2 = ("pour > " + Boolean.valueOf(_isLetterOrDigit_2));
    InputOutput.<String>println(_plus_2);
  }
  
  /**
   * Transform a String with default computed names with the new names
   */
  public static String replaceDevName(final GenBase base, final String stringToTranslate) {
    String _xblockexpression = null;
    {
      final GenModel genModel = base.getGenModel();
      GMATransform dt = null;
      if ((genModel instanceof GMAGenModelImpl)) {
        final GMAGenModelImpl gm = ((GMAGenModelImpl) genModel);
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
