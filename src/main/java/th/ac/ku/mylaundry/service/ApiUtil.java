package th.ac.ku.mylaundry.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ApiUtil {

    public static String decodeRespond(InputStreamReader input){
        try {
            BufferedReader in = new BufferedReader(input) ;
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
