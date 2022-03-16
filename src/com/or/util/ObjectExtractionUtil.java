package com.or.util;

import com.or.enums.Categories;
import com.or.model.Company;
import com.or.model.Coupon;
import com.or.model.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectExtractionUtil {

    //------------------------------------------Getting a company object-------------------------------------------

    public static Company resultToCompany(final ResultSet result) throws SQLException {

        //Returning a company object from ResultSet
        return new Company(
                result.getLong("id"),
                result.getString("name"),
                result.getString("email"),
                result.getString("password")
        );
    }

    //------------------------------------------Getting a customer object-------------------------------------------

    public static Customer resultToCustomer(final ResultSet result) throws SQLException {

        //Returning a customer object from ResultSet
        return new Customer(
                result.getLong("id"),
                result.getString("first_name"),
                result.getString("last_name"),
                result.getString("email"),
                result.getString("password")
        );
    }

    //------------------------------------------Getting a customer Id-------------------------------------------

    public static Customer resultToCustomerId(final ResultSet result) throws SQLException {

        //Returning a customer Id from ResultSet
        return new Customer(
                result.getLong("customer_id")
        );
    }

    //------------------------------------------Getting a coupon object--------------------------------------------

    public static Coupon resultToCoupon(final ResultSet result) throws SQLException {

        //Returning a coupon object from ResultSet
        return new Coupon(
                result.getLong("id"),
                result.getLong("company_id"),
                Categories.valueOf(result.getString("category")),
                result.getString("title"),
                result.getString("description"),
                result.getDate("start_date"),
                result.getDate("end_date"),
                result.getInt("amount"),
                result.getDouble("price"),
                result.getString("image")
        );
    }
}
