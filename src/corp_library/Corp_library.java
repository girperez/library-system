/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corp_library;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Gerryl
 */
public class Corp_library extends Application {
    private static Stage rootStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/files_FXML/FXML_login.fxml"));
        setRootStage(stage);
        Scene scene = new Scene(root,Color.TRANSPARENT);
        
        stage.setScene(scene);
        stage.setTitle("Corp Library");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public static Stage getRootStage() {
        return rootStage;
    }

    public static void setRootStage(Stage rootStage) {
        Corp_library.rootStage = rootStage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
