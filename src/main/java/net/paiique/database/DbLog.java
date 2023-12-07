package net.paiique.database;

import net.paiique.slack.SlackWebhook;

import java.sql.*;

import static net.paiique.Main.conDb;

public class DbLog {

    public static void log(String nodeFqdn, int nodeID, String reason) {
        if (check(nodeFqdn)) return;
        try {
            PreparedStatement prepStmt = conDb.prepareStatement("insert into log_nodes values (?, ?, ?, ?, ?, ?)");
            prepStmt.setInt(1, nodeID);
            prepStmt.setString(2, nodeFqdn);
            prepStmt.setString(3, reason);
            prepStmt.setDate(4, new Date(System.currentTimeMillis()));
            prepStmt.setTime(5, new Time(System.currentTimeMillis()));
            prepStmt.setBoolean(6, false);
            prepStmt.execute();
            SlackWebhook.send("O node \"" + nodeFqdn + "\" está offline. CodMotivo: \"" + reason + "\"");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void setResolved(String nodeFqdn) {
        try {
            PreparedStatement prepStmt = conDb.prepareStatement("update log_nodes set resolved = ? where fqdn = ? and resolved = ?");
            prepStmt.setBoolean(1, true);
            prepStmt.setString(2, nodeFqdn);
            prepStmt.setBoolean(3, false);
            prepStmt.execute();
            SlackWebhook.send("O node \"" + nodeFqdn + "\" está online novamente!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean check(String nodeFqdn) {
        try {
            PreparedStatement prepStmt = conDb.prepareStatement("select fqdn from log_nodes where resolved = ?");
            prepStmt.setBoolean(1, false);
            ResultSet rs = prepStmt.executeQuery();

            while (rs.next()) if (nodeFqdn.equals(rs.getString(1))) return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
