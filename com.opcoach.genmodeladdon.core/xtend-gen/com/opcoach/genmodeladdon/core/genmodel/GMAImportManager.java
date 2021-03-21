package com.opcoach.genmodeladdon.core.genmodel;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.codegen.util.ImportManager;

/**
 * An Import manager wrapper to override the emitImports
 */
@SuppressWarnings("all")
public class GMAImportManager extends ImportManager {
  private ImportManager dim = null;
  
  private GMAGenModel gm = null;
  
  public GMAImportManager(final ImportManager im, final GMAGenModel pgm, final String compilationUnitPackage) {
    super(compilationUnitPackage);
    this.dim = im;
    this.gm = pgm;
  }
  
  @Override
  public int hashCode() {
    return this.dim.hashCode();
  }
  
  @Override
  public boolean equals(final Object obj) {
    return this.dim.equals(obj);
  }
  
  @Override
  public String toString() {
    return this.dim.toString();
  }
  
  @Override
  public String getImportedName(final String qualifiedName, final boolean autoImport) {
    final String res = this.dim.getImportedName(GMATransform.replaceDevName(this.gm, qualifiedName), autoImport);
    return res;
  }
  
  @Override
  public String getImportedName(final String qualifiedName) {
    final String res = this.dim.getImportedName(GMATransform.replaceDevName(this.gm, qualifiedName));
    return res;
  }
  
  @Override
  public void addImport(final String packageName, final String shortName) {
    this.dim.addImport(packageName, GMATransform.replaceDevName(this.gm, shortName));
  }
  
  @Override
  public void addImport(final String qualifiedName) {
    final String rep = GMATransform.replaceDevName(this.gm, qualifiedName);
    this.dim.addImport(rep);
  }
  
  @Override
  public void addPseudoImport(final String qualifiedName) {
    this.dim.addPseudoImport(GMATransform.replaceDevName(this.gm, qualifiedName));
  }
  
  @Override
  public void addMasterImport(final String packageName, final String shortName) {
    this.dim.addMasterImport(GMATransform.replaceDevName(this.gm, packageName), GMATransform.replaceDevName(this.gm, shortName));
  }
  
  @Override
  public boolean hasImport(final String shortName) {
    return this.dim.hasImport(shortName);
  }
  
  @Override
  public Collection<String> getImports() {
    return this.dim.getImports();
  }
  
  @Override
  public String getLineDelimiter() {
    return this.dim.getLineDelimiter();
  }
  
  @Override
  public void setLineDelimiter(final String lineDelimiter) {
    this.dim.setLineDelimiter(lineDelimiter);
  }
  
  @Override
  public String computeSortedImports() {
    return GMATransform.replaceDevName(this.gm, this.dim.computeSortedImports());
  }
  
  @Override
  public void addCompilationUnitImports(final String compilationUnitContents) {
    this.dim.addCompilationUnitImports(GMATransform.replaceDevName(this.gm, compilationUnitContents));
  }
  
  @Override
  public void markImportLocation(final StringBuilder stringBuilder) {
    this.dim.markImportLocation(stringBuilder);
  }
  
  @Override
  public void markImportLocation(final StringBuffer stringBuffer) {
    this.dim.markImportLocation(stringBuffer);
  }
  
  @Override
  public void emitSortedImports() {
    this.dim.emitSortedImports();
  }
  
  @Override
  public void addJavaLangImports(final List<String> javaLangClassNames) {
    this.dim.addJavaLangImports(javaLangClassNames);
  }
}
