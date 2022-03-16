package com.or.dao;

import com.or.db.ConnectionPool;
import com.or.errors.CrudException;
import com.or.enums.CrudOperation;
import com.or.enums.EntityType;
import com.or.model.Coupon;
import com.or.util.ObjectExtractionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO implements CrudDAO<Long, Coupon> {

    public static final CouponDAO instance = new CouponDAO();
    private ConnectionPool connectionPool = null;

    //Calling to the connection pool and getting an instance with creation of this class
    //Creating a private constructor
    private CouponDAO(){
        try {
            connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    //-------------------------------------Creating a new coupon--------------------------------------------------

    @Override
    public Long create(final Coupon coupon) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "INSERT INTO coupons (company_id, category, title, description" +
                    ", start_date, end_date, amount, price, image) VALUES(?,?,?,?,?,?,?,?,?)";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our sql query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, coupon.getCompanyID());
            preparedStatement.setString(2, String.valueOf(coupon.getCategory()));
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5,  coupon.getStartDate());
            preparedStatement.setDate(6,  coupon.getEndDate());
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
            //Storing our "id" in a ResultSet variable
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            //Checking if our variable is not null and auto-increment is valid
            if (!generatedKeysResult.next()) {
                throw new RuntimeException("Failed to retrieve auto-incremented id");
            }
            //Getting our "id"
            return generatedKeysResult.getLong(1);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON, CrudOperation.CREATE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Getting a coupon(read)--------------------------------------------------

    @Override
    public Coupon read(final Long id) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM coupons WHERE id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our sql query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, id);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();
            //Checking if our variable is not empty/null
            if (!result.next()) {
                return null;
            }
            //Getting our result from our database translated to our Coupon object
            return ObjectExtractionUtil.resultToCoupon(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Updating an existing coupon--------------------------------------------------

    @Override
    public void update(final Coupon coupon) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "UPDATE coupons SET company_id = ?, category = ?,  " +
                                  "title = ?, description = ?, start_date = ?, end_date = ?," +
                                  " amount = ?, price = ?, image = ? WHERE id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our sql query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, coupon.getCompanyID());
            preparedStatement.setString(2, String.valueOf(coupon.getCategory()));
            preparedStatement.setString(3, coupon.getTitle());
            preparedStatement.setString(4, coupon.getDescription());
            preparedStatement.setDate(5,  coupon.getStartDate());
            preparedStatement.setDate(6,  coupon.getEndDate());
            preparedStatement.setInt(7, coupon.getAmount());
            preparedStatement.setDouble(8, coupon.getPrice());
            preparedStatement.setString(9, coupon.getImage());
            preparedStatement.setLong(10, coupon.getId());
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.UPDATE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Deleting an existing coupon--------------------------------------------------

    @Override
    public void delete(final Long id) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "DELETE FROM coupons WHERE id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead of the question marks in the SQL statement above
            preparedStatement.setLong(1, id);
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.DELETE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Getting all existing coupons--------------------------------------------------

    @Override
    public List<Coupon> readAll() throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM coupons";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();

            List<Coupon> coupons = new ArrayList<>();
            //Checking if our variable is not empty/null and adding the returning coupon object to a list
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.READALL);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Creating a new purchase of coupon ---------------------------------------

    public void addCouponPurchase(final Long customerID, final Long couponID) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "INSERT INTO customer_vs_coupon (customer_id, coupon_id) VALUES(?,?)";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, customerID);
            preparedStatement.setLong(2, couponID);
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.CREATE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Deleting an existing purchase of coupon ---------------------------------------

    public void deleteCouponPurchase(final Long customerID, final Long couponID) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "DELETE FROM customer_vs_coupon WHERE customer_id = ? AND coupon_id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, customerID);
            preparedStatement.setLong(2, couponID);
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.DELETE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Deleting an existing purchase of coupon---------------------------------------

    public void deleteCouponPurchaseByCouponId(final Long couponID) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "DELETE FROM customer_vs_coupon WHERE coupon_id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, couponID);
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.DELETE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-----------------------------Getting an existing customer ID by coupon's Id(read)-----------------------------

    public Long readCouponPurchaseByCouponId(final Long couponID) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM customer_vs_coupon WHERE coupon_id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, couponID);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();
            //Checking if our variable is not empty/null
            if (!result.next()) {
                return null;
            }
            //Getting our result from our database translated to our Customer's Id
            return ObjectExtractionUtil.resultToCustomerId(result).getId();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-----------------------------Getting an existing coupon by customer's Id(read)---------------------------------

    public Coupon readCouponPurchaseByCustomerId(final Long customerId) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM coupons, customer_vs_coupon WHERE coupon_id = " +
                    "customer_vs_coupon.coupon_id " +
                    "AND customer_vs_coupon.customer_id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, customerId);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();
            //Checking if our variable is not empty/null
            if (!result.next()) {
                return null;
            }
            //Getting our result from our database translated to our Coupon object
            return ObjectExtractionUtil.resultToCoupon(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-----------------------------Getting an existing coupons by customer's Id(read)--------------------------------

    public List<Coupon> readCouponsPurchaseByCustomerId(final Long customerId) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT coupon_id FROM customer_vs_coupon WHERE customer_id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, customerId);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();

            List<Coupon> couponsByCustomerId = new ArrayList<>();
            //Checking if our variable is not empty/null
            while (result.next()) {
                //Adding to our coupons list the result of coupon
                couponsByCustomerId.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            //Getting our result from our database translated to our Coupon object
            return couponsByCustomerId;
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------Getting list of coupons Id's by customer's Id (read)-------------------------------

    public List<Long> readCouponsByCustomerId(final long customerId) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            final String sqlStatement = "SELECT coupon_id FROM customer_vs_coupon WHERE customer_id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            final PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, customerId);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            final ResultSet result = preparedStatement.executeQuery();

            final List<Long> idList = new ArrayList<>();
            //Checking if our variable is not empty/null
            while (result.next()) {
                //Adding to our Id's list the result of coupons Id
                idList.add(result.getLong(1));
            }
            return idList;
        } catch (SQLException | InterruptedException e) {
            throw new CrudException(EntityType.COUPON, CrudOperation.CREATE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-----------------------------Getting all company coupons by company's Id(read)-------------------------------

    public List<Coupon> readCouponsByCompanyId(final long companyId) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM coupons WHERE company_id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, companyId);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();

            List<Coupon> coupons = new ArrayList<>();
            //Checking if our variable is not empty/null and adding the returning coupon object to a list
            while (result.next()) {
                coupons.add(ObjectExtractionUtil.resultToCoupon(result));
            }
            return coupons;
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------Getting a specific company coupon by company's Id(read)-----------------------------

    public Coupon readCouponByCompanyId(final long companyId) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM coupons WHERE company_id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, companyId);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();
            //Checking if our variable is not empty/null
            if (!result.next()) {
                return null;
            }
            //Getting our result from our database translated to our Coupon object
            return ObjectExtractionUtil.resultToCoupon(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COUPON,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }
}



