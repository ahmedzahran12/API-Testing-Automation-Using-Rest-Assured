package org.example.specs_variables;

public class ErrorMessages {
    public static final String USERNAME_ALPHANUMERIC = "Username must contain only alphanumeric characters.";
    public static final String USERNAME_REQUIRED = "Username is required.";
    public static final String PASSWORD_MIN_LENGTH = "Password should have a minimum length of 6.";
    public static final String USERNAME_CANNOT_BE_EMPTY = "Username cannot be empty.";
    public static final String AUTHENTICATION_REQUIRED = "Authentication required";
    public static final String FORBIDDEN_CUSTOMER = "Forbidden: customer does not have access.";
    public static final String ITEM_NAME_REQUIRED = "Item name is required.";
    public static final String PRICE_POSITIVE = "Price must be a positive number.";
    public static final String CUSTOMER_ID_REQUIRED = "Customer ID is required.";
    
    public static String itemNotFound(String id) {
        return "Item with ID " + id + " not found.";
    }
}
