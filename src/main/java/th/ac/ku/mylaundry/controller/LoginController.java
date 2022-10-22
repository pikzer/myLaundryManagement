package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.service.ApiCall;

import java.io.IOException;

public class LoginController {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    @FXML
    TextField emailTextField ;
    @FXML
    PasswordField pwdField ;
    @FXML
    protected Button loginBtn, backBtn ;

    @FXML
    public void initialize()  {
        emailTextField.setText("owner@mail.com");
        pwdField.setText("password");
    }


    @FXML
    void onBackBtn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/landingView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onLoginBtn(ActionEvent event) throws IOException {
//        APICall.login(emailTextField.getText(),pwdField.getText());
        if (emailTextField.getText().isEmpty() || pwdField.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("กรุรากรอกอีเมลล์หรือรหัสผ่าน");
            a.show();
        }
        else{
            int login = ApiCall.login(emailTextField.getText(),pwdField.getText()) ;
            if(login == 1){
                System.out.println("Log in Complete");
                root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/homeView.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else if(login == 2){
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("ิลูกค้าไม่สามรถใช้งานได้");
                a.show();
            }
            else if(login == 0){
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("อีเมลล์หรือรหัสไม่ถูกต้อง");
                a.show();
            }
        }
    }

    @FXML
    public void onClickForgetPwd(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/forgetPasswordView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
