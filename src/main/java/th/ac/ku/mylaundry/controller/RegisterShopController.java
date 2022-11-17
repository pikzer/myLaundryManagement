package th.ac.ku.mylaundry.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Employee;
import th.ac.ku.mylaundry.model.Laundry;
import th.ac.ku.mylaundry.service.ApiCall;
import th.ac.ku.mylaundry.service.Validator;

import java.io.IOException;
import java.util.Arrays;

public class RegisterShopController  {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    @FXML
    TextField shopNameField, shopTelField, shopMailField, idLineField, nameField, telField, mailField, idField ,
            bankNumField ;
    @FXML
    ComboBox openTimeCombo, closeTimeCombo, bankCombo;

    @FXML
    PasswordField pwdField,conPwdField,mailPwdField ;
    @FXML
    CheckBox sunCheck, monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck, telCheck, mailCheck, adsCheck ;
    @FXML
    Button registerButton ;
    @FXML
    TextArea adsTextArea, ownerAdsTextArea ;

    String timeTable[] = {"06:00","07:00","08:00", "09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00",
            "17:00", "18:00","19:00","20.00","21:00","22.00","23:00","00:00","01:00","02:00","03:00","04:00","05:00",} ;
    String bankTable[] = {"กรุงเทพ","ไทยพาณิขณ์","กสิกรไทย","ออมสิน","ทหารไทย"} ;

    @FXML
    public void initialize(){
        openTimeCombo.getItems().addAll(timeTable);
        bankCombo.getItems().addAll(bankTable);
        bankCombo.setEditable(true);
        openTimeCombo.setOnAction((event -> {
            int selectOp = openTimeCombo.getSelectionModel().getSelectedIndex();
            String[] closeTimeTable = Arrays.copyOfRange(timeTable,selectOp+1,timeTable.length);
            closeTimeCombo.getItems().clear();
            closeTimeCombo.getItems().addAll(closeTimeTable);
        }));
        telField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    telField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(newValue.length() > 10){
                    telField.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });
        shopTelField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    shopTelField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(telCheck.isSelected()){
                    telField.setText(newValue);
                }
                if(newValue.length() > 10){
                    shopTelField.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });
        idField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    idField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(newValue.length() > 13){
                    idField.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        bankNumField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    bankNumField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        shopMailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(mailCheck.isSelected()){
                    mailField.setText(newValue);
                }
            }
        });

        adsTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(adsCheck.isSelected()){
                    ownerAdsTextArea.setText(newValue);
                }
            }
        });



        telCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    telField.setDisable(true);
                    telField.setText(shopTelField.getText());
                }
                else{
                    telField.setDisable(false);
                    telField.clear();
                }
            }
        });

        mailCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    mailField.setDisable(true);
                    mailField.setText(shopMailField.getText());
                }
                else{
                    mailField.setDisable(false);
                    mailField.clear();
                }
            }
        });
        adsCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    ownerAdsTextArea.setDisable(true);
                    ownerAdsTextArea.setText(adsTextArea.getText());
                }
                else{
                    ownerAdsTextArea.setDisable(false);
                    ownerAdsTextArea.clear();
                }
            }
        });

        monCheck.setSelected(true);
        tueCheck.setSelected(true);
        wedCheck.setSelected(true);
        thuCheck.setSelected(true);
        friCheck.setSelected(true);
        satCheck.setSelected(true);


    }
    @FXML
    public void onClickRegister(ActionEvent event) throws IOException {
        // Check Null
        if (nameField.getText().isEmpty()||telField.getText().isEmpty()||mailField.getText().isEmpty()||idField.getText().isEmpty()
        || ownerAdsTextArea.getText().isEmpty() || bankNumField.getText().isEmpty() || bankCombo.getItems().isEmpty() ||
                pwdField.getText().isEmpty() || conPwdField.getText().isEmpty() || shopNameField.getText().isEmpty()
        ||shopTelField.getText().isEmpty() || shopMailField.getText().isEmpty() ||adsTextArea.getText().isEmpty() ||
        idLineField.getText().isEmpty() || bankCombo.getItems().isEmpty() || mailPwdField.getText().isEmpty() ){
            pushAlertWarning("กรุณากรอกข้อมูลให้ครบถ้วน");
        }
        // At least must have 1 day work time
        else if(!sunCheck.isSelected() &&!monCheck.isSelected() && !tueCheck.isSelected() && !wedCheck.isSelected() &&
                !thuCheck.isSelected() &&!friCheck.isSelected() && !satCheck.isSelected()){
            pushAlertWarning("กรุณาเลือกวันทำการ");
        }
        // Validator
        else if(!Validator.isEmail(mailField.getText()) && !Validator.isEmail(mailField.getText())){
            pushAlertWarning("อีเมลล์ไม่ถูกต้อง");
        }
        else if(!Validator.isPhoneNumber(telField.getText()) || !Validator.isPhoneNumber(shopTelField.getText())){
            pushAlertWarning("หมายเลขโทรศัพท์ไม่ถูกต้อง");
        }
        // Password Policy
        else if(pwdField.getText().length() <= 8 && pwdField.getText().length() >= 32){
            pushAlertWarning("รหัสจะต้องมีความยาว 8 ตัวอักษรขึ้นไป");
        }
        else if(pwdField.getText().length() >= 32){
            pushAlertWarning("รหัสมีความมากกว่า 32 ตัวอักษร");
        }
        else if(!pwdField.getText().equals(conPwdField.getText())){
            pushAlertWarning("รหัสยืนยันไม่ถูกต้อง");
        }
        // other Validation
        else if(bankNumField.getText().length() < 10 || bankNumField.getText().length() > 15){
            pushAlertWarning("หมายเลขบัญชีไม่ถูกต้อง");
        }
        else{

            Laundry laundry = new Laundry(shopNameField.getText(), shopTelField.getText(),shopMailField.getText()
                    ,adsTextArea.getText(),idLineField.getText(),getWorkDayCode(),openTimeCombo.getSelectionModel().getSelectedItem().toString(),
                    closeTimeCombo.getSelectionModel().getSelectedItem().toString(),mailPwdField.getText()) ;
            Employee employee = new Employee(nameField.getText(),telField.getText(),mailField.getText(),
                    pwdField.getText(),ownerAdsTextArea.getText(), idField.getText(),bankNumField.getText(),
                    bankCombo.getSelectionModel().getSelectedItem().toString()) ;
            root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/loginView.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ApiCall.postLaundry(laundry,employee);
        }
    }

    @FXML
    void onBackBtn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/landingView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void pushAlertWarning(String message){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.WARNING);
        a.setContentText(message);
        a.show();
    }

    public String getWorkDayCode(){
        String workDay = "" ;
        if(sunCheck.isSelected())
            workDay+="1";
        else
            workDay+=0;
        if(monCheck.isSelected())
            workDay+="1";
        else
            workDay+="0";
        if(tueCheck.isSelected())
            workDay+="1";
        else
            workDay+="0";
        if(wedCheck.isSelected())
            workDay+="1";
        else
            workDay+="0";
        if(thuCheck.isSelected())
            workDay+="1";
        else
            workDay+="0";
        if(friCheck.isSelected())
            workDay+="1";
        else
            workDay+="0";
        if(satCheck.isSelected())
            workDay+="1";
        else
            workDay+="0";
        return workDay;
    }

    private void dev(){
        shopNameField.setText("My Meetang Laundry");
        shopTelField.setText("0255555555");
        shopMailField.setText("shop@mail.com");
        adsTextArea.setText("1767/8 Thanon Si Vara, Khwaeng Phlabphla, Khet Wang Thonglang, Krung Thep Maha Nakhon 10312");
        idLineField.setText("meetangLaundryID");
        nameField.setText("Rujpas");
        ownerAdsTextArea.setText("21 Siethong Niwet Soi 5, Tambon Tha Sai, Amphoe Mueang Nonthaburi, Chang Wat Nonthaburi 11000");
        telField.setText("0855555555");
        idField.setText("1104355259586");
        mailField.setText("rujipas@mail.com");
        bankNumField.setText("1234567890");
        pwdField.setText("password");
        conPwdField.setText("password");
    }
}
