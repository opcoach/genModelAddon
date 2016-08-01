package com.opcoach.genmodeladdon.core;

import com.google.common.base.Objects;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.pde.core.plugin.IExtensionsModelFactory;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.PDEExtensionRegistry;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel;
import org.eclipse.pde.internal.core.project.PDEProject;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class GenerateExtensions {
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
      IPluginModelBase projetBase = null;
      IPluginModelBase[] _workspaceModels = PluginRegistry.getWorkspaceModels();
      for (final IPluginModelBase m : _workspaceModels) {
        if (((!Objects.equal(m.getBundleDescription(), null)) && this.project.getName().equals(m.getBundleDescription().getSymbolicName()))) {
          projetBase = m;
        }
      }
      PDECore _default = PDECore.getDefault();
      PDEExtensionRegistry _extensionsRegistry = _default.getExtensionsRegistry();
      IPluginExtension[] _findExtensionsForPlugin = _extensionsRegistry.findExtensionsForPlugin(projetBase);
      for (final IPluginExtension e : _findExtensionsForPlugin) {
        IPluginBase _pluginBase = this.fModel.getPluginBase();
        IPluginExtension _copyExtension = this.copyExtension(e);
        _pluginBase.add(_copyExtension);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private String printExtension(final IPluginExtension ext) {
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
      IExtensionsModelFactory _factory = this.fModel.getFactory();
      final IPluginExtension clonedExt = _factory.createExtension();
      String _point = ext.getPoint();
      clonedExt.setPoint(_point);
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
  
  private IPluginElement copyExtensionElement(final IPluginElement elt, final IPluginObject parent) {
    try {
      IExtensionsModelFactory _factory = this.fModel.getFactory();
      final IPluginElement clonedElt = _factory.createElement(parent);
      String _name = elt.getName();
      clonedElt.setName(_name);
      IPluginAttribute[] _attributes = elt.getAttributes();
      for (final IPluginAttribute a : _attributes) {
        String _name_1 = a.getName();
        String _value = a.getValue();
        clonedElt.setAttribute(_name_1, _value);
      }
      IPluginObject[] _children = elt.getChildren();
      for (final IPluginObject e : _children) {
        if ((e instanceof IPluginElement)) {
          final IPluginElement ipe = ((IPluginElement) e);
          IPluginElement _copyExtensionElement = this.copyExtensionElement(ipe, clonedElt);
          clonedElt.add(_copyExtensionElement);
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
      String _key = entry.getKey();
      String _value = entry.getValue();
      this.generateOrUpdateExtension(GenerateExtensions.FACTORY_OVERRIDE, _key, GenerateExtensions.FACTORY_ELT, _value);
    }
    Set<Map.Entry<String, String>> _entrySet_1 = packages.entrySet();
    for (final Map.Entry<String, String> entry_1 : _entrySet_1) {
      String _key_1 = entry_1.getKey();
      String _value_1 = entry_1.getValue();
      this.generateOrUpdateExtension(GenerateExtensions.EMF_GENERATED_PACKAGE, _key_1, GenerateExtensions.PACKAGE_ELT, _value_1);
    }
    this.fModel.save();
  }
  
  public void generateOrUpdateExtension(final String extName, final String modelURI, final String nodeName, final String classname) {
    try {
      IPluginExtension factoryExt = null;
      do {
        {
          IPluginExtension _findPluginElement = this.findPluginElement(extName, modelURI, nodeName);
          factoryExt = _findPluginElement;
          boolean _notEquals = (!Objects.equal(factoryExt, null));
          if (_notEquals) {
            IPluginBase _pluginBase = this.fModel.getPluginBase();
            _pluginBase.remove(factoryExt);
          }
        }
      } while((!Objects.equal(factoryExt, null)));
      IExtensionsModelFactory _factory = this.fModel.getFactory();
      final IPluginExtension updatedExtension = _factory.createExtension();
      updatedExtension.setPoint(extName);
      IExtensionsModelFactory _factory_1 = this.fModel.getFactory();
      final IPluginElement factoryElement = _factory_1.createElement(updatedExtension);
      factoryElement.setName(nodeName);
      factoryElement.setAttribute(GenerateExtensions.URI_ATTR, modelURI);
      factoryElement.setAttribute(GenerateExtensions.CLASS_ATTR, classname);
      updatedExtension.add(factoryElement);
      IPluginBase _pluginBase = this.fModel.getPluginBase();
      _pluginBase.add(updatedExtension);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Search for a plugin element 'factory' for factoryoverride extension
   * or 'package' for the emf_generatedPackage extension
   */
  private IPluginExtension findPluginElement(final String extPoint, final String modelURI, final String nodeName) {
    IPluginBase _pluginBase = this.fModel.getPluginBase();
    IPluginExtension[] _extensions = _pluginBase.getExtensions();
    for (final IPluginExtension e : _extensions) {
      String _point = e.getPoint();
      boolean _equals = _point.equals(extPoint);
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
