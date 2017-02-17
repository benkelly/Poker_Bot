package poker;

/**
 * Created by benjamin kelly on 27/01/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869
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
		String typeStr = "";
		switch(this.cardType) {
			case 'A':
				typeStr = "Ace";
				break;
			case '1':
				typeStr = "One";
				break;
			case '2':
				typeStr = "Two";
				break;
			case '3':
				typeStr = "Three";
				break;
			case '4':
				typeStr = "Four";
				break;
			case '5':
				typeStr = "Five";
				break;
			case '6':
				typeStr = "Six";
				break;
			case '7':
				typeStr = "Seven";
				break;
			case '8':
				typeStr = "Eight";
				break;
			case '9':
				typeStr = "Nine";
				break;
			case '0':
				typeStr = "Ten";
				break;
			case 'J':
				typeStr = "Jack";
				break;
			case 'Q':
				typeStr = "Queen";
				break;
			case 'K':
				typeStr = "King";
				break;
		}
		return typeStr;
	}
	/*returns string of cards Suit
	* */
	public String cardSuit(){
		String suitStr = "";
		switch(this.cardSuit) {
			case 'H':
				suitStr = "Hearts";
				break;
			case 'C':
				suitStr = "Clubs";
				break;
			case 'S':
				suitStr = "Spades";
				break;
			case 'D':
				suitStr = "Diamonds";
				break;
		}
		return suitStr;
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
