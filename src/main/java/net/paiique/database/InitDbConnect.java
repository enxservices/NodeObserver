package net.paiique.database;
import java.sql.*;

import static net.paiique.util.Env.*;

public class InitDbConnect {
    private final Connection con;
    public InitDbConnect() {
        String jdbc = "jdbc:mysql://" + DB_HOST.getString() + ":" + DB_PORT.getString() + "/" + DB_NAME.getString();
        System.out.println("🛜 | Connecting to database " + DB_NAME.getString() + " on " + DB_HOST.getString() + ":" + DB_PORT.getString());
        try {
            con = DriverManager.getConnection(jdbc, DB_USER.getString(), DB_PASS.getString());
        } catch (SQLException e) {
            System.out.println("❌ | Failed to connect to database " + DB_NAME.getString() + " on " + DB_HOST.getString() + ":" + DB_PORT.getString());
            throw new RuntimeException(e);
        }
        System.out.println("✔️ | Connected to database successfully!");
    }

    public Connection getCon() {
        return con;
    }
}
