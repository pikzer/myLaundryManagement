package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterShopController  {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    @FXML
    TextField shopNameField, shopTelField, shopMailField, idLineField, openTimeField, closeTimeField,
    nameField, telField, mailField, idField ;
    @FXML
    PasswordField pwdField,conPwdField ;
    @FXML
    CheckBox sunCheck, monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck ;
    @FXML
    Button registerButton ;
    @FXML
    TextArea adsTextArea, ownerAdsTextArea ;

    @FXML
    public void initialize(){

    }
    @FXML
    public void onClickRegister(ActionEvent event){
        // TODO
    }

    @FXML
    void onBackBtn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/landingView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
