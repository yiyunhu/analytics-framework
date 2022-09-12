package edu.cmu.cs514.hw6.framework;

import edu.cmu.cs514.hw6.plugin.DataPlugin;
import edu.cmu.cs514.hw6.plugin.VisualPlugin;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Document.Type;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementation the interface of {@link SchoolFramework}
 * and register different kinds of plugins
 */
public class SchoolFrameworkImpl implements SchoolFramework{

	private List<DataPlugin> dataPlugins;
	private List<VisualPlugin> visualPlugins;
	private List<processedData> data;

	public SchoolFrameworkImpl() {
		dataPlugins=new ArrayList<>();
		visualPlugins=new ArrayList<>();
		data=new ArrayList<>();
	}

	@Override
	public void registerDataPlugin(DataPlugin plugin) {
		dataPlugins.add(plugin);
	}

	@Override
	public void registerVisualPlugin(VisualPlugin plugin) {
		visualPlugins.add(plugin);
	}

	/**
	 * For each keyword, use plugin to process the data we get
	 * then, sort by timestamp
	 * @param universityName keyword to search for
	 */
	@Override
	public void search(String universityName) {
		data.clear();

		for(DataPlugin plugin: dataPlugins){
			List<String> text = plugin.generate(universityName);
			List<Timestamp> timestamps = plugin.extractTime();
			for (int i = 0; i < text.size(); i++) {
				processedData newData = new processedData(text.get(i), timestamps.get(i));
				data.add(newData);
			}
		}
		//sort by timestamp
		Collections.sort(data);
	}


	/**
	 * Get an emotion score in the range of -1 to 1.
	 * A score less than 0 and up to -1 implies a negative emotion
	 * and a score of more than 0 and up to +1
	 * @throws Exception
	 */
	public void sentimentAnalyze() {
		for (int i = 0; i < data.size(); i++) {
			processedData dataObj = data.get(i);
			String text = dataObj.getText();
			try (LanguageServiceClient language = LanguageServiceClient.create()) {

				// The text to analyze
				Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

				// Detects the sentiment of the text
				com.google.cloud.language.v1.Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

				// Returns an emotion score in the range of -1 to +1.
				// A score less than 0 and up to -1 implies a negative emotion
				// and a score of more than 0 and up to +1 implies positive emotions.
				// Magnitude is used to measure the amount of emotional content found in the text.
				// Its value can range from 0 to infinity.
				dataObj.setScore(sentiment.getScore());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * visualize for each registered visualPlugin,
	 * send the list of processedData and call the draw function
	 * and return the bitMap
	 * @return a JSONObect which hold the bitMap information
	 */
	public JSONObject visualize(){

		JSONArray arr = new JSONArray();
		for (int i = 0; i < visualPlugins.size(); i++) {
			arr.put(new JSONObject(visualPlugins.get(i).draw(data)));
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("displayList",arr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
