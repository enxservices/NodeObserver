package net.paiique.pterodactyl;

import net.paiique.slack.SlackWebhook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static net.paiique.util.Env.PTERO_TOKEN;

public class PterodactylAPI {

    public static ArrayList<JSONObject> nodeStatus() {
        System.out.println("Getting node list from Pterodactyl.");

        JSONObject fromApi = getJson("https://painel.enxadahost.com/api/application/nodes");
        int pages = fromApi.getJSONObject("meta").getJSONObject("pagination").getInt("total_pages");

        ArrayList<JSONObject> nodes = new ArrayList<>();
        for (int i = 1; i <= pages; i++) {
        JSONObject nodeJson = getJson("https://painel.enxadahost.com/api/application/nodes?page=" + i);
        JSONArray data = nodeJson.getJSONArray("data");

            for (int j = 0; j < data.length(); j++) {
                nodes.add(data.getJSONObject(j).getJSONObject("attributes"));
            }
        }
        return nodes;
    }
    public static JSONObject getJson(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + PTERO_TOKEN.get());
            con.setRequestProperty("Cookie", "pterodactyl_session=eyJpdiI6InhIVXp5ZE43WlMxUU1NQ1pyNWRFa1E9PSIsInZhbHVlIjoiQTNpcE9JV3FlcmZ6Ym9vS0dBTmxXMGtST2xyTFJvVEM5NWVWbVFJSnV6S1dwcTVGWHBhZzdjMHpkN0RNdDVkQiIsIm1hYyI6IjAxYTI5NDY1OWMzNDJlZWU2OTc3ZDYxYzIyMzlhZTFiYWY1ZjgwMjAwZjY3MDU4ZDYwMzhjOTRmYjMzNDliN2YifQ%3D%3D");
            con.addRequestProperty("User-Agent", "Node_Observer/4.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String iL;
            StringBuilder resp = new StringBuilder();

            while ((iL = in.readLine()) != null) {
                resp.append(iL);
            }
            in.close();

            con.disconnect();

            return new JSONObject(new JSONTokener(resp.toString()));

        } catch (Exception e) {
            SlackWebhook.send("Ocorreu um erro ao tentar se conectar com a API do Pterodactyl.");
            e.printStackTrace();
        }
        return null;
    }
}
