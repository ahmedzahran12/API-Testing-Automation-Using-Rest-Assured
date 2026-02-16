package org.example.pojos_bodies.request.adminrequests;
// POJO
public class AdminLoginPojo {
    private String username;
    private String password;
    public AdminLoginPojo(){}
    public AdminLoginPojo(String username, String password) {
    this.username=username;
    this.password=password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {return username;}
    public String getPassword() {return password;}

}
