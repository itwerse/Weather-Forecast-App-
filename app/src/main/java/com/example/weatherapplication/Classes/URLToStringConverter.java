package com.example.weatherapplication.Classes;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLToStringConverter {
    public static void main(String args) {
        try {
            // Specify the URL
            String urlString = "https://example.com/api/data";

            // Create a URL object
            URL url = new URL(urlString);

            // Open a connection to the URL
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            // Read the response and convert it to a string
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Convert the response to a string
            String jsonString = response.toString();

            // Print the string representation of the response
            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
