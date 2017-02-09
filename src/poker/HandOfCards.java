package poker;

import java.util.ArrayList;

/**
 * Created by benjamin kelly on 08/02/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869
 */
public class HandOfCards extends ArrayList<PlayingCard> {

	private boolean isRoyalFlush = false;
	private boolean isStraightFlush = false;
	private boolean isFourOfAKind = false;
	private boolean isFullHouse = false;
	private boolean isFlush = false;
	private boolean isStraight = false;
	private boolean isThreeOfAKind = false;
	private boolean isTwoPair = false;
	private boolean isPair = false;
	private boolean isHighCard = false;


	public HandOfCards() {
	}

	private void sort() {
		//this.get().getFaceValue().sort();
	}

	/*A, K, Q, J, 10 all of the same suit
	* */
	public boolean isRoyalFlush() {
		return isRoyalFlush;
	}

	/*Any five cards sequence in the same suit
	* */
	public boolean isStraightFlush() {
		return isStraightFlush;
	}
	/*All four cards of the same rank
	* */
	public boolean isFourOfAKind() {
		return isFourOfAKind;
	}
	/*Three of a kind combined with a pair
	* */
	public boolean isFullHouse() {
		return isFullHouse;
	}
	/*Any five cards of the same suit, but not in sequence.
	* */
	public boolean isFlush() {
		return isFlush;
	}
	/*Five cards in sequence, but not in the same suit
	* */
	public boolean isStraight() {
		return isStraight;
	}
	/*Three cards of the same rank
	* */
	public boolean isThreeOfAKind() {
		return isThreeOfAKind;
	}
	/*Two separate pairs
*    */
	public boolean isTwoPair() {
		return isTwoPair;
	}
	/*Two cards of the same rank
	* */
	public boolean isPair() {
		return isPair;
	}
	/*Otherwise unrelater cards ranked by the highest single card.
	* */
	public boolean isHighCard() {
		return isHighCard;
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
		System.out.println("poker.HandOfCards.java!");

		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		HandOfCards testHand = new HandOfCards();
		for (int i = 0; i < 5; i++) {
			testHand.add(deck.dealNext());
		}
		testHand.sort();
		System.out.println(testHand.fullDeckToString());


	}
}
