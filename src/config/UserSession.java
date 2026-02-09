/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author LAGARBE
 */
public class UserSession {
    
    private static UserSession instance;
    
    private int userId;
    private String username;
    private String email;
    private String role;
    private String status;
    
    private UserSession() {
    }
    
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void setUserData(int userId, String username, String email, String role, String status) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
    }
    
    public void clearSession() {
        this.userId = 0;
        this.username = null;
        this.email = null;
        this.role = null;
        this.status = null;
    }
    
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
}