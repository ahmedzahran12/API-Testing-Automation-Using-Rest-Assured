package org.example.pojos_bodies.request;

public class UserData {
    private String username;
    private String password;
    private boolean isAdmin;
    private String details;

    public UserData() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}