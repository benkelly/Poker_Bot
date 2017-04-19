package poker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */
public class PlayerBot extends PokerPlayer {

	// fixed stats
	private static final String FIRST_NAMES_LIST = "resources/firstNames.txt";
	private static final String LAST_NAMES_LIST = "resources/lastNames.txt";


	public PlayerBot(PokerGame game, DeckOfCards deck, int chips) {
		super("", game, deck, chips, false);
		playerName = generateName();
	}

	/*returns string of a random name generated form a list of names.
	* */
	private String generateName() {
		String randomFirstName = getNameFromFile(FIRST_NAMES_LIST);
		String randomLastName = getNameFromFile(LAST_NAMES_LIST);
		return randomFirstName+" "+randomLastName;
	}
	/*used in generateName()
	* */
	private String getNameFromFile(String inputFile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> lines = new ArrayList<String>();
		while (line != null) {
			lines.add(line);
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Random r = new Random();
		return lines.get(r.nextInt(lines.size()));
	}


	/*Class testing method
		* */
	public static void main(String[] args) {
		System.out.println("poker.PlayerBot.java!");

		PlayerBot pb = new PlayerBot(PokerGame.getInstance(), DeckOfCards.getInstance(), 3000);
		System.out.println(pb.getPlayerName());
		System.out.println(pb.getPlayerChipAmount());
	}
}
