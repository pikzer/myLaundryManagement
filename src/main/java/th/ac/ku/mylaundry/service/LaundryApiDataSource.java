package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Category;
import th.ac.ku.mylaundry.model.Employee;
import th.ac.ku.mylaundry.model.Laundry;
import th.ac.ku.mylaundry.model.ServiceRate;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static th.ac.ku.mylaundry.service.ApiUtil.decodeRespond;

public class LaundryApiDataSource extends ApiCall {


    public static Laundry getShop(){
        try {
            URL url = new URL(baseURL+"laundry"+"/1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j) ;
            return new Laundry(jsonObject.getInt("id"),jsonObject.getString("name"),jsonObject.getString("phone"),
                    jsonObject.getString("owner"),jsonObject.getString("email"),jsonObject.getString("address"),
                    jsonObject.getString("lineId"),jsonObject.getString("workDay"),jsonObject.getString("opentime"),
                    jsonObject.getString("closetime"),jsonObject.getInt("numOfWork"),jsonObject.getString("status"),
                    jsonObject.getString("email_pwd")
            );
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static int getStatusShop(){
        try {
            URL url = new URL(baseURL+"laundry"+"/1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonDo = new JSONObject(j) ;

            if(jsonDo.getString("status").equals("close")){
                return 0;
            }
            else if(jsonDo.getString("status").equals("open")){
                return 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 3 ;
    }


    public static void openShop() throws IOException {
        try {
            URL url = new URL(baseURL+"laundry"+"/1/open");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestMethod("PUT");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonDo = new JSONObject(j) ;
            System.out.println(jsonDo);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean patchLaundry(String shopName,String shopPhone, String shopEmail, String shopAddress,
                                       String lineId, String openTime, String closeTime,String workDay,Integer numOfWork,String email_pwd){
//        var urlParameters = laundry.getPostLaundry();
        var urlParameters = "shopName="+shopName+"&"+"shopPhone="+shopPhone
                +"&"+"shopEmail="+shopEmail+"&"+"lineId="+lineId+"&"+"openTime="+openTime
                +"&"+"closeTime="+closeTime+"&"+"numOfWork="+numOfWork +"&"+ "shopAddress="+shopAddress
                + "&" + "workDay=" +workDay
                ;
        if(!email_pwd.isEmpty()){
            urlParameters += "&email_pwd=" +email_pwd;
        }
        System.out.println(urlParameters);
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(baseURL+"laundry"+"/"+1);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
            System.out.println(jsonObject);
            return true ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeShop() throws IOException {
        try {
            URL url = new URL(baseURL+"laundry"+"/1/close");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestMethod("PUT");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonDo = new JSONObject(j) ;
            System.out.println(jsonDo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLaundryName(int id) throws IOException {
        URL url = new URL(baseURL + "laundry/"+id+"/getName");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("GET");
        String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
        JSONObject jsonObject = new JSONObject(j);
        return jsonObject.getString("name");
    }

}
