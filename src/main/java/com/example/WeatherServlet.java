package com.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
eimport java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {

    private static final String API_KEY = "your_api_key_here";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String city = request.getParameter("city");
        String urlString = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();
            if (status == 200) {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder inline = new StringBuilder();
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();

                JsonObject jsonObject = JsonParser.parseString(inline.toString()).getAsJsonObject();
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonObject);
                out.flush();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
