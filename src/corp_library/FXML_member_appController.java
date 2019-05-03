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
import custom_components.LeftPaneInfo;
import database.Database_programCL;
import database.dataFields.Account_dat;
import database.dataFields.BookRent_dat;
import database.dataFields.Book_dat;
import database.dataFields.Member_dat;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Gerryl
 */
public class FXML_member_appController implements Initializable {
    
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
    @FXML
    Button logoutButton;
    // labels
    @FXML
    Label accountlabel;
    @FXML
    VBox memberInfoVbox;
//right pane components//
    //  Books section components
    @FXML
    Button fantasybuton;
    @FXML
    AnchorPane booksSection;
    @FXML
    GridPane booksGridPane;
    @FXML
    TextField searchbooksTF;
    
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
    private Task<ArrayList<Object>> taskList;
    
//others
     
    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            
            booksGridPane.getChildren().clear();
            booksGridPane.getRowConstraints().clear();
            memberInfoVbox.getChildren().clear();
            
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
                        BooksSectionPane booksSectionPane = new BooksSectionPane(new Book_dat(book.getBookID(), book.getBookTitle(), book.getSection(), book.getCopies(), new ByteArrayInputStream(imgs)), program.countRents(book.getBookID()));
                        booksSectionPane.noEdit();
                        booksGridPane.getRowConstraints().add(rows);
                        booksGridPane.addRow(i, new ImageBook(new Image(new ByteArrayInputStream(imgs))));
                        booksGridPane.addRow(i, booksSectionPane);
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
        }catch(Exception e){
            
            alertOK(program.getMessages());
        }
    }
///////////////////////////Normal methods ///////////////////////////////////////////////////////////////////
    private void alertOK(String message){//alert window with OK botton
        alert_message.setMessage(message);
        alert_message.okButton();
        alert_message.getOkButton().setOnMouseClicked((eve) -> {
            alertstage.close();
        });
        alertstage.showAndWait();
    }
    
    private void alertExit(String message){//alert for application exit prompt
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
    
    private void showloading(){
        mainpane.setDisable(true);
        mainpane.setOpacity(0.8);
        loadingScreen.setDisable(false);
        loadingScreen.setVisible(true);
        loadingScreen.startLoading();
    }
    
    private void endloading(){
        mainpane.setDisable(false);
        mainpane.setOpacity(1);
        loadingScreen.setDisable(true);
        loadingScreen.setVisible(false);
        loadingScreen.stopLoading();
    }
    
    private void categorySearch(String category){// this method is for the buttons in category(books section) on the upper bar
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
    
    public void setAccountInfo(Account_dat account){
        Member_dat member = program.getMember(account.getUsername());
        avatar.setFill(null);
        avatar.setFill(new ImagePattern(new Image(member.getImageInputStream())));
        accountlabel.setText(member.getFirstname());
        
        ArrayList<BookRent_dat> rents = program.getBookRents(member.getUsername());
        for(BookRent_dat rent : rents){
            Book_dat book = program.getBook(rent.getBookID());
            LeftPaneInfo leftPaneInfo = new LeftPaneInfo(book, rent.getDateRented());
            memberInfoVbox.getChildren().add(leftPaneInfo);
        }
    }
    
//Mouse Click events//
//////////////  Left pane Mouse clicks      ////////////////////////////////////////////////////////////////////////////
    
    @FXML
    private void logoutClicked(MouseEvent event){
        alertExit("Are you sure you want to Log out?");
    }
    
//////////////  Books section mouseclicks   ////////////////////////////////////////////////////////////////////////////////
    
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
}
