package poker;

/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */
public class PlayingCard {

	private char cardType;
	private char cardSuit;
	private int faceValue;
	private int gameValue;

	static public final char HEARTS = 'H';
	static public final char CLUBS = 'C';
	static public final char SPADES = 'S';
	static public final char DIAMONDS = 'D';

	/*Card object holds Type, Suit, Face value and Games values
	* */
	public PlayingCard(char type, char suit, int face, int game) {
		cardType = type;
		cardSuit = suit;
		faceValue = face;
		gameValue = game;

	}
	/*returns string of short name of card,
	i.e. AC, 7H, KD...
	* */
	public String toString(){
		return ""+this.cardType+this.cardSuit+"";
	}
	/*returns cards Face value as int.
	* */
	public int faceValue(){
		return this.faceValue;
	}
	/*returns cards Game value as int.
	* */
	public int gameValue(){
		return this.gameValue;
	}
	/*returns string of cards type
	* */
	public String cardType(){
		switch(this.cardType) {
			case 'A': return "Ace";
			case '1': return "One";
			case '2': return "Two";
			case '3': return "Three";
			case '4': return "Four";
			case '5': return "Five";
			case '6': return "Six";
			case '7': return "Seven";
			case '8': return "Eight";
			case '9': return "Nine";
			case '0': return "Ten";
			case 'J': return "Jack";
			case 'Q': return "Queen";
			case 'K': return "King";
		}
		return "";
	}
	/*returns string of cards Suit
	* */
	public String cardSuit(){
		switch(this.cardSuit) {
			case 'H': return "Hearts";
			case 'C': return "Clubs";
			case 'S': return "Spades";
			case 'D': return "Diamonds";
		}
		return "";
	}

	/*returns string of cards Suit Symbol ♥, ♣, ♠, ♦.
	* */
	public String getSuitSymbol(){
		switch(this.cardSuit) {
			case 'H': return "♥";
			case 'C': return "♣";
			case 'S': return "♠";
			case 'D': return "♦";
		}
		return "";
	}
	/*returns string of full name of card.
	* */
	public String getFullName(){
		return this.cardType()+" of "+this.cardSuit();
	}

	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.PlayingCard.java!");
		// testing object methods.
		PlayingCard aceOfHearts = new PlayingCard('A', PlayingCard.HEARTS, 1, 14);
		System.out.println(aceOfHearts.getFullName());
		System.out.println(aceOfHearts.toString());
		System.out.println(aceOfHearts.faceValue());
		System.out.println(aceOfHearts.gameValue());
		System.out.println(aceOfHearts.getSuitSymbol());

	}
}
