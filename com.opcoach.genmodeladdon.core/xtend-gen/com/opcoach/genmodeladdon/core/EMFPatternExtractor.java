package com.opcoach.genmodeladdon.core;

import com.google.common.base.Objects;
import java.io.File;
import java.io.FileInputStream;
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
  
  public InputStream extractClassTemplateIncurrentPlugin() {
    try {
      InputStream _xblockexpression = null;
      {
        final Bundle codegenBundle = Platform.getBundle(EMFPatternExtractor.EMF_CODEGEN_PLUGIN_SN);
        final Path path = new Path(EMFPatternExtractor.EMF_CODEGEN_CLASSGEN_PATH);
        final InputStream jETClassFile = FileLocator.openStream(codegenBundle, path, false);
        _xblockexpression = jETClassFile;
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
      boolean _exists = file.exists();
      boolean _not = (!_exists);
      if (_not) {
        NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
        file.create(sourceJetFile, true, _nullProgressMonitor);
      }
      IPath _location = file.getLocation();
      File _file = _location.toFile();
      FileInputStream _fileInputStream = new FileInputStream(_file);
      String _encoding = ResourcesPlugin.getEncoding();
      String content = IOUtils.toString(_fileInputStream, _encoding);
      String _replaceFirst = content.replaceFirst(EMFPatternExtractor.DEV_CLASS_PATTERN, this.devClassPattern);
      content = _replaceFirst;
      String _replaceFirst_1 = content.replaceFirst(EMFPatternExtractor.DEV_INTERFACE_PATTERN, this.devInterfacePattern);
      content = _replaceFirst_1;
      IPath _location_1 = file.getLocation();
      File _file_1 = _location_1.toFile();
      FileOutputStream _fileOutputStream = new FileOutputStream(_file_1);
      String _encoding_1 = ResourcesPlugin.getEncoding();
      IOUtils.write(content, _fileOutputStream, _encoding_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IFolder createSourceDirectoryStructure() {
    try {
      if ((this.targetProject instanceof IProject)) {
        IPath tgtSourcePath = ((IPath) null);
        final IProject javaTargetProject = ((IProject) this.targetProject);
        final IFolder sourcePath = javaTargetProject.getFolder(EMFPatternExtractor.TARGET_SOURCE_PATH);
        InputOutput.<String>println(("The sourcePath is : " + sourcePath));
        boolean _exists = sourcePath.exists();
        boolean _not = (!_exists);
        if (_not) {
          try {
            NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
            sourcePath.create(true, true, _nullProgressMonitor);
          } catch (final Throwable _t) {
            if (_t instanceof Exception) {
              final Exception e = (Exception)_t;
              InputOutput.<String>println(("Unable to create the file :  " + sourcePath));
              e.printStackTrace();
            } else {
              throw Exceptions.sneakyThrow(_t);
            }
          }
        }
        IPath _fullPath = sourcePath.getFullPath();
        tgtSourcePath = _fullPath;
        boolean _notEquals = (!Objects.equal(tgtSourcePath, null));
        if (_notEquals) {
          final Path p = new Path(((EMFPatternExtractor.TARGET_SOURCE_PATH + "/") + EMFPatternExtractor.TARGET_MODEL_PATH));
          final IFolder modelFolder = this.targetProject.getFolder(p);
          boolean _exists_1 = modelFolder.exists();
          boolean _not_1 = (!_exists_1);
          if (_not_1) {
            NullProgressMonitor _nullProgressMonitor_1 = new NullProgressMonitor();
            modelFolder.create(true, true, _nullProgressMonitor_1);
          }
          return modelFolder;
        }
        return null;
      }
      return null;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
