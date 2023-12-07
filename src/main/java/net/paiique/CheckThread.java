package net.paiique;

import net.paiique.database.DbList;
import net.paiique.database.DbLog;
import net.paiique.pterodactyl.PterodactylAPI;
import net.paiique.slack.SlackWebhook;
import net.paiique.tests.TestConnection;
import net.paiique.util.Env;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

public class CheckThread extends Thread {

    public CheckThread() {
        SlackWebhook.send("Starting NodeObserver.");
    }

    @Override
    public void run() {
        SlackWebhook.send("Watcher running at " + this.getName() + " thread.");
        while (true) {
            ArrayList<JSONObject> pteroResp = PterodactylAPI.nodeStatus();

            int FqdnMaxLen = 0;
            if (pteroResp == null) return;
            for (JSONObject node : pteroResp) {
                if (node.getString("fqdn").length() > FqdnMaxLen) {
                    FqdnMaxLen = node.getString("fqdn").length();
                }
            }

            for (JSONObject node : pteroResp) {

                boolean maintenance = node.getBoolean("maintenance_mode");
                String fqdn = node.getString("fqdn");
                int id = node.getInt("id");
                String verifyMsg = "🔎 | Checking node \"" + fqdn + "\"";
                System.out.print(verifyMsg);


                if (fqdn.length() < FqdnMaxLen) {

                    for (int i = 0; i < FqdnMaxLen - fqdn.length(); i++) {

                        System.out.print(" ");

                    }
                }
                System.out.print("|");

                System.out.print(" 🟡 (Checking)");

                ArrayList<Object> sttsList = new TestConnection().node(fqdn);

                System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b");
                System.out.flush();


                if (maintenance) sttsList.set(0, "maintenance");

                if (!sttsList.get(0).equals("online")) {
                    DbLog.log(fqdn, id, sttsList.get(0).toString());
                    System.out.println(" 💔 (" + sttsList.get(0).toString() + ")");
                }

                if (sttsList.get(0).equals("online")) {
                    DbList.create(fqdn, id, new Date((Long) sttsList.get(1)));

                    if (DbLog.check(fqdn)) DbLog.setResolved(fqdn);

                    System.out.println(" 💚 (Alive)  ");
                    DbList.updCert(fqdn, new Date((Long) sttsList.get(1)));
                }


                try {

                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                    System.out.println("Error while sleeping " + this.getName() + ".");

                }
            }

            for (int i = 0; i < Env.TIMEOUT.getInt() / 1000; i++) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                    System.out.println("Error while sleeping" + this.getName() + ".");

                }

                System.out.print("\r\r");
                System.out.flush();
                System.out.print("⏰ | Waiting " + ((Env.TIMEOUT.getInt() / 1000) - i) + " seconds to check again.");

            }

            System.out.print("\r\r");
            System.out.flush();
            System.out.println("🔄 | Checking again.");

        }
    }
}
