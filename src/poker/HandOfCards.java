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

	public HandOfCards(){
	}

	private void sort() {
		Collections.sort(this ,new Comparator<PlayingCard>() {
			@Override
			public int compare(PlayingCard s1, PlayingCard s2) {
				return Float.compare(s1.getFaceValue(), s2.getFaceValue());
			}
		});
	}
	private void generateHandType() {
		this.isRoyalFlush();
		this.isStraightFlush();
		this.isFourOfAKind();
		this.isFullHouse();
		this.isFlush();
		this.isStraight();
		this.isThreeOfAKind();
		this.isTwoPair();
		this.isPair();
		this.isHighCard();
	}

	private String HandtypeToBooleanStr() {
		return"isRoyalFlush: "+this.isRoyalFlush+
		"\nisStraightFlush: "+this.isStraightFlush+
		"\nisFourOfAKind: "+this.isFourOfAKind+
		"\nisFullHouse: "+this.isFullHouse+
		"\nisFlush: "+this.isFlush+
		"\nisStraight: "+this.isStraight+
		"\nisThreeOfAKind: "+this.isThreeOfAKind+
		"\nisTwoPair: "+this.isTwoPair+
		"\nisPair: "+this.isPair+
		"\nisHighCard: "+this.isHighCard;
	}

	private String bestHandType() {
		if(true == isRoyalFlush) {
			return "Royal Flush";
		}
		if(true ==  isStraightFlush) {
			return "StraightFlush";
		}
		if(true ==  isFourOfAKind) {
			return "FourOfAKind";
		}
		if(true ==  isFullHouse) {
			return "FullHouse";
		}
		if(true ==  isFlush) {
			return "Flush";
		}
		if(true ==  isStraight) {
			return "Straight";
		}
		if(true ==  isThreeOfAKind) {
			return "ThreeOfAKind";
		}
		if(true ==  isTwoPair) {
			return "TwoPair";
		}
		if(true ==  isPair) {
			return "Pair";
		}
		if(true ==  isHighCard) {
			return "HighCard";
		}
		return null;
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
		if(this.get(0).getSuit().equals(this.get(1).getSuit())
				&& this.get(0).getSuit().equals(this.get(2).getSuit())
				&& this.get(0).getSuit().equals(this.get(3).getSuit())
				&& this.get(0).getSuit().equals(this.get(4).getSuit())) {
			if (this.get(1).getFaceValue() == (this.get(0).getFaceValue() + 1) &&
					this.get(2).getFaceValue() == (this.get(0).getFaceValue() + 2) &&
					this.get(3).getFaceValue() == (this.get(0).getFaceValue() + 3) &&
					this.get(4).getFaceValue() == (this.get(0).getFaceValue() + 4)) {
				isStraightFlush = true;
			}
		}
		return isStraightFlush;
	}
	/*All four cards of the same rank
	* */
	public boolean isFourOfAKind() {
		if (this.get(0).getFaceValue() == this.get(1).getFaceValue() &&
				this.get(0).getFaceValue() == this.get(2).getFaceValue() &&
				this.get(0).getFaceValue() == this.get(3).getFaceValue() ||
				this.get(1).getFaceValue() == this.get(2).getFaceValue() &&
						this.get(1).getFaceValue() == this.get(3).getFaceValue() &&
						this.get(1).getFaceValue() == this.get(4).getFaceValue() ) {
			isFourOfAKind =true;
		}
		return isFourOfAKind;
	}
	/*Three of a kind combined with a pair
	* */
	public boolean isFullHouse() {
		if (this.get(0).getFaceValue() == this.get(1).getFaceValue() &&
				this.get(0).getFaceValue() == this.get(2).getFaceValue() &&
				this.get(3).getFaceValue() == this.get(4).getFaceValue() ) {
			isFullHouse =true;
		}
		if (this.get(2).getFaceValue() == this.get(3).getFaceValue() &&
				this.get(2).getFaceValue() == this.get(4).getFaceValue() &&
				this.get(0).getFaceValue() == this.get(1).getFaceValue() ) {
			isFullHouse =true;
		}
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
		if (this.get(1).getFaceValue() == (this.get(0).getFaceValue() + 1) &&
				this.get(2).getFaceValue() == (this.get(0).getFaceValue() + 2) &&
				this.get(3).getFaceValue() == (this.get(0).getFaceValue() + 3) &&
				this.get(4).getFaceValue() == (this.get(0).getFaceValue() + 4)) {
			if(this.get(0).getSuit() != (this.get(1).getSuit())
					|| this.get(0).getSuit() != (this.get(2).getSuit())
					|| this.get(0).getSuit() != (this.get(3).getSuit())
					|| this.get(0).getSuit() != (this.get(4).getSuit())) {
				isStraight = true;
			}
		}
		return isStraight;
	}
	/*Three cards of the same rank
	* */
	public boolean isThreeOfAKind() {
		if (this.get(0).getFaceValue() == this.get(1).getFaceValue() &&
				this.get(0).getFaceValue() == this.get(2).getFaceValue() ||
				this.get(1).getFaceValue() == this.get(2).getFaceValue() &&
						this.get(1).getFaceValue() == this.get(3).getFaceValue() ||
				this.get(2).getFaceValue() == this.get(3).getFaceValue() &&
						this.get(2).getFaceValue() == this.get(4).getFaceValue()) {
			isThreeOfAKind = true;
		}
		return isThreeOfAKind;
	}
	/*Two separate pairs
*    */
	public boolean isTwoPair() {
		if (this.get(0).getFaceValue() == this.get(1).getFaceValue() && this.get(2).getFaceValue() == this.get(3).getFaceValue() &&
				this.get(3).getFaceValue() != this.get(4).getFaceValue() ||
				this.get(1).getFaceValue() == this.get(2).getFaceValue() && this.get(3).getFaceValue() == this.get(4).getFaceValue() &&
						this.get(0).getFaceValue() != this.get(1).getFaceValue() ||
				this.get(0).getFaceValue() == this.get(1).getFaceValue() && this.get(3).getFaceValue() == this.get(4).getFaceValue() &&
						this.get(1).getFaceValue() != this.get(2).getFaceValue() &&
						this.get(2).getFaceValue() != this.get(3).getFaceValue() ) {
			isTwoPair = true;
		}
		return isTwoPair;
	}
	/*Two cards of the same rank
	* */
	public boolean isPair() {
		if (this.get(0).getFaceValue() == this.get(1).getFaceValue() ||
				this.get(1).getFaceValue() == this.get(2).getFaceValue() ||
				this.get(2).getFaceValue() == this.get(3).getFaceValue() ||
				this.get(3).getFaceValue() == this.get(4).getFaceValue() ) {
			isPair = true;
		}
		return isPair;
	}
	/*Otherwise unrelater cards ranked by the highest single card.
	* */
	public boolean isHighCard() {
		if(isRoyalFlush == false && isStraightFlush == false && isFourOfAKind == false && isFullHouse == false && 
		isFlush == false && isStraight == false && isThreeOfAKind == false && isTwoPair == false && isPair == false ) {
			isHighCard =true;
		}
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
		royalFlushDeck.add(deck2.get(43));
		royalFlushDeck.add(deck2.get(47));
		royalFlushDeck.add(deck2.get(3));
		royalFlushDeck.add(deck2.get(39));
		royalFlushDeck.add(deck2.get(51));
		//System.out.println(royalFlushDeck.fullDeckToString());
		System.out.println("*********** royalFlushDeck");
		royalFlushDeck.sort();
		System.out.println(royalFlushDeck.isRoyalFlush());

		HandOfCards straightFlushDeck = new HandOfCards();
		straightFlushDeck.add(deck2.get(17));
		straightFlushDeck.add(deck2.get(21));
		straightFlushDeck.add(deck2.get(5));
		straightFlushDeck.add(deck2.get(9));
		straightFlushDeck.add(deck2.get(13));
		//System.out.println(straightFlushDeck.fullDeckToString());
		System.out.println("*********** straightFlushDeck");
		straightFlushDeck.sort();
		System.out.println(straightFlushDeck.isStraightFlush());


		HandOfCards fourOfAKindDeck = new HandOfCards();
		fourOfAKindDeck.add(deck2.get(23));
		fourOfAKindDeck.add(deck2.get(0));
		fourOfAKindDeck.add(deck2.get(1));
		fourOfAKindDeck.add(deck2.get(2));
		fourOfAKindDeck.add(deck2.get(3));
		//System.out.println(straightFlushDeck.fullDeckToString());
		System.out.println("*********** fourOfAKindDeck aces");
		fourOfAKindDeck.sort();
		System.out.println(fourOfAKindDeck.isFourOfAKind());
		HandOfCards fourOfAKindDeck2 = new HandOfCards();
		fourOfAKindDeck2.add(deck2.get(51));
		fourOfAKindDeck2.add(deck2.get(50));
		fourOfAKindDeck2.add(deck2.get(49));
		fourOfAKindDeck2.add(deck2.get(48));
		fourOfAKindDeck2.add(deck2.get(3));
		//System.out.println(straightFlushDeck.fullDeckToString());
		System.out.println("*********** fourOfAKindDeck king");
		fourOfAKindDeck2.sort();
		System.out.println(fourOfAKindDeck2.isFourOfAKind());


		HandOfCards threeOfAKindDeck = new HandOfCards();
		threeOfAKindDeck.add(deck2.get(23));
		threeOfAKindDeck.add(deck2.get(0));
		threeOfAKindDeck.add(deck2.get(1));
		threeOfAKindDeck.add(deck2.get(2));
		threeOfAKindDeck.add(deck2.get(3));
		//System.out.println(threeOfAKindDeck.fullDeckToString());
		System.out.println("*********** threeOfAKindDeck aces");
		threeOfAKindDeck.sort();
		System.out.println(threeOfAKindDeck.isThreeOfAKind());
		HandOfCards threeOfAKindDeck2 = new HandOfCards();
		threeOfAKindDeck2.add(deck2.get(51));
		threeOfAKindDeck2.add(deck2.get(50));
		threeOfAKindDeck2.add(deck2.get(49));
		threeOfAKindDeck2.add(deck2.get(48));
		threeOfAKindDeck2.add(deck2.get(3));
		//System.out.println(straightFlushDeck.fullDeckToString());
		System.out.println("*********** threeOfAKindDeck2 king");
		threeOfAKindDeck2.sort();
		System.out.println(threeOfAKindDeck2.isThreeOfAKind());
		HandOfCards threeOfAKindDeck3 = new HandOfCards();
		threeOfAKindDeck3.add(deck2.get(0));
		threeOfAKindDeck3.add(deck2.get(4));
		threeOfAKindDeck3.add(deck2.get(5));
		threeOfAKindDeck3.add(deck2.get(6));
		threeOfAKindDeck3.add(deck2.get(30));
		//System.out.println(threeOfAKindDeck3.fullDeckToString());
		System.out.println("*********** threeOfAKindDeck3 ace,3*2,8");
		threeOfAKindDeck3.sort();
		System.out.println(threeOfAKindDeck3.isThreeOfAKind());

		HandOfCards fullHouseDeck = new HandOfCards();
		fullHouseDeck.add(deck2.get(23));
		fullHouseDeck.add(deck2.get(0));
		fullHouseDeck.add(deck2.get(1));
		fullHouseDeck.add(deck2.get(2));
		fullHouseDeck.add(deck2.get(22));
		//System.out.println(fullHouseDeck.fullDeckToString());
		System.out.println("*********** fullHouseDeck aces*3,6*2");
		fullHouseDeck.sort();
		System.out.println(fullHouseDeck.isFullHouse());
		HandOfCards fullHouseDeck2 = new HandOfCards();
		fullHouseDeck2.add(deck2.get(51));
		fullHouseDeck2.add(deck2.get(50));
		fullHouseDeck2.add(deck2.get(0));
		fullHouseDeck2.add(deck2.get(2));
		fullHouseDeck2.add(deck2.get(3));
		//System.out.println(fullHouseDeck2.fullDeckToString());
		System.out.println("*********** fullHouseDeck2 king*2,A*3");
		fullHouseDeck2.sort();
		System.out.println(fullHouseDeck2.isFullHouse());

		HandOfCards straightDeck = new HandOfCards();
		straightDeck.add(deck2.get(17));
		straightDeck.add(deck2.get(21));
		straightDeck.add(deck2.get(14));
		straightDeck.add(deck2.get(6));
		straightDeck.add(deck2.get(9));
		//System.out.println(straightDeck.fullDeckToString());
		System.out.println("*********** straightDeck");
		straightDeck.sort();
		System.out.println(straightDeck.isStraight());


		HandOfCards pairDeck = new HandOfCards();
		pairDeck.add(deck2.get(23));
		pairDeck.add(deck2.get(0));
		pairDeck.add(deck2.get(1));
		pairDeck.add(deck2.get(2));
		pairDeck.add(deck2.get(22));
		//System.out.println(pairDeck.fullDeckToString());
		System.out.println("*********** pairDeck aces*3,6*2");
		pairDeck.sort();
		System.out.println(pairDeck.isPair());
		HandOfCards pairDeck2 = new HandOfCards();
		pairDeck2.add(deck2.get(51));
		pairDeck2.add(deck2.get(50));
		pairDeck2.add(deck2.get(0));
		pairDeck2.add(deck2.get(2));
		pairDeck2.add(deck2.get(3));
		//System.out.println(pairDeck2.fullDeckToString());
		System.out.println("*********** pairDeck2 king*2,A*3");
		pairDeck2.sort();
		System.out.println(pairDeck2.isPair());


		HandOfCards twoPairDeck = new HandOfCards();
		twoPairDeck.add(deck2.get(23));
		twoPairDeck.add(deck2.get(4));
		twoPairDeck.add(deck2.get(1));
		twoPairDeck.add(deck2.get(2));
		twoPairDeck.add(deck2.get(22));
		System.out.println("*********** twoPairDeck aces*2,2,6*2");
		twoPairDeck.sort();
		//System.out.println(twoPairDeck.fullDeckToString());
		System.out.println(twoPairDeck.isTwoPair());
		HandOfCards twoPairDeck2 = new HandOfCards();
		twoPairDeck2.add(deck2.get(41));
		twoPairDeck2.add(deck2.get(40));
		twoPairDeck2.add(deck2.get(51));
		twoPairDeck2.add(deck2.get(2));
		twoPairDeck2.add(deck2.get(3));
		//System.out.println(twoPairDeck2.fullDeckToString());
		System.out.println("*********** twoPairDeck2 king,J*2,A*2");
		twoPairDeck2.sort();
		System.out.println(twoPairDeck2.isTwoPair());
		HandOfCards twoPairDeck3 = new HandOfCards();
		twoPairDeck3.add(deck2.get(41));
		twoPairDeck3.add(deck2.get(40));
		twoPairDeck3.add(deck2.get(51));
		twoPairDeck3.add(deck2.get(50));
		twoPairDeck3.add(deck2.get(3));
		//System.out.println(twoPairDeck3.fullDeckToString());
		System.out.println("*********** twoPairDeck3 king*2,J*2,A");
		twoPairDeck3.sort();
		System.out.println(twoPairDeck3.isTwoPair());

		HandOfCards highCardDeck = new HandOfCards();
		highCardDeck.add(deck2.get(0));
		highCardDeck.add(deck2.get(35));
		highCardDeck.add(deck2.get(30));
		highCardDeck.add(deck2.get(50));
		highCardDeck.add(deck2.get(9));
		System.out.println(highCardDeck.fullDeckToString());
		System.out.println("*********** highCardDeck");
		highCardDeck.sort();
		System.out.println(highCardDeck.isTwoPair());
		System.out.println(highCardDeck.isHighCard());
		highCardDeck.generateHandType();
		System.out.println(highCardDeck.bestHandType());
		System.out.println(highCardDeck.HandtypeToBooleanStr());

		System.out.println("*********** test all hands");
		System.out.println("***** flushDeck");
		flushDeck.generateHandType();
		System.out.println(flushDeck.bestHandType());
		//System.out.println(flushDeck.HandtypeToBooleanStr());
		System.out.println("***** royalFlushDeck");
		royalFlushDeck.generateHandType();
		System.out.println(royalFlushDeck.bestHandType());
		//System.out.println(royalFlushDeck.HandtypeToBooleanStr());

	}
}
