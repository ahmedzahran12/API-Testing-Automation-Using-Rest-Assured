package org.example.pojos_bodies.request.adminrequests;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateOrderPojo {
    private String customerId;
    private List<Items> items;
    public void setItems(List<Items> items){
        this.items=items;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getCustomerId() {
        return customerId;
    }
    public List<Items> getItems() {
        return items;
    }
}
