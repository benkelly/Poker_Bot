package poker;

import java.util.ArrayList;

/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */

public class PokerGame extends ArrayList<PokerPlayer> {
	private static PokerGame instance;

	// fixed game stats
	private static final int MAX_BOTS = 3;
	private static final int INITIAL_CHIP_AMOUNT = 3000;

	// game stats
	private DeckOfCards gameDeck;
	private boolean gameOver = false;
	private int roundCount = 0;

	// current round variables
	private int currentRoundsAnteAmount = 100; // the current round ante, entering fee
	private int currentRoundsStakeAmount = currentRoundsAnteAmount; // the current round cost
	private int currentRoundsHeldStake = 0; // the current pot (the ships on the table)
	private ArrayList<PokerPlayer> curRoundPlayerList = new ArrayList<PokerPlayer>(); // player playing that current hand of poker


	public PokerGame() {
		setPokerTable(); // adds humand and bots to poker table

		currentRoundPlayerOptions(); // gets players round inputs


		getRoundWinner();

	}
	public static PokerGame getInstance() {
		if (instance == null) { instance = new PokerGame();}
		return instance;
	}

	/*adds poker players to game.
	* */
	private void setPokerTable() {
		this.add(new PokerPlayer("human",this, gameDeck, INITIAL_CHIP_AMOUNT)); // add human user.
		// add bots
		for (int j = 0; j < MAX_BOTS; j++) {
			this.add(new PlayerBot(this, gameDeck, INITIAL_CHIP_AMOUNT));
		}
	}

	/*allows users to input their hand changes and betting options.
	* */
	private void currentRoundPlayerOptions() {
		payAnteFee(currentRoundsAnteAmount); // players pay their ante to enter game.

		for (PokerPlayer player : this) { // players choose card discard options
			while(player.playersHandOptions()==false){}
		}

		for (PokerPlayer player : this) { // players choose their betting options
			while(player.playersBettingOptions()==false){}
		}

	}

	/*player will pay enter fee if unable then bankrupted
	* */
	synchronized private void payAnteFee(int AnteFee) {
		for (PokerPlayer player : this) {
			player.payAnteFee(AnteFee);
		}
	}

	/*removes pokerPlayer at that list location.
	* ~ checks if human player and if so gameOver = true;
	* */
	synchronized public void playerIsBankrupted(PokerPlayer player) {
		if( this.get(0).getPlayerName().equals(player.getPlayerName())) {
			gameOver = true;
			System.out.println(this.get(0).getPlayerName()+" is bankrupt.... game over dude....");
		}
		else {
			for (PlayingCard card : player.hand) {
				gameDeck.returnCard(card);
			}
			this.remove(player);
			System.out.println(player.getPlayerName() + " is bankrupt!");
		}
	}
	synchronized private void currentRoundAllTurnOverCards() {
		getRoundWinner();
	}



	synchronized private void getRoundWinner() {
		PokerPlayer temp = calcPlayerHandScores();
		temp.receivesStake(currentRoundsHeldStake);
		temp.totalRoundsWon += 1;
		System.out.println(temp.getPlayerName()+" is the winner!");
		System.out.println(temp.getPlayerName()+": new Chips amount: "+temp.getPlayerChipAmount());
		System.out.println("ships now on table: "+getCurrentRoundsHeldStake());

	}
	/*Returns the pokerPlayer with the highest score.
	* */
	synchronized private PokerPlayer calcPlayerHandScores() {
		int tempHighScore = 0;
		int tempHighScorePlayerIndex = 0;
		int index = 0;
		for (PokerPlayer player : curRoundPlayerList) {
			if (tempHighScore < player.getCurrentHandScore()) { // re-calcs all players current scores.
				tempHighScore = player.getCurrentHandScore();
				tempHighScorePlayerIndex = index;
			}
			index++;
		}
		return this.get(tempHighScorePlayerIndex);
	}

	private void resetRound() {
		currentRoundsStakeAmount = 0;
		currentRoundsHeldStake = 0;
		curRoundPlayerList.clear();
	}

	synchronized public void addToCurRoundPlayerList(PokerPlayer temp) {
		curRoundPlayerList.add(temp);
	}

	synchronized public int curRoundPlayerFolds(PokerPlayer temp) {
		if(curRoundPlayerList.contains(temp)) {
			//System.out.println("curRoundPlayerList contain: "+temp.getPlayerName());
			curRoundPlayerList.remove(temp);
			}
		return 0;
	}

	synchronized public void matchStakeIncrease(int stakeIncrease) {
		//System.out.println("matchStakeIncrease START");
		ArrayList<PokerPlayer> foldingList = new ArrayList<PokerPlayer>(); // players going to fold

		for (PokerPlayer object : curRoundPlayerList) {
			if( object.reRaiseStake(stakeIncrease) == false ) { // if player chooses fold add to list
				foldingList.add(object);
			}
		}
		for (PokerPlayer object : foldingList) {
			object.foldFromRound();
		}
	}

	/*returns bool statement the game continue to next round or not.
	* */
	public boolean isGameOver() {
		return gameOver;
	}

	/*returns int of current round.
	* */
	public int getRoundCount() {
		return roundCount;
	}

	/*returns int of current round's stake value for players
	* */
	synchronized public int getCurrentRoundsHeldStake() {
		return currentRoundsHeldStake;
	}

	/*pokerPlayers able to add their stake in conjunction with PokerPlayer.payCurrentStake()
	* */
	synchronized public void addToCurrentRoundsHeldStake(int addStake) {
		currentRoundsHeldStake += addStake;
	}


	synchronized public void setCurrentRoundsStakeAmount(int currentRoundsStakeAmount) {
		this.currentRoundsStakeAmount = currentRoundsStakeAmount;
	}

	/*returns int of total amount of betted stake of the current hand
		* */
	synchronized public int getCurrentRoundsStakeAmount() {
		return currentRoundsStakeAmount;
	}

	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.PokerGame.java!");

		// testing deck instance
		PokerGame pg = new PokerGame();
		for (PokerPlayer object : pg) {
			object.hand.generateHandType();
			System.out.println(object.toString()+"   "+object.hand.getBestHandTypeName());
			System.out.println(object.getPlayerChipAmount());

		}
		System.out.println(pg.gameDeck.getInstance().size());
		System.out.println(pg.gameDeck.getInstance().toString());

		System.out.println("getCurrentRoundsHeldStake: "+pg.getCurrentRoundsHeldStake());
		System.out.println("curRoundPlayerList: "+pg.curRoundPlayerList);


	}
}
