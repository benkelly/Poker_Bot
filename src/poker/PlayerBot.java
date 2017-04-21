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

	public static String FACE_TELLS[] = {"\uD83D\uDE00", "\uD83D\uDE2C", "\uD83D\uDE01", "\uD83D\uDE02", "\uD83D\uDE03", "\uD83D\uDE04", "\uD83D\uDE05", "\uD83D\uDE06", "\uD83D\uDE07", "\uD83D\uDE09", "\uD83D\uDE0A", "\uD83D\uDE42", "\uD83D\uDE43",
			"☺️", "\uD83D\uDE0B", "\uD83D\uDE0C", "\uD83D\uDE0D", "\uD83D\uDE18", "\uD83D\uDE17",
			"\uD83D\uDE19", "\uD83D\uDE1A", "\uD83D\uDE1C", "\uD83D\uDE1D", "\uD83D\uDE1B",
			"\uD83E\uDD11", "\uD83E\uDD13", "\uD83D\uDE0E", "\uD83E\uDD17", "\uD83D\uDE0F",
			"\uD83D\uDE36", "\uD83D\uDE10", "\uD83D\uDE11", "\uD83D\uDE12", "\uD83D\uDE44", "\uD83E\uDD14",
			"\uD83D\uDE33", "\uD83D\uDE1E", "\uD83D\uDE1F", "\uD83D\uDE20", "\uD83D\uDE21", "\uD83D\uDE14",
			"\uD83D\uDE15", "\uD83D\uDE41", "☹️", "\uD83D\uDE23", "\uD83D\uDE16", "\uD83D\uDE2B", "\uD83D\uDE29",
			"\uD83D\uDE24", "\uD83D\uDE2E", "\uD83D\uDE31", "\uD83D\uDE28", "\uD83D\uDE30", "\uD83D\uDE2F", "\uD83D\uDE26",
			"\uD83D\uDE27", "\uD83D\uDE22", "\uD83D\uDE25", "\uD83D\uDE2A", "\uD83D\uDE13", "\uD83D\uDE2D", "\uD83D\uDE35",
			"\uD83D\uDE32", "\uD83E\uDD10", "\uD83D\uDE37", "\uD83E\uDD12", "\uD83E\uDD15", "\uD83D\uDE34", "\uD83D\uDCA9",
			"\uD83D\uDE08", "\uD83D\uDC7D", "\uD83E\uDD16"};

	// bot stats
	private int botAgressrion;
	private int botIntellagence;

	public PlayerBot(PokerGame game, DeckOfCards deck, int chips) {
		super("", game, deck, chips, false);
		playerName = generateName();
		generateBotStats();
		System.out.println(getPlayerName()+": botAgressrion: "+botAgressrion+": botIntellagence: "+botIntellagence);
	}

	private void generateBotStats() {
		Random rand = new Random();
		int value = rand.nextInt(4);
		switch (value) {
			case 0: {
				botAgressrion = 100;
				botIntellagence = 50;
				break;
			}
			case 1: {
				botAgressrion = 40;
				botIntellagence = 80;
				break;
			}
			case 2: {
				botAgressrion = 50;
				botIntellagence = 39;
				break;
			}
			case 3: {
				botAgressrion = 20;
				botIntellagence = 20;
				break;
			}
		}
		//System.out.println(getPlayerName()+": generateBotStats VALUE: "+value);
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



	//methods from pokerPlayer needed to override to bot
	public boolean playersHandOptions() {
		int discardNumber = 0;
		if (botIntellagence >= 40 | botAgressrion >=60) {
			int tempHandScore = getCurrentHandScore();
			// the lower score more cards to discard.
			for (int i = 0; i < hand.ROYAL_FLUSH_WEIGHT; i+=1000000) {
				if (i+1 <= tempHandScore && tempHandScore <= i+333333) {
					discardNumber = 3;
					System.out.println(getPlayerName()+": discardNumber: "+discardNumber);
				}
				if (i+333334 <= tempHandScore && tempHandScore <= i+666666) {
					discardNumber = 2;
					System.out.println(getPlayerName()+": discardNumber: "+discardNumber);

				}
				if (i+666666 <= tempHandScore && tempHandScore <= i+999999) {
					discardNumber = 1;
					System.out.println(getPlayerName()+": discardNumber: "+discardNumber);

				}
			}
			hand.generateHandType();
			discard(discardNumber);
			getNewCardsForHand();
			return true;
		}
		if(botIntellagence < 39 | botAgressrion < 59) {
			Random rand = new Random();
			int value = rand.nextInt(2);
			switch (value) {
				case 0: { // discard at random
					Random disNum = new Random();
					discardNumber = disNum.nextInt(2) + 1;
					hand.generateHandType();
					discard(discardNumber);
					getNewCardsForHand();
					System.out.println(getPlayerName()+": RANDODM COINFLIPP ONE discardNumber: "+discardNumber);
					return true;
				}
				case 1: { // keep
					break;
				}
			}
		}
		else { // keep
			System.out.println(getPlayerName()+": KEEP: ");
			return true;
		}
		return false;
	}

	public boolean playersBettingOptions() {
		System.out.println(getPlayerName()+": playersBettingOptions: START");

		if (botIntellagence >= 40 | botAgressrion >=60) {
			System.out.println(getPlayerName()+": playersBettingOptions: botIntellagence >= 40 | botAgressrion >=60)");

			hand.generateHandType();
			int tempHandScore = getCurrentHandScore();
			for (int i = 0; i < hand.ROYAL_FLUSH_WEIGHT; i += 1000000) {
				if (i + 1 <= tempHandScore && tempHandScore <= i + 333333) {
					Random rand = new Random();
					int value = rand.nextInt(3);
					switch (value) {
						case 0: {
							if(botIntellagence < 39 | botAgressrion < 59) {
								foldFromRound();
								return true;
							}
							else
								payCurrentStake();
							return true;
						}
						case 1: {
							payCurrentStake();
							return true;
						}
						case 2: { // increaseStake
							int riseStakeAmount = pokerGame.getCurrentRoundsStakeAmount();
							riseStakeAmount += (pokerGame.getCurrentRoundsStakeAmount() * (botAgressrion / 100));
							if (riseStakeAmount <= playerChipAmount) {
								increaseStake(riseStakeAmount);
								System.out.println(getPlayerName() + ": increaseStake: " + riseStakeAmount);
							} else {
								payCurrentStake();
								System.out.println(getPlayerName() + ": increaseStake BUT UNABLE TO: " + riseStakeAmount);
							}
							return true;
						}
					}
				}
				if (i + 333334 <= tempHandScore && tempHandScore <= i + 999999) {
					System.out.println(getPlayerName()+": playersBettingOptions: (i + 333334 <= tempHandScore && tempHandScore <= i + 999999)");
					Random rand = new Random();
					int value = rand.nextInt(3);
					switch (value) {
						case 0: {
							payCurrentStake();
							return true;
						}
						case 1: { // increaseStake
							int riseStakeAmount = pokerGame.getCurrentRoundsStakeAmount();
							riseStakeAmount += (pokerGame.getCurrentRoundsStakeAmount() * (botAgressrion / 100));
							if (riseStakeAmount <= playerChipAmount) {
								System.out.println(getPlayerName() + ": increaseStake: " + riseStakeAmount);
								increaseStake(riseStakeAmount);
							} else {
								payCurrentStake();
								System.out.println(getPlayerName() + ": increaseStake BUT UNABLE TO : " + riseStakeAmount);
							}
							return true;
						}
						case 2: { // increaseStake ALL-in
							increaseStake(playerChipAmount);
							System.out.println(getPlayerName() + ": ALL-IN: " + playerChipAmount);
							return true;
						}
					}
				}
			}
		}
		foldFromRound();
		System.out.println(getPlayerName() + ": playersBettingOptions FOLD FROM ROUND");
		return true;
	}

	synchronized public boolean reRaiseStake(int stakeIncrease) {
		System.out.println(getPlayerName()+": reRaiseStake: START");
		if (botIntellagence >= 40 | botAgressrion >=60) {
			if (stakeIncrease <= currentStakePaid / 2) {
				pokerGame.addToCurrentRoundsHeldStake(stakeIncrease);
				playerChipAmount -= stakeIncrease;
				currentStakePaid = pokerGame.getCurrentRoundsHeldStake();
				return true;
			}
			if (stakeIncrease > currentStakePaid / 2) {
				if (botIntellagence >= 50 & botAgressrion >=50) {
					payReRaiseStake(stakeIncrease);
				}
			}
		}
		else{
			Random rand = new Random();
			int value = rand.nextInt(2);
			switch (value) {
				case 0: {
					payReRaiseStake(stakeIncrease);
				}
				case 2: {
					return false;
				}
			}
		}
		return false;
	}


	/*public void payAnteFee(int anteFee, boolean noUserInputForRound) {

	}
*/

	/*Class testing method
		* */
	public static void main(String[] args) {
		System.out.println("poker.PlayerBot.java!");
		PlayerBot pb = new PlayerBot(PokerGame.getInstance(), DeckOfCards.getInstance(), 3000);
		System.out.println(pb.getPlayerName());
		System.out.println(pb.getPlayerChipAmount());

		visualHand.TweetVisualHand(pb.hand, pb.getPlayerChipAmount(), pb.FACE_TELLS[3]);
	}
}
