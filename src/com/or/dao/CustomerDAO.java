package com.or.dao;

import com.or.db.ConnectionPool;
import com.or.errors.CrudException;
import com.or.enums.CrudOperation;
import com.or.enums.EntityType;
import com.or.model.Customer;
import com.or.util.ObjectExtractionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends UserDAO<Long, Customer> {

    public static final CustomerDAO instance = new CustomerDAO();
    private ConnectionPool connectionPool = null;

    //Calling to the connection pool and getting an instance with creation of this class
    //Creating a private constructor
    private CustomerDAO() {
        try {
            connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    //-------------------------------------Creating a new customer--------------------------------------------------
    @Override
    public Long create(final Customer customer) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "INSERT INTO customers (first_name, last_name, email, password) VALUES(?,?,?,?)";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, String.valueOf(customer.getPassword().hashCode()));
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
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.CREATE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Getting a customer(read)--------------------------------------------------

    @Override
    public Customer read(final Long id) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM customers WHERE id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setLong(1, id);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();
            //Checking if our variable is not empty/null
            if (!result.next()) {
                return null;
            }
            //Getting our result from our database translated to our Customer object
            return ObjectExtractionUtil.resultToCustomer(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Updating an existing customer--------------------------------------------------

    @Override
    public void update(final Customer customer) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "UPDATE customers SET first_name = ?, last_name = ?,  email = ?, password = ? WHERE id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, String.valueOf(customer.getPassword().hashCode()));
            preparedStatement.setLong(5, customer.getId());
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.UPDATE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Deleting an existing customer--------------------------------------------------

    @Override
    public void delete(final Long id) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "DELETE FROM customers WHERE id = ?";
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
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.DELETE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Getting all existing customers--------------------------------------------

    @Override
    public List<Customer> readAll() throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM customers";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();

            List<Customer> customers = new ArrayList<>();

            //Checking if our variable is not empty/null and adding the returning customer object to a list
            while (result.next()) {
                customers.add(ObjectExtractionUtil.resultToCustomer(result));
            }
            return customers;
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.READALL);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Getting an existing company by customer's email ---------------------------------------

    @Override
    public Customer readByEmail(final String email) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM customers WHERE email = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setString(1, email);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();

            //Checking if our variable is not empty/null
            if (!result.next()) {
                return null;
            }
            //Getting our result from our database translated to our Customer object
            return ObjectExtractionUtil.resultToCustomer(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.CUSTOMER, CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Getting an existing company by company's id ---------------------------------------

    @Override
    public Customer readById(final Long customerId) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM customers WHERE id = ?";
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
            //Getting our result from our database translated to our Customer object
            return ObjectExtractionUtil.resultToCustomer(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY, CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Getting an existing company by company's name ---------------------------------------

    @Override
    public Customer readByName(final String name) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM customers WHERE first_name = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setString(1, name);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();

            //Checking if our variable is not empty/null
            if (!result.next()) {
                return null;
            }
            //Getting our result from our database translated to our Customer object
            return ObjectExtractionUtil.resultToCustomer(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY, CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }
}

