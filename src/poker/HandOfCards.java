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

	private boolean isRoyalFlush;
	private boolean isStraightFlush;
	private boolean isFourOfAKind;
	private boolean isFullHouse;
	private boolean isFlush;
	private boolean isStraight;
	private boolean isThreeOfAKind;
	private boolean isTwoPair;
	private boolean isPair;
	private boolean isHighCard;

	/*
	* global calculation weights
	* this weight will * the highest card w/ higher power to insure greater score difference
	* 14 as they're are 14 types of cards
	*/
	static private final int CALC_WEIGHT = 14;
	/*
	* hand type weights
	* using multiples of a million to segregate each hand in its own spectrum.
	* therefor a TwoPair of 2s and 3s will still win over a Pair of aces and so on.
	*/
	static private final int ROYAL_FLUSH_WEIGHT = 9000000;
	static private final int STRAIGHT_FLUSH_WEIGHT = 8000000;
	static private final int FOUR_OF_A_KIND_WEIGHT = 7000000;
	static private final int FULL_HOUSE_WEIGHT = 6000000;
	static private final int FLUSH_WEIGHT = 5000000;
	static private final int STRAIGHT_WEIGHT = 4000000;
	static private final int THREE_OF_A_KIND_WEIGHT = 3000000;
	static private final int TWO_PAIR_WEIGHT = 2000000;
	static private final int PAIR_WEIGHT = 1000000;
	static private final int HIGH_CARD_WEIGHT = 0;

	public HandOfCards(){
	}

	/*Sorts hand of cards in ascending order by their type value.
	* */
	synchronized private void sort() {
		Collections.sort(this ,new Comparator<PlayingCard>() {
			@Override
			public int compare(PlayingCard card1, PlayingCard card2) {
				return Float.compare(card1.faceValue(), card2.faceValue());
			}
		});
	}
	/*Calls sort() then goes through all hand Type functions for its hand
	* */
	synchronized public void generateHandType() {
		this.sort();
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

	/*For private testing: Returns string of all
	*   boolean values for its hand.
	*   ~ used to test if hand has multiple handTypes and if follows order
	*   ~ generateHandType() needed to run first!
	* */
	private String handTypeToBooleanStr() {
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

	/*Returns string of name of best hand type.
	*   ~ generateHandType() needed to run first!
	*
	*   ### In future I believe that each hand type will need an int value then * by the main card types
	*   ### value to implement the scoring method.
	* */
	public String getBestHandTypeName() {
		if(isRoyalFlush) return "Royal Flush";
		if(isStraightFlush) return "Straight Flush";
		if(isFourOfAKind) return "Four of a Kind";
		if(isFullHouse) return "Full House";
		if(isFlush) return "Flush";
		if(isStraight) return "Straight";
		if(isThreeOfAKind) return "Three of a Kind";
		if(isTwoPair) return "Two Pair";
		if(isPair) return "Pair";
		if(isHighCard) return "High Card";
		return null;
	}

	public int getGameValue() {
		if(isRoyalFlush) {
			return ROYAL_FLUSH_WEIGHT + this.calcHighCardScore();
		}
		if(isStraightFlush) {
			return STRAIGHT_FLUSH_WEIGHT + this.calcHighCardScore();
		}
		if(isFourOfAKind) {
			return FOUR_OF_A_KIND_WEIGHT + this.get(2).gameValue();
		}
		if(isFullHouse) {
			return FULL_HOUSE_WEIGHT + this.get(2).gameValue();
		}
		if(isFlush) {
			return FLUSH_WEIGHT + this.calcHighCardScore();
		}
		if(isStraight) {
			return STRAIGHT_WEIGHT + this.calcHighCardScore();
		}
		if(isThreeOfAKind) {
			return THREE_OF_A_KIND_WEIGHT + this.get(2).gameValue();
		}
		if(isTwoPair) {
			int twoPairScore;
			if ( this.get(0).gameValue() == this.get(1).gameValue() &&
					this.get(2).gameValue() == this.get(3).gameValue() ) {
				if(this.get(0).faceValue() == 1 && this.get(1).faceValue() == 1){ //check if ace pair in hand
					this.add(this.remove(0)); // move Ace to end of hand
					this.add(this.remove(0)); // move Ace to end of hand
				}
				twoPairScore = CALC_WEIGHT * CALC_WEIGHT  *this.get(2).gameValue() + CALC_WEIGHT *this.get(0).gameValue() + this.get(4).gameValue(); }
			else if ( this.get(0).gameValue() == this.get(1).gameValue() &&
					this.get(3).gameValue() == this.get(4).gameValue() )
				twoPairScore = CALC_WEIGHT * CALC_WEIGHT  *this.get(3).gameValue() + CALC_WEIGHT *this.get(0).gameValue() + this.get(2).gameValue();
			else {
				if(this.get(0).faceValue() == 1 && this.get(1).faceValue() == 1){
					this.add(this.remove(2)); // middle non-pair card to end of hand
					this.add(this.remove(0)); // move Ace to end of hand
					this.add(this.remove(0)); // move Ace to end of hand
				}
				twoPairScore = CALC_WEIGHT * CALC_WEIGHT  *this.get(3).gameValue() + CALC_WEIGHT *this.get(1).gameValue() + this.get(0).gameValue(); }
			return TWO_PAIR_WEIGHT + twoPairScore;
		}
		if(isPair) {
			int pairScore;
			if (this.get(0).gameValue() == this.get(1).gameValue())
				pairScore = CALC_WEIGHT * CALC_WEIGHT * CALC_WEIGHT *this.get(0).gameValue() +
						+ this.get(2).gameValue() + CALC_WEIGHT *this.get(3).gameValue() + CALC_WEIGHT * CALC_WEIGHT *this.get(4).gameValue();
			else if (this.get(1).gameValue() == this.get(2).gameValue())
				pairScore = CALC_WEIGHT * CALC_WEIGHT * CALC_WEIGHT *this.get(1).gameValue() +
						+ this.get(0).gameValue() + CALC_WEIGHT *this.get(3).gameValue() + CALC_WEIGHT * CALC_WEIGHT *this.get(4).gameValue();
			else if (this.get(2).gameValue() == this.get(3).gameValue())
				pairScore = CALC_WEIGHT * CALC_WEIGHT * CALC_WEIGHT *this.get(2).gameValue() +
						+ this.get(0).gameValue() + CALC_WEIGHT *this.get(1).gameValue() + CALC_WEIGHT * CALC_WEIGHT *this.get(4).gameValue();
			else
				pairScore = CALC_WEIGHT * CALC_WEIGHT * CALC_WEIGHT *this.get(3).gameValue() +
						+ this.get(0).gameValue() + CALC_WEIGHT *this.get(1).gameValue() + CALC_WEIGHT * CALC_WEIGHT *this.get(2).gameValue();
			return PAIR_WEIGHT + pairScore;
		}
		if(isHighCard) {
			return HIGH_CARD_WEIGHT + this.calcHighCardScore();
		}
		return 0;
	}
	/*Will calculate the high card score for this hand.
	*   ~ the highest ranked cards have the greatest scoring to increase the scoring gaps between other high card hands
	*   ~ i.e.      [], []*CALC_WEIGHT, []*CALC_WEIGHT^2, []*CALC_WEIGHT^3, []*CALC_WEIGHT^4
	* */
	private int calcHighCardScore() {
		int highCardScore;
		if(this.get(0).faceValue() == 1){
			this.add(this.remove(0)); // move Ace to end of hand
		}
		highCardScore = this.get(0).gameValue() + CALC_WEIGHT * this.get(1).gameValue() + CALC_WEIGHT * CALC_WEIGHT * this.get(2).gameValue()
				+ CALC_WEIGHT * CALC_WEIGHT * CALC_WEIGHT * this.get(3).gameValue() + CALC_WEIGHT * CALC_WEIGHT * CALC_WEIGHT * CALC_WEIGHT * this.get(4).gameValue();
		return highCardScore;
	}




	//////////////// Type testing methods ///////////////
	/*A, K, Q, J, 10 all of the same suit
	* */
	public boolean isRoyalFlush() {
		if(this.get(0).cardSuit().equals(this.get(1).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(2).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(3).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(4).cardSuit())) {
			if(this.get(0).faceValue() == 1 &&
					this.get(1).faceValue() == 10 &&
					this.get(2).faceValue() == 11 &&
					this.get(3).faceValue() == 12 &&
					this.get(4).faceValue() == 13 ) {
				this.add(this.remove(0)); // move Ace to end of hand
				isRoyalFlush = true;
			}
		}
		return isRoyalFlush;
	}

	/*Any five cards sequence in the same suit
	* */
	public boolean isStraightFlush() {
		if(this.get(0).cardSuit().equals(this.get(1).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(2).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(3).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(4).cardSuit())) {
			if (this.get(1).faceValue() == (this.get(0).faceValue() + 1) &&
					this.get(2).faceValue() == (this.get(0).faceValue() + 2) &&
					this.get(3).faceValue() == (this.get(0).faceValue() + 3) &&
					this.get(4).faceValue() == (this.get(0).faceValue() + 4)) {
				isStraightFlush = true;
			}
		}
		return isStraightFlush;
	}
	/*All four cards of the same rank
	* */
	public boolean isFourOfAKind() {
		if (this.get(0).faceValue() == this.get(1).faceValue() &&
				this.get(0).faceValue() == this.get(2).faceValue() &&
				this.get(0).faceValue() == this.get(3).faceValue() ||
				this.get(1).faceValue() == this.get(2).faceValue() &&
						this.get(1).faceValue() == this.get(3).faceValue() &&
						this.get(1).faceValue() == this.get(4).faceValue() ) {
			isFourOfAKind =true;
		}
		return isFourOfAKind;
	}
	/*Three of a kind combined with a pair
	* */
	public boolean isFullHouse() {
		if (this.get(0).faceValue() == this.get(1).faceValue() &&
				this.get(0).faceValue() == this.get(2).faceValue() &&
				this.get(3).faceValue() == this.get(4).faceValue() ) {
			isFullHouse =true;
		}
		if (this.get(2).faceValue() == this.get(3).faceValue() &&
				this.get(2).faceValue() == this.get(4).faceValue() &&
				this.get(0).faceValue() == this.get(1).faceValue() ) {
			isFullHouse =true;
		}
		return isFullHouse;
	}
	/*Any five cards of the same suit, but not in sequence.
	* */
	public boolean isFlush() {
		if(this.get(0).cardSuit().equals(this.get(1).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(2).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(3).cardSuit())
				&& this.get(0).cardSuit().equals(this.get(4).cardSuit())) {
			isFlush = true;
		}
		return isFlush;
	}
	/*Five cards in sequence, but not in the same suit
	* */
	public boolean isStraight() {
		if (this.get(1).faceValue() == (this.get(0).faceValue() + 1) &&
				this.get(2).faceValue() == (this.get(0).faceValue() + 2) &&
				this.get(3).faceValue() == (this.get(0).faceValue() + 3) &&
				this.get(4).faceValue() == (this.get(0).faceValue() + 4)) {
			if(this.get(0).cardSuit() != (this.get(1).cardSuit()) //if not same suit
					|| this.get(0).cardSuit() != (this.get(2).cardSuit())
					|| this.get(0).cardSuit() != (this.get(3).cardSuit())
					|| this.get(0).cardSuit() != (this.get(4).cardSuit())) {
				isStraight = true;
			}
		}
		// if, 10, J, Q, K case
		if (this.get(0).gameValue() == (this.get(1).gameValue() + 4) &&
				this.get(2).gameValue() == (this.get(1).gameValue() + 1) &&
				this.get(3).gameValue() == (this.get(1).gameValue() + 2) &&
				this.get(4).gameValue() == (this.get(1).gameValue() + 3)) {
			if(this.get(0).cardSuit() != (this.get(1).cardSuit()) //if not same suit
					|| this.get(0).cardSuit() != (this.get(2).cardSuit())
					|| this.get(0).cardSuit() != (this.get(3).cardSuit())
					|| this.get(0).cardSuit() != (this.get(4).cardSuit())) {
				this.add(this.remove(0)); // move Ace to end of hand
				isStraight = true;
			}
		}
		return isStraight;
	}
	/*Three cards of the same rank
	* */
	public boolean isThreeOfAKind() {
		if (this.get(0).faceValue() == this.get(1).faceValue() &&
				this.get(0).faceValue() == this.get(2).faceValue() ||
				this.get(1).faceValue() == this.get(2).faceValue() &&
						this.get(1).faceValue() == this.get(3).faceValue() ||
				this.get(2).faceValue() == this.get(3).faceValue() &&
						this.get(2).faceValue() == this.get(4).faceValue()) {
			isThreeOfAKind = true;
		}
		return isThreeOfAKind;
	}
	/*Two separate pairs
	**/
	public boolean isTwoPair() {
		if (this.get(0).faceValue() == this.get(1).faceValue() && this.get(2).faceValue() == this.get(3).faceValue() &&
				this.get(3).faceValue() != this.get(4).faceValue() ||
				this.get(1).faceValue() == this.get(2).faceValue() && this.get(3).faceValue() == this.get(4).faceValue() &&
						this.get(0).faceValue() != this.get(1).faceValue() ||
				this.get(0).faceValue() == this.get(1).faceValue() && this.get(3).faceValue() == this.get(4).faceValue() &&
						this.get(1).faceValue() != this.get(2).faceValue() &&
						this.get(2).faceValue() != this.get(3).faceValue() ) {
			isTwoPair = true;
		}
		return isTwoPair;
	}
	/*Two cards of the same rank
	* */
	public boolean isPair() {
		if (this.get(0).faceValue() == this.get(1).faceValue() ||
				this.get(1).faceValue() == this.get(2).faceValue() ||
				this.get(2).faceValue() == this.get(3).faceValue() ||
				this.get(3).faceValue() == this.get(4).faceValue() ) {
			isPair = true;
		}
		return isPair;
	}
	/*Otherwise unrelated cards ranked by the highest single card.
	* */
	public boolean isHighCard() {
		if(!isRoyalFlush  && !isStraightFlush  && !isFourOfAKind  && !isFullHouse  &&
				!isFlush  && !isStraight  && !isThreeOfAKind  && !isTwoPair  && !isPair  ) {
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

		//testing sort()
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


		//testing hand classing methods with true cases.
		DeckOfCards deck2 = new DeckOfCards();
		HandOfCards flushDeck = new HandOfCards();
		flushDeck.add(deck2.get(0));
		flushDeck.add(deck2.get(4));
		flushDeck.add(deck2.get(8));
		flushDeck.add(deck2.get(12));
		flushDeck.add(deck2.get(20));
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
		//royalFlushDeck.sort();
		System.out.println(royalFlushDeck.isRoyalFlush());

		HandOfCards straightFlushDeck = new HandOfCards();
		straightFlushDeck.add(deck2.get(17));
		straightFlushDeck.add(deck2.get(21));
		straightFlushDeck.add(deck2.get(5));
		straightFlushDeck.add(deck2.get(9));
		straightFlushDeck.add(deck2.get(13));
		//System.out.println(straightFlushDeck.fullDeckToString());
		System.out.println("*********** straightFlushDeck");
		//straightFlushDeck.sort();
		System.out.println(straightFlushDeck.isStraightFlush());


		HandOfCards fourOfAKindDeck = new HandOfCards();
		fourOfAKindDeck.add(deck2.get(23));
		fourOfAKindDeck.add(deck2.get(0));
		fourOfAKindDeck.add(deck2.get(1));
		fourOfAKindDeck.add(deck2.get(2));
		fourOfAKindDeck.add(deck2.get(3));
		//System.out.println(straightFlushDeck.fullDeckToString());
		System.out.println("*********** fourOfAKindDeck aces");
		//fourOfAKindDeck.sort();
		System.out.println(fourOfAKindDeck.isFourOfAKind());
		HandOfCards fourOfAKindDeck2 = new HandOfCards();
		fourOfAKindDeck2.add(deck2.get(51));
		fourOfAKindDeck2.add(deck2.get(50));
		fourOfAKindDeck2.add(deck2.get(49));
		fourOfAKindDeck2.add(deck2.get(48));
		fourOfAKindDeck2.add(deck2.get(3));
		//System.out.println(straightFlushDeck.fullDeckToString());
		System.out.println("*********** fourOfAKindDeck king");
		//fourOfAKindDeck2.sort();
		System.out.println(fourOfAKindDeck2.isFourOfAKind());


		HandOfCards threeOfAKindDeck = new HandOfCards();
		threeOfAKindDeck.add(deck2.get(23));
		threeOfAKindDeck.add(deck2.get(0));
		threeOfAKindDeck.add(deck2.get(1));
		threeOfAKindDeck.add(deck2.get(2));
		threeOfAKindDeck.add(deck2.get(3));
		//System.out.println(threeOfAKindDeck.fullDeckToString());
		System.out.println("*********** threeOfAKindDeck aces");
		//threeOfAKindDeck.sort();
		System.out.println(threeOfAKindDeck.isThreeOfAKind());
		HandOfCards threeOfAKindDeck2 = new HandOfCards();
		threeOfAKindDeck2.add(deck2.get(51));
		threeOfAKindDeck2.add(deck2.get(50));
		threeOfAKindDeck2.add(deck2.get(40));
		threeOfAKindDeck2.add(deck2.get(48));
		threeOfAKindDeck2.add(deck2.get(3));
		//System.out.println(straightFlushDeck.fullDeckToString());
		System.out.println("*********** threeOfAKindDeck2 king");
		//threeOfAKindDeck2.sort();
		System.out.println(threeOfAKindDeck2.isThreeOfAKind());
		HandOfCards threeOfAKindDeck3 = new HandOfCards();
		threeOfAKindDeck3.add(deck2.get(0));
		threeOfAKindDeck3.add(deck2.get(4));
		threeOfAKindDeck3.add(deck2.get(5));
		threeOfAKindDeck3.add(deck2.get(6));
		threeOfAKindDeck3.add(deck2.get(30));
		//System.out.println(threeOfAKindDeck3.fullDeckToString());
		System.out.println("*********** threeOfAKindDeck3 ace,3*2,8");
		//threeOfAKindDeck3.sort();
		System.out.println(threeOfAKindDeck3.isThreeOfAKind());

		HandOfCards fullHouseDeck = new HandOfCards();
		fullHouseDeck.add(deck2.get(23));
		fullHouseDeck.add(deck2.get(0));
		fullHouseDeck.add(deck2.get(1));
		fullHouseDeck.add(deck2.get(2));
		fullHouseDeck.add(deck2.get(22));
		//System.out.println(fullHouseDeck.fullDeckToString());
		System.out.println("*********** fullHouseDeck aces*3,6*2");
		//fullHouseDeck.sort();
		System.out.println(fullHouseDeck.isFullHouse());
		HandOfCards fullHouseDeck2 = new HandOfCards();
		fullHouseDeck2.add(deck2.get(51));
		fullHouseDeck2.add(deck2.get(50));
		fullHouseDeck2.add(deck2.get(0));
		fullHouseDeck2.add(deck2.get(2));
		fullHouseDeck2.add(deck2.get(3));
		//System.out.println(fullHouseDeck2.fullDeckToString());
		System.out.println("*********** fullHouseDeck2 king*2,A*3");
		//fullHouseDeck2.sort();
		System.out.println(fullHouseDeck2.isFullHouse());

		HandOfCards straightDeck = new HandOfCards();
		straightDeck.add(deck2.get(17));
		straightDeck.add(deck2.get(21));
		straightDeck.add(deck2.get(14));
		straightDeck.add(deck2.get(6));
		straightDeck.add(deck2.get(9));
		//System.out.println(straightDeck.fullDeckToString());
		System.out.println("*********** straightDeck");
		//straightDeck.sort();
		System.out.println(straightDeck.isStraight());


		HandOfCards pairDeck = new HandOfCards();
		pairDeck.add(deck2.get(23));
		pairDeck.add(deck2.get(9));
		pairDeck.add(deck2.get(1));
		pairDeck.add(deck2.get(51));
		pairDeck.add(deck2.get(22));
		//System.out.println(pairDeck.fullDeckToString());
		System.out.println("*********** pairDeck ace,3c,6*2,kd");
		//pairDeck.sort();
		System.out.println(pairDeck.isPair());
		HandOfCards pairDeck2 = new HandOfCards();
		pairDeck2.add(deck2.get(51));
		pairDeck2.add(deck2.get(30));
		pairDeck2.add(deck2.get(40));
		pairDeck2.add(deck2.get(2));
		pairDeck2.add(deck2.get(50));
		//System.out.println(pairDeck2.fullDeckToString());
		System.out.println("*********** pairDeck2 king*2,A,8s,jh");
		//pairDeck2.sort();
		System.out.println(pairDeck2.isPair());


		HandOfCards twoPairDeck = new HandOfCards();
		twoPairDeck.add(deck2.get(23));
		twoPairDeck.add(deck2.get(4));
		twoPairDeck.add(deck2.get(1));
		twoPairDeck.add(deck2.get(2));
		twoPairDeck.add(deck2.get(22));
		System.out.println("*********** twoPairDeck aces*2,2,6*2");
		//twoPairDeck.sort();
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
		//twoPairDeck2.sort();
		System.out.println(twoPairDeck2.isTwoPair());
		HandOfCards twoPairDeck3 = new HandOfCards();
		twoPairDeck3.add(deck2.get(41));
		twoPairDeck3.add(deck2.get(40));
		twoPairDeck3.add(deck2.get(51));
		twoPairDeck3.add(deck2.get(50));
		twoPairDeck3.add(deck2.get(3));
		//System.out.println(twoPairDeck3.fullDeckToString());
		System.out.println("*********** twoPairDeck3 king*2,J*2,A");
		//twoPairDeck3.sort();
		System.out.println(twoPairDeck3.isTwoPair());

		HandOfCards highCardDeck = new HandOfCards();
		highCardDeck.add(deck2.get(0));
		highCardDeck.add(deck2.get(35));
		highCardDeck.add(deck2.get(30));
		highCardDeck.add(deck2.get(50));
		highCardDeck.add(deck2.get(9));
		//System.out.println(highCardDeck.fullDeckToString());
		System.out.println("*********** highCardDeck");
		//highCardDeck.sort();
		System.out.println(highCardDeck.isHighCard());
		highCardDeck.generateHandType();
		//System.out.println(highCardDeck.getBestHandTypeName());
		//System.out.println(highCardDeck.handTypeToBooleanStr());


		/*testing made hands against all checking methods to ensure they hold for that hand only
		**/
		System.out.println("\n*********************** test all hands");
		System.out.println("***** flushDeck");
		flushDeck.generateHandType();
		System.out.println(flushDeck.getBestHandTypeName());
		//System.out.println(flushDeck.handTypeToBooleanStr());
		//System.out.println(flushDeck.fullDeckToString());


		System.out.println("***** royalFlushDeck");
		royalFlushDeck.generateHandType();
		System.out.println(royalFlushDeck.getBestHandTypeName());
		//System.out.println(royalFlushDeck.handTypeToBooleanStr());
		//System.out.println(royalFlushDeck.fullDeckToString());



		System.out.println("***** straightFlushDeck");
		straightFlushDeck.generateHandType();
		System.out.println(straightFlushDeck.getBestHandTypeName());
		//System.out.println(straightFlushDeck.handTypeToBooleanStr());
		//System.out.println(straightFlushDeck.fullDeckToString());

		System.out.println("***** fourOfAKindDeck");
		fourOfAKindDeck.generateHandType();
		System.out.println(fourOfAKindDeck.getBestHandTypeName());
		//System.out.println(fourOfAKindDeck.handTypeToBooleanStr());

		System.out.println("***** fourOfAKindDeck2");
		fourOfAKindDeck2.generateHandType();
		System.out.println(fourOfAKindDeck2.getBestHandTypeName());
		//System.out.println(fourOfAKindDeck2.handTypeToBooleanStr());

		System.out.println("***** threeOfAKindDeck");
		threeOfAKindDeck.generateHandType();
		System.out.println(threeOfAKindDeck.getBestHandTypeName());
		//System.out.println(threeOfAKindDeck.handTypeToBooleanStr());

		System.out.println("***** threeOfAKindDeck2");
		threeOfAKindDeck2.generateHandType();
		System.out.println(threeOfAKindDeck2.getBestHandTypeName());
		//System.out.println(threeOfAKindDeck2.handTypeToBooleanStr());

		System.out.println("***** threeOfAKindDeck3");
		threeOfAKindDeck3.generateHandType();
		System.out.println(threeOfAKindDeck3.getBestHandTypeName());
		//System.out.println(threeOfAKindDeck3.handTypeToBooleanStr());

		System.out.println("***** fullHouseDeck");
		fullHouseDeck.generateHandType();
		System.out.println(fullHouseDeck.getBestHandTypeName());
		//System.out.println(fullHouseDeck.handTypeToBooleanStr());

		System.out.println("***** fullHouseDeck2");
		fullHouseDeck2.generateHandType();
		System.out.println(fullHouseDeck2.getBestHandTypeName());
		//System.out.println(fullHouseDeck2.handTypeToBooleanStr());

		System.out.println("***** royalFlushDeck");
		royalFlushDeck.generateHandType();
		System.out.println(royalFlushDeck.getBestHandTypeName());
		//System.out.println(royalFlushDeck.handTypeToBooleanStr());

		System.out.println("***** straightDeck");
		straightDeck.generateHandType();
		System.out.println(straightDeck.getBestHandTypeName());
		//System.out.println(straightDeck.handTypeToBooleanStr());

		System.out.println("***** pairDeck");
		pairDeck.generateHandType();
		System.out.println(pairDeck.getBestHandTypeName());
		//System.out.println(pairDeck.handTypeToBooleanStr());
		//System.out.println(pairDeck.fullDeckToString());
		System.out.println("***** pairDeck2");
		pairDeck2.generateHandType();
		System.out.println(pairDeck2.getBestHandTypeName());
		//System.out.println(pairDeck2.handTypeToBooleanStr());
		//System.out.println(pairDeck2.fullDeckToString());

		System.out.println("***** twoPairDeck");
		twoPairDeck.generateHandType();
		System.out.println(twoPairDeck.getBestHandTypeName());
		//System.out.println(twoPairDeck.handTypeToBooleanStr());
		System.out.println("***** twoPairDeck2");
		twoPairDeck2.generateHandType();
		System.out.println(twoPairDeck2.getBestHandTypeName());
		//System.out.println(twoPairDeck2.handTypeToBooleanStr());
		System.out.println("***** twoPairDeck3");
		twoPairDeck3.generateHandType();
		System.out.println(twoPairDeck3.getBestHandTypeName());
		//System.out.println(twoPairDeck3.handTypeToBooleanStr());

		System.out.println("***** highCardDeck");
		highCardDeck.generateHandType();
		System.out.println(highCardDeck.getBestHandTypeName());
		//System.out.println(highCardDeck.handTypeToBooleanStr());

		System.out.println("*********** spacial straight cases");

		HandOfCards straightDeck2 = new HandOfCards();
		straightDeck2.add(deck2.get(51));
		straightDeck2.add(deck2.get(40));
		straightDeck2.add(deck2.get(0));
		straightDeck2.add(deck2.get(37));
		straightDeck2.add(deck2.get(45));
		//System.out.println(straightDeck2.fullDeckToString());
		System.out.println("*********** straightDeck2 10c, jh, qc, kd, ah");
		//straightDeck2.sort();
		//System.out.println(straightDeck2.isStraight());
		//System.out.println(straightDeck2.fullDeckToString());
		straightDeck2.generateHandType();
		System.out.println(straightDeck2.getBestHandTypeName());

		HandOfCards royalFlushDeck2 = new HandOfCards();
		royalFlushDeck2.add(deck2.get(48));
		royalFlushDeck2.add(deck2.get(40));
		royalFlushDeck2.add(deck2.get(0));
		royalFlushDeck2.add(deck2.get(36));
		royalFlushDeck2.add(deck2.get(44));
		//System.out.println(royalFlushDeck2.fullDeckToString());
		System.out.println("*********** royalFlushDeck2 10h, jh, qh, kh, ah");
		//royalFlushDeck2.sort();
		//System.out.println(royalFlushDeck2.isRoyalFlush());
		//System.out.println(royalFlushDeck2.fullDeckToString());
		royalFlushDeck2.generateHandType();
		System.out.println(royalFlushDeck2.getBestHandTypeName());
		//System.out.println(royalFlushDeck2.fullDeckToString());



		System.out.println("*********** Testing card ranking scheme");
		System.out.println("royalFlushDeck: "+royalFlushDeck.getGameValue());
		System.out.println("royalFlushDeck2: "+royalFlushDeck2.getGameValue());
		System.out.println("flushDeck: "+flushDeck.getGameValue());
		System.out.println("straightFlushDeck: "+straightFlushDeck.getGameValue());
		System.out.println("fourOfAKindDeck: "+fourOfAKindDeck.getGameValue());
		System.out.println("fourOfAKindDeck2: "+fourOfAKindDeck2.getGameValue());
		System.out.println("fourOfAKindDeck3: "+threeOfAKindDeck.getGameValue());
		System.out.println("threeOfAKindDeck2: "+threeOfAKindDeck2.getGameValue());
		System.out.println("threeOfAKindDeck3: "+threeOfAKindDeck3.getGameValue());

		System.out.println("fullHouseDeck: "+fullHouseDeck.getGameValue());
		System.out.println("fullHouseDeck2: "+fullHouseDeck2.getGameValue());

		System.out.println("straightDeck: "+straightDeck.getGameValue());
		System.out.println("straightDeck2: "+straightDeck2.getGameValue());
		System.out.println("pairDeck: "+pairDeck.getGameValue());
		System.out.println("pairDeck2: "+pairDeck2.getGameValue());

		System.out.println("twoPairDeck: "+twoPairDeck.getGameValue());
		System.out.println("twoPairDeck2: "+twoPairDeck2.getGameValue());
		System.out.println("twoPairDeck3: "+twoPairDeck3.getGameValue());

		System.out.println("highCardDeck: "+highCardDeck.getGameValue());
		//System.out.println("highCardDeck: "+highCardDeck);


		//System.out.println(fourOfAKindDeck2.fullDeckToString());
		//System.out.println(threeOfAKindDeck2.fullDeckToString());

		HandOfCards pairDeck3 = new HandOfCards();
		pairDeck3.add(deck2.get(51));
		pairDeck3.add(deck2.get(30));
		pairDeck3.add(deck2.get(40));
		pairDeck3.add(deck2.get(2));
		pairDeck3.add(deck2.get(0));
		//System.out.println(pairDeck2.fullDeckToString());
		//System.out.println("*********** pairDeck3 a,A,8s,jh");
		//System.out.println(pairDeck3);
		pairDeck3.generateHandType();
		//System.out.println(pairDeck3);
		System.out.println(pairDeck3 + "\t" + pairDeck3.getBestHandTypeName() + "\t\tScore: " + pairDeck3.getGameValue());
		HandOfCards pairDeck4= new HandOfCards();
		pairDeck4.add(deck2.get(51));
		pairDeck4.add(deck2.get(30));
		pairDeck4.add(deck2.get(40));
		pairDeck4.add(deck2.get(6));
		pairDeck4.add(deck2.get(4));
		//System.out.println(pairDeck4.fullDeckToString());
		//System.out.println("*********** pairDeck4 2,2,8s,j, h");
		//System.out.println(pairDeck4);
		pairDeck4.generateHandType();
		//System.out.println(pairDeck4);
		System.out.println(pairDeck4 + "\t" + pairDeck4.getBestHandTypeName() + "\t\tScore: " + pairDeck4.getGameValue());
		//testing 2 pair scoring
		System.out.println(twoPairDeck2 + "\t" + twoPairDeck2.getBestHandTypeName() + "\t\tScore: " + twoPairDeck2.getGameValue());
		System.out.println(twoPairDeck3 + "\t" + twoPairDeck3.getBestHandTypeName() + "\t\tScore: " + twoPairDeck3.getGameValue());



	}
}
