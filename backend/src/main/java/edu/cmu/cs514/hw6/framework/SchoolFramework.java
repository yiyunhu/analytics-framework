package edu.cmu.cs514.hw6.framework;

import edu.cmu.cs514.hw6.plugin.DataPlugin;
import edu.cmu.cs514.hw6.plugin.VisualPlugin;

/**
 * The interface by which can directly interact
 * with the School framework.
 */
public interface SchoolFramework {
	/**
	 * Method to register the data plugin
	 * @param plugin
	 */
	public void registerDataPlugin(DataPlugin plugin);

	/**
	 * Method to register the visualization plugin
	 * @param plugin
	 */
	public void registerVisualPlugin(VisualPlugin plugin);

	/**
	 * Search for the keyword
	 * @param universityName
	 */
	public void search(String universityName);
}
