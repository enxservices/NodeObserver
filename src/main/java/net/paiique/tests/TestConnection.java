package net.paiique.tests;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;


public class TestConnection {
    public ArrayList<Object> node(String fqdn) {
        ArrayList<Object> resp = new ArrayList<>();
        try {

            HttpsURLConnection conn = (HttpsURLConnection) new URL("https://" + fqdn + ":8080").openConnection();
            conn.setConnectTimeout(30000);
            conn.connect();

            for (Certificate cert : conn.getServerCertificates()) {
                X509Certificate x509Cert = (X509Certificate) cert;

                if (x509Cert.getSubjectX500Principal().getName().equals("CN=" + fqdn)) {

                    Date currentDate = new Date(System.currentTimeMillis());
                    Date certDate = x509Cert.getNotAfter();

                    if (currentDate.getTime() >= certDate.getTime()) {
                        resp.add("certificateExpired");
                        resp.add(certDate);
                        resp.add(fqdn);
                    } else {
                        resp.add("online");
                        resp.add(certDate.getTime());
                    }
                }
            }
        } catch (IOException e) {
            resp.add(e.getClass().getSimpleName());
        }
        return resp;
    }
}
