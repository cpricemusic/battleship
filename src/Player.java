import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringEscapeUtils;


public class Player {
	private static String alphabet = "ABCDEFGHIJ";
	
	private String name = null;
	private String[][] initialBoard = null;
	private int gameId = -1;
	private boolean expert = false;
	
    private ArrayList possibleExpertAttacks = new ArrayList<String>();
    
    private String lastAttackPoint = null;
	private ArrayList possibleMoves = new ArrayList<String>(Arrays.asList("A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8", "G9", "G10", "H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9", "H10", "I1", "I2", "I3", "I4", "I5", "I6", "I7", "I8", "I9", "I10", "J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", "J9", "J10"));
	
	public Player() {}
	
	public Player(String name, String[][] board) {
	    this.name = name;
	    this.initialBoard = board;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public boolean isExpert() {
		return expert;
	}

	public void setExpert(boolean expert) {
		this.expert = expert;
	}
	
	//join the game
	public int joinGame() {
		String boardData = "";
		String jsonResult = "";
		String charset = "UTF-8";
				
		for (int i=0; i<initialBoard.length; i++) {
	        String[] temp = (String[])initialBoard[i];
	        boardData += (Arrays.toString(temp));
	        if (!((i+1) == initialBoard.length)) {
	        	boardData += ",";
	        }
	    }		
	    	    
		try {
			String url = "http://localhost:3000/games/join";
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Accept-Charset", charset);
			urlConnection.setRequestProperty("Content-Type", "application/json; charset=" + charset);
			//urlConnection.connect();
			//OutputStream outputStream = urlConnection.getOutputStream();
			OutputStream outputStream = null;
			
			try {
				outputStream = urlConnection.getOutputStream();
				//outputStream.write(query.getBytes(charset));
				outputStream.write(("{\"user\": \"" + this.name + "\", \"board\": \"[" + StringEscapeUtils.escapeEcmaScript(boardData) + "]\"}").getBytes("UTF-8"));
				outputStream.flush();
			} finally {
			     if (outputStream != null) try { outputStream.close(); } catch (IOException logOrIgnore) {}
			}
			
	        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	        	jsonResult += inputLine;
	        	//System.out.println(inputLine);
	        }
	        in.close();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		//parse out the game_id from the JSON response and send it back 
		if (jsonResult != null && jsonResult.length() > 0) {
			Map<String, Object> m = parseJSON(jsonResult);
			Integer id = new Integer(m.get("game_id").toString());
			this.gameId = id.intValue();
		}
		
		return this.gameId;
	}
	
	//fire
	public void executeFire() {
		Map<String, Object> m = fire();
		
		//expert strategy
		if (Boolean.valueOf(m.get("hit").toString()) && this.isExpert()) {
			//System.out.println("SETTING ATTACK ROOT NODE TO " + this.lastAttackPoint);
			//set attackPoint
			generateSmartAttacks();
		}
	}
	
	//generate some "smart"/expert attacks that will help give an advanced player the upper hand
	private void generateSmartAttacks() {
		//possibleExpertAttacks = new ArrayList<String>();
		String col = this.lastAttackPoint.substring(0, 1);
		String row = this.lastAttackPoint.substring(1, 2);
		
		int idx = alphabet.indexOf(col);
		int iRow = Integer.parseInt(row);
		
		//attack all around any "hit" points.  odds should be greater of a hit than
		//simply continuing in a random pattern
		if ((idx - 1) >= 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(alphabet.charAt(idx - 1));
			sb.append(row);
			if (this.possibleMoves.contains(sb.toString())) {
				possibleExpertAttacks.add(sb.toString());
			}
		}
		if ((idx + 1) <= 9) {
			StringBuilder sb = new StringBuilder();
			sb.append(alphabet.charAt(idx + 1));
			sb.append(row);
			if (this.possibleMoves.contains(sb.toString())) {
				possibleExpertAttacks.add(sb.toString());
			}
		}
		if ((iRow - 1) >= 1) {
			StringBuilder sb = new StringBuilder();
			sb.append(alphabet.charAt(idx));
			sb.append(iRow - 1);
			if (this.possibleMoves.contains(sb.toString())) {
				possibleExpertAttacks.add(sb.toString());
			}
		}
		if ((iRow + 1) <= 10) {
			StringBuilder sb = new StringBuilder();
			sb.append(alphabet.charAt(idx));
			sb.append(iRow + 1);
			if (this.possibleMoves.contains(sb.toString())) {
				possibleExpertAttacks.add(sb.toString());
			}
		}
		
		//for (int i=0; i<this.possibleExpertAttacks.size(); i++) {
		//	System.out.println("POSS. SMART ATTACK: " + this.possibleExpertAttacks.get(i));
		//}
	}
	
	//fire
	private Map<String, Object> fire() {
		String jsonResult = "";
		String charset = "UTF-8";
		Map<String, Object> m = null;
		
		Random rand = new Random();
		StringBuilder f = new StringBuilder();
		
		f.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		f.append(((1 + (int)(Math.random() * ((10 - 1) + 1)))+""));
		
		//used to have the non-expert not even keep track of what shots he has made before.
		//that seemed a little unfair, so i removed that (both "expert" and non-expert keep track now).
		//if (this.isExpert()) {
			if (this.isExpert()) {
				//here's where the expert actually chooses a smart attack as opposed to the random one
				if (this.possibleExpertAttacks.size() > 0) {
					f = new StringBuilder();
					System.out.println(this.name + ": SMART ATTACKING");
					f.append(this.possibleExpertAttacks.get(0));
					this.possibleExpertAttacks.remove(0);
				}
				else {
					//this.attackRootNode = null;
					while (!this.possibleMoves.contains(f.toString())) {
						//System.out.println("I've played this before...");
						f = new StringBuilder();
						f.append(alphabet.charAt(rand.nextInt(alphabet.length())));
						f.append(((1 + (int)(Math.random() * ((10 - 1) + 1)))+""));
					}
				}
			} else {
				//check to see if we've played this move before.
				while (!this.possibleMoves.contains(f.toString())) {
					//System.out.println("I've played this before...");
					f = new StringBuilder();
					f.append(alphabet.charAt(rand.nextInt(alphabet.length())));
					f.append(((1 + (int)(Math.random() * ((10 - 1) + 1)))+""));
				}
			}
			
			//System.out.println("Removing " + f.toString());
			this.lastAttackPoint = f.toString();
			this.possibleMoves.remove(f.toString());
		//}
		
		//String f = new StringBuilder('A' + rand.nextInt(10)) + ((1 + (int)(Math.random() * ((10 - 1) + 1)))+"");
        System.out.println(this.name + ": " + f.toString());				
		
		try {
			String query = String.format("user=%s&game_id=%s&shot=%s", 
				     URLEncoder.encode(this.name, charset), 
				     URLEncoder.encode(this.gameId+"", charset),
				     URLEncoder.encode(new String(f), charset));
			
			String url = "http://localhost:3000/games/fire";
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Accept-Charset", charset);
			urlConnection.setRequestProperty("Content-Type", "application/json; charset=" + charset);
			//urlConnection.connect();
			//OutputStream outputStream = urlConnection.getOutputStream();
			OutputStream outputStream = null;
			
			try {
				outputStream = urlConnection.getOutputStream();
				//outputStream.write(query.getBytes(charset));
				outputStream.write(("{\"user\": \"" + this.name + "\", \"game_id\": \"" + this.gameId + "\", \"shot\": \"" + f.toString() + "\"}").getBytes("UTF-8"));
				outputStream.flush();
			} finally {
			     if (outputStream != null) try { outputStream.close(); } catch (IOException logOrIgnore) {}
			}
			
	        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	        	jsonResult += inputLine;
	        	//System.out.println(inputLine);
	        }
	        in.close();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		if (jsonResult != null && jsonResult.length() > 0) {
			m = parseJSON(jsonResult);
		}
		
		return m;
	}
	
	//parse a JSON response into a Map
	private Map<String, Object> parseJSON(String json) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		json = json.trim().substring(1, json.length()-1);
		json = json.replace("\"", "");
		
		String[] pairs = json.split(",");
		//String[] pairs = { json };
		
		for (int i=0; i<pairs.length; i++) {
		    String pair = pairs[i];
		    String[] keyValue = pair.split(":");
		    //result.put(keyValue[0], Integer.valueOf(keyValue[1]));
		    //System.out.println("putting " + keyValue[0].trim().substring(1,  keyValue[0].length()-1) + ", " + keyValue[1]);
		    result.put(keyValue[0], keyValue[1]);
		}
		
		return result;
	}
	
	//get the game status
	public Map<String, Object> getGameStatus() {
		String jsonResult = "";
		String charset = "UTF-8";
		Map<String, Object> m = null;
		
		try {
			String query = String.format("user=%s&game_id=%s", 
				     URLEncoder.encode(this.name, charset), 
				     URLEncoder.encode(this.gameId+"", charset));
			
			String url = "http://localhost:3000/games/status";
			URLConnection urlConnection = new URL(url + "?" + query).openConnection();
			urlConnection.setRequestProperty("Accept-Charset", charset);
			InputStream response = urlConnection.getInputStream();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	        	jsonResult += inputLine;
	        	//System.out.println(inputLine);
	        }
	        in.close();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		if (jsonResult != null && jsonResult.length() > 0) {
			m = parseJSON(jsonResult);
		}
		
		return m;
	}
		
}
