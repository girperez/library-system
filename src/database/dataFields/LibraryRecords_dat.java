/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.dataFields;

/**
 *
 * @author Gerryl
 */
public class LibraryRecords_dat{
    
    private String bookID;
    private String username;
    private String dateRented;
    private String dateReturned;
    
    public LibraryRecords_dat(String bookID, String dateRented, String dateReturned, String username) {
        this.bookID = bookID;
        this.dateRented = dateRented;
        this.dateReturned = dateReturned;
        this.username = username;
    }

    public String getDateReturned() {
        return dateReturned;
    }

    public String getBookID() {
        return bookID;
    }

    public String getUsername() {
        return username;
    }

    public String getDateRented() {
        return dateRented;
    }
}
