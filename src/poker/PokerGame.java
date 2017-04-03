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

	// game stats
	private boolean gameOver = false;
	private int roundCount = 0;

	// current round variables
	private int currentRoundsStakeAmount = 0;
	private int currentRoundsHeldStake = 0;
	private ArrayList<PokerPlayer> curRoundPlayerList = new ArrayList<PokerPlayer>(); // player playing that current hand of poker


	public PokerGame() {
		setPokerTable();
	}

	/*adds poker players to game.
	* */
	private void setPokerTable() {
		this.add(new PokerPlayer("human", 3000)); // add human user.
		// add bots
		for (int j = 0; j < 3; j++) {
			this.add(new PokerPlayer("bot: "+j, 3000));
		}
	}

	/*removes pokerPlayer at that list location.
	* ~ checks if human player and if so gameOver = true;
	* */
	private void removePlayer(int number) {
		if(number == 0) {
			gameOver = true;
			System.out.println(this.get(0).getPlayerName()+" is bankrupt.... game over dude....");
		}
		this.remove(number);
		System.out.println(this.get(number).getPlayerName()+" is bankrupt!");
	}


	/*Returns the pokerPlayer with the highest score.
	* */
	private PokerPlayer calcPlayerHandScores() {
		int tempHighScore = 0;
		int tempHighScorePlayerIndex = 0;
		int index = 0;
		for (PokerPlayer object : this) {
			if (tempHighScore < object.getCurrentHandScore()) { // re-calcs all players current scores.
			tempHighScore = object.getCurrentHandScore();
			tempHighScorePlayerIndex = index;
			}
			index++;
		}
		return this.get(tempHighScorePlayerIndex);
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


	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.PokerGame.java!");
	}
}
