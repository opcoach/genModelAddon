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
    if ((v != null)) {
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
    EList<EClassifier> _eClassifiers = p.getEClassifiers();
    for (final EClassifier c : _eClassifiers) {
      if (((c instanceof EClass) && (!c.getName().endsWith("Package")))) {
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
  
  public String replaceDevName(final String stringToTranslate) {
    String res = stringToTranslate;
    Set<String> _keySet = this.devNames.keySet();
    for (final String key : _keySet) {
      boolean _contains = stringToTranslate.contains(key);
      if (_contains) {
        res = res.replaceAll(key, this.devNames.get(key));
      }
    }
    return res;
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
