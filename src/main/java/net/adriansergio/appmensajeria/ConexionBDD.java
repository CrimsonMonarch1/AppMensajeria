package net.adriansergio.appmensajeria;

import java.sql.*;
import java.sql.SQLException;

public class ConexionBDD {
    String url = "jdbc:postgresql://localhost:5432/messageapp_db";
    String usuario = "adrian";
    String contrasena = "Asrieldremurr23";

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
