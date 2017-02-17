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
		HandOfCards player6 = new HandOfCards();
		HandOfCards player7 = new HandOfCards();
		HandOfCards player8 = new HandOfCards();
		HandOfCards player9 = new HandOfCards();
		HandOfCards player10 = new HandOfCards();

		System.out.println("***************************** deal cards");
		System.out.println(deck);
		for (int i = 0; i < 5; i++) {
			player1.add(deck.dealNext());
			player2.add(deck.dealNext());
			player3.add(deck.dealNext());
			player4.add(deck.dealNext());
			player5.add(deck.dealNext());
			player6.add(deck.dealNext());
			player7.add(deck.dealNext());
			player8.add(deck.dealNext());
			player9.add(deck.dealNext());
			player10.add(deck.dealNext());
		}
		System.out.println("player1: "+player1);
		System.out.println("player2: "+player2);
		System.out.println("player3: "+player3);
		System.out.println("player4: "+player4);
		System.out.println("player5: "+player5);
		System.out.println("player6: "+player6);
		System.out.println("player7: "+player7);
		System.out.println("player8: "+player8);
		System.out.println("player9: "+player9);
		System.out.println("player10: "+player10);
		player1.generateHandType();
		player2.generateHandType();
		player3.generateHandType();
		player4.generateHandType();
		player5.generateHandType();
		player6.generateHandType();
		player7.generateHandType();
		player8.generateHandType();
		player9.generateHandType();
		player10.generateHandType();
		System.out.println("***************************** sort and calc hand type");
		System.out.println("player1: "+player1+"\t"+player1.getBestHandTypeName()+"\t\tScore: "+player1.getGameValue());
		System.out.println("player2: "+player2+"\t"+player2.getBestHandTypeName()+"\t\tScore: "+player2.getGameValue());
		System.out.println("player3: "+player3+"\t"+player3.getBestHandTypeName()+"\t\tScore: "+player3.getGameValue());
		System.out.println("player4: "+player4+"\t"+player4.getBestHandTypeName()+"\t\tScore: "+player4.getGameValue());
		System.out.println("player5: "+player5+"\t"+player5.getBestHandTypeName()+"\t\tScore: "+player5.getGameValue());
		System.out.println("player6: "+player6+"\t"+player6.getBestHandTypeName()+"\t\tScore: "+player6.getGameValue());
		System.out.println("player7: "+player7+"\t"+player7.getBestHandTypeName()+"\t\tScore: "+player7.getGameValue());
		System.out.println("player8: "+player8+"\t"+player8.getBestHandTypeName()+"\t\tScore: "+player8.getGameValue());
		System.out.println("player9: "+player9+"\t"+player9.getBestHandTypeName()+"\t\tScore: "+player9.getGameValue());
		System.out.println("player10: "+player10+"\t"+player10.getBestHandTypeName()+"\t\tScore: "+player10.getGameValue());

	}
}
