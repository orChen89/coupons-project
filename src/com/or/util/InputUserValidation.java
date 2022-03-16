package com.or.util;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUserValidation {

    private final static String PASSWORD_REGEX = "[a-zA-Z0-9]{4,12}";
    private final static String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private final static String DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";

    // --------------------------------Password validation--------------------------------------------------

    public static boolean isPasswordValid (String password) {

        //Checking if the password is matching to the REGEX format
        return password.matches(PASSWORD_REGEX);
    }

    // --------------------------------Email validation-----------------------------------------------------

    public static boolean isEmailValid (String email) {

        //Translating the specific pattern to the REGEX format
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

        //Checking if the inserted email is matching to the pattern
        Matcher matcher = emailPattern.matcher(email);

        return matcher.find();
    }

    // --------------------------------Date validation-------------------------------------------------------

    public static boolean isDateValid (Date date) {

       //Checking if the inserted date is matching to the REGEX format
       return date.toString().matches(DATE_REGEX);
    }
}
