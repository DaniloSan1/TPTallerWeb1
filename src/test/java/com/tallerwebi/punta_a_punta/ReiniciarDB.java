package com.tallerwebi.punta_a_punta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ReiniciarDB {
    public static void limpiarBaseDeDatos() {
        try {
            String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
            String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
            String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "tallerwebi";
            String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "user";
            String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "user";

            String url = String.format("jdbc:mysql://%s:%s/%s", dbHost, dbPort, dbName);
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM Usuario WHERE email = 'e2e@unlam.edu.ar';");
            stmt.executeUpdate(
                    "DELETE FROM EquipoJugador WHERE usuario_id = 1 AND equipo_id IN (SELECT equipo_id FROM PartidoEquipo WHERE partido_id = 2);");
            conn.close();
            System.out.println("Base de datos limpiada exitosamente");

        } catch (SQLException e) {
            System.err.println("Error al limpiar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
