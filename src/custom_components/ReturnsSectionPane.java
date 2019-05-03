/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_components;

import corp_library.Corp_library;
import database.Database_programCL;
import database.dataFields.BookRent_dat;
import database.dataFields.Book_dat;
import database.dataFields.Member_dat;
import database.dataFields.Payment_dat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.text.DateFormatter;

/**
 *
 * @author Gerryl
 */
public class ReturnsSectionPane extends Pane{
    //Vbox//////////////
    private VBox leftvbox1;
    private VBox leftvbox2;
    private VBox rightVBox1;
    private VBox rightVBox2;
//Labels///////////
    //  Static Labels leftvbox1
    private Label booktitleLabel;
    private Label bookidLabel;
    private Label booksectionLabel;
    private Label memberusernameLabel;
    // info Labels leftvbox2
    private Label infobooktitle;
    private Label infobookID;
    private Label infobooksection;
    private Label infomemberuser;
    
    //  Static Labels rightvbox1
    private Label dateofleaseLabel;
    private Label dateofreturnLabel;
    private Label penaltyLabel;
    //info labels rightvbox2
    private Label infodatelease;
    private DatePicker infodatereturn;
    private Label infopenalty;
//checkbox//////////////////////
    private CheckBox checkBox;
    
//others///////////////////////
    private Database_programCL program;
    private BookRent_dat bookrent;
    private Alert_message alert_message;
    private Stage alertstage;
    private DateTimeFormatter returndate;
    private SimpleDoubleProperty doubleProperty;
    private Double penaltyDouble = 0.00;
    private Payment_dat payment_dat;
    
    public ReturnsSectionPane(BookRent_dat bookrent) throws Exception{
        super();
        super.getStyleClass().add("divider-down");
        super.setPrefSize(200, 200);
        
        this.bookrent = bookrent;
        doubleProperty = new SimpleDoubleProperty(penaltyDouble);
        program = new Database_programCL();
        
        //initialize alert message
        alert_message = new Alert_message();
        alertstage = new Stage(StageStyle.DECORATED);
        alertstage.initOwner(Corp_library.getRootStage());
        alertstage.initModality(Modality.APPLICATION_MODAL);
        alertstage.setResizable(false);
        alertstage.setScene(new Scene(alert_message));
        
        //initialize components
        
        leftvbox1 = new VBox(10);
        leftvbox1.setPrefSize(USE_COMPUTED_SIZE, 200);
        leftvbox1.setMinSize(USE_COMPUTED_SIZE, 200);
        leftvbox1.setMaxSize(USE_COMPUTED_SIZE, 200);
        leftvbox1.setAlignment(Pos.CENTER_LEFT);
        leftvbox1.setLayoutX(14);
        leftvbox1.setLayoutY(0);
        
        booktitleLabel = new Label("Book Title :");
        bookidLabel = new Label("Book ID : ");
        booksectionLabel = new Label("Section : ");
        memberusernameLabel = new Label("Customer ID :");
        
        leftvbox1.getChildren().addAll(booktitleLabel, bookidLabel, booksectionLabel, memberusernameLabel);
        
        leftvbox2 = new VBox(10);
        leftvbox2.setPrefSize(USE_COMPUTED_SIZE, 200);
        leftvbox2.setMinSize(USE_COMPUTED_SIZE, 200);
        leftvbox2.setMaxSize(USE_COMPUTED_SIZE, 200);
        leftvbox2.setAlignment(Pos.CENTER_LEFT);
        leftvbox2.setLayoutX(193);
        leftvbox2.setLayoutY(0);
        
        Book_dat book = program.getBook(this.bookrent.getBookID());
        
        infobooktitle = new Label(book.getBookTitle());
        infobookID = new Label(book.getBookID());
        infobooksection = new Label(book.getSection());
        infomemberuser = new Label(this.bookrent.getUsername());
        
        leftvbox2.getChildren().addAll(infobooktitle, infobookID, infobooksection, infomemberuser);
        
        
        rightVBox1 = new VBox(10);
        rightVBox1.setPrefSize(USE_COMPUTED_SIZE, 200);
        rightVBox1.setMinSize(USE_COMPUTED_SIZE, 200);
        rightVBox1.setMaxSize(USE_COMPUTED_SIZE, 200);
        rightVBox1.setAlignment(Pos.CENTER_LEFT);
        rightVBox1.setLayoutX(475);
        rightVBox1.setLayoutY(0);
        
        dateofleaseLabel = new Label("Date or Lease :");
        dateofreturnLabel = new Label("Date of return : ");
        penaltyLabel = new Label("Penalty : ");
        
        rightVBox1.getChildren().addAll(dateofleaseLabel, dateofreturnLabel, penaltyLabel);
        
        rightVBox2 = new VBox(10);
        rightVBox2.setPrefSize(USE_COMPUTED_SIZE, 200);
        rightVBox2.setMinSize(USE_COMPUTED_SIZE, 200);
        rightVBox2.setMaxSize(USE_COMPUTED_SIZE, 200);
        rightVBox2.setAlignment(Pos.CENTER_LEFT);
        rightVBox2.setLayoutX(664);
        rightVBox2.setLayoutY(0);
        
        infodatelease = new Label(this.bookrent.getDateRented());
        infopenalty = new Label();
        infodatereturn = new DatePicker(LocalDate.now());
        returndate = DateTimeFormatter.ISO_LOCAL_DATE;
        
        payment_dat = program.returnBook(bookrent, infodatereturn.getValue().format(returndate));
        penaltyDouble = payment_dat.getPenaltyPrice();
        infopenalty.setText("$"+Double.toString(payment_dat.getPenaltyPrice()));
        
        infodatereturn.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkBox.setSelected(false);
            payment_dat = program.returnBook(bookrent, newValue.format(returndate));
            penaltyDouble = payment_dat.getPenaltyPrice();
            infopenalty.setText("$"+Double.toString(penaltyDouble));
        });
        
        rightVBox2.getChildren().addAll(infodatelease, infodatereturn, infopenalty);
        
        checkBox = new CheckBox();
        checkBox.setLayoutX(900);
        checkBox.setLayoutY(14);
        
        ConstructComponents();
    }
    
    public void ConstructComponents(){
        super.getChildren().addAll(leftvbox1,leftvbox2, rightVBox1, rightVBox2, checkBox);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public SimpleDoubleProperty getDoubleProperty() {
        return doubleProperty;
    }

    public Payment_dat getPayment_dat() {
        return payment_dat;
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
