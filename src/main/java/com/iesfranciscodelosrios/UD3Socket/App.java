package com.iesfranciscodelosrios.UD3Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stagemain;
    static double x,y = 0;
    
    @Override
    public void start(Stage stage) throws IOException {
    	Parent root = loadFXML("controller/Login");
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.getIcons().add(new Image("Zorro1.png"));
        stage.setTitle("Scrammer Bank");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stagemain = stage;
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });        
        stage.show();
    }

    /**
     * Cambio de pantalla con la funcionalidad de poder moverla.
     * @param fxml el cual va a mostrarse.
     * @throws IOException
     */
    public static void setRoot(String fxml) throws IOException {
    	try {
    		Parent root = loadFXML(fxml);
    		scene.setRoot(root);
    		
    		root.setOnMousePressed(event -> {
    			x = event.getSceneX();
    			y = event.getSceneY();
    		});
    		
    		 root.setOnMouseDragged(event -> {
    	            stagemain.setX(event.getScreenX() - x);
    	            stagemain.setY(event.getScreenY() - y);
	        }); 
		} catch (Exception e) {
			System.out.println();
		}
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}