package com.opcoach.genmodeladdon.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/** A specific dialog to select the files that should be overridden */
public class ConfirmFileSelectionDialog extends MessageDialog
{
	private Map<String, Object> filesNotYetGenerated;
	private Collection<String> filesToBeGenerated;
	private CheckboxTableViewer tv;
	private String relativeDir;
	private ImageRegistry registry;

	private static final String IMG_CHECKBOX_SELECTED = "icons/checkbox_selected.gif";
	private static final String IMG_CHECKBOX_UNSELECTED = "icons/checkbox_unselected.gif";

	public ConfirmFileSelectionDialog(Shell parentShell, Map<String, Object> filesNotGenerated, String pRelativeDir)
	{
		super(
				parentShell,
				"Overriding existing file(s)",
				null,
				"Some files already exist.\nSelect the files you want to override.\nA tooltip will display the content of the file if it is kept for generation",
				MessageDialog.CONFIRM, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		filesNotYetGenerated = filesNotGenerated;
		relativeDir = pRelativeDir + File.separator;
		filesToBeGenerated = new ArrayList<String>();

	}

	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite root = new Composite(parent, SWT.NONE);
		root.setLayout(new GridLayout(2, false));

		tv = CheckboxTableViewer.newCheckList(root, SWT.NONE);
		final Table cTable = tv.getTable();
		cTable.setLinesVisible(true);
		GridData gd_cTable = new GridData(SWT.FILL, SWT.TOP, true, true);
		cTable.setLayoutData(gd_cTable);

		TableViewerColumn filenameCol = new TableViewerColumn(tv, SWT.LEAD);
		filenameCol.getColumn().setWidth(400);
		filenameCol.getColumn().setText("Filename");
		filenameCol.setLabelProvider(new ColumnLabelProvider()
			{
				@Override
				public String getText(Object element)
				{
					String absName = element.toString();
					int pos = absName.indexOf(relativeDir);
					return element.toString().substring(pos);
				}

				@Override
				public String getToolTipText(Object element)
				{
					return "This content will be generated: \n-----------------------------------\n"
							+ filesNotYetGenerated.get(element);
				}
			});
		ColumnViewerToolTipSupport.enableFor(tv, ToolTip.NO_RECREATE);

		tv.setContentProvider(ArrayContentProvider.getInstance());
		tv.setInput(filesNotYetGenerated.keySet());

		tv.addCheckStateListener(new ICheckStateListener()
			{

				@Override
				public void checkStateChanged(CheckStateChangedEvent event)
				{
					if (event.getChecked())
					{
						filesToBeGenerated.add((String) event.getElement());
					} else
					{
						filesToBeGenerated.remove(event.getElement());
					}
				}
			});

		Composite selectComposite = new Composite(root, SWT.NONE);
		selectComposite.setLayout(new GridLayout(1, false));

		Button selectAll = new Button(selectComposite, SWT.PUSH);
		selectAll.setImage(getLocalImage(IMG_CHECKBOX_SELECTED));
		selectAll.setToolTipText("Select all");
		selectAll.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					tv.setAllChecked(true);
					filesToBeGenerated.addAll(filesNotYetGenerated.keySet());
				}
			});

		Button deselectAll = new Button(selectComposite, SWT.PUSH);
		deselectAll.setImage(getLocalImage(IMG_CHECKBOX_UNSELECTED));
		deselectAll.setToolTipText("Unselect all");
		deselectAll.addSelectionListener(new SelectionAdapter()
			{
				@Override
				public void widgetSelected(SelectionEvent e)
				{
					tv.setAllChecked(false);
					filesToBeGenerated.clear();

				}
			});

		return root;
	}

	private Image getLocalImage(String key)
	{
		if (registry == null)
		{
			Bundle b = FrameworkUtil.getBundle(getClass());
			registry = new ImageRegistry();
			registry.put(IMG_CHECKBOX_SELECTED, ImageDescriptor.createFromURL(b.getEntry(IMG_CHECKBOX_SELECTED)));
			registry.put(IMG_CHECKBOX_UNSELECTED, ImageDescriptor.createFromURL(b.getEntry(IMG_CHECKBOX_UNSELECTED)));
		}

		return registry.get(key);
	}
	
	public Collection<String> getFilesToBeGenerated()
	{
		return filesToBeGenerated;
	}


}