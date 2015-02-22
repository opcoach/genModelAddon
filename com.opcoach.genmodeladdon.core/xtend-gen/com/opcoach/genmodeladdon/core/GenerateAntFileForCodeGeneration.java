package com.opcoach.genmodeladdon.core;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GenerateAntFileForCodeGeneration {
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
}
