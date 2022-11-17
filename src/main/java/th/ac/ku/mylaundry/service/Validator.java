package th.ac.ku.mylaundry.service;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {



    public static boolean isEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPhoneNumber(String phone){
        String regex = "^0[0-9]{9}$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isDoubleAndPositive(String ddouble){
        String regex = "^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$" ;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ddouble);
        if(matcher.matches() && Double.parseDouble(ddouble) > 0){
            return true ;
        }
        else{
            return false ;
        }
    }

}
