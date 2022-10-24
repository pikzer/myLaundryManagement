package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Category;
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

public class ServiceRateApiDataSource extends  ApiCall {

    public static ArrayList<ServiceRate> getServiceRate(){
        ArrayList<ServiceRate> serviceRates = new ArrayList<>() ;
        try {
            URL url = new URL(baseURL+"service-rate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONArray jsonArray = new JSONArray(j) ;
            for(int i = 0 ; i < jsonArray.length();i++){
                serviceRates.add(new ServiceRate(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("service"),
                        jsonArray.getJSONObject(i).getDouble("basePrice")
                ));
            }
            return serviceRates ;
        } catch (Exception e) {
            System.out.println(e);
        }
        return serviceRates ;
    }

    public static ArrayList<Category> getCategory(){
        ArrayList<Category> serviceRates = new ArrayList<>() ;
        try {
            URL url = new URL(baseURL+"category");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONArray jsonArray = new JSONArray(j) ;
            for(int i = 0 ; i < jsonArray.length();i++){
                serviceRates.add(new Category(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getInt("service_rate_id"),
                        jsonArray.getJSONObject(i).getString("clothType"),
                        jsonArray.getJSONObject(i).getDouble("addOnPrice")
                ));
            }
            return serviceRates ;
        } catch (Exception e) {
            System.out.println(e);
        }
        return serviceRates ;
    }

    public static boolean deleteCategory(int id) throws IOException {
        // Delete
        try{
            URL url = new URL(baseURL+"category/"+id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("DELETE");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            System.out.println(j);
            return true;
        } catch (IOException | JSONException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean addNewCategory(Category category) throws IOException {
        try {
            var urlParameters = "service_rate_id="+category.getService_rate_id()+"&"+"clothType="+category.getClothType()+"&"
                    + "addOnPrice="+category.getAddOnPrice() ;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL+"category");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
            System.out.println(jsonObject);
            return true ;
        } catch (IOException | JSONException e) {
            System.out.println(e);
            return false ;
        }
    }

    public static boolean patchServiceRate(int id, double basePrice){
        String serviceType[] = {"ซักอบ","ซักรีด","ซักแห้ง","รีด"} ;
        var urlParameters = "service="+serviceType[id-1]+"&"+"basePrice="+basePrice;
        System.out.println(urlParameters);
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(baseURL+"service-rate"+"/"+id);
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
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
