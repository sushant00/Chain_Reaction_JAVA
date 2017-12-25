

import com.sun.scenario.effect.Reflection;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;

public class game extends Application {
public static void main(String[] args) {
	launch(args);
}


private Cell[][] cell =new Cell[9][6];


@Override
public void start(Stage primaryStage) {
GridPane pane = new GridPane(); 
for (int i = 0; i < 9; i++)
for (int j = 0; j < 6; j++)
pane.add(cell[i][j] = new Cell(), j, i);
//#661a33
//#dc143c

pane.setStyle("-fx-background-color: linear-gradient(from 1% 1% to 100% 100%, #000000, #000000)");

BorderPane borderPane = new BorderPane();
borderPane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #661a33, #661a33)");

borderPane.setCenter(pane);


Scene scene = new Scene(borderPane, 540.0f, 540.0f);
primaryStage.setTitle("ChainReaction");
primaryStage.setScene(scene);
primaryStage.show();}
}

class Cell extends Pane {
int count=0;
private char token = ' '; // one of blank, X, or O
public Cell() {
setStyle("-fx-border-color: #661a33"); 
setPrefSize(100.0f, 100.0f);
setOnMouseClicked(e -> handleMouseClick());
}
private void handleMouseClick() {
	// TODO Auto-generated method stub
	count++;
	
	if(count<=3) {
	double w = getWidth(), h = getHeight();
	PhongMaterial material = new PhongMaterial();
// Uncomment this, and change the file path accordingly to see the rotation effect		
//	material.setDiffuseMap(
//			new Image(
//					"file:///D:/IMPORTANT/JAVA/labs/src/7.png",
//					true)
//
    material.setDiffuseColor(Color.valueOf("#66FF66"));
	Sphere sph=new Sphere(20);
	
	sph.setMaterial(material);
	int ans=(count%3)+1;
	System.out.println(ans);
	if(ans!=1) {
	sph.setTranslateX(w/ans); 
    sph.setTranslateY(h/ans);
	}
	else {
		ans=4;
		sph.setTranslateX(w-30); 
	    sph.setTranslateY((h+30)/ans);
	 
	}
//	Circle circle = new Circle();
	  //sph.setEffect(new javafx.scene.effect.Reflection());
//	  RotateTransition rt = new RotateTransition();
//      rt.setNode(sph);
//      rt.setFromAngle(0);
//      rt.setToAngle(360);
//      rt.setCycleCount(6);
//      rt.setInterpolator(Interpolator.LINEAR);
//      rt.setCycleCount(Timeline.INDEFINITE);
//      rt.setDuration(new Duration(30000));
//      getChildren().add(sph);
//      rt.play();
      
     // rt.pause();
      
//
//	Circle circle = new Circle();
//  
//    circle.setRadius(100);
//    circle.setFill(Color.YELLOW);
//    FillTransition ft = new FillTransition();
//    ft.setShape(circle);
//    ft.setDuration(new Duration(2000));
//    ft.setToValue(Color.GOLD);
//    ft.setCycleCount(Timeline.INDEFINITE);
//    ft.setAutoReverse(true);
//    ft.play();
//
//
//    getChildren().add(circle);
	
//    Rectangle rect = new Rectangle (100, 40, 100, 100);
//    rect.setArcHeight(50);
//    rect.setArcWidth(50);
//    rect.setFill(Color.VIOLET);


// Uncomment the following to see the revolution effect for animation.
// For final project We need to restart the animation after each click
// so that when orb count = 1 orb is at centre of cell,
//count = 2 orbs are at two opposite ends of an orbit around centre of cell
// count = 3 orbs are at three places on on orbit around centre of cell
		
//     Circle path = new Circle(w/2, h/2, 10);
//     PathTransition pt = new PathTransition();
//     pt.setPath(path);
//     pt.setNode(sph);
//     pt.setInterpolator(Interpolator.LINEAR);
//     pt.setDuration(Duration.seconds(3));
//     pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
//     pt.setCycleCount(Timeline.INDEFINITE);
//     pt.play();
		
    RotateTransition rt = new RotateTransition(Duration.millis(3000), sph);
    rt.setByAngle(180);
    rt.setCycleCount(4);
    rt.setAutoReverse(true);

    rt.play();


	
	getChildren().add(sph);
	//sph.setTranslateX(2);
   
    
	}
	
	
	
}
}



