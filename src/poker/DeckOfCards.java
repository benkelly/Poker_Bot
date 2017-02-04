package poker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjamin kelly on 02/02/2017.
 * benjamin.kelly.1@ucdconnect.ie
 * student number: 14700869

 */
public class DeckOfCards {

	private int[] faceValue = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	private int[] gameValue = {14, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	private char[] suitType = {'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'};

	public DeckOfCards() {

	}


	void reset() {
		List<PlayingCard> deckList = new ArrayList<PlayingCard>();
		for (int i = 0; i < 13; i++) {
			deckList.add(new PlayingCard(suitType[i], PlayingCard.HEARTS, faceValue[i], gameValue[i]));
			deckList.add(new PlayingCard(suitType[i], PlayingCard.CLUBS, faceValue[i], gameValue[i]));
			deckList.add(new PlayingCard(suitType[i], PlayingCard.SPADES, faceValue[i], gameValue[i]));
			deckList.add(new PlayingCard(suitType[i], PlayingCard.DIAMONDS, faceValue[i], gameValue[i]));
		}
	}
	String fullDeckToString() {
		String strList = "";
		for(int i=0; i<52; i++) {
			strList = strList + this.get(i).toString()+ this.get(i).getFullName() +"\n";
		}
		return strList;
	}





	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.DeckOfCards.java!");
		DeckOfCards testDeck = new DeckOfCards();
		testDeck.reset();
		System.out.println(testDeck.fullDeckToString());
	}
}





/*
	*/
/*Currently testing creating deck array, most of these code will be in DeckofCards.java
* *//*

	PlayingCard[] deck = new PlayingCard[52];
	int[] faceValue = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	int[] gameValue = {14, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
	char[] suitType = {'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'};
	int j=0;
	for(int i=0; i<13; i++){
		deck[j] = new PlayingCard(suitType[i], PlayingCard.HEARTS, faceValue[i], gameValue[i]);
		j++;
		deck[j] = new PlayingCard(suitType[i], PlayingCard.CLUBS, faceValue[i], gameValue[i]);
		j++;
		deck[j] = new PlayingCard(suitType[i], PlayingCard.SPADES, faceValue[i], gameValue[i]);
		j++;
		deck[j] = new PlayingCard(suitType[i], PlayingCard.DIAMONDS, faceValue[i], gameValue[i]);
		j++;
	}

	String str = "";    // testing short name to string
	String nameStr = ""; // testing full name to string
	for(int i=0; i<52; i++) {
		str = str + deck[i] + "\n";
		nameStr = nameStr + deck[i].getFullName() + "\n";
	}
	System.out.println(str);
	System.out.println(nameStr);


*/
/*
        // This method is using the deck as an arraylist
        //    ~ was testing weather an array or arraylist would suite the task more.

        List<PlayingCard> deckList = new ArrayList<PlayingCard>();
        for(int i=0; i<13; i++){
            deckList.add(new PlayingCard(suitType[i], PlayingCard.HEARTS, faceValue[i], gameValue[i]));
            deckList.add(new PlayingCard(suitType[i], PlayingCard.CLUBS, faceValue[i], gameValue[i]));
            deckList.add(new PlayingCard(suitType[i], PlayingCard.SPADES, faceValue[i], gameValue[i]));
            deckList.add(new PlayingCard(suitType[i], PlayingCard.DIAMONDS, faceValue[i], gameValue[i]));
        }
        String strList = "";
        for (PlayingCard object: deckList) {
            strList= deckList.toString();
        }
        System.out.println(strList);
*/
