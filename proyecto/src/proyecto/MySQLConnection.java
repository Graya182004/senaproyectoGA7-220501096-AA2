/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    // URL de conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/proyecto";
    // Usuario y contraseña de la base de datos
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Registrar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Conexión exitosa a la base de datos!");

        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}