/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_components;

import corp_library.Corp_library;
import database.Database_programCL;
import database.dataFields.BookRent_dat;
import database.dataFields.Member_dat;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Gerryl
 */
public class MembersSectionPane extends Pane{
//Vbox//////////////
    private VBox leftvbox;
    private VBox rightVBox;
//Labels///////////
    //  Static Labels
    private Label memberIdLabel;
    private Label firstnameLabel;
    private Label lastnameLabel;
    private Label contactLabel;
    private Label emailLabel;
    private Label bookspossesion;
    
    //  labels information got from database
    private Label infomemberIdLabel;
    private Label infofirstnameLabel;
    private Label infolastnameLabel;
    private Label infocontactLabel;
    private Label infoemailLabel;
    
//Buttons/////////////////////////
    private Button editButton;
    
//List view//////////////////////
    private ListView<String> possesionbooks;
    private ObservableList<String> listobserevable;
    
//textfields///////////////////
    private TextField editmemID;
    private TextField editfname;
    private TextField editlname;
    private TextField editcontact;
    private TextField editemail;
    
//others///////////////////////
    private Database_programCL program;
    private ArrayList<BookRent_dat> bookrent;
    private Member_dat member;
    private Alert_message alert_message;
    private Stage alertstage;
    private Member_dat proxmem;
    
//multithread///////////////
    private Service<Object> service;
    private Task<Object> task;

//animation////////////////
    private FadeTransition fade;
    private TranslateTransition translate;
    
    public MembersSectionPane(Member_dat member, ArrayList<BookRent_dat> bookrent) throws Exception{
        super();
        super.getStyleClass().add("divider-down");
        super.setPrefSize(200, 200);
        
        this.member = member;
        this.bookrent = bookrent;
        
        //initialize thread
        service = new Service<Object>() {
            @Override
            protected Task<Object> createTask() {
                return task;
            }
        };
        service.setOnSucceeded((event) -> {
            try {
                alertOK(service.getValue().toString());
                proxmem = program.getMember(this.member.getUsername());
                infofirstnameLabel.setText(proxmem.getFirstname());
                infolastnameLabel.setText(proxmem.getLastname());
                infocontactLabel.setText(proxmem.getContact());
                infoemailLabel.setText(proxmem.getEmail());
                editButton.setText(">");
                editButton.setAlignment(Pos.TOP_CENTER);
                rightVBox.getChildren().clear();
                rightVBox.getChildren().addAll(infomemberIdLabel, infofirstnameLabel,infolastnameLabel,infocontactLabel,infoemailLabel);
                animateedit();
            } catch (Exception e) {
                alertOK("Exeption occured");
            }finally{
                service.reset();
            }
        });
        
        service.setOnFailed((event) -> {
            alertOK("Threading Error Ocurred");
            service.reset();
        });
        
        //initialize alert message
        alert_message = new Alert_message();
        alertstage = new Stage(StageStyle.DECORATED);
        alertstage.initOwner(Corp_library.getRootStage());
        alertstage.initModality(Modality.APPLICATION_MODAL);
        alertstage.setResizable(false);
        alertstage.setScene(new Scene(alert_message));
        
        //initialize components
        
        leftvbox = new VBox(10);
        leftvbox.setPrefSize(USE_COMPUTED_SIZE, 200);
        leftvbox.setMinSize(USE_COMPUTED_SIZE, 200);
        leftvbox.setMaxSize(USE_COMPUTED_SIZE, 200);
        leftvbox.setAlignment(Pos.CENTER_LEFT);
        leftvbox.setLayoutX(14);
        leftvbox.setLayoutY(0);
        
        memberIdLabel = new Label("Member ID :");
        firstnameLabel = new Label("First name : ");
        lastnameLabel = new Label("Last name : ");
        contactLabel = new Label("Contact no. :");
        emailLabel = new Label("Email :");
        
        leftvbox.getChildren().addAll(memberIdLabel, firstnameLabel, lastnameLabel, contactLabel, emailLabel);
        
        rightVBox = new VBox(10);
        rightVBox.setPrefSize(USE_COMPUTED_SIZE, 200);
        rightVBox.setMinSize(USE_COMPUTED_SIZE, 200);
        rightVBox.setMaxSize(USE_COMPUTED_SIZE, 200);
        rightVBox.setAlignment(Pos.CENTER_LEFT);
        rightVBox.setLayoutX(206);
        rightVBox.setLayoutY(0);
        
        infomemberIdLabel = new Label(this.member.getUsername());
        infofirstnameLabel = new Label(this.member.getFirstname());
        infolastnameLabel = new Label(this.member.getLastname());
        infocontactLabel = new Label(this.member.getContact());
        infoemailLabel = new Label(this.member.getEmail());
        
        editmemID = new TextField();
        editmemID.setStyle("-fx-font-size: 12px;");
        editmemID.setEditable(false);
        editfname = new TextField();
        editfname.setStyle("-fx-font-size: 12px;");
        editfname.setPromptText("First name");
        editlname = new TextField();
        editlname.setStyle("-fx-font-size: 12px;");
        editlname.setPromptText("Last name");
        editcontact = new TextField();
        editcontact.setStyle("-fx-font-size: 12px;");
        editcontact.setPromptText("Contact number");
        editemail = new TextField();
        editemail.setStyle("-fx-font-size: 12px;");
        editemail.setPromptText("E-mail Address");
        
        rightVBox.getChildren().addAll(infomemberIdLabel, infofirstnameLabel, infolastnameLabel, infocontactLabel, infoemailLabel);
        
        bookspossesion = new Label("Books in possession");
        bookspossesion.setLayoutX(565);
        bookspossesion.setLayoutY(24);
        
        listobserevable = FXCollections.observableArrayList();
        for(BookRent_dat items : this.bookrent){
            listobserevable.add(items.getBookID());
        }
        possesionbooks = new ListView<>(listobserevable);
        possesionbooks.setPrefSize(330, 120);
        possesionbooks.setEditable(false);
        possesionbooks.setLayoutX(565);
        possesionbooks.setLayoutY(56);
        
        editButton = new Button(">");
        editButton.setAlignment(Pos.TOP_CENTER);
        editButton.getStyleClass().add("buttons-members");
        editButton.setStyle("-fx-font-size: 24");
        editButton.setLayoutX(475);
        editButton.setLayoutY(126);
        editButton.setOnMouseClicked((event) -> {
            if (editButton.getText().equals(">")) {
                editButton.setText("Edit");
                editButton.setAlignment(Pos.CENTER);
                editmemID.setText(infomemberIdLabel.getText());
                editfname.setText(infofirstnameLabel.getText());
                editlname.setText(infolastnameLabel.getText());
                editcontact.setText(infocontactLabel.getText());
                editemail.setText(infoemailLabel.getText());
                rightVBox.getChildren().clear();
                rightVBox.getChildren().addAll(editmemID, editfname,editlname,editcontact,editemail);
                animateedit();
            }else if (editButton.getText().equals("Edit")) {
                editmember();
            }
        });
        
        ConstructComponents();
    }
    
    public void ConstructComponents(){
        super.getChildren().addAll(leftvbox, rightVBox, editButton, possesionbooks, bookspossesion);
    }
    
    public void editmember(){
        try {
            String memid = editmemID.getText();
            String fnString = editfname.getText();
            String lnString = editlname.getText();
            String cString = editcontact.getText();
            String emString = editemail.getText();
            
            task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                if (!memid.equals("")) {
                    program = new Database_programCL();
                    
                    proxmem = new Member_dat(fnString, lnString, emString, cString, memid, member.getImageInputStream());
            
                    String message = program.editMember(proxmem);
                    
                    return message;
                }else{
                    return "Member is not yet assigned";
                }
            }
        };
        
        service.start();
        
        } catch (Exception e) {
            alertOK("Editing member Error occured");
        }
    }
    
    public void animateedit(){
        fade = new FadeTransition(Duration.millis(200), rightVBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        
        translate = new TranslateTransition(Duration.millis(300), rightVBox);
        translate.setFromX(-100);
        translate.setToX(0);
        
        fade.playFromStart();
        translate.playFromStart();
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
            
        });
        
        alert_message.getCanceButton().setOnMouseClicked((ev) -> {
            alertstage.close();
        });
        alertstage.showAndWait();
    }
}
