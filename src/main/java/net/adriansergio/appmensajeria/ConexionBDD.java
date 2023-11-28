package net.adriansergio.appmensajeria;

import java.sql.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
public class ConexionBDD {
    String url = "jdbc:postgresql://localhost:5432/p2p";
    String usuario = "sergio";
    String contrasena = "sergio";

    public Connection conexion;


    public ConexionBDD() {
        conectar();
    }

    private void conectar() {
        try {
            this.conexion = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("Conexión exitosa");
        } catch (SQLException e) {
            System.out.println("Error en conexión: " + e.getMessage());
        }
    }

    private void desconectar() {
        try {
            this.conexion.close();
            System.out.println("Desconexión exitosa");
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
