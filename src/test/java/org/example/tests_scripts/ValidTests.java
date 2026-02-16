package org.example.tests_scripts;

import org.example.apis_admin.AdminLogin;
import org.example.apis_admin.ItemManager;
import org.example.apis_admin.OrderManager;
import org.example.apis_customer.CustomerLogin;
import org.example.apis_customer.ItemViewer;
import org.example.apis_customer.OrderCustomerManager;
import org.example.pojos_bodies.request.UserData;
import org.example.pojos_bodies.request.adminrequests.*;
import org.example.pojos_bodies.request.customerrequests.CustomerLoginPojo;
import org.example.pojos_bodies.response.adminresponse.AdminLoginResponse;
import org.example.pojos_bodies.response.adminresponse.ItemResponse;
import org.example.pojos_bodies.response.adminresponse.OrderResponse;
import org.example.pojos_bodies.response.customerresponse.CustomerLoginResponse;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
public class ValidTests {
    private String adminToken;
    private String customerToken;
    private String id;
    private String orderId;
    private UserData adminData;
    private UserData customerData;
    public ValidTests(UserData adminData, UserData customerData) {
        this.adminData = adminData;
        this.customerData = customerData;
    }

    @BeforeClass
    public void Login() throws InterruptedException {
        System.out.println("=== Starting flow ===");
        System.out.println("Admin: " + adminData.getUsername());
        System.out.println("Customer: " + customerData.getUsername());

        // Admin Login
        AdminLoginPojo adminCredentials = new AdminLoginPojo();
        adminCredentials.setUsername(adminData.getUsername());
        adminCredentials.setPassword(adminData.getPassword());

        AdminLogin adminLogin = new AdminLogin();
        AdminLoginResponse adminResponse = adminLogin.login(adminCredentials)
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response().as(AdminLoginResponse.class);
        adminToken = adminResponse.getToken();
        Thread.sleep(2000);

        // Customer Login
        CustomerLoginPojo customerCredentials = new CustomerLoginPojo();
        customerCredentials.setUsername(customerData.getUsername());
        customerCredentials.setPassword(customerData.getPassword());

        CustomerLogin customerLogin = new CustomerLogin();
        CustomerLoginResponse customerResponse = customerLogin.login(customerCredentials)
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response().as(CustomerLoginResponse.class);
        customerToken = customerResponse.getToken();
        Thread.sleep(2000);
    }



    @Test
    public void addItemsTest() throws InterruptedException {
        Thread.sleep(2000);
        AddItemPojo addItems = new AddItemPojo();
        addItems.setName("phone");
        addItems.setPrice(10000);
        addItems.setDescription("This is a new phone");
        addItems.setStock(10);
        ItemManager itemManager = new ItemManager();
        ItemResponse itemResponse = itemManager.addItem(adminToken,addItems)
                .then()
                .statusCode(201)
                .log().all()
                .extract()
                .response().as(ItemResponse.class);
        id=itemResponse.getId();
    }

    @Test(dependsOnMethods = "addItemsTest")
    public void editItemsTest() throws InterruptedException {
        Thread.sleep(2000);
        EditItemPojo editItems = new EditItemPojo();
        editItems.setName("phone");
        editItems.setPrice(10000);
        editItems.setDescription("This is a new phone");
        editItems.setStock(10);
        ItemManager itemManager = new ItemManager();
        ItemResponse itemResponse = itemManager.editItem(id,adminToken,editItems)
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response().as(ItemResponse.class);
    }

    @Test(dependsOnMethods = "editItemsTest")
    public void createOrderTest() throws InterruptedException {
        Thread.sleep(2000);
        OrderManager orderManager = new OrderManager();
        CreateOrderPojo createOrder = new CreateOrderPojo();
        createOrder.setCustomerId(customerData.getUsername());
        List<Items> items = new ArrayList<>();
        Items item = new Items();
        item.setItemId(id);
        item.setQuantity(1);
        items.add(item);
        createOrder.setItems(items);
        OrderResponse response = orderManager.createOrder(adminToken,createOrder)
                .then()
                .statusCode(201)
                .log().all()
                .extract()
                .response().as(OrderResponse.class);
        orderId=response.getId();
    }

   @Test(dependsOnMethods = "createOrderTest")
    public void getOrdersTest() throws InterruptedException {
        Thread.sleep(2000);
        OrderManager orderManager = new OrderManager();
         OrderResponse [] response=orderManager.getOrders(adminToken)
                .then()
                .statusCode(200)
                .log().all()
                 .extract()
                 .as(OrderResponse[].class);

    }
    @Test(dependsOnMethods = "getOrdersTest")
    public void getItemsTest() throws InterruptedException{
        Thread.sleep(2000);
        ItemViewer itemViewer = new ItemViewer();
        itemViewer.getItems(customerToken)
                .then()
                .statusCode(200)
                .log().all();
    }
    @Test(dependsOnMethods = "getItemsTest")
    public void checkOutOrderTest() throws InterruptedException{
        Thread.sleep(3000);
        OrderCustomerManager orderManager = new OrderCustomerManager();
        orderManager.checkoutOrder(orderId,customerToken)
                .then()
                .statusCode(200)
                .log().all();
    }
    @Test(dependsOnMethods = "checkOutOrderTest")
    public void getOrderTest() throws InterruptedException{
        Thread.sleep(2000);
        OrderCustomerManager orderManager = new OrderCustomerManager();
        orderManager.getOrders(customerToken)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(dependsOnMethods = "getOrderTest" )
    public void getPaidOrdersTest() throws InterruptedException{
        Thread.sleep(2000);
        OrderManager orderManager = new OrderManager();
        orderManager.getPaidOrders(adminToken)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(dependsOnMethods = "getPaidOrdersTest")
    public void deleteOrderTest() throws InterruptedException {
        Thread.sleep(1000);
        OrderManager orderManager = new OrderManager();
        orderManager.deleteOrder(orderId,adminToken)
                .then()
                .statusCode(204)
                .log().all();

    }

    @Test(dependsOnMethods = "deleteOrderTest")
    public void deleteItemTest() throws InterruptedException {
        Thread.sleep(3000);
        ItemManager itemManager = new ItemManager();
         itemManager.deleteItem(id,adminToken)
                .then()
                .statusCode(204)
                 .log().all();
    }
    @AfterMethod
    public void tearDown(){
        System.out.println("Test End");
    }
}
