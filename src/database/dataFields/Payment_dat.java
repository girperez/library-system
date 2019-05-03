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
public class Payment_dat{

    private String paymentID;
    private String username;
    private String bookID;
    private int rentID;
    private Double penaltyPrice;
    private String paymentDate;
    
    public Payment_dat(String paymentID, String username, String bookID, int bookindex, Double penaltyPrice) {
        this.paymentID = paymentID;
        this.username = username;
        this.bookID = bookID;
        this.penaltyPrice = penaltyPrice;
        this.rentID = bookindex;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getUsername() {
        return username;
    }

    public String getBookID() {
        return bookID;
    }

    public int getRentID() {
        return rentID;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public Double getPenaltyPrice() {
        return penaltyPrice;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
