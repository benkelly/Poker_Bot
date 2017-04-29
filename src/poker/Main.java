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

public class Main {

			public static void main(String[] args) {
				System.out.println("main class!");

		TwitterInterpreter ti = new TwitterInterpreter();
		String keywords[] = {"dealmein", ti.getTwitterScreenName()};
		ti.publicStreamReader(keywords);
	}
}
