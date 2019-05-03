/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corp_library;

import custom_components.Alert_message;
import custom_components.LoadingScreen;
import database.Database_programCL;
import database.dataFields.Account_dat;
import database.dataFields.Book_dat;
import database.dataFields.Member_dat;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Gerryl
 */
public class FXML_register_memberController implements Initializable {

    @FXML
    AnchorPane root;
    
    @FXML
    TextField fnameTF;
    @FXML
    TextField lnameTF;
    @FXML
    TextField mobileTF;
    @FXML
    TextField emailTF;
    @FXML
    TextField userTF;
    @FXML
    PasswordField passTF;
    @FXML
    PasswordField confirmpassTF;
    
    @FXML
    Button submitBTN;
    
    @FXML
    Label popuplabel;
    
//other variables
    private File defaultimageFile;
    private Alert_message alert_message;
    private Stage alertstage;
    private FadeTransition fadeTransition1;
    private FadeTransition fadeTransition2;
    private LoadingScreen loading;
    private Service<Object> memberthred;
    private Task<Object> membertask;
    private Database_programCL program;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            program = new  Database_programCL();
        
        //initialize multi thread
            memberthred = new Service<Object>() {
                @Override
                protected Task<Object> createTask() {
                    return membertask;
                }
            };

            memberthred.setOnSucceeded((event) -> {
                try {
                    alertOK((String)memberthred.getValue());

                } catch (Exception e) {
                    alertOK("Error " + e.toString());
                }finally{
                    endloading();
                    memberthred.reset();
                }
            });

            memberthred.setOnFailed((event) -> {
                alertOK("Threading Failure Occured");
                endloading();
                memberthred.reset();
            });


            //initialize components
            loading = new LoadingScreen();
            loading.setLayoutX(300);
            loading.setLayoutY(445);
            defaultimageFile = new File("src/images/blank_pic.png");
            alert_message = new Alert_message();
            alertstage = new Stage(StageStyle.DECORATED);
            alertstage.initOwner(Corp_library.getRootStage());
            alertstage.initModality(Modality.APPLICATION_MODAL);
            alertstage.setResizable(false);
            alertstage.setScene(new Scene(alert_message));
            root.getChildren().add(loading);
        } catch (Exception e) {
            alertOK("Error initialization");
        }
    }    
    
    //normal methods
    
    public void registerProcess(){
        membertask = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
               showloading();
               Member_dat member = new Member_dat(fnameTF.getText(), lnameTF.getText(), emailTF.getText(), mobileTF.getText(), userTF.getText(), defaultimageFile);
               Account_dat account = new Account_dat(userTF.getText(), "Member", passTF.getText());
               String message = program.registerNewMember(member, account);
               return message;
            }
        };
        
        memberthred.start();
    }
    
    public void showloading(){
        root.setDisable(true);
        root.setOpacity(0.8);
        loading.setDisable(false);
        loading.setVisible(true);
        loading.startLoading();
    }
    
    public void endloading(){
        root.setDisable(false);
        root.setOpacity(1);
        loading.setDisable(true);
        loading.setVisible(false);
        loading.stopLoading();
    }
    
    public void revealnotif(String message){
        fadeTransition1 = new FadeTransition(Duration.millis(300), popuplabel);
        fadeTransition2 = new FadeTransition(Duration.millis(300), popuplabel);
        popuplabel.setVisible(true);
        popuplabel.setText(message);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);
        fadeTransition1.playFromStart();
        fadeTransition1.setOnFinished((event) -> {
            fadeTransition2.setDelay(Duration.millis(3000));
            fadeTransition2.setFromValue(1);
            fadeTransition2.setToValue(0);
            fadeTransition2.playFromStart();
        });
    }
    
    public void alertOK(String message){//alert window with OK botton
        alert_message.setMessage(message);
        alert_message.okButton();
        alert_message.getOkButton().setOnMouseClicked((eve) -> {
            alertstage.close();
        });
        alertstage.showAndWait();
    }
 // confirming the fields if ok   
    public void alertconfirm(String message){//alert for application exit prompt
        alert_message.setMessage(message);
        alert_message.okcancelButton();
        alert_message.getOkButton().setText("Yes");
        alert_message.getOkButton().setOnMouseClicked((eve) -> {
            registerProcess();
            alertstage.close();
            userTF.setText("");
            passTF.setText("");
            confirmpassTF.setText("");
        });
        
        alert_message.getCanceButton().setOnMouseClicked((ev) -> {
            alertstage.close();
        });
        alertstage.showAndWait();
    }
    
    @FXML
    private void submitclicked(MouseEvent event){
        
        if (!fnameTF.getText().equals("") && !lnameTF.getText().equals("") && !mobileTF.getText().equals("") && !emailTF.getText().equals("") &&
                !userTF.getText().equals("") && !passTF.getText().equals("") && !confirmpassTF.getText().equals("")){
            
            if (passTF.getText().equals(confirmpassTF.getText())) {
                alertconfirm("Are you sure you want to continue?");
            }else{
                revealnotif("Password and Confirmation does not match");
            }
            
        }else{
            revealnotif("Please fill up the missing fields");
        }
    }
}
