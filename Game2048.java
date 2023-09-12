import java.util.Arrays;

public class Game2048 {
	
	public static void main(String[] args) {
		
		Grid gameGrid = new Grid();
		int[] gridValues = gameGrid.getGridValues();
		boolean canMove = true;
		boolean canMoveDirection = true;
		boolean validCommand = false;
		boolean forceLeave = false;
		boolean[] hasSecrets = gameGrid.getHasSecrets();
		String awsd = "";
		int[] leaderboard = new int[10];
		
		System.out.println("Welcome to 2048!!!");
		System.out.println("PLEASE READ THE COMMANDS LIST BEFORE PLAYING: \n");
		commandHelp();

		System.out.println("\nCurrent top 10 personal highscores: ");
		
		//the following is for the highscores
		TextIO.readFile("result.txt");
		String highscore = TextIO.getlnString();
		System.out.println(highscore);
		TextIO.readStandardInput();
		
		
		//creates the mosaic
		String size = "";
		System.out.println("\nWhat size mosaic would you like?" +
		"\nType either 'small' 'medium' or 'large'. Typing an invalid response will default to small.");
		size = TextIO.getlnString();
		size = size.toLowerCase();
		switch(size) {
			case("medium"):
				Mosaic.open(4, 4, 75, 75);
				break;
			case("large"):
				Mosaic.open(4, 4, 100, 100);
				break;
			default:
				Mosaic.open(4, 4, 50, 50);
		}
		gameGrid.setMosaic(gridValues);
		System.out.println("The game has started! Good luck!!");
		
		//this is what is actually running during the game
		while(canMove) {
			validCommand = false;
			awsd = TextIO.getlnString(); //user input
			if(awsd.equals("a")) {
				validCommand = true;
				canMoveDirection = gameGrid.canMove("L");
				if(canMoveDirection) {
					gameGrid.moveLeft(gridValues);
					gameGrid.spawnNew();
				}
				else {
					System.out.println("Cannot move in that direction! Try another one");
				}
			}
			if(awsd.equals("d")) {
				validCommand = true;
				canMoveDirection = gameGrid.canMove("R");
				if(canMoveDirection) {
					gameGrid.moveRight(gridValues);
					gameGrid.spawnNew();
				}
				else {
					System.out.println("Cannot move in that direction! Try another one");
				}
			}
			if(awsd.equals("w")) {
				validCommand = true;
				canMoveDirection = gameGrid.canMove("U");
				if(canMoveDirection) {
					gameGrid.moveUp(gridValues);
					gameGrid.spawnNew();
				}
				else {
					System.out.println("Cannot move in that direction! Try another one");
				}
			}
			if(awsd.equals("s")) {
				validCommand = true;
				canMoveDirection = gameGrid.canMove("D");
				if(canMoveDirection) {
					gameGrid.moveDown(gridValues);
					gameGrid.spawnNew();
				}
				else {
					System.out.println("Cannot move in that direction! Try another one");
				}
			}
			if(awsd.equals("b")) {
				validCommand = true;
				System.out.println(gameGrid.toString());
			}
			if(awsd.equals("x")) {
				validCommand = true;
				System.out.println("Your score is " + gameGrid.getScore());
			}
			if(awsd.equals("c") || awsd.equals("color")) {
				validCommand = true;
				hasSecrets = gameGrid.getHasSecrets();
				displayColors(hasSecrets);
			}
			if(awsd.equals("info")) {
				validCommand = true;
				howToPlay();
			}
			if(awsd.equals("l")) {
				validCommand = true;
				System.out.println("\nCurrent top 10 personal highscores: ");
				
				TextIO.readFile("result.txt");
				highscore = TextIO.getlnString();
				System.out.println(highscore);
				TextIO.readStandardInput();
			}
			if(awsd.equals("h") || awsd.equals("help")) {
				validCommand = true;
				commandHelp();
			}
			if(awsd.equals("quit")) {
				validCommand = true;
				String response;
				
				System.out.println("Are you sure you want to quit? Quitting will end the game and save your current score to your personal leaderboard.");
				System.out.println("Type 'yes' or 'no' (Typing an invalid response will continue the game)");
				response = TextIO.getlnString();
				response.toLowerCase();
				
				if(response.equals("yes")) {
					forceLeave = true;
				}
				else {
					System.out.println("Game resumed.");
				}
			}
			
			if(!validCommand) {
				System.out.println("That is not a valid command! Type 'h' or 'help' to view the commands list");
			}
			
			//displays the mosaic properly
			gameGrid.setMosaic(gridValues);
			canMove = gameGrid.canMove(gridValues);
			gameGrid.fixGrid();
			
			if(forceLeave) {
				canMove = false;
			}
			
		}
		System.out.println(gameGrid.toString());
		System.out.println("You have no more moves. Your final score is " + gameGrid.getScore() + "!!");
		
		
		//SAVES ON LEADERBOARD AS A STRING
		setLeaderboard(highscore, leaderboard);
		int lowest = 0;
		int lowestIndex = 0;
		for(int i = 0; i < 10; i++) {
			if(leaderboard[i] <= lowest) {
				lowest = leaderboard[i];
				lowestIndex = i;
			}
		}
		if(gameGrid.getScore() > leaderboard[lowestIndex]) {
			leaderboard[lowestIndex] = gameGrid.getScore();
		}
		
		TextIO.writeFile("result.txt");
		int[] flipped = new int[10]; //used because the Arrays.sort function sorts it the other way around...
		Arrays.sort(leaderboard);
		for(int i = 0; i < 10; i++) {
			flipped[i] = leaderboard[i];
		}
		for(int i = 0; i < 10; i++) {
			leaderboard[i] = flipped[9-i];
		}
		
		TextIO.putln(leaderboardToString(leaderboard));
		TextIO.readStandardInput();
		
		setLeaderboard(highscore, leaderboard);
		
		System.out.println("Here is your updated personal leaderboard:");
		TextIO.readFile("result.txt");
		highscore = TextIO.getlnString();
		System.out.println(highscore);
		TextIO.readStandardInput();
		
	}
	
	public static String leaderboardToString(int[] leaderboard) {
		String str = "";
		for(int i = 0; i < 9; i++) {
			str = str + leaderboard[i] + ", ";
		}
		str = str + leaderboard[9];
		return str;
	}
	
	public static void setLeaderboard(String highscores, int[] leaderboard) {
		String[] leaderString = new String[10];
		leaderString = highscores.split(", "); //gets only the numerical values
		
		for(int i = 0; i < 10; i++) {
			leaderboard[i] = Integer.parseInt(leaderString[i]);
		}
		Arrays.sort(leaderboard);
		
	}
	
	public static void commandHelp() {
		System.out.println("To move (use wasd keys): \n" + "Type 'w' to move up \n" + 
		"Type 'a' to move left \n" + "Type 's' to move down \n" + "Type 'd' to move right \n");
		System.out.println("Additional commands:");
		System.out.println("Type 'b' to display the grid with numbers \n" + 
		"Type 'x' to display your score \n" + "Type 'c' or 'color' to view the list of colors and their corresponding values \n" + 
		"Type 'l' to display your personal leaderboard \n" + "Type 'quit' to end the game \n" + "Type 'info' to learn more about how to play \n" +
		"Type 'h' or 'help' to view the commands list");

	}
	
	public static void howToPlay() {
		System.out.println("This game is very much like the normal 2048 game. \n"
				+ "The goal of the game is to reach 2048 (or in this case, the 'Hot Pink' tile on the mosaic) \n" + 
				"In order to do that, you must combine like colors (red with red, orange with orange, etc) \n" +
				"Colors that are not the same will not combine. \n" + "Use the 'wasd' keys to move (view the commands list) \n" + 
				"\n" + "The score system works like this: the value that each 'merge' of tiles combines to is added to your total score. \n" +
				"For example, if you combine two 16 tiles, your score goes up by 32.");
		
	}
	
	public static void displayColors(boolean[] hasSecrets) { //added a unique feature here: I don't say the last five colors unless you unlock it (by playing the game until you get it)
		System.out.printf("%-15s %d", "Red:", 2);
		System.out.printf("\n%-15s %d", "Orange:", 4);
		System.out.printf("\n%-15s %d", "Yellow:", 8);
		System.out.printf("\n%-15s %d", "Light Green:", 16);
		System.out.printf("\n%-15s %d", "Dark Green:", 32);
		System.out.printf("\n%-15s %d", "Cyan:", 64);
		System.out.printf("\n%-15s %d", "Blue:", 128);
		System.out.printf("\n%-15s %d", "Dark Blue:", 256);
		System.out.printf("\n%-15s %d", "Light Purple:", 512);
		System.out.printf("\n%-15s %d", "Magenta:", 1024);
		System.out.printf("\n%-15s %d", "Hot Pink:", 2048);
		if(hasSecrets[0]) {
			System.out.printf("\n%-15s %d", "Light Pink:", 4096);
		}
		else {
			System.out.printf("\n%-15s %d", "????", 4096);
		}
		
		if(hasSecrets[1]) {
			System.out.printf("\n%-15s %d", "White:", 8192);
		}
		else {
			System.out.printf("\n%-15s %d", "?????", 8192);
		}
		
		if(hasSecrets[2]) {
			System.out.printf("\n%-15s %d", "Light Gray:", 16384);
		}
		else {
			System.out.printf("\n%-15s %d", "??????", 16384);
		}
		
		if(hasSecrets[3]) {
			System.out.printf("\n%-15s %d", "Gray:", 32768);
		}
		else {
			System.out.printf("\n%-15s %d", "???????", 32768);
		}
		
		if(hasSecrets[4]) {
			System.out.printf("\n%-15s %d", "Dark Gray:", 65536);
		}
		else {
			System.out.printf("\n%-15s %d", "????????", 65536); 
		}
		if(hasSecrets[0] && hasSecrets[1] && hasSecrets[2] && hasSecrets[3] && hasSecrets[4]) {
			System.out.println("\nHere is the list of colors with their corresponding values! Congrats on finding all of the mystery colors!!");
		}
		else {
			System.out.println("\nHere is the list of colors with their corresponding values! Are you able to discover the mystery colors??");
		}

	}
		
}
