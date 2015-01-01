package com.opcoach.genmodeladdon.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.pde.core.plugin.IExtensionsModelFactory;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel;
import org.eclipse.pde.internal.core.project.PDEProject;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class GenerateFactoryOverrideExtension {
  private WorkspaceBundlePluginModel fModel;
  
  public GenerateFactoryOverrideExtension(final String projectName) {
    try {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      final IWorkspaceRoot root = _workspace.getRoot();
      final IProject project = root.getProject(projectName);
      final IFile pluginXml = PDEProject.getPluginXml(project);
      final IFile manifest = PDEProject.getManifest(project);
      final IPluginModelBase registryModel = PluginRegistry.findModel(projectName);
      WorkspaceBundlePluginModel _workspaceBundlePluginModel = new WorkspaceBundlePluginModel(manifest, pluginXml);
      this.fModel = _workspaceBundlePluginModel;
      IPluginBase _pluginBase = registryModel.getPluginBase();
      IPluginExtension[] _extensions = _pluginBase.getExtensions();
      for (final IPluginExtension e : _extensions) {
        {
          final IPluginExtension clonedExtens = this.copyExtension(e);
          IPluginBase _pluginBase_1 = this.fModel.getPluginBase();
          _pluginBase_1.add(clonedExtens);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
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
  
  public void generateOverideExtension(final String modelUri, final String factoryClassname) {
    try {
      final String point = "org.eclipse.emf.ecore.factory_override";
      IPluginBase _pluginBase = this.fModel.getPluginBase();
      IPluginExtension[] _extensions = _pluginBase.getExtensions();
      for (final IPluginExtension e : _extensions) {
        String _point = e.getPoint();
        boolean _equals = _point.equals(point);
        if (_equals) {
          IPluginObject[] _children = e.getChildren();
          for (final IPluginObject elt : _children) {
            if ((elt instanceof IPluginElement)) {
              final IPluginElement ipe = ((IPluginElement) elt);
              IPluginAttribute _attribute = ipe.getAttribute("class");
              final String classAtt = _attribute.getValue();
              IPluginAttribute _attribute_1 = ipe.getAttribute("uri");
              final String uriAtt = _attribute_1.getValue();
              boolean _and = false;
              IPluginAttribute _attribute_2 = ipe.getAttribute("class");
              String _value = _attribute_2.getValue();
              boolean _equals_1 = factoryClassname.equals(_value);
              if (!_equals_1) {
                _and = false;
              } else {
                IPluginAttribute _attribute_3 = ipe.getAttribute("uri");
                String _value_1 = _attribute_3.getValue();
                boolean _equals_2 = modelUri.equals(_value_1);
                _and = _equals_2;
              }
              if (_and) {
                return;
              }
            }
          }
        }
      }
      IExtensionsModelFactory _factory = this.fModel.getFactory();
      final IPluginExtension ext = _factory.createExtension();
      ext.setPoint(point);
      IPluginBase _pluginBase_1 = this.fModel.getPluginBase();
      _pluginBase_1.add(ext);
      IExtensionsModelFactory _factory_1 = this.fModel.getFactory();
      final IPluginElement elt_1 = _factory_1.createElement(ext);
      elt_1.setName("factory");
      elt_1.setAttribute("class", factoryClassname);
      elt_1.setAttribute("uri", modelUri);
      ext.add(elt_1);
      this.fModel.save();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
