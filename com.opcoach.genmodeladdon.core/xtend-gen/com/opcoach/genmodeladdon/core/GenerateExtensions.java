package com.opcoach.genmodeladdon.core;

import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginExtensionPoint;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel;
import org.eclipse.pde.internal.core.project.PDEProject;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

/**
 * This class updates extensions in the current project.
 * Actually some of extension points must be updated if they already exists
 * and the factory_override extension must be added.
 * 
 * Namely :
 *  - if there is already an emf.ecore.generated_package it must be updated with the new package class
 *  - there must be a new emf.ecore.factory_override
 * 
 * Some code is inspired from class PointSelectionPage in org.eclipse.pde.internal.ui.wizards.extension
 */
@SuppressWarnings("all")
class GenerateExtensions {
  private final static String EMF_GENERATED_PACKAGE = "org.eclipse.emf.ecore.generated_package";
  
  private final static String FACTORY_OVERRIDE = "org.eclipse.emf.ecore.factory_override";
  
  private final static String PACKAGE_ELT = "package";
  
  private final static String FACTORY_ELT = "factory";
  
  private final static String URI_ATTR = "uri";
  
  private final static String CLASS_ATTR = "class";
  
  private WorkspaceBundlePluginModel fModel;
  
  private IProject project;
  
  public GenerateExtensions(final IProject p) {
    try {
      this.project = p;
      final IFile pluginXml = PDEProject.getPluginXml(this.project);
      final IFile manifest = PDEProject.getManifest(this.project);
      WorkspaceBundlePluginModel _workspaceBundlePluginModel = new WorkspaceBundlePluginModel(manifest, pluginXml);
      this.fModel = _workspaceBundlePluginModel;
      IPluginModelBase sourceModel = null;
      IPluginModelBase[] _workspaceModels = PluginRegistry.getWorkspaceModels();
      for (final IPluginModelBase m : _workspaceModels) {
        if (((m.getBundleDescription() != null) && this.project.getName().equals(m.getBundleDescription().getSymbolicName()))) {
          sourceModel = m;
        }
      }
      IPluginExtensionPoint[] _extensionPoints = sourceModel.getExtensions().getExtensionPoints();
      for (final IPluginExtensionPoint ept : _extensionPoints) {
        this.fModel.getPluginBase().add(this.copyExtensionPoint(ept));
      }
      IPluginExtension[] _extensions = sourceModel.getExtensions().getExtensions();
      for (final IPluginExtension e : _extensions) {
        this.fModel.getPluginBase().add(this.copyExtension(e));
      }
      PDECore.getDefault().getModelManager().bundleRootChanged(this.project);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String printExtension(final IPluginExtension ext) {
    String _xblockexpression = null;
    {
      String _point = ext.getPoint();
      String _plus = ("<extension point=\"" + _point);
      String _plus_1 = (_plus + "\">");
      InputOutput.<String>println(_plus_1);
      IPluginObject[] _children = ext.getChildren();
      for (final IPluginObject elt : _children) {
        if ((elt instanceof IPluginElement)) {
          final IPluginElement ipe = ((IPluginElement) elt);
          String _name = ipe.getName();
          String _plus_2 = ("<" + _name);
          String _plus_3 = (_plus_2 + " ");
          InputOutput.<String>print(_plus_3);
          IPluginAttribute[] _attributes = ipe.getAttributes();
          for (final IPluginAttribute a : _attributes) {
            String _name_1 = a.getName();
            String _plus_4 = (_name_1 + "=\'");
            String _value = a.getValue();
            String _plus_5 = (_plus_4 + _value);
            String _plus_6 = (_plus_5 + "\' ");
            InputOutput.<String>print(_plus_6);
          }
          InputOutput.<String>println("/>");
        }
      }
      _xblockexpression = InputOutput.<String>println("</extension>");
    }
    return _xblockexpression;
  }
  
  private IPluginExtension copyExtension(final IPluginExtension ext) {
    try {
      final IPluginExtension clonedExt = this.fModel.getFactory().createExtension();
      clonedExt.setPoint(ext.getPoint());
      int _length = ext.getName().length();
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        clonedExt.setName(ext.getName());
      }
      clonedExt.setId(ext.getId());
      IPluginObject[] _children = ext.getChildren();
      for (final IPluginObject elt : _children) {
        if ((elt instanceof IPluginElement)) {
          final IPluginElement ipe = ((IPluginElement) elt);
          final IPluginElement clonedElt = this.copyExtensionElement(ipe, ext);
          clonedExt.add(clonedElt);
        }
      }
      return clonedExt;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private IPluginExtensionPoint copyExtensionPoint(final IPluginExtensionPoint extPt) {
    try {
      final IPluginExtensionPoint clonedExtPt = this.fModel.getFactory().createExtensionPoint();
      clonedExtPt.setId(extPt.getId());
      clonedExtPt.setName(extPt.getName());
      clonedExtPt.setSchema(extPt.getSchema());
      return clonedExtPt;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private IPluginElement copyExtensionElement(final IPluginElement elt, final IPluginObject parent) {
    try {
      final IPluginElement clonedElt = this.fModel.getFactory().createElement(parent);
      clonedElt.setName(elt.getName());
      IPluginAttribute[] _attributes = elt.getAttributes();
      for (final IPluginAttribute a : _attributes) {
        clonedElt.setAttribute(a.getName(), a.getValue());
      }
      IPluginObject[] _children = elt.getChildren();
      for (final IPluginObject e : _children) {
        if ((e instanceof IPluginElement)) {
          final IPluginElement ipe = ((IPluginElement) e);
          clonedElt.add(this.copyExtensionElement(ipe, clonedElt));
        }
      }
      return clonedElt;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Check if factory_override or generated_package are correct in the plugin.xml
   *  Must be checked in case the class names have changed between 2 generations
   * factories : map<model uri, classForFactory>
   * packages : map<model uri, classForPackage>
   */
  public void generateOrUpdateExtensions(final Map<String, String> factories, final Map<String, String> packages) {
    Set<Map.Entry<String, String>> _entrySet = factories.entrySet();
    for (final Map.Entry<String, String> entry : _entrySet) {
      this.generateOrUpdateExtension(GenerateExtensions.FACTORY_OVERRIDE, entry.getKey(), GenerateExtensions.FACTORY_ELT, entry.getValue());
    }
    Set<Map.Entry<String, String>> _entrySet_1 = packages.entrySet();
    for (final Map.Entry<String, String> entry_1 : _entrySet_1) {
      this.generateOrUpdateExtension(GenerateExtensions.EMF_GENERATED_PACKAGE, entry_1.getKey(), GenerateExtensions.PACKAGE_ELT, entry_1.getValue());
    }
    this.fModel.save();
    PDECore.getDefault().getModelManager().bundleRootChanged(this.project);
  }
  
  private void generateOrUpdateExtension(final String extName, final String modelURI, final String nodeName, final String classname) {
    try {
      IPluginExtension factoryExt = null;
      do {
        {
          factoryExt = this.findPluginElement(extName, modelURI, nodeName);
          if ((factoryExt != null)) {
            this.fModel.getPluginBase().remove(factoryExt);
          }
        }
      } while((factoryExt != null));
      final IPluginExtension updatedExtension = this.fModel.getFactory().createExtension();
      updatedExtension.setPoint(extName);
      final IPluginElement factoryElement = this.fModel.getFactory().createElement(updatedExtension);
      factoryElement.setName(nodeName);
      factoryElement.setAttribute(GenerateExtensions.URI_ATTR, modelURI);
      factoryElement.setAttribute(GenerateExtensions.CLASS_ATTR, classname);
      updatedExtension.add(factoryElement);
      this.fModel.getPluginBase().add(updatedExtension);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Search for a plugin element 'factory' for factoryoverride extension
   * or 'package' for the emf_generatedPackage extension
   */
  private IPluginExtension findPluginElement(final String extPoint, final String modelURI, final String nodeName) {
    IPluginExtension[] _extensions = this.fModel.getPluginBase().getExtensions();
    for (final IPluginExtension e : _extensions) {
      boolean _equals = e.getPoint().equals(extPoint);
      if (_equals) {
        IPluginObject[] _children = e.getChildren();
        for (final IPluginObject elt : _children) {
          if ((elt instanceof IPluginElement)) {
            final IPluginElement ipe = ((IPluginElement) elt);
            if ((ipe.getName().equals(nodeName) && modelURI.equals(ipe.getAttribute(GenerateExtensions.URI_ATTR).getValue()))) {
              return e;
            }
          }
        }
      }
    }
    return null;
  }
}
