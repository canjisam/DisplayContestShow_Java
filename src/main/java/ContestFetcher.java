import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ContestFetcher {

    public static void main(String[] args) {
        String apiUrl = "https://algcontest.rainng.com/contests/acm";
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
                connection.disconnect();

                return response.toString();
            }

            @Override
            protected void done() {
                try {
                    String jsonResponse = get();
                    JSONArray contests = new JSONArray(jsonResponse);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < contests.length(); i++) {
                        JSONObject contest = contests.getJSONObject(i);
                        String name = contest.getString("name");
                        String startTime = contest.getString("startTime");
                        String endTime = contest.getString("endTime");
                        String link = contest.getString("link");
                        sb.append("Name: ").append(name).append("\n")
                          .append("Start Time: ").append(startTime).append("\n")
                          .append("End Time: ").append(endTime).append("\n")
                          .append("Link: ").append(link).append("\n")
                          .append("\n");
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage());
                }
            }
        }.execute();
    }
}