package poker;

import com.sun.xml.internal.bind.v2.TODO;

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

	private int MAX_HAND = 5;
	private int MAX_DISCARD = 3;

	public HandOfCards hand = new HandOfCards();

	// basic stats
	private String playerName = "";
	private int playerChipAmount = 0;

	public int currentHandScore = 0;
	public int totalDiscardCount = 0;
	public int totalTakeCardCount = 0;

	// current round variables
	private boolean paidStake = false;

	public PokerPlayer(String name, int chips) {
		playerName = name;
		playerChipAmount = chips;

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
			hand.add(DeckOfCards.getInstance().dealNext());
		}

	}

	/*private method collects new card from deck after discarding.
	* */
	synchronized public void getNewCardsForHand() {
		if(hand.size() < MAX_HAND) {
			for (int i = hand.size(); i < MAX_HAND; i++) {
				hand.add(DeckOfCards.getInstance().dealNext());
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


	synchronized public int discard() {
		int discardCount = 0;
		List<probabilityScoreList> scoreList = new ArrayList<>();

		for (int i = 0; i < MAX_HAND; i++) { scoreList.add(new probabilityScoreList(i, hand.getDiscardProbability(i))); }
		// sorts scores in descending order
		sortProbabilityScoreDescending(scoreList);
		List<probabilityScoreList> removeList = new ArrayList<>();
		for (probabilityScoreList object : scoreList) {
			if (discardCount >= MAX_DISCARD ) { break; }
			if (object.getCardProbabilityScore() > 0) {
				DeckOfCards.getInstance().returnCard(hand.get(object.cardLocation));
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

	public void payCurrentStake() {
		if(playerChipAmount >= PokerGame.getInstance().getCurrentRoundsHeldStake()) {
			playerChipAmount -= PokerGame.getInstance().getCurrentRoundsHeldStake();
			PokerGame.getInstance().addToCurrentRoundsHeldStake(PokerGame.getInstance().getCurrentRoundsHeldStake());
			paidStake = true;
		}
		System.out.println(this.getPlayerName()+"unable to payCurrentStake...");
	}

	public boolean isPaidStake() {
		return paidStake;
	}

	public void resetPaidStake() {
		paidStake = false;
	}


















	/*Class testing method
				* */
	public static void main(String[] args) {
		System.out.println("poker.PokerPlayer.java!");
		//System.out.println("deck: "+DeckOfCards.getInstance());

		ArrayList<PokerPlayer> playerList = new ArrayList<PokerPlayer>();
		for (int j = 0; j < 10; j++) {
			playerList.add(new PokerPlayer("player:"+j, 3000));
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
			playerList.get(i).discard();
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