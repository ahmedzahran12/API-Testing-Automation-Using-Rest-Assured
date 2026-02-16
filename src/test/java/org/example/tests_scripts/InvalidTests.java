package org.example.tests_scripts;

import org.example.apis_admin.AdminLogin;
import org.example.apis_admin.ItemManager;
import org.example.apis_admin.OrderManager;
import org.example.apis_customer.CustomerLogin;
import org.example.apis_customer.ItemViewer;
import org.example.pojos_bodies.request.adminrequests.*;
import org.example.pojos_bodies.request.customerrequests.CustomerLoginPojo;
import org.example.specs_variables.ErrorMessages;
import org.example.utils.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class InvalidTests {
    
    private String adminToken;
    private String customerToken;
    private static final String NON_EXISTENT_ITEM_ID = "17723123";
    private static final String EXISTING_ITEM_ID = "1770659104510"; 

    @BeforeClass
    public void setup() throws InterruptedException {
        // Admin Login
        AdminLogin adminLogin = new AdminLogin();
        AdminLoginPojo adminCredentials = new AdminLoginPojo();
        adminCredentials.setUsername(ConfigManager.getProperty("admin.username"));
        adminCredentials.setPassword(ConfigManager.getProperty("admin.password"));
        
        adminToken = adminLogin.login(adminCredentials)
                .then()
                .statusCode(200)
                .extract()
                .response().jsonPath().getString("token");

        Thread.sleep(1000);

        // Customer Login
        CustomerLogin customerLogin = new CustomerLogin();
        CustomerLoginPojo customerCredentials = new CustomerLoginPojo();
        customerCredentials.setUsername(ConfigManager.getProperty("customer.username"));
        customerCredentials.setPassword(ConfigManager.getProperty("customer.password"));
        
        customerToken = customerLogin.login(customerCredentials)
                .then()
                .statusCode(200)
                .extract()
                .response().jsonPath().getString("token");
                
        Thread.sleep(1000);
    }

    @Test
    public void invalidCredentialsTest() throws InterruptedException {
        Thread.sleep(2000);
        CustomerLoginPojo credentials = new CustomerLoginPojo();
        credentials.setUsername(CredentialsGenerator.generateRandomUsername());
        credentials.setPassword(CredentialsGenerator.generateRandomPassword());
        CustomerLogin customerLogin = new CustomerLogin();
        customerLogin.login(credentials)
                .then()
                .statusCode(401)
                .log().all();
    }

    @Test
    public void specialChUserNameTest() throws InterruptedException {
        Thread.sleep(2000);
        CustomerLoginPojo credentials = new CustomerLoginPojo();
        credentials.setUsername(CredentialsGenerator.generateSpecialChUserName());
        credentials.setPassword(CredentialsGenerator.generateRandomPassword());
        CustomerLogin customerLogin = new CustomerLogin();
        customerLogin.login(credentials)
                .then()
                .statusCode(400)
                .body("error" , equalTo(ErrorMessages.USERNAME_ALPHANUMERIC))
                .log().all();
    }

    @Test
    public void LoginWithMissingUserName() throws InterruptedException {
        Thread.sleep(2000);
        CustomerLoginPojo credentials = new CustomerLoginPojo();
        credentials.setPassword(CredentialsGenerator.generateRandomPassword());
        credentials.setUsername(null);
        CustomerLogin customerLogin = new CustomerLogin();
        customerLogin.login(credentials)
                .then()
                .statusCode(400)
                .body("error", equalTo(ErrorMessages.USERNAME_REQUIRED))
                .log().all();
    }

    @Test
    public void LoginWithInvalidPassword() throws InterruptedException {
        Thread.sleep(2000);
        CustomerLoginPojo credentials = new CustomerLoginPojo();
        credentials.setUsername(CredentialsGenerator.generateRandomUsername());
        credentials.setPassword(CredentialsGenerator.generateInvalidPassword());
        CustomerLogin customerLogin = new CustomerLogin();
        customerLogin.login(credentials)
                .then()
                .statusCode(400)
                .body("error", equalTo(ErrorMessages.PASSWORD_MIN_LENGTH))
                .log().all();
    }

    @Test
    public void LoginWithEmptyUserName() throws InterruptedException {
        Thread.sleep(2000);
        CustomerLoginPojo credentials = new CustomerLoginPojo();
        credentials.setUsername("");
        credentials.setPassword(CredentialsGenerator.generateRandomPassword());
        CustomerLogin customerLogin = new CustomerLogin();
        customerLogin.login(credentials)
                .then()
                .statusCode(400)
                .body("error", equalTo(ErrorMessages.USERNAME_CANNOT_BE_EMPTY))
                .log().all();
    }

    @Test
    public void getItemsUnAuthorized() throws InterruptedException {
        Thread.sleep(2000);
        ItemViewer itemViewer = new ItemViewer();
        itemViewer.getItems("invalid_token")
                .then()
                .statusCode(401)
                .log().all();
    }

    @Test
    public void createItemCustomer() throws InterruptedException {
        Thread.sleep(2000);
        ItemManager itemManager = new ItemManager();
        
        AddItemPojo addItem = new AddItemPojo();
        addItem.setName(ItemGenerator.generateRandomProductName());
        addItem.setPrice(ItemGenerator.generateRandomPrice());
        addItem.setDescription(ItemGenerator.generateRandomDescription());
        addItem.setStock(ItemGenerator.generateRandomStock());
        
        itemManager.addItem(customerToken, addItem)
                .then()
                .statusCode(403)
                .body("error" , equalTo(ErrorMessages.FORBIDDEN_CUSTOMER))
                .log().all();
    }

    @Test
    public void createItemWithMissingName() throws InterruptedException {
        Thread.sleep(2000);
        ItemManager itemManager = new ItemManager();
        
        AddItemPojo addItem = new AddItemPojo();
        addItem.setPrice(ItemGenerator.generateRandomPrice());
        addItem.setDescription(ItemGenerator.generateRandomDescription());
        addItem.setStock(ItemGenerator.generateRandomStock());
        
        itemManager.addItem(adminToken, addItem)
                .then()
                .statusCode(400)
                .body("error" , equalTo(ErrorMessages.ITEM_NAME_REQUIRED))
                .log().all();
    }

    @Test
    public void createItemWithNegativePrice() throws InterruptedException {
        Thread.sleep(2000);
        ItemManager itemManager = new ItemManager();
        
        AddItemPojo addItem = new AddItemPojo();
        addItem.setName(ItemGenerator.generateRandomProductName());
        addItem.setPrice( -(ItemGenerator.generateRandomPrice()));
        addItem.setDescription(ItemGenerator.generateRandomDescription());
        addItem.setStock(ItemGenerator.generateRandomStock());
        
        itemManager.addItem(adminToken, addItem)
                .then()
                .statusCode(400)
                .body("error" , equalTo(ErrorMessages.PRICE_POSITIVE))
                .log().all();
    }

    @Test
    public void EditItemCustomer() throws InterruptedException {
        Thread.sleep(2000);
        ItemManager itemManager = new ItemManager();
        
        EditItemPojo editItem = new EditItemPojo();
        editItem.setName(ItemGenerator.generateRandomProductName());
        editItem.setPrice(ItemGenerator.generateRandomPrice());
        editItem.setDescription(ItemGenerator.generateRandomDescription());
        editItem.setStock(ItemGenerator.generateRandomStock());
        
        itemManager.editItem(EXISTING_ITEM_ID, customerToken, editItem)
                .then()
                .statusCode(403)
                .body("error" , equalTo(ErrorMessages.FORBIDDEN_CUSTOMER))
                .log().all();
    }

    @Test
    public void EditNotFoundItem() throws InterruptedException {
        Thread.sleep(2000);
        ItemManager itemManager = new ItemManager();
        
        EditItemPojo editItem = new EditItemPojo();
        editItem.setName(ItemGenerator.generateRandomProductName());
        editItem.setPrice(ItemGenerator.generateRandomPrice());
        editItem.setDescription(ItemGenerator.generateRandomDescription());
        editItem.setStock(ItemGenerator.generateRandomStock());
        
        itemManager.editItem(NON_EXISTENT_ITEM_ID, adminToken, editItem)
                .then()
                .statusCode(404)
                .body("error" , equalTo(ErrorMessages.itemNotFound(NON_EXISTENT_ITEM_ID)))
                .log().all();
    }

    @Test
    public void EditItemWithNegativePrice() throws InterruptedException {
        Thread.sleep(2000);
        ItemManager itemManager = new ItemManager();
        
        EditItemPojo editItem = new EditItemPojo();
        editItem.setName(ItemGenerator.generateRandomProductName());
        editItem.setPrice( -(ItemGenerator.generateRandomPrice()));
        editItem.setDescription(ItemGenerator.generateRandomDescription());
        editItem.setStock(ItemGenerator.generateRandomStock());
        
        itemManager.editItem(EXISTING_ITEM_ID, adminToken, editItem)
                .then()
                .statusCode(400)
                .body("error" , equalTo(ErrorMessages.PRICE_POSITIVE))
                .log().all();
    }

    @Test
    public void DeleteNotFoundItem() throws InterruptedException {
        Thread.sleep(2000);
        ItemManager itemManager = new ItemManager();
        
        itemManager.deleteItem(NON_EXISTENT_ITEM_ID, adminToken)
                .then()
                .statusCode(404)
                .body("error" , equalTo(ErrorMessages.itemNotFound(NON_EXISTENT_ITEM_ID)))
                .log().all();
    }

    @Test
    public void deleteItemCustomer() throws InterruptedException {
        Thread.sleep(2000);
        ItemManager itemManager = new ItemManager();
        
        itemManager.deleteItem(EXISTING_ITEM_ID, customerToken)
                .then()
                .statusCode(403)
                .body("error" , equalTo(ErrorMessages.FORBIDDEN_CUSTOMER))
                .log().all();
    }

    @Test
    public void createOrderWithoutCustomerId() throws InterruptedException {
        Thread.sleep(2000);
        OrderManager orderManager = new OrderManager();
        
        CreateOrderPojo order = new CreateOrderPojo();
        List<Items> items = new ArrayList<>();
        items.add(OrderGenerator.generateRandomItems());
        order.setItems(items);
        
        orderManager.createOrder(adminToken, order)
                .then()
                .statusCode(400)
                .body("error" , equalTo(ErrorMessages.CUSTOMER_ID_REQUIRED))
                .log().all();
    }
}
