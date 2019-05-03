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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Gerryl
 */
public class FXML_loginController implements Initializable {

    @FXML
    AnchorPane rootpane;
    @FXML
    VBox mainVbox;
    @FXML
    Label AlertLabel;
    @FXML
    TextField UserTF;
    @FXML
    TextField PassTF;
    
    private Database_programCL program;
    private Stage alertstage;
    private Alert_message alert_message;
    private LoadingScreen loadingScreen;
    private Service<Account_dat> service;
    private Task<Account_dat> task;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        program = new Database_programCL();
        rootpane.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(300), rootpane);
        fade.setDelay(Duration.millis(500));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.playFromStart();
        
        PassTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                PassTF.setStyle(null);
                PassTF.setText("");
            }
        });
        UserTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                UserTF.setStyle(null);
                UserTF.setText("");
            }
        });
        
        mainVbox.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    confirmlogin();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        
        service = new Service<Account_dat>() {
            @Override
            protected Task<Account_dat> createTask() {
                return task;
            }
        };
        
        service.setOnSucceeded((event) -> {
            try {
                Account_dat accounttype = service.getValue();
                if (accounttype.getType().equals("Admin")) {
                    FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/files_FXML/corp_libraryFXML.fxml"));
                    Parent root1 = (Parent) adminLoader.load();
                    Corp_library.getRootStage().close();
                    Stage stage = new Stage();
                    Corp_library.setRootStage(stage);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setTitle("Corp Library");
                    stage.setScene(new Scene(root1,Color.TRANSPARENT));
                    stage.show();
                }else if (accounttype.getType().equals("Member")) {
                    FXMLLoader memberLoader = new FXMLLoader(getClass().getResource("/files_FXML/FXML_member_app.fxml"));

                    Parent root2 = (Parent) memberLoader.load();
                    FXML_member_appController membercontroller = memberLoader.<FXML_member_appController>getController();
                    membercontroller.setAccountInfo(accounttype);
                    Corp_library.getRootStage().close();
                    Stage stage = new Stage();
                    Corp_library.setRootStage(stage);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setTitle("Corp Library");
                    stage.setScene(new Scene(root2,Color.TRANSPARENT));
                    stage.show();
                }
                
            }catch (NullPointerException ne){
                System.out.println(ne);
                AlertLabel.setVisible(true);
                AlertLabel.setText(program.getMessages());
                AlertLabel.setTextFill(Color.RED);
            }catch (Exception e) {
                System.out.println(e);
                alertOK("Threading error occured");
            }finally{
                endloading();
                service.reset();
                PassTF.setText("");
            }
        });
        
        service.setOnFailed((event) -> {
            alertOK(program.getMessages());
            endloading();
            service.reset();
        });
        
        
        alert_message = new Alert_message();
        alertstage = new Stage(StageStyle.DECORATED);
        alertstage.initOwner(Corp_library.getRootStage());
        alertstage.initModality(Modality.APPLICATION_MODAL);
        alertstage.setResizable(false);
        alertstage.setScene(new Scene(alert_message));
        
        loadingScreen = new LoadingScreen();
        loadingScreen.setLayoutX(225);
        loadingScreen.setLayoutY(275);
        rootpane.getChildren().add(loadingScreen);
    }    
    
    // Normal Methods///////////////////////////////////////////////////////////////////////
    public void alertOK(String message){//alert window with OK botton
        alert_message.setMessage(message);
        alert_message.okButton();
        alert_message.getOkButton().setOnMouseClicked((eve) -> {
            alertstage.close();
        });
        alertstage.showAndWait();
    }
    
    public void showloading(){
        rootpane.setDisable(true);
        rootpane.setOpacity(0.8);
        loadingScreen.setDisable(false);
        loadingScreen.setVisible(true);
        loadingScreen.startLoading();
    }
    
    public void endloading(){
        rootpane.setDisable(false);
        rootpane.setOpacity(1);
        loadingScreen.setDisable(true);
        loadingScreen.setVisible(false);
        loadingScreen.stopLoading();
    }
    public void confirmlogin() throws Exception{
        if (!UserTF.getText().equals("") && !PassTF.getText().equals("")) {
            task = new Task<Account_dat>() {
                @Override
                protected Account_dat call() throws Exception {
                    showloading();
                    Account_dat confirm = program.loginAccount(UserTF.getText(), PassTF.getText());
                    return confirm;
                }
            };
            
            service.start();
            
        }else if (UserTF.getText().equals("") && PassTF.getText().equals("")) {
            AlertLabel.setVisible(true);
            AlertLabel.setText("Please fill up the Username and Password field");
            AlertLabel.setTextFill(Color.RED);
            PassTF.setStyle("-fx-border-color: red; -fx-border-width: 3");
            UserTF.setStyle("-fx-border-color: red; -fx-border-width: 3");
            
        } else if(UserTF.getText().equals("")) {
            AlertLabel.setVisible(true);
            AlertLabel.setText("Please fill up the Username field");
            AlertLabel.setTextFill(Color.RED);
            UserTF.setStyle("-fx-border-color: red; -fx-border-width: 3");
            
        } else if (PassTF.getText().equals("")) {
            AlertLabel.setVisible(true);
            AlertLabel.setText("Please fill up the Password field");
            AlertLabel.setTextFill(Color.RED);
            PassTF.setStyle("-fx-border-color: red; -fx-border-width: 3");
            
        }
    }
    
    
    // FXML Methods////////////////////////////////////////////////////////////////////////
    @FXML
    private void signinclicked(MouseEvent event){
        try {
            confirmlogin();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    @FXML
    private void closeclicked(MouseEvent event){
        Platform.exit();
    }
    
    @FXML
    private void signupclicked(MouseEvent event){
        try {
            Parent addmemberWindow = FXMLLoader.load(getClass().getResource("/files_FXML/FXML_register_member.fxml"));
            Stage addmemberStage = new Stage();
            addmemberStage.initModality(Modality.APPLICATION_MODAL);
            addmemberStage.initStyle(StageStyle.DECORATED);
            addmemberStage.setTitle("Register Member");
            addmemberStage.setScene(new Scene(addmemberWindow));
            addmemberStage.setResizable(false);
            addmemberStage.showAndWait();
        } catch (Exception e) {
            System.out.println("Failed to load admembers window");
        }
    }
}
