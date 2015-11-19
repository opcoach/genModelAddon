package com.opcoach.genmodeladdon.ui.dialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.FrameworkUtil;

public class DerivedSourceParametersDialog extends Dialog
{
	private static final String DEFAULT_SRC_DEV = "src";
	// Define the properties constants to retrieve values in dialog.
	private static final String PLUGIN_ID = "com.opcoach.genmodeladdon.ui";
	private static final QualifiedName PROP_INTERFACE_PATTERN = new QualifiedName(PLUGIN_ID, "interfacePattern");
	private static final QualifiedName PROP_CLASS_PATTERN = new QualifiedName(PLUGIN_ID, "classPattern");
	private static final QualifiedName PROP_SRCDIR = new QualifiedName(PLUGIN_ID, "srcDir");

	// Constants for the default name patterns
	private static final String DEFAULT_INTERFACE_PATTERN = "{0}";
	private static final String DEFAULT_CLASS_IMPL_PATTERN = "{0}Impl";

	private Text genInterfacePattern;
	private Text genClassPattern;
	private Text genSourceDir;
	private Text devSourceDir;
	private Text devInterfacePattern;
	private Text devClassPattern;
	private GenModel genModel;
	private String interfacePattern;
	private String classPattern;
	private String srcDir;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public DerivedSourceParametersDialog(Shell parentShell)
	{
		super(parentShell);
		setShellStyle(SWT.RESIZE);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);

		Group grpParametersSetIn = new Group(container, SWT.NONE);
		grpParametersSetIn.setLayout(new GridLayout(2, false));
		grpParametersSetIn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpParametersSetIn.setText("Parameters set in genModel (src-gen)");

		Label lblGenSourceDirectory = new Label(grpParametersSetIn, SWT.NONE);
		lblGenSourceDirectory.setToolTipText("For generated source directory, convention is to have 'src-gen'");
		lblGenSourceDirectory.setText("Gen source directory :");
		lblGenSourceDirectory.setBounds(0, 0, 114, 14);

		genSourceDir = new Text(grpParametersSetIn, SWT.BORDER);
		genSourceDir.setToolTipText("For generated source directory, convention is to have 'src-gen'");
		genSourceDir.setEnabled(false);
		genSourceDir.setEditable(false);
		genSourceDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		genSourceDir.setBounds(0, 0, 259, 19);

		Label lblInterafacePatternName = new Label(grpParametersSetIn, SWT.NONE);
		lblInterafacePatternName
				.setToolTipText("This value comes from the genModel file. {0} is the name of the EClass. A good idea here is to prefix default names with M to mean 'Model' or 'G' to mean 'Generated'. \nExample : M{0}  for the EClass 'Car' will generate the 'MCar' interface");
		lblInterafacePatternName.setText("Gen Interface pattern name :");

		genInterfacePattern = new Text(grpParametersSetIn, SWT.BORDER);
		genInterfacePattern.setEditable(false);
		genInterfacePattern.setEnabled(false);
		genInterfacePattern
				.setToolTipText("This value comes from the genModel file. {0} is the name of the EClass. A good idea here is to prefix default names with M to mean 'Model' or 'G' to mean 'Generated'. \nExample : M{0}  for the EClass 'Car' will generate the 'MCar' interface");
		genInterfacePattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblClassPatternName = new Label(grpParametersSetIn, SWT.NONE);
		lblClassPatternName
				.setToolTipText("This value comes from the genModel file. {0} is the name of the EClass. A good idea here is to prefix default names with M to mean 'Model' or 'G' to mean 'Generated'. \nExample : M{0}Impl  for the EClass 'Car' will generate the 'MCarImpl' class");
		lblClassPatternName.setText("Gen Class pattern name :");
		lblClassPatternName.setBounds(0, 0, 138, 14);

		genClassPattern = new Text(grpParametersSetIn, SWT.BORDER);
		genClassPattern.setEditable(false);
		genClassPattern.setEnabled(false);
		genClassPattern
				.setToolTipText("This value comes from the genModel file. {0} is the name of the EClass. A good idea here is to prefix default names with M to mean 'Model' or 'G' to mean 'Generated'. \nExample : M{0}Impl  for the EClass 'Car' will generate the 'MCarImpl' class");
		genClassPattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		genClassPattern.setBounds(0, 0, 259, 19);

		Group grpParametersForGeneration = new Group(container, SWT.NONE);
		grpParametersForGeneration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpParametersForGeneration.setText("Parameters for dev generation (src)");
		grpParametersForGeneration.setLayout(new GridLayout(2, false));

		Label lblDevSourceDirectory = new Label(grpParametersForGeneration, SWT.NONE);
		lblDevSourceDirectory.setToolTipText("For overridden source directory, convention is to have 'src'");
		lblDevSourceDirectory.setText("Dev source directory :");

		devSourceDir = new Text(grpParametersForGeneration, SWT.BORDER);
		devSourceDir
				.setToolTipText("Set here the name of the folder where the development code structure must be created. It should be 'src' while the model code is generated in a 'src-gen' source folder.");
		devSourceDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDevInterfacePattern = new Label(grpParametersForGeneration, SWT.NONE);
		lblDevInterfacePattern.setText("Dev Interface pattern name :");

		devInterfacePattern = new Text(grpParametersForGeneration, SWT.BORDER);
		devInterfacePattern
				.setToolTipText("This value will be used to generate the dev source structure. {0} is the name of the EClass. A good idea here is to keep the default names if the 'M' prefix has been added for the generated classes. \nExample : {0}  for the EClass 'Car' will generate the 'Car' interface extending the MCar generated interface. ");
		devInterfacePattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDevClassPattern = new Label(grpParametersForGeneration, SWT.NONE);
		lblDevClassPattern.setText("Dev Class pattern name :");

		devClassPattern = new Text(grpParametersForGeneration, SWT.BORDER);
		devClassPattern
				.setToolTipText("This value will be used to generate the dev source structure. {0} is the name of the EClass. \nA good idea here is to keep the default names when the 'M' prefix has been added for the generated classes. \nExample : {0}Impl  for the EClass 'Car' will generate the 'CarImpl' class extending the MCarImpl class and implementing the MCar interface. ");
		devClassPattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		updateValues();
		
		container.pack();

		return container;
	}

	public void setGenModel(GenModel gm)
	{
		genModel = gm;
	}

	private IFile getGenModelFile()
	{
		URI genModelUri = genModel.eResource().getURI();
		IPath p = new Path(genModelUri.toString().replaceFirst("platform:/resource", ""));
		IWorkspaceRoot ws = ResourcesPlugin.getWorkspace().getRoot();
		return ws.getFile(p);
	}

	public void updateValues()
	{

		// Set the default values depending on values found in genmodel and in
		// properties.
		String cp = genModel.getClassNamePattern() == null ? DEFAULT_CLASS_IMPL_PATTERN : genModel
				.getClassNamePattern();
		genClassPattern.setText(cp);
		String ip = genModel.getInterfaceNamePattern() == null ? DEFAULT_INTERFACE_PATTERN : genModel
				.getInterfaceNamePattern();
		genInterfacePattern.setText(ip);
		// Get the gen source directory (remove the model project name)
		String genSrcDirTxt = genModel.getModelDirectory().replace(genModel.getModelProjectDirectory() + "/", "");
		genSourceDir.setText(genSrcDirTxt);

		// Try to restore the previous properties if they exist.
		IFile f = getGenModelFile();
		String cpProp = getProperty(f, PROP_CLASS_PATTERN);
		String ipProp = getProperty(f, PROP_INTERFACE_PATTERN);
		String srcProp = getProperty(f, PROP_SRCDIR);

		if (cp.equals(DEFAULT_CLASS_IMPL_PATTERN))
			devClassPattern.setText(cp + "Ext");
		else
			devClassPattern.setText(cpProp != null ? cpProp : DEFAULT_CLASS_IMPL_PATTERN);

		if (ip.equals(DEFAULT_INTERFACE_PATTERN))
			devInterfacePattern.setText(ip + "Ext");
		else
			devInterfacePattern.setText(ipProp != null ? ipProp : DEFAULT_INTERFACE_PATTERN);

		devSourceDir.setText(srcProp != null ? srcProp : DEFAULT_SRC_DEV);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		button.setToolTipText("Generate the dev directory");
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 300);
	}

	public String getDevInterfacePattern()
	{
		return interfacePattern;
	}

	public String getDevClassPattern()
	{
		return classPattern;
	}

	public String getSrcDir()
	{
		return srcDir;
	}

	@Override
	protected void okPressed()
	{

		// Remember of entered values
		classPattern = devClassPattern.getText();
		interfacePattern = devInterfacePattern.getText();
		srcDir = devSourceDir.getText();

		// Store this values in properties...
		IFile f = getGenModelFile();
		setProperty(f, PROP_SRCDIR, srcDir);
		setProperty(f, PROP_CLASS_PATTERN, classPattern);
		setProperty(f, PROP_INTERFACE_PATTERN, interfacePattern);

		super.okPressed();
	}

	private String getProperty(IFile f, QualifiedName qn)
	{
		String result = null;
		try
		{
			result = f.getPersistentProperty(qn);
		} catch (Exception e)
		{
			result = null;
		}
		return result;
	}

	private void setProperty(IFile f, QualifiedName qn, String value)
	{
		try
		{
			f.setPersistentProperty(qn, value);
		} catch (Exception e)
		{
			ILog logger = Platform.getLog(FrameworkUtil.getBundle(this.getClass()));
			logger.log(new Status(IStatus.WARNING, PLUGIN_ID, "Unable to store the property : " + qn, e));
		}
	}

}
