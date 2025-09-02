package com.seguimiento.config;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Conexion {

    private static DataSource dataSource;

    static {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/seguimientopagoDS");
        } catch (NamingException e) {
            System.err.println("Error al inicializar el DataSource: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConexion() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource no inicializado. Verifica el context.xml.");
        }
        return dataSource.getConnection();
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando la conexión: " + e.getMessage());
            }
        }
    }
}
