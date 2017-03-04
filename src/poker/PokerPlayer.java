package poker;

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
		for (int i = 0; i < MAX_HAND; i++) {
			if (discardCount >= 3) { break; }
			if(hand.getDiscardProbability(i)>0) {
				discardCount++;
				DeckOfCards.getInstance().returnCard(hand.get(i));
				hand.remove(i);
			}
		}
		return discardCount;
	}
}
