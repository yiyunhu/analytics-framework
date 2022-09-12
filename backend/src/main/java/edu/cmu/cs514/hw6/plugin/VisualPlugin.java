package edu.cmu.cs514.hw6.plugin;

import edu.cmu.cs514.hw6.framework.processedData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Visulization plug-in interface.
 */
public interface VisualPlugin {
	public String draw(List<processedData> data);
}
