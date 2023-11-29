package net.paiique.database;
import java.sql.*;

import static net.paiique.util.Env.*;

public class InitDbConnect {
            private String jdbc = "jdbc:mysql://"+ DB_HOST.get() +":"+ DB_PORT.get() +"/" + DB_NAME.get();
            private Connection con;
    public InitDbConnect() {
        System.out.println(jdbc);
        System.out.println("🛜 | Connecting to database " + DB_NAME.get() + " on " + DB_HOST.get() + ":" + DB_PORT.get());
        try {
            con = DriverManager.getConnection(jdbc, DB_USER.get(), DB_PASS.get());
        } catch (SQLException e) {
            System.out.println("❌ | Failed to connect to database " + DB_NAME.get() + " on " + DB_HOST.get() + ":" + DB_PORT.get());
            throw new RuntimeException(e);
        }
        System.out.println("✔️ | Connected to database successfully!");
    }

    public Connection getCon() {
        return con;
    }
}
