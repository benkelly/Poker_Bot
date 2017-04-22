package poker;

import twitter4j.*;


import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


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

	private void checkStreamStatus(Status status) throws TwitterException {
		String temp = status.getText();
		if (temp.toLowerCase().contains("dealmein")) {
			postTweet("@" + status.getUser().getScreenName() + " " + "yoyo, see you like poker \uD83E\uDD16");
		}
		else if (status.getInReplyToScreenName().equalsIgnoreCase(twitter.getScreenName())) {
			//postTweet("@" + status.getUser().getScreenName() + " " + "tks for the mention \uD83E\uDD16");
			//User tempUser = status.getUser();
			parseToGameState(status.getUser());
		}
	}

	private void parseToGameState(User user) {
		PokerGame pokerGame = GameState.getInstance().checkForGameState(user);
		pokerGame.setUserFromTwitter(user);
		//pokerGame.;
		pokerGame.playPoker();
	}

	StatusListener listener = new StatusListener() {
		public void onStatus(Status status) {
			System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			try {
				checkStreamStatus(status);
			} catch (TwitterException e) {
				e.printStackTrace();
			}

		}

		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
		}

		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
		}

		public void onScrubGeo(long userId, long upToStatusId) {
			System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
		}

		@Override
		public void onStallWarning(StallWarning stallWarning) {

		}

		public void onException(Exception ex) {
			ex.printStackTrace();
		}
	};


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

	private void publicStreamReader(String keyword) {
		String keywords[] = {keyword};
		publicStreamReader(keywords);
	}

	private void publicStreamReader(String keywords[]) {
		FilterQuery fq = new FilterQuery();
		//String keywords[] = {"France", "Germany"};
		fq.track(keywords);
		this.twitterStream.addListener(listener);
		this.twitterStream.filter(fq);
	}



	/*Class testing method
	* */
	public static void main(String[] args)  {
		System.out.println("poker.TwitterInterpreter.java!");

		TwitterInterpreter ti = new TwitterInterpreter();
		//ti.postTweet("Hello joe :^)");
		//ti.postTweet("Some text" + "U+1F638"+ "\uD83D\uDE0D" + "" + "" + " \uD83D\uDE00\uD83D\uDE2C\uD83D\uDE01\uD83D\uDE02\uD83D\uDE03\uD83D\uDE04\uD83D\uDE05\uD83D\uDE06\uD83D\uDE07\uD83D\uDE09\uD83D\uDE0A\uD83D\uDE42\uD83D\uDE43☺️\uD83D\uDE0B\uD83D\uDE0C\uD83D\uDE0D\uD83D\uDE18\uD83D\uDE17\uD83D\uDE19\uD83D\uDE1A\uD83D\uDE1C\uD83D\uDE1D\uD83D\uDE1B\uD83E\uDD11\uD83E\uDD13\uD83D\uDE0E\uD83E\uDD17\uD83D\uDE0F\uD83D\uDE36\uD83D\uDE10\uD83D\uDE11\uD83D\uDE12\uD83D\uDE44\uD83E\uDD14\uD83D\uDE33\uD83D\uDE1E\uD83D\uDE1F\uD83D\uDE20\uD83D\uDE21\uD83D\uDE14\uD83D\uDE15\uD83D\uDE41☹️\uD83D\uDE23\uD83D\uDE16\uD83D\uDE2B\uD83D\uDE29\uD83D\uDE24\uD83D\uDE2E\uD83D\uDE31\uD83D\uDE28\uD83D\uDE30\uD83D\uDE2F\uD83D\uDE26\uD83D\uDE27\uD83D\uDE22\uD83D\uDE25\uD83D\uDE2A\uD83D\uDE13\uD83D\uDE2D\uD83D\uDE35\uD83D\uDE32\uD83E\uDD10\uD83D\uDE37\uD83E\uDD12\uD83E\uDD15\uD83D\uDE34\uD83D\uDCA9\uD83D\uDE08\uD83D\uDC7D\uD83E\uDD16");
		//ti.getTimeline();
		//ti.sendDirectMessages("b3nkelly", "hey sxc ;)");
		//ti.searchForTweets("trump");
		//ti.repliesToBot();
		//ti.repliesToBotLoop();

		String keywords[] = {"dealmein", "poker__bot"};
		ti.publicStreamReader(keywords);


	}
}



