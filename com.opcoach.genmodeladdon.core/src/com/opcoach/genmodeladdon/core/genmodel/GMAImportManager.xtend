package com.opcoach.genmodeladdon.core.genmodel

import java.util.Collection
import java.util.List
import org.eclipse.emf.codegen.util.ImportManager

/** An Import manager wrapper to override the emitImports */
class GMAImportManager extends ImportManager {
	
	ImportManager dim = null   // Delegated Import Manager
	GMAGenModel gm = null  // Parent GenModel
	
	new(ImportManager im,  GMAGenModel pgm, String compilationUnitPackage) {
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
		val res = dim.getImportedName(GMATransform.replaceDevName(gm,qualifiedName), autoImport);
		return res;
	}

	override String getImportedName(String qualifiedName)
	{
		val res = dim.getImportedName(GMATransform.replaceDevName(gm,qualifiedName));
		return res
	}

	override void addImport(String packageName, String shortName)
	{
		dim.addImport(packageName, GMATransform.replaceDevName(gm,shortName));
	}

	override void addImport(String qualifiedName)
	{
		val rep = GMATransform.replaceDevName(gm,qualifiedName)
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
		 dim.hasImport(shortName);
	}

	override Collection<String> getImports()
	{
	     dim.imports
	}

	override String getLineDelimiter()
	{
		 dim.getLineDelimiter();
	}

	override void setLineDelimiter(String lineDelimiter)
	{
		dim.setLineDelimiter(lineDelimiter);
	}

	override String computeSortedImports()
	{
		// THIS METHOD IS NEVER CALLED BY DELEGATION BECAUSE IT IS CALLED INTERNALLY BY 
		// emitSortedImport
		GMATransform.replaceDevName(gm, dim.computeSortedImports)
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