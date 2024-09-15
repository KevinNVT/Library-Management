package com.autozone.utils;

import java.lang.reflect.Field;

import com.autozone.annotations.Isbn;
import com.autozone.annotations.MembersName;
import com.autozone.annotations.NotEmpty;
import com.autozone.annotations.NotNull;

public class Validator {
	
	public static void validate(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            // Not null validation
            if (field.isAnnotationPresent(NotNull.class)) {
                Object value = field.get(obj);

                if (value == null) {
                    throw new IllegalArgumentException(field.getName() + " cannot be null");
                }
            }
            
            // Not Empty validation
            if (field.isAnnotationPresent(NotEmpty.class)) {
                String value = (String) field.get(obj);
                    
                if (value != null && value.isEmpty()) {
                    throw new IllegalArgumentException(field.getName() + " cannot be empty");
                }
            }

            // Members name validation. Only letters, allows spaces, compound names or multiple surnames.
            // Updated to accept accents
            if (field.isAnnotationPresent(MembersName.class)) {
                String value = (String) field.get(obj);

                if (value != null && !value.matches("^[-a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s']+$")) {
                    throw new IllegalArgumentException(field.getName() + " is not a valid entry");
                }
            }

            // Validates the ISBN contains only numbers and is 13 digits long
            if (field.isAnnotationPresent(Isbn.class)) {
                String value = (String) field.get(obj);

                if (value != null && !value.matches("^\\d{13}$")) {
                    String fieldName = field.getName();
                    String errorMessage = "Field " + fieldName + " must contain only numbers and must be 13 digits long.";
                    System.out.println(errorMessage);
                }
            }
        }
    }
}
