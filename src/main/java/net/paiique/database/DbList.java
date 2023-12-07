package net.paiique.database;

import net.paiique.slack.SlackWebhook;

import java.sql.*;

import static net.paiique.Main.conDb;

public class DbList {
    public static void create(String nodeFqdn, int nodeID, Date certExpiration) {
        try {
            if (!checkNode(nodeFqdn)) {
                PreparedStatement prepStmt = conDb.prepareStatement("insert into nodes values (?, ?, ?, ?, ?)");
                prepStmt.setString(1, nodeFqdn);
                prepStmt.setInt(2, nodeID);
                prepStmt.setDate(3, new Date(System.currentTimeMillis()));
                prepStmt.setBoolean(4, false);
                prepStmt.setDate(5, certExpiration);
                prepStmt.execute();
                SlackWebhook.send("O novo node \"" + nodeFqdn + "\" foi adicionado a database!");
            }
        } catch (SQLException e) {
            SlackWebhook.send("Ocorreu um erro ao adicionar o node \"" + nodeFqdn + "\" a database!");
            e.printStackTrace();
        }
    }

    private static boolean checkNode(String nodeFqdn) {
        try {
            PreparedStatement prepStmt = conDb.prepareStatement("select fqdn from nodes");
            ResultSet rs = prepStmt.executeQuery();

            while (rs.next()) {
                if (nodeFqdn.equals(rs.getString(1))) return true;
            }

        } catch (SQLException e) {
            SlackWebhook.send("Ocorreu um erro ao verificar o node \"" + nodeFqdn + "\" na database!");
            e.printStackTrace();
        }
        return false;
    }

    public static void updCert(String nodeFqdn, Date certExpiration) {
        try {
            if (isCertRenewed(certExpiration, nodeFqdn)) {
                PreparedStatement prepStmt = conDb.prepareStatement("update nodes set cert_expiration = ? where fqdn = ?");
                prepStmt.setDate(1, certExpiration);
                prepStmt.setString(2, nodeFqdn);
                prepStmt.execute();
                System.out.println("Certificate for node \"" + nodeFqdn + "\" has been renewed.");
                SlackWebhook.send("O certificado do node \"" + nodeFqdn + "\" foi renovado.");
            }
        } catch (SQLException e) {
            SlackWebhook.send("Ocorreu um erro ao atualizar o certificado do node \"" + nodeFqdn + "\" na database!");
            e.printStackTrace();
        }
    }

    private static boolean isCertRenewed(Date certExpiration, String nodeFqdn) {
        try {
            PreparedStatement prepStmt = conDb.prepareStatement("select cert_expiration from nodes where fqdn = ?");
            prepStmt.setString(1, nodeFqdn);
            ResultSet rs = prepStmt.executeQuery();

            while (rs.next()) if (certExpiration.getTime() < rs.getDate(1).getTime()) return true;

        } catch (SQLException e) {
            SlackWebhook.send("Ocorreu um erro ao verificar a renovação do certificado do node \"" + nodeFqdn + "\" na database!");
            e.printStackTrace();
        }
        return false;
    }

}
