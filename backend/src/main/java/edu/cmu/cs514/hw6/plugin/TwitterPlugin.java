package edu.cmu.cs514.hw6.plugin;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import twitter4j.*;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterPlugin implements DataPlugin {

    private static final String CONSUMER_KEY = "sYlgnpHwt5gpzU5T4z9tQk2u4";
    private static final String CONSUMER_SECRET = "ahRRKg891kexjgg7HyKiflO7vrtzwdn5W7DVvlh8uy14tiCQpK";
    private static final int TWEETS_PER_QUERY = 10;
    private static final int MAX_QUERIES = 3;
    private static final String SEARCH_TERM = "Carnegie Mellon";


    /**
     * Method to generate and start fetching from Twitter API
     * @param keyword the keyword we want to search for
     * @return fetched information from Twitter API
     */
    @Override
    public List<String> generate(String keyword) {
        List<String> data = new ArrayList<>();
        long maxID = -1;
        Twitter twitter = getTwitter();

        try {
            Query q = new Query(SEARCH_TERM);
            q.setCount(TWEETS_PER_QUERY);
            q.resultType(Query.ResultType.valueOf("recent"));
            q.setLang("en");

            if (maxID != -1) {
                q.setMaxId(maxID - 1);
            }

            QueryResult r = twitter.search(q);

            for (Status s: r.getTweets()) {
                if (maxID == -1 || s.getId() < maxID) {
                    maxID = s.getId();
                }
                data.add(cleanText(s.getText()));
            }
        }
        catch (Exception e) {
            System.out.println("Something wrong...");
            e.printStackTrace();
        }
        return data;
    }


    /**
     * Method to extract the time information we get from Twitter API
     * @return a list in timestamp
     */
    @Override
    public List<Timestamp> extractTime() {
        List<Timestamp> times = new ArrayList<>();
        long maxID = -1;
        Twitter twitter = getTwitter();

        try {
            Query q = new Query(SEARCH_TERM);
            q.setCount(TWEETS_PER_QUERY);
            q.resultType(Query.ResultType.valueOf("recent"));
            q.setLang("en");

            if (maxID != -1) {
                q.setMaxId(maxID - 1);
            }

            QueryResult r = twitter.search(q);

            for (Status s: r.getTweets()) {
                if (maxID == -1 || s.getId() < maxID) {
                    maxID = s.getId();
                }
                Timestamp ts = new Timestamp(s.getCreatedAt().getTime());
                times.add(ts);
            }
        }
        catch (Exception e) {
            System.out.println("Something wrong...");
            e.printStackTrace();
        }
        return times;
    }

    /**
     * Replace newlines and tabs in text with escaped versions to making printing cleaner
     *
     * @param text	The text of a tweet, sometimes with embedded newlines and tabs
     * @return		The text passed in, but with the newlines and tabs replaced
     */
    public static String cleanText(String text) {
        text = text.replace("\n", "\\n");
        text = text.replace("\t", "\\t");
        return text;
    }

    /**
     * Retrieve the "bearer" token from Twitter in order to make application-authenticated calls.
     *
     * This is the first step in doing application authentication, as described in Twitter's documentation at
     * https://dev.twitter.com/docs/auth/application-only-auth
     *
     * If there's an error in this process, we just print a message and quit.
     * @return	The oAuth2 bearer token
     */
    public static OAuth2Token getOAuth2Token() {
        OAuth2Token token = null;
        ConfigurationBuilder cb;

        cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true);

        cb.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET);

        try {
            token = new TwitterFactory(cb.build()).getInstance().getOAuth2Token();
        }
        catch (Exception e) {
            System.out.println("Fail to get the OAuth2 token.");
            e.printStackTrace();
            System.exit(0);
        }
        return token;
    }

    /**
     * Get a fully application-authenticated Twitter object useful for making subsequent calls.
     * @return	Twitter4J Twitter object that's ready for API calls
     */
    public static Twitter getTwitter() {
        OAuth2Token token;
        token = getOAuth2Token();
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true);
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);

        cb.setOAuth2TokenType(token.getTokenType());
        cb.setOAuth2AccessToken(token.getAccessToken());
        //	create the Twitter object
        return new TwitterFactory(cb.build()).getInstance();
    }
    
}
