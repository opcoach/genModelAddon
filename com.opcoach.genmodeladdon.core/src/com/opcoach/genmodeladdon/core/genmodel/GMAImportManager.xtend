package com.opcoach.genmodeladdon.core.genmodel

import java.io.FileWriter
import java.util.Collection
import java.util.List
import org.eclipse.emf.codegen.util.ImportManager

/** An Import manager wrapper to override the emitImports */
class GMAImportManager extends ImportManager {
	
	ImportManager dim = null   // Delegated Import Manager
	GMAGenModelImpl gm = null  // Parent GenModel
	
	new(ImportManager im,  GMAGenModelImpl pgm, String compilationUnitPackage) {
		super(compilationUnitPackage)
		dim=im
		gm = pgm
	}
	
	override int hashCode()
	{
		return dim.hashCode();
	}

	override boolean equals(Object obj)
	{
		return dim.equals(obj);
	}

	override String toString()
	{
		return dim.toString();
	}

	override String getImportedName(String qualifiedName, boolean autoImport)
	{
		return dim.getImportedName(qualifiedName, autoImport);
	}

	override String getImportedName(String qualifiedName)
	{
		return dim.getImportedName(qualifiedName);
	}

	override void addImport(String packageName, String shortName)
	{
		dim.addImport(packageName, shortName);
	}

	override void addImport(String qualifiedName)
	{
		dim.addImport(qualifiedName);
	}

	override void addPseudoImport(String qualifiedName)
	{
		dim.addPseudoImport(qualifiedName);
	}

	override void addMasterImport(String packageName, String shortName)
	{
		dim.addMasterImport(packageName, shortName);
	}

	override boolean hasImport(String shortName)
	{
		return dim.hasImport(shortName);
	}

	override Collection<String> getImports()
	{
		return dim.getImports();
	}

	override String getLineDelimiter()
	{
		return dim.getLineDelimiter();
	}

	override void setLineDelimiter(String lineDelimiter)
	{
		dim.setLineDelimiter(lineDelimiter);
	}

	override String computeSortedImports()
	{
		val sortedImports = dim.computeSortedImports()
		val fw = new FileWriter("/tmp/imports")
		fw.write(sortedImports)
		fw.close
		
		val after = GMATransform.replaceDevName(gm, sortedImports)
		
	    val fwa = new FileWriter("/tmp/importsAfter")
		fwa.write(after)
		fwa.close
		
		return after
	}

	override void addCompilationUnitImports(String compilationUnitContents)
	{
		dim.addCompilationUnitImports(compilationUnitContents);
	}

	override void markImportLocation(StringBuilder stringBuilder)
	{
		dim.markImportLocation(stringBuilder);
	}

	override void markImportLocation(StringBuffer stringBuffer)
	{
		dim.markImportLocation(stringBuffer);
	}

	override void emitSortedImports()
	{
		dim.emitSortedImports();
	}

	override void addJavaLangImports(List<String> javaLangClassNames)
	{
		dim.addJavaLangImports(javaLangClassNames);
	}

	
}