package sistemacompras.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String URL = "jdbc:sqlite:database.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "description TEXT NOT NULL," +
                    "code TEXT NOT NULL," +
                    "brand TEXT," +
                    "status TEXT NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "suppliers_prices TEXT NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS company (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "cnpj TEXT NOT NULL," +
                    "buyer_name TEXT NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS suppliers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "cnpj TEXT NOT NULL," +
                    "seller_name TEXT NOT NULL)");
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
        }
    }
}