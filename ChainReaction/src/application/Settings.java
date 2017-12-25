
package application;



import java.io.Serializable;

import javafx.scene.paint.Color;
/**
 * 
 * <h1>Settings.java</h1>
 * Coordinates with the UI to look for selected colors and initializes players accordingly
 * @author Dhruv Bhagat 2016146
 * @author Sushant Kumar Singh 2016103
 *
 */


public class Settings implements Serializable{
	private static final long serialVersionUID = 1L;

	public final static int MAX_NUM_PLAYERS = 8;
	public final static int[] SMALL_GRID = {9, 6};
	public final static int[] LARGE_GRID = {15, 10};
	
	public int numPlayers = 2;
	public int[] gridSize = {9, 6};
	//initial colors assigned to each player
	private String[] colors = {null,		// index 1-8 hold color for players 1-8
									"#"+Color.RED.toString().substring(2),
									"#"+Color.GREEN.toString().substring(2),
									"#"+Color.BLUE.toString().substring(2),
									"#"+Color.YELLOW.toString().substring(2),
									"#"+Color.BLACK.toString().substring(2),
									"#"+Color.WHITE.toString().substring(2),
									"#"+Color.ORANGE.toString().substring(2),
									"#"+Color.PINK.toString().substring(2),			
									};

	// set color 
	/**
	 * Sets the colors of the player ,called during initialization of players
	 * @param color First parameter,denotes String value of color
	 * @param playerNumber Second parameter,denotes the PlayerNumber
	 * @throws NonUniqueColorException exception thrown when 2 or more players click/want to choose the same color
	 */
	public void setColor(String color, int playerNumber) throws NonUniqueColorException{
		for(int i = 1; i <= Game.MAX_NUM_PLAYERS; i++){
			if(colors[i].equals(color)){
				throw new NonUniqueColorException("Player "+i+" is already using this color("+color+")");
			}
		}
		colors[playerNumber] =  color;
	}

	

	// return color of the player #playerNumber
/**
 * getter method for color
 * @param playerNumber First Parameter,denotes the playernumber
 * @return color assigned to player in accordance with the player number
 */
	public String getColor(int playerNumber){
		return colors[playerNumber];
	}
	/**
	 * getter method for color in hex 
	 * @param playerNumber First Parameter,denotes the playernumber
	 * @return color assigned to player in accordance with the player number
	 */

	public String getColorHex(int playerNumber){
		return getColor(playerNumber);
	}
	/**
	 * getter method for number of players
	 * @return numplayers ,returns the total number of players playing
	 */
	public int getNumPlayers(){
		return this.numPlayers;
	}
	/**
	 * getter method for gridsize
	
	 * @return grid_size returns a 2 tuple denoting the grid size
	 */
	public int[] getGridSize(){
		return this.gridSize;
	}
}