package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMAImportManager;
import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import org.eclipse.emf.codegen.ecore.genmodel.impl.GenModelImpl;
import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class GMAGenModelImpl extends GenModelImpl {
  private GMATransform gmaTransform = null;
  
  private ImportManager delegatedImportManager;
  
  public GMATransform getGMATransform() {
    InputOutput.<String>println("Enter in getGMATransform ");
    if ((this.gmaTransform == null)) {
      GMATransform _gMATransform = new GMATransform(this);
      this.gmaTransform = _gMATransform;
    }
    this.gmaTransform.init();
    return this.gmaTransform;
  }
  
  @Override
  public void setImportManager(final ImportManager im) {
    if ((this.delegatedImportManager == null)) {
      if ((im instanceof GMAImportManager)) {
        this.delegatedImportManager = im;
      } else {
        GMAImportManager _gMAImportManager = new GMAImportManager(im, this, "");
        this.delegatedImportManager = _gMAImportManager;
      }
    }
  }
  
  @Override
  public ImportManager getImportManager() {
    if ((this.delegatedImportManager == null)) {
      InputOutput.<String>println("Create a GMAImportManager");
      ImportManager _importManager = super.getImportManager();
      GMAImportManager _gMAImportManager = new GMAImportManager(_importManager, this, "");
      this.delegatedImportManager = _gMAImportManager;
    }
    return this.delegatedImportManager;
  }
}
