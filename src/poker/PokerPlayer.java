package poker;

import java.util.ArrayList;

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
	public String toString(){
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
		for (int i = 0; i < MAX_HAND; i++) {
			if (discardCount >= 3) { return discardCount; }
			if(hand.getDiscardProbability(i) > 0) {
				//DeckOfCards.getInstance().returnCard(hand.get(i));
				temp = hand.get(i);
				//hand.removeCard(i);
				DeckOfCards.getInstance().returnCard(temp);

				discardCount++;
			}
		}
		return discardCount;
	}



	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.PokerPlayer.java!");
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
		}
		System.out.println(DeckOfCards.getInstance().size());

		playerList.get(0).discard();

		playNumber = 1;
		for (PokerPlayer object : playerList) {
			System.out.println("player" + playNumber + ": " + object + "\t" + object.hand.getBestHandTypeName()
					+ "\t\tScore: " + object.hand.getGameValue() + "\tprob: " + object.hand.getDiscardProbability(0) + ", "
					+ object.hand.getDiscardProbability(1) + ", " + object.hand.getDiscardProbability(2) + ", "
					+ object.hand.getDiscardProbability(3) + ", " + object.hand.getDiscardProbability(4));
			playNumber++;
		}

		System.out.println(DeckOfCards.getInstance().size());
	}

}
