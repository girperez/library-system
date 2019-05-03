/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.dataFields;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author Gerryl
 */
public class Book_dat{
    
    private String bookID;
    private String bookTitle;
    private String section;
    private int copies;
    private InputStream imageInputStream;

    public Book_dat(){
    }
    
    
    //important attributes that should be in database
    public Book_dat(String bookID, String section, File image, int copies) throws Exception{
        this.bookID = bookID;
        this.section = section;
        this.imageInputStream = new FileInputStream(image);
        this.copies = copies;
    }
    //complete attributes
    public Book_dat(String bookID, String bookTitle, String section, File image, int copies) throws Exception{
        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.section = section;
        this.imageInputStream = new FileInputStream(image);
        this.copies = copies;
    }
    //for data retrieval of members information
    public Book_dat(String bookID, String bookTitle, String section) {
        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.section = section;
    }
    //for complete data retrieval
    public Book_dat(String bookID, String bookTitle, String section, int copies, InputStream imageInputStream) {
        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.section = section;
        this.copies = copies;
        this.imageInputStream = imageInputStream;
    }

    public String getBookID() {
        return bookID;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
    
    public String getSection() {
        return section;
    }

    public InputStream getImageInputStream() {
        return imageInputStream;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }
}
