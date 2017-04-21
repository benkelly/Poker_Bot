package poker;

import sun.rmi.runtime.Log;
import twitter4j.*;
import twitter4j.conf.*;


import twitter4j.conf.*;
import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.api.*;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;


import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */
public class TwitterInterpreter {
	private static TwitterInterpreter instance;

	/* API Keys to our Twitter account (https://twitter.com/poker__bot)
	 *  Remove keys in future if keys if showing off this code to potential employers/ppl
	 *  or you'll get shanked my me.
	 *  Love ben XXX
	* */
	private final String CONSUMER_KEY = "0WPzHZO7lXrVSDgfTXlg1Taid";
	private final String CONSUMER_SECRET = "RKaY2xEiJpjfJuTAjRtxugdGb8WLonBQ1UAzuLwBtmTFPGrT2q";
	private final String ACCESS_TOKEN = "824278872212512768-zibsklC9KmsURIg0APsDFEvQMIWAXWe";
	private final String ACCESS_TOKEN_SECRET = "BpIBU1x5t4sHf9ZJ5zNduWDngKDxn9vsQd2Yr3RHw42JA";

	private Twitter twitter; // thread safe, initialised in setTwitterInstance()
	private TwitterStream twitterStream;
	private Paging repliesPage = new Paging();

	ImageUploadFactory imageUploadFactory;

	public static TwitterInterpreter getInstance()
	{
		if (instance == null) { instance = new TwitterInterpreter();}
		return instance;
	}
	public TwitterInterpreter() {
		setTwitterInstance();
		setTwitterStreamInstance();
	}

	/*Set ConfigurationBuilder with twitter API keys.
	* */
	private final ConfigurationBuilder getConfigBuilder() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(CONSUMER_KEY)
				.setOAuthConsumerSecret(CONSUMER_SECRET)
				.setOAuthAccessToken(ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
		return cb;
	}
	/* initialises app with twitter api keys and makes TwitterFactory instance
	* */
	private final void setTwitterInstance() {
		TwitterFactory tf = new TwitterFactory(getConfigBuilder().build());
		twitter = tf.getInstance();
	}
	/* initialises app with twitter api keys and makes TwitterStreamFactory instance
	* */
	private final void setTwitterStreamInstance() {
		TwitterStreamFactory tsf = new TwitterStreamFactory(getConfigBuilder().build());
		twitterStream = tsf.getInstance();
	}



	private void postTweet(String strStatus) {
		Status status = null;
		try {
			status = twitter.updateStatus(strStatus);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		System.out.println("Successfully updated the status to [" + status.getText() + "].");

	}


	public void tweetPic(InputStream _file, String theTweet) throws Exception {
		StatusUpdate status = new StatusUpdate(theTweet);
		status.setMedia("image.jpg", _file);
		twitter.updateStatus(status);
	}


	private void getTimeline() {
		List<Status> statuses = null;
		try {
			statuses = twitter.getHomeTimeline();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		System.out.println("Showing home timeline.");
		for (Status status : statuses) {
			System.out.println(status.getUser().getName() + ":" +
					status.getText());
		}
	}

	private void sendDirectMessages(String recipientName, String message) {
		DirectMessage msg = null;
		try {
			msg = twitter.sendDirectMessage(recipientName, message);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		System.out.println("Sent: "+ msg.getText() + " to @" + msg.getRecipientScreenName());
	}

	private void searchForTweets(String queryStr) {
		Query query = new Query(queryStr);
		QueryResult result = null;
		try {
			result = twitter.search(query);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		for (Status status : result.getTweets()) {
			System.out.println("@" + status.getUser().getScreenName() + " : " + status.getText());
		}
	}


	private void repliesToBot() {
		long recentid = 1;
		List<Status> statuses = null;
		try {
			statuses = twitter.getMentionsTimeline(repliesPage.sinceId(recentid));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		for (Status status : statuses) {
			recentid = Math.min(status.getId(),recentid);
			//System.out.println("@" + status.getUser().getScreenName() + " : " + status.getText());
			repliesPage.setSinceId(status.getId());
		}
	}

	private void repliesToBotLoop() {
		do {
			final long startTime = System.nanoTime();

			this.repliesToBot();

			final long duration = System.nanoTime() - startTime;
			if ((5500 - duration / 1000000) > 0) {
				//logger.info("Sleep for " + (6000 - duration / 1000000) + " miliseconds");
				try {
					Thread.sleep((5500 - duration / 1000000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (true);
	}


	private void publicStreamReader() {

	}



	/*Class testing method
	* */
	public static void main(String[] args)  {
		System.out.println("poker.TwitterInterpreter.java!");

		TwitterInterpreter ti = new TwitterInterpreter();
		//ti.postTweet("Hello joe :^)");
		//ti.postTweet("oo");
		//ti.getTimeline();
		//ti.sendDirectMessages("b3nkelly", "hey sxc ;)");
		ti.searchForTweets("trump");
		//ti.repliesToBot();
		//ti.repliesToBotLoop();

	}
}



