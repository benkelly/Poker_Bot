package poker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by benjamin kelly on 02/02/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869

 */
public class DeckOfCards extends ArrayList<PlayingCard> {

	private int[] faceValue = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	private int[] gameValue = {14, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	private char[] suitType = {'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'};

	public DeckOfCards() {
		this.reset();
	}

	/*Clears and reinitialise whole deck. e.g. a new fresh deck.
	* */
	void reset() {
		this.clear();
		for (int i = 0; i < 13; i++) {
			this.add(new PlayingCard(suitType[i], PlayingCard.HEARTS, faceValue[i], gameValue[i]));
			this.add(new PlayingCard(suitType[i], PlayingCard.CLUBS, faceValue[i], gameValue[i]));
			this.add(new PlayingCard(suitType[i], PlayingCard.SPADES, faceValue[i], gameValue[i]));
			this.add(new PlayingCard(suitType[i], PlayingCard.DIAMONDS, faceValue[i], gameValue[i]));
		}
	}

	/*Randomises deck thoroughly.
	* */
	void shuffle() {
		for (int i = 0; i < Math.pow(this.size(), 2); i++) {
			Collections.shuffle(this);
		}
	}

	/*Removes First PlayingCard from deck and returns it.
	* */
	PlayingCard dealNext() {
		PlayingCard curCard = this.get(0);
		this.remove(0);
		return curCard;
	}
	/*Returns a PlayingCard @Parameter back into Deck.
	* */
	void returnCard(PlayingCard discarded) {
		this.add(discarded);
	}

	/*Used for testing class. returns string of cards in deck currently.
	* */
	private String fullDeckToString() {
		String strList = "";
		for(int i=0; i<52; i++) {
			strList = strList + this.get(i).toString()+"\t"+ this.get(i).getFullName() +"\n";
		}
		return strList;
	}


	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.DeckOfCards.java!");
		DeckOfCards testDeck = new DeckOfCards();
		//testDeck.reset();
		System.out.println(testDeck.fullDeckToString());
		System.out.println("*************** shuffle");
		System.out.println(testDeck.size());
		testDeck.shuffle();
		System.out.println(testDeck.fullDeckToString());
		System.out.println("*************** reset");
		System.out.println(testDeck.size());
		testDeck.reset();
		System.out.println(testDeck.fullDeckToString());

		System.out.println("***************************** deal");
		PlayingCard dealtCard = testDeck.dealNext();
		System.out.println(dealtCard.getFullName());
		System.out.println(testDeck.size());
		testDeck.shuffle();
		PlayingCard dealtCard2 = testDeck.dealNext();
		System.out.println(dealtCard2.getFullName());
		System.out.println(testDeck.size());

		System.out.println("***************************** return");
		testDeck.returnCard(dealtCard);
		System.out.println(testDeck.get(50).getFullName());
	}
}
