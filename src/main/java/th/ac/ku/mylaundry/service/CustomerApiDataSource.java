package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Customer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static th.ac.ku.mylaundry.service.ApiUtil.decodeRespond;

public class CustomerApiDataSource extends ApiCall {

    public static ArrayList<Customer> getCustomers() throws IOException {
        try {
            URL url = new URL(baseURL+"customers");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            j = j.substring(8,j.length()-1);
//            System.out.println(j);
            ArrayList<Customer> customerShows = new ArrayList<>() ;
            JSONArray jsonArray = new JSONArray(j) ;
//            System.out.println(jsonArray.getJSONObject(1));
            for(int i = 0 ; i < jsonArray.length();i++){
//                jsonArray.getJSONObject(i).getInt("id") ;
                customerShows.add(new Customer(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).getString("phone"),
                        jsonArray.getJSONObject(i).getString("email"),
                        jsonArray.getJSONObject(i).getInt("isMembership"),
                        jsonArray.getJSONObject(i).getString("memService"),
                        jsonArray.getJSONObject(i).getInt("memCredit")
                        ));
            }
//            customerShows.toString();
            return customerShows ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addNewCustomer(String name, String phone){
        var urlParameters = "name="+name+"&"+"phone="+phone;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(baseURL+"customers");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
            System.out.println(jsonObject);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean updateCustomer(){
        return false;
    }

    public static boolean addMembership(String phone){
        return false ;
    }

}
