package com.or;

import com.or.constants.Constants;
import com.or.dao.CompanyDAO;
import com.or.dao.CustomerDAO;
import com.or.db.ConnectionPool;
import com.or.db.DataBaseInitializer;
import com.or.enums.Categories;
import com.or.enums.ClientType;
import com.or.errors.ApplicationException;
import com.or.facade.*;
import com.or.loggin.LoginManager;
import com.or.model.Company;
import com.or.model.Coupon;
import com.or.model.Customer;
import com.or.thread.CouponExpirationDailyJob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class TestingSystem {

    //Creating a state of this class scanner & and login manager instance
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final LoginManager loggingManager = LoginManager.instance;

    public void testAll() throws ApplicationException, SQLException {

        boolean stop = true;

        CouponExpirationDailyJob couponExpirationDailyJob = new CouponExpirationDailyJob();
        //Setting a new thread and injecting the runnable to its constructor
        Thread dailyJobThread = new Thread(couponExpirationDailyJob);

        openingPrompt();

        while (stop) {
            startMenu();
            int choice = SCANNER.nextInt();
            switch (choice) {

                case 1:
                    //Dropping & Setting up the sql tables
                    DataBaseInitializer.dropTables();
                    DataBaseInitializer.setupDatabase();
                    DataBaseInitializer.setupCategoriesInTable();
                    break;

                case 2:
                    creatingDefaultEntities();
                    dailyJobThread.start();
                    break;

                case 3:
                    loginTest();
                    break;

                case 4:
                    advancedTests();
                    break;

                case 5:
                    //Closing all connections
                    ConnectionPool.getInstance().closeAllConnections();
                    //Killing our thread and closing the program
                    dailyJobThread.stop();
                    stop = false;
                    System.out.println(Constants.ANSI_YELLOW_BACKGROUND + Constants.ANSI_BLACK + "GOODBYE!" +
                            Constants.ANSI_DEFAULT_RESET);
                    break;
            }
        }
    }


    private void creatingDefaultEntities() throws ApplicationException {

        final AdminFacade adminFacade = AdminFacade.instance;
        final CompanyFacade companyFacade = CompanyFacade.instance;
        final CustomerFacade customerFacade = CustomerFacade.instance;

        //------------------------Creating default companies in our database-------------------------------------

        List<Company> companiesList = new ArrayList<>();

        companiesList.add(new Company("Orburger", "com1@gmail.com", "Com1"));
        companiesList.add(new Company("Michnasaim", "com2@gmail.com", "Com2"));
        companiesList.add(new Company("Hashmal", "com3@gmail.com", "Com3"));
        companiesList.add(new Company("Hul", "com4@gmail.com", "Com4"));
        companiesList.add(new Company("Shualim", "com5@gmail.com", "Com5"));
        companiesList.add(new Company("Tachles", "com6@gmail.com", "Com6"));
        companiesList.add(new Company("Dilim", "com7@gmail.com", "Com7"));
        companiesList.add(new Company("Yeshivot", "com8@gmail.com", "Com8"));
        companiesList.add(new Company("Teruf", "com9@gmail.com", "Com9"));
        companiesList.add(new Company("Weepo", "com10@gmail.com", "Com10"));

        System.out.println("Companies creation by default --> ");
        System.out.println();
        //Creating operation for each company
        companiesList.forEach(company -> {
            try {
                adminFacade.createCompany(company);
            } catch (ApplicationException e) {
                System.err.println(e);
            }
        });
        System.out.println();

        //------------------------Creating default customers in our database-------------------------------------

        List<Customer> customersList = new ArrayList<>();
        customersList.add(new Customer("Shaul", "Shalom", "user1@gmail.com", "User1"));
        customersList.add(new Customer("Kadosh", "Rezach", "user2@gmail.com", "User2"));
        customersList.add(new Customer("Shmuel", "Hagever", "user3@gmail.com", "User3"));
        customersList.add(new Customer("Gever", "Levy", "user4@gmail.com", "User4"));
        customersList.add(new Customer("Yaron", "London", "user5@gmail.com", "User5"));
        customersList.add(new Customer("Moti", "Manor", "user6@gmail.com", "User6"));
        customersList.add(new Customer("Miki", "Liki", "user7@gmail.com", "User7"));
        customersList.add(new Customer("Teva", "Naot", "user8@gmail.com", "User8"));
        customersList.add(new Customer("Levi", "Cohen", "user9@gmail.com", "User9"));
        customersList.add(new Customer("Nir", "Katorza", "user10@gmail.com", "User10"));

        System.out.println("Customers creation by default --> ");
        System.out.println();
        //Creating operation for each customer
        customersList.forEach(customer -> {
            try {
                adminFacade.createCustomer(customer);
            } catch (ApplicationException e) {
                System.err.println(e);
            }
        });
        System.out.println();

        //------------------------Creating default coupons in our database---------------------------------------

        List<Coupon> couponsList = new ArrayList<>();
        couponsList.add(new Coupon(1L, Categories.FOOD, "Lamburger", "BURGER", Date.valueOf("2022-02-28"),
                Date.valueOf("2022-04-30"), 3, 59.0, "jdbc:mysql://localhost:3306/coupons_project/1"));
        couponsList.add(new Coupon(1L, Categories.FOOD, "Cheeseburger", "BURGER", Date.valueOf("2022-02-28"),
                Date.valueOf("2022-04-27"), 5, 45.0, "jdbc:mysql://localhost:3306/coupons_project/2"));
        couponsList.add(new Coupon(2L, Categories.FASHION, "Skinny Jeans", "PANTS", Date.valueOf("2022-02-28"),
                Date.valueOf("2022-05-25"), 20, 100.0, "jdbc:mysql://localhost:3306/coupons_project/3"));
        couponsList.add(new Coupon(3L, Categories.ELECTRICITY, "Tanoor", "OVEN", Date.valueOf("2022-02-28"),
                Date.valueOf("2022-07-30"), 6, 1500.50, "jdbc:mysql://localhost:3306/coupons_project/4"));
        couponsList.add(new Coupon(4L, Categories.VACATION, "Kal", "ZANZIBAR", Date.valueOf("2022-02-28"),
                Date.valueOf("2022-08-30"), 1, 7000.80, "jdbc:mysql://localhost:3306/coupons_project/5"));
        couponsList.add(new Coupon(4L, Categories.VACATION, "Barur", "Ski", Date.valueOf("2022-02-15"),
                Date.valueOf("2022-08-30"), 3, 15000.80, "jdbc:mysql://localhost:3306/coupons_project/6"));
        couponsList.add(new Coupon(5L, Categories.FOOD, "Orez", "Parsi", Date.valueOf("2022-02-28"),
                Date.valueOf("2022-06-30"), 6, 8.90, "jdbc:mysql://localhost:3306/coupons_project/7"));
        couponsList.add(new Coupon(6L, Categories.FASHION, "Kapuchon", "Style", Date.valueOf("2022-01-28"),
                Date.valueOf("2022-10-30"), 15, 199.0, "jdbc:mysql://localhost:3306/coupons_project/8"));
        couponsList.add(new Coupon(7L, Categories.COMPUTERS, "MACBOOK PRO", "Til", Date.valueOf("2022-03-10"),
                Date.valueOf("2022-05-12"), 4, 13500.80, "jdbc:mysql://localhost:3306/coupons_project/9"));
        couponsList.add(new Coupon(8L, Categories.FURNITURE, "Sofa", "Natuzi", Date.valueOf("2022-03-05"),
                Date.valueOf("2022-10-30"), 2, 4100.0, "jdbc:mysql://localhost:3306/coupons_project/10"));

        System.out.println("Coupons creation by default --> ");
        System.out.println();
        //Creating operation for each coupon
        couponsList.forEach(coupon -> {
            try {
                companyFacade.createCoupon(coupon);
            } catch (ApplicationException e) {
                System.err.println(e);
            }
        });
        System.out.println();

        //------------------------Creating default purchases in our database-----------------------------------

        System.out.println("Purchases creation by default --> ");
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Amount of coupon before the purchasing: "
                + companyFacade.getCoupon(1L).getTitle() + " is: "
                + companyFacade.getCoupon(1L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(1L, 1L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(1L).getTitle() + " is: "
                + companyFacade.getCoupon(1L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Amount of coupon before the purchasing: "
                + companyFacade.getCoupon(2L).getTitle() + " is: "
                + companyFacade.getCoupon(2L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(2L, 2L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(2L).getTitle() + " is: "
                + companyFacade.getCoupon(2L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Amount of coupon before the purchasing: "
                + companyFacade.getCoupon(3L).getTitle() + " is: "
                + companyFacade.getCoupon(3L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(3L, 3L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(3L).getTitle() + " is: "
                + companyFacade.getCoupon(3L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Amount of coupon before the purchasing: "
                + companyFacade.getCoupon(4L).getTitle() + " is: "
                + companyFacade.getCoupon(4L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(4L, 4L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(4L).getTitle() + " is: "
                + companyFacade.getCoupon(4L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Current amount of coupon: "
                + companyFacade.getCoupon(5L).getTitle() + " is: "
                + companyFacade.getCoupon(5L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(5L, 5L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(5L).getTitle() + " is: "
                + companyFacade.getCoupon(5L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Current amount of coupon: "
                + companyFacade.getCoupon(6L).getTitle() + " is: "
                + companyFacade.getCoupon(6L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(6L, 6L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(6L).getTitle() + " is: "
                + companyFacade.getCoupon(6L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Current amount of coupon: "
                + companyFacade.getCoupon(3L).getTitle() + " is: "
                + companyFacade.getCoupon(3L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(7L, 3L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(3L).getTitle() + " is: "
                + companyFacade.getCoupon(3L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Current amount of coupon: "
                + companyFacade.getCoupon(7L).getTitle() + " is: "
                + companyFacade.getCoupon(7L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(8L, 7L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(7L).getTitle() + " is: "
                + companyFacade.getCoupon(7L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Current amount of coupon: "
                + companyFacade.getCoupon(9L).getTitle() + " is: "
                + companyFacade.getCoupon(9L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(9L, 9L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(9L).getTitle() + " is: "
                + companyFacade.getCoupon(9L).getAmount());
        System.out.println();

        //Showing the amount of coupon before purchasing
        System.out.println("Current amount of coupon: "
                + companyFacade.getCoupon(10L).getTitle() + " is: "
                + companyFacade.getCoupon(10L).getAmount());
        //Creating the purchase operation
        customerFacade.addCouponPurchase(10L, 10L);
        //Showing the amount of coupon after purchasing
        System.out.println("Current amount of coupon after purchasing: "
                + companyFacade.getCoupon(10L).getTitle() + " is: "
                + companyFacade.getCoupon(10L).getAmount());
        System.out.println();
    }

    private void loginTest() throws ApplicationException {

        System.out.println();
        System.out.println("Please perform login:");
        System.out.print("Please enter your clientType: " + "admin/company/customer: ");
        String clientType = SCANNER.next().toUpperCase();
        System.out.print("Please enter your Email: ");
        String loginEmail = SCANNER.next();
        System.out.print("Please enter your Password: ");
        String loginPassword = SCANNER.next();

        //Setting an enum variable to enum class values translated to capital letters
        ClientType chosenClientType = ClientType.valueOf(clientType.toUpperCase());

        switch (clientType) {

            case "ADMINISTRATOR":
                chosenClientType = ClientType.ADMIN;

                break;
            case "COMPANY":
                chosenClientType = ClientType.COMPANY;

                break;
            case "CUSTOMER":
                chosenClientType = ClientType.CUSTOMER;
                break;
        }

        ClientFacade clientFacade = loggingManager.login(loginEmail, loginPassword, chosenClientType);

        String loginSucceed = "Login completed successfully! ***Welcome ";

        //Getting the correct facade according to login
        if (clientFacade instanceof AdminFacade) {
            clientFacade = AdminFacade.instance;
            String facadeName = clientFacade.getClass().getCanonicalName();
            System.out.println(loginSucceed + facadeName + adminNamePrompt());
            System.out.println();
            //Printing the admin operations
            adminOperationsMenu();
            System.out.println();
            //Running admin's operations Test
            adminTest();

        } else if (clientFacade instanceof CompanyFacade) {
            clientFacade = CompanyFacade.instance;
            String facadeName = clientFacade.getClass().getCanonicalName();
            Company company = CompanyDAO.instance.readByEmail(loginEmail);
            System.out.println(loginSucceed + facadeName + companyNamePrompt(company));
            System.out.println();
            //Printing the company operations
            companyOperationsMenu();
            System.out.println();
            //Running company's operations Test
            companyTest();

        } else if (clientFacade instanceof CustomerFacade) {
            clientFacade = CustomerFacade.instance;
            String facadeName = clientFacade.getClass().getCanonicalName();
            Customer customer = CustomerDAO.instance.readByEmail(loginEmail);
            System.out.println(loginSucceed + facadeName + customerNamePrompt(customer));
            System.out.println();
            //Printing the customer operations
            customerOperationsMenu();
            System.out.println();
            //Running customer's operations Test
            customerTest();
        }
    }

    private String adminNamePrompt() {

        return "|" + Constants.ANSI_GREEN + "|| Administrator ||" + Constants.ANSI_DEFAULT_RESET + "***";
    }

    private String companyNamePrompt(Company company) {

        return "||" + Constants.ANSI_GREEN + company.getName() + Constants.ANSI_DEFAULT_RESET + "||" + "***";
    }

    private String customerNamePrompt(Customer customer) {

        return "||" + Constants.ANSI_GREEN + customer.getFirstName() + " " + customer.getLastName() +
                Constants.ANSI_DEFAULT_RESET + "||" + "***";
    }

    private void adminOperationsMenu() {

        System.out.println("Admin's operations: ");
        System.out.println(
                        "Adding a new company " + "\n" +
                        "Updating an existing company " + "\n" +
                        "Deleting an existing company " + "\n" +
                        "Getting all existing companies " + "\n" +
                        "Getting a specific company (Company ID) " + "\n" +
                        "Adding a new customer " + "\n" +
                        "Updating an existing customer " + "\n" +
                        "Deleting an existing customer " + "\n" +
                        "Getting all existing customers" + "\n" +
                        "Getting a specific customer (Customer ID)");
        System.out.println();
    }

    private void companyOperationsMenu() {

        System.out.println("Company's operations: ");
        System.out.println(
                        "Adding a new coupon " + "\n" +
                        "Updating an existing coupon " + "\n" +
                        "Deleting an existing coupon " + "\n" +
                        "Getting all existing coupons of company " + "\n" +
                        "Getting all coupons (Category type) " + "\n" +
                        "Getting all coupons (Max price limit) " + "\n" +
                        "Getting a specific company ");
        System.out.println();
    }

    private void customerOperationsMenu() {

        System.out.println("Customer's operations: ");
        System.out.println(
                        "Purchase a coupon " + "\n" +
                        "Getting all existing coupons of company  " + "\n" +
                        "Getting all coupons (Category type) " + "\n" +
                        "Getting all coupons (Max price limit) " + "\n" +
                        "Getting a specific customer");
        System.out.println();
    }

    private void startMenu() {
        System.out.println(Constants.ANSI_RED_BACKGROUND + "Please choose the desired activity you wish to perform: " +
                           Constants.ANSI_DEFAULT_RESET);
        System.out.println();
        System.out.println(
                        "1 - Drop and create tables in Database" + "\n" +
                        "2 - Create by default all Entities & purchases (10 from each)" + "\n" +
                        "3 - Perform login & enter to system tests" + "\n" +
                        "4 - Enter to advanced tests" + "\n" +
                        "5 - Exit");
    }

    private void openingPrompt() {

        //Setting a local date variable to current date
        LocalDate localDate = LocalDate.now();
        //Getting the specific day of the week
        java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        //Setting a local time variable to current time
        LocalTime currentTime = LocalTime.now();
        //Setting a variable to specific hour
        final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String dayPeriod = "";

        if (hour < Constants.NOON) {
            dayPeriod = "Morning";
        } else if (hour >= Constants.NOON && hour < Constants.EVENING) {
            dayPeriod = "Afternoon";
        } else if (hour >= Constants.EVENING && hour < Constants.NIGHT) {
            dayPeriod = "Evening";
        } else if (hour >= Constants.NIGHT) {
            dayPeriod = "Night";
        }

        System.out.println();
        System.out.println(Constants.ANSI_CYAN + "--------------------(|" + dayOfWeek + "| - "
                + currentTime.format(DateTimeFormatter.ofPattern("HH:mm")) + ")------------------------"
                + Constants.ANSI_DEFAULT_RESET);
        System.out.println();
        System.out.println(Constants.ANSI_ORANGE + "Good " + dayPeriod + "!" +
                           " Welcome to the 1st phase of Coupons project!" +
                           Constants.ANSI_DEFAULT_RESET);
        System.out.println();
    }

    private void adminTest() throws ApplicationException {

        AdminFacade clientFacade = AdminFacade.instance;

        System.out.println("Test 1 - Updating a company -->");
        //Getting the current company before updating
        System.out.println("Older company: " + clientFacade.getCompany(1L));
        clientFacade.updateCompany(new Company(1L, "Orburger", "Test@gmail.com", "Test1"));
        //Getting the updated company including coupons
        System.out.println("Updated company: " + clientFacade.getCompany(1L));
        System.out.println();

        System.out.println("Test 2 - Deleting an existing company -->");
        clientFacade.deleteCompany(2L);
        try {
            //If test valid - we will receive an exception - Company not exist
            System.out.println(clientFacade.getCompany(2L));
        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Test 3 - Getting all existing companies including coupons -->");
        System.out.println(clientFacade.getAllCompanies());
        System.out.println();

        System.out.println("Test 4 - Getting an existing company including coupons -->");
        System.out.println(clientFacade.getCompany(3L));
        System.out.println();

        System.out.println("Test 5 - Updating an existing customer -->");
        //Getting the current customer before updating
        System.out.println("Older customer: " + clientFacade.getCustomer(1L));
        clientFacade.updateCustomer(new Customer(1L, "Test", "Test", "Test@gmail.com",
                "Test1"));
        //Getting the updated customer including coupons
        System.out.println("Updated customer: " + clientFacade.getCustomer(1L));
        System.out.println();

        System.out.println("Test 6 - Deleting an existing customer -->");
        clientFacade.deleteCustomer(2L);
        try {
            //If test valid - we will receive an exception - Customer not exist
            System.out.println(clientFacade.getCustomer(2L));
        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Test 7 - Getting all existing customers including coupons -->");
        System.out.println(clientFacade.getAllCustomers());
        System.out.println();

        System.out.println("Test 8 - Getting an existing customer including coupons -->");
        //If test valid - In that case we will receive customer with no coupons due to company deletion on test 2
        System.out.println(clientFacade.getCustomer(3L));

        System.out.println();
        System.out.println(Constants.ANSI_RED_BACKGROUND + "Admin test passed successfully!" +
                           Constants.ANSI_DEFAULT_RESET);
        System.out.println();

    }

    private void companyTest() throws ApplicationException {

        CompanyFacade clientFacade = CompanyFacade.instance;

        System.out.println("Test 1 - Updating an existing coupon -->");
        //Getting the current coupon before updating
        System.out.println("Older coupon: " + clientFacade.getCoupon(4L));
        clientFacade.updateCoupon(new Coupon(4L, 3L, Categories.COMPUTERS, "Asus-laptop",
                "Gamers Future", Date.valueOf("2022-03-05"), Date.valueOf("2022-06-20"),
                7, 6000.0, "www"));
        //Getting the updated coupon
        System.out.println("Updated coupon: " + clientFacade.getCoupon(4L));
        System.out.println();

        System.out.println("Test 2 - Deleting an existing coupon -->");
        clientFacade.deleteCoupon(4L);
        try {
            //If test valid - we will receive an exception - Coupon not exist
            System.out.println(clientFacade.getCoupon(4L));
        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Test 3 - Getting all existing coupons for specific company -->");
        System.out.println(clientFacade.getAllCoupons(1L));
        System.out.println();

        System.out.println("Test 4 - Getting all coupons for specific company from specific category -->");
        //If test valid - We will receive the coupon according to category
        System.out.println(clientFacade.getCouponsByCategory(4L, Categories.VACATION));
        //If test valid - We will receive a comment - this category is not relevant to company's coupons
        System.out.println(clientFacade.getCouponsByCategory(4L, Categories.FOOD));
        System.out.println();

        System.out.println("Test 5 - Getting all coupons for specific company according to specific price -->");
        //If test valid - We will receive the relevant coupons that are less than the inserted price
        System.out.println(clientFacade.getCouponsByMaxPrice(1L, 50.0));
        System.out.println();

        System.out.println("Test 6 - Getting an existing company including coupons -->");
        System.out.println(clientFacade.getCompany(8L));

        System.out.println();
        System.out.println(Constants.ANSI_RED_BACKGROUND + "Company test passed successfully!" +
                           Constants.ANSI_DEFAULT_RESET);
        System.out.println();

    }

    private void customerTest() throws ApplicationException {

        CustomerFacade clientFacade = CustomerFacade.instance;

        System.out.println("Test 1 - Getting all coupons which specific customer has purchased -->");
        System.out.println(clientFacade.getAllCoupons(1L));
        System.out.println();

        System.out.println("Test 2 - Getting all coupons for specific customer from specific category -->");
        //If test valid - We will receive the coupon according to category
        System.out.println(clientFacade.getCouponsByCategory(1L, Categories.FURNITURE));
        //If test valid - We will receive a comment this category is not relevant to customer's coupons
        System.out.println(clientFacade.getCouponsByCategory(1L, Categories.FOOD));
        System.out.println();

        System.out.println("Test 3 - Getting all coupons for specific customer according to specific price -->");
        //If test valid - We will receive the relevant coupons that are less than the inserted price
        System.out.println(clientFacade.getCouponByMaxPrice(5L, 2500.0));
        System.out.println(clientFacade.getCouponByMaxPrice(5L, 9500.0));
        System.out.println();

        System.out.println("Test 4 - Getting an existing customer including coupons -->");
        System.out.println(clientFacade.getCustomer(5L));

        System.out.println();
        System.out.println(Constants.ANSI_RED_BACKGROUND + "Customer test passed successfully!" + Constants.ANSI_DEFAULT_RESET);
        System.out.println();
    }


    private void advancedTests() {

        AdminFacade adminFacade = AdminFacade.instance;
        CompanyFacade companyFacade = CompanyFacade.instance;
        CustomerFacade customerFacade = CustomerFacade.instance;

        System.out.println("Testing creation of company with same existing company's email -->");
        try {
            //If test valid - we will receive an exception - Company already exist
            adminFacade.createCompany(new Company("Salamtak", "com3@gmail.com", "Com9"));

        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Testing reading a company that is not exist -->");
        try {
            //If test valid - we will receive an exception - Company is not exist
            adminFacade.getCompany(15L);

        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Testing updating an existing company's name -->");
        try {
            //If test valid - we will receive an exception - Company's name is already exists
            adminFacade.updateCompany(new Company(3L, "Wallak", "com3@gmail.com", "Com3"));

        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Testing creating a new customer with same email of an existing customer -->");
        try {
            //If test valid - we will receive an exception - Customer already exist
            adminFacade.createCustomer(new Customer("Or", "Or", "user3@gmail.com",
                                      "Com4"));
        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Testing purchase of same coupon -->");
        try {
            //If test valid - we will receive an exception - Coupon already purchased
            customerFacade.addCouponPurchase(1L, 1L);

        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Testing purchase of coupon that not in stock anymore -->");
        try {
            //If test valid - we will receive an exception - Coupon is currently not in stock
            customerFacade.addCouponPurchase(3L, 5L);

        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Testing creation of coupon with same title as of existing one related to same company -->");
        try {
            //If test valid - we will receive an exception - Coupon already exist
            companyFacade.createCoupon(new Coupon(1L, Categories.FURNITURE, "Kiseh",
                    "CHAIR", Date.valueOf("2022-02-28"), Date.valueOf("2022-05-30"),
                    5, 20.0, "jdbc:mysql://localhost:3306/coupons_project/2"));

        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("Testing creation of coupon with invalid date -->");
        try {
            //If test valid - we will receive an exception - Date is invalid
            companyFacade.createCoupon(new Coupon(10L, Categories.FOOD, "Toast-Naknik",
                    "Taim", Date.valueOf("2022-01-28"), Date.valueOf("2022-02-30"),
                    2, 32.5, "jdbc:mysql://localhost:3306/coupons_project"));
        } catch (ApplicationException e) {
            System.out.println(e);
        }
        System.out.println();
    }
}
















