package com.opcoach.genmodeladdon.core.genmodel;

import com.opcoach.genmodeladdon.core.genmodel.GMAGenModelImpl;
import com.opcoach.genmodeladdon.core.genmodel.GMATransform;
import java.io.FileWriter;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.codegen.util.ImportManager;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * An Import manager wrapper to override the emitImports
 */
@SuppressWarnings("all")
public class GMAImportManager extends ImportManager {
  private ImportManager dim = null;
  
  private GMAGenModelImpl gm = null;
  
  public GMAImportManager(final ImportManager im, final GMAGenModelImpl pgm, final String compilationUnitPackage) {
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
    return this.dim.getImportedName(qualifiedName, autoImport);
  }
  
  @Override
  public String getImportedName(final String qualifiedName) {
    return this.dim.getImportedName(qualifiedName);
  }
  
  @Override
  public void addImport(final String packageName, final String shortName) {
    this.dim.addImport(packageName, shortName);
  }
  
  @Override
  public void addImport(final String qualifiedName) {
    this.dim.addImport(qualifiedName);
  }
  
  @Override
  public void addPseudoImport(final String qualifiedName) {
    this.dim.addPseudoImport(qualifiedName);
  }
  
  @Override
  public void addMasterImport(final String packageName, final String shortName) {
    this.dim.addMasterImport(packageName, shortName);
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
    try {
      final String sortedImports = this.dim.computeSortedImports();
      final FileWriter fw = new FileWriter("/tmp/imports");
      fw.write(sortedImports);
      fw.close();
      final String after = GMATransform.replaceDevName(this.gm, sortedImports);
      final FileWriter fwa = new FileWriter("/tmp/importsAfter");
      fwa.write(after);
      fwa.close();
      return after;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public void addCompilationUnitImports(final String compilationUnitContents) {
    this.dim.addCompilationUnitImports(compilationUnitContents);
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
