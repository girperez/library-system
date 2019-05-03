/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_components;

import corp_library.Corp_library;
import corp_library.FXML_add_bookController;
import database.Database_programCL;
import database.dataFields.Book_dat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * 
 * @author Gerryl
 */
public class BooksSectionPane extends Pane{//this pane is for the table search
//that accepts parameters of data collected from database
    
//labels////////////////
    //  static labels
    private Label titleLabel;
    private Label IdLabel;
    private Label bookSectionlLabel;
    private Label bookAvailableLabel;
    private Label bookcopiesLabel;
    
    //  labels information got from database
    private Label infoTitleLabel;
    private Label infoIdLabel;
    private Label infoSectionLabel;
    private Label infoAvailableLabel;
    private Label infoCopiesLabel;
    

    private Label alertRented;
    
//textfields///////////////   
    private TextField assignMember;
    private TextField editcopy;
//datepicker//////////////
    private DatePicker datePicker;
    private DateTimeFormatter formatter;
    
//buttons/////////////////
    private Button assignButton;
    private Button addcopiesreveal;
    private Button addcopiesButton;
    private Button assignReveal;

//source variable//////////////
    private Book_dat book;
    private int count;
    private Database_programCL program;
    private Alert_message alert_message;
    private Stage alertstage;
//this loader is some kind calling the root controler to do external actions
    private FXMLLoader editbookloader;
    private Parent addbookWindow;
    private Stage addbookStage;
//animations
    private FadeTransition fade1;
    private FadeTransition fade2;
    private TranslateTransition translate1;
    private TranslateTransition translate2;
    
//multithread///////////////////
    private Service<Object> service;
    private Task<Object> task;
    
    public BooksSectionPane(Book_dat book, int count) throws Exception{
        super();
        super.getStyleClass().add("divider-down");
        super.setPrefSize(200, 200);
        
        this.book = book;
        this.count = book.getCopies() - count;
        
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
                infoCopiesLabel.setText(Integer.toString(book.getCopies()));
                infoCopiesLabel.setDisable(false);
                infoCopiesLabel.setVisible(true);
                
                editcopy.setVisible(false);
                editcopy.setDisable(true);

                addcopiesreveal.setVisible(true);
                addcopiesreveal.setDisable(false);

                addcopiesButton.setVisible(false);
                addcopiesButton.setDisable(true);
                
                assignButton.setVisible(false);
                assignButton.setDisable(true);
                
                assignMember.setDisable(true);
                assignMember.setVisible(false);
                assignMember.setText("");
                assignMember.setOpacity(0);
                
                datePicker.setDisable(true);
                datePicker.setVisible(false);
                datePicker.setOpacity(0);
                
                assignReveal.setVisible(true);
                assignReveal.setDisable(false);
                
                System.out.println(this.count);
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
        
        //initialize alert stage
        alert_message = new Alert_message();
        alertstage = new Stage(StageStyle.DECORATED);
        alertstage.initOwner(Corp_library.getRootStage());
        alertstage.initModality(Modality.APPLICATION_MODAL);
        alertstage.setResizable(false);
        alertstage.setScene(new Scene(alert_message));
        
        //initialize components
        titleLabel = new Label("Book Title:");
        IdLabel = new Label("Book ID:");
        bookSectionlLabel = new Label("Section:");
        bookAvailableLabel = new Label("Available copies:");
        bookcopiesLabel = new Label("Copies:");
        infoTitleLabel = new Label(book.getBookTitle());
        infoIdLabel = new Label(book.getBookID());
        infoSectionLabel = new Label(book.getSection());
        infoAvailableLabel = new Label(Integer.toString(this.count));
        infoCopiesLabel = new Label(Integer.toString(book.getCopies()));
        alertRented = new Label("Book Leased!");
        assignMember = new TextField();
        editcopy = new TextField();
        assignButton = new Button("Assign");
        assignReveal = new Button("Lease book");
        addcopiesreveal = new Button();
        addcopiesButton = new Button("Edit Copies");
        datePicker = new DatePicker(LocalDate.now());
        formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        
        //layouts
        //  labels
        titleLabel.setLayoutX(36);
        titleLabel.setLayoutY(24);
        IdLabel.setLayoutX(36);
        IdLabel.setLayoutY(56);
        bookSectionlLabel.setLayoutX(36);
        bookSectionlLabel.setLayoutY(90);
        bookAvailableLabel.setLayoutX(36);
        bookAvailableLabel.setLayoutY(119);
        bookcopiesLabel.setLayoutX(36);
        bookcopiesLabel.setLayoutY(158);
        infoTitleLabel.setLayoutX(214);
        infoTitleLabel.setLayoutY(24);
        infoIdLabel.setLayoutX(214);
        infoIdLabel.setLayoutY(56);
        infoSectionLabel.setLayoutX(214);
        infoSectionLabel.setLayoutY(90);
        infoAvailableLabel.setLayoutX(214);
        infoAvailableLabel.setLayoutY(119);
        infoAvailableLabel.setStyle("-fx-text-fill: red;");
        infoCopiesLabel.setLayoutX(213);
        infoCopiesLabel.setLayoutY(158);
        alertRented.setLayoutX(551);
        alertRented.setLayoutY(44);
        alertRented.setVisible(false);
        alertRented.setOpacity(0);
        datePicker.setLayoutX(516);
        datePicker.setLayoutY(83);
        datePicker.setPrefSize(248, 38);
        datePicker.setVisible(false);
        datePicker.setDisable(true);
        datePicker.setOpacity(0);
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                datePicker.setValue(oldValue);
            }
        });
        
        //  textfield
        assignMember.setLayoutX(516);
        assignMember.setLayoutY(131);
        assignMember.setVisible(false);
        assignMember.setDisable(true);
        assignMember.setPromptText("Assign Member ID");
        editcopy.setMaxSize(50, USE_COMPUTED_SIZE);
        editcopy.setLayoutX(212);
        editcopy.setLayoutY(150);
        editcopy.setVisible(false);
        editcopy.setDisable(true);
        
        //  assign button
        assignButton.setLayoutX(774);
        assignButton.setLayoutY(125);
        assignButton.setPrefHeight(50);
        assignButton.setStyle("-fx-font-size: 24");
        assignButton.getStyleClass().add("books-button-bar");
        assignButton.setVisible(false);
        assignButton.setDisable(true);
        assignButton.setOnMouseClicked((event) -> {
            assignbook();
        });
        
        // assign reveal button
        assignReveal.setLayoutX(774);
        assignReveal.setLayoutY(125);
        assignReveal.setPrefHeight(50);
        assignReveal.setStyle("-fx-font-size: 24");
        assignReveal.getStyleClass().add("books-button-bar");
        assignReveal.setOnMouseClicked((event) -> {
            assignButton.setVisible(true);
            assignButton.setDisable(false);
            
            assignMember.setVisible(true);
            assignMember.setDisable(false);
            
            assignReveal.setDisable(true);
            assignReveal.setVisible(false);
            
            datePicker.setDisable(false);
            datePicker.setVisible(true);
            
            animateAssign();
        });
        
        //  add copies button(the cross button)
        addcopiesreveal.setLayoutX(281);
        addcopiesreveal.setLayoutY(153);
        addcopiesreveal.setMaxSize(32, 32);
        addcopiesreveal.setMinSize(32, 32);
        addcopiesreveal.getStyleClass().add("add_button");
        addcopiesreveal.setOnMouseClicked((event) -> {
            infoCopiesLabel.setDisable(true);
            infoCopiesLabel.setVisible(false);
            
            editcopy.setVisible(true);
            editcopy.setDisable(false);
            editcopy.requestFocus();
            
            addcopiesreveal.setVisible(false);
            addcopiesreveal.setDisable(true);
            
            addcopiesButton.setVisible(true);
            addcopiesButton.setDisable(false);
        });
        addcopiesButton.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        addcopiesButton.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        addcopiesButton.setPrefSize(120, 32);
        addcopiesButton.setLayoutX(281);
        addcopiesButton.setLayoutY(153);
        addcopiesButton.setAlignment(Pos.CENTER);
        addcopiesButton.getStyleClass().clear();
        addcopiesButton.getStyleClass().add("books-button-bar");
        addcopiesButton.setText("Add copies");
        addcopiesButton.setVisible(false);
        addcopiesButton.setDisable(true);
        addcopiesButton.setOnMouseClicked((event) -> {
            addcopy();
        });
        
        super.setOnMouseClicked((event) -> {
            infoCopiesLabel.setDisable(false);
            infoCopiesLabel.setVisible(true);
            
            editcopy.setVisible(false);
            editcopy.setDisable(true);
            
            addcopiesreveal.setVisible(true);
            addcopiesreveal.setDisable(false);
            
            addcopiesButton.setVisible(false);
            addcopiesButton.setDisable(true);
            
            
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    try {
                        editbookloader = new FXMLLoader(getClass().getResource("/files_FXML/FXML_add_book.fxml"));
                        
                        addbookWindow = (Parent) editbookloader.load();
                        FXML_add_bookController editcontroller = editbookloader.<FXML_add_bookController>getController();
                        editcontroller.editbook(book);
                        addbookStage = new Stage();
                        addbookStage.initModality(Modality.APPLICATION_MODAL);
                        addbookStage.initStyle(StageStyle.DECORATED);
                        addbookStage.setTitle("Edit Book");
                        addbookStage.setScene(new Scene(addbookWindow));
                        addbookStage.setResizable(false);
                        addbookStage.showAndWait();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }finally{
                        addbookStage.close();
                    }
                }
            }
        });
        
        ConstructComponents();
    }
    
    public void ConstructComponents(){
        super.getChildren().addAll(titleLabel,IdLabel,bookSectionlLabel,bookAvailableLabel,
                infoTitleLabel,infoIdLabel,infoSectionLabel,infoAvailableLabel,alertRented,assignMember,assignButton,
                editcopy,infoCopiesLabel,bookcopiesLabel,addcopiesreveal,addcopiesButton,assignReveal,datePicker);
    }
    //  animation method
    public void animateAssign(){
        fade1 = new FadeTransition(Duration.millis(200), assignMember);
        fade1.setFromValue(0);
        fade1.setToValue(1);
        
        fade2 = new FadeTransition(Duration.millis(200), datePicker);
        fade2.setFromValue(0);
        fade2.setToValue(1);
        fade2.setDelay(Duration.millis(200));
        
        translate1 = new TranslateTransition(Duration.millis(300), assignMember);
        translate1.setFromY(20);
        translate1.setToY(0);
        
        translate2 = new TranslateTransition(Duration.millis(300), datePicker);
        translate2.setFromY(30);
        translate2.setToY(0);
        translate2.setDelay(Duration.millis(200));
        
        fade1.playFromStart();
        fade2.playFromStart();
        translate1.playFromStart();
        translate2.playFromStart();
    }
    
    public void assignbook(){
        task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                if (!assignMember.getText().equals("")) {
                    program = new Database_programCL();
                    
                    String dateString =(String) datePicker.getValue().format(formatter);
                    String message = program.rentBook(assignMember.getText(), book, dateString);

                    return message;
                }else{
                    return "Member is not yet assigned";
                }
            }
        };
        
        service.start();
    }
    
    
    public void addcopy(){
        task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                String message = "";
                try {
                    program = new Database_programCL();
                    int add = Integer.parseInt(editcopy.getText());
                    int current = book.getCopies();
                    int total = current + add;
                    book.setCopies(total);
                    message = program.editBook(book);
                }catch (NumberFormatException en) {
                    return "the text field must contain numbers";
                }catch (Exception e) {
                    return "Error " + e.toString();
                }
                
                return message;
            }
        };
        
        service.start();
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
    
    public void noEdit(){
        assignReveal.setVisible(false);
        assignReveal.setDisable(true);
        addcopiesreveal.setVisible(false);
        addcopiesreveal.setDisable(true);
        addcopiesButton.setVisible(false);
        addcopiesButton.setDisable(true);
        assignButton.setVisible(false);
        assignButton.setDisable(true);
        super.setOnMouseClicked(null);
    }
}
