package com.or.dao;

import com.or.db.ConnectionPool;
import com.or.errors.CrudException;
import com.or.enums.CrudOperation;
import com.or.enums.EntityType;
import com.or.model.Company;
import com.or.util.ObjectExtractionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO extends UserDAO<Long, Company> {

    public static final CompanyDAO instance = new CompanyDAO();
    private ConnectionPool connectionPool = null;

    //Calling to the connection pool and getting an instance with creation of this class
    //Creating a private constructor
    private CompanyDAO(){
        try {
            connectionPool = ConnectionPool.getInstance();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
//-------------------------------------Creating a new company--------------------------------------------------
    @Override
    public Long create(final Company company) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "INSERT INTO companies (name, email, password) VALUES(?,?,?)";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, String.valueOf(company.getPassword().hashCode()));
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
            //Storing our "id" in a ResultSet variable
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            //Checking if our variable is not empty/null and auto-increment is valid
            if (!generatedKeysResult.next()) {
                throw new RuntimeException("Failed to retrieve auto-incremented id");
            }
            //Getting  our "id"
            return generatedKeysResult.getLong(1);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY,CrudOperation.CREATE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Getting a company(read)--------------------------------------------------

    @Override
    public Company read(final Long id) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM companies WHERE id = ?";
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
            //Getting our result from our database translated to our Company object
            return ObjectExtractionUtil.resultToCompany(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Updating an existing company--------------------------------------------------

    @Override
    public void update(final Company company) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "UPDATE companies SET name = ?, email = ?, password = ? WHERE id = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setString(1, company.getName());
            preparedStatement.setString(2, company.getEmail());
            preparedStatement.setString(3, String.valueOf(company.getPassword().hashCode()));
            preparedStatement.setLong(4, company.getId());
            //Executing our query on SQL - Performing a DML operation
            preparedStatement.executeUpdate();
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY,CrudOperation.UPDATE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Deleting an existing company--------------------------------------------------

    @Override
    public void delete(final Long id) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "DELETE FROM companies WHERE id = ?";
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
            throw new CrudException(EntityType.COMPANY,CrudOperation.DELETE);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //-------------------------------------Getting all existing companies--------------------------------------------------

    @Override
    public List<Company> readAll() throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM companies";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();

            List<Company> companies = new ArrayList<>();
            //Checking if our variable is not empty/null and adding the returning company object to a list
            while (result.next()) {
                companies.add(ObjectExtractionUtil.resultToCompany(result));
           }
            return companies;
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY,CrudOperation.READALL);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Getting an existing company by company's email ---------------------------------------

    @Override
    public Company readByEmail(final String email) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM companies WHERE email = ?";
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
            //Getting our result from our database translated to our Company object
            return ObjectExtractionUtil.resultToCompany(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Getting an existing company by company's id ---------------------------------------

    public Company readById(final Long companyId) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM companies WHERE id = ?";
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
            //Getting our result from our database translated to our Company object
            return ObjectExtractionUtil.resultToCompany(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }

    //--------------------------------Getting an existing company by company's name ---------------------------------------

    public Company readByName(final String companyName) throws CrudException {
        Connection connection = null;
        try {
            //Creating a SQL query
            String sqlStatement = "SELECT * FROM companies WHERE name = ?";
            //Establishing a connection from the connection pool - Asking for one of the connections(pop)
            connection = connectionPool.getConnection();
            //Setting our connection with our SQL query and merging them together
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            //Placing our relevant data/properties instead the question marks in the SQL statement above
            preparedStatement.setString(1, companyName);
            //Executing our query on SQL - Retrieving data from our database and storing it on a ResultSet variable
            ResultSet result = preparedStatement.executeQuery();
            //Checking if our variable is not empty/null
            if (!result.next()) {
                return null;
            }
            //Getting our result from our database translated to our Company object
            return ObjectExtractionUtil.resultToCompany(result);
        } catch (SQLException | InterruptedException e) {
            System.err.println(e);
            throw new CrudException(EntityType.COMPANY,CrudOperation.READ);
        } finally {
            //Returning the chosen connection to the connections stack
            connectionPool.returnConnection(connection);
        }
    }
}
