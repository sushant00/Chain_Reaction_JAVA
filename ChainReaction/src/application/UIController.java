package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UIController implements Initializable, Serializable {	
	Game game;
	boolean justChangedSettings;
	private Cell[][] cells;
	
	
	final String IDLE_BUTTON_STYLE = "-fx-background-color: #ffffff;-fx-text-fill:  #1895af;";
	final String HOVERED_BUTTON_STYLE = "-fx-background-color: #1895af;-fx-text-fill:  #ffffff;";
	
	
	Grid prev;

	@FXML
	public AnchorPane Pane_Home, Pane_Settings, Pane_Grid, Pane_GameOver, Pane_Join;
	
	@FXML
	public GridPane smallGrid, largeGrid, previous, start;
		
	@FXML
	private ImageView btn_home, btn_quit, btn_settings, btn_dropDown, btn_undo;
	
	@FXML
	private Button btn_newGame, btn_resume, btn_playAgain, btn_host, btn_join, btn_joinLAN;
	
	@FXML
	private MenuButton btn_dropMenu;
	
	
	@FXML
	private ChoiceBox<String> chBox_numPlayers, chBox_gridSize;
	
	@FXML
	private ColorPicker colorPick1, colorPick2, colorPick3, colorPick4, colorPick5, colorPick6, colorPick7, colorPick8;
	
	@FXML
	private Label label_gameOver;
	
	@FXML
	private TextField textField_ip;
	
	@FXML
	private Circle sphere_Blue, sphere_Red, sphere_Yellow, sphere_Green; //color combination inspired from google assistant
	
	//btn_home clicked action
	
	/**
	 * sets the view to home page after making other panes invisible
	 */
	@FXML
	public void home(){
		Pane_Home.setVisible(true);
		Pane_Settings.setVisible(false);
		Pane_Grid.setVisible(false);
		Pane_GameOver.setVisible(false);
		Pane_Join.setVisible(false);
		btn_dropDown.setVisible(false);
		btn_undo.setVisible(false);
		btn_settings.setDisable(false);
		if(game.history.size()!=0&& !justChangedSettings){
			btn_resume.setVisible(true);
		}
		else{
			btn_resume.setVisible(false);
		}
	} 
	
	//btn_settings clicked action

	/**
	 * sets the view to settings page after making other panes invisible
	 */
	@FXML
	public void settings(){		
		Pane_Home.setVisible(false);
		Pane_Settings.setVisible(true);	
		Pane_Grid.setVisible(false);
		Pane_GameOver.setVisible(false);
		Pane_Join.setVisible(false);
		btn_dropDown.setVisible(false);
		btn_undo.setVisible(false);	
		btn_settings.setDisable(false);
	}    
	
	
	//btn_quit clicked action

	/**
	 * quit the game after serializing it
	 * @throws IOException Inbuilt Exception
	 */
	@FXML
	public void quit() throws IOException{
		game.serialize();
				
		((Stage) btn_quit.getScene().getWindow()).close();
		
		
	}
	



	/**
	 * undo the last move
	 */
	@FXML
	public void undo(){
		// get the copy of currently stored history
		
		
		if(game.getHistory().size()>=1) {
		ArrayList<History> copy =  new ArrayList<History>(game.getHistory());
		newGame();
		
		System.out.println("undoing "+copy.size());
		
		//delete the last move of history so that all but last step are rePlayed
		
		copy.remove(copy.size()-1);
		// replay all the moves in the history
		
		game.replayHistory(copy);
		}
		
		else {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Undo is unavailable");
			alert.setHeaderText("Facility of undo is temporary unavailable");
			alert.setContentText("Click one or more cells first");

			alert.showAndWait();
			System.out.println("Undo is unavailable");
		}
	}
		
		
	/**
	 * fire the drop down menu on page while game is playing
	 */
	@FXML
	public void dropDown(){
		btn_dropMenu.fire();		
	}

	/**
	 * host a LAN game
	 */
	@FXML
	public void hotspot(){
		newGame();
		btn_undo.setDisable(true);
		btn_settings.setDisable(true);
		game.hostGame();
	}
	
	/**
	 * join a LAN game
	 */
	@FXML
	public void join(){
		Pane_Join.setVisible(true);
	}
	
	@FXML
	public void startLANGame(){
		newGame();
		btn_undo.setDisable(true);
		btn_settings.setDisable(true);
		game.joinLanGame(textField_ip.getText());
	}
	
	/**
	 * play a new game, calls playNewGame of Game class after setting view of game board
	 */
	@FXML
	public void newGame(){
		// clear the grid pane
		Pane_Grid.getChildren().clear();
		
		Pane_Home.setVisible(false);
		Pane_Settings.setVisible(false);
		Pane_Grid.setVisible(true);
		Pane_GameOver.setVisible(false);
		Pane_Join.setVisible(false);
		btn_dropDown.setVisible(true);
		btn_undo.setVisible(true); 
		btn_settings.setDisable(true);		

		justChangedSettings = false;
		
		game.playNewGame(this);			
	}
	
	/**
	 * resume last played game
	 * @throws ClassNotFoundException Inbuilt exception
	 * @throws IOException Inbuilt exception 
	 */
	@FXML
	public void resumeGame() throws ClassNotFoundException, IOException{
		ArrayList<History> copy =  new ArrayList<History>(game.getHistory());
	
		
		newGame();
		
		this.game.replayHistory(copy);
		
		
		
	}
	
	
	@FXML
	public void mouseEntered_newGame(){
	    btn_newGame.setStyle(HOVERED_BUTTON_STYLE);
	}
	@FXML
	public void mouseExited_newGame(){
	    btn_newGame.setStyle(IDLE_BUTTON_STYLE);
	}
	
	@FXML
	public void mouseEntered_playAgain(){
	    btn_playAgain.setStyle(HOVERED_BUTTON_STYLE);
	}
	
	@FXML
	public void mouseExited_playAgain(){
	    btn_playAgain.setStyle(IDLE_BUTTON_STYLE);
	}
	
	@FXML
	public void mouseEntered_resume(){
	    btn_resume.setStyle(HOVERED_BUTTON_STYLE);
	}
	@FXML
	public void mouseExited_resume(){
	    btn_resume.setStyle(IDLE_BUTTON_STYLE);
	}

	
	@FXML
	public void mouseEntered_host(){
	    btn_host.setStyle(HOVERED_BUTTON_STYLE);
	}
	@FXML
	public void mouseExited_host(){
	    btn_host.setStyle(IDLE_BUTTON_STYLE);
	}
	
	@FXML
	public void mouseEntered_join(){
	    btn_join.setStyle(HOVERED_BUTTON_STYLE);
	}
	@FXML
	public void mouseExited_join(){
	    btn_join.setStyle(IDLE_BUTTON_STYLE);
	}
	@FXML
	public void mouseEntered_joinLAN(){
	    btn_joinLAN.setStyle(HOVERED_BUTTON_STYLE);
	}
	@FXML
	public void mouseExited_joinLAN(){
	    btn_joinLAN.setStyle(IDLE_BUTTON_STYLE);
	}
	
	/**
	 * This method gets the color picker whose value changed and update it with settings class
	 * @param event The event of changing value of color picker
	 */
	@FXML
	public void action_colorPick(ActionEvent event){
		// mark that the settings have been changed
		justChangedSettings = true;
		ColorPicker tmp = (ColorPicker)event.getSource();
		// get the playerNumber of player to which color picker corresponds to
		int playerNumber = Integer.parseInt( tmp.getId().substring(9) );
		try{			
			game.settings.setColor("#"+tmp.getValue().toString().substring(2), playerNumber);
		}
		catch(NonUniqueColorException e){	
			tmp.setValue(Color.valueOf(game.settings.getColor(playerNumber)));
		}
	}
	
	/**
	 * update the game class with new number of players choosed
	 * @param e The even of changing the number of players
	 */
	@FXML
	public void action_chBox_numPlayers(Event e){
		 ChoiceBox<String> clicked = (ChoiceBox<String>)e.getSource();
		 game.settings.numPlayers = Integer.parseInt(clicked.getValue().substring(0,1)) ;
	}
	
	/**
	 *  update the game class with new size of grid choosed
	 * @param e The even of changing the size of grid
	 */
	@FXML
	public void action_chBox_gridSize(Event e){
		 ChoiceBox<String> clicked = (ChoiceBox<String>)e.getSource();
		 if ( clicked.getValue().equals("9 x 6") ){
			 game.settings.gridSize = Game.SMALL_GRID;
		 }else{	
			 game.settings.gridSize = Game.LARGE_GRID;
		 }
	}
	
	/**
	 * View the winning view with name of player of who won and
	 * @param playerNumber The id of player who won the game
	 */
	public void gameOverAction(int playerNumber){		
		btn_undo.setVisible(false);
		if(!game.online){
			label_gameOver.setText("Player "+playerNumber+" won !!");
			label_gameOver.setStyle("-fx-text-alignment: center; -fx-text-fill: "+game.settings.getColorHex(playerNumber)+";");
			
			
		}else{
			if(game.lanPlayer.playerNum==playerNumber)
				label_gameOver.setText("You won !!");
			else
				label_gameOver.setText("You loose !!");
			label_gameOver.setStyle("-fx-text-alignment: center; -fx-text-fill: "+game.settings.getColorHex(game.lanPlayer.playerNum)+";");
		}
		Pane_GameOver.setVisible(true);
	}
	 
    @Override
    public void initialize(URL location, ResourceBundle resources) {    	
//    	
    	Circle forRed = new Circle(-150,50,170);
    	Circle forGreen = new Circle(-200,200,320);
    	Circle forYellow = new Circle(-100,50,150);
//   	
		PathTransition pt = new PathTransition();
		pt.setPath(forRed);
		pt.setNode(sphere_Red);
		pt.setInterpolator(Interpolator.LINEAR);
		pt.setDuration(Duration.seconds(100));
		pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt.setCycleCount(Timeline.INDEFINITE);
		
		pt.play();
    	
//    	
		PathTransition pt1 = new PathTransition();
		pt1.setPath(forGreen);
		pt1.setNode(sphere_Green);
		pt1.setInterpolator(Interpolator.LINEAR);
		pt1.setDuration(Duration.seconds(40));
		pt1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt1.setCycleCount(Timeline.INDEFINITE);
		
		pt1.play();

    	
		PathTransition pt2 = new PathTransition();
		pt2.setPath(forYellow);
		pt2.setNode(sphere_Yellow);
		pt2.setInterpolator(Interpolator.LINEAR);
		pt2.setDuration(Duration.seconds(200));
		pt2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt2.setCycleCount(Timeline.INDEFINITE);
		
		pt2.play();
		
    	game = Game.getGame();
    	
    	chBox_numPlayers.setOnAction(this::action_chBox_numPlayers);
    	chBox_gridSize.setOnAction(this::action_chBox_gridSize);
    	
	    btn_newGame.setStyle(IDLE_BUTTON_STYLE);
	    btn_playAgain.setStyle(IDLE_BUTTON_STYLE);	    
	    btn_resume.setStyle(IDLE_BUTTON_STYLE);	    
	    btn_host.setStyle(IDLE_BUTTON_STYLE);	    
	    btn_join.setStyle(IDLE_BUTTON_STYLE);	    
	    btn_joinLAN.setStyle(IDLE_BUTTON_STYLE);
	    chBox_gridSize.setStyle(IDLE_BUTTON_STYLE);chBox_gridSize.setTooltip(new Tooltip("Grid Size"));
	    chBox_numPlayers.setStyle(IDLE_BUTTON_STYLE);chBox_numPlayers.setTooltip(new Tooltip("Number of Players"));
	    
	    chBox_numPlayers.setValue(game.settings.getNumPlayers()+" Players");
	    int[] tmpGridSize = game.settings.getGridSize();
	    chBox_gridSize.setValue(tmpGridSize[0] + " x " + tmpGridSize[1]);

	    colorPick1.setValue(Color.valueOf(game.settings.getColor(1)));
	    colorPick2.setValue(Color.valueOf(game.settings.getColor(2)));
	    colorPick3.setValue(Color.valueOf(game.settings.getColor(3)));
	    colorPick4.setValue(Color.valueOf(game.settings.getColor(4)));
	    colorPick5.setValue(Color.valueOf(game.settings.getColor(5)));
	    colorPick6.setValue(Color.valueOf(game.settings.getColor(6)));
	    colorPick7.setValue(Color.valueOf(game.settings.getColor(7)));
	    colorPick8.setValue(Color.valueOf(game.settings.getColor(8)));
	    
	    
		if(game.history == null || game.history.size()==0){
			btn_resume.setVisible(false);
		}
		else{
			btn_resume.setVisible(true);
		}
	    
    }	
}