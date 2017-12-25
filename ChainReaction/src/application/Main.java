package application;
/**
 * <h1>Main.java</h1>

 * Responsible for initializing UI and setting the stage for the game
 * @author Dhruv Bhagat 2016146
 * @author Sushant Kumar Singh 2016103
 */
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;


public class Main extends Application {
		
	private double x_pos = 0.0;
	private double y_pos = 0.0;
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {			
			Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
			
			// Enable dragging
			root.setOnMousePressed(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
					x_pos = e.getSceneX();
					y_pos = e.getSceneY();
				}				
			});
			root.setOnMouseDragged(new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
					primaryStage.setX(e.getScreenX() - x_pos);
					primaryStage.setY(e.getScreenY() - y_pos);
				}
			});			

			primaryStage.initStyle(StageStyle.TRANSPARENT);
			
			Scene scene = new Scene(root);
			scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
