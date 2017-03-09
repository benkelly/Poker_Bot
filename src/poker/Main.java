package poker;

import java.util.ArrayList;

/**
 * Created by benjamin kelly on 27/01/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869
 */

public class Main {

	public static void main(String[] args) {
		System.out.println("main class!");

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
		System.out.println("DeckOfCards: " + DeckOfCards.getInstance().size());
		System.out.println("DeckOfCards: " + DeckOfCards.getInstance());


		for (int j = 0; j < 100; j++) {

		for (int i = 0; i < playerList.size(); i++) {
			playerList.get(i).discard();
			playerList.get(i).getNewCardsForHand();
		}

		playNumber = 1;
		int totalTotalDiscardCount = 0;
		for (PokerPlayer object : playerList) {
			System.out.println("player" + playNumber + ": " + object + "\t");
			System.out.println("totalDiscardCount: "+object.totalDiscardCount);
			totalTotalDiscardCount += object.totalDiscardCount;
			playNumber++;
		}
			for (PokerPlayer object : playerList) {
				object.hand.generateHandType();
				System.out.println("player" + playNumber + ": " + object + "\t" + object.hand.getBestHandTypeName()
						+ "\t\tScore: " + object.hand.getGameValue() + "\tprob: " + object.hand.getDiscardProbability(0) + ", "
						+ object.hand.getDiscardProbability(1) + ", " + object.hand.getDiscardProbability(2) + ", "
						+ object.hand.getDiscardProbability(3) + ", " + object.hand.getDiscardProbability(4));
				playNumber++;
			}
		System.out.println("totalTotalDiscardCount(+2): " + (totalTotalDiscardCount+2));
		System.out.println("DeckOfCards: " + DeckOfCards.getInstance().size());
		System.out.println("DeckOfCards: " + DeckOfCards.getInstance());

		}
	}
}
