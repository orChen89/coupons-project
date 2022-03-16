package com.or.db;

import com.or.constants.Constants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {

    //Setting an instance for this class
    private static ConnectionPool instance = null;
    //Creating a stack of connections
    private final Stack<Connection> connections = new Stack<>();

    //Creating a private constructor and injecting the stack of connections
    private ConnectionPool() throws SQLException {
        connectionPreviewPrint();
        openAllConnections();
    }

    //Getting a thread-safe instance for a connection
    public static ConnectionPool getInstance() throws SQLException {
        //Making double check thread-safe operation
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    //Inserting all connections to the stack of connections
    private void openAllConnections() throws SQLException {
        for (int counter = 0; counter < Constants.NUMBER_OF_CONNECTIONS; counter++) {
            final Connection connection = DriverManager.getConnection(
                    Constants.SQL_URL,
                    Constants.SQL_USER,
                    Constants.SQL_PASS);
            connections.push(connection);
        }
    }

    public void closeAllConnections() throws InterruptedException {
        synchronized (connections) {
            while (connections.size() < Constants.NUMBER_OF_CONNECTIONS) {
                connections.wait();
            }
            connections.removeAllElements();
        }
    }

    //Getting a specific connection from the stack for any desirable operation
    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            while (connections.isEmpty()) {
                connections.wait();
            }
            return connections.pop();
        }
    }

    //Returning one of the connections back to the stack
    public void returnConnection(final Connection connection) {
        synchronized (connections) {
            if (connection == null) {
                return;
            }
            connections.push(connection);
            connections.notify();
        }
    }

    public void connectionPreviewPrint() {

        System.out.println();
        System.out.println("****************************************");
        System.out.println("~~ Created new connection pool ~~");
        System.out.println("****************************************");
        System.out.println();
    }
}