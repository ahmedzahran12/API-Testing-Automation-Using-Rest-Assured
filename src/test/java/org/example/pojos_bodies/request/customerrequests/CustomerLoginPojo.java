package org.example.pojos_bodies.request.customerrequests;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerLoginPojo {
    private String username;
    private String password;
    public CustomerLoginPojo(){}
    public CustomerLoginPojo(String username, String password) {
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
