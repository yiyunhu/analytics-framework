package edu.cmu.cs514.hw6.plugin;

import java.sql.Timestamp;
import java.util.List;

/**
 * Data plug-in interface.
 */
public interface DataPlugin {

	/**
	 * Method to generate and start fetching
	 * @param keyword the keyword we want to search for
	 * @return a string of information we get
	 */
	public List<String> generate(String keyword);

	/**
	 * Method to extract the time information we get
	 * @return a list in timestamp
	 */
	public List<Timestamp> extractTime();

}
