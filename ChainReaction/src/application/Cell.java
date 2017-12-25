package application;

import java.io.Serializable;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;

import javafx.scene.paint.PhongMaterial;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;

import javafx.util.Duration;
/**
 * <h1> Cell.java </h1>
 * This class replicates all the cells on the grid,while playing the game and thus provides the user the facility
 * to click on it,this class is responsible for displaying of spheres,getting next turn,moreover the animations
 * are also implemented in this class itself.
 * @author Dhruv Bhagat 2016146
 * @author Sushant Kumar Singh 2016103
 *
 */



public class Cell extends Pane implements Serializable, Cloneable{

	String mycolor;
	
	
	Sphere[] sph= new Sphere[3];
	Player owner;
	Game curGame;
	Grid curGrid;
	
	
	int count=0;
	int myrow;
	int mycol;
	char type;
	double w, h;
	/**
	 * Constructor that Initializes the Cell with its type,row,column, dimension ,its game and its grid.
	 * @param type type of cell
	 * @param mrow its row
	 * @param mcol its column 
	 * @param dimension its dimension 
	 * @param g its present game
	 * @param curGrid its present grid
	 */
	
	public Cell(char type,int mrow,int mcol, int[] dimension, Game g, Grid curGrid) {
		w = dimension[0]; h = dimension[1];
	this.type=type;
	this.mycol=mcol;
	this.myrow=mrow;
	String str=g.settings.getColorHex(1);
	setStyle("-fx-border-color: "+str); 
	
	
	setPrefSize(100.0f, 100.0f);
	this.curGame = g;
	this.curGrid = curGrid;
	this.sph[0]=new Sphere(12);
	this.sph[1]=new Sphere(12);
	this.sph[2]=new Sphere(12);
	setOnMouseClicked(e -> handleMouseClick(false));
	
	}
	
	/**
	 * Method that internally calls setCellStyle as defined by game
	 * @param str This is the String parameter that holds the string equivalent of color to be put,according to the turn<br>
	 * 
	 * 
	 */
	
	void setCellStyle(String str) {
		
		curGame.setCellStyle(str);
		
		
	}
	/**
	 * It sets the materials of the Sphere according to the color provided which in turn is in accordance with the current player
	 * @param newColor This is the first parameter of setmaterialSpheres 
	 */
	void setmaterialSpheres(String newColor) {
		
		PhongMaterial material = new PhongMaterial();
		//System.out.println(game.getTurn().getId());
		//System.out.println(Color.valueOf(game.getTurn().getColor()));
		material.setDiffuseColor(Color.valueOf(this.owner.getColor()));
		this.sph[0].setMaterial(material);
		this.sph[1].setMaterial(material);
		this.sph[2].setMaterial(material);  
		
	}
	/**
	 * This method defines the things to do after the Splitting animation has finished
	 * @param pt This is the first parameter of makeavailable
	 */
	
	public void makeavailable(ParallelTransition pt){
	count=0;	
	this.getChildren().clear();
	curGrid.funct(this.myrow,this.mycol,this.type,this.owner,false);	
	}
	/**
	 * This method is the root method which defines how the spheres are placed inside the cells,how they are splitting<br>
	 * This method also checks for the splitting conditions and splits according to the animation logic defined in this function itself <br>
	 * 
	 * 
	 * @param isVirtual The first parameter,Check on whether player is real or virtual
	 * @param curPlayer The Second paramter,denotes the current player
	 */
	
	public void increaseMass(boolean isVirtual, Player curPlayer){
		//System.out.println("hey");
		//System.out.println(count);
		//System.out.println(owner+" this is owner");
		if(owner!=curPlayer){
			if (owner!=null){
				this.owner.decreaseOrbs(count);
			}
			
			curPlayer.increaseOrbs(count);
		}
		//System.out.println(owner);
		count++;
		
		this.owner = curPlayer;
		//System.out.println(owner+" this is new owner");
		setmaterialSpheres("#ff0000");
		
	    if(count==1) {
	    	
	    	
			sph[0].setTranslateX(w/2);
			sph[0].setTranslateY(h/2);
			Circle path = new Circle(w/2, h/2, 1);
		     PathTransition pt = new PathTransition();
		     pt.setPath(path);
		     pt.setNode(sph[0]);
		     pt.setInterpolator(Interpolator.LINEAR);
		     pt.setDuration(Duration.seconds(1));
		     //ORTHOGONAL_TO_TANGENT
		     pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		     pt.setCycleCount(Timeline.INDEFINITE);
		     pt.play();
		    RotateTransition rt = new RotateTransition(Duration.millis(3000), sph[0]);
		    rt.setByAngle(180);
		    rt.setCycleCount(4);
		    rt.setAutoReverse(true);
			getChildren().add(sph[0]);
			
		}
	  
	    if(count==2&&type!='c') {
	    	//getChildren().remove(sph[0]);
	    	
			sph[1].setTranslateX(w/3);
			sph[1].setTranslateY(h/3);
			Circle path = new Circle(0.6*w, 0.6*h, 8);
		     PathTransition pt = new PathTransition();
		     pt.setPath(path);
		     pt.setNode(sph[1]);
		     pt.setInterpolator(Interpolator.LINEAR);
		     pt.setDuration(Duration.seconds(3));
		     pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		     pt.setCycleCount(Timeline.INDEFINITE);
		     pt.play();
		    RotateTransition rt = new RotateTransition(Duration.millis(3000), sph[1]);
		    rt.setByAngle(180);
		    rt.setCycleCount(4);
		    rt.setAutoReverse(true);
		    getChildren().add(sph[1]);
		    
	    	}
	    	
		
	    

		if(count==3&&type=='n') {
			//getChildren().remove(sph[1]);
			getChildren().remove(sph[0]);
			getChildren().remove(sph[1]);
			

			sph[0].setTranslateX(w/2);
			sph[0].setTranslateY(h/2);
			Circle path = new Circle(0.7*w, 0.5*h, 8);
		     PathTransition pt = new PathTransition();
		     pt.setPath(path);
		     pt.setNode(sph[0]);
		     pt.setInterpolator(Interpolator.LINEAR);
		     pt.setDuration(Duration.seconds(2));
		     //ORTHOGONAL_TO_TANGENT
		     pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		     pt.setCycleCount(Timeline.INDEFINITE);
		     pt.play();
		    RotateTransition rt = new RotateTransition(Duration.millis(3000), sph[0]);
		    rt.setByAngle(180);
		    rt.setCycleCount(4);
		    rt.setAutoReverse(true);
		    rt.play();
			getChildren().add(sph[0]);
			
			
			
			sph[1].setTranslateX(w/3);
			sph[1].setTranslateY(h/3);
			Circle path1 = new Circle(0.5*w, 0.7*h, 8);
		     PathTransition pt1 = new PathTransition();
		     pt1.setPath(path1);
		     pt1.setNode(sph[1]);
		     pt1.setInterpolator(Interpolator.LINEAR);
		     pt1.setDuration(Duration.seconds(2));
		     pt1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		     pt1.setCycleCount(Timeline.INDEFINITE);
		     pt1.play();
		    RotateTransition rt1 = new RotateTransition(Duration.millis(3000), sph[1]);
		    rt1.setByAngle(180);
		    rt1.setCycleCount(4);
		    rt1.setAutoReverse(true);
		    getChildren().add(sph[1]);
		    
			
			
			
			
			
			
			sph[2].setTranslateX(w-30);
			sph[2].setTranslateX((h+30)/4);
			Circle path2 = new Circle(0.7*w, 0.7*h, 8);
		     PathTransition pt2 = new PathTransition();
		     pt2.setPath(path2);
		     pt2.setNode(sph[2]);
		     pt2.setInterpolator(Interpolator.LINEAR);
		     pt2.setDuration(Duration.seconds(2));
		     pt2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		     pt2.setCycleCount(Timeline.INDEFINITE);
		     pt2.play();
		    RotateTransition rt2 = new RotateTransition(Duration.millis(3000), sph[2]);
		    rt2.setByAngle(180);
		    rt2.setCycleCount(4);
		    rt2.setAutoReverse(true);
			getChildren().add(sph[2]); 
		}

		curGame.setCellStyle("#"+Color.valueOf(curGame.getTurn().getColor()).toString().substring(2));

		if(isVirtual && ((count==4&&type=='n') || (count==2&&type=='c') ||  (count==3&&type=='b')) ){
			this.count = 0;
			getChildren().clear();
			curGrid.funct(this.myrow,this.mycol,this.type,this.owner, true);
		}
		else{
		   if(count==4&&type=='n') {
			   //System.out.println(this.owner+" yhi");
				 count=0;
				   
			   getChildren().clear();
			  
			   	Sphere[] spha =new Sphere[4];
			   	
			   	spha[0]=new Sphere(12);
			   	spha[1]=new Sphere(12);
			   	spha[2]=new Sphere(12);
			   	spha[3]=new Sphere(12);
			   	//PhongMaterial material = new PhongMaterial();
				
				spha[0].setMaterial(this.sph[0].getMaterial());
				spha[1].setMaterial(this.sph[0].getMaterial());
				spha[2].setMaterial(this.sph[0].getMaterial());
				spha[3].setMaterial(this.sph[0].getMaterial());
				
			   	spha[0].setTranslateX(w/2);
			   	spha[0].setTranslateY(h/2);
			   	
			   	spha[1].setTranslateX(w/2);
			   	spha[1].setTranslateY(h/2);
			   	
			   	spha[2].setTranslateX(w/2);
			   	spha[2].setTranslateY(h/2);
			   	
			   	spha[3].setTranslateX(w/2);
			   	spha[3].setTranslateY(h/2);
		   	
			     TranslateTransition tt = new TranslateTransition(Duration.millis(200), spha[0]);
			     tt.setByX(w);
			     TranslateTransition tt1 = new TranslateTransition(Duration.millis(200), spha[1]);
			     tt1.setByX(-w);
			     TranslateTransition tt2 = new TranslateTransition(Duration.millis(200), spha[2]);
			     tt2.setByY(h);
			     TranslateTransition tt3 = new TranslateTransition(Duration.millis(200), spha[3]);
			     tt3.setByY(-h);
			     
			     ParallelTransition pt = new ParallelTransition(tt,tt1,tt2,tt3);
			       		    
			     
				this.getChildren().add(spha[0]);
					this.getChildren().add(spha[1]);
					this.getChildren().add(spha[2]);
					this.getChildren().add(spha[3]);
					pt.statusProperty().addListener(
						(o, oldStatus, newStatus) -> 
						curGrid.disableListeners(newStatus == Animation.Status.RUNNING));
								
					pt.play();
					//pu.play();
					pt.setOnFinished(e->makeavailable(pt));				
				}		   
	
		   if(count==2&&type=='c') {
			   getChildren().clear();
			   Sphere[] spha =new Sphere[2];
			   	spha[0]=new Sphere(12);
			   	spha[1]=new Sphere(12);
				
			   	spha[0].setMaterial(this.sph[0].getMaterial());
				spha[1].setMaterial(this.sph[0].getMaterial());
				
				spha[0].setTranslateX(w/2);
			   	spha[0].setTranslateY(h/2);
			   	
			   	spha[1].setTranslateX(w/2);
			   	spha[1].setTranslateY(h/2);
			   	
			   	TranslateTransition tt = new TranslateTransition(Duration.millis(200), spha[0]);
			     TranslateTransition tt1 = new TranslateTransition(Duration.millis(200), spha[1]);
			   	if(this.myrow==0&&this.mycol==0) {
			   		
			   		tt.setByX(w);
			   		tt1.setByY(h);
			   	}
			   	else if(this.myrow==curGrid.gridSize[0]-1&&this.mycol==0) {
			   		tt.setByX(w);
			   		tt1.setByY(-h);
			   		
			   	}
			   	else if(this.mycol==curGrid.gridSize[1]-1&&this.myrow==0) {
			   		tt.setByX(-w);
			   		tt1.setByY(h);
			   	}
			   	else {
			   		tt.setByX(-w);
			   		tt1.setByY(-h);
			   	}
			   	
			    
			    
			     
			     
			     ParallelTransition pt = new ParallelTransition(tt,tt1);

				pt.statusProperty().addListener(
						(o, oldStatus, newStatus) -> 
						curGrid.disableListeners(newStatus == Animation.Status.RUNNING));
			     pt.play();
			 	this.getChildren().add(spha[0]);
				this.getChildren().add(spha[1]);
			   count=0;
			   pt.setOnFinished(e->makeavailable(pt));
			  
		   }
	
		   if(count==3&&type=='b') {
			   getChildren().clear();
			   Sphere[] spha =new Sphere[3];
			   	spha[0]=new Sphere(12);
			   	spha[1]=new Sphere(12);
			   	spha[2]=new Sphere(12);
			   	
			   	spha[0].setMaterial(this.sph[0].getMaterial());
				spha[1].setMaterial(this.sph[0].getMaterial());
				spha[2].setMaterial(this.sph[0].getMaterial());
				
				spha[0].setTranslateX(w/2);
			   	spha[0].setTranslateY(h/2);
			   	
			   	spha[1].setTranslateX(w/2);
			   	spha[1].setTranslateY(h/2);
			   	
			   
			   	
			   	spha[2].setTranslateX(w/2);
			   	spha[2].setTranslateY(h/2);
			   	
			    TranslateTransition tt = new TranslateTransition(Duration.millis(200), spha[0]);
			     TranslateTransition tt1 = new TranslateTransition(Duration.millis(200), spha[1]);
			     TranslateTransition tt2 = new TranslateTransition(Duration.millis(200), spha[2]);
			     
			   	
			   	
			     
			     if(this.myrow==0) {
			    	 tt.setByX(-w);
			    	 tt1.setByY(+h);
			    	 tt2.setByX(+w);
			    	
			     }
			     else if(this.mycol==0) {
			    	 tt.setByY(h);
			    	 tt1.setByX(+w);
			    	 tt2.setByY(-h);
			     }
			     else if(this.mycol==curGrid.gridSize[1]-1) {
			    	 tt.setByY(h);
			    	 tt1.setByX(-w);
			    	 tt2.setByY(-h);
			    	 
			     }
			     else {
			    	 tt.setByX(-w);
			    	 tt1.setByY(-h);
			    	 tt2.setByX(+w);
			    	 
			     }
			     ParallelTransition pt = new ParallelTransition(tt,tt1,tt2);

				pt.statusProperty().addListener(
						(o, oldStatus, newStatus) -> 
						curGrid.disableListeners(newStatus == Animation.Status.RUNNING));
			     pt.play();
			 	this.getChildren().add(spha[0]);
				this.getChildren().add(spha[1]);
				this.getChildren().add(spha[2]);
			   count=0;
			   pt.setOnFinished(e->makeavailable(pt));
		   }
		}
		owner.checkWin();
	}
	/**
	 * This function gets the turn from the game class and defines its owner accordingly which in turn is responsible for making the<br>
	 * the current move and calling the function increase mass which has the logic for addition/deletion/animation of orbs.<br>
	 * This also saves the current moves internally <br>
	 * @param isVirtual The first paramter,defines whether player is virtual or real
	 */
	
	public void handleMouseClick(boolean isVirtual) {
		
		if( this.count == 0 ){
			this.owner = curGame.getTurn();
		
		}
		if ( this.owner.getId() == curGame.getTurn().getId() ){
			//add this valid move to history
			this.curGame.addToHistory(this.myrow, this.mycol);
			this.owner.makeMove();
			if(curGame.online){
				if(curGame.getTurn().getId() == curGame.lanPlayer.playerNum){
					System.out.println("myturn "+curGame.getTurn().getId());
					curGame.curGrid.disableListeners(false);
				}	
			}
			increaseMass(isVirtual, this.owner);	
		}
		
	}
	
	/**
	 * When the game is played on LAN this method writes the step taken to server
	 */
	
	public void handleMouseClickClient(){
		if ( this.count == 0 || this.owner.getId() == curGame.getTurn().getId() ){
			System.out.println("taking move");
				curGame.lanPlayer.output.println("MOVE");
				curGame.lanPlayer.output.println(myrow);
				curGame.lanPlayer.output.println(mycol);			

				curGrid.disableListeners(true);
		}
	}
	
	
	/**
	 * utility for performing click by the client while playing on LAN
	 */
	public void clicked(){
		Platform.runLater( ()->handleMouseClick(false) );
	}
	
	/**
	 * changes the listener of cell when the game is played on LAN so that handleMouseClient is new listener which writes move to server
	 */
	
	public void changeListener(){
		this.setOnMouseClicked(e -> handleMouseClickClient() );
	}
}