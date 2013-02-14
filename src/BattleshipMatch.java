

public class BattleshipMatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String expertArg = "";
		if (args.length > 0) {
		    try {
		    	expertArg = args[0];
		    } catch (Exception e) {}
		}
		
		//data for player 1
		String[] a_row5 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] a_row2 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] a_row3 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] a_row4 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] a_row1 = {"\"\"", "5", "5", "5", "5", "5", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] a_row6 = {"\"\"", "3", "3", "3", "\"\"", "\"\"", "4", "4", "4", "4"};
		String[] a_row7 = {"\"\"", "2", "2", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "3"};
		String[] a_row8 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "3"};
		String[] a_row9 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "3"};
		String[] a_row10 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		
		String[][] playerOneInitialBoard = { a_row1, a_row2, a_row3, a_row4, a_row5, a_row6, a_row7, a_row8, a_row9, a_row10 };
		
		Player a = new Player("dirsten", playerOneInitialBoard);
		
		//player a is set on expert skills
		if (expertArg.equals("-e")) {
			a.setExpert(true);
		}
		
		int aJsonId = a.joinGame();
		System.out.println("Player " + a.getName() + " has joined the game " + aJsonId);
		
		//data for player 2
		String[] b_row1 = {"4", "4", "4", "4", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] b_row2 = {"\"\"", "\"\"", "\"\"", "\"\"", "5", "5", "5", "5", "5", "\"\""};
		String[] b_row3 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] b_row4 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] b_row5 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] b_row6 = {"\"\"", "3", "3", "3", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		String[] b_row7 = {"\"\"", "2", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "3"};
		String[] b_row8 = {"\"\"", "2", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "3"};
		String[] b_row9 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "3"};
		String[] b_row10 = {"\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\"", "\"\""};
		
		String[][] playerTwoInitialBoard = { b_row1, b_row2, b_row3, b_row4, b_row5, b_row6, b_row7, b_row8, b_row9, b_row10 };
		
		Player b = new Player("walter", playerTwoInitialBoard);
		int bJsonId = b.joinGame();
		System.out.println("Player " + b.getName() + " has joined the game " + bJsonId);
		
		System.out.println("");
		System.out.println("Waiting 10 seconds so you can head over to watch this match at http://localhost:3000/games/" + bJsonId);
		System.out.println("");
		
		for (int j=10; j>0; j--) {
			try {
				System.out.print(j + " ");
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				break;
			}
		}
		
		System.out.println("");
		System.out.println("GO!");
		System.out.println("");
		
		//play
		while(((String)a.getGameStatus().get("game_status")).equals("playing") && ((String)b.getGameStatus().get("game_status")).equals("playing")) {
			boolean a_turn = Boolean.valueOf(a.getGameStatus().get("my_turn").toString());
			boolean b_turn = Boolean.valueOf(b.getGameStatus().get("my_turn").toString());
			
			if (a_turn) {
				a.executeFire();
			}
			
			if (b_turn) {
				b.executeFire();
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		System.out.println("");
		System.out.println("GAME OUTCOME:");
		System.out.println(a.getName() + ": " + (String)a.getGameStatus().get("game_status"));
		System.out.println(b.getName() + ": " + (String)b.getGameStatus().get("game_status"));
	}

}
