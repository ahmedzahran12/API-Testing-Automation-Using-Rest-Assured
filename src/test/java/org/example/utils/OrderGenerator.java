package org.example.utils;

import com.github.javafaker.Faker;
import org.example.pojos_bodies.request.adminrequests.Items;

import java.util.List;

public class OrderGenerator {
    private static final Faker faker = new Faker();

    public static String generateCustomerId(){
        return faker.name().username();
    }
    public static Items generateRandomItems(){
        Items item = new Items();
        item.setItemId("1770661221274");
        item.setQuantity(faker.number().numberBetween(1,100));
        return item;
    }
}
