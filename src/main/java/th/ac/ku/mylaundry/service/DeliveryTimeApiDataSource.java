package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import th.ac.ku.mylaundry.model.DeliveryTime;
import th.ac.ku.mylaundry.model.ServiceRate;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
}
