package poker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by benjamin kelly on 02/02/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869
 */
public class DeckOfCards extends ArrayList<PlayingCard> {

	// Constant data of cards for deck.
	private int[] faceValue = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	private int[] gameValue = {14, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	private char[] suitType = {'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'};
	static private final int DECK_SIZE = 52;


	private int returnCardCounter = 0;

	public DeckOfCards() {
		this.reset();
	}

	/*Clears and reinitialise whole deck. e.g. a new fresh deck.
	* */
	synchronized void reset() {
		this.clear();
		for (int i = 0; i < suitType.length; i++) {
			this.add(new PlayingCard(suitType[i], PlayingCard.HEARTS, faceValue[i], gameValue[i]));
			this.add(new PlayingCard(suitType[i], PlayingCard.CLUBS, faceValue[i], gameValue[i]));
			this.add(new PlayingCard(suitType[i], PlayingCard.SPADES, faceValue[i], gameValue[i]));
			this.add(new PlayingCard(suitType[i], PlayingCard.DIAMONDS, faceValue[i], gameValue[i]));
		}
	}

	/*Randomises deck thoroughly.
	* */
	synchronized void shuffle() {
		Collections.shuffle(this);
	}

	/*Removes First PlayingCard from deck and returns it.
	* */
	synchronized PlayingCard dealNext() {
		if (this != null && !this.isEmpty()) {
			PlayingCard curCard = this.get(0);
			this.remove(0);
			return curCard;
		}
		else{
			return null;
		}
	}
	/*Returns a PlayingCard @Parameter back into Deck.
		~ if all 52 cards returned, deck will reshuffle.
	* */
	synchronized void returnCard(PlayingCard discarded) {
		if(discarded!=null) {
			this.add(discarded);
			returnCardCounter++;
			if (returnCardCounter == DECK_SIZE) {
				this.shuffle();
				returnCardCounter = 0;
			}
		}
	}

	/*Used for testing class. returns string of cards in deck currently.
	* */
	private String fullDeckToString() {
		String strList = "";
		for(int i=0; i<this.size(); i++) {
			strList = strList + this.get(i).toString()+"\t"+ this.get(i).getFullName() +"\n";
		}
		return strList;
	}


	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.DeckOfCards.java!");
		DeckOfCards testDeck = new DeckOfCards();

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
		//testDeck.shuffle();
		PlayingCard dealtCard2 = testDeck.dealNext();
		System.out.println(dealtCard2.getFullName());
		System.out.println(testDeck.size());

		System.out.println("***************************** return");
		testDeck.returnCard(dealtCard);
		testDeck.returnCard(dealtCard2);
		System.out.println(testDeck.get(50).getFullName());
		System.out.println(testDeck.fullDeckToString());
		System.out.println(testDeck.size());

	}
}
