package application;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Runnable{
	
	Game curGame;
	transient ServerSocket hotspot;	
/**
 * Initializes the Server with its current game
 * @param curGame
 * @throws IOException Inbuilt Exception
 */
	public Server(Game curGame) throws IOException{
		this.curGame = curGame;
		hotspot = new ServerSocket(8901);		
	}
	
	public void run(){
		try{
			curGame.joinLanGame("localhost");		
			// wait for players
			for(int i = 1; i<=curGame.numPlayers; i++){
				try {
					curGame.currentPlayers[i].setSocket(hotspot.accept());
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("made a player"+i);
			}
			for(int i = 1; i<=curGame.numPlayers; i++){
				Thread playerThread = new Thread(curGame.currentPlayers[i]);
				System.out.println("started thread "+i);
				playerThread.start();
				curGame.currentPlayers[i].output.println(i);
			}
		}
		finally{
			try {
				System.out.println("closed hotspot");
				hotspot.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
