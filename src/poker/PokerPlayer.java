package poker;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by benjamin kelly on 03/03/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869
 */
public class PokerPlayer {

	int MAX_HAND = 5;
	int currentHandScore = 0;
	HandOfCards hand = new HandOfCards();


	public PokerPlayer() {
		getInitialHand();
		getCurrentHandInfo();
	}

	/* prints  in [3S, 6H, 6D, 7D, 9H]... format
	*
	* */
	public String toString() {
		return hand.toString();
	}


	synchronized private void getInitialHand() {
		for (int i = 0; i < MAX_HAND; i++) {
			hand.add(DeckOfCards.getInstance().dealNext());
		}

	}

	synchronized private void getCurrentHandInfo() {
		hand.generateHandType();
		currentHandScore = hand.getGameValue();
	}


	synchronized public int discard() {
		int discardCount = 0;
		PlayingCard temp;

		List<probabilityScoreList> scoreList = new ArrayList<>();
		for (int i = 0; i < MAX_HAND; i++) {
			scoreList.add(new probabilityScoreList(i, hand.getDiscardProbability(i)));
		}
		System.out.println(scoreList);
		// sorts scores in descending order
		Collections.sort(scoreList, new Comparator<probabilityScoreList>() {
			@Override
			public int compare(probabilityScoreList score1, probabilityScoreList score2) {
				return Float.compare(score2.getCardProbabilityScore(), score1.getCardProbabilityScore());
			}
		});
		System.out.println(scoreList);
		System.out.println("hand: "+hand);


		List<probabilityScoreList> removeList = new ArrayList<>();
		for (probabilityScoreList object : scoreList) {
			if (discardCount >= 3) {
				break;
			}
			else if (object.getCardProbabilityScore() > 0) {
				DeckOfCards.getInstance().returnCard(hand.get(object.cardLocation));
				temp = hand.get(object.cardLocation);
				System.out.println("*****card discarded: " + temp + "\t" + temp.getFullName());
				//hand.remove(temp);
				removeList.add(object);
				discardCount++;
			}
		}

		System.out.println(removeList);
		Collections.sort(removeList, new Comparator<probabilityScoreList>() {
			@Override
			public int compare(probabilityScoreList score1, probabilityScoreList score2) {
				return Float.compare(score2.getCardLocation(), score1.getCardLocation());
			}
		});

		System.out.println(removeList);


		for (int i = 0; i < removeList.size() - 1; i++) {
			if (removeList.get(i) != null) {
				hand.removeCard(removeList.get(i).cardLocation);
			}
		}
		System.out.println("hand: "+hand);
		System.out.println("deck: "+DeckOfCards.getInstance());

		return discardCount;
	}

	private class probabilityScoreList {
		int cardLocation;
		int cardProbabilityScore;

		probabilityScoreList(int location, int probabilityScore) {
			cardLocation = location;
			cardProbabilityScore = probabilityScore;
		}

		public int getCardLocation() {
			return cardLocation;
		}

		public int getCardProbabilityScore() {
			return cardProbabilityScore;
		}

		public String toString() {
			return "[" + this.cardLocation + ", " + this.cardProbabilityScore + "]";
		}
	}


	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.PokerPlayer.java!");
		//System.out.println("deck: "+DeckOfCards.getInstance());

		ArrayList<PokerPlayer> playerList = new ArrayList<PokerPlayer>();
		for (int j = 0; j < 10; j++) {
			playerList.add(new PokerPlayer());
		}
		int playNumber = 1;
		for (PokerPlayer object : playerList) {
			System.out.println("player" + playNumber + ": " + object + "\t" + object.hand.getBestHandTypeName()
					+ "\t\tScore: " + object.hand.getGameValue() + "\tprob: " + object.hand.getDiscardProbability(0) + ", "
					+ object.hand.getDiscardProbability(1) + ", " + object.hand.getDiscardProbability(2) + ", "
					+ object.hand.getDiscardProbability(3) + ", " + object.hand.getDiscardProbability(4));
			playNumber++;
			object.hand.discard();
		}
		System.out.println(DeckOfCards.getInstance().size());

		playerList.get(0).discard();
		playerList.get(1).discard();
		playerList.get(2).discard();
		playerList.get(3).discard();
		playerList.get(4).discard();
		playerList.get(5).discard();
		playerList.get(6).discard();
		playerList.get(7).discard();
		playerList.get(8).discard();
		playerList.get(9).discard();


		playNumber = 1;
		for (PokerPlayer object : playerList) {
			System.out.println("player" + playNumber + ": " + object + "\t");
			playNumber++;
		}

		System.out.println(DeckOfCards.getInstance().size());
	}
}