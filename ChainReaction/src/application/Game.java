package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import javafx.collections.ObservableList;
import javafx.scene.Node;
/**
 * <h1>Game.java</h1>
 * It is used for initial assignment of all the needed resources
 * This class is static and is an helper to all other classes in the package it defines the game<br>
 * It has methods to save the state of game,methods to replay the history(helper to resume and undo)
 * @author Dhruv Bhagat 2016146
 *@author Sushant Kumar Singh 2016103
 */

public class Game implements Serializable{
	private static Game game;	
	
	private final static File f = new File("./save");
	public final static int MAX_NUM_PLAYERS = 8;
	public final static int[] SMALL_GRID = {9, 6};
	public final static int[] LARGE_GRID = {15, 10};
	
	public int numPlayers;
	public int turnIndex;
	public int[] gridSize;
	public Settings settings;
	
	boolean online;
	transient Client lanPlayer;
	transient Server gameServer;
	transient Socket socket;
	transient Scanner input;
	transient PrintWriter output;
	
	public ArrayList<History> history;
	public  Player[] currentPlayers; // pseudo-index-1 based array so index 0 holds null
	
	public transient Grid curGrid;
	
	transient UIController ui;
	
	/**
	 * Sets the color of all the cells in accordance with the current move
	 * @param str This is the first parameter,represents color
	 */
	
	void setCellStyle(String str){
		for(int i=0;i<curGrid.gridSize[0];i++) {
		for(int j=0;j<curGrid.gridSize[1];j++) {
			curGrid.cells[i][j].setStyle("-fx-border-color: "+str); ;
		}
		}
		}
	/**
	 * This method returns a new game object if presently game object is not null
	 * Useful for initialization
	 * @return Game Returns game object
	 */

	
	
	public static Game getGame(){
		if(game==null){
			for( String file: f.list() ){
				if( file.equals("SavedGame.txt") ){
					try {
						return Game.deserialize();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			game = new Game();
		}
		return game;
	}
	
	private Game(){
		this.settings = new Settings();
		
	}
	/**
	 * Takes Input of the UIController and initializes all the variables need,called at the start of the game
	 * 
	 * @param ui First parameter,holds the grid object,grid pane,etc as defined by fxml.
	 */
	public void playNewGame( UIController ui ){
		this.ui = ui;
		
		online = false;
		numPlayers = settings.numPlayers;
		gridSize = settings.gridSize;
		currentPlayers = new Player[numPlayers+1];
		history = new ArrayList<>();
		for(int i = 1; i<=numPlayers; i++){
			currentPlayers[i] = new Player(i,settings.getColorHex(i), this);
		}
		curGrid = new Grid(gridSize, ui, this);
				
		//start the game by giving turn to player 1
		turnIndex = 1;		
	}
	
	
	
	/**
	 * host a server for playing a game on LAN, and later join the hosted game
	 */
	public void hostGame(){
		try {
			gameServer = new Server(this);
			// spawn a thread to start a server
			Thread serverGame = new Thread( gameServer );
			// run the server game
			serverGame.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * join the hosted game on LAN
	 * @param ipAddress, String ip address of the server
	 */
	public void joinLanGame(String ipAddress){
		online = true;
		try {
			lanPlayer =  new Client(this, ipAddress);
			Thread lanGame = new Thread(lanPlayer);
			lanGame.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/**
	 * This method Responsible for making moves in the game internally useful for undo and resume
	 * @param past First parameter,arraylist storing the moves made
	 */
	
	public void replayHistory(ArrayList<History> past){
		for(History h: past){
			this.curGrid.virtualMove(h.row, h.col);
		}
	}
	/**
	 * This method is responsible for saving moves made by the user at each step
	 * @param row First parameter,takes row number of the cell
	 * @param col Second parameter,takes column number of the cell
	 */
	public void addToHistory(int row, int col){
		History tmp = new History(row, col);
		this.history.add(tmp);
	}
	/**
	 * Returns the saved ArrayList
	 * @return ArrayList<History> 
	 */
	
	
	public ArrayList<History> getHistory(){
		return this.history;
	}
	/**
	 * This method returns the current player
	 * @return Player  
	 */
	public Player getTurn(){
		if(currentPlayers[turnIndex].gameLost()){
			nextTurn();
			getTurn();
		}
		return currentPlayers[turnIndex];
	}
	/**
	 * This method is responsible for increment of turnIndex after turn has been made
	 */
	public void nextTurn(){
		turnIndex = (turnIndex)%numPlayers + 1;

	}
	/**
	 * This method Serializes the current state of game
	 * @throws IOException
	 */
	
	public void serialize() throws IOException{
		ObjectOutputStream out = null;
		try{
			out = new ObjectOutputStream ( new FileOutputStream("./save/SavedGame.txt"));
			out.writeObject(this);
			
		} finally{
			out.close();
		}
		System.out.println("serialized");
	}
	/**
	 * This method deserializes and returns the saved game
	 * @return Game
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	
	public static Game deserialize() throws IOException, ClassNotFoundException{
		ObjectInputStream in = null;
		Game game;
		try{
			in = new ObjectInputStream ( new FileInputStream("./save/SavedGame.txt"));
			game = (Game) in.readObject();
		} finally{
			in.close();
		}
		return game;
	}
}