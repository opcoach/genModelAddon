package com.opcoach.genmodeladdon.core.genmodel;

import com.google.common.base.Objects;
import com.opcoach.genmodeladdon.core.genmodel.GMAImportManager;
import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelImpl;
import org.eclipse.emf.codegen.util.ImportManager;

@SuppressWarnings("all")
public class GMAGenModelImpl extends GenModelImpl {
  private GMATransform gmaTransform = null;
  
  private ImportManager delegatedImportManager;
  
  public GMATransform getGMATransform() {
    if ((this.gmaTransform == null)) {
      GMATransform _gMATransform = new GMATransform(this);
      this.gmaTransform = _gMATransform;
    }
    this.gmaTransform.init();
    return this.gmaTransform;
  }
  
  @Override
  public void setImportManager(final ImportManager im) {
    final GMAImportManager dim = new GMAImportManager(im, this, "");
    super.setImportManager(dim);
  }
  
  @Override
  public ImportManager getImportManager() {
    boolean _equals = Objects.equal(this.delegatedImportManager, null);
    if (_equals) {
      ImportManager _importManager = super.getImportManager();
      GMAImportManager _gMAImportManager = new GMAImportManager(_importManager, this, "");
      this.delegatedImportManager = _gMAImportManager;
    }
    return this.delegatedImportManager;
  }
}
