package com.opcoach.genmodeladdon.core;

import com.opcoach.genmodeladdon.core.GenerateCommon;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GenerateAntFileForCodeGeneration {
  public final static String ANT_FILENAME = "generateEMFCode.xml";
  
  public CharSequence generateAntFileContent(final String modelName) {
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
    _builder.append("<description> Build the javacode from ecore model   </description>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<target name=\"generateCode\" description=\"description\">");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<emf.Ecore2Java genModel=\"model/");
    _builder.append(modelName, "\t\t");
    _builder.append(".genmodel\" ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("model=\"model/");
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
  
  public File getAntFile(final GenModel gm) {
    final IProject proj = GenerateCommon.getProject(gm);
    final IPath location = proj.getLocation();
    String _oSString = location.toOSString();
    String _plus = (_oSString + File.separator);
    final String srcAbsolutePath = (_plus + GenerateAntFileForCodeGeneration.ANT_FILENAME);
    final File f = new File(srcAbsolutePath);
    return f;
  }
  
  public File generateAntFile(final GenModel gm) throws IOException, CoreException {
    Resource _eResource = gm.eResource();
    final String s = _eResource.toString();
    int pos = s.lastIndexOf(File.separator);
    String modelName = s.substring((pos + 1));
    int _indexOf = modelName.indexOf(".genmodel");
    pos = _indexOf;
    String _substring = modelName.substring(0, pos);
    modelName = _substring;
    final File f = this.getAntFile(gm);
    boolean _exists = f.exists();
    boolean _not = (!_exists);
    if (_not) {
      f.createNewFile();
    }
    final FileWriter fw = new FileWriter(f);
    CharSequence _generateAntFileContent = this.generateAntFileContent(modelName);
    String _string = _generateAntFileContent.toString();
    fw.write(_string);
    fw.flush();
    fw.close();
    final IProject proj = GenerateCommon.getProject(gm);
    proj.refreshLocal(IResource.DEPTH_ONE, null);
    return f;
  }
}
