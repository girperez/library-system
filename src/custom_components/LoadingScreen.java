/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_components;

import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author Gerryl
 */
public class LoadingScreen extends VBox{

    RotateTransition rotate;
    ImageView imageView;
    
    public LoadingScreen() {
        super.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        super.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        super.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        super.setAlignment(Pos.CENTER);
        super.setVisible(false);
        super.setDisable(true);
        
        imageView = new ImageView();
        imageView.setImage(new Image("/images/loading2.png"));
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        rotate = new RotateTransition(Duration.millis(1000), imageView);
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.setFromAngle(0);
        rotate.setToAngle(360);
        
        super.getChildren().add(imageView);
    }
    
    public void startLoading(){
        rotate.playFromStart();
    }
    
    public void stopLoading(){
        rotate.pause();
    }
}
