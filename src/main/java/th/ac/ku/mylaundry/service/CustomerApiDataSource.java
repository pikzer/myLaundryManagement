package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Address;
import th.ac.ku.mylaundry.model.Customer;

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
                        jsonArray.getJSONObject(i).get("email").toString(),
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

    public static boolean addNewCustomer(String name, String phone, String ucode){
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

            urlParameters = "u_code="+ucode+"&"+"cus_phone="+phone;
            postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            url = new URL(baseURL+"address");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            j = decodeRespond(new InputStreamReader(conn.getInputStream()));

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    public static boolean updateCustomer(int id,String name, String phone, String email){
        var urlParameters = "name="+name+"&"+"phone="+phone+"email="+email;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(baseURL+"customers"+"/"+id);
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
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    // TODO Add Membership
    public static boolean addMembership(int id,String memService, Integer memCredit) throws IOException {
        var urlParameters = "memService="+memService+"&"+"memCredit="+memCredit;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        URL url = new URL(baseURL+"customers"+"/"+id+"/"+"addMemberService");
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
        return false ;
    }

    public static Customer searchCustomer(String phone){

        try {
            URL url = new URL(baseURL+"customers"+"/"+"search?q="+phone);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
//            JSONObject jsonObject = new JSONObject(j);
//            j = j.substring(8,j.length()-1);
            JSONArray jsonArray = new JSONArray(j) ;
//            System.out.println(jsonArray);
//            System.out.println(jsonArray.getJSONObject(0));
            return new Customer(jsonArray.getJSONObject(0).getInt("id"),
                    jsonArray.getJSONObject(0).getString("name"),
                    jsonArray.getJSONObject(0).getString("phone"),
                    jsonArray.getJSONObject(0).get("email").toString(),
                    jsonArray.getJSONObject(0).getInt("isMembership"),
                    jsonArray.getJSONObject(0).getString("memService"),
                    jsonArray.getJSONObject(0).getInt("memCredit")
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Address getCustomerAddress(int id){
        try {
            URL url = new URL(baseURL+"customers"+"/"+id+"/"+"getCustomerAddress");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            if(j.equals("[]")){
                return null;
            }
            JSONArray jsonArray = new JSONArray(j) ;
            return new Address(jsonArray.getJSONObject(0).getInt("id"),jsonArray.getJSONObject(0).getString("u_code"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAddress(Address address){

    }
}
