package Conecxao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conecxao {
    private static final String url = "jdbc:mysql://localhost:3306/aulajava";  // URL do banco de dados
    private static final String user = "root";  // Usuário do banco de dados
    private static final String password = "Dinossauro13";  // Substitua pela sua senha real

    private static Connection conn;

    // Tornar o método público para que outras classes possam acessá-lo
    public static Connection getConecxao() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Conexão com o banco de dados estabelecida.");
            }
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}