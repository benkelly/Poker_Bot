package poker;


import twitter4j.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */
public class PokerPlayer {

	private DeckOfCards gameDeck;
	PokerGame pokerGame;

	public HandOfCards hand = new HandOfCards();

	// basic stats
	String playerName = "";
	public boolean isHuman;
	public boolean isPlayersHandOptionsSent = false;
	public boolean isPlayersBettingOptionsSent = false;
	public boolean isPlayersPayAnteFeeOptionsSent = false;
	int playerChipAmount = 0;
	int currentStakePaid = 0;

	public int currentHandScore = 0;
	public int totalDiscardCount = 0;
	public int totalTakeCardCount = 0;
	public int totalRoundsWon = 0;
	public int totalRoundsPlayed = 0;


	public PokerPlayer(String name, PokerGame game, DeckOfCards deck, int chips, boolean human) {
		playerName = name;
		pokerGame = game;
		gameDeck = deck;
		playerChipAmount = chips;
		isHuman = human;

	}

	/* prints  in Name: [3S, 6H, 6D, 7D, 9H]... format
	* */
	public String toString() {
		return playerName+ ": " + hand.toString()+"  Chips: "+getPlayerChipAmount();
	}


	/*private method creates first hand from deck.
	* */
	synchronized private void getInitialHand() {
		for (int i = 0; i < pokerGame.MAX_HAND; i++) {
			hand.add(gameDeck.dealNext());
		}

	}


	public void sendPlayerHandOptions() {
		hand.generateHandType();
		hand.getGameValue();
		getHandsDiscardProbability();

		System.out.println(this.getPlayerName() + ": your current hand is: " + hand
				+ " HandType: " + hand.getBestHandTypeName());

		pokerGame.tweetStr += "@"+pokerGame.user.getScreenName() + " your hand's in \uD83D\uDDBC\n"+"\n";
		pokerGame.tweetStr += "auto discard, discard, keep, help?";

		System.out.println(this.getPlayerName() +": *****char count: "+pokerGame.tweetStr.length());
		System.out.println(this.getPlayerName() +": TWEETSTR: "+pokerGame.tweetStr);

		tweetPlayerVisualHand(hand, playerChipAmount, pokerGame.tweetStr, pokerGame.currentFromStatus );
	}


	/*public method, used in PokerGame to allow player to chose its hand option cmds
	* */
	public boolean playersHandOptions(String inputStr) {
		hand.generateHandType();
		hand.getGameValue();
		getHandsDiscardProbability();

		inputStr = pokerGame.cleanUpInputTweet(inputStr);
		inputStr = inputStr.replace("10", "t"); // as 10 cards are t in our format :)
		//inputStr = getConsoleInput();
		System.out.println("######:playersHandOptions: inputStr: "+inputStr);

		//auto discard cmd.
		if (inputStr.toLowerCase().contains("auto") | inputStr.toLowerCase().contains("a")) {
			if (inputStr.matches(".*\\d+.*")) { // if str has number
				String isStrInt = extractIntFromString(inputStr);
				int discardAmount = Integer.parseInt(isStrInt);
				if (discardAmount < pokerGame.MAX_DISCARD && discardAmount > 0) {
					discard(discardAmount);
				} else
					discard();
			} else {
				discard();
			}
			getNewCardsForHand();
			System.out.println(getPlayerName()+": auto discard");
			return true;
		}
		// user discard cmd.
		if (inputStr.toUpperCase().contains(hand.get(0).toString().toUpperCase())
				| inputStr.toUpperCase().contains(hand.get(1).toString().toUpperCase())
				| inputStr.toUpperCase().contains(hand.get(2).toString().toUpperCase())
				| inputStr.toUpperCase().contains(hand.get(3).toString().toUpperCase())
				| inputStr.toUpperCase().contains(hand.get(4).toString().toUpperCase())
				| inputStr.toLowerCase().startsWith("d")
				| inputStr.toLowerCase().contains("discard")) {
			System.out.println(hand.get(0).toString().toUpperCase());
			int discardCount = 0;
			ArrayList<PlayingCard> discardList = new ArrayList<>();

			for (PlayingCard card : hand) {
				if (inputStr.toUpperCase().contains(card.toString().toUpperCase())) {
					if (discardCount < pokerGame.MAX_DISCARD) {
						discardList.add(card);
						System.out.println(card + ": added to removedList");
						discardCount++;
					}
				}
			}
			for (PlayingCard card : discardList) {
				hand.remove(card);
				System.out.println(card + ": removed");
			}
			getNewCardsForHand();
			System.out.println(this.getPlayerName() + ": your new hand is: " + hand
					+ " HandType: " + hand.getBestHandTypeName());
			return true;
		}
		// keep cmd
		if (inputStr.toLowerCase().contains("keep") | inputStr.toLowerCase().startsWith("k")) {
			System.out.println(getPlayerName()+": keep");
			return true;
		}
		else
			return false;
	}

	public void sendPlayersBettingOptions() {
		hand.generateHandType();
		hand.getGameValue();
		getHandsDiscardProbability();

		System.out.println(this.getPlayerName() + ": your current hand is: " + hand
				+ " HandType: " + hand.getBestHandTypeName());

		pokerGame.tweetStr += "@"+pokerGame.user.getScreenName() + " your hand's in \uD83D\uDDBC\n"+"\n";
		pokerGame.tweetStr += "call, raise, fold, help?";

		System.out.println(this.getPlayerName() +": *****char count: "+pokerGame.tweetStr.length());
		System.out.println(this.getPlayerName() +": TWEETSTR: "+pokerGame.tweetStr);

		tweetPlayerVisualHand(hand, playerChipAmount, pokerGame.tweetStr, pokerGame.currentFromStatus);
	}



	/*public method, used in PokerGame to allow player to chose its betting cmds
	* */
	public boolean playersBettingOptions(String inputStr) {
		inputStr = pokerGame.cleanUpInputTweet(inputStr);
		System.out.println("###### inputStr: "+inputStr);
		//System.out.println(this.getPlayerName() + ": your current hand is: " + hand + "");
		//inputStr = getConsoleInput();
		if (inputStr.toLowerCase().contains("pay") | inputStr.toLowerCase().equals("ps")
				| inputStr.toLowerCase().equals("pay stake") | inputStr.toLowerCase().contains("call")
				| inputStr.toLowerCase().startsWith("c")) {
			call();
			return true;
		}

		if (inputStr.toLowerCase().contains("increase") | inputStr.toLowerCase().contains("i")
				| inputStr.toLowerCase().startsWith("r") | inputStr.toLowerCase().startsWith("rise")) {
			System.out.println(this.getPlayerName() + ": *********** increase triggered");
			while (true){

				if (inputStr.matches(".*\\d+.*")) { // if str has number
					String isStrInt = extractIntFromString(inputStr);
					raise(Integer.parseInt(isStrInt));
					//System.out.println(Integer.parseInt(isStrInt));
					System.out.println(this.getPlayerName() + ": *********** raise() called");
					return true;
				}
				else{
					System.out.println(this.getPlayerName() + ": please re-enter the amount: ");
					inputStr = getConsoleInput();
				}
			}
		}
		if (inputStr.toLowerCase().contains("fold") | inputStr.toLowerCase().startsWith("f")) {
			fold();
			System.out.println(this.getPlayerName() + ": *********** folded");
			return true;
		}
		return false;
	}

	/*public method collects new card from deck after discarding.
	* */
	synchronized public void getNewCardsForHand() {
		if(hand.size() < pokerGame.MAX_HAND) {
			for (int i = hand.size(); i < pokerGame.MAX_HAND; i++) {
				hand.add(gameDeck.dealNext());
				totalTakeCardCount++;
			}
		}
	}
	/*private method gets and returns hands current score and updates currentHandScore
	* */
	synchronized private int getCurrentHandInfo() {
		hand.generateHandType();
		currentHandScore = hand.getGameValue();
		return currentHandScore;
	}

	/*auto discards cards based on their discard getDiscardProbability
	* */
	synchronized private int discard() {
		return discard(pokerGame.MAX_DISCARD);
	}
	synchronized int discard(int discardAmount) {
		if(discardAmount > pokerGame.MAX_DISCARD) { discardAmount = pokerGame.MAX_DISCARD; }
		int discardCount = 0;
		List<probabilityScoreList> scoreList = new ArrayList<>();

		for (int i = 0; i < pokerGame.MAX_HAND; i++) { scoreList.add(new probabilityScoreList(i, hand.getDiscardProbability(i))); }
		// sorts scores in descending order
		sortProbabilityScoreDescending(scoreList);
		List<probabilityScoreList> removeList = new ArrayList<>();
		for (probabilityScoreList object : scoreList) {
			if (discardCount >= discardAmount ) { break; }
			if (object.getCardProbabilityScore() > 0) {
				gameDeck.returnCard(hand.get(object.cardLocation));
				removeList.add(object);
				discardCount++;
				totalDiscardCount++;
			}
		}
		// to avoid index out of bounds, sort removeList into descending order and remove.
		sortLocationDescending(removeList);

		for (int i = 0; i < removeList.size() ; i++) {
			if (removeList.get(i) != null) {
				hand.removeCard(removeList.get(i).cardLocation);
			}
		}
		return discardCount;
	}

	/*called my PokerGame when resetting round
	* */
	synchronized public void returnHandToDeck() {
		for (PlayingCard card : hand) {
			gameDeck.returnCard(card);
		}
		hand.clear();
	}

	/*Will sort List<probabilityScoreList> object.cardLocation in descending order
	* */
	private void sortLocationDescending(List<probabilityScoreList> currentList) {
		Collections.sort(currentList, new Comparator<probabilityScoreList>() {
			@Override
			public int compare(probabilityScoreList score1, probabilityScoreList score2) {
				return Float.compare(score2.getCardLocation(), score1.getCardLocation());
			}
		});
	}

	/*Will sort List<probabilityScoreList> object.cardProbabilityScore in descending order
	* */
	private void sortProbabilityScoreDescending(List<probabilityScoreList> currentList) {
		Collections.sort(currentList, new Comparator<probabilityScoreList>() {
			@Override
			public int compare(probabilityScoreList score1, probabilityScoreList score2) {
				return Float.compare(score2.getCardProbabilityScore(), score1.getCardProbabilityScore());
			}
		});
	}

	/*Object used in lists to sort cards scores.
	* */
	private class probabilityScoreList {
		int cardLocation;
		int cardProbabilityScore;

		probabilityScoreList(int location, int probabilityScore) {
			cardLocation = location;
			cardProbabilityScore = probabilityScore;
		}

		private int getCardLocation() {
			return cardLocation;
		}

		private int getCardProbabilityScore() {
			return cardProbabilityScore;
		}

		public String toString() {
			return "[" + this.cardLocation + ", " + this.cardProbabilityScore + "]";
		}
	}


	public String getPlayerName() {
		return playerName;
	}

	public int getPlayerChipAmount() {
		return playerChipAmount;
	}

	synchronized public int getCurrentHandScore() {
		hand.generateHandType();
		return hand.getGameValue();
	}

	/*player will call the current stake to stay in game.
	* */
	synchronized public void call() {

		if(playerChipAmount >= pokerGame.getCurrentRoundsStakeAmount()) {
			playerChipAmount -= pokerGame.getCurrentRoundsStakeAmount();
			pokerGame.addToCurrentRoundsHeldStake(pokerGame.getCurrentRoundsStakeAmount());
			currentStakePaid = pokerGame.getCurrentRoundsHeldStake();
			//paidStake = true;
			pokerGame.addToCurRoundPlayerList(this); // add self to cur hand/round list
			System.out.println(this.getPlayerName()+": call paid");

		}
		else
			System.out.println(this.getPlayerName()+": unable to call...");
	}

	/*command to increase stake to stated amount
	*
	* */
	synchronized public boolean raise(int amount) {
		if(playerChipAmount >= amount) {
			if (amount > pokerGame.getCurrentRoundsStakeAmount()) {
				int stakeDiff = amount - pokerGame.getCurrentRoundsStakeAmount();
				playerChipAmount -= amount;
				pokerGame.addToCurrentRoundsHeldStake(amount);
				pokerGame.setCurrentRoundsStakeAmount(amount);

				pokerGame.matchStakeIncrease(stakeDiff); // asks all others who paid to match or fold

				currentStakePaid = amount;
				pokerGame.addToCurRoundPlayerList(this); // add self to cur hand/round list

				System.out.println(this.getPlayerName()+" raise paid"); // for testing
				return true;
			}
		}
		else
			fold();
			return false;
	}

	/* previous players who betted will be asked to match the new stake or fold
	* */
	synchronized public boolean reRaiseStake(int stakeIncrease) {

		System.out.println(playerName + ": do you want to re-raise of " + stakeIncrease + " (y/n)?");
		String inputStr = getConsoleInput();

		while (true) {
			if (inputStr.toLowerCase().startsWith("y")) {
				payReRaiseStake(stakeIncrease);
			}
			if (inputStr.toLowerCase().startsWith("n")) {
				return false;
			}
		}
	}

	/*called if user decides to pay new raise amount.
	* */
	synchronized public boolean payReRaiseStake(int stakeIncrease) {
		if (playerChipAmount >= stakeIncrease) {
			pokerGame.addToCurrentRoundsHeldStake(stakeIncrease);
			playerChipAmount -= stakeIncrease;
			currentStakePaid = pokerGame.getCurrentRoundsHeldStake();
			return true;
		}
		else
			return false;
	}

	/*Calls fold from round in PokerGame.
	* */
	synchronized public void fold() {
		pokerGame.curRoundPlayerFolds(this);
		System.out.println(playerName+": has folded.");
	}

	/*get keyboard input
	* */
	private String getConsoleInput() {
		String str= "";
		try{
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			str = bufferRead.readLine();
			System.out.println(str);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return str;
	}

	public void sendPayAnteFeeDialog(int anteFee) {
		System.out.println(playerName + ": do you want to play another round (y/n)?");
		pokerGame.tweetStr += "Ante: "+anteFee+"\n";
		pokerGame.tweetStr += "@"+pokerGame.user.getScreenName() + " do you want to play another round (y/n)?";
		tweetPlayer(pokerGame.tweetStr);
	}


	/*PokerGame will call this at beginning of every round.
	* - if human player & not firstround
	* */
	public boolean payAnteFee(int anteFee, boolean noUserInputForRound, String inputStr) {
		//String inputStr = "";
		inputStr = pokerGame.cleanUpInputTweet(inputStr);

		if ( !noUserInputForRound ) {
				if( !inputStr.toLowerCase().startsWith("y") | !inputStr.toLowerCase().startsWith("n")) {
					pokerGame.tweetStr = "Ante: "+anteFee+"\n";
					pokerGame.tweetStr += "@"+pokerGame.user.getScreenName() + " do you want to play another round (y/n)?";
					tweetPlayer(pokerGame.tweetStr);
					return false;
				}
		}
		else {
			inputStr = "y";
		}

		if (inputStr.toLowerCase().startsWith("y")) {
			if (playerChipAmount >= anteFee) {
				pokerGame.addToCurrentRoundsHeldStake(anteFee);
				playerChipAmount -= anteFee;
				totalRoundsPlayed++;
				return true;
			} else {
				pokerGame.playerIsLeavingGame(this, true);
				return false;
			}
		}
		if (inputStr.toLowerCase().startsWith("n")) {
			pokerGame.playerIsLeavingGame(this, false);
			return true;

		}
		return false;
	}


	/*when player wins round, PokerGame will call this.
	* */
	public void receivesStake(int amount) {
		playerChipAmount += amount;
		totalRoundsWon++;
	}

	/*Calcs Probability for all 5 cards in players hand.
	* */
	private void getHandsDiscardProbability() {
		for (int i = 0; i < pokerGame.MAX_HAND; i++) {
			hand.getDiscardProbability(i);
		}
	}

	/*Collects number from String.
	* */
	public static String extractIntFromString(final String str) {
		if(str == null || str.isEmpty()) { return ""; }
		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for(char cur: str.toCharArray()) {
			if(Character.isDigit(cur)) {
				sb.append(cur);
				found = true;
			}
			else if(found) {
				break;
			}
		}
		return sb.toString();
	}

	/*tweets player with in conversation reply with their currentt card hand
	* */
	private void tweetPlayerVisualHand(HandOfCards hnd, int chp, String tweetStr, Status fromStatus) {
		VisualHand.TweetVisualHand(hnd, chp, tweetStr, fromStatus);
		tweetStr = "";
	}

	/*tweets player with in conversation reply
	* */
	private void tweetPlayer(String tweetStr) {
		try {
			TwitterInterpreter.getInstance().postTweet(tweetStr, pokerGame.currentFromStatus);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tweetStr = "";
	}

	/*set players PlayerChipAmount used in restoring data
	* */
	public void setPlayerChipAmount(int amount) {
		playerChipAmount = amount;
	}

	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.PokerPlayer.java!");
	}
}