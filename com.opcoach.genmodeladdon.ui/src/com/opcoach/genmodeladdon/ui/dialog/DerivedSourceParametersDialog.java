package com.opcoach.genmodeladdon.ui.dialog;

import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
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

public class DerivedSourceParametersDialog extends Dialog {
	
	private Text genInterfacePattern;
	private Text genClassPattern;
	private Text genSourceDir;
	private Text devSourceDir;
	private Text devInterfacePattern;
	private Text devClassPattern;
	private GenModel genModel;
	private String interfacePattern;
	private String classPattern;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DerivedSourceParametersDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
		parentShell.setText("Enter the parameters for dev generation");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
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
		genSourceDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		genSourceDir.setBounds(0, 0, 259, 19);
		
		Label lblInterafacePatternName = new Label(grpParametersSetIn, SWT.NONE);
		lblInterafacePatternName.setText("Gen Interface pattern name :");
		
		genInterfacePattern = new Text(grpParametersSetIn, SWT.BORDER);
		genInterfacePattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblClassPatternName = new Label(grpParametersSetIn, SWT.NONE);
		lblClassPatternName.setText("Gen Class pattern name :");
		lblClassPatternName.setBounds(0, 0, 138, 14);
		
		genClassPattern = new Text(grpParametersSetIn, SWT.BORDER);
		genClassPattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		genClassPattern.setBounds(0, 0, 259, 19);
		
		Group grpParametersForGeneration = new Group(container, SWT.NONE);
		grpParametersForGeneration.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpParametersForGeneration.setText("Parameters for dev generation (src)");
		grpParametersForGeneration.setLayout(new GridLayout(2, false));
		
		Label lblDevSourceDirectory = new Label(grpParametersForGeneration, SWT.NONE);
		lblDevSourceDirectory.setToolTipText("For overriden source directory, convention is to have 'src'");
		lblDevSourceDirectory.setText("Dev source directory :");
		
		devSourceDir = new Text(grpParametersForGeneration, SWT.BORDER);
		devSourceDir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDevInterfacePattern = new Label(grpParametersForGeneration, SWT.NONE);
		lblDevInterfacePattern.setText("Dev Interface pattern name :");
		
		devInterfacePattern = new Text(grpParametersForGeneration, SWT.BORDER);
		devInterfacePattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDevClassPattern = new Label(grpParametersForGeneration, SWT.NONE);
		lblDevClassPattern.setText("Dev Class pattern name :");
		
		devClassPattern = new Text(grpParametersForGeneration, SWT.BORDER);
		devClassPattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		updateValues();
		
		return container;
	}
	
	
	public void setGenModel(GenModel gm)
	{
		genModel = gm;
	}
	
	public void updateValues()
	{
		String cp = genModel.getClassNamePattern() == null ? "{0}Impl" : genModel.getClassNamePattern();
		genClassPattern.setText(cp);
		String ip = genModel.getInterfaceNamePattern() == null ? "{0}" : genModel.getInterfaceNamePattern();
		genInterfacePattern.setText(ip);
		genSourceDir.setText( genModel.getModelDirectory());
		
		devClassPattern.setText(cp + "Ext");
		devInterfacePattern.setText(ip + "Ext");
		devSourceDir.setText(genModel.getModelProjectDirectory() + "/src");
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		button.setToolTipText("Generate the dev directory");
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public String getDevInterfacePattern() {
		return interfacePattern;
	}

	public String getDevClassPattern() {
		return classPattern;
	}
	
	@Override
	protected void okPressed() {
		
		// Remember of entered values
		classPattern = devClassPattern.getText();
		interfacePattern = devInterfacePattern.getText();
		
		super.okPressed();
	}
}
