package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		Collections.sort(this ,new Comparator<PlayingCard>() {
			@Override
			public int compare(PlayingCard s1, PlayingCard s2) {
				return Float.compare(s1.getFaceValue(), s2.getFaceValue());
			}
		});
	}


	private boolean bestHandtype() {
		if(true == isRoyalFlush) {
			return isRoyalFlush();
		}
		if(true ==  isStraightFlush) {
			return isStraightFlush();
		}
		if(true ==  isFourOfAKind) {
			return isFourOfAKind();
		}
		if(true ==  isFullHouse) {
			return isFullHouse();
		}
		if(true ==  isFlush) {
			return isFlush();
		}
		if(true ==  isStraight) {
			return isStraight();
		}
		if(true ==  isThreeOfAKind) {
			return isThreeOfAKind();
		}
		if(true ==  isTwoPair) {
			return isTwoPair();
		}
		if(true ==  isPair) {
			return isPair();
		}
		if(true ==  isHighCard) {
			return isHighCard();
		}
		return false;
	}

	/*A, K, Q, J, 10 all of the same suit
	* */
	public boolean isRoyalFlush() {
		if(this.get(0).getSuit().equals(this.get(1).getSuit())
				&& this.get(0).getSuit().equals(this.get(2).getSuit())
				&& this.get(0).getSuit().equals(this.get(3).getSuit())
				&& this.get(0).getSuit().equals(this.get(4).getSuit())) {
			if(this.get(0).getFaceValue() == 1 &&
					this.get(1).getFaceValue() == 10 &&
					this.get(2).getFaceValue() == 11 &&
					this.get(3).getFaceValue() == 12 &&
					this.get(4).getFaceValue() == 13 ) {
				isRoyalFlush = true;
			}
		}
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
		if(this.get(0).getSuit().equals(this.get(1).getSuit())
				&& this.get(0).getSuit().equals(this.get(2).getSuit())
				&& this.get(0).getSuit().equals(this.get(3).getSuit())
				&& this.get(0).getSuit().equals(this.get(4).getSuit())) {
			isFlush = true;
		}
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
		System.out.println(testHand.fullDeckToString());
		System.out.println("*********** sort");
		testHand.sort();
		System.out.println(testHand.fullDeckToString());

		DeckOfCards deck2 = new DeckOfCards();
		HandOfCards flushDeck = new HandOfCards();
			flushDeck.add(deck2.get(0));
			flushDeck.add(deck2.get(4));
			flushDeck.add(deck2.get(8));
			flushDeck.add(deck2.get(12));
			flushDeck.add(deck2.get(16));
		//System.out.println(flushDeck.fullDeckToString());
		System.out.println("*********** flushDeck");
		System.out.println(flushDeck.isFlush());

		HandOfCards royalFlushDeck = new HandOfCards();
		royalFlushDeck.add(deck2.get(3));
		royalFlushDeck.add(deck2.get(39));
		royalFlushDeck.add(deck2.get(43));
		royalFlushDeck.add(deck2.get(47));
		royalFlushDeck.add(deck2.get(51));
		System.out.println(royalFlushDeck.fullDeckToString());
		System.out.println("*********** royalFlushDeck");
		System.out.println(royalFlushDeck.isRoyalFlush());


	}
}
