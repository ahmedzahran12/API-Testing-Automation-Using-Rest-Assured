package org.example.pojos_bodies.response.adminresponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResponse {
    private String id;
    private String name;
    private String description;
    private String price;
    private String stock;

    public String getId() {return id;}
    public void setId(String id) {this.id=id;}

    public String getName() {return name;}
    public void setName(String name) {this.name=name;}

    public String getDescription() {return description;}
    public void setDescription(String age) {this.description=description;}

    public String getPrice() {return price;}
    public void setJob(String job) {this.price=price;}

    public String getStock() {return stock;}
    public void setGender(String gender) {this.stock=stock;}
}
