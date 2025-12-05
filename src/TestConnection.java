import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String url  = "jdbc:postgresql://localhost:5432/bankingsystem?currentSchema=banking";
        String user = "postgres";
        String password = "";  // empty for Postgres.app

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL successfully!");
            System.out.println("URL: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}
