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
		val res = dim.getImportedName(qualifiedName, autoImport);
		return res;
	}

	override String getImportedName(String qualifiedName)
	{
		val res = dim.getImportedName(qualifiedName);
		return res
	}

	override void addImport(String packageName, String shortName)
	{
		if ("*".equals(shortName))
		   println("Found a short name '*' for packageName :" + packageName)
		dim.addImport(GMATransform.replaceDevName(gm,packageName), GMATransform.replaceDevName(gm,shortName));
	}

	override void addImport(String qualifiedName)
	{
		val rep = GMATransform.replaceDevName(gm,qualifiedName)
		println("Replace this import :  " + qualifiedName + "\n by this one : " + rep)
		dim.addImport(rep)
	}

	override void addPseudoImport(String qualifiedName)
	{
		dim.addPseudoImport(GMATransform.replaceDevName(gm,qualifiedName));
	}

	override void addMasterImport(String packageName, String shortName)
	{
		dim.addMasterImport(GMATransform.replaceDevName(gm,packageName), GMATransform.replaceDevName(gm,shortName));
	}

	override boolean hasImport(String shortName)
	{
		return dim.hasImport(shortName);
	}

	override Collection<String> getImports()
	{
		return dim.imports
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
		return GMATransform.replaceDevName(gm, dim.computeSortedImports)
		
	/* 	val sortedImports = dim.computeSortedImports()
		val fw = new FileWriter("/tmp/imports.txt")
		fw.write(sortedImports)
		fw.close
		
		val after = GMATransform.replaceDevName(gm, sortedImports)
		
	    val fwa = new FileWriter("/tmp/importsAfter.txt")
		fwa.write(after)
		fwa.close
		
		return after */
	}

	override void addCompilationUnitImports(String compilationUnitContents)
	{
		dim.addCompilationUnitImports(GMATransform.replaceDevName(gm,compilationUnitContents));
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