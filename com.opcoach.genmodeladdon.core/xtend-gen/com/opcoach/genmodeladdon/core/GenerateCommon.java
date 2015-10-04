package com.opcoach.genmodeladdon.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * A class to provide some generation common methods
 */
@SuppressWarnings("all")
public class GenerateCommon {
  /**
   * Extract the project name from the resource of genmodel
   */
  public static String getProjectName(final GenModel gm) {
    Resource _eResource = gm.eResource();
    final URI genModelUri = _eResource.getURI();
    String _string = genModelUri.toString();
    final String p = _string.replaceFirst("platform:/resource/", "");
    final int pos = p.indexOf("/");
    return p.substring(0, pos);
  }
  
  /**
   * Find the project from a genmodel
   */
  public static IProject getProject(final GenModel gm) {
    IWorkspace _workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    String _projectName = GenerateCommon.getProjectName(gm);
    return _root.getProject(_projectName);
  }
}
