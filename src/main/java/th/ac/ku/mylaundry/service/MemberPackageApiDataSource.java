package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Category;
import th.ac.ku.mylaundry.model.MemberPackage;

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

public class MemberPackageApiDataSource extends ApiCall {

    public static ArrayList<MemberPackage> getMemberPackage() throws IOException {
        try {
            URL url = new URL(baseURL+"member-package");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
//            j = j.substring(8,j.length()-1);
//            System.out.println(j);
            ArrayList<MemberPackage> memberPackages = new ArrayList<>() ;
            JSONArray jsonArray = new JSONArray(j) ;
//            System.out.println(jsonArray.getJSONObject(1));
            for(int i = 0 ; i < jsonArray.length();i++){
//                jsonArray.getJSONObject(i).getInt("id") ;
                memberPackages.add(new MemberPackage(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("service"),
                        jsonArray.getJSONObject(i).getInt("quantity"),
                        jsonArray.getJSONObject(i).getDouble("price")
                ));
            }
            return memberPackages ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addMemberPackage(String service, int quantity, double price) throws IOException {
        try {
            var urlParameters = "service="+service+"&"+"quantity="+quantity+"&"
                    + "price="+price ;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL+"member-package");
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

    public static boolean patchMemberPackage(int id, String service, int quantity, double price){
        var urlParameters = "service="+service+"&"+"quantity="+quantity+"&"+"price="+price;
        System.out.println(urlParameters);
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(baseURL+"member-package"+"/"+id);
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

    public static boolean deleteMemberPackage(int id) throws IOException {
        // Delete
        try{
            URL url = new URL(baseURL+"member-package/"+id);
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




}
