package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import th.ac.ku.mylaundry.model.Employee;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
}
