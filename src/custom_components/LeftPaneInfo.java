/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_components;

import database.dataFields.Book_dat;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author Gerryl
 */
public class LeftPaneInfo extends Pane{
    //components
    private Label bookIdLabel;
    private Label titleLabel;
    private Label dateLabel;
    private Label infobookId;
    private Label infotitle;
    private Label infodate;
    //program
    private Book_dat book;
    private String datelease;
    
    public LeftPaneInfo(Book_dat book, String datelease) {
        super();
        super.getStyleClass().add("lefetpane-member-info");
        super.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        super.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        super.setPrefSize(250, 100);
        this.book = book;
        this.datelease = datelease;
        
        //initialize components
        bookIdLabel = new Label("Book ID:");
        titleLabel = new Label("Title:");
        dateLabel = new Label("Date of lease:");
        infobookId= new Label();
        infotitle = new Label();
        infodate = new Label();
        
        bookIdLabel.setLayoutX(14);
        bookIdLabel.setLayoutY(11);
        titleLabel.setLayoutX(14);
        titleLabel.setLayoutY(39);
        dateLabel.setLayoutX(14);
        dateLabel.setLayoutY(66);
        infobookId.setLayoutX(125);
        infobookId.setLayoutY(12);
        infotitle.setLayoutX(125);
        infotitle.setLayoutY(39);
        infodate.setLayoutX(125);
        infodate.setLayoutY(66);
        
        super.getChildren().addAll(bookIdLabel,titleLabel,dateLabel,infobookId,infotitle,infodate);
        
        fetchinfo();
    }
    
    private void fetchinfo(){
        infobookId.setText(book.getBookID());
        infobookId.setText(book.getBookTitle());
        infodate.setText(datelease);
    }
}
