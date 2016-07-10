package com.opcoach.genmodeladdon.core;

import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.InputOutput;

/**
 * A class to provide some generation common methods
 */
@SuppressWarnings("all")
public class GenerateCommon {
  /**
   * Extract the project name from the genmodel resource
   */
  public static String getProjectName(final GenModel gm) {
    Resource _eResource = gm.eResource();
    URI _uRI = _eResource.getURI();
    return GenerateCommon.getProjectNameFromURI(_uRI);
  }
  
  public static String getProjectNameFromURI(final URI genModelUri) {
    IWorkspace _workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    java.net.URI _locationURI = _root.getLocationURI();
    String _string = _locationURI.toString();
    final URI rootUri = URI.createURI(_string);
    final String lastSegOfRootUri = rootUri.lastSegment();
    final String genModelUriStr = genModelUri.toString();
    boolean _startsWith = genModelUriStr.startsWith("platform:/resource/");
    if (_startsWith) {
      final String s = genModelUriStr.replace("platform:/resource/", "");
      final int lastSlash = s.indexOf("/");
      return s.substring(0, lastSlash);
    }
    final List<String> segments = genModelUri.segmentsList();
    final int lastIndex = segments.lastIndexOf(lastSegOfRootUri);
    return segments.get((lastIndex + 1));
  }
  
  /**
   * Find the project from a genmodel
   */
  public static IProject getProject(final GenModel gm) {
    IProject _xblockexpression = null;
    {
      final String projectName = GenerateCommon.getProjectName(gm);
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      _xblockexpression = _root.getProject(projectName);
    }
    return _xblockexpression;
  }
  
  /**
   * Find the model name from the genmodel
   */
  public static String getModelName(final GenModel gm) {
    Resource _eResource = gm.eResource();
    final URI uri = _eResource.getURI();
    final String s = uri.toString();
    int pos = s.lastIndexOf("/");
    String modelName = s.substring((pos + 1));
    int _indexOf = modelName.indexOf(".genmodel");
    pos = _indexOf;
    String _substring = modelName.substring(0, pos);
    modelName = _substring;
    return modelName;
  }
  
  /**
   * Find the model directory in its project
   */
  public static String getModelPath(final GenModel gm) {
    Resource _eResource = gm.eResource();
    final URI uri = _eResource.getURI();
    IProject _project = GenerateCommon.getProject(gm);
    final String projectName = _project.getName();
    String _string = uri.toString();
    return GenerateCommon.getModelPathFromStringURI(projectName, _string);
  }
  
  /**
   * Find the model directory in its project
   */
  public static String getModelPathFromStringURI(final String projectName, final String uri) {
    InputOutput.<String>println(("Model uri is : " + uri));
    int _lastIndexOf = uri.lastIndexOf(projectName);
    int _length = projectName.length();
    int _plus = (_lastIndexOf + _length);
    final int pathPos = (_plus + 1);
    final int lastSlashPos = uri.lastIndexOf("/");
    String modelDir = ".";
    if ((pathPos < lastSlashPos)) {
      String _substring = uri.substring(pathPos, lastSlashPos);
      modelDir = _substring;
    }
    return modelDir;
  }
}
