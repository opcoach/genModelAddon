package com.opcoach.genmodeladdon.core;

import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.InputOutput;

/**
 * A class to provide some generation common methods
 */
@SuppressWarnings("all")
public class GenerateCommon implements GMAConstants {
  /**
   * Extract the project name from the genmodel resource
   */
  public static String getProjectName(final GenModel gm) {
    return GenerateCommon.getProjectNameFromURI(gm.eResource().getURI());
  }
  
  public static String getProjectNameFromURI(final URI genModelUri) {
    final URI rootUri = URI.createURI(ResourcesPlugin.getWorkspace().getRoot().getLocationURI().toString());
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
      _xblockexpression = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
    }
    return _xblockexpression;
  }
  
  /**
   * Find the IFile from a genmodel
   */
  public static IFile getModelFile(final GenModel gm) {
    Resource _eResource = gm.eResource();
    boolean _tripleNotEquals = (_eResource != null);
    if (_tripleNotEquals) {
      final IProject pro = GenerateCommon.getProject(gm);
      final URI genModelUri = gm.eResource().getURI();
      final String uriString = genModelUri.toString();
      final int pos = uriString.indexOf(pro.getName());
      final String localPath = uriString.substring(pos);
      final Path p = new Path(localPath);
      final IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();
      return ws.getFile(p);
    } else {
      InputOutput.<String>println("eresource null dans gemmodel ?? ");
    }
    return null;
  }
  
  /**
   * Find the model name from the genmodel
   */
  public static String getModelName(final GenModel gm) {
    String _xblockexpression = null;
    {
      final URI uri = gm.eResource().getURI();
      final String s = uri.toString();
      int pos = s.lastIndexOf("/");
      String modelName = s.substring((pos + 1));
      pos = modelName.indexOf(".genmodel");
      _xblockexpression = modelName.substring(0, pos);
    }
    return _xblockexpression;
  }
  
  /**
   * Find the model directory in its project
   */
  public static String getModelPath(final GenModel gm) {
    final URI uri = gm.eResource().getURI();
    final String projectName = GenerateCommon.getProject(gm).getName();
    return GenerateCommon.getModelPathFromStringURI(projectName, uri.toString());
  }
  
  /**
   * Find the model directory in its project
   */
  public static String getModelPathFromStringURI(final String projectName, final String uri) {
    int _lastIndexOf = uri.lastIndexOf(projectName);
    int _length = projectName.length();
    int _plus = (_lastIndexOf + _length);
    final int pathPos = (_plus + 1);
    final int lastSlashPos = uri.lastIndexOf("/");
    String modelDir = ".";
    if ((pathPos < lastSlashPos)) {
      modelDir = uri.substring(pathPos, lastSlashPos);
    }
    return modelDir;
  }
  
  public static boolean isMapType(final EClassifier c) {
    final String name = c.getInstanceClassName();
    return ("java.util.Map.Entry".equals(name) || "java.util.Map$Entry".equals(name));
  }
  
  public static boolean isMapType(final GenClass c) {
    return GenerateCommon.isMapType(c.getEcoreClass());
  }
}
