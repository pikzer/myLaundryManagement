package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Cloth;
import th.ac.ku.mylaundry.model.ClothList;
import th.ac.ku.mylaundry.model.Order;
import th.ac.ku.mylaundry.model.PreviewClothList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

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
                orderArrayList.add(new Order(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("cus_phone"),
                        jsonArray.getJSONObject(i).getString("service"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).get("pick_date").toString(),
                        jsonArray.getJSONObject(i).get("pick_time").toString(),
                        jsonArray.getJSONObject(i).get("deli_date").toString(),
                        jsonArray.getJSONObject(i).get("deli_time").toString(),
                        jsonArray.getJSONObject(i).get("address").toString(),
                        jsonArray.getJSONObject(i).get("responder").toString(),
                        jsonArray.getJSONObject(i).get("deliver").toString(),
                        jsonArray.getJSONObject(i).getInt("pay_status"),
                        jsonArray.getJSONObject(i).getString("pay_method"),
                        jsonArray.getJSONObject(i).getDouble("pick_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("deli_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("total"),
                        jsonArray.getJSONObject(i).getString("status"),
                        jsonArray.getJSONObject(i).getInt("is_membership_or")));
                if(orderArrayList.get(i).getPickDate().equals("null")){
                    orderArrayList.get(i).setPickDate("");
                    orderArrayList.get(i).setPickTime("");
                }
                if(orderArrayList.get(i).getDeliDate().equals("null")){
                    orderArrayList.get(i).setDeliDate("");
                    orderArrayList.get(i).setDeliDate("");
                }

            }
            return orderArrayList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return orderArrayList;
    }


    public static Order getOrder(int id) {
        try {
            Order order;
            URL url = new URL(baseURL + "orders/"+id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
                        order = new Order(jsonObject.getInt("id"),
                        jsonObject.getString("cus_phone"),
                        jsonObject.getString("service"),
                        jsonObject.getString("name"),
                        jsonObject.get("pick_date").toString(),
                        jsonObject.get("pick_time").toString(),
                        jsonObject.get("deli_date").toString(),
                        jsonObject.get("deli_time").toString(),
                        jsonObject.get("address").toString(),
                        jsonObject.get("responder").toString(),
                        jsonObject.get("deliver").toString(),
                        jsonObject.getInt("pay_status"),
                        jsonObject.getString("pay_method"),
                        jsonObject.getDouble("pick_ser_charge"),
                        jsonObject.getDouble("deli_ser_charge"),
                        jsonObject.getDouble("total"),
                        jsonObject.getString("status"),
                        jsonObject.getInt("is_membership_or"));
                        return order;
            } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ArrayList<Order> getReport(String from, String to) throws IOException {
        ArrayList<Order> orderArrayList = new ArrayList<>();
        var urlParameters = "from="+from+"&"+"to="+to ;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        URL url = new URL(baseURL + "orders/getReport");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("User-Agent", "Java client");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (var wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        }
        String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
        JSONArray jsonArray = new JSONArray(j);
        for (int i = 0; i < jsonArray.length(); i++) {
            orderArrayList.add(new Order(jsonArray.getJSONObject(i).getInt("id"),
                    jsonArray.getJSONObject(i).getString("cus_phone"),
                    jsonArray.getJSONObject(i).getString("service"),
                    jsonArray.getJSONObject(i).getString("name"),
                    jsonArray.getJSONObject(i).get("pick_date").toString(),
                    jsonArray.getJSONObject(i).get("pick_time").toString(),
                    jsonArray.getJSONObject(i).get("deli_date").toString(),
                    jsonArray.getJSONObject(i).get("deli_time").toString(),
                    jsonArray.getJSONObject(i).get("address").toString(),
                    jsonArray.getJSONObject(i).get("responder").toString(),
                    jsonArray.getJSONObject(i).get("deliver").toString(),
                    jsonArray.getJSONObject(i).getInt("pay_status"),
                    jsonArray.getJSONObject(i).getString("pay_method"),
                    jsonArray.getJSONObject(i).getDouble("pick_ser_charge"),
                    jsonArray.getJSONObject(i).getDouble("deli_ser_charge"),
                    jsonArray.getJSONObject(i).getDouble("total"),
                    jsonArray.getJSONObject(i).getString("status"),
                    jsonArray.getJSONObject(i).getInt("is_membership_or"),
                    jsonArray.getJSONObject(i).getString("created_at")
                    ));
            if(orderArrayList.get(i).getPickDate().equals("null")){
                orderArrayList.get(i).setPickDate("");
                orderArrayList.get(i).setPickTime("");
            }
            if(orderArrayList.get(i).getDeliDate().equals("null")){
                orderArrayList.get(i).setDeliDate("");
                orderArrayList.get(i).setDeliDate("");
            }
            if(orderArrayList.get(i).getDeliver().equals("null")){
                orderArrayList.get(i).setDeliver("");
            }
        }
        return orderArrayList ;
    }





    public static ArrayList<Order> getWeekOrder(){
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
                orderArrayList.add(new Order(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("cus_phone"),
                        jsonArray.getJSONObject(i).getString("service"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).get("pick_date").toString(),
                        jsonArray.getJSONObject(i).get("pick_time").toString(),
                        jsonArray.getJSONObject(i).get("deli_date").toString(),
                        jsonArray.getJSONObject(i).get("deli_time").toString(),
                        jsonArray.getJSONObject(i).get("address").toString(),
                        jsonArray.getJSONObject(i).get("responder").toString(),
                        jsonArray.getJSONObject(i).get("deliver").toString(),
                        jsonArray.getJSONObject(i).getInt("pay_status"),
                        jsonArray.getJSONObject(i).getString("pay_method"),
                        jsonArray.getJSONObject(i).getDouble("pick_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("deli_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("total"),
                        jsonArray.getJSONObject(i).getString("status"),
                        jsonArray.getJSONObject(i).getInt("is_membership_or")));
                if(orderArrayList.get(i).getPickDate().equals("null")){
                    orderArrayList.get(i).setPickDate("");
                    orderArrayList.get(i).setPickTime("");
                }
                if(orderArrayList.get(i).getDeliDate().equals("null")){
                    orderArrayList.get(i).setDeliDate("");
                    orderArrayList.get(i).setDeliDate("");
                }
            }
            return orderArrayList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return orderArrayList;
    }

    public static ArrayList<Order> getMonthOrder(){
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
                orderArrayList.add(new Order(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("cus_phone"),
                        jsonArray.getJSONObject(i).getString("service"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).get("pick_date").toString(),
                        jsonArray.getJSONObject(i).get("pick_time").toString(),
                        jsonArray.getJSONObject(i).get("deli_date").toString(),
                        jsonArray.getJSONObject(i).get("deli_time").toString(),
                        jsonArray.getJSONObject(i).get("address").toString(),
                        jsonArray.getJSONObject(i).get("responder").toString(),
                        jsonArray.getJSONObject(i).get("deliver").toString(),
                        jsonArray.getJSONObject(i).getInt("pay_status"),
                        jsonArray.getJSONObject(i).getString("pay_method"),
                        jsonArray.getJSONObject(i).getDouble("pick_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("deli_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("total"),
                        jsonArray.getJSONObject(i).getString("status"),
                        jsonArray.getJSONObject(i).getInt("is_membership_or")));
                if(orderArrayList.get(i).getPickDate().equals("null")){
                    orderArrayList.get(i).setPickDate("");
                    orderArrayList.get(i).setPickTime("");
                }
                if(orderArrayList.get(i).getDeliDate().equals("null")){
                    orderArrayList.get(i).setDeliDate("");
                    orderArrayList.get(i).setDeliDate("");
                }
                if(orderArrayList.get(i).getDeliver().equals("null")){
                    orderArrayList.get(i).setDeliver("");
                }
            }
            return orderArrayList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return orderArrayList;
    }

    public static ArrayList<Order> getYearOrder(){
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
                orderArrayList.add(new Order(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("cus_phone"),
                        jsonArray.getJSONObject(i).getString("service"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).get("pick_date").toString(),
                        jsonArray.getJSONObject(i).get("pick_time").toString(),
                        jsonArray.getJSONObject(i).get("deli_date").toString(),
                        jsonArray.getJSONObject(i).get("deli_time").toString(),
                        jsonArray.getJSONObject(i).get("address").toString(),
                        jsonArray.getJSONObject(i).get("responder").toString(),
                        jsonArray.getJSONObject(i).get("deliver").toString(),
                        jsonArray.getJSONObject(i).getInt("pay_status"),
                        jsonArray.getJSONObject(i).getString("pay_method"),
                        jsonArray.getJSONObject(i).getDouble("pick_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("deli_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("total"),
                        jsonArray.getJSONObject(i).getString("status"),
                        jsonArray.getJSONObject(i).getInt("is_membership_or")));
                if(orderArrayList.get(i).getPickDate().equals("null")){
                    orderArrayList.get(i).setPickDate("");
                    orderArrayList.get(i).setPickTime("");
                }
                if(orderArrayList.get(i).getDeliDate().equals("null")){
                    orderArrayList.get(i).setDeliDate("");
                    orderArrayList.get(i).setDeliDate("");
                }
                if(orderArrayList.get(i).getDeliver().equals("null")){
                    orderArrayList.get(i).setDeliver("");
                }
            }
            return orderArrayList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return orderArrayList;
    }

    public static ArrayList<ClothList> getOrderClothList(int id) throws IOException {
        try {
            ArrayList<ClothList> clothLists = new ArrayList<>();
            URL url = new URL(baseURL + "orders"+"/"+id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
            JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("cloth_lists").toString());
            for(int i = 0 ; i < jsonArray.length() ; i++){
                clothLists.add(new ClothList(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("service"),
                        jsonArray.getJSONObject(i).getString("category"),
                        jsonArray.getJSONObject(i).getInt("quantity")));
            }
            return clothLists;
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }



    public static Integer addOrderWithNoDeli(String phone, Order order, ArrayList<ClothList> clothLists) {
        try {
            // TEMP
            var urlParameters = "service="+order.getService()+"&"+"pay_method="+order.getPayMethod()
                    +"&"+"status="+order.getStatus()+"&"+"is_membership_or="+order.getMemOrder()+"&"+"cus_phone="+phone
                    +"&"+"responder="+getEmployeeName()+"&"+"pay_status="+order.getPayStatus();

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
            return id;
        } catch (IOException e) {
            System.out.println(e);
            return 0;
        }
    }

    public static String getEmployeeName() throws IOException {
        URL url = new URL(baseURL+"auth/me");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization","Bearer "+ token);
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestMethod("POST");
        String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
//        System.out.println(j);
        JSONObject jsonObject = new JSONObject(j);
        return jsonObject.getString("name");
    }

    public static Integer addOrderWithDeli(String phone, Order order, ArrayList<ClothList> clothLists) {
        try {

            // TEMP
            var urlParameters = "service="+order.getService()+"&"+"pay_method="+order.getPayMethod()
                    +"&"+"status="+order.getStatus()+"&"+"is_membership_or="+order.getMemOrder()+"&"+"cus_phone="+phone
                    +"&"+"responder="+getEmployeeName()+"&"+"deli_date="+order.getDeliDate()+"&"+"deli_time="+order.getDeliTime()
                    +"&"+"address="+order.getAddress()+"&"+"pay_status="+order.getPayStatus();

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
            return id;
        } catch (IOException e) {
            System.out.println(e);
            return 0;
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

    public static boolean payMoney(int id){
        try {
            URL url = new URL(baseURL + "orders"+"/"+id+"/"+"payStatus");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("PUT");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            return true ;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean cancelOrder(int id){
        try {
            URL url = new URL(baseURL + "orders"+"/"+id+"/"+"cancelOrder");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("PUT");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            return true ;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean updateOrderStatus(int id,String status){
        try {
            var urlParameters = "status="+status ;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL + "orders"+"/"+id+"/"+"updateStatus");
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
            return true ;
        } catch (Exception e) {
            System.out.println(e);
            return false;
//            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Order> getTodayOrder(){
        ArrayList<Order> orderArrayList = new ArrayList<>();
        try {
            URL url = new URL(baseURL + "orders"+"/getTodayOrder");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONArray jsonArray = new JSONArray(j);
            for (int i = 0; i < jsonArray.length(); i++) {
                orderArrayList.add(new Order(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("cus_phone"),
                        jsonArray.getJSONObject(i).getString("service"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).get("pick_date").toString(),
                        jsonArray.getJSONObject(i).get("pick_time").toString(),
                        jsonArray.getJSONObject(i).get("deli_date").toString(),
                        jsonArray.getJSONObject(i).get("deli_time").toString(),
                        jsonArray.getJSONObject(i).get("address").toString(),
                        jsonArray.getJSONObject(i).get("responder").toString(),
                        jsonArray.getJSONObject(i).get("deliver").toString(),
                        jsonArray.getJSONObject(i).getInt("pay_status"),
                        jsonArray.getJSONObject(i).getString("pay_method"),
                        jsonArray.getJSONObject(i).getDouble("pick_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("deli_ser_charge"),
                        jsonArray.getJSONObject(i).getDouble("total"),
                        jsonArray.getJSONObject(i).getString("status"),
                        jsonArray.getJSONObject(i).getInt("is_membership_or")));
                if(orderArrayList.get(i).getPickDate().equals("null")){
                    orderArrayList.get(i).setPickDate("");
                    orderArrayList.get(i).setPickTime("");
                }
                if(orderArrayList.get(i).getDeliDate().equals("null")){
                    orderArrayList.get(i).setDeliDate("");
                    orderArrayList.get(i).setDeliDate("");
                }
                if(orderArrayList.get(i).getDeliver().equals("null")){
                    orderArrayList.get(i).setDeliver("");
                }
            }
            return orderArrayList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return orderArrayList;
    }

    public static boolean payMember(int cusId,int quantity) throws IOException {
        try {
            var urlParameters = "pay="+quantity ;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL+"customers"+"/"+cusId+"/"+"payMember");
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

    public static PreviewClothList getPreviewClothList(int cl_id){
        try {
            var urlParameters = "cl_id="+cl_id;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL + "orders/getPreviewClothList");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
            PreviewClothList previewClothList = new PreviewClothList(jsonObject.getString("service"),
                    jsonObject.getString("clothType"),
                    jsonObject.getInt("quantity"),
                    jsonObject.getDouble("pricePerU"),
                    jsonObject.getDouble("amount")
                    );
            return previewClothList ;

        } catch (IOException e) {
            System.out.println(e);
        }
        return null ;
    }

    public static boolean confirmOrder(int id){
        try {
            URL url = new URL(baseURL + "orders"+"/"+id+"/"+"acceptOrderForEmployee");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("PUT");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addClothListApp(Order order, ArrayList<ClothList> clothLists,String date, String time) throws IOException {
        if(addClothList(order.getId(),clothLists)){
            updateOrderStatus(order.getId(),"เพิ่มรายการ");
            if(order.getIsMemOrder()==0){
                URL url = new URL(baseURL + "orders"+"/"+order.getId()+"/"+"calDeliApp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "Bearer " + token);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("PUT");
                String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
                updateAppOrder(order.getId(),date,time);
                return true;
            }
            else{
                updateAppOrder(order.getId(),date,time);
                return true;
            }
        }
        else{
            return  false ;
        }
    }

    public static void updateAppOrder(int id,String date, String time) throws IOException {
        var urlParameters = "deli_date="+date+"&"+"deli_time="+time;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        URL url = new URL(baseURL+"orders"+"/"+id+"/"+"updateAppOrder");
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
    }
}