package th.ac.ku.mylaundry.service;


import org.json.*;
import th.ac.ku.mylaundry.model.Employee;
import th.ac.ku.mylaundry.model.Laundry;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static th.ac.ku.mylaundry.service.ApiUtil.decodeRespond;

public class ApiCall {
    public static final String baseURL = "http://localhost/api/" ;
    public static String token ;
    
    public static String role = null;


    public static String getRole() throws IOException {
        URL url = new URL(baseURL+"auth/me");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization","Bearer "+ token);
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestMethod("POST");
        String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
//        System.out.println(j);
        JSONObject jsonObject = new JSONObject(j);
        return jsonObject.getString("realrole");
    }

    public static int login(String email, String pwd) throws IOException {
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
            if(getRole().equals("CUSTOMER")){
                return 2;
            }
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean logout(){
        try{
            URL url = new URL(baseURL+"auth/logout");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("POST");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            System.out.println(j);
            JSONObject jsonObject = new JSONObject(j);
            delTempFile();
            return jsonObject.getString("message").equals("Successfully logged out");
        } catch (Exception e) {
            return false ;
//            throw new RuntimeException(e);
        }
    }


    public static void postLaundry(Laundry laundry, Employee employee) throws IOException {
        try{
            // Laundry Section
            var urlParameters = laundry.getPostLaundry();
            urlParameters += "&";
            //  OwnerSection
            urlParameters += employee.getPostEmployee();
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL+"laundry");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            System.out.println(j);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> getDashboardData() throws IOException {
        ArrayList<String> s = new ArrayList<>();
        DecimalFormat f = new DecimalFormat("#0.00");
        URL url = new URL(baseURL + "orders"+"/getDashboardData");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("GET");
        String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
        JSONObject jsonObject = new JSONObject(j);
        s.add(String.valueOf(jsonObject.getInt("inprogress")));
        s.add(String.valueOf(jsonObject.getInt("completeOrder")));
        s.add(String.valueOf(jsonObject.getInt("notPay")));
        s.add(f.format(jsonObject.getDouble("income")));
        s.add(String.valueOf(jsonObject.getInt("numOfCus")));
        s.add(String.valueOf(jsonObject.getInt("numOfMem")));
            return s;
    }

    public static void delTempFile(){
        new File("inv").delete();
        new File("receipt").delete();
        // on del report
        new File("report").delete();
    }



}

