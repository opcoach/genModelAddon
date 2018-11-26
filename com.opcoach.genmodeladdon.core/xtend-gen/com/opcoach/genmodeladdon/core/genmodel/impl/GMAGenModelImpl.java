package com.opcoach.genmodeladdon.core.genmodel.impl;

import com.google.common.base.Objects;
import com.opcoach.genmodeladdon.core.GMAConstants;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;
import com.opcoach.genmodeladdon.core.genmodel.GMAImportManager;
import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelImpl;
import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class GMAGenModelImpl extends GenModelImpl implements GMAGenModel {
  private GMATransform gmaTransform = null;
  
  private String cPattern = GMAConstants.ADVISED_DEV_CLASS_IMPL_PATTERN;
  
  private String iPattern = GMAConstants.ADVISED_DEV_INTERFACE_PATTERN;
  
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
  }
  
  @Override
  public void setDevInterfacePattern(final String ipattern) {
    this.iPattern = ipattern;
  }
  
  @Override
  public String getDevClassPattern() {
    return this.cPattern;
  }
  
  @Override
  public String getDevInterfacePattern() {
    return this.iPattern;
  }
}
