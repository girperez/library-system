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
public class Member_dat{
    
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String contact;
    private InputStream imageInputStream;
    
    public Member_dat(){
        
    }
//Registration
    //  important attributes that should be in database
    public Member_dat(String username, String contact,  File image) throws Exception{
        this.username = username;
        this.contact = contact;
        this.imageInputStream = new FileInputStream(image);
    }
    //full detailed data for registration
    public Member_dat(String firstname, String lastname, String email, String contact, String username, File image) throws Exception {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.username = username;
        this.imageInputStream = new FileInputStream(image);
    }
//data retrieval
    // profile member with no books possesion details
    public Member_dat(String firstname, String lastname, String email, String contact, String username, InputStream imageInputStream) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.username = username;
        this.imageInputStream = imageInputStream;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public InputStream getImageInputStream() {
        return imageInputStream;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageInputStream(InputStream imageInputStream) {
        this.imageInputStream = imageInputStream;
    }
}
