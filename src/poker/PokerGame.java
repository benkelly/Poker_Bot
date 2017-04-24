package poker;

import twitter4j.*;

import java.util.ArrayList;
import java.util.Date;

import java.io.*;



/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */

public class PokerGame extends ArrayList<PokerPlayer> {

	// fixed game stats
	private static final int MAX_BOTS = 3;
	private static final int INITIAL_CHIP_AMOUNT = 3000;
	public static final int MAX_HAND = 5;
	public static final int MAX_DISCARD = 3;

	// game stats
	private DeckOfCards gameDeck;
	private boolean gameOver = false;
	private int roundCount = 0;
	private  boolean firstRound = true;

	private  boolean hasSetPokerTable = false;
	private  boolean hasCurrentRoundBeenDealt = false;
	private  boolean hasCurrentRoundPlayersHandOptionsTweetedAndReplied = false;
	private  boolean hasCurrentRoundPlayersBettingOptionsTweetedAndReplied = false;
	private  boolean hasCurrentRoundPlayersAnteFeeOptionsTweetedAndReplied = true;
	private  boolean hasCurrentRoundPlayersAnteFeeOptionsTweeted = false;

	// current round variables
	private int currentRoundsAnteAmount = 100; // the current round ante, entering fee
	private int currentRoundsStakeAmount = currentRoundsAnteAmount; // the current round cost
	private int currentRoundsHeldStake = 0; // the current pot (the ships on the table)
	private ArrayList<PokerPlayer> curRoundPlayerList = new ArrayList<PokerPlayer>(); // player playing that current hand of poker

	public User user;
	//public String userName = user.getScreenName();
	public static final String database="resources/database.csv"; 
	
	public String tweetStr = "";


	public PokerGame() {
	}


	public void playPoker(String TweetBody, Status status) {
		if (!gameOver) {
			if (!hasSetPokerTable) {
				setPokerTable(); // adds human and bots to poker table
				for (PokerPlayer player : this) { // if human player has no chips left
					if(player.isHuman & player.getPlayerChipAmount() < INITIAL_CHIP_AMOUNT) {
						player.setPlayerChipAmount(INITIAL_CHIP_AMOUNT);
					}
				}
				hasSetPokerTable = true;
			}
			if (!hasCurrentRoundBeenDealt) {
				if (firstRound) {
					payAnteFee(currentRoundsAnteAmount); // players pay their ante to enter game.
					System.out.println("**********payAnteFee PAID!!!!: " + currentRoundsAnteAmount);
					System.out.println("**********currentRoundsHeldStake on poker Table!!!!: " + currentRoundsHeldStake);
				}

				System.out.println("hasCurrentRoundBeenDealt START!");

				if (!hasCurrentRoundPlayersAnteFeeOptionsTweetedAndReplied) {
					if (!hasCurrentRoundPlayersAnteFeeOptionsTweeted) {
						System.out.println("(!hasCurrentRoundPlayersAnteFeeOptionsTweeted) START!");

						tweetStr += "@" + user.getScreenName() + " do you want to play another round (y/n)?"+PlayerBot.faceTellGenerator();
						TwitterInterpreter.getInstance().postTweet(tweetStr);
						tweetStr = "";
						hasCurrentRoundPlayersAnteFeeOptionsTweeted = true;
						return;
					}
					if (hasCurrentRoundPlayersAnteFeeOptionsTweeted) {
						System.out.println("(!\t\t\t\t\tString cleanedUpInputTweet = cleanUpInputTweet(tweetStr).toLowerCase();\n) START!");
						String cleanedUpInputTweet = cleanUpInputTweet(TweetBody).toLowerCase();
						System.out.println("cleanedUpInputTweet:  "+cleanedUpInputTweet);
						System.out.println("(!\t\t\t\t\tString cleanedUpInputTweet = cleanUpInputTweet(tweetStr).toLowerCase();\n) END!");

						if (cleanedUpInputTweet.startsWith("y") | cleanedUpInputTweet.contains("yes")) {
							System.out.println("(!\t\t\t\t\tcleanedUpInputTweet.startsWith(\"y\") | cleanedUpInputTweet.contains(\"yes\"))!");

							if( !payAnteFee(currentRoundsAnteAmount) ) { // players pay their ante to enter game.
								tweetStr += "@" + user.getScreenName() + " \n\n";
								TwitterInterpreter.getInstance().postTweet(tweetStr);
								tweetStr = "";
								return;
							}
							hasCurrentRoundPlayersAnteFeeOptionsTweetedAndReplied = true;
							System.out.println("(!\t\t\t\t\tcleanedUpInputTweet.startsWith(\"y\") | cleanedUpInputTweet.contains(\"yes\"))! BEFORE LOOP");

							playPoker("", status);
						} else {
							System.out.println("(!\t\t\t\t\tELSE START ");
							gameOver = true;
							tweetStr += "@" + user.getScreenName() + " Come back again!!"+PlayerBot.faceTellGenerator();
							TwitterInterpreter.getInstance().postTweet(tweetStr);
							tweetStr = "";
							System.out.println("(!\t\t\t\t\tELSE END ");
							return;
						}
					}
				}


				dealOutCards(firstRound); // deals out new cards.

				while (checkForBumDeck()) {
				} // will re-deal till at least a play has a pair
				hasCurrentRoundBeenDealt = true;

			}


			if (!hasCurrentRoundPlayersHandOptionsTweetedAndReplied) {
				if (currentRoundPlayersHandOptions(TweetBody) == false) {
					return;
				}
			}

			if (!hasCurrentRoundPlayersBettingOptionsTweetedAndReplied) {
				if (currentRoundPlayersBettingOptions(TweetBody) == false) {
					return;
				}
			}

			if (hasCurrentRoundPlayersBettingOptionsTweetedAndReplied
					& hasCurrentRoundPlayersHandOptionsTweetedAndReplied) {
				getRoundWinner(); // calculates winning hand and pays that player the pot.
				resetRound(); // clears round.
				playPoker("", status);
			}
		}
		if ( gameOver ) { // start up a new game of poker
			String cleanedUpInputTweet = cleanUpInputTweet(TweetBody).toLowerCase();
			if( cleanedUpInputTweet.toLowerCase().startsWith("y") | cleanedUpInputTweet.toLowerCase().contains("yes")) {
				gameOver = false;
				hasSetPokerTable = false;
				resetRound();
				hasCurrentRoundPlayersAnteFeeOptionsTweetedAndReplied = false;
				this.clear();
				playPoker("", status);			}
			else {
				tweetStr = "Hey @" + user.getScreenName() + "! " + PlayerBot.faceTellGenerator() + "\n";
				tweetStr += "You're currently not in any game. Like me to set up a new poker table up?\n(y/n)";
				TwitterInterpreter.getInstance().postTweet(tweetStr);
				tweetStr = "";
			}
		}
	}

	/*adds poker players to game.
	* */
	private void setPokerTable() {
		// add bots
				//before that we need check whether those information are stored in database already.
		String  informationIndatabase=getScreenNameIndatabase(user.getScreenName());

		if(!informationIndatabase.equals("")){
			String[] tempSample=informationIndatabase.split(",");
			int numOfPlayer=Integer.parseInt(tempSample[1]);
			PokerPlayer[] players=new PokerPlayer[numOfPlayer];

			for(int i=0;i<=numOfPlayer-2;i++){

				String name=tempSample[2*(i+1)];
				int amountOfChips=Integer.parseInt(tempSample[2*(i+1)+1]);
				this.add(new PlayerBot(name,this,gameDeck,amountOfChips));
			}

			int amountOfhuman=Integer.parseInt(tempSample[tempSample.length-1]);
			this.add(new PokerPlayer(user.getScreenName(), this, gameDeck, amountOfhuman, true)); // add human user.
			// human last may be nicer for tweet format.
		}
			else {


			for (int j = 0; j < MAX_BOTS; j++) {
				this.add(new PlayerBot(this, gameDeck, INITIAL_CHIP_AMOUNT));
			}
			this.add(new PokerPlayer(user.getScreenName(), this, gameDeck, INITIAL_CHIP_AMOUNT, true)); // add human user.
			// human last may be nicer for tweet format.
		}
	}
	
		//before start game, seach in the database. if contain the information about the amount of chip of player and his/her bot, return it
	//if database doesnt contain such information, use default value.
	private static synchronized String getScreenNameIndatabase(String screenName){
		String database="resources/database.csv";
		BufferedReader br;
		String result="";
		ArrayList<String> tempFile=new ArrayList<String>();
		try{

			br=new BufferedReader(new FileReader(file));
			String line="";
			while((line=br.readLine())!=null){

				String[] tempSample=line.split(",");

				if(tempSample[0].equals(screenName)){
					result=line;
				}
				else{
					tempFile.add(line);
				}

			}
			br.close();
			BufferedWriter bw=new BufferedWriter(new FileWriter(file));

			for(int i=0;i<=tempFile.size()-1;i++){
				bw.append(tempFile.get(i));
				bw.newLine();
			}

			bw.close();

		}
		catch(Exception ex){
			System.err.println(ex);
		}

		return result;
	}
	
	
	//except human player is bankruptted, when a pockergame is eliminated, we call this function.(important)
	//the database contains the name and chipamount of each player in this table.
	//the format of each line in database.
	//first column is username
	//second is the remain number of player
	//than each pair is the name of computer player
	//the last two is the name of user and the chipamount of user
	private synchronized void WritingScreenNameIndatabase(String screenName){

		BufferedReader br;
		String result="";
		ArrayList<String> tempFile=new ArrayList<String>();
		try{


			BufferedWriter bw=new BufferedWriter(new FileWriter(file,true));
			String string="";
			string+=user.getScreenName();
			string+=","+this.size();
			for(int i=0;i<=this.size()-2;i++){
				string+=","+this.get(i).playerName;
				string+=","+this.get(i).getPlayerChipAmount();
			}
			string+=","+this.get(this.size()-1).getPlayerName();

			string+=","+this.get(this.size()-1).getPlayerChipAmount();
			bw.append("");
			bw.newLine();


			bw.close();




		}
		catch(Exception ex){
			System.err.println(ex);
		}




	}
	
	

	public User setUserFromTwitter(User usr) {
		return user = usr;
	}

	/*private void setNameFromTwitter() {
		userName = user.getName();
	}*/

	/*deals out cards for the next round/
	* */
	synchronized private void dealOutCards(boolean firstR) {
		if ( !firstR ) {
			for (PokerPlayer player : this) {
				player.getNewCardsForHand();
			}
		}
	}

	/*allows users to input their hand changes and betting options.
	* */
	private boolean currentRoundPlayersHandOptions(String TweetBody) {
		System.out.println("currentRoundPlayersHandOptions START");
		tweetStr = "\uD83C\uDFB4\n"; // flower deck emoji
		for (PokerPlayer player : this) { // players choose card discard options
			if (!player.isPlayersHandOptionsSent & player.isHuman) {
				System.out.println(player+"\t\t\tif (!player.isPlayersHandOptionsSent & player.isHuman) {\n START");
				player.sendPlayerHandOptions();
				player.isPlayersHandOptionsSent = true;
				System.out.println("currentRoundPlayersHandOptions END - \t\t\t\treturn FALSe;\n");
				return false;
			}
			else {
				if (!player.isPlayersHandOptionsSent & !player.isHuman) {
					player.playersHandOptions(TweetBody);
					player.isPlayersHandOptionsSent = true;
				}
				if (player.isHuman) {
					if( !player.playersHandOptions(TweetBody)) {
						tweetStr = "@" + user.getScreenName() + " Need some help? "+PlayerBot.faceTellGenerator();
						tweetStr += "\n yr hand is "+player.hand+"\n";
						tweetStr += "you can:\n";
						tweetStr += "Discard: eg: d"+player.hand.get(3)+player.hand.get(0)+"\n";
						tweetStr += "Auto discard: eg: a, A2\n";
						tweetStr += "Keep: eg: K\n";
						TwitterInterpreter.getInstance().postTweet(tweetStr);
						tweetStr = "";
						return false;
					}
					else
						hasCurrentRoundPlayersHandOptionsTweetedAndReplied = true;
				}
				System.out.println("currentRoundPlayersHandOptions END - \t\t\t\treturn true;\n");
			}
		}
		return true;
	}

	private boolean currentRoundPlayersBettingOptions(String TweetBody) { // flower deck emoji
		tweetStr = "\uD83C\uDFB0\n"; // slot machine emoji
		for (PokerPlayer player : this) { // players choose betting options
			if (!player.isPlayersBettingOptionsSent & player.isHuman) {
				player.sendPlayersBettingOptions();
				player.isPlayersBettingOptionsSent = true;
				return false;
			}
			else {
				if (!player.isPlayersBettingOptionsSent & !player.isHuman) {
					player.playersBettingOptions(TweetBody);
					player.isPlayersBettingOptionsSent = true;
				}
				if (player.isHuman) {
					if( !player.playersBettingOptions(TweetBody)) {
						tweetStr = "@" + user.getScreenName() + " Need some betting help? "+PlayerBot.faceTellGenerator();
						tweetStr += "\nyr hand is "+player.hand+"\n";
						tweetStr += "Stake: "+getCurrentRoundsStakeAmount()+" you can:\n";
						tweetStr += "Call eg: call, c";
						tweetStr += "raise stake: eg: r"+getCurrentRoundsStakeAmount()+"+ \n";
						tweetStr += "Fold eg: fold, f\n";
						TwitterInterpreter.getInstance().postTweet(tweetStr);
						tweetStr = "";
						return false;
					}
					else
						hasCurrentRoundPlayersBettingOptionsTweetedAndReplied = true;
				}
				//return true;
			}
		}
		return true;
	}

	synchronized private boolean payAnteFee(int AnteFee) {
		for (int i = 0; i < this.size(); i++) {
			System.out.println("************************ anteFee loop!!!!: "+this.get(i));
			this.get(i).payAnteFee(AnteFee, true, "");
		}
		return true;
	}


/*	*//*player will pay enter fee if unable then bankrupted
	* *//*
	synchronized private boolean payAnteFee(int AnteFee, boolean isFistRound, String TweetMsg) {
		//for (PokerPlayer player : this) {
		for (int i = 0; i < this.size(); i++) {
			System.out.println("************************REPLAY anteFee loop!!!!: "+this.get(i));

			if (!this.get(i).isPlayersPayAnteFeeOptionsSent & this.get(i).isHuman) {
				this.get(i).sendPayAnteFeeDialog(AnteFee);
				this.get(i).isPlayersPayAnteFeeOptionsSent = true;
				return false;
			}
			if (this.get(i).isPlayersPayAnteFeeOptionsSent & this.get(i).isHuman) {
				this.get(i).payAnteFee(AnteFee, isFistRound, TweetMsg);
				hasCurrentRoundPlayersAnteFeeOptionsTweetedAndReplied = true;
				return true;
			}
			if (!this.get(i).isPlayersPayAnteFeeOptionsSent & !this.get(i).isHuman) {
				this.get(i).payAnteFee(AnteFee, true, TweetMsg);
				this.get(i).isPlayersPayAnteFeeOptionsSent = true;
			}
		}
		return true;
	}*/

	/*removes pokerPlayer at that list location.
	* ~ checks if human player and if so gameOver = true;
	* */
	synchronized public boolean playerIsLeavingGame(PokerPlayer player, boolean isBankrupted) {
		if( player.isHuman ) {
			gameOver = true;
			if (isBankrupted) {
				System.out.println(player.getPlayerName() + " is bankrupt.... game over dude....");
				tweetStr +=player.getPlayerName() + " is bankrupt.... game over dude....";
				this.remove(player);
				return false;
			}
		}
		else {
			for (PlayingCard card : player.hand) {
				gameDeck.returnCard(card);
				return true;
			}
			this.remove(player);
			System.out.println(player.getPlayerName() + " is bankrupt!");
			tweetStr +=(player.getPlayerName() + " is bankrupt!");
			return true;
		}
		return true;
	}

	/*gets highest scored player and gives them the pot.
	* */
	synchronized private void getRoundWinner() {
		System.out.println("getRoundWinnerSTART**********curRoundPlayerList: " + curRoundPlayerList + "\n\n\n\n\n\n");

		PokerPlayer tempPlayer = calcPlayerHandScores();
		if(tempPlayer == null) {
			int tempHighScore = 0;
			for (PokerPlayer player : curRoundPlayerList) {
				if (tempHighScore < player.getCurrentHandScore()) { // re-calcs all players current scores.
					tempHighScore = player.getCurrentHandScore();
					tempPlayer = player;
				}
			}
		}
		tempPlayer.receivesStake(currentRoundsHeldStake);
		tempPlayer.totalRoundsWon += 1;
		System.out.println(tempPlayer.getPlayerName() + " is the winner!");
		tweetStr = tempPlayer.getPlayerName() + " won with a " + tempPlayer.hand.getBestHandTypeName() + "\n\n";
		System.out.println(tempPlayer.getPlayerName() + ": new Chips amount: " + tempPlayer.getPlayerChipAmount());
		System.out.println("ships now on table: " + getCurrentRoundsHeldStake());
		System.out.println("**********curRoundPlayerList: " + curRoundPlayerList + "\n\n\n\n\n\n");

	}
	/*Returns the pokerPlayer with the highest score.
	* */
	synchronized private PokerPlayer calcPlayerHandScores() {
		int tempHighScore = 0;
		PokerPlayer tempPlayer = null;
		for (PokerPlayer player : curRoundPlayerList) {
			if (tempHighScore < player.getCurrentHandScore()) { // re-calcs all players current scores.
				tempHighScore = player.getCurrentHandScore();
				tempPlayer = player;
			}
		}
		return tempPlayer;
	}

	/*if all players have lover than a pair. then re-deal.
	* */
	synchronized private boolean checkForBumDeck() {
		//System.out.println("checkForBumDeck   START");
		int tempHighScore = 0;
		for (PokerPlayer player : this) {
			System.out.println(player+": checkForBumDeck");
			if (tempHighScore < player.getCurrentHandScore()) { // re-calcs all players current scores.
				tempHighScore = player.getCurrentHandScore();
			}
		}
		if(tempHighScore < HandOfCards.PAIR_WEIGHT) {
			System.out.println("BUM DECK!!!!");
			for (PokerPlayer player : this) {
				player.returnHandToDeck();
			}
			gameDeck.getInstance().shuffle();

			dealOutCards(false); // deals out new cards.
			return true;
		}
		else
			//System.out.println("checkForBumDeck   END");
			return false;
	}

	/* removes @mentions from string.
	* */
	public String cleanUpInputTweet(String inputTweet) {
		inputTweet = inputTweet.replace("@"+user.getScreenName(), "");
		inputTweet = inputTweet.replace("@"+TwitterInterpreter.getInstance().getTwitterScreenName(), "");
		inputTweet = inputTweet.replace(" ", "");
		return inputTweet;
	}


	/*rests rounds data.
	* */
	synchronized private void resetRound() {
		//currentRoundsStakeAmount = 0;
		currentRoundsHeldStake = 0;
		curRoundPlayerList.clear();
		//System.out.println("**** Return cards to deck: "+gameDeck.getInstance().size());
		for (PokerPlayer player : this) {
			player.returnHandToDeck();
			player.isPlayersBettingOptionsSent = false;
			player.isPlayersHandOptionsSent = false;
			player.isPlayersPayAnteFeeOptionsSent = false;
		}
		//System.out.println("**** Return cards to deck END: "+gameDeck.getInstance().size());
		gameDeck.getInstance().shuffle();
		firstRound = false; // add option to human to exit game at the next ante pay.
		hasCurrentRoundPlayersHandOptionsTweetedAndReplied = false;
		hasCurrentRoundPlayersBettingOptionsTweetedAndReplied = false;
		hasCurrentRoundPlayersAnteFeeOptionsTweetedAndReplied = false;
		hasCurrentRoundBeenDealt = false;
		hasCurrentRoundPlayersAnteFeeOptionsTweeted = false;

	}

	/*called from pokerPlayer if
	* */
	synchronized public void addToCurRoundPlayerList(PokerPlayer temp) {
		curRoundPlayerList.add(temp);
	}

	/*called from pokerPlayer if folding from that round.
	* */
	synchronized public int curRoundPlayerFolds(PokerPlayer temp) {
		if(curRoundPlayerList.contains(temp)) {
			//System.out.println("curRoundPlayerList contain: "+temp.getPlayerName());
			curRoundPlayerList.remove(temp);
		}
		return 0;
	}

	/*Called when player chooses to rise stake. all pervious called players will have option
	* to call again to new price or fold.
	* */
	synchronized public void matchStakeIncrease(int stakeIncrease) {
		//System.out.println("matchStakeIncrease START");
		ArrayList<PokerPlayer> foldingList = new ArrayList<PokerPlayer>(); // players going to fold

		for (PokerPlayer object : curRoundPlayerList) {
			if( object.reRaiseStake(stakeIncrease) == false ) { // if player chooses fold add to list
				foldingList.add(object);
			}
		}
		for (PokerPlayer object : foldingList) {
			object.foldFromRound();
		}
	}

	/*returns bool statement the game continue to next round or not.
	* */
	public boolean isGameOver() {
		return gameOver;
	}

	/*returns int of current round.
	* */
	public int getRoundCount() {
		return roundCount;
	}

	/*returns int of current round's stake value for players
	* */
	synchronized public int getCurrentRoundsHeldStake() {
		return currentRoundsHeldStake;
	}

	/*pokerPlayers able to add their stake in conjunction with PokerPlayer.payCurrentStake()
	* */
	synchronized public void addToCurrentRoundsHeldStake(int addStake) {
		currentRoundsHeldStake += addStake;
	}


	/*Used in pokerPlayers when raising stake.
	* */
	synchronized public void setCurrentRoundsStakeAmount(int currentRoundsStakeAmount) {
		this.currentRoundsStakeAmount = currentRoundsStakeAmount;
	}

	/*returns int of total amount of betted stake of the current hand
		* */
	synchronized public int getCurrentRoundsStakeAmount() {
		return currentRoundsStakeAmount;
	}




	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.PokerGame.java!");
		User user = new User() {
			@Override
			public long getId() {
				return 0;
			}

			@Override
			public String getName() {
				return "getName";
			}

			@Override
			public String getScreenName() {
				return "human  ";
			}

			@Override
			public String getLocation() {
				return null;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public boolean isContributorsEnabled() {
				return false;
			}

			@Override
			public String getProfileImageURL() {
				return null;
			}

			@Override
			public String getBiggerProfileImageURL() {
				return null;
			}

			@Override
			public String getMiniProfileImageURL() {
				return null;
			}

			@Override
			public String getOriginalProfileImageURL() {
				return null;
			}

			@Override
			public String getProfileImageURLHttps() {
				return null;
			}

			@Override
			public String getBiggerProfileImageURLHttps() {
				return null;
			}

			@Override
			public String getMiniProfileImageURLHttps() {
				return null;
			}

			@Override
			public String getOriginalProfileImageURLHttps() {
				return null;
			}

			@Override
			public boolean isDefaultProfileImage() {
				return false;
			}

			@Override
			public String getURL() {
				return null;
			}

			@Override
			public boolean isProtected() {
				return false;
			}

			@Override
			public int getFollowersCount() {
				return 0;
			}

			@Override
			public Status getStatus() {
				return null;
			}

			@Override
			public String getProfileBackgroundColor() {
				return null;
			}

			@Override
			public String getProfileTextColor() {
				return null;
			}

			@Override
			public String getProfileLinkColor() {
				return null;
			}

			@Override
			public String getProfileSidebarFillColor() {
				return null;
			}

			@Override
			public String getProfileSidebarBorderColor() {
				return null;
			}

			@Override
			public boolean isProfileUseBackgroundImage() {
				return false;
			}

			@Override
			public boolean isDefaultProfile() {
				return false;
			}

			@Override
			public boolean isShowAllInlineMedia() {
				return false;
			}

			@Override
			public int getFriendsCount() {
				return 0;
			}

			@Override
			public Date getCreatedAt() {
				return null;
			}

			@Override
			public int getFavouritesCount() {
				return 0;
			}

			@Override
			public int getUtcOffset() {
				return 0;
			}

			@Override
			public String getTimeZone() {
				return null;
			}

			@Override
			public String getProfileBackgroundImageURL() {
				return null;
			}

			@Override
			public String getProfileBackgroundImageUrlHttps() {
				return null;
			}

			@Override
			public String getProfileBannerURL() {
				return null;
			}

			@Override
			public String getProfileBannerRetinaURL() {
				return null;
			}

			@Override
			public String getProfileBannerIPadURL() {
				return null;
			}

			@Override
			public String getProfileBannerIPadRetinaURL() {
				return null;
			}

			@Override
			public String getProfileBannerMobileURL() {
				return null;
			}

			@Override
			public String getProfileBannerMobileRetinaURL() {
				return null;
			}

			@Override
			public boolean isProfileBackgroundTiled() {
				return false;
			}

			@Override
			public String getLang() {
				return null;
			}

			@Override
			public int getStatusesCount() {
				return 0;
			}

			@Override
			public boolean isGeoEnabled() {
				return false;
			}

			@Override
			public boolean isVerified() {
				return false;
			}

			@Override
			public boolean isTranslator() {
				return false;
			}

			@Override
			public int getListedCount() {
				return 0;
			}

			@Override
			public boolean isFollowRequestSent() {
				return false;
			}

			@Override
			public URLEntity[] getDescriptionURLEntities() {
				return new URLEntity[0];
			}

			@Override
			public URLEntity getURLEntity() {
				return null;
			}

			@Override
			public String[] getWithheldInCountries() {
				return new String[0];
			}

			@Override
			public int compareTo(User o) {
				return 0;
			}

			@Override
			public RateLimitStatus getRateLimitStatus() {
				return null;
			}

			@Override
			public int getAccessLevel() {
				return 0;
			}
		};
		// testing deck instance
		PokerGame pg = new PokerGame();
		pg.setUserFromTwitter(user);
		//pg.setNameFromTwitter();
		//System.out.print(pg.userName);
		for (PokerPlayer object : pg) {
			//object.hand.generateHandType();
			//System.out.println(object.toString()+"   "+object.hand.getBestHandTypeName());
			System.out.println(object.toString());
			System.out.println(object.getPlayerChipAmount());

		}
		System.out.println(pg.gameDeck.getInstance().size());
		System.out.println(pg.gameDeck.getInstance().toString());

		System.out.println("PokerGame: "+pg.toString());

		System.out.println("getCurrentRoundsHeldStake: "+pg.getCurrentRoundsHeldStake());
		System.out.println("curRoundPlayerList: "+pg.curRoundPlayerList);


	}
}
