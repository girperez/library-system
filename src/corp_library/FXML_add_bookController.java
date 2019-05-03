/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corp_library;

import custom_components.Alert_message;
import custom_components.LoadingScreen;
import database.Database_programCL;
import database.dataFields.Book_dat;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class FXML_add_bookController implements Initializable {
     
    @FXML
    AnchorPane rootadd;
//TextFields////////////////
    @FXML
    TextField bookidTF;
    @FXML
    TextField booktitleTF;
    @FXML
    TextField bookcopiesTF;
    
//Imageview////////////////
    @FXML
    ImageView imageIMV;
    
//ChooseBox/////////////
    @FXML
    ChoiceBox sectionCB;
//Labels///////////////
    @FXML
    Label notif;
    @FXML
    Label banner;
//Buttons/////////////////
    @FXML
    Button submitBTN;
//file chooser///////////
    private FileChooser fileChooser;
    private File imagefile;
    private Alert_message alert_message;
    private Stage alertstage;
    private InputStream imageInputStream;
    
//animations///////////
    private FadeTransition fadeTransition1;
    private FadeTransition fadeTransition2;
//program///////////
    private Service<Object> bookthread;
    private Task<Object> booktask;
    private Database_programCL program;
    private LoadingScreen loading;
    private String bookidString;
    private String titleString;
    private int copiesString;
    private String choosboxvalue;
    private byte [] img;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        program = new  Database_programCL();
        
        //initialize multi thread
        bookthread = new Service<Object>() {
            @Override
            protected Task<Object> createTask() {
                return booktask;
            }
        };
        
        bookthread.setOnSucceeded((event) -> {
            try {
                alertOK((String)bookthread.getValue());
                
            } catch (Exception e) {
                alertOK("Error " + e.toString());
            }finally{
                endloading();
                bookthread.reset();
            }
        });
        
        bookthread.setOnFailed((event) -> {
            alertOK("Threading Failure Occured");
            endloading();
            bookthread.reset();
        });
        
        
        //initialize components
        loading = new LoadingScreen();
        loading.setLayoutX(300);
        loading.setLayoutY(400);
        fileChooser = new FileChooser();
        imagefile = new File("src/images/image_defaultwhite.png");
        alert_message = new Alert_message();
        alertstage = new Stage(StageStyle.DECORATED);
        alertstage.initOwner(Corp_library.getRootStage());
        alertstage.initModality(Modality.APPLICATION_MODAL);
        alertstage.setResizable(false);
        alertstage.setScene(new Scene(alert_message));
        sectionCB.getItems().addAll("Fantasy", "Romance", "Thriller", "Mystery", "Horror");
        rootadd.getChildren().add(loading);
    }
    
//normal methods
    public void setsection(String section){
        sectionCB.getItems().add(section);
    }
    public void showloading(){
        rootadd.setDisable(true);
        rootadd.setOpacity(0.8);
        loading.setDisable(false);
        loading.setVisible(true);
        loading.startLoading();
    }
    
    public void endloading(){
        rootadd.setDisable(false);
        rootadd.setOpacity(1);
        loading.setDisable(true);
        loading.setVisible(false);
        loading.stopLoading();
    }
//Registration process
    public void registerProcess(){
        booktask = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
               showloading();
               String message = program.registerNewBook(new Book_dat(bookidString, titleString, choosboxvalue, imagefile, copiesString));
               return message;
            }
        };
        
        bookthread.start();
    }
//  Editing Process    
    public void editbook(Book_dat book) throws Exception{
        banner.setText("Edit Book");
        submitBTN.setText("Edit");
        
        bookidTF.setText(book.getBookID());
        bookidTF.setEditable(false);
        booktitleTF.setText(book.getBookTitle());
        bookcopiesTF.setText(Integer.toString(book.getCopies()));
        sectionCB.setValue(book.getSection());
        imageInputStream = (ByteArrayInputStream) book.getImageInputStream();
        img = new byte[imageInputStream.available()];
        imageInputStream.read(img);
        imageIMV.setImage(new Image(new ByteArrayInputStream(img)));
    }
    
    public void editProcess(){
        booktask = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
               showloading();
               String message = program.editBook(new Book_dat(bookidString, titleString, choosboxvalue,copiesString, new ByteArrayInputStream(img)));
               return message;
            }
        };
        
        bookthread.start();
    }
//animate methods   
    public void revealnotif(String message){
        fadeTransition1 = new FadeTransition(Duration.millis(300), notif);
        fadeTransition2 = new FadeTransition(Duration.millis(300), notif);
        notif.setVisible(true);
        notif.setText(message);
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
            if (banner.getText().equals("Register A Book") && submitBTN.getText().equals("Submit")){
                registerProcess();
            }else if (banner.getText().equals("Edit Book") && submitBTN.getText().equals("Edit")) {
                editProcess();
            }else{
                revealnotif("Process did not take place");
            }
            alertstage.close();
            bookcopiesTF.setText("");
            bookidTF.setText("");
            booktitleTF.setText("");
            sectionCB.getSelectionModel().clearSelection();
            imagefile = new File("src/images/image_defaultwhite.png");
        });
        
        alert_message.getCanceButton().setOnMouseClicked((ev) -> {
            alertstage.close();
        });
        alertstage.showAndWait();
    }
//FXML methods    
    @FXML
    private void chooseClicked(MouseEvent event){
        try {
            imagefile = fileChooser.showOpenDialog(new Stage(StageStyle.UTILITY));
            imageInputStream = new FileInputStream(imagefile);
            img = new byte[imageInputStream.available()];
            imageInputStream.read(img);
            if (imagefile.getName().contains(".png") || imagefile.getName().contains(".jpg")) {
                imageIMV.setImage(new Image(new ByteArrayInputStream(img)));
            }else{
                throw new Exception("file must be a PNG or JPG file");
            }
        }catch (NullPointerException el){
            if (imageInputStream != null) {
                revealnotif("Image is chosen by default");
            }else{
                revealnotif("Please choose an Image");
            }
        } catch (Exception e) {
            alertOK(e.toString());
        }
    }
    
    @FXML
    private void submitClicked(MouseEvent event){
        try {
            bookidString = bookidTF.getText();
            titleString = booktitleTF.getText();
            copiesString = Integer.parseInt(bookcopiesTF.getText());
            choosboxvalue = (sectionCB.getValue() == null) ? "" : sectionCB.getValue().toString();
            if (!bookidString.equals("") && !titleString.equals("") && copiesString != 0 && !choosboxvalue.equals("")) {
                if (bookidTF.getText().length() > 5) {
                    if (imageInputStream != null) {
                        alertconfirm("Are you sure about your entered informations?");
                    }else{
                        revealnotif("Please choose an image");
                    }
                    
                }else{
                    revealnotif("Book Id must be unique and more then 5 characters long");
                }
            }else{
                revealnotif("Plase fill up the missing fields");
            }
        }catch(NumberFormatException ne){
            revealnotif("Copies field should contain numbers");
            bookcopiesTF.setText("");
        }catch (Exception e) {
            alertOK("Error "+ e.toString());
        }
    }

}
