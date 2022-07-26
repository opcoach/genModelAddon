package com.opcoach.genmodeladdon.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.codegen.ecore.genmodel.GenBase;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class GMAHelper {
  public static boolean GMACompliant(final GenBase context) {
    final GenModel genModel = context.getGenModel();
    final String path = genModel.getModelProjectDirectory();
    final IWorkspace workspace = ResourcesPlugin.getWorkspace();
    final IProject modelProject = workspace.getRoot().getProject(path);
    try {
      return modelProject.hasNature(GMAConstants.NATURE_ID);
    } catch (final Throwable _t) {
      if (_t instanceof CoreException) {
        return false;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
}
