package poker;

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
		HandOfCards player1 = new HandOfCards();
		HandOfCards player2 = new HandOfCards();
		HandOfCards player3 = new HandOfCards();
		HandOfCards player4 = new HandOfCards();
		HandOfCards player5 = new HandOfCards();

		System.out.println("***************************** deal cards");
		System.out.println(deck);
		for (int i = 0; i < 5; i++) {
			player1.add(deck.dealNext());
			player2.add(deck.dealNext());
			player3.add(deck.dealNext());
			player4.add(deck.dealNext());
			player5.add(deck.dealNext());
		}
		System.out.println("player1: "+player1);
		System.out.println("player2: "+player2);
		System.out.println("player3: "+player3);
		System.out.println("player4: "+player4);
		System.out.println("player5: "+player5);
		player1.generateHandType();
		player2.generateHandType();
		player3.generateHandType();
		player4.generateHandType();
		player5.generateHandType();
		System.out.println("sort and calc hand type");
		System.out.println("player1: "+player1+"\t"+player1.getBestHandTypeName());
		System.out.println("player1: "+player2+"\t"+player2.getBestHandTypeName());
		System.out.println("player1: "+player3+"\t"+player3.getBestHandTypeName());
		System.out.println("player1: "+player4+"\t"+player4.getBestHandTypeName());
		System.out.println("player1: "+player5+"\t"+player5.getBestHandTypeName());

	}
}
