/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import database.dataFields.Payment_dat;
import database.dataFields.Member_dat;
import database.dataFields.Book_dat;
import database.dataFields.Account_dat;
import database.dataFields.BookRent_dat;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author Gerryl
 */
public class Database_programCL extends DatabaseConnect{

    private String messages;
    
    public Database_programCL() {
        super();
        
        messages = "Unknown Error Occured";
    }
    
//member retrieval operations
    // register member
    public String registerNewMember(Member_dat member, Account_dat account){//working
        
        String memberquery = "INSERT INTO members(FirstName, LastName, MobileNo, Email, "
                + "Username, UserImage) VALUES (?,?,?,?,?,?)";
        String accountquery = "INSERT INTO accounts(Type, Username, Password) VALUES(?,?,?)";
        
        try {
            PreparedStatement stmtmember = super.getConnection().prepareStatement(memberquery);
            stmtmember.setString(1, member.getFirstname());
            stmtmember.setString(2, member.getLastname());
            stmtmember.setString(3, member.getContact());
            stmtmember.setString(4, member.getEmail());
            stmtmember.setString(5, member.getUsername());
            stmtmember.setBinaryStream(6, (InputStream)member.getImageInputStream(), member.getImageInputStream().available());
            
            PreparedStatement stmtaccount = super.getConnection().prepareStatement(accountquery);
            stmtaccount.setString(1, account.getType());
            stmtaccount.setString(2, account.getUsername());
            stmtaccount.setString(3, account.getPassword());
            
            if (validateAccount(account.getUsername()) == 0) {
                stmtmember.executeUpdate();
                stmtmember.close();
                stmtaccount.executeUpdate();
                stmtaccount.close();
                messages = "Registration Sucessful!";
                return messages;
            }else{
                stmtmember.clearParameters();
                stmtaccount.clearParameters();
                return "The Username is already been used please try another name";
            }
            
        } catch (Exception ex) {
            System.out.println("failed to register Member: "+ ex);
            messages = "failed to register Member";
        } finally{
            super.closeConnection();
        }
        
        return messages;
    }
    //  member edit
    public String editMember(Member_dat member){//working
        
        String memberquery = "UPDATE members SET FirstName  = ?, LastName = ?, MobileNo = ?, Email = ? WHERE Username = ?";
        try {
            PreparedStatement stmtmembers = super.getConnection().prepareStatement(memberquery);
            stmtmembers.setString(1, member.getFirstname());
            stmtmembers.setString(2, member.getLastname());
            stmtmembers.setString(3, member.getContact());
            stmtmembers.setString(4, member.getEmail());
            stmtmembers.setString(5, member.getUsername());
            
            int confirmupdate = stmtmembers.executeUpdate();
            
            stmtmembers.close();
            return (confirmupdate > 0) ? "Member edit sucessful!" : "Seems like the member Username is not in database please try again.";
            
        } catch (Exception e) {
            System.out.println("Failed to edit Member: "+ e);
            messages = "Failed to edit Member";
        }finally{
            super.closeConnection();
        }
        
        return messages;
    }
    //  edit member picture
    public String editmemberPic(Member_dat member){
        String memberquery = "UPDATE members SET UserImage  = ? WHERE Username = ?";
        try {
            PreparedStatement stmtmembers = super.getConnection().prepareStatement(memberquery);
            stmtmembers.setBinaryStream(1, member.getImageInputStream(), member.getImageInputStream().available());
            stmtmembers.setString(2, member.getUsername());
            
            int confirmupdate = stmtmembers.executeUpdate();
            
            stmtmembers.close();
            return (confirmupdate > 0) ? "Member edit sucessful!" : "Seems like the member Username is not in database please try again.";
            
        } catch (Exception e) {
            System.out.println("Failed to edit Member: "+ e);
            messages = "Failed to edit Member";
        }finally{
            super.closeConnection();
        }
        
        return messages;
    }
    //  get a specific member using its username
    public Member_dat getMember(String username){//working
        Member_dat member = null;
        String memberquery = "SELECT FirstName, LastName, MobileNo, Email, Username, UserImage FROM members WHERE Username = ?";
        
        try {
            PreparedStatement stmtmember = super.getConnection().prepareStatement(memberquery);
            stmtmember.setString(1, username);
            
            ResultSet rsmember = stmtmember.executeQuery();
                
            while(rsmember.next()){
                member = new Member_dat(rsmember.getString("FirstName"), rsmember.getString("LastName"),rsmember.getString("Email"), rsmember.getString("MobileNo"), rsmember.getString("Username"), (ByteArrayInputStream) rsmember.getBinaryStream("UserImage"));
            }
            stmtmember.close();
            return member;
        } catch (Exception ex) {
            System.out.println("Failed to fetch Member: "+ ex);
            messages = "Failed to fetch Member";
        }finally{
            super.closeConnection();
        }
        
        return null;
    }
    
    //  get list of Members
    public ArrayList<Member_dat> searchmembers(String search){//working
        ArrayList<Member_dat> members = new ArrayList<>();
        String memberquery = "SELECT FirstName, LastName, MobileNo, Email, Username, UserImage FROM members WHERE FirstName LIKE ? OR LastName LIKE ? OR "
                + "MobileNo LIKE ? OR Email LIKE ? OR Username LIKE ?";
        try {
            PreparedStatement stmtmembers = super.getConnection().prepareStatement(memberquery);
            stmtmembers.setString(1, "%"+search+"%");
            stmtmembers.setString(2, "%"+search+"%");
            stmtmembers.setString(3, "%"+search+"%");
            stmtmembers.setString(4, "%"+search+"%");
            stmtmembers.setString(5, "%"+search+"%");
            
            ResultSet rsmember = stmtmembers.executeQuery();
            
            while (rsmember.next()) {                
                Member_dat member = new Member_dat(rsmember.getString("FirstName"), rsmember.getString("LastName"),rsmember.getString("Email"), rsmember.getString("MobileNo"), rsmember.getString("Username"), (ByteArrayInputStream) rsmember.getBinaryStream("UserImage"));
                members.add(member);
            }
            stmtmembers.close();
        } catch (Exception ex) {
            System.out.println("Failed to fetch List of Members: "+ ex);
            messages = "Failed to fetch List of Members";
        }finally{
            super.closeConnection();
        }
        
        return members;
    }

//account retrieval and admin registration operations
    // validate account
    private int validateAccount(String username){//working
        
        String findduplicatequery = "SELECT COUNT(*) FROM accounts WHERE Username = ?";
        
        try {
            PreparedStatement stmtaccount = super.getConnection().prepareStatement(findduplicatequery);
            stmtaccount.setString(1, username);
            
            ResultSet rsaccount = stmtaccount.executeQuery();
            
            while(rsaccount.next()){
                return rsaccount.getInt(1);
            }
            stmtaccount.close();
        } catch (Exception ex) {
            System.out.println("failed to validate account: "+ ex);
        }finally{
            super.closeConnection();
        }
        
        return -1;
    }
    //  log-in account
    public Account_dat loginAccount(String username, String password){//working
        String loginString = "SELECT COUNT(*) AS 'Count', Type, Username FROM accounts WHERE Username = ? AND Password = ?";
        
        try {
            PreparedStatement stmtaccount = super.getConnection().prepareStatement(loginString);
            stmtaccount.setString(1, username);
            stmtaccount.setString(2, password);
            
            ResultSet rslogin = stmtaccount.executeQuery();
            
            while(rslogin.next()){
                int confirm = rslogin.getInt("Count");
                if (confirm > 0) {
                     Account_dat account = new Account_dat(rslogin.getString("Username"), rslogin.getString("Type"));
                     return account;
                }else{
                    messages = "Invalid Username or Password";
                    return null;
                }
            }
            stmtaccount.close();
        } catch (Exception e) {
            System.out.println("Failed to Login.database un reacheable " + e);
            messages = "Failed to Login. database un-reacheable";
        }finally{
            super.closeConnection();
        }
        
        return null;
    }
    
    //  edit account
    public String editAccount(Account_dat newaccount, Account_dat oldaccount){//working

        String accountquery = "UPDATE accounts SET Username  = ?, Password = ? WHERE Username = ? AND Type = ?";
        String query = "Update members SET Username = ? WHERE Username = ?";
        try {
            PreparedStatement stmtaccount = super.getConnection().prepareStatement(accountquery);
            stmtaccount.setString(1, newaccount.getUsername());
            stmtaccount.setString(2, newaccount.getPassword());
            stmtaccount.setString(3, oldaccount.getUsername());
            stmtaccount.setString(4, oldaccount.getType());
            if (loginAccount(oldaccount.getUsername(), oldaccount.getPassword()).getType().equals("Member")) {//triple security measures
                if(validateAccount(newaccount.getUsername()) == 0){
                    int confirmupdate = 0;
                    
                    if (oldaccount.getType().equals("Member")) {
                        PreparedStatement stmt = super.getConnection().prepareStatement(query);
                        stmt.setString(1, newaccount.getUsername());
                        stmt.setString(2, oldaccount.getUsername());
                        confirmupdate = stmtaccount.executeUpdate() + stmt.executeUpdate();
                        stmtaccount.close();
                        stmt.close();
                    }else{
                        confirmupdate = stmtaccount.executeUpdate();
                        stmtaccount.close();
                    }
                    
                    return (confirmupdate > 0) ? "Account edit sucessful!" : "Seems like the Username or user Type does not match please try again.";
                }else{
                    return "Username already exists please try another name";
                }
            }else{
                return "Incorrect Username or password";
            }
            
            
        } catch (Exception e) {
            System.out.println("Account edit failed "+e);
            messages = "Account edit failed";
        }finally{
            super.closeConnection();
        }
        
        return messages;
    }
    
    //create an admin account
    public String createadmin(Account_dat account){//working

        String accountquery = "INSERT INTO accounts(Type, Username, Password) VALUES(?,?,?)";
        
        try {
            PreparedStatement stmtaccount = super.getConnection().prepareStatement(accountquery);
            stmtaccount.setString(1, account.getType());
            stmtaccount.setString(2, account.getUsername());
            stmtaccount.setString(3, account.getPassword());
            
            if (validateAccount(account.getUsername()) == 0) {
                if (account.getType().equals("Admin")) {
                    stmtaccount.executeUpdate();
                    stmtaccount.close();
                    return "Admin Creation Successful.";
                }else{
                    return "Cannot create a type other than Admin";
                }
                
            }else{
                stmtaccount.clearParameters();
                return "The Username is already been used please try another name";
            }
            
        } catch (Exception ex) {
            System.out.println("Database connection failed: "+ ex);
            messages = "Database connection failed";
            
        } finally{
            super.closeConnection();
        }
        
        return messages;
    }
    
//Books retrieval and Registration operations
    //register new book
    public String registerNewBook(Book_dat book){//working
        
        String bookquery = "INSERT INTO books (BookId, BookTitle, Section, BookImage, Copies) "
                + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmtbook = super.getConnection().prepareStatement(bookquery);
            stmtbook.setString(1, book.getBookID());
            stmtbook.setString(2, book.getBookTitle());
            stmtbook.setString(3, book.getSection());
            stmtbook.setBinaryStream(4, book.getImageInputStream(), book.getImageInputStream().available());
            stmtbook.setInt(5, book.getCopies());
            
            if (validateBook(book.getBookID()) == 0) {
                stmtbook.executeUpdate();
                stmtbook.close();
                return "Registration Sucessful!";
                
            }else{
                stmtbook.clearParameters();
                return "The Book ID is already been used please try another ID";
            }
            
        } catch (Exception ex) {
            System.out.println("Database connection failed "+ ex);
            messages = "Database connection failed";
        } finally{
            super.closeConnection();
        }
        
        return messages;
    }
    
    //  editbook
    public String editBook(Book_dat book){//working
        String messages = "Unknown Error Occured.";
        String bookquery = "UPDATE books SET BookTitle  = ?, Section = ?, BookImage = ?, Copies = ? WHERE BookId = ?";
        try {
            PreparedStatement stmtbook = super.getConnection().prepareStatement(bookquery);
            stmtbook.setString(1, book.getBookTitle());
            stmtbook.setString(2, book.getSection());
            stmtbook.setBinaryStream(3, book.getImageInputStream(), book.getImageInputStream().available());
            stmtbook.setInt(4, book.getCopies());
            stmtbook.setString(5, book.getBookID());
            
            int confirmupdate = stmtbook.executeUpdate();
            stmtbook.close();
            return (confirmupdate > 0) ? "Book edit sucessful!" : "book is not registered in database please try again or register a new one";
            
        } catch (Exception e) {
            System.out.println("Statement failed: "+e);
            messages = "Database Un-reacheable";
        }finally{
            super.closeConnection();
        }
        
        return messages;
    }
    
    //validate bookid
    private int validateBook(String bookid){//working
        String findduplicatequery = "SELECT COUNT(*) FROM books WHERE BookId = ?";
        
        try {
            PreparedStatement stmtbook = super.getConnection().prepareStatement(findduplicatequery);
            stmtbook.setString(1, bookid);
            
            ResultSet rsbook = stmtbook.executeQuery();
            
            while(rsbook.next()){
                return rsbook.getInt(1);
            }
            stmtbook.close();
        } catch (Exception ex) {
            System.out.println("Database Un-reacheable " + ex.toString());
            messages = "Database Un-reacheable";
        }finally{
            super.closeConnection();
        }
        
        return -1;
    }
    
    //get a specific book by id
    public Book_dat getBook(String bookID){//working
         Book_dat book = null;
        String memberquery = "SELECT BookId, BookTitle, Section, Copies, BookImage FROM books WHERE BookId = ?";
        try {
            PreparedStatement stmtbooks = super.getConnection().prepareStatement(memberquery);
            stmtbooks.setString(1, bookID);
            ResultSet rsbook = stmtbooks.executeQuery();
            
            while (rsbook.next()) {                
                book = new Book_dat(rsbook.getString("BookId"), rsbook.getString("BookTitle"), rsbook.getString("Section"), rsbook.getInt("Copies"), (ByteArrayInputStream) rsbook.getBinaryStream("BookImage"));
            }
            stmtbooks.close();
        } catch (Exception ex) {
            System.out.println("Database Un-reacheable " + ex.toString());
            messages = "Database Un-reacheable";
        }finally{
            super.closeConnection();
        }
        
        return book;
    }
    
    public ArrayList<Book_dat> searchBook(String search){//working
        ArrayList<Book_dat> books = new ArrayList<>();
        String memberquery = "SELECT BookId, BookTitle, Section, Copies, BookImage FROM books WHERE BookId LIKE ? OR BookTitle LIKE ? OR "
                + "Section LIKE ?";
        try {
            PreparedStatement stmtbooks = super.getConnection().prepareStatement(memberquery);
            stmtbooks.setString(1, "%"+search+"%");
            stmtbooks.setString(2, "%"+search+"%");
            stmtbooks.setString(3, "%"+search+"%");
            
            ResultSet rsbook = stmtbooks.executeQuery();
            
            while (rsbook.next()) {                
                Book_dat book = new Book_dat(rsbook.getString("BookId"), rsbook.getString("BookTitle"), rsbook.getString("Section"), rsbook.getInt("Copies"), (ByteArrayInputStream) rsbook.getBinaryStream("BookImage"));
                books.add(book);
            }
            stmtbooks.close();
        } catch (Exception ex) {
            System.out.println("Database Un-reacheable" + ex.toString());
            messages = "Database Un-reacheable";
        }finally{
            super.closeConnection();
        }
        
        return books;
    }
    
//book leases//////////
    //rent book
    public String rentBook(String member, Book_dat book, String daterented){//working
        messages = "Unknown Error Occured";
        String rentquery = "INSERT INTO book_status(BookId, Username, DateRented) VALUES (?,?,?)";
        
        try {
            
            PreparedStatement stmtbook = super.getConnection().prepareStatement(rentquery);
            stmtbook.setString(1, book.getBookID());
            stmtbook.setString(2, member);
            stmtbook.setString(3, daterented);
            
            Member_dat memberdat = getMember(member);
            
            if(memberdat != null){
                if (validateBook(book.getBookID()) > 0) {
                    int copies = book.getCopies() - getBookRents(book.getBookID()).size();
                    if(copies > 0){
                        stmtbook.executeUpdate();
                        stmtbook.close();
                        return "The book is now under the rent of "+ member;
                        
                    }else{
                        return "The number of book you are trying to rent has reached its available copies";
                        
                    }
                    
                }else{
                    
                    return "Sorry the Book is not registered please try to type again";
                }
            }else{
               return "Sorry the Username is invalid";
            }
            
            
        } catch (Exception ex) {
            System.out.println("Database Un-reacheable" + ex.toString());
            messages = "Database Un-reacheable";
        }finally{
            super.closeConnection();
        }
        return messages;
    }
    
    //get books that under rent
    public ArrayList<BookRent_dat> getBookRents(String search){//working
        String bookrentquery = "SELECT * FROM book_status WHERE BookId = ? OR Username = ?";
        ArrayList<BookRent_dat> bookrents = new ArrayList<>();
        
        try {
            PreparedStatement stmtrent = super.getConnection().prepareStatement(bookrentquery);
            stmtrent.setString(1, search);
            stmtrent.setString(2, search);
            
            ResultSet rsrent = stmtrent.executeQuery();
            
            while (rsrent.next()) {                
                BookRent_dat bookRent_dat = new BookRent_dat(rsrent.getInt("Id"), rsrent.getString("BookId"), rsrent.getString("Username"), rsrent.getString("DateRented"));
                bookrents.add(bookRent_dat);
            }
            stmtrent.close();
        } catch (Exception e) {
            System.out.println("Database Un-reacheable" + e.toString());
            messages = "Database Un-reacheable";
        }finally{
            super.closeConnection();
        }
        
        
        return bookrents;
    }
    
    //get lease counts
    public int countRents(String rent){
        String query = "SELECT COUNT(*) FROM book_status WHERE BookId = ? OR Username = ?";
        
        try {
            PreparedStatement stmt = super.getConnection().prepareStatement(query);
            stmt.setString(1, rent);
            stmt.setString(2, rent);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {                
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Count failed: "+ e);
            messages = "Database un-reacheable";
        }finally{
            super.closeConnection();
        }
        
        return -1;
    }
    
    //delete lease
    public String deletebookRent(Payment_dat rent){//working
        messages = "Unknown Error Occured";
        
        String query = "DELETE FROM book_status WHERE Id = ?";
        
        try {
            PreparedStatement stmt = super.getConnection().prepareStatement(query);
            stmt.setInt(1, rent.getRentID());
            
            int validate = stmt.executeUpdate();
            
            return (validate > 0) ? "Lease period settled!": "We couldn't find the lease you are looking for";
            
        } catch (Exception e) {
            System.out.println("Delete rent failed : " + e);
            messages = "Internal Error occured";
        }
        
        return messages;
    }
    
    //get the initial penalty price for the payment $2.50 per day with 3 days rental period
    public Payment_dat returnBook(BookRent_dat bookrent, String datereturn){//working
        
        LocalDate daterented;
        LocalDate dateofreturn;
        long rentalperiod = 3;
        double penaltyperday = 2.50;
        double penaltyprice = 0;
        
        dateofreturn = LocalDate.parse(datereturn, DateTimeFormatter.ISO_LOCAL_DATE);
        daterented = LocalDate.parse(bookrent.getDateRented(), DateTimeFormatter.ISO_LOCAL_DATE);
        Duration duration = Duration.between(daterented.atStartOfDay(), dateofreturn.atStartOfDay());
        
        long dur = duration.toDays();
        long days = (dur > rentalperiod) ? (dur - rentalperiod) : 0; 

        penaltyprice = (double)(days*penaltyperday);
        
        String id1 = "cpl-";
        String id2 = bookrent.getBookID().substring(3);
        String id3 = bookrent.getUsername().substring(4);
        String id4 = Integer.toString(bookrent.getId());
        String paymentid = id1+id2+id3+id4;
        
        Payment_dat payment = (penaltyprice >= 0) ? new Payment_dat(paymentid, bookrent.getUsername(), bookrent.getBookID(), bookrent.getId(), penaltyprice) : null;
        
        return payment;
    }
    
    public ArrayList<Payment_dat> createPaymentReciept(){
        ArrayList<Payment_dat> payment_dats = new ArrayList<>();
        return payment_dats;
    }
    //  record payments after paying
    public int recordPayments(ArrayList<Payment_dat> payment_dats){
        int validate = 0;
        String query = "INSERT INTO payment_records (PaymentId, Username, BookId, PenaltyPrice, PaymentDate) VALUES (?,?,?,?,?)";
        try {
            
            for(Payment_dat rec : payment_dats){
                LocalDate paydate = LocalDate.now();
                DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE;
                rec.setPaymentDate(paydate.format(format));
                PreparedStatement stmt = super.getConnection().prepareStatement(query);
                stmt.setString(1, rec.getPaymentID());
                stmt.setString(2, rec.getUsername());
                stmt.setString(3, rec.getBookID());
                stmt.setDouble(4, rec.getPenaltyPrice());
                stmt.setString(5, rec.getPaymentDate());
                validate += stmt.executeUpdate();
                stmt.close();
            }
            
        } catch (Exception e) {
            System.out.println("Error Occured "+ e);
            messages = "Database Un-reacheable";
        }finally{
            super.closeConnection();
        }
        
        return validate;
    }

    public String getMessages() {
        return messages;
    }
}
