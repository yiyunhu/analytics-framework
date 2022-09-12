package edu.cmu.cs514.hw6.plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

public class NewsPlugin implements DataPlugin{

	private final String API_key = "596a283e3a8c4a77b5ff884d6108099b";
	private final String from = "2022-04-01";
	private final String to = "2022-04-15";
	private String urlString = "https://newsapi.org/v2/everything?language=en&from="+from+"&to="+to+"&apiKey="+API_key+"&q=";
	private JSONObject obj = null;


	/**
	 * Method to generate and start fetching from News API
	 * @param keyword the keyword we want to search for
	 * @return fetched information from News API
	 */
	@Override
	public List<String> generate(String keyword) {
		String url = urlString + keyword;
		String jsonString = fetch(url);
		List<String> data = new ArrayList<>();

		try {
			obj = new JSONObject(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			return null; // so compiler does not complain
		}
		if (obj == null
				|| !obj.getString("status").equalsIgnoreCase("ok")
				|| obj.getInt("totalResults")==0){
			return data;
		} else {
			JSONArray articles = obj.getJSONArray("articles");
			for (int i = 0; i < articles.length(); i++) {
				JSONObject article = articles.getJSONObject(i);
				data.add(article.getString("description") + article.getString("content"));
			}
			return data;
		}

	}

	/**
	 * Method to extract the time information we get from News API
	 * @return a list in timestamp
	 */
	@Override
	public List<Timestamp> extractTime() {
		List<Timestamp> times = new ArrayList<>();
		if (obj == null
				|| !obj.getString("status").equalsIgnoreCase("ok")
				|| obj.getInt("totalResults")==0){
			return times;
		} else {
			JSONArray articles = obj.getJSONArray("articles");
			for (int i = 0; i < articles.length(); i++) {
				JSONObject article = articles.getJSONObject(i);
				String time = article.getString("publishedAt");
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				try {
					Date date = dateFormat.parse(time);
					times.add(new Timestamp(date.getTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return times;
		}
	}

	private String fetch(String urlString) {
		String response = "";
		try {
			URL url = new URL(urlString);
			/*
			 * Create an HttpURLConnection.  This is useful for setting headers
			 * and for getting the path of the resource that is returned (which
			 * may be different than the URL above if redirected).
			 * HttpsURLConnection (with an "s") can be used if required by the site.
			 */
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String str;
			// Read each line of "in" until done, adding each to "response"
			while ((str = in.readLine()) != null) {
				// str is one line of text readLine() strips newline characters
				response += str;
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Eeek, an exception");
			// Do something reasonable.  This is left for students to do.
		}
		return response;
	}
}
