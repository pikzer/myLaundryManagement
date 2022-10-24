package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Category;
import th.ac.ku.mylaundry.model.Laundry;
import th.ac.ku.mylaundry.model.ServiceRate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
                    jsonObject.getString("closetime"),jsonObject.getInt("numOfWork"),jsonObject.getString("status"));

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
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

}
