package org.example.utils;

import com.github.javafaker.Faker;

public class ItemGenerator {
    private static final Faker faker = new Faker();

    public static String generateRandomProductName() {
        return faker.commerce().productName();
    }

    public static int generateRandomPrice() {
        return (int) faker.number().randomNumber(4, true);
    }

    public static String generateRandomDescription() {
        return faker.lorem().paragraph();
    }
    public static int generateRandomStock(){
        return (int) faker.number().randomNumber(2,true);
    }
}
