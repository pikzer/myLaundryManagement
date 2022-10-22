package th.ac.ku.mylaundry.service;

import org.json.JSONArray;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.model.MemberPackage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static th.ac.ku.mylaundry.service.ApiUtil.decodeRespond;

public class MemberPackageDataSource extends ApiCall {

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
}
