package com.opcoach.genmodeladdon.nature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.opcoach.genmodeladdon.core.GMAConstants;

public class GMANature implements IProjectNature
{
	private IProject project;

	/**
	 * Constructor
	 */
	public GMANature() {
		super();
	}

	@Override
	public void configure() throws CoreException {
	}

	@Override
	public void deconfigure() throws CoreException {
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * Toggles a nature on a project.
	 *
	 * @param project
	 *            to have sample nature added or removed
	 * @param natureID the nature ID to toggle
	 * @throws CoreException issue while toggling nature
	 */
	public static void toggleNature(IProject project) throws CoreException {
		final IProjectDescription description = project.getDescription();
		final String[] natures = description.getNatureIds();

		for (int i = 0; i < natures.length; ++i) {
			if (GMAConstants.NATURE_ID.equals(natures[i])) {
				// Remove the nature
				final String[] newNatures = new String[natures.length - 1];
				System.arraycopy(natures, 0, newNatures, 0, i);
				System.arraycopy(natures, i + 1, newNatures, i, natures.length - i - 1);
				description.setNatureIds(newNatures);
				project.setDescription(description, null);
				return;
			}
		}

		// Add the nature
		final String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = GMAConstants.NATURE_ID;
		description.setNatureIds(newNatures);
		project.setDescription(description, null);
	}

}
