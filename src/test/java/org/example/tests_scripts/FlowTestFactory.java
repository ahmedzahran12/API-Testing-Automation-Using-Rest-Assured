package org.example.tests_scripts;

import org.example.utils.JsonReader;
import org.example.pojos_bodies.request.UserData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FlowTestFactory {

    @DataProvider(name = "userDataProvider")
    public static Object[][] getUserData() throws IOException {
        String jsonFilePath = "src/test/resources/users.json";

        List<UserData> allUsers = JsonReader.getUsersFromJson(jsonFilePath);

        // Find admin (first user with isAdmin = true)
        UserData admin = allUsers.stream()
                .filter(UserData::isAdmin)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No admin found in users.json"));

        // Get all customers (users with isAdmin = false)
        List<UserData> customers = allUsers.stream()
                .filter(user -> !user.isAdmin())
                .collect(Collectors.toList());

        // Create array with admin + each customer
        Object[][] data = new Object[customers.size()][2];
        for (int i = 0; i < customers.size(); i++) {
            data[i][0] = admin;           // Same admin for all instances
            data[i][1] = customers.get(i); // Different customer per instance
        }

        return data;
    }

    @Factory(dataProvider = "userDataProvider")
    public Object[] createInstances(UserData admin, UserData customer) {
        return new Object[]{new ValidTests(admin, customer)};
    }
}