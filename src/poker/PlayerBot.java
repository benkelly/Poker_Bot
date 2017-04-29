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
	private static final String FIRST_NAMES_LIST = "resources/short_first_names_list.txt";
	//private static final String FIRST_NAMES_LIST = "resources/firstNames.txt";
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
	private int aggression;
	private int intelligence;


	//add a new construct function.
	public PlayerBot(String name, PokerGame game, DeckOfCards deck, int chips) {
		super(name, game, deck, chips, false);
		//playerName = generateName();
		generateBotStats();
		System.out.println(getPlayerName() + ": aggression: " + aggression + ": intelligence: " + intelligence);
	}

	public PlayerBot(PokerGame game, DeckOfCards deck, int chips) {
		super("", game, deck, chips, false);
		playerName = generateName();
		generateBotStats();
		System.out.println(getPlayerName() + ": aggression: " + aggression + ": intelligence: " + intelligence);
	}

	public static String faceTellGenerator() {
		Random rand = new Random();
		int value = rand.nextInt(FACE_TELLS.length);
		return FACE_TELLS[value];
	}


	private void generateBotStats() {
		Random rand = new Random();
		aggression = rand.nextInt(100);
		intelligence = rand.nextInt(100);
		if (intelligence > 50) {
			aggression = rand.nextInt(65);
		} else {
			aggression = rand.nextInt(100);
		}
		/*int value = rand.nextInt(4);
				switch (value) {
					case 0: {
						aggression = 100;
						intelligence = 50;
						break;
					}
					case 1: {
						aggression = 40;
						intelligence = 80;
						break;
					}
					case 2: {
				aggression = 50;
				intelligence = 39;
				break;
			}
			case 3: {
				aggression = 20;
				intelligence = 20;
				break;
			}
		}*/
		//System.out.println(getPlayerName()+": generateBotStats VALUE: "+value);
	}

	/*returns string of a random name generated form a list of names.
	* */
	private String generateName() {
		String randomFirstName = getNameFromFile(FIRST_NAMES_LIST);
		//String randomLastName = getNameFromFile(LAST_NAMES_LIST);
		//return randomFirstName+" "+randomLastName;
		return randomFirstName;
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


	public void sendPlayerHandOptions() {
		//robots don't have twitter :(
	}

	public void sendPlayersBettingOptions() {
		//robots don't have twitter :(
	}

	//methods from pokerPlayer needed to override to bot
	public boolean playersHandOptions(String inputStr) {
		int discardNumber = 0;
		if (intelligence >= 40 | aggression >= 60) {
			int tempHandScore = getCurrentHandScore();
			// the lower score more cards to discard.
			for (int i = 0; i < hand.ROYAL_FLUSH_WEIGHT; i += 1000000) {
				if (i + 1 <= tempHandScore && tempHandScore <= i + 333333) {
					discardNumber = 3;
					System.out.println(getPlayerName() + ": discardNumber: " + discardNumber);
					//pokerGame.tweetStr +=getPlayerName()+" discarded "+discardNumber+" cards\n";
					pokerGame.tweetStr += getPlayerName() + " discarded " + discardNumber + " \uD83C\uDFB4\n";
				}
				if (i + 333334 <= tempHandScore && tempHandScore <= i + 666666) {
					discardNumber = 2;
					System.out.println(getPlayerName() + ": discardNumber: " + discardNumber);
					//pokerGame.tweetStr +=getPlayerName()+" discarded "+discardNumber+" cards\n";
					pokerGame.tweetStr += getPlayerName() + " discarded " + discardNumber + " \uD83C\uDFB4\n";

				}
				if (i + 666666 <= tempHandScore && tempHandScore <= i + 999999) {
					discardNumber = 1;
					System.out.println(getPlayerName() + ": discardNumber: " + discardNumber);
					//pokerGame.tweetStr +=getPlayerName()+" discarded "+discardNumber+" card\n";
					pokerGame.tweetStr += getPlayerName() + " discarded " + discardNumber + " \uD83C\uDFB4\n";


				}
			}
			hand.generateHandType();
			discard(discardNumber);
			getNewCardsForHand();
			return true;
		}
		if (intelligence < 39 | aggression < 59) {
			Random rand = new Random();
			int value = rand.nextInt(2);
			switch (value) {
				case 0: { // discard at random
					Random disNum = new Random();
					discardNumber = disNum.nextInt(2) + 1;
					hand.generateHandType();
					discard(discardNumber);
					getNewCardsForHand();
					System.out.println(getPlayerName() + ": RANDODM COINFLIP ONE discardNumber: " + discardNumber);
					if (discardNumber == 1) {
						//pokerGame.tweetStr +=getPlayerName()+" discarded "+discardNumber+" card\n";
						pokerGame.tweetStr += getPlayerName() + " discarded " + discardNumber + " \uD83C\uDFB4\n";
					} else
						//pokerGame.tweetStr +=getPlayerName()+" discarded "+discardNumber+" cards\n";
						pokerGame.tweetStr += getPlayerName() + " discarded " + discardNumber + " \uD83C\uDFB4\n";
					return true;
				}
				case 1: { // keep
					//System.out.println(getPlayerName()+": KEEP: ");
					//pokerGame.tweetStr +=getPlayerName()+": Kept: \n";
					System.out.println(getPlayerName() + ": KEEP: ");
					pokerGame.tweetStr += getPlayerName() + " Kept\n";
					return true;
				}
			}
		} else { // keep
			System.out.println(getPlayerName() + ": KEEP: ");
			pokerGame.tweetStr += getPlayerName() + " Kept\n";
			return true;
		}
		return false;
	}

	public boolean playersBettingOptions(String inputStr) {
		System.out.println(getPlayerName() + ": playersBettingOptions: START");

		int raiseAmount = 0, action = 0;
		if (intelligence >= 40 | aggression >= 60) {
			System.out.println(getPlayerName() + ": playersBettingOptions: intelligence >= 40 | aggression >=60)");

			hand.generateHandType();
			int tempHandScore = getCurrentHandScore();
			int handScore = getCurrentHandScore();
			Random rand = new Random();
			if (handScore < hand.PAIR_WEIGHT) {
				int value = rand.nextInt(2);
				switch (value) {
					case 0: {
						fold();
						action = 0;
					}
					case 1: {
						if (aggression > 50) {
							call();
							action = 1;
						} else {
							fold();
							action = 0;
						}

					}
				}
			} else if (handScore > hand.PAIR_WEIGHT && handScore < hand.TWO_PAIR_WEIGHT) {
				int value = rand.nextInt(3);
				switch (value) {
					case 0: {
						if (aggression < 25) {
							fold();
							action = 0;
						}
						call();
						action = 1;
					}
					case 1: {
						if (aggression > 50) {
							raiseAmount = 50;
							raise(raiseAmount);
							action = 2;
						} else {
							call();
							action = 1;
						}

					}
					case 2: {
						call();
						action = 1;
					}
				}
			} else if (handScore > hand.TWO_PAIR_WEIGHT && handScore < hand.STRAIGHT_WEIGHT) {
				int value = rand.nextInt(3);
				switch (value) {
					case 0: {
						if (aggression < 25) {
							call();
							action = 1;
						}
						raiseAmount = 50;
						raise(raiseAmount);
						action = 2;
					}
					case 1: {
						if (aggression > 50) {
							raiseAmount = 50;
							raise(raiseAmount);
						} else {
							raiseAmount = 50;
							raise(raiseAmount);
							action = 2;
						}

					}
					case 2: {
						if (aggression > 50) {
							raiseAmount = 50;
							raise(raiseAmount);
							action = 2;
						} else {
							call();
							action = 1;
						}
					}
				}
			} else if (handScore > hand.STRAIGHT_WEIGHT && handScore < hand.FLUSH_WEIGHT) {
				int value = rand.nextInt(3);
				switch (value) {
					case 0: {
						if (aggression < 20) {
							call();
							action = 1;
						}
						raiseAmount = 50;
						raise(raiseAmount);
						action = 2;
					}
					case 1: {
						raiseAmount = 50;
						raise(raiseAmount);
						action = 2;
					}
					case 2: {
						if (aggression > 40) {
							raiseAmount = 50;
							raise(raiseAmount);
							action = 2;
						} else {
							call();
							action = 1;
						}
					}
				}
			} else if (handScore > hand.PAIR_WEIGHT && handScore < hand.TWO_PAIR_WEIGHT) {
				call();
			} else if (handScore > hand.TWO_PAIR_WEIGHT && handScore < hand.THREE_OF_A_KIND_WEIGHT) {
				call();
			} else if (handScore > hand.THREE_OF_A_KIND_WEIGHT && handScore < hand.STRAIGHT_WEIGHT) {
				call();
			} else if (handScore > hand.STRAIGHT_WEIGHT && handScore < hand.FLUSH_WEIGHT) {
				call();
			} else if (handScore > hand.FLUSH_WEIGHT && handScore < hand.FULL_HOUSE_WEIGHT) {
				call();

			} else if (handScore > hand.FULL_HOUSE_WEIGHT && handScore < hand.FOUR_OF_A_KIND_WEIGHT) {
				call();
			} else if (handScore > hand.FOUR_OF_A_KIND_WEIGHT && handScore < hand.STRAIGHT_FLUSH_WEIGHT) {
				call();
			} else if (handScore > hand.STRAIGHT_FLUSH_WEIGHT) {
				int value = rand.nextInt(2);
				switch (value) {
					case 0: {
						raiseAmount = 50;
						raise(raiseAmount);
						action = 2;
					}
					case 1: {
						if (aggression > 50) {
							raiseAmount = 50;
							raise(raiseAmount);
							action = 2;
						} else {
							raiseAmount = 50;
							raise(raiseAmount);
							action = 2;
						}
					}
					case 2: {
						if (intelligence < 30) {//all in
							raiseAmount = this.playerChipAmount;
							raise(raiseAmount);
							action = 2;
						}
					}
				}
			}


		}
		switch (action) {
			case 0: {
				pokerGame.tweetStr += faceTellGenerator() + " " + getPlayerName() + " folded\n";
			}
			case 1: {
				pokerGame.tweetStr += faceTellGenerator() + " " + getPlayerName() + " called\n";
			}
			case 2: {
				pokerGame.tweetStr += faceTellGenerator() + " " + getPlayerName() + " raised by " + raiseAmount + "\n";
			}
		}
		return true;
	}


				/*
					int value = rand.nextInt(3);
					switch (value) {
						case 0: {
							if(intelligence < 39 | aggression < 59) {
								fold();
								pokerGame.tweetStr +=getPlayerName()+" folded\n";
								return true;
							}
							else
								call();
								pokerGame.tweetStr +=getPlayerName()+" called\n";
								return true;
						}
						case 1: {
							call();
							pokerGame.tweetStr +=getPlayerName()+" called\n";
							return true;
						}
						case 2: { // raise(raiseAmount)
							int riseStakeAmount = pokerGame.getCurrentRoundsStakeAmount();
							riseStakeAmount += ( riseStakeAmount * (aggression / 100));
							if (riseStakeAmount <= playerChipAmount) {
								raise(raiseAmount)(riseStakeAmount);
								System.out.println(getPlayerName() + ": raise(raiseAmount): " + riseStakeAmount);
								pokerGame.tweetStr +=getPlayerName()+" raise(raiseAmount)d to "+riseStakeAmount+"\n";
							} else {
								call();
								pokerGame.tweetStr +=getPlayerName()+" called\n";
								System.out.println(getPlayerName() + ": raise(raiseAmount) BUT UNABLE TO: " + riseStakeAmount);
								System.out.println(getPlayerName()+" THEREFOR called\n");

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
							call();
							pokerGame.tweetStr +=getPlayerName()+" called\n";
							return true;
						}
						case 1: { // raise(raiseAmount)
							int riseStakeAmount = pokerGame.getCurrentRoundsStakeAmount();
							riseStakeAmount += (riseStakeAmount * (aggression / 100));
							if (riseStakeAmount <= playerChipAmount) {
								System.out.println(getPlayerName() + ": raise(raiseAmount): " + riseStakeAmount);
								raise(raiseAmount)(riseStakeAmount);
								pokerGame.tweetStr +=getPlayerName()+" raise(raiseAmount)d to "+riseStakeAmount+"\n";
							} else {
								call();
								System.out.println(getPlayerName() + ": raise(raiseAmount) BUT UNABLE TO : " + riseStakeAmount);
								System.out.println(getPlayerName()+" THEREFOR called\n");
								pokerGame.tweetStr +=getPlayerName()+" called\n";
							}
							return true;
						}
						case 2: { // raise(raiseAmount) ALL-in
							System.out.println(getPlayerName() + " went ALL-IN w/ " + playerChipAmount);
							pokerGame.tweetStr +=getPlayerName() + ": ALL-IN: " + playerChipAmount+"\n";
							raise(raiseAmount)(playerChipAmount);
							return true;
						}
					}
				}
			}
			*/

	synchronized public boolean reraiseStake(int stakeIncrease) {
		System.out.println(getPlayerName()+": reraise(raiseAmount)Stake: START");
		if (intelligence >= 40 | aggression >=60) {
			if (stakeIncrease <= currentStakePaid / 2) {
				botPayReraiseStake(stakeIncrease);
			}
			if (stakeIncrease > currentStakePaid / 2) {
				if (intelligence >= 50 & aggression >= 50) {
					botPayReraiseStake(stakeIncrease);
				}
			}
		}
		else {
			Random rand = new Random();
			int value = rand.nextInt(2);
			switch (value) {
				case 0: {
					botPayReraiseStake(stakeIncrease);
				}
				case 2: {
					pokerGame.tweetStr += getPlayerName() + " folded\n";
					return false;
				}
			}
		}
		pokerGame.tweetStr += getPlayerName() + " folded\n";
		return false;
	}

	private boolean botPayReraiseStake(int stakeIncrease) {
		if (payReRaiseStake(stakeIncrease)) {
			pokerGame.tweetStr += getPlayerName() + " called raise(raiseAmount)\n";
			return true;
		}
		else {
			pokerGame.tweetStr += getPlayerName() + " folded\n";
			return false;
		}
	}


	/*Class testing method
		* */
	public static void main(String[] args) {
		System.out.println("poker.PlayerBot.java!");
		/*PlayerBot pb = new PlayerBot(PokerGame.getInstance(), DeckOfCards.getInstance(), 3000);
		System.out.println(pb.getPlayerName());
		System.out.println(pb.getPlayerChipAmount());

		VisualHand.TweetVisualHand(pb.hand, pb.getPlayerChipAmount(), pb.FACE_TELLS[3]);
	*/}
}
