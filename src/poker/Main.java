package poker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin kelly on 27/01/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869
 */

public class Main {

	public static void main(String[] args) {
		System.out.println("main class!");

		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		List<PlayingCard> player1 = new ArrayList<>();
		List<PlayingCard> player2 = new ArrayList<>();
		System.out.println("***************************** deal cards");
		for (int i = 0; i < 5; i++) {
			player1.add(deck.dealNext());
			player2.add(deck.dealNext());
		}
		System.out.println(player1);
		System.out.println(player2);
		System.out.println(deck.size());
		System.out.println("***************************** return cards");
		for (int i = 0; i < 5; i++) {
			deck.returnCard(player1.get(0));
			deck.returnCard(player2.get(0));
			player1.remove(0);
			player2.remove(0);
		}
		System.out.println(deck.size());
		System.out.println(deck);
	}
}
