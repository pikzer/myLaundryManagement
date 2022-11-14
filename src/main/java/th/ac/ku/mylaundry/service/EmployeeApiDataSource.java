package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import th.ac.ku.mylaundry.model.Employee;

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

public class EmployeeApiDataSource extends ApiCall {
    public static ArrayList<Employee> getEmployees() throws IOException {
        try {
            URL url = new URL(baseURL + "employees");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            j = j.substring(8, j.length() - 1);
            ArrayList<Employee> employeeArrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(j);
            for (int i = 0; i < jsonArray.length(); i++) {
                employeeArrayList.add(new Employee(jsonArray.getJSONObject(i).getInt("id"),
                        jsonArray.getJSONObject(i).getString("name"),
                        jsonArray.getJSONObject(i).getString("phone"),
                        jsonArray.getJSONObject(i).get("email").toString(),
                        jsonArray.getJSONObject(i).getString("role"),
                        jsonArray.getJSONObject(i).getDouble("salary"),
                        jsonArray.getJSONObject(i).getString("address"),
                        jsonArray.getJSONObject(i).getString("ID_Card"),
                        jsonArray.getJSONObject(i).getString("bank_account_number"),
                        jsonArray.getJSONObject(i).getString("bank_name")
                ));
            }
            return employeeArrayList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getOwnerQR(){
        try {
            URL url = new URL(baseURL + "employees/1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            JSONObject jsonObject = new JSONObject(j);
            return jsonObject.getString("phone");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delEmployee(int id){
        try{
            URL url = new URL(baseURL+"employees/"+id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("DELETE");
            String j = decodeRespond(new InputStreamReader(conn.getInputStream()));
            return true;
        } catch (IOException | JSONException e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean addEmployee(Employee employee){
        try {
            var urlParameters = "name="+employee.getName()+"&"+"phone="+employee.getPhone()+"&"
                    + "email="+employee.getEmail()+"&"+"role="+employee.getRole()+"&"+"password="+"12345678"
                    +"&"+"salary="+employee.getSalary()+"&"+"address="+employee.getAddress()+"&"+"ID_Card="+employee.getIdCard()
                    + "&"+"bank_account_number="+employee.getBankAccountNumber()+"&"+"bank_name="+employee.getBankName();
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL+"employees");
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

    public static boolean patchEmployee(Employee employee){
        var urlParameters = "name="+employee.getName()+"&"+"phone="+employee.getPhone()+"&"
                + "email="+employee.getEmail()+"&"+"role="+employee.getRole()
                +"&"+"salary="+employee.getSalary()+"&"+"address="+employee.getAddress()+"&"+"ID_Card="+employee.getIdCard()
                + "&"+"bank_account_number="+employee.getBankAccountNumber()+"&"+"bank_name="+employee.getBankName();
        System.out.println(urlParameters);
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {
            URL url = new URL(baseURL+"employees"+"/"+employee.getId());
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

    public static boolean changePassword(String password,String idCard) throws IOException {
        try{
            var urlParameters = "ID_Card="+idCard+"&"+"password="+password;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            URL url = new URL(baseURL+"employees"+"/"+"changePassword");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestProperty("User-Agent", "Java client");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("PUT");
            try (var wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }
}
