package poker;

import twitter4j.User;

import java.util.ArrayList;

/**
 * Group: @poker_bot
 * Sean Regan - 13388996 - sean.regan@ucdconnect.ie
 * Junshu Jiang - 16201124 - junshu.jiang@ucdconnect.ie
 * Liam van der Spek - 14365951 - liam.van-der-spek@ucdconnect.ie
 * Eoin Kerr - 13366801 - eoin.kerr@ucdconnect.ie
 * Benjamin Kelly - 14700869 - benjamin.kelly.1@ucdconnect.ie
 */
public class GameState extends ArrayList<PokerGame> {
	private static GameState instance;
	private ArrayList<PokerGame> interactionList = new ArrayList<PokerGame>();
	private static int MAX_GAMES = 50 ;

	public GameState() {
	}

	public static GameState getInstance() {
		if (instance == null) {
			instance = new GameState();
		}
		return instance;
	}

	/*called by checkForGameState() if user has no existing pokerGame
	* */
	synchronized private PokerGame createNewPokerGame(User usr) {
		this.add(new PokerGame());
		System.out.println(usr.getName() + "add to gameState list");
		return this.get((this.size() - 1));
	}

	/*User parsed from their tweet. if new user. will create new pokerGame for them.
	* if returning user then will return their PokerGame.
	* */
	synchronized public PokerGame checkForGameState(User usr) {
		checkGamesInstancesLimit();
		for (PokerGame game : this) {
			if (game.user.equals(usr)) {
				touchPokerGameInteractionList(game);
				return game;
			}
		}
		PokerGame newGame = createNewPokerGame(usr);
		touchPokerGameInteractionList(newGame);
		return newGame;
	}

	/* every time user touches their pokerGame, they'll go to the back of the interactionList.
	* if/when games reaches MAX_GAMES the last used pokerGame will be saved anf dropped.
	* */
	synchronized private void touchPokerGameInteractionList( PokerGame pkgame) {
		if( !interactionList.isEmpty() ) {
			PokerGame tempGame = null;
			for (PokerGame game : interactionList) {
				if (game.user.equals(pkgame.user)) {
					tempGame = game;
				}
			}
			if (tempGame != null) {
				interactionList.remove(tempGame);
			}
		}
		interactionList.add(pkgame);
	}

	/* if at MAX_GAMES the last touched poked game will get saved into database and dropped
	* for Garbage collection to help insure server from being overloaded. :^)
	* */
	synchronized private void checkGamesInstancesLimit(){
		if((this.size() + 1) > MAX_GAMES){
			System.out.println(interactionList);
			PokerGame temp = interactionList.get(0);
			for(PokerGame game: this) {
				if (game.user.equals(temp.user)) {
					for(PokerPlayer player: game) { // human gets the held stake for his trouble
						if (player.isHuman) {
							player.receivesStake(game.getCurrentRoundsHeldStake());
							System.out.println("####### "+player.getPlayerName()+"'s game saved and dropped as games at MAX_GAMES of: "+MAX_GAMES);
						}
					}
					game.WritingScreenNameInDatabase(game.user.getScreenName());
					this.remove(game);
				}
			}
			interactionList.remove(0);
		}
	}

	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.GameState.java!");
	}
}
