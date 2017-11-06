package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMAGenModelImpl;
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

/**
 * This class computes the new names for generated classes according to pattern name matching
 */
@SuppressWarnings("all")
public class GMATransform {
  protected Map<String, String> devNames;
  
  private String devInterfaceNamePattern;
  
  private String genInterfaceNamePattern;
  
  private String devClassNamePattern;
  
  private String genClassNamePattern;
  
  private GenModel gm;
  
  private boolean isInit = false;
  
  public GMATransform(final GenModel gm) {
    this(gm, "{0}", "M{0}", "{0}Impl", "M{0}Impl");
  }
  
  public GMATransform(final GenModel gm, final String devInterfaceNamePattern, final String genInterfaceNamePattern, final String devClassNamePattern, final String genClassNamePattern) {
    super();
    this.devInterfaceNamePattern = devInterfaceNamePattern;
    this.genInterfaceNamePattern = genInterfaceNamePattern;
    this.devClassNamePattern = devClassNamePattern;
    this.genClassNamePattern = genClassNamePattern;
    this.gm = gm;
    TreeMap<String, String> _treeMap = new TreeMap<String, String>();
    this.devNames = _treeMap;
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
