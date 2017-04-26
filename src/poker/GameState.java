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
	private ArrayList<PokerGame> interactionlist;
	private static int MAX_GAMES = 2;

	//public static User user;

	public GameState() {
		//createNewPokerGame(usr);
	}

	public static GameState getInstance() {
		if (instance == null) {
			instance = new GameState();
		}
		return instance;
	}

	synchronized private PokerGame createNewPokerGame(User usr) {
		this.add(new PokerGame());
		System.out.println(usr.getName() + "add to gameState list");
		return this.get((this.size() - 1));
	}

	synchronized public PokerGame checkForGameState(User usr) {
		checkGamesInstances();
		for (PokerGame game : this) {
			if (game.user.equals(usr)) {
				cleanPokerGame(game);
				return game;
			}
		}
		return createNewPokerGame(usr);
	}

	synchronized private void cleanPokerGame( PokerGame pkgame) {
		for(PokerGame game: this) {
			if (game.user.equals(pkgame.user)) {
				interactionlist.remove(pkgame);
			}
		}
		interactionlist.add(pkgame);
	}

	synchronized private void checkGamesInstances(){
		if(this.size() >= MAX_GAMES){
			PokerGame temp = interactionlist.get(0);
			for(PokerGame game: this) {
				if (game.user.equals(temp.user)) {
					temp.WritingScreenNameInDatabase(temp.user.getScreenName());
					System.out.println("A");
					this.remove(temp);
				}
				interactionlist.remove(0);
			}
		}
		System.out.println("B");
	}

	/*Class testing method
	* */
	public static void main(String[] args) {
		System.out.println("poker.GameState.java!");
	}
}
