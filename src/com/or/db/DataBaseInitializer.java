package com.or.db;

import com.or.errors.DBError;
import com.or.enums.DBType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInitializer {

    //-------------------------------------Creating the tables in our database---------------------------------------

    public static void setupDatabase() {
        try {
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            //Creating a SQL query and Executing our query - COMPANY TABLE
            ConnectionPool.getInstance().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `coupons_project`.`companies` (\n" +
                    "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  `email` VARCHAR(45) NOT NULL,\n" +
                    "  `password` INT NOT NULL,\n" +
                    "   PRIMARY KEY (`id`),\n" +
                    "   UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                    "   UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,\n" +
                    "   UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);"
            ).execute();

            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            //Creating a SQL query and Executing our query - CATEGORIES TABLE
            ConnectionPool.getInstance().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `coupons_project`.`categories` (\n" +
                    " `category` VARCHAR(45) NOT NULL,\n" +
                    "  UNIQUE INDEX `category_UNIQUE` (`category` ASC));"
            ).execute();

            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            //Creating a SQL query and Executing our query - COUPONS TABLE
            ConnectionPool.getInstance().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `coupons_project`.`coupons` (\n" +
                    "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "  `company_id` BIGINT NOT NULL,\n" +
                    "  `category` VARCHAR(45) NOT NULL,\n" +
                    "  `title` VARCHAR(45) NOT NULL,\n" +
                    "  `description` VARCHAR(45) NULL,\n" +
                    "  `start_date` VARCHAR(45) NOT NULL,\n" +
                    "  `end_date` VARCHAR(45) NOT NULL,\n" +
                    "  `amount` VARCHAR(45) NOT NULL,\n" +
                    "  `price` VARCHAR(45) NOT NULL,\n" +
                    "  `image` VARCHAR(120) NULL,\n" +
                    "   PRIMARY KEY (`id`),\n" +
                    "   UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                    "   UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE,\n" +
                    "   CONSTRAINT `company_id`\n" +
                    "   FOREIGN KEY (`company_id`)\n" +
                    "   REFERENCES `coupons_project`.`companies` (`id`)\n" +
                    "   ON DELETE NO ACTION\n" +
                    "   ON UPDATE NO ACTION,\n" +
                    "   CONSTRAINT `category`\n" +
                    "   FOREIGN KEY (`category`)\n" +
                    "   REFERENCES `coupons_project`.`categories` (`category`)\n" +
                    "   ON DELETE NO ACTION\n" +
                    "   ON UPDATE NO ACTION);"
            ).execute();

            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            //Creating a SQL query and Executing our query - CUSTOMERS TABLE
            ConnectionPool.getInstance().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `coupons_project`.`customers` (\n" +
                    " `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    " `first_name` VARCHAR(45) NOT NULL,\n" +
                    " `last_name` VARCHAR(45) NOT NULL,\n" +
                    " `email` VARCHAR(45) NOT NULL,\n" +
                    " `password` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                    "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);"
            ).execute();

            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            //Creating a SQL query and Executing our query - CUSTOMERS TO COUPONS TABLE
            ConnectionPool.getInstance().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `coupons_project`.`customer_vs_coupon` (\n" +
                    " `customer_id` BIGINT NOT NULL,\n" +
                    " `coupon_id` BIGINT NOT NULL,\n" +
                    "  PRIMARY KEY (`customer_id`, `coupon_id`),\n" +
                    "  CONSTRAINT `coupon_id`\n" +
                    "  FOREIGN KEY (`coupon_id`)\n" +
                    "  REFERENCES `coupons_project`.`coupons` (`id`)\n" +
                    "  ON DELETE NO ACTION\n" +
                    "  ON UPDATE NO ACTION,\n" +
                    "  CONSTRAINT `customer_id`\n" +
                    "  FOREIGN KEY (`customer_id`)\n" +
                    "  REFERENCES `coupons_project`.`customers` (`id`)\n" +
                    "  ON DELETE NO ACTION\n" +
                    "  ON UPDATE NO ACTION);"
            ).execute();
            System.out.println("*** All tables in * coupons_project * have been created! ***");
            System.out.println();
        } catch (InterruptedException | SQLException e) {
            System.err.println(e);
            throw new DBError(DBType.CREATE_TABLES);
        }
    }

    //---------------------------Injecting the specific categories to the categories table---------------------------

    public static void setupCategoriesInTable() {

        //Creating the SQL queries
        String query = "INSERT INTO `coupons_project`.`categories` (`category`) VALUE ('COMPUTERS')";
        String query1 = "INSERT INTO `coupons_project`.`categories` (`category`) VALUE ('FOOD')";
        String query2 = "INSERT INTO `coupons_project`.`categories` (`category`) VALUE ('ELECTRICITY');";
        String query3 = "INSERT INTO `coupons_project`.`categories` (`category`) VALUE ('VACATION');";
        String query4 = "INSERT INTO `coupons_project`.`categories` (`category`) VALUE ('FASHION');";
        String query5 = "INSERT INTO `coupons_project`.`categories` (`category`) VALUE ('FURNITURE');";

        //Adding the queries to a list
        List<String> queries = new ArrayList<>();
        queries.add(query);
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);
        queries.add(query4);
        queries.add(query5);

        //Executing our query on sql - for each category
        queries.forEach(q -> {
            try {
                ConnectionPool.getInstance().getConnection().prepareStatement(q).execute();
            } catch (InterruptedException | SQLException e) {
                System.err.println(e);
                throw new DBError(DBType.CREATE_TABLES);
            }
        });
        System.out.println("*** The Categories table in * coupons_project * has been created successfully! ***");
        System.out.println();
    }

    //-----------------------------------Deleting our tables on SQL----------------------------------------------

    public static void dropTables() {
        try {
            //Creating a SQL query
            String query = "DROP TABLE `coupons_project`.`coupons`, `coupons_project`.`customer_vs_coupon`," +
                    " `coupons_project`.`customers`, `coupons_project`.`categories`, `coupons_project`.`companies` ";
            ////Executing our query on SQL
            ConnectionPool.getInstance().getConnection().prepareStatement(query).execute();
            System.out.println();
            System.out.println("*** All tables in * coupons_project * have been deleted! ***");
            System.out.println();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new DBError(DBType.DROP_TABLES);
        }
    }
}
