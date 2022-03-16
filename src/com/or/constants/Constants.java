package com.or.constants;

public class Constants {

    //Daily job sleep period - 24 hours in milliseconds
    public static final int DAILY_JOB_SLEEP_PERIOD = 86400000;
    //Admin login credentials -
    public static final String ADMIN_LOGIN_EMAIL = "admin@admin.com";
    public static final String ADMIN_LOGIN_PASSWORD = "admin";
    //Database connection url - SQL
    public static final String SQL_URL = "jdbc:mysql://localhost:3306/coupons_project";
    //SQL user default name
    public static final String SQL_USER = "root";
    //Placing SQL password into an environment variable
    public static final String SQL_PASS = System.getenv("SQL_PASSWORD");
    //Setting the number of connections in the stack
    public static final int NUMBER_OF_CONNECTIONS = 20;
    //Setting the hours of a day
    public static final int NOON = 12;
    public static final int EVENING = 18;
    public static final int NIGHT = 20;
    //Setting the ANSI reset so that we can reset the color of the console in the end
    public static final String ANSI_DEFAULT_RESET = "\u001B[0m";
    //Setting the ANSI colors
    public static final String ANSI_ORANGE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";


}
