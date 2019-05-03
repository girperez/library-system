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
public class BookRent_dat{
    
    private int id;
    private String username;
    private String bookID;
    private String dateRented;

    public BookRent_dat(int id, String bookid, String username, String daterented) {
        this.id = id;
        this.bookID = bookid;
        this.username = username;
        this.dateRented = daterented;
    }

    public String getUsername() {
        return username;
    }

    public String getBookID() {
        return bookID;
    }

    public String getDateRented() {
        return dateRented;
    }

    public int getId() {
        return id;
    }
}
