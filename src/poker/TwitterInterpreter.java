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
public class TwitterInterpreter extends GameState {
	private static TwitterInterpreter instance;

	/* API Keys to our Twitter account (https://twitter.com/poker__bot)
	 *  Remove keys in future if keys if showing off this code to potential employers/ppl
	 *  or you'll get shanked my me.
	 *  Love ben XXX
	* */
	private final String CONSUMER_KEY = "UJ3uKBzdzwHGBx6pRBmM42od8";
	private final String CONSUMER_SECRET = "H7MbO5fkqPjWBEYcZRDsirhwVavKtpKzz8nfrc0LDPZ0Sul2XP";
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

	public String getTwitterScreenName()  {
		try {
			return twitter.getScreenName();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}

	synchronized private void checkStreamStatus(Status status) throws TwitterException {
		String temp = status.getText();
		try {

			if (temp.toLowerCase().contains("dealmein")) {
				postTweet("@" + status.getUser().getScreenName() + " " + "We see you like poker! If you would like to play, just reply to this tweet! \uD83E\uDD16"+PlayerBot.faceTellGenerator(), status);
			}
			if (twitterStream.getScreenName().equalsIgnoreCase(status.getInReplyToScreenName())
					& !status.getUser().getScreenName().equalsIgnoreCase(twitterStream.getScreenName())) {
				parseToGameState(status.getUser(), status,  status.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	synchronized private void parseToGameState(User user,Status status, String TweetBody) {
		PokerGame pokerGame = GameState.getInstance().checkForGameState(user);
		pokerGame.setUserFromTwitter(user);
		try {
			pokerGame.playPoker(TweetBody, status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	StatusListener listener = new StatusListener() {
		public void onStatus(Status status) {
			System.out.println("onStatus START");
			System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			try {
				checkStreamStatus(status);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			System.out.println("onStatus END");

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


	public void postTweet(String strStatus, Status fromStatus) throws Exception {
		StatusUpdate status = new StatusUpdate(strStatus);
		status.setInReplyToStatusId(fromStatus.getId());
		twitter.updateStatus(status);
		System.out.println("Successfully updated the status to [" + status.toString() + "].");
	}



	public void tweetPic(InputStream _file, String theTweet, Status fromStatus) throws Exception {
		StatusUpdate status = new StatusUpdate(theTweet);
		status.setInReplyToStatusId(fromStatus.getId());
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

	public void publicStreamReader(String keywords[]) {
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



