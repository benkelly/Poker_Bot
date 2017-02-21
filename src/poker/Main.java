package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by benjamin kelly on 27/01/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869
 */

public class Main {

	public static void main(String[] args) {
		System.out.println("main class!");



		int count =0;
		while(true){

			DeckOfCards deck = new DeckOfCards();
			deck.shuffle();
			ArrayList<HandOfCards> playerList = new ArrayList<HandOfCards>();
			for (int i = 0; i < 10; i++) {
				playerList.add(new HandOfCards());
			}
			for (HandOfCards object : playerList) {
				for (int j = 0; j < 5; j++) {
					object.add(deck.dealNext());
				}
			}
			int position = 1;
			for (HandOfCards object : playerList) {
				//System.out.println("player1: "+object);
				object.generateHandType();
				System.out.println("player" + position + ": " + object + "\t" + object.getBestHandTypeName() + "\t\tScore: " + object.getGameValue());
				position++;
			}

			Collections.sort(playerList, new Comparator<HandOfCards>() {
				public int compare(HandOfCards card1, HandOfCards card2) {
					return Float.compare(card1.getGameValue(), card2.getGameValue());
				}
			});

			System.out.println("********* winning hand *********");
			System.out.println(playerList.get(9) + "\t" + playerList.get(9).getBestHandTypeName() + "\t\tScore: " + playerList.get(9).getGameValue()+"\n");


			/*will loop til card looked for here is the winning hand
			* */
			if(playerList.get(9).isRoyalFlush()){
				System.out.println(" hand: "+count);
				return;
			}
			count++;
		}
	}
}
