package application;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
 /**
  * 
  * <h1>Player.java</h1>
  * Controls and defines the action of players,their initial assignment
  * @author Dhruv Bhagat 2016146
  * @author Sushant Kumar Singh 2016103
  *
  */
public class Player implements Runnable, Serializable{
	Game curGame;
	String color;
	final int id;
	int numOrbs;
	boolean started;
	Grid grid;
	int numMoves;
	

	transient Socket socket;
	transient Scanner input;
	transient PrintWriter output;
	/**
	 * Setter method for color
	 * @param c First Parameter,denotes color
	 */
	void setcolor(String c){
		this.color=c;
	}
	/**
	 * Getter method for color
	 * @return color returns colors string value
	 */
	
	String getColor(){
		return color;
	}
	/**
	 * 
	 * getter moethod for numorbs
	 * @return numorbs return the number of orbs on screen
	 */
	
	int get_numorbs() {
		return this.numOrbs;
	}
	
	public Player(int id, String color, Game game){
		this.id = id;
		numOrbs = 0;
		this.color = color;
		this.curGame = game;
		this.started = false;
	}
	

	/**
	 * sets the socket, input and output streams for the connected client to the server for LAN game
	 * @param socket First parameter, socket to which client is connected while playing on LAN
	 */
	public void setSocket( Socket socket){
		try{
			this.socket = socket;
			input = new Scanner(new InputStreamReader(socket.getInputStream()) );
			output = new PrintWriter(socket.getOutputStream(), true);
			output.println("CONNECTED");
		}catch (IOException e){
			System.out.println("player "+id+" disconnected");
		}
	}
	
	
	/**
	 * getter method for player id
	 * @return game_id ,returns game id of the player
	 */
	public int getId(){
		return this.id;
	}
	/**
	 * Check on whether this player has lost the game
	 * @return boolean,true if player has lost ; false otherwise
	 */
	
	public boolean gameLost(){
		return numOrbs==0 && numMoves>0;
	}
	/**
	 * Check on whether this player has won the game
	 * @return boolean,true if player has won ; false otherwise
	 */
	
	public boolean checkWin(){
		if( this.numOrbs == curGame.curGrid.curTotalOrbs && numMoves>1){
		
			curGame.ui.gameOverAction(this.id);
			
			if(curGame.online){
				curGame.lanPlayer.output.println("EXIT");
			}
			
			return true;
		}
		return false;
	}
	/**
	 * increments the variables like (number of orbs for this player,number of moves) and seeks the next turn
	 * 
	 */
	public void makeMove(){
		curGame.curGrid.curTotalOrbs++;
		numMoves++;
		numOrbs++;
		//System.out.println("makeMove");
		//checkWin();
		curGame.nextTurn();
		//System.out.println("madeMove");
	}
	/**
	 * increases orb count by num
	 * @param num First parameter,denotes value to be incremented
	 */
	public void increaseOrbs(int num){
		numOrbs+=num;
	}
	/**
	 * decreases orb count by num
	 * @param num First parameter,denotes value to be decremented
	 */
	public void decreaseOrbs(int num){
		numOrbs-=num;
	}
	
	
	/**
	 * this method is used to listen client request after generating a thread of player<br>
	 * Thread is generated, started by the server
	 */
	@Override
	public void run() {
		int row, col;
		String command;
		try{
			while(true){
				command = input.next();
				if(command.equals("EXIT")){
					System.out.println("quit");
					for(int i = 1; i<=curGame.numPlayers; i++){
						curGame.currentPlayers[i].output.println("EXIT");
					}
					return;
				}
				row = input.nextInt();
				col = input.nextInt();
				for(int i = 1; i<=curGame.numPlayers; i++){
					curGame.currentPlayers[i].output.println("MOVE");
					curGame.currentPlayers[i].output.println(row);
					curGame.currentPlayers[i].output.println(col);
				}
			}
		}catch(Exception e){
			
		}
		finally{
			try {
				socket.close();
			} catch (Exception e) {
			}
		}
	}
}