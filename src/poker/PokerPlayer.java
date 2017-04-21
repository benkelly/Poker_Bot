package poker;


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
	int playerChipAmount = 0;

	int currentStakePaid = 0;

	public int currentHandScore = 0;
	public int totalDiscardCount = 0;
	public int totalTakeCardCount = 0;
	public int totalRoundsWon = 0;
	public int totalRoundsPlayed = 0;

	// current round variables
	//private boolean paidStake = false;

	public PokerPlayer(String name, PokerGame game, DeckOfCards deck, int chips, boolean human) {
		playerName = name;
		pokerGame = game;
		gameDeck = deck;
		playerChipAmount = chips;
		isHuman = human;

		getInitialHand();
		getCurrentHandInfo();
	}

	/* prints  in Name: [3S, 6H, 6D, 7D, 9H]... format
	* */
	public String toString() {
		return playerName+ ": " + hand.toString();
	}


	/*private method creates first hand from deck.
	* */
	synchronized private void getInitialHand() {
		for (int i = 0; i < pokerGame.MAX_HAND; i++) {
			hand.add(gameDeck.getInstance().dealNext());
		}

	}

	/*public method, used in PokerGame to allow player to chose its hand option cmds
	* */
	public boolean playersHandOptions() {
		hand.generateHandType();
		hand.getGameValue();
		getHandsDiscardProbability();

		System.out.println(this.getPlayerName() + ": your current hand is: " + hand
				+ " HandType: " + hand.getBestHandTypeName());
		String inputStr = getConsoleInput();

		//auto discard cmd.
		if (inputStr.toLowerCase().contains("auto discard") | inputStr.toLowerCase().contains("a")
				| inputStr.toLowerCase().contains("auto")) {
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
			System.out.println(this.getPlayerName() + ": it worked!!!!!!!!!!!!!!!!!!!!!");
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
			return true;
		}
		return false;
	}
	/*public method, used in PokerGame to allow player to chose its betting cmds
	* */
	public boolean playersBettingOptions() {
		System.out.println(this.getPlayerName() + ": your current hand is: " + hand + "");
		String inputStr = getConsoleInput();
		if (inputStr.toLowerCase().contains("pay") | inputStr.toLowerCase().equals("ps")
				| inputStr.toLowerCase().equals("pay stake") | inputStr.toLowerCase().contains("call")
				| inputStr.toLowerCase().startsWith("c")) {
			payCurrentStake();
			return true;
		}

		if (inputStr.toLowerCase().contains("increase") | inputStr.toLowerCase().contains("i")
				| inputStr.toLowerCase().startsWith("r") | inputStr.toLowerCase().startsWith("rise")) {
			System.out.println(this.getPlayerName() + ": *********** increase triggered");
			while (true){

				if (inputStr.matches(".*\\d+.*")) { // if str has number
					String isStrInt = extractIntFromString(inputStr);
					increaseStake(Integer.parseInt(isStrInt));
					//System.out.println(Integer.parseInt(isStrInt));
					System.out.println(this.getPlayerName() + ": *********** increaseStake() called");
					return true;
				}
				else{
					System.out.println(this.getPlayerName() + ": please re-enter the amount: ");
					inputStr = getConsoleInput();
				}
			}
		}
		if (inputStr.toLowerCase().contains("fold") | inputStr.toLowerCase().startsWith("f")) {
			foldFromRound();
			return true;
		}
		return false;
	}

	/*public method collects new card from deck after discarding.
	* */
	synchronized public void getNewCardsForHand() {
		if(hand.size() < pokerGame.MAX_HAND) {
			for (int i = hand.size(); i < pokerGame.MAX_HAND; i++) {
				hand.add(gameDeck.getInstance().dealNext());
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
				gameDeck.getInstance().returnCard(hand.get(object.cardLocation));
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

	synchronized public void returnHandToDeck() {
		//System.out.println(getPlayerName()+": returnHandToDeck: "+hand);
		for (PlayingCard card : hand) {
			gameDeck.getInstance().returnCard(card);
		}
		hand.clear();
		//System.out.println(getPlayerName()+": returnHandToDeck END ");
		//System.out.println(getPlayerName()+": returnHandToDeck: "+hand);
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
	synchronized public void payCurrentStake() {
		//System.out.println("getCurrentRoundsStakeAmount: "+pokerGame.getCurrentRoundsStakeAmount()+"");
		//System.out.println("getCurrentRoundsHeldStake: "+pokerGame.getCurrentRoundsHeldStake()+"");

		if(playerChipAmount >= pokerGame.getCurrentRoundsStakeAmount()) {
			playerChipAmount -= pokerGame.getCurrentRoundsStakeAmount();
			pokerGame.addToCurrentRoundsHeldStake(pokerGame.getCurrentRoundsStakeAmount());
			currentStakePaid = pokerGame.getCurrentRoundsHeldStake();
			//paidStake = true;
			pokerGame.addToCurRoundPlayerList(this); // add self to cur hand/round list
			System.out.println(this.getPlayerName()+": payCurrentStake paid");

		}
		else
			System.out.println(this.getPlayerName()+": unable to payCurrentStake...");
	}

	/*command to increase stake to stated amount
	*
	* */
	synchronized public void increaseStake(int amount) {
		if(playerChipAmount >= amount) {
			if (amount > pokerGame.getCurrentRoundsStakeAmount()) {
				int stakeDiff = amount - pokerGame.getCurrentRoundsStakeAmount();
				playerChipAmount -= amount;
				pokerGame.addToCurrentRoundsHeldStake(amount);
				pokerGame.setCurrentRoundsStakeAmount(amount);

				pokerGame.matchStakeIncrease(stakeDiff); // asks all others who paid to match or fold

				currentStakePaid = amount;
				//paidStake = true;
				pokerGame.addToCurRoundPlayerList(this); // add self to cur hand/round list

				System.out.println(this.getPlayerName()+" increaseStake paid"); // for testing

			}
		}
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
	synchronized public void foldFromRound() {
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

	/*PokerGame will call this at beginning of every round.
	* - if player
	* */
	public void payAnteFee(int anteFee, boolean noUserInputForRound) {
		String inputStr = "";
		if ( !noUserInputForRound ) {
			boolean inputLoop = true;
			while ( inputLoop ) {
				System.out.println(playerName + ": do you want to play another round (y/n)?");
				inputStr = getConsoleInput();
				if(inputStr.toLowerCase().startsWith("y") | inputStr.toLowerCase().startsWith("n")) {
					inputLoop = false;
				}
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
			} else {
				pokerGame.playerIsLeavingGame(this, true);
			}
		}
		if (inputStr.toLowerCase().startsWith("n")) {
			pokerGame.playerIsLeavingGame(this, false);
		}
	}



	/*when player wins round, PokerGame will call this.
	* */
	public void receivesStake(int amount) {
		playerChipAmount += amount;
		totalRoundsWon++;
	}


/*	public boolean isPaidStake() {
		return paidStake;
	}

	public void resetPaidStake() {
		paidStake = false;
	}*/

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


	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.PokerPlayer.java!");
		//System.out.println("deck: "+gameDeck);

		ArrayList<PokerPlayer> playerList = new ArrayList<PokerPlayer>();
		for (int j = 0; j < 10; j++) {
			playerList.add(new PokerPlayer("player:"+j,PokerGame.getInstance(),  DeckOfCards.getInstance(), 3000, true));
		}
		int playNumber = 1;
		for (PokerPlayer object : playerList) {
			System.out.println("player" + playNumber + ": " + object + "\t" + object.hand.getBestHandTypeName()
					+ "\t\tScore: " + object.hand.getGameValue() + "\tprob: " + object.hand.getDiscardProbability(0) + ", "
					+ object.hand.getDiscardProbability(1) + ", " + object.hand.getDiscardProbability(2) + ", "
					+ object.hand.getDiscardProbability(3) + ", " + object.hand.getDiscardProbability(4));
			playNumber++;
		}
		System.out.println("DeckOfCards: "+DeckOfCards.getInstance().size());

		for (int i = 0; i < playerList.size(); i++) {
			playerList.get(i).discard(PokerGame.getInstance().MAX_DISCARD);
			//playerList.get(i).getNewCardsForHand();
		}

		playNumber = 1;
		for (PokerPlayer object : playerList) {
			System.out.println("player" + playNumber + ": " + object + "\t");
			playNumber++;
		}
		System.out.println("DeckOfCards: "+DeckOfCards.getInstance().size());
	}
}