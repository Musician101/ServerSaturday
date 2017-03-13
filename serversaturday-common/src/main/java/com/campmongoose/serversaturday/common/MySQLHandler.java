package com.campmongoose.serversaturday.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Nonnull;

public class MySQLHandler {

    private final String database;
    private final String hostname;
    private final String password;
    private final String port;
    private final String user;
    private Connection connection;

    public MySQLHandler(@Nonnull String database, @Nonnull String hostname, @Nonnull String password, @Nonnull String port, @Nonnull String user) {
        this.connection = null;
        this.database = database;
        this.hostname = hostname;
        this.password = password;
        this.port = port;
        this.user = user;
    }

    private boolean checkConnection() {
        return connection != null;
    }

    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Nonnull
    public Connection getConnection() {
        return connection;
    }

    private Connection openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Drive");
        connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, user, password);
        return connection;
    }

    @Nonnull
    public ResultSet querySQL(@Nonnull String query) throws ClassNotFoundException, SQLException {
        Connection c;
        if (checkConnection()) {
            c = getConnection();
        }
        else {
            c = openConnection();
        }

        Statement s = c.createStatement();
        ResultSet rset = s.executeQuery(query);
        s.close();
        closeConnection();
        return rset;
    }

    public void updateSQL(@Nonnull String update) throws ClassNotFoundException, SQLException {
        Connection c;
        if (checkConnection()) {
            c = getConnection();
        }
        else {
            c = openConnection();
        }

        Statement s = c.createStatement();
        s.executeUpdate(update);
        s.close();
        closeConnection();
    }
}
