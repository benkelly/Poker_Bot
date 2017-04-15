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
		setPokerTable();


		currentRoundPlayerOptions();
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

	private void currentRoundPlayerOptions() {
		payAnteFee(currentRoundsAnteAmount); // players pay their ante to enter game.

/*		for (PokerPlayer player : this) { // players choose card discard options
			player.playersHandOptions();
		}*/



		for (PokerPlayer player : this) { // players choose their betting options
			while(player.playersBettingOptions()==false){}
		}
	}

	/*player will pay enter fee if unable then bankrupted
	* */
	private void payAnteFee(int AnteFee) {
		for (PokerPlayer player : this) {
			player.payAnteFee(AnteFee);
		}
	}

	/*removes pokerPlayer at that list location.
	* ~ checks if human player and if so gameOver = true;
	* */
	public void playerIsBankrupted(PokerPlayer player) {
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


	private void getRoundwinner() {
		PokerPlayer temp = calcPlayerHandScores();
		temp.receivesStake(currentRoundsHeldStake);
		temp.totalRoundsWon += 1;
		System.out.println(temp.getPlayerName()+" is the winner!");
	}
	/*Returns the pokerPlayer with the highest score.
	* */
	private PokerPlayer calcPlayerHandScores() {
		int tempHighScore = 0;
		int tempHighScorePlayerIndex = 0;
		int index = 0;
		for (PokerPlayer object : curRoundPlayerList) {
			if (tempHighScore < object.getCurrentHandScore()) { // re-calcs all players current scores.
				tempHighScore = object.getCurrentHandScore();
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

	public void addToCurRoundPlayerList(PokerPlayer temp) {
		curRoundPlayerList.add(temp);
	}

	public void curRoundPlayerFolds(PokerPlayer temp) {
		curRoundPlayerList.remove(temp);
	}

	public void matchStakeIncrease(int stakeIncrease) {
		//System.out.println("matchStakeIncrease START");
		for (PokerPlayer object : curRoundPlayerList) {
			object.reRaiseStake(stakeIncrease);
			System.out.println(object);

		}
		//System.out.println("matchStakeIncrease ENd");

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
	public int getCurrentRoundsHeldStake() {
		return currentRoundsHeldStake;
	}

	/*pokerPlayers able to add their stake in conjunction with PokerPlayer.payCurrentStake()
	* */
	public void addToCurrentRoundsHeldStake(int addStake) {
		currentRoundsHeldStake += addStake;
	}


	public void setCurrentRoundsStakeAmount(int currentRoundsStakeAmount) {
		this.currentRoundsStakeAmount = currentRoundsStakeAmount;
	}

	/*returns int of total amount of betted stake of the current hand
		* */
	public int getCurrentRoundsStakeAmount() {
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
