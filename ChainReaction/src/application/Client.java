package application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javafx.animation.Animation;

public class Client implements Runnable {

	Game curGame;
	Socket socket;
	Scanner input;
	PrintWriter output;
	int playerNum;
	/**
	 * 
	 * Initializes the client with its current game
	 * @param curGame takes input of the current game
	 * @param ipAddress String address of server
	 * @throws IOException Inbuilt Exception
	 */
	
	public Client(Game curGame, String ipAddress) throws IOException{
		this.curGame = curGame;
		
		socket = new Socket(ipAddress, 8901);
		input = new Scanner(socket.getInputStream());
		output = new PrintWriter(socket.getOutputStream(), true);
		
		System.out.println("game joined");
		// stop listeners
		
	}
	
	public void run(){		

		try{		
			String command = input.nextLine();
			if( command.equals("CONNECTED") ){
				playerNum = input.nextInt();
			}
			curGame.curGrid.changeListeners();
			if(curGame.getTurn().getId() != playerNum){
				curGame.curGrid.disableListeners(true);
			}	
			int row, col;
			while(true){
				command = input.next();
				if(command.equals("EXIT")){
					return;
				}
				row = input.nextInt();	
				System.out.println("client got row "+row);

				col = input.nextInt();
				System.out.println("client got col "+col);
				curGame.curGrid.cells[row][col].clicked();
			}
		}
		catch(Exception e){
			
		}
		finally{
			try {
				System.out.println("closed client");
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("exited client "+playerNum);
		}

	}
}
