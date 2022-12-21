package com.opcoach.genmodeladdon.core.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import com.opcoach.genmodeladdon.core.GMAConstants;
import com.opcoach.genmodeladdon.core.GenerateCommon;
import com.opcoach.genmodeladdon.core.GenerateDevStructure;
import com.opcoach.genmodeladdon.core.genmodel.GMAGenModel;
import com.opcoach.genmodeladdon.nature.GMANature;

/**
 * This class is used once to initialize the projects : - unzip the sample
 * project - generate the code only once - remember that everything is generated
 * - this is a singleton
 * 
 * @author olivier
 *
 */
public class WorkspaceConfigurator implements ProjectConstants, GMAConstants {

	private Map<String, GMAGenModel> gmMap = new HashMap<>();
	private Map<String, GenerateDevStructure> genMap = new HashMap<>();

	private boolean initDone = false;
	private static WorkspaceConfigurator instance = null;

	private IWorkspaceRoot root;

	private IProject sampleProject;

	/** Hidden constructor for singleton */
	private WorkspaceConfigurator() {
		// Start manually the jdt.ui and catch the bad exception..
		// Actually we don't need this plugin (or may ben ant.launching needs it
		// ?) but it appears with dependencies
		Bundle jdtUI = Platform.getBundle("org.eclipse.jdt.ui");
		try {
			if (jdtUI != null)
				jdtUI.start();
		} catch (BundleException e) {
		}

	}

	/** Accessor for external use */
	public static WorkspaceConfigurator getDefault() {
		if (instance == null)
			instance = new WorkspaceConfigurator();

		return instance;

	}

	public IWorkspaceRoot getWorkspaceRoot() {
		return root;
	}

	public boolean isInitDone() {
		return initDone;
	}

	/** initialize the sample project only once */
	public IProject getSampleProject() {
		if (!initDone) {
			// System.out.println("****** Creating the project in test workspace ");
			try {
				// Copy the sample project in the runtime workspace
				root = initWorkspace();
				initGenModel(PROJECT_GENMODEL, PROJECT_ANT_FILE);
				initGenModel(PROJECT2_GENMODEL, PROJECT_ANT2_FILE, "{0}Impl", "G{0}", "src");
				initGenModel(FANNOISE_GENMODEL, FANOISE_ANT_FILE);
				initGenModel(RAILTOPO_GENMODEL, RAILTOPO_ANT_FILE);
				initGenModel(XTEND_GENMODEL, XTEND_ANT_FILE, "{0}Impl", "{0}", "src", true);
				// System.out.println("Init finished");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			initDone = true;
		}

		return sampleProject;
	}

	public void initGenModel(String genModelName, String antFilename) throws IOException, InterruptedException {
		initGenModel(genModelName, antFilename, false);
	}

	public void initGenModel(String genModelName, String antFilename, boolean xtendGeneration)
			throws IOException, InterruptedException {
		initGenModel(genModelName, antFilename, ADVISED_DEV_CLASS_IMPL_PATTERN, ADVISED_DEV_INTERFACE_PATTERN,
				DEFAULT_SRC_DEV, xtendGeneration);
	}

	public void initGenModel(String genModelName, String antFilename, String cPattern, String iPattern, String srcDir)
			throws IOException, InterruptedException {
		initGenModel(genModelName, antFilename, cPattern, iPattern, srcDir, false);
	}

	public void initGenModel(String genModelName, String antFilename, String cPattern, String iPattern, String srcDir,
			boolean xtendGeneration) throws IOException, InterruptedException {
		// Read the genModel
		GMAGenModel gm = readSampleGenModel(root, genModelName);
		gm.setDevClassPattern(cPattern);
		gm.setDevInterfacePattern(iPattern);
		gm.setSrcDir(srcDir);
		gm.setGenerateEMFCode(true);
		gm.setGenerateOverridenImplAsXtend(xtendGeneration);

		gmMap.put(genModelName, gm);

		// Create the generator.
		GenerateDevStructure gen = new GenerateDevStructure(gm);
		genMap.put(genModelName, gen);

		// Remember of sample project
		sampleProject = root.getProject(DEST_SAMPLE_PROJECT);

		// Assign it the GMA nature is not set aleady
		try {
			if (!sampleProject.hasNature(GMAConstants.NATURE_ID))
				GMANature.toggleNature(sampleProject);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NullProgressMonitor npm = new NullProgressMonitor();
		try {
			sampleProject.open(npm);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Do all in the right order !
		gen.generateAll(antFilename);

	}

	public GMAGenModel getGenModel(String name) {
		return gmMap.get(name);
	}

	public GenerateDevStructure getGenDevStructure(String name) {
		return genMap.get(name);
	}

	/**
	 * Read the sample gen model located in sample project
	 */
	private static GMAGenModel readSampleGenModel(IWorkspaceRoot root, String pathToGenModel) {
		// Read the sample gen model in temporary workspace
		String path = root.getLocation().toString() + pathToGenModel;

		ResourceSet rset = new ResourceSetImpl();
		// rset.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap(true));

		// Get the resource
		Resource resource = rset.getResource(URI.createURI("file:/" + path), true);
		return (GMAGenModel) resource.getContents().get(0);

	}

	/**
	 * This method initializes the test workspace with a sample project It calls the
	 * prepareTestWorkspace ant file that will unzip the sample project
	 */
	private IWorkspaceRoot initWorkspace() throws IOException {
		// Get the zipped file to extract
		Bundle b = Platform.getBundle(SRC_SAMPLE_PROJECT);

		URL url = b.getEntry("sampleProject.zip");
		String fileURL = FileLocator.toFileURL(url).toString();

		// Create a sample empty project in workspace root
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject proj = root.getProject(DEST_SAMPLE_PROJECT);

		NullProgressMonitor npm = new NullProgressMonitor();
		try {
			if (proj.exists()) {
				// remove previous project version.
				System.out.println("Deleting existing project : " + DEST_SAMPLE_PROJECT);
				proj.delete(true, npm);
			}

			proj.create(npm);

		} catch (CoreException e1) {
			e1.printStackTrace();
		}

		// Then get the ant file to run to copy the template project
		AntRunner runner = new AntRunner();
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("wsRoot", root.getLocation().toOSString() + File.separator + DEST_SAMPLE_PROJECT);
		properties.put("zipFile", fileURL.replace("file:", ""));
		properties.put("destProjectName", DEST_SAMPLE_PROJECT);
		properties.put("srcProjectName", SRC_SAMPLE_PROJECT);
		runner.addUserProperties(properties);
		runner.setBuildFileLocation("prepareTestWorkspace.xml");
		runner.addBuildLogger("org.apache.tools.ant.DefaultLogger");
		try {
			runner.run();
			root.refreshLocal(IResource.DEPTH_INFINITE, null);

			// Now close the project and reopen it
			proj.close(npm);
			proj = root.getProject(DEST_SAMPLE_PROJECT);
			proj.open(npm);

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root;
	}

}
