package poker;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */
public class PokerPlayer {

	private static final int MAX_HAND = 5;
	private static final int MAX_DISCARD = 3;

	private DeckOfCards gameDeck;
	private PokerGame pokerGame;

	public HandOfCards hand = new HandOfCards();

	// basic stats
	String playerName = "";
	private int playerChipAmount = 0;

	private int currentStakePaid = 0;

	public int currentHandScore = 0;
	public int totalDiscardCount = 0;
	public int totalTakeCardCount = 0;
	public int totalRoundsWon = 0;
	public int totalRoundsPlayed = 0;

	// current round variables
	private boolean paidStake = false;

	public PokerPlayer(String name,PokerGame game, DeckOfCards deck,  int chips) {
		playerName = name;
		playerChipAmount = chips;
		pokerGame = game;
		gameDeck = deck;


		getInitialHand();
		getCurrentHandInfo();
	}

	/* prints  in [3S, 6H, 6D, 7D, 9H]... format
	* */
	public String toString() {
		return hand.toString();
	}


	/*private method creates first hand from deck.
	* */
	synchronized private void getInitialHand() {
		for (int i = 0; i < MAX_HAND; i++) {
			hand.add(gameDeck.getInstance().dealNext());
		}

	}

	public void playersHandOptions() {

		hand.generateHandType();
		hand.getGameValue();
		getHandsDiscardProbability();

		System.out.println(this.getPlayerName()+": your current hand is: "+hand
				+" HandType: "+hand.getBestHandTypeName());
		String inputStr = getConsoleInput();

		if(inputStr.toLowerCase().contains("auto discard") | inputStr.toLowerCase().contains("a")
				| inputStr.toLowerCase().contains("auto") ) {
			if(inputStr.matches(".*\\d+.*")) { // if str has number
				String isStrInt = extractIntFromString(inputStr);
				int discardAmount = Integer.parseInt(isStrInt);
				if (discardAmount < MAX_DISCARD && discardAmount > 0) {
					discard(discardAmount);
				}
				else
					discard();
			}
			else {
				discard();
			}
			getNewCardsForHand();
		}


		System.out.println(this.getPlayerName()+": your current hand is: "+hand+" HandType: "
				+hand.getBestHandTypeName());
	}




	public boolean playersBettingOptions() {
		System.out.println(this.getPlayerName() + ": your current hand is: " + hand + "");
		String inputStr = getConsoleInput();
		if (inputStr.toLowerCase().equals("paystake") | inputStr.toLowerCase().equals("ps")
				| inputStr.toLowerCase().equals("pay stake")) {
			payCurrentStake();
			return true;
		}

		if (inputStr.toLowerCase().contains("increase") | inputStr.toLowerCase().contains("i")
				| inputStr.toLowerCase().contains("r") | inputStr.toLowerCase().contains("rise")) {
			if (inputStr.matches(".*\\d+.*")) { // if str has number
				String isStrInt = extractIntFromString(inputStr);
				increaseStake(Integer.parseInt(isStrInt));
				//System.out.println(Integer.parseInt(isStrInt));

			}
			return true;
		}

		return false;
	}



	/*private method collects new card from deck after discarding.
	* */
	synchronized public void getNewCardsForHand() {
		if(hand.size() < MAX_HAND) {
			for (int i = hand.size(); i < MAX_HAND; i++) {
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
	synchronized public int discard() {
		return discard(MAX_DISCARD);
	}
	synchronized public int discard(int discardAmount) {
		if(discardAmount > MAX_DISCARD) { discardAmount = MAX_DISCARD; }
		int discardCount = 0;
		List<probabilityScoreList> scoreList = new ArrayList<>();

		for (int i = 0; i < MAX_HAND; i++) { scoreList.add(new probabilityScoreList(i, hand.getDiscardProbability(i))); }
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

	public int getCurrentHandScore() {
		hand.generateHandType();
		return hand.getGameValue();
	}

	synchronized public void payCurrentStake() {
		//System.out.println("getCurrentRoundsStakeAmount: "+pokerGame.getCurrentRoundsStakeAmount()+"");
		//System.out.println("getCurrentRoundsHeldStake: "+pokerGame.getCurrentRoundsHeldStake()+"");

		if(playerChipAmount >= pokerGame.getCurrentRoundsStakeAmount()) {
			playerChipAmount -= pokerGame.getCurrentRoundsStakeAmount();
			pokerGame.addToCurrentRoundsHeldStake(pokerGame.getCurrentRoundsStakeAmount());
			currentStakePaid = pokerGame.getCurrentRoundsHeldStake();
			paidStake = true;
			pokerGame.addToCurRoundPlayerList(this); // add self to cur hand/round list
			System.out.println(this.getPlayerName()+"payCurrentStake paid");

		}
		System.out.println(this.getPlayerName()+"unable to payCurrentStake...");
	}

	/*command to increase stake to stated amount
	*
	* */
	synchronized public void increaseStake(int amount) {
		if(playerChipAmount >= amount) {
			if (amount > pokerGame.getCurrentRoundsHeldStake()) {
				int stakeDiff = amount - pokerGame.getCurrentRoundsHeldStake();
				playerChipAmount -= amount;
				pokerGame.addToCurrentRoundsHeldStake(amount);
				pokerGame.setCurrentRoundsStakeAmount(amount);

				//System.out.println("before matchStakeIncrease called");
				pokerGame.matchStakeIncrease(stakeDiff); // asks all others who paid to match or fold
				//System.out.println("after matchStakeIncrease called");



				currentStakePaid = amount;
				paidStake = true;
				pokerGame.addToCurRoundPlayerList(this); // add self to cur hand/round list

				System.out.println(this.getPlayerName()+"increaseStake paid");

			}
		}
	}

	/* previous players who betted will be asked to match the new stake or fold
	* */
	synchronized public void reRaiseStake(int stakeIncrease) {

		System.out.println("do you want to re-raise of "+stakeIncrease+ " (y/n)?");
		String inputStr= getConsoleInput();


		if(inputStr.toLowerCase() == "y" | inputStr.toLowerCase() =="ok" |inputStr.toLowerCase() == "yes") {
			if(playerChipAmount >= stakeIncrease) {
				pokerGame.addToCurrentRoundsHeldStake(stakeIncrease);
				playerChipAmount -= stakeIncrease;
				currentStakePaid = pokerGame.getCurrentRoundsHeldStake();
			}
			else{
				pokerGame.curRoundPlayerFolds(this);
			}
		}
		if(inputStr.toLowerCase() == "n" | inputStr.toLowerCase() == "nah" | inputStr.toLowerCase() == "no") {
			pokerGame.curRoundPlayerFolds(this);
		}
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

	public void payAnteFee(int anteFee) {
		if(playerChipAmount >= anteFee) {
			pokerGame.addToCurrentRoundsHeldStake(anteFee);
			playerChipAmount -= anteFee;
			totalRoundsPlayed++;
		}
		else {
			pokerGame.playerIsBankrupted(this);
		}
	}




	public void receivesStake(int amount) {
		playerChipAmount += amount;
		totalRoundsWon++;
	}


	public boolean isPaidStake() {
		return paidStake;
	}

	public void resetPaidStake() {
		paidStake = false;
	}


	private void getHandsDiscardProbability() {
		for (int i = 0; i < MAX_HAND; i++) {
			hand.getDiscardProbability(i);
		}
	}

	public static String extractIntFromString(final String str) {

		if(str == null || str.isEmpty()) return "";

		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for(char c : str.toCharArray()){
			if(Character.isDigit(c)){
				sb.append(c);
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
			playerList.add(new PokerPlayer("player:"+j,PokerGame.getInstance(),  DeckOfCards.getInstance(), 3000));
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
			playerList.get(i).discard(MAX_DISCARD);
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