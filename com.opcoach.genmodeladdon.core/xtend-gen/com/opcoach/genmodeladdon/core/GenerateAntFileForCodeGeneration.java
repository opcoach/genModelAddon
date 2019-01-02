package com.opcoach.genmodeladdon.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GenerateAntFileForCodeGeneration {
  public static final String ANT_FILENAME = "generateEMFCode.xml";
  
  private CharSequence generateAntFileContent(final String modelDir, final String modelName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<!--  Dont forget to launch this ant file in the same JRE than your Eclipse -->");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<project name=\"project\" default=\"generateCode\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<taskdef name=\"emf.Ecore2Java\" classname=\"org.eclipse.emf.importer.ecore.taskdefs.EcoreGeneratorTask\"/>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<description> Build the javacode from ecore model   </description>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<target name=\"generateCode\" description=\"description\">");
    _builder.newLine();
    _builder.append("\t    ");
    _builder.append("<echo message=\"   --> Generate emf code for ");
    _builder.append(modelDir, "\t    ");
    _builder.append("/");
    _builder.append(modelName, "\t    ");
    _builder.append(".ecore\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("<emf.Ecore2Java genModel=\"");
    _builder.append(modelDir, "\t\t");
    _builder.append("/");
    _builder.append(modelName, "\t\t");
    _builder.append(".genmodel\" ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("model=\"");
    _builder.append(modelDir, "\t\t\t");
    _builder.append("/");
    _builder.append(modelName, "\t\t\t");
    _builder.append(".ecore\" ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("generatemodelproject=\"true\" ");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("generateeditorproject=\"false\" ");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("generateeditproject=\"false\" ");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("reconcilegenmodel=\"reload\" />");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</target>");
    _builder.newLine();
    _builder.append("</project>");
    _builder.newLine();
    return _builder;
  }
  
  public File getAntFile(final IProject proj) {
    return this.getAntFile(proj, GenerateAntFileForCodeGeneration.ANT_FILENAME);
  }
  
  public File getAntFile(final IProject proj, final String antFileName) {
    final IPath location = proj.getLocation();
    String _oSString = location.toOSString();
    String _plus = (_oSString + File.separator);
    final String srcAbsolutePath = (_plus + antFileName);
    final File f = new File(srcAbsolutePath);
    return f;
  }
  
  public File generateAntFile(final String modelDir, final String modelName, final IProject proj) throws IOException, CoreException {
    return this.generateAntFile(modelDir, modelName, proj, GenerateAntFileForCodeGeneration.ANT_FILENAME);
  }
  
  public File generateAntFile(final String modelDir, final String modelName, final IProject proj, final String antFileName) throws IOException, CoreException {
    final File f = this.getAntFile(proj, antFileName);
    boolean _exists = f.exists();
    boolean _not = (!_exists);
    if (_not) {
      f.createNewFile();
    }
    final FileWriter fw = new FileWriter(f);
    fw.write(this.generateAntFileContent(modelDir, modelName).toString());
    fw.flush();
    fw.close();
    proj.refreshLocal(IResource.DEPTH_ONE, null);
    return f;
  }
}
