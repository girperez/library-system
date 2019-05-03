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
public class Account_dat{
    
    private String username;
    private String type;
    private String password;
    
    
    public Account_dat(){
        
    }
    
    public Account_dat(String username, String type, String password){
        this.username = username;
        this.type = type;
        this.password = password;
    }
    
    public Account_dat(String username, String type){
        this.username = username;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
