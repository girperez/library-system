/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Gerryl
 */
public class Alert_message extends VBox{
    
    private Button okButton;
    private Button canceButton;
    private Label messageLabel;
    private HBox buttoncontainer;

    public Alert_message() {
        super.setPrefSize(500, 200);
        super.setMinSize(500, 200);
        super.setMaxSize(500, 200);
        super.setSpacing(50);
        super.setAlignment(Pos.CENTER);
        super.getStylesheets().add(getClass().getResource("/css_files/alert.css").toString());
        //initialize components
        messageLabel = new Label("Error");
        messageLabel.setStyle("-fx-font-size: 14");
        messageLabel.setPrefWidth(500);
        messageLabel.setAlignment(Pos.CENTER);
        
        buttoncontainer = new HBox(20);
        buttoncontainer.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        buttoncontainer.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        buttoncontainer.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        buttoncontainer.setAlignment(Pos.CENTER);
        
        okButton = new Button("OK");
        okButton.setPrefSize(120, 40);
        okButton.setStyle("-fx-font-size: 24");
        
        canceButton = new Button("Cancel");
        canceButton.setPrefSize(120, 40);
        canceButton.setStyle("-fx-font-size: 24");
        
        super.getChildren().addAll(messageLabel, buttoncontainer);
    }

    public Button getOkButton() {
        return okButton;
    }

    public Button getCanceButton() {
        return canceButton;
    }
    
    public void setMessage(String message){
        messageLabel.setText(message);
    }
    
    public void okButton(){
        buttoncontainer.getChildren().clear();
        buttoncontainer.getChildren().add(okButton);
    }
    
    public void okcancelButton(){
        buttoncontainer.getChildren().clear();
        buttoncontainer.getChildren().addAll(okButton,canceButton);
    }
}
