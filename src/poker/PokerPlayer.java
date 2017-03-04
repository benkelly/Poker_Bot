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
		for (int i = 0; i < hand.size(); i++) {
			if (discardCount >= 3) { return discardCount; }
			if(hand.getDiscardProbability(i)>0) {
				DeckOfCards.getInstance().returnCard(hand.get(i));
				hand.remove(i);
				discardCount++;
			}
		}
		return discardCount;
	}

}
