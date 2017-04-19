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
	public static final int MAX_HAND = 5;
	public static final int MAX_DISCARD = 3;

	// game stats
	private DeckOfCards gameDeck;
	private boolean gameOver = false;
	private int roundCount = 0;
	private  boolean firstRound = true;

	// current round variables
	private int currentRoundsAnteAmount = 100; // the current round ante, entering fee
	private int currentRoundsStakeAmount = currentRoundsAnteAmount; // the current round cost
	private int currentRoundsHeldStake = 0; // the current pot (the ships on the table)
	private ArrayList<PokerPlayer> curRoundPlayerList = new ArrayList<PokerPlayer>(); // player playing that current hand of poker


	public PokerGame() {
		setPokerTable(); // adds human and bots to poker table

		while ( !gameOver ) {
			payAnteFee(currentRoundsAnteAmount, firstRound); // players pay their ante to enter game.
			if(gameOver) { break; }
			dealOutCards(firstRound); // deals out new cards.

			currentRoundPlayerOptions(); // gets players round inputs

			getRoundWinner(); // calculates winning hand and pays that player the pot.

			resetRound(); // clears round.
		}
	}
	public static PokerGame getInstance() {
		if (instance == null) { instance = new PokerGame();}
		return instance;
	}

	/*adds poker players to game.
	* */
	private void setPokerTable() {
		this.add(new PokerPlayer("human",this, gameDeck, INITIAL_CHIP_AMOUNT, true)); // add human user.
		// add bots
		for (int j = 0; j < MAX_BOTS; j++) {
			this.add(new PlayerBot(this, gameDeck, 100));
		}
	}

	/*deals out cards for the next round/
	* */
	synchronized private void dealOutCards(boolean firstR) {
		if ( !firstR ) {
			for (PokerPlayer player : this) {
				player.getNewCardsForHand();
			}
		}
	}

	/*allows users to input their hand changes and betting options.
	* */
	private void currentRoundPlayerOptions() {
		for (PokerPlayer player : this) { // players choose card discard options
			while(player.playersHandOptions()==false){}
		}




		for (PokerPlayer player : this) { // players choose their betting options
			while(player.playersBettingOptions()==false){}
		}

	}

	/*player will pay enter fee if unable then bankrupted
	* */
	synchronized private void payAnteFee(int AnteFee, boolean isFistRound) {
		//for (PokerPlayer player : this) {
		for (int i = 0; i < this.size(); i++) {
			if( this.get(i).isHuman ) {
				this.get(i).payAnteFee(AnteFee, isFistRound);
			}
			else {
				this.get(i).payAnteFee(AnteFee, true);
			}
		}
	}

	/*removes pokerPlayer at that list location.
	* ~ checks if human player and if so gameOver = true;
	* */
	synchronized public void playerIsBankrupted(PokerPlayer player) {
		if( player.isHuman ) {
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

	/*gets highest scored player and gives them the pot.
	* */
	synchronized private void getRoundWinner() {
		PokerPlayer tempPlayer = calcPlayerHandScores();
		tempPlayer.receivesStake(currentRoundsHeldStake);
		tempPlayer.totalRoundsWon += 1;
		System.out.println(tempPlayer.getPlayerName()+" is the winner!");
		System.out.println(tempPlayer.getPlayerName()+": new Chips amount: "+tempPlayer.getPlayerChipAmount());
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

	/*rests rounds data.
	* */
	synchronized private void resetRound() {
		//currentRoundsStakeAmount = 0;
		currentRoundsHeldStake = 0;
		curRoundPlayerList.clear();
		//System.out.println("**** Return cards to deck: "+gameDeck.getInstance().size());
		for (PokerPlayer player : this) {
			player.returnHandToDeck();
		}
		//System.out.println("**** Return cards to deck END: "+gameDeck.getInstance().size());
		gameDeck.getInstance().shuffle();
		firstRound = false; // add option to human to exit game at the next ante pay.
	}

	/*called from pokerPlayer if
	* */
	synchronized public void addToCurRoundPlayerList(PokerPlayer temp) {
		curRoundPlayerList.add(temp);
	}

	/*called from pokerPlayer if folding from that round.
	* */
	synchronized public int curRoundPlayerFolds(PokerPlayer temp) {
		if(curRoundPlayerList.contains(temp)) {
			//System.out.println("curRoundPlayerList contain: "+temp.getPlayerName());
			curRoundPlayerList.remove(temp);
		}
		return 0;
	}

	/*Called when player chooses to rise stake. all pervious called players will have option
	* to call again to new price or fold.
	* */
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


	/*Used in pokerPlayers when raising stake.
	* */
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
			//object.hand.generateHandType();
			//System.out.println(object.toString()+"   "+object.hand.getBestHandTypeName());
			System.out.println(object.toString());
			System.out.println(object.getPlayerChipAmount());

		}
		System.out.println(pg.gameDeck.getInstance().size());
		System.out.println(pg.gameDeck.getInstance().toString());

		System.out.println("PokerGame: "+pg.toString());

		System.out.println("getCurrentRoundsHeldStake: "+pg.getCurrentRoundsHeldStake());
		System.out.println("curRoundPlayerList: "+pg.curRoundPlayerList);


	}
}
