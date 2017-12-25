package application;

import java.io.Serializable;

import java.util.ArrayList;

import javafx.animation.Animation.Status;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
/**
 * <h1>Grid.java</h1>
 * This class defines all the virtual moves,it initializes cells according to a check on there types
 * 
 * @author Dhruv Bhagat 2016146 
 * @author Sushant Kumar Singh 2016103
 *
 */
public class Grid  implements Serializable, Cloneable{
	Cell[][] cells;
	Cell[][] temp;
	int[] gridSize;
	int curTotalOrbs;	
	private static Grid previousGrid;
	public transient GridPane previousGridPane;
	public transient GridPane curGridPane;
	private Game curGame;
	private transient UIController ui;
	ArrayList<Object> forundo;
	/**
	 * Initializes Grid for the ui
	 * @param gridSize Gridsize initialization
	 * @param ui UIcontroller for the current grid
	 * @param game Game Object for the current grid
	 */
	public Grid(int[] gridSize, UIController ui, Game game){
		this.gridSize = gridSize;
		this.cells = new Cell[gridSize[0]][gridSize[1]];
		this.ui = ui;
		this.curGame = game;
		this.curTotalOrbs = 0;
		
		int[] cellDimension = {(int)ui.Pane_Grid.getWidth()/gridSize[1], (int)ui.Pane_Grid.getWidth()/gridSize[0]};
		
		curGridPane = new GridPane();
		//previousGridPane=new GridPane();
		char type;
		for (int i = 0; i < gridSize[0]; i++) {
			for (int j = 0; j < gridSize[1]; j++){
				if(i==0&&j==0||( i==(gridSize[0]-1)&&j==0) || (i==(gridSize[0]-1)&&j==(gridSize[1]-1)) ||i==0&&j==(gridSize[1]-1)) {
					type='c';
				}
				else if(i==0||i==(gridSize[0]-1)||j==0||j==(gridSize[1]-1)) {
					type='b';
				}
				else {
					type='n';//boundary
				}
				curGridPane.add(cells[i][j] = new Cell(type,i,j, cellDimension, game, this), j, i);		
			}
		}
		addToUi(curGridPane, ui);
		
	}
	
	/**
	 * Adds the current grid to ui as a child of pane grid defined by the ui
	 * @param grid
	 * @param ui
	 */
	// Add the grid to ui
	private void addToUi(GridPane grid, UIController ui){
		ui.Pane_Grid.getChildren().clear();
		
		ui.Pane_Grid.getChildren().add(grid);
		AnchorPane.setRightAnchor(grid, .0);
		AnchorPane.setTopAnchor(grid, .0);
		AnchorPane.setLeftAnchor(grid, .0);
		AnchorPane.setBottomAnchor(grid, .0);	
	}
	/**
	 * Responsible for making an internal move on ith row and jth column
	 * @param i First Parameter,row number of cell
	 * @param j Second Parameter,column number of cell
	 */
	
	
	public void virtualMove(int i, int j){
		cells[i][j].handleMouseClick(true);
	}
	
	
	
	
	/**
	 * Disables Listeners when orbs are splitting
	 * @param s First Paramter,denotes the Status of the Splitting animation
	 */
	public void disableListeners(boolean s){
		for (int i = 0; i < gridSize[0]; i++) {
			for (int j = 0; j < gridSize[1]; j++){
				cells[i][j].setDisable( s );
				if (!s){
					curGame.setCellStyle("#"+Color.valueOf(curGame.getTurn().getColor()).toString().substring(2));
				}
			}
		}
	}
	
	/**
	 * changeListeners of all the cells when game is played on LAN
	 */
	public void changeListeners(){
		for (int i = 0; i < gridSize[0]; i++) {
			for (int j = 0; j < gridSize[1]; j++){
				cells[i][j].changeListener();
			}
		}
	}
	
	
	
	/**
	 * Responsible for making a virtual move internally calling corresponding cell,according to checks based on cell
	 * types and their positions in the grid
	 * @param i First parameter,denotes row
	 * @param j Second parameter,denotes column
	 * @param type Third Parameter,denotes the type of cell(based on its position)
	 * @param p Fourth Paramter,denotes the Player 
	 * @param isVirtual,denotes the virtual or real check.
	 */

	public void funct(int i, int j,char type,Player p, boolean isVirtual) {
		
		//System.out.println(i+" "+j);
		
		int rMax = gridSize[0]-1; int cMax = gridSize[1]-1;
		
		if(type=='n') { 
//		this.cells[i][j+1].owner=this.cells[i][j].owner;
		//this.cells[i][j].owner=p;
		
		this.cells[i][j+1].increaseMass(isVirtual, p);
		
		this.cells[i-1][j].increaseMass(isVirtual, p);
		this.cells[i+1][j].increaseMass(isVirtual, p);
		this.cells[i][j-1].increaseMass(isVirtual, p);
		
		}
		
		if(type=='c') {
			if(i==0&&j==0) {
				this.cells[0][1].increaseMass(isVirtual, p);
				this.cells[1][0].increaseMass(isVirtual, p);
			}
			else if(i==0&&j==cMax) {
				this.cells[0][cMax-1].increaseMass(isVirtual, p);
				this.cells[1][cMax].increaseMass(isVirtual, p);
			}
			else if(i==rMax&&j==0) {
				this.cells[rMax-1][0].increaseMass(isVirtual, p);
				this.cells[rMax][1].increaseMass(isVirtual, p);
				
			}
			else if(i==rMax&&j==cMax) {
				this.cells[rMax][cMax-1].increaseMass(isVirtual, p);
				this.cells[rMax-1][cMax].increaseMass(isVirtual, p);
			}		
		}
		
		
		
		if(type=='b') {
			
			if(i==0||i==rMax) {
			this.cells[i][j+1].increaseMass(isVirtual, p);
			this.cells[i][j-1].increaseMass(isVirtual, p);
			if(i==0) {
			this.cells[i+1][j].increaseMass(isVirtual, p);
			
			
			}
			else {
				this.cells[i-1][j].increaseMass(isVirtual, p);	
			}
			}
			
			else {
				this.cells[i-1][j].increaseMass(isVirtual, p);
				this.cells[i+1][j].increaseMass(isVirtual, p);
				if(j==0) {
				this.cells[i][j+1].increaseMass(isVirtual, p);
				}
				else {
					this.cells[i][j-1].increaseMass(isVirtual, p);	
				}
				
			}
			
		}	
		
	}
	
}