import java.awt.Color;


public class Grid {
	
	private int[] gridValues = new int[16];
	private int startingPlacement = (int)(Math.random()*16);{ 
	gridValues[startingPlacement] = 2;}
	private int maxMerges = 0;
	private int nonZeros = 0;
	private boolean specialCase = false; //had to create this to address a unique combination 
	private boolean cameHere = false;
	private boolean has2048 = false; 
	private boolean[] hasSecrets = new boolean[5]; //cool feature I added, used with the colors command
	private int[] holdGrid = new int[16]; //used to save the grid when checking if the player can still move
	private int score = 0;
	
	public Grid() {
		int j = startingPlacement;
		for (int i = 0; i < 16; i++) {
			if(i == j) {
				gridValues[i] = 2;
			}
			else {
				gridValues[i] = 0;
			}
		}
	}
	
	public int[] getGridValues() {
		return gridValues;
	}
	
	public boolean[] getHasSecrets() {
		return hasSecrets;
	}
	
	public int getScore() {
		return score;
	}

	public String toString() {
		int multOfFour = 1;
		String grid = "";
		for(int i = 0; i < 16; i++) {
			if(multOfFour == 4) {
				grid = grid + String.format("%-5d", gridValues[i]) + "\n";
				multOfFour = 0;
			}
			else {
				grid = grid + String.format("%-5d", gridValues[i]) + " ";
			}
			multOfFour++;
		}
		return grid;
	}
	
	public void spawnNew() { //spawns a two in a random place
		int j = (int)(Math.random()*16);
		for (int i = 0; i < 16; i++) {
			if(i == j && gridValues[i] != 0) {
				j = (int)(Math.random()*16);
				i = -1;
			}
			else {
				if(i == j && gridValues[i] == 0) {
					gridValues[i] = 2;
				}
			}
			
		}
	}
	
	public void setMosaic(int[] gridValues) { //creates the mosaic and tiles and also calculates the score
 		score = 0; //mathematically calculated the addition for the score per tile (each tile is worth X amount)
		int row = 0;
		for(int i = 0; i < 16; i++) {
			if(i > 3) {
				row = (i + 4)/4 - 1;
			}
			switch(gridValues[i]) {
				case 2:
					Mosaic.setColor(row, (i + 4)%4, new Color(255,0,0));
					break;
				case 4:
					Mosaic.setColor(row, (i + 4)%4, Color.ORANGE);
					score = score + 4;
					break;
				case 8:
					Mosaic.setColor(row, (i + 4)%4, Color.YELLOW);
					score = score + 16;
					break;
				case 16:
					Mosaic.setColor(row, (i + 4)%4, Color.GREEN);
					score = score + 48;
					break;
				case 32:
					Mosaic.setColor(row, (i + 4)%4, new Color(0,153,76));
					score = score + 128;
					break;
				case 64:
					Mosaic.setColor(row, (i + 4)%4, Color.CYAN);
					score = score + 320;
					break;
				case 128:
					Mosaic.setColor(row, (i + 4)%4, new Color(0,128,255));
					score = score + 768;
					break;
				case 256:
					Mosaic.setColor(row, (i + 4)%4, Color.BLUE);
					score = score + 1792;
					break;
				case 512:
					Mosaic.setColor(row, (i + 4)%4, new Color(204,153,255));
					score = score + 4096;
					break;
				case 1024:
					Mosaic.setColor(row, (i + 4)%4, new Color(255,0,255));
					score = score + 9216;
					break;
				case 2048:
					Mosaic.setColor(row, (i + 4)%4, new Color(255,0,127));
					score = score + 20480;
					if(!has2048) {
						System.out.println("CONGRATULATIONS! You made it to 2048!");
						System.out.println("Keep on going! See how far you can get!");
						has2048 = true;
					}
					break;
				case 4096:
					Mosaic.setColor(row, (i + 4)%4, new Color(255,153,204));
					score = score + 45056;
					hasSecrets[0] = true;
					break;
				case 8192:
					Mosaic.setColor(row, (i + 4)%4, new Color(255,255,255));
					score = score + 98304;
					hasSecrets[1] = true;
					break;
				case 16384:
					Mosaic.setColor(row, (i + 4)%4, new Color(160,160,160));
					score = score + 212992;
					hasSecrets[2] = true;
					break;
				case 32768:
					Mosaic.setColor(row, (i + 4)%4, new Color(96,96,96));
					score = score + 458752;
					hasSecrets[3] = true;
					break;
				case 65536: //theoretically speaking I think this is possible which is why I included it so it doesn't display a black tile just in case
					Mosaic.setColor(row, (i + 4)%4, new Color(64,64,64));
					score = score + 983040;
					hasSecrets[4] = true;
					break;
				default:
					Mosaic.setColor(row, (i + 4)%4, Color.BLACK);
			}
		}
	}
	
	private int getMaxMerges(int row) { //calculates the max number of merges possible per row
		nonZeros = 0;
		maxMerges = 0;
		if(row == 1) {
			for(int i = 0; i < 4; i++) {
				if(gridValues[i] != 0) {
					nonZeros++;
				}
			}
			if(nonZeros == 2 || nonZeros == 3) {
				maxMerges = 1;
			}
			else {
				for(int i = 0; i < 3; i++) {
					if(gridValues[i] == gridValues[i + 1]) {
						maxMerges++;
						i++;
					}
				}
			}
		}
		if(row == 2) {
			for(int i = 4; i < 8; i++) {
				if(gridValues[i] != 0) {
					nonZeros++;
				}
			}
			if(nonZeros == 2 || nonZeros == 3) {
				maxMerges = 1;
			}
			else {
				for(int i = 4; i < 7; i++) {
					if(gridValues[i] == gridValues[i + 1]) {
						maxMerges++;
						i++;
					}
				}
			}
		}
		if(row == 3) {
			for(int i = 8; i < 12; i++) {
				if(gridValues[i] != 0) {
					nonZeros++;
				}
			}
			if(nonZeros == 2 || nonZeros == 3) {
				maxMerges = 1;
			}
			else {
				for(int i = 8; i < 11; i++) {
					if(gridValues[i] == gridValues[i + 1]) {
						maxMerges++;
						i++;
					}
				}
			}
		}
		if(row == 4) {
			for(int i = 12; i < 16; i++) {
				if(gridValues[i] != 0) {
					nonZeros++;
				}
			}
			if(nonZeros == 2 || nonZeros == 3) {
				maxMerges = 1;
			}
			else {
				for(int i = 12; i < 15; i++) {
					if(gridValues[i] == gridValues[i + 1]) {
						maxMerges++;
						i++;
					}
				}
			}
		}
		return maxMerges;
	}
	
	private int getMaxMergesVert(int col) { //calculates the max number of merges possible per column
		nonZeros = 0;
		maxMerges = 0;
		if(col == 1) {
			for(int i = 0; i < 4; i++) {
				if(gridValues[i * 4] != 0) {
					nonZeros++;
				}
			}
			if(nonZeros == 2 || nonZeros == 3) {
				maxMerges = 1;
			}
			else {
				for(int i = 0; i < 9; i = i + 4) {
					if(gridValues[i] == gridValues[i + 4]) {
						maxMerges++;
						i = i + 4;
					}
				}
			}
		}
		if(col == 2) {
			for(int i = 1; i < 14; i = i + 4) {
				if(gridValues[i] != 0) {
					nonZeros++;
				}
			}
			if(nonZeros == 2 || nonZeros == 3) {
				maxMerges = 1;
			}
			else {
				for(int i = 1; i < 10; i = i + 4) {
					if(gridValues[i] == gridValues[i + 4]) {
						maxMerges++;
						i = i + 4;
					}
				}
			}
		}
		if(col == 3) {
			for(int i = 2; i < 15; i = i + 4) {
				if(gridValues[i] != 0) {
					nonZeros++;
				}
			}
			if(nonZeros == 2 || nonZeros == 3) {
				maxMerges = 1;
			}
			else {
				for(int i = 2; i < 11; i = i + 4) {
					if(gridValues[i] == gridValues[i + 4]) {
						maxMerges++;
						i = i + 4;
					}
				}
			}
		}
		if(col == 4) {
			for(int i = 3; i < 16; i = i + 4) {
				if(gridValues[i] != 0) {
					nonZeros++;
				}
			}
			if(nonZeros == 2 || nonZeros == 3) {
				maxMerges = 1;
			}
			else {
				for(int i = 3; i < 12; i = i + 4) {
					if(gridValues[i] == gridValues[i + 4]) {
						maxMerges++;
						i = i + 4;
					}
				}
			}
		}
		return maxMerges;
	}
	
	private boolean specialCase(String direction, int row) { //one unique combination did not work with the normal code, so I had to address it separately
		int i;
		if(direction.equals("L")) {
			i = row*4 - 4;
			if(gridValues[i] == 0) {
				return false;
			}
			if(gridValues[i] == gridValues[i + 1] && gridValues[i + 2] == 2 * gridValues[i] && gridValues[i + 2] == gridValues[i + 3]) {
				return true;
			}
		}
		if(direction.equals("R")) {
			i = row*4 - 4;
			if(gridValues[i] == 0) {
				return false;
			}
			if(gridValues[i] == gridValues[i + 1] && gridValues[i] == 2 * gridValues[i + 2] && gridValues[i + 2] == gridValues[i + 3]) {
				return true;
			}
		}
		if(direction.equals("U")) {
			i = row - 1;
			if(gridValues[i] == 0) {
				return false;
			}
			if(gridValues[i] == gridValues[i + 4] && gridValues[i] * 2 == gridValues[i + 8] && gridValues[i + 8] == gridValues[i + 12]) {
				return true;
			}
		}
		if(direction.equals("D")) {
			i = row - 1;
			if(gridValues[i] == 0) {
				return false;
			}
			if(gridValues[i] == gridValues[i + 4] && gridValues[i] == 2 * gridValues[i + 8] && gridValues[i + 8] == gridValues[i + 12]) {
				return true;
			}
		}
		
		return false;
	}
	
	public void moveLeft(int[] gridValues) { //code to move left 
		cameHere = false;
		maxMerges = getMaxMerges(1);
		specialCase = specialCase("L", 1);
		for(int i = 1; i < 4; i++) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 1; k < i + 1; k++) {
					if(gridValues[k - 1] == 0) {
						gridValues[k - 1] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m > 0; m--) {
						if(gridValues[m - 1] != 0 && gridValues[m - 1] != gridValues[i]) {
							break;
						}
						if(gridValues[m - 1] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m - 1] = 2*gridValues[m - 1];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMerges(2);
		specialCase = specialCase("L", 2);
		for(int i = 5; i < 8; i++) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 5; k < i + 1; k++) {
					if(gridValues[k - 1] == 0) {
						gridValues[k - 1] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m > 3; m--) {
						if(gridValues[m - 1] != 0 && gridValues[m - 1] != gridValues[i]) {
							break;
						}
						if(gridValues[m - 1] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m - 1] = 2*gridValues[m - 1];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMerges(3);
		specialCase = specialCase("L", 3);
		for(int i = 9; i < 12; i++) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 9; k < i + 1; k++) {
					if(gridValues[k - 1] == 0) {
						gridValues[k - 1] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m > 7; m--) {
						if(gridValues[m - 1] != 0 && gridValues[m - 1] != gridValues[i]) {
							break;
						}
						if(gridValues[m - 1] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m - 1] = 2*gridValues[m - 1];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMerges(4);
		specialCase = specialCase("L", 4);
		for(int i = 13; i < 16; i++) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 13; k < i + 1; k++) {
					if(gridValues[k - 1] == 0) {
						gridValues[k - 1] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m > 11; m--) {
						if(gridValues[m - 1] != 0 && gridValues[m - 1] != gridValues[i]) {
							break;
						}
						if(gridValues[m - 1] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m - 1] = 2*gridValues[m - 1];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
	}
	
	public void moveRight(int[] gridValues) { //code to move right
		cameHere = false;
		maxMerges = getMaxMerges(1);
		specialCase = specialCase("R", 1);
		for(int i = 2; i > -1; i--) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 2; k > i - 1; k--) {
					if(gridValues[k + 1] == 0) {
						gridValues[k + 1] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m < 3; m++) {
						if(gridValues[m + 1] != 0 && gridValues[m + 1] != gridValues[i]) {
							break;
						}
						if(gridValues[m + 1] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m + 1] = 2*gridValues[m + 1];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMerges(2);
		specialCase = specialCase("R", 2);
		for(int i = 6; i > 3; i--) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 6; k > i - 1; k--) {
					if(gridValues[k + 1] == 0) {
						gridValues[k + 1] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m < 7; m++) {
						if(gridValues[m + 1] != 0 && gridValues[m + 1] != gridValues[i]) {
							break;
						}
						if(gridValues[m + 1] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m + 1] = 2*gridValues[m + 1];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMerges(3);
		specialCase = specialCase("R", 3);
		for(int i = 10; i > 7; i--) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 10; k > i - 1; k--) {
					if(gridValues[k + 1] == 0) {
						gridValues[k + 1] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m < 11; m++) {
						if(gridValues[m + 1] != 0 && gridValues[m + 1] != gridValues[i]) {
							break;
						}
						if(gridValues[m + 1] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m + 1] = 2*gridValues[m + 1];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMerges(4);
		specialCase = specialCase("R", 4);
		for(int i = 14; i > 11; i--) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 14; k > i - 1; k--) {
					if(gridValues[k + 1] == 0) {
						gridValues[k + 1] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m < 15; m++) {
						if(gridValues[m + 1] != 0 && gridValues[m + 1] != gridValues[i]) {
							break;
						}
						if(gridValues[m + 1] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m + 1] = 2*gridValues[m + 1];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
	}
	
	public void moveUp(int[] gridValues) { //code to move up
		cameHere = false;
		maxMerges = getMaxMergesVert(1);
		specialCase = specialCase("U", 1);
		for(int i = 4; i < 13; i = i + 4) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 4; k < i + 1; k = k + 4) {
					if(gridValues[k - 4] == 0) {
						gridValues[k - 4] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m > 3; m = m - 4) {
						if(gridValues[m - 4] != 0 && gridValues[m - 4] != gridValues[i]) {
							break;
						}
						if(gridValues[m - 4] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m - 4] = 2*gridValues[m - 4];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMergesVert(2);
		specialCase = specialCase("U", 2);
		for(int i = 5; i < 14; i = i + 4) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 5; k < i + 1; k = k + 4) {
					if(gridValues[k - 4] == 0) {
						gridValues[k - 4] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m > 3; m = m - 4) {
						if(gridValues[m - 4] != 0 && gridValues[m - 4] != gridValues[i]) {
							break;
						}
						if(gridValues[m - 4] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m - 4] = 2*gridValues[m - 4];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMergesVert(3);
		specialCase = specialCase("U", 3);
		for(int i = 6; i < 15; i = i + 4) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 6; k < i + 1; k = k + 4) {
					if(gridValues[k - 4] == 0) {
						gridValues[k - 4] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m > 3; m = m - 4) {
						if(gridValues[m - 4] != 0 && gridValues[m - 4] != gridValues[i]) {
							break;
						}
						if(gridValues[m - 4] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m - 4] = 2*gridValues[m - 4];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMergesVert(4);
		specialCase = specialCase("U", 4);
		for(int i = 7; i < 16; i = i + 4) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 7; k < i + 1; k = k + 4) {
					if(gridValues[k - 4] == 0) {
						gridValues[k - 4] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m > 3; m = m - 4) {
						if(gridValues[m - 4] != 0 && gridValues[m - 4] != gridValues[i]) {
							break;
						}
						if(gridValues[m - 4] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m - 4] = 2*gridValues[m - 4];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
	}
	
	public void moveDown(int[] gridValues) { //code to move down
		cameHere = false;
		maxMerges = getMaxMergesVert(1);
		specialCase = specialCase("D", 1);
		for(int i = 8; i > -1; i = i - 4) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 8; k > i - 1; k = k - 4) {
					if(gridValues[k + 4] == 0) {
						gridValues[k + 4] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m < 12; m = m + 4) {
						if(gridValues[m + 4] != 0 && gridValues[m + 4] != gridValues[i]) {
							break;
						}
						if(gridValues[m + 4] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m + 4] = 2*gridValues[m + 4];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMergesVert(2);
		specialCase = specialCase("D", 2);
		for(int i = 9; i > 0; i = i - 4) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 9; k > i - 1; k = k - 4) {
					if(gridValues[k + 4] == 0) {
						gridValues[k + 4] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m < 13; m = m + 4) {
						if(gridValues[m + 4] != 0 && gridValues[m + 4] != gridValues[i]) {
							break;
						}
						if(gridValues[m + 4] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m + 4] = 2*gridValues[m + 4];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMergesVert(3);
		specialCase = specialCase("D", 3);
		for(int i = 10; i > 1; i = i - 4) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 10; k > i - 1; k = k - 4) {
					if(gridValues[k + 4] == 0) {
						gridValues[k + 4] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m < 14; m = m + 4) {
						if(gridValues[m + 4] != 0 && gridValues[m + 4] != gridValues[i]) {
							break;
						}
						if(gridValues[m + 4] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m + 4] = 2*gridValues[m + 4];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
		cameHere = false;
		maxMerges = getMaxMergesVert(4);
		specialCase = specialCase("D", 4);
		for(int i = 11; i > 2; i = i - 4) {
			if(cameHere) {
				specialCase = false;
			}
			if(gridValues[i] != 0) {
				
				for(int k = 11; k > i - 1; k = k - 4) {
					if(gridValues[k + 4] == 0) {
						gridValues[k + 4] = gridValues[i];
						gridValues[i] = 0;
						break;
					}
					for(int m = i; m < 12; m = m + 4) {
						if(gridValues[m + 4] != 0 && gridValues[m + 4] != gridValues[i]) {
							break;
						}
						if(gridValues[m + 4] == gridValues[i] && maxMerges > 0) {
							if(maxMerges == 1 && specialCase) {
								cameHere = true;
								break;
							}
							gridValues[m + 4] = 2*gridValues[m + 4];
							gridValues[i] = 0;
							maxMerges--;
							break;
						}
					}
				}
				
			}
		}
	}
	
	public boolean canMove(int[] gridValues) { //checks if you can still move (otherwise the game is over)
		boolean moveLeft = true;
		boolean moveRight = true;
		boolean moveUp = true;
		boolean moveDown = true;
		
		
		moveLeft = canMove("L");
		moveRight = canMove("R");
		moveUp = canMove("U");
		moveDown = canMove("D");
		
		if(!moveLeft && !moveRight && !moveUp && !moveDown) {
			return false;
		}
		return true;
	}
	
	public void fixGrid() { //when checking if you can move, I have to try moving it but then return it back to what it was before
		for(int i = 0; i < 16; i++) {
			gridValues[i] = holdGrid[i];
		}
	}
	
	public boolean canMove(String direction) { //checks if you can move in the direction passed in 
		for(int i = 0; i < 16; i++) {
			holdGrid[i] = gridValues[i];
		}
		if(direction.equals("L")) {
			moveLeft(gridValues);
			for(int i = 0; i < 16; i++) {
				if(holdGrid[i] != gridValues[i]) {
					fixGrid();
					return true;
				}
			}
		}
		if(direction.equals("R")) {
			moveRight(gridValues);
			for(int i = 0; i < 16; i++) {
				if(holdGrid[i] != gridValues[i]) {
					fixGrid();
					return true;
				}
			}
		}
		if(direction.equals("U")) {
			moveUp(gridValues);
			for(int i = 0; i < 16; i++) {
				if(holdGrid[i] != gridValues[i]) {
					fixGrid();
					return true;
				}
			}
		}
		if(direction.equals("D")) {
			moveDown(gridValues);
			for(int i = 0; i < 16; i++) {
				if(holdGrid[i] != gridValues[i]) {
					fixGrid();
					return true;
				}
			}
		}
		return false;
	}
}
