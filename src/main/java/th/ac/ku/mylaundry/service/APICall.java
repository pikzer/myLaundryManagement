package th.ac.ku.mylaundry.service;


import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class APICall {
    public static final String baseURL = "http://localhost/api/" ;
    public static String token ;
    public static void get(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0L2FwaS9hdXRoL2xvZ2luIiwiaWF0IjoxNjY2MjU2NTE3LCJleHAiOjE2NjYyNjAxMTcsIm5iZiI6MTY2NjI1NjUxNywianRpIjoiNlAzZVVMM2ZQc25HblFxaSIsInN1YiI6IjMiLCJwcnYiOiIyM2JkNWM4OTQ5ZjYwMGFkYjM5ZTcwMWM0MDA4NzJkYjdhNTk3NmY3In0.6p9GqHGX5EL0AEOveyRUHHlBwBjwqulsTUYADpOu5NA" ;

        try {
//            HttpResponse<JsonNode> apiResponse = Unirest.get("http://localhost/api/laundry").asJson();
//            DogResponse dogResponse = new Gson().fromJson(apiResponse.getBody().toString(), DogResponse.class);
//            return dogResponse.getMessage();
            URL url = new URL("http://localhost/api/laundry");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            System.out.println(decodeRespond(new InputStreamReader(conn.getInputStream())));

    } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static String decodeRespond(InputStreamReader input){
        try {
            BufferedReader in = new BufferedReader(input) ;
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void login(String email, String pwd) throws IOException {
        var urlParameters = "email="+email+"&"+"password="+pwd;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(baseURL+"auth/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
            token = jsonObject.getString("access_token");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

