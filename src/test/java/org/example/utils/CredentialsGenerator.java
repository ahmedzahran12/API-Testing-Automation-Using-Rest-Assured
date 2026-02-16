package org.example.utils;

import com.github.javafaker.Faker;

public class CredentialsGenerator {
    private static final Faker faker = new Faker();

    public static String generateRandomUsername() {
        return faker.name().firstName() + faker.name().lastName();
    }
    public static String generateSpecialChUserName(){
        return faker.lorem().characters(20) + "_@{]";
    }

    public static String generateRandomPassword() {
        return faker.internet().password(8, 16);
    }
    public static String generateInvalidPassword() { return faker.internet().password(1, 5); }
}
