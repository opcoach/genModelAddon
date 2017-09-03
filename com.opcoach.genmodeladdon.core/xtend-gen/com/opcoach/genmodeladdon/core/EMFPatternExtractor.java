package com.opcoach.genmodeladdon.core;

import com.google.common.base.Objects;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.osgi.framework.Bundle;

@SuppressWarnings("all")
public class EMFPatternExtractor implements Runnable {
  private final static String EMF_CODEGEN_PLUGIN_SN = "com.opcoach.genmodeladdon.core";
  
  private final static String EMF_CODEGEN_CLASSGEN_PATH = "/templates/model/Class.javajet";
  
  private final static String TARGET_SOURCE_PATH = "templates";
  
  private final static String TARGET_MODEL_PATH = "model";
  
  private final static String TARGET_CLASS_TEMPLATE_FILE = "Class.javajet";
  
  private final static String DEV_CLASS_PATTERN = "%DEV_CLASS_PATTERN%";
  
  private final static String DEV_INTERFACE_PATTERN = "%DEV_INTERFACE_PATTERN%";
  
  private final IProject targetProject;
  
  private final String devClassPattern;
  
  private final String devInterfacePattern;
  
  public EMFPatternExtractor(final IProject targetProject, final String cp, final String ip) {
    this.targetProject = targetProject;
    this.devClassPattern = cp;
    this.devInterfacePattern = ip;
  }
  
  private InputStream extractClassTemplateIncurrentPlugin() {
    try {
      InputStream _xblockexpression = null;
      {
        final Bundle codegenBundle = Platform.getBundle(EMFPatternExtractor.EMF_CODEGEN_PLUGIN_SN);
        final Path path = new Path(EMFPatternExtractor.EMF_CODEGEN_CLASSGEN_PATH);
        _xblockexpression = FileLocator.openStream(codegenBundle, path, false);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public void run() {
    try {
      final InputStream sourceJetFile = this.extractClassTemplateIncurrentPlugin();
      final IFolder templateFolder = this.createSourceDirectoryStructure();
      final IFile file = templateFolder.getFile(EMFPatternExtractor.TARGET_CLASS_TEMPLATE_FILE);
      String content = IOUtils.toString(sourceJetFile, ResourcesPlugin.getEncoding());
      content = content.replaceFirst(EMFPatternExtractor.DEV_CLASS_PATTERN, this.devClassPattern);
      content = content.replaceFirst(EMFPatternExtractor.DEV_INTERFACE_PATTERN, this.devInterfacePattern);
      File _file = file.getLocation().toFile();
      FileOutputStream _fileOutputStream = new FileOutputStream(_file);
      IOUtils.write(content, _fileOutputStream, ResourcesPlugin.getEncoding());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IFolder createSourceDirectoryStructure() {
    if ((this.targetProject instanceof IProject)) {
      IPath tgtSourcePath = ((IPath) null);
      final IProject javaTargetProject = ((IProject) this.targetProject);
      final IFolder sourcePath = javaTargetProject.getFolder(EMFPatternExtractor.TARGET_SOURCE_PATH);
      this.createFolderIfNotExists(sourcePath);
      tgtSourcePath = sourcePath.getFullPath();
      boolean _notEquals = (!Objects.equal(tgtSourcePath, null));
      if (_notEquals) {
        final Path p = new Path(((EMFPatternExtractor.TARGET_SOURCE_PATH + "/") + EMFPatternExtractor.TARGET_MODEL_PATH));
        final IFolder modelFolder = this.targetProject.getFolder(p);
        this.createFolderIfNotExists(modelFolder);
        return modelFolder;
      }
      return null;
    }
    return null;
  }
  
  public void createFolderIfNotExists(final IFolder f) {
    boolean _exists = f.exists();
    boolean _not = (!_exists);
    if (_not) {
      InputOutput.<String>println(("Create folder : " + f));
      try {
        NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
        f.create(true, true, _nullProgressMonitor);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          InputOutput.<String>println(("Unable to create the folder :  " + f));
          e.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
}
