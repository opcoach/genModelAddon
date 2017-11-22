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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
	
	private static final Color tooltipFileBg = new Color(Display.getDefault(), 251, 215, 150); // #FBD796

	public ConfirmFileSelectionDialog(Shell parentShell, Map<String, Object> filesNotGenerated, String pRelativeDir)
	{
		super(parentShell, "Overriding existing file(s)", null,
				"Some files already exist.\nSelect the files you want to override.\nDrag over the files to display the generated content if it is selected in the list",
				MessageDialog.CONFIRM, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		filesNotYetGenerated = filesNotGenerated;
		relativeDir = pRelativeDir + File.separator;
		filesToBeGenerated = new ArrayList<String>();
		setShellStyle(getShellStyle() | SWT.RESIZE);

	}

	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite root = new Composite(parent, SWT.NONE);
		root.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		root.setLayout(new GridLayout(2, false));

		ScrolledComposite tableParent = new ScrolledComposite(root, SWT.V_SCROLL | SWT.H_SCROLL);
		tableParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableParent.setLayout(new FillLayout());
		tv = CheckboxTableViewer.newCheckList(tableParent, SWT.BORDER);
		final Table cTable = tv.getTable();
		cTable.setLinesVisible(true);

		tableParent.setContent(tv.getControl());
		tableParent.setExpandHorizontal(true);
		tableParent.setExpandVertical(true);
		tableParent.setAlwaysShowScrollBars(false);

		// sort elements
		tv.setComparator(new ViewerComparator()
			{
				@Override
				public int compare(Viewer viewer, Object e1, Object e2)
				{
					String f1 = getText(e1);
					String f2 = getText(e2);
					int pos1 = f1.lastIndexOf('/');
					int pos2 = f2.lastIndexOf('/');
					int pathComp = String.CASE_INSENSITIVE_ORDER.compare(f1.substring(0, pos1), f2.substring(0, pos2));
					return (pathComp != 0) ? pathComp : String.CASE_INSENSITIVE_ORDER.compare(f1.substring(pos1 + 1, f1.length()), f2.substring(pos2 + 1, f2.length()));
				}

				private String getText(Object element)
				{
					String absName = element.toString();
					int pos = absName.indexOf(relativeDir);
					return absName.substring(pos).replace('\\', '/');
				}
			});

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
					return element.toString().substring(pos).replace('\\', '/');
				}

				@Override
				public String getToolTipText(Object element)
				{
					return "This content will be generated: \n-----------------------------------\n"
							+ filesNotYetGenerated.get(element);
				}
				
				@Override
				public Color getToolTipBackgroundColor(Object object)
				{
					return tooltipFileBg;
				}
			});
		ColumnViewerToolTipSupport.enableFor(tv, ToolTip.NO_RECREATE);

		tv.setContentProvider(ArrayContentProvider.getInstance());
		tv.setInput(filesNotYetGenerated.keySet());

		filenameCol.getColumn().pack();

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