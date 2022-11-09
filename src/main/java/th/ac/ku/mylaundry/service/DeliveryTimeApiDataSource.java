package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.DeliveryTime;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static th.ac.ku.mylaundry.service.ApiUtil.decodeRespond;

public class DeliveryTimeApiDataSource extends ApiCall{
    public static ArrayList<DeliveryTime> getDeliveryTime(){
        ArrayList<DeliveryTime> deliveryTimes = new ArrayList<>() ;
        try {
            URL url = new URL(baseURL+"delivery-time");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONArray jsonArray = new JSONArray(j) ;
            for(int i = 0 ; i < jsonArray.length();i++){
                deliveryTimes.add(new DeliveryTime(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("date"),
                        jsonArray.getJSONObject(i).getString("time"),
                        jsonArray.getJSONObject(i).getString("orderName"),
                        jsonArray.getJSONObject(i).getString("deliver"),
                        jsonArray.getJSONObject(i).getString("job")
                        ));
            }
            return deliveryTimes ;
        } catch (Exception e) {
            System.out.println(e);
        }
        return deliveryTimes ;
    }

    public static ArrayList<Boolean> getAvailableInDateTime(String date){
        ArrayList<Boolean> avTime = new ArrayList<>();
        try {
            URL url = new URL(baseURL + "delivery-time/getAvailableInDateTime" );
            var urlParameters = "deli_date="+date;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("PUT");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
            avTime.add(jsonObject.getBoolean("morning"));
            avTime.add(jsonObject.getBoolean("after"));
            avTime.add(jsonObject.getBoolean("even"));
            return avTime;
        } catch (Exception e) {
            System.out.println(e);
        }
        return avTime;
    }

    public static boolean addDeliveryTime(DeliveryTime deliveryTime){
        try {
            var urlParameters = "date="+deliveryTime.getDate()+"&"+"time="+deliveryTime.getTime()+"&"
                    + "orderName="+deliveryTime.getOrderName()+"&"+"job="+deliveryTime.getJob();
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL+"delivery-time");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            System.out.println(j);
            JSONObject jsonObject = new JSONObject(j);
            return true ;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean cancelDelivery(int id){
        try {
            URL url = new URL(baseURL + "delivery-time"+"/"+id+"/"+"cancelDelivery");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("PUT");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }


    public static boolean addDeliver (int id, String deliver,String orderName) throws IOException {
        try {
            var urlParameters = "deliver="+deliver +"&"+"orderName="+orderName;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL+"delivery-time"+"/"+id+"/"+"addDeliver");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("PUT");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            System.out.println(j);
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false ;
        }
    }


}
