/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corp_library;

import custom_components.Alert_message;
import custom_components.BooksSectionPane;
import custom_components.LoadingScreen;
import custom_components.ImageBook;
import custom_components.ImageMembers;
import custom_components.MembersSectionPane;
import custom_components.ReturnsSectionPane;
import database.Database_programCL;
import database.dataFields.BookRent_dat;
import database.dataFields.Book_dat;
import database.dataFields.Member_dat;
import database.dataFields.Payment_dat;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Gerryl
 */
public class FXML_corp_libraryController implements Initializable {
    
//Root and main panes
    @FXML
    AnchorPane rootpane;
    @FXML
    BorderPane mainpane;
//left pane components//
    //  avatar
    @FXML
    Circle avatar;
    @FXML
    ImageView lodimg;
    //  buttons
    //      leftpane
    @FXML
    Button booksButton;
    @FXML
    Button membersButton;
    @FXML
    Button returnsButton;
    @FXML
    Button logoutButton;
    //      members rightpane
    @FXML
    Button fantasybuton;
    //  labels
    @FXML
    Label accountlabel;
//right pane components//
    //  Books section components
    @FXML
    AnchorPane booksSection;
    @FXML
    GridPane booksGridPane;
    @FXML
    TextField searchbooksTF;
    
    // Members section components
    @FXML
    AnchorPane membersSection;
    @FXML
    GridPane membersGridPane;
    @FXML
    TextField searchmemTF;
    // Return Section components
    @FXML
    AnchorPane returnsSection;
    @FXML
    Pane recietPane;
    @FXML
    ScrollPane returnsscrollPane;
    @FXML
    GridPane returnsGridpane;
    @FXML
    TextField searchreturnsTF;
    @FXML
    Label totalsinfoLabel;
    @FXML
    Button paymentBtn;
    @FXML
    Button backBtn;
    
    //for recieptList format
    @FXML
    Label paydateL;
    @FXML
    Label totalsL;
    @FXML
    VBox leftvboxR;
    @FXML
    VBox rightvbocR;
    @FXML
    GridPane recieptGP;
    @FXML
    Button confrmpayBtn;
//Custom components//////
    private Stage alertstage;
    private Alert_message alert_message;
    private LoadingScreen loadingScreen;
//Animation Effect Variables//
    // for right pane sections effects
    private FadeTransition fade;
    private ScaleTransition scale;
//Database variable//
    private Database_programCL program;
//multi Thread Variables////
    private Service<ArrayList<Object>> listServicebooks;
    private Service<ArrayList<Object>> listserviceMembers;
    private Service<ArrayList<Object>> listserviceReturns;
    private Task<ArrayList<Object>> taskList;
    
//others
    private Double totals = 0.00;
    private SimpleDoubleProperty doubleProperty;
    private ArrayList<Payment_dat> recieptList;
     
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            doubleProperty = new SimpleDoubleProperty(totals);
            totalsinfoLabel.textProperty().bind(doubleProperty.asString());
            recieptList = new ArrayList<>();
            
            booksGridPane.getChildren().clear();
            booksGridPane.getRowConstraints().clear();
            membersGridPane.getChildren().clear();
            membersGridPane.getRowConstraints().clear();
            returnsGridpane.getChildren().clear();
            returnsGridpane.getRowConstraints().clear();
            
            //initialize the popup window alert
            alert_message = new Alert_message();
            alertstage = new Stage(StageStyle.DECORATED);
            alertstage.initOwner(Corp_library.getRootStage());
            alertstage.initModality(Modality.APPLICATION_MODAL);
            alertstage.setResizable(false);
            alertstage.setScene(new Scene(alert_message));
            
            //initialize loading screen
            loadingScreen = new LoadingScreen();
            loadingScreen.setLayoutX(800);
            loadingScreen.setLayoutY(450);
            rootpane.getChildren().add(loadingScreen);
            //initialize the database program
            program = new Database_programCL();
            
            //initialize multi threads
            //  members multi thread
            listServicebooks = new Service<ArrayList<Object>>() {
                @Override
                protected Task<ArrayList<Object>> createTask() {
                    return taskList;
                }
            };
            
            listServicebooks.setOnSucceeded((we) -> {
                try {
                    
                   ArrayList<Object> books = listServicebooks.getValue();
                   
                   for (int i = 0; i < books.size(); i++) {
                        Book_dat book = (Book_dat) books.get(i);
                        InputStream img = (ByteArrayInputStream) book.getImageInputStream();
                        byte[] imgs = new byte[img.available()];
                        img.read(imgs);
                        
                        RowConstraints rows = new RowConstraints(200, 200, 200, Priority.NEVER, VPos.CENTER, true);
                        booksGridPane.getRowConstraints().add(rows);
                        booksGridPane.addRow(i, new ImageBook(new Image(new ByteArrayInputStream(imgs))));
                        booksGridPane.addRow(i, new BooksSectionPane(new Book_dat(book.getBookID(), book.getBookTitle(), book.getSection(), book.getCopies(), new ByteArrayInputStream(imgs)), program.countRents(book.getBookID())));
                    }
                } catch (Exception e) {
                    alertOK("Threading Error Occured");
                }finally{
                    endloading();
                    listServicebooks.reset();
                }
            });
            
            listServicebooks.setOnFailed((event) -> {
                alertOK("Threading Failure Occured");
                endloading();
                listServicebooks.reset();
            });
            
            // members multi thread
            listserviceMembers = new Service<ArrayList<Object>>() {
                @Override
                protected Task<ArrayList<Object>> createTask() {
                    return taskList;
                }
            };
            
            listserviceMembers.setOnSucceeded((we) -> {
                try {
                   Database_programCL programrent = new Database_programCL();
                   ArrayList<Object> members = listserviceMembers.getValue();
                   for (int i = 0; i < members.size(); i++) {
                        Member_dat memb = (Member_dat) members.get(i);
                        InputStream img = (ByteArrayInputStream) memb.getImageInputStream();
                        byte[] imgs = new byte[img.available()];
                        img.read(imgs);
                        
                        ArrayList<BookRent_dat> bookrents = programrent.getBookRents(memb.getUsername());
                        
                        RowConstraints rows = new RowConstraints(200, 200, 200, Priority.NEVER, VPos.CENTER, true);
                        membersGridPane.getRowConstraints().add(rows);
                        membersGridPane.addRow(i, new ImageMembers(memb, new Image(new ByteArrayInputStream(imgs))));
                        membersGridPane.addRow(i, new MembersSectionPane(new Member_dat(memb.getFirstname(), memb.getLastname(), memb.getEmail(), memb.getContact(), memb.getUsername(), new ByteArrayInputStream(imgs)), bookrents));
                    }
                } catch (Exception e) {
                    alertOK("Threading Error Occured");
                }finally{
                    endloading();
                    listserviceMembers.reset();
                }
            });
            
            listserviceMembers.setOnFailed((event) -> {
                alertOK("Threading Failure Occured");
                endloading();
                listserviceMembers.reset();
            });
            
            //  returns multithread
            listserviceReturns = new Service<ArrayList<Object>>() {
                @Override
                protected Task<ArrayList<Object>> createTask() {
                    return taskList;
                }
            };
            
            listserviceReturns.setOnSucceeded((we) -> {
                try {
                   ArrayList<Object> returns = listserviceReturns.getValue();
                   for (int i = 0; i < returns.size(); i++) {
                        BookRent_dat bookRent_dat = (BookRent_dat) returns.get(i);
                        ReturnsSectionPane returnsSectionPane = new ReturnsSectionPane(bookRent_dat);
                        returnsSectionPane.getCheckBox().selectedProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                totals += returnsSectionPane.getPayment_dat().getPenaltyPrice();
                                doubleProperty.set(totals);
                                recieptList.add(returnsSectionPane.getPayment_dat());
                            }else{
                                totals -= returnsSectionPane.getPayment_dat().getPenaltyPrice();
                                doubleProperty.set(totals);
                                recieptList.remove(returnsSectionPane.getPayment_dat());
                            }
                        });
                        File imagefile = new File("src/images/icon_image.png");
                        InputStream imageinputStream = new FileInputStream(imagefile);
                        byte[] img = new byte[imageinputStream.available()];
                        imageinputStream.read(img);
                        RowConstraints rows = new RowConstraints(200, 200, 200, Priority.NEVER, VPos.CENTER, true);
                        returnsGridpane.getRowConstraints().add(rows);
                        returnsGridpane.addRow(i, new ImageBook(new Image(new ByteArrayInputStream(img))));
                        returnsGridpane.addRow(i, returnsSectionPane);
                    }
                } catch (Exception e) {
                    alertOK("Threading Error " + e.toString());
                }finally{
                    endloading();
                    listserviceReturns.reset();
                    searchreturnsTF.setText("");
                }
            });
            
            listserviceReturns.setOnFailed((event) -> {
                alertOK("Threading Failure Occured");
                endloading();
                listserviceReturns.reset();
            });
            
        }catch(Exception e){
            
            alertOK(program.getMessages());
        }
    }
///////////////////////////Normal methods ///////////////////////////////////////////////////////////////////
    public void alertOK(String message){//alert window with OK botton
        alert_message.setMessage(message);
        alert_message.okButton();
        alert_message.getOkButton().setOnMouseClicked((eve) -> {
            alertstage.close();
        });
        alertstage.showAndWait();
    }
    
    public void alertExit(String message){//alert for application exit prompt
        alert_message.setMessage(message);
        alert_message.okcancelButton();
        alert_message.getOkButton().setOnMouseClicked((eve) -> {
            try {
                FXMLLoader loginloader = new FXMLLoader(getClass().getResource("/files_FXML/FXML_login.fxml"));
                Parent root1 = (Parent) loginloader.load();
                Corp_library.getRootStage().close();
                Stage stage = new Stage();
                Corp_library.setRootStage(stage);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setTitle("Corp Library");
                stage.setScene(new Scene(root1,Color.TRANSPARENT));
                stage.show();
                alertstage.close();
            } catch (Exception e) {
                System.out.println(e);
                alertOK("Exit Error Occured");
            }
        });
        
        alert_message.getCanceButton().setOnMouseClicked((ev) -> {
            alertstage.close();
        });
        alertstage.showAndWait();
    }
    
    public void showloading(){
        mainpane.setDisable(true);
        mainpane.setOpacity(0.8);
        loadingScreen.setDisable(false);
        loadingScreen.setVisible(true);
        loadingScreen.startLoading();
    }
    
    public void endloading(){
        mainpane.setDisable(false);
        mainpane.setOpacity(1);
        loadingScreen.setDisable(true);
        loadingScreen.setVisible(false);
        loadingScreen.stopLoading();
    }
    
    public void categorySearch(String category){// this method is for the buttons in category(books section) on the upper bar
        try {
            booksGridPane.getChildren().clear();
            booksGridPane.getRowConstraints().clear();
            
            taskList = new Task<ArrayList<Object>>() {
                @Override
                protected ArrayList<Object> call() throws Exception {
                    showloading();
                    ArrayList<Object> list = new ArrayList<>();
                    for(Book_dat book_dat : program.searchBook(category)){
                        list.add(book_dat);
                    }
                    
                    return list;
                }
            };
            listServicebooks.start();
        } catch (Exception e) {
            alertOK("Error: "+ e.toString());
        }
    }
    
    public void createReciept(ArrayList<Payment_dat> payments){
        LocalDate datenow= LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        paydateL.setText(datenow.format(formatter));
        for (Payment_dat payment : payments) {
            Label payidLabel = new Label(payment.getPaymentID());
            payidLabel.setStyle("-fx-font-size: 18");
            
            Label paypriceLabel = new Label(payment.getPenaltyPrice().toString());
            paypriceLabel.setStyle("-fx-font-size: 18");
            
            leftvboxR.getChildren().add(payidLabel);
            rightvbocR.getChildren().add(paypriceLabel);
        }
        
        totalsL.setText(totals.toString());
    }
//Mouse Click events//
//////////////  Left pane Mouse clicks      ////////////////////////////////////////////////////////////////////////////
    @FXML
    private void booksButtonClicked(MouseEvent event){
        fade = new FadeTransition(Duration.millis(300), booksSection);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.playFromStart();
        booksSection.setVisible(true);
        membersSection.setOpacity(0);
        membersSection.setVisible(false);
        returnsSection.setOpacity(0);
        returnsSection.setVisible(false);
    }
    @FXML
    private void membersButtonClicked(MouseEvent event){
        fade = new FadeTransition(Duration.millis(300), membersSection);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.playFromStart();
        membersSection.setVisible(true);
        booksSection.setOpacity(0);
        booksSection.setVisible(false);
        returnsSection.setOpacity(0);
        returnsSection.setVisible(false);
    }
    @FXML
    private void returnsButtonClicked(MouseEvent event){
        fade = new FadeTransition(Duration.millis(300), returnsSection);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.playFromStart();
        returnsSection.setVisible(true);
        membersSection.setOpacity(0);
        membersSection.setVisible(false);
        booksSection.setOpacity(0);
        booksSection.setVisible(false);
    }
    
    @FXML
    private void logoutClicked(MouseEvent event){
        alertExit("Are you sure you want to Log out?");
    }
    
//////////////  Books section mouseclicks   ////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void addBookClicked(MouseEvent event){
        try {
            Parent addbookWindow = FXMLLoader.load(getClass().getResource("/files_FXML/FXML_add_book.fxml"));
            Stage addbookStage = new Stage();
            addbookStage.initModality(Modality.APPLICATION_MODAL);
            addbookStage.initStyle(StageStyle.DECORATED);
            addbookStage.setTitle("Add Book");
            addbookStage.setScene(new Scene(addbookWindow));
            addbookStage.setResizable(false);
            addbookStage.showAndWait();
        } catch (Exception ex) {
            System.out.println("Failed to load Add book window: "+ ex);
        }
    }
    
    @FXML
    private void fanbutonclicked(MouseEvent event){
        categorySearch("Fantasy");
    }
    
    @FXML
    private void trhillClicked(MouseEvent event){
        categorySearch("Thriller");
    }
    
    @FXML
    private void mysteryClicked(MouseEvent event){
        categorySearch("Mystery");
    }
    
    @FXML
    private void horrorClicked(MouseEvent event){
        categorySearch("Horror");
    }
    
    @FXML
    private void romnceClicked(MouseEvent event){
        categorySearch("Romance");
    }
    
    @FXML
    private void searchbooksButonClicked(MouseEvent event){
        try {
            
            booksGridPane.getChildren().clear();
            booksGridPane.getRowConstraints().clear();
            
            taskList = new Task<ArrayList<Object>>() {
                @Override
                protected ArrayList<Object> call() throws Exception {
                    showloading();
                    String search = searchbooksTF.getText();
                    ArrayList<Object> list = new ArrayList<>();
                    if (!search.equals("")) {
                        for(Book_dat book_dat : program.searchBook(search)){
                            list.add(book_dat);
                        }
                    }
                    
                    return list;
                }
            };
            listServicebooks.start();
        } catch (Exception e) {
            alertOK("Error: "+ e.toString());
        }
    }
    
////////////  members mouse clicks //////////////////////////////////////////////////////////////////////////
    @FXML
    private void searchmemClicked(MouseEvent event){
        try {
            membersGridPane.getChildren().clear();
            membersGridPane.getRowConstraints().clear();
            
            
            taskList = new Task<ArrayList<Object>>() {
                @Override
                protected ArrayList<Object> call() throws Exception {
                    showloading();
                    String search = searchmemTF.getText();
                    ArrayList<Object> list = new ArrayList<>();
                    if (!search.equals("")) {
                        for(Member_dat member_dat : program.searchmembers(search)){
                            list.add(member_dat);
                        }
                    }
                    
                    return list;
                }
            };
            listserviceMembers.start();
            
        } catch (Exception e) {
            alertOK("Error: "+ e.toString());
        }   
    }
    
    @FXML
    private void admembtnClicked(MouseEvent event){
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
    
    
///////// returns mouse clicks ////////////////////////////////////////////////////////////////////
    @FXML
    private void searchreturnsClicked(MouseEvent event){
        try {
            
            totals = 0.00;
            doubleProperty.setValue(totals);
            recieptList.clear();
            returnsGridpane.getChildren().clear();
            returnsGridpane.getRowConstraints().clear();
            
            taskList = new Task<ArrayList<Object>>() {
                @Override
                protected ArrayList<Object> call() throws Exception {
                    showloading();
                    String search = searchreturnsTF.getText();
                    ArrayList<Object> list = new ArrayList<>();
                    if (!search.equals("")) {
                        for(BookRent_dat bookrent : program.getBookRents(search)){
                            list.add(bookrent);
                        }
                    }
                    
                    return list;
                }
            };
            listserviceReturns.start();
        } catch (Exception e) {
            alertOK("Error: "+ e.toString());
        }
    }
    
    @FXML
    private void confrmselectClicked(MouseEvent event){
        if (totals > 0) {
            alert_message.setMessage("The total Penalty is $"+totals+". Continue?");
            alert_message.okcancelButton();
            alert_message.getOkButton().setText("Yes");
            alert_message.getOkButton().setOnMouseClicked((eve) -> {
                alertstage.close();
                paymentBtn.setDisable(false);
                paymentBtn.setVisible(true);
            });
            
            alert_message.getCanceButton().setOnMouseClicked((ev) -> {
                alertstage.close();
            });
            
            alertstage.showAndWait();
        }else{
            alert_message.setMessage("No penalty available. Continue?");
            alert_message.okcancelButton();
            alert_message.getOkButton().setText("Yes");
            alert_message.getOkButton().setOnMouseClicked((eve) -> {
                alertstage.close();
                paymentBtn.setDisable(false);
                paymentBtn.setVisible(true);
                
                scale = new ScaleTransition(Duration.millis(300), recietPane);
                scale.setFromX(0.8);
                scale.setFromY(0.8);
                scale.setToX(1);
                scale.setToY(1);

                createReciept(recieptList);
                recietPane.setVisible(true);
                backBtn.setVisible(true);
                returnsscrollPane.setDisable(true);
                scale.playFromStart();
            });
            
            alert_message.getCanceButton().setOnMouseClicked((ev) -> {
                alertstage.close();
            });
            
            alertstage.showAndWait();
        }
    }
    
    @FXML
    private void pymntbtnClicked(MouseEvent event){
        
        scale = new ScaleTransition(Duration.millis(300), recietPane);
        
        scale.setFromX(0.8);
        scale.setFromY(0.8);
        scale.setToX(1);
        scale.setToY(1);
        
        createReciept(recieptList);
        recietPane.setVisible(true);
        backBtn.setVisible(true);
        returnsscrollPane.setDisable(true);
        scale.playFromStart();
    }
    
    @FXML
    private void backBtnClicked(MouseEvent event){
        scale = new ScaleTransition(Duration.millis(300), recietPane);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(0.8);
        scale.setToY(0.8);
        
        scale.playFromStart();
        scale.setOnFinished((even) -> {
            recietPane.setVisible(false);
            backBtn.setVisible(false);
            returnsscrollPane.setDisable(false);
            paymentBtn.setDisable(true);
            paymentBtn.setVisible(false);
            rightvbocR.getChildren().clear();
            leftvboxR.getChildren().clear();
            paydateL.setText("--");
            totalsL.setText("--");
        }); 
    }
    
////////////////////////for confirming the recipt /////////////////////////////////////////////////////////////
    @FXML
    private void cnfrmpayclicked(MouseEvent event){
        Service<Void> confirmservice = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        showloading();
                        if (totals > 0) {
                            program.recordPayments(recieptList);
                            
                            for(Payment_dat payment_dat : recieptList){
                                program.deletebookRent(payment_dat);
                            }
                            PrinterJob printerJob = PrinterJob.createPrinterJob();
                            if(printerJob != null){
                                printerJob.showPrintDialog(Corp_library.getRootStage());
                                printerJob.printPage(recieptGP);
                                printerJob.endJob();
                            }

                        } else {
                            program.recordPayments(recieptList);
                            
                            for(Payment_dat payment_dat : recieptList){
                                program.deletebookRent(payment_dat);
                            }

                        }
                        return null;
                    }
                };
            }
        };
        
        confirmservice.setOnSucceeded((eve) -> {
            endloading();
            alertOK("Book return Confirmed");
            confirmservice.reset();

            scale = new ScaleTransition(Duration.millis(300), recietPane);
            scale.setFromX(1);
            scale.setFromY(1);
            scale.setToX(0.8);
            scale.setToY(0.8);

            scale.playFromStart();
            scale.setOnFinished((even) -> {
                recietPane.setVisible(false);
                backBtn.setVisible(false);
                returnsscrollPane.setDisable(false);
                paymentBtn.setDisable(true);
                paymentBtn.setVisible(false);
                rightvbocR.getChildren().clear();
                leftvboxR.getChildren().clear();
                paydateL.setText("--");
                totalsL.setText("--");
                returnsGridpane.getChildren().clear();
            }); 
        });

        confirmservice.setOnFailed((eve) -> {
            endloading();
            alertOK("Threading Error Occured");
            confirmservice.reset();
        });
        
        confirmservice.start();
        
    }
}
