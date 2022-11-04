package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Employee;
import th.ac.ku.mylaundry.service.EmployeeApiDataSource;

import java.io.IOException;

public class ForgetPwdController {

    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    TextField idField ;
    @FXML
    PasswordField pwdField, conPwdField;

    public void onClickBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/loginView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickEdit() throws IOException {
        if(!idField.getText().isEmpty() && !pwdField.getText().isEmpty()){
            if(idField.getText().length() != 13){
                pushAlertWarning("รหัสบัตรประชาชนไม่ถูกต้่อง", Alert.AlertType.WARNING);

            }
            else if(pwdField.getText().length() < 8 || pwdField.getText().length() > 32){
                pushAlertWarning("รหัสจะต้องมีความยาวอย่งน้อย 8 ตัวอักษรและยาวน้อยกว่า 32 ตัวอักษร", Alert.AlertType.WARNING);
            }
            else if(!pwdField.getText().equals(conPwdField.getText())){
                pushAlertWarning("รหัสยืนยันไม่ถูกต้อง",Alert.AlertType.WARNING);
            }
            else if(pwdField.getText().equals(conPwdField.getText())){
                if(EmployeeApiDataSource.changePassword(pwdField.getText(),idField.getText())){
                    pushAlertWarning("แก้ไขรหัสสำเร็จ", Alert.AlertType.INFORMATION);
                }
                else{
                    pushAlertWarning("แก้ไขรหัสไม่สำเร็จ", Alert.AlertType.ERROR);

                }
            }
        }
    }

    public void pushAlertWarning(String message, Alert.AlertType alertType){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(alertType);
        a.setContentText(message);
        a.show();
    }
}
