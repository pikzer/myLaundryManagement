package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.service.APICall;

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
        APICall.login(emailTextField.getText(),pwdField.getText());
    }

}
