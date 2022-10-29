package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Cloth;
import th.ac.ku.mylaundry.model.ClothList;
import th.ac.ku.mylaundry.model.Order;

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

public class OrderApiDataSource extends ApiCall {
    public static ArrayList<Order> getOrderList() {
        ArrayList<Order> orderArrayList = new ArrayList<>();
        try {
            URL url = new URL(baseURL + "orders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONArray jsonArray = new JSONArray(j);
            for (int i = 0; i < jsonArray.length(); i++) {
                // TODO
//                orderArrayList.add(new ServiceRate(jsonArray.getJSONObject(i).getInt("id"),
//                        jsonArray.getJSONObject(i).getString("service"),
//                        jsonArray.getJSONObject(i).getDouble("basePrice")
//                ));
            }
            return orderArrayList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return orderArrayList;
    }

    public static boolean addOrderWithNoDeli(String phone, Order order, ArrayList<ClothList> clothLists) {
        try {

            // TEMP
            var urlParameters = "service="+order.getService()+"&"+"pay_method="+order.getPayMethod()
                    +"&"+"status="+order.getStatus()+"&"+"is_membership_or="+order.getMemOrder()+"&"+"cus_phone="+phone
                    +"&"+"responder="+"employee 1";

            // REAL-
//            var urlParameters = "service="+order.getService()+"&"+"pay_method="+order.getPayMethod()
//                    +"&"+"status="+order.getStatus()+"&"+"is_membership_or="+order.getMemOrder()+"&"+"cus_phone="+phone;

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL + "orders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            System.out.println(j);
            JSONObject jsonObject = new JSONObject(j);
            Integer id = jsonObject.getInt("order_id");
            addClothList(id,clothLists);
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addOrderWithDeli(String phone, Order order, ArrayList<ClothList> clothLists) {
        try {

            // TEMP
            var urlParameters = "service="+order.getService()+"&"+"pay_method="+order.getPayMethod()
                    +"&"+"status="+order.getStatus()+"&"+"is_membership_or="+order.getMemOrder()+"&"+"cus_phone="+phone
                    +"&"+"responder="+"employee 1"+"&"+"deliDate="+order.getDeliDate()+"&"+"deliTime="+order.getDeliTime()
                    +"&"+"address="+order.getAddress();

            // REAL-
//            var urlParameters = "service="+order.getService()+"&"+"pay_method="+order.getPayMethod()
//                    +"&"+"status="+order.getStatus()+"&"+"is_membership_or="+order.getMemOrder()+"&"+"cus_phone="+phone;

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL + "orders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            System.out.println(j);
            JSONObject jsonObject = new JSONObject(j);
            Integer id = jsonObject.getInt("order_id");
            addClothList(id,clothLists);
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addClothList(int id, ArrayList<ClothList> clothLists){
        try{
            URL url = new URL(baseURL + "orders"+"/"+id+"/"+"clothList");
            for (ClothList cl:clothLists) {
                var urlParameters = "service="+cl.getService()+"&"+"category="+cl.getCategory()+"&"+"quantity="+cl.getQuantity();
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("User-Agent", "Java client");
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                try (var wr = new DataOutputStream(conn.getOutputStream())) {
                    wr.write(postData);
                }
                String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            }
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    public static ArrayList<Cloth> getCloth(Integer serviceId) {
        ArrayList<Cloth> cloths = new ArrayList<>() ;
        try {
            URL url = new URL(baseURL + "service-rate"+"/"+serviceId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j) ;
            JSONArray jsonArray = new JSONArray(jsonObject.get("category").toString());
            for(int i = 0 ; i < jsonArray.length() ; i++){
                cloths.add(new Cloth(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("clothType"),
                        jsonArray.getJSONObject(i).getDouble("addOnPrice")
                                + jsonObject.getDouble("basePrice"))
                );
            }
            return cloths;
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void payMoney(int id){

    }

    public static void updateOrderStatus(int id){
        try {
            URL url = new URL(baseURL + "orders"+"/"+id+"/"+"nextStatus");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
