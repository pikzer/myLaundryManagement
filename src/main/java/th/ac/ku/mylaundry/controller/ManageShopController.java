package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageShopController extends Navigator  {

    @FXML
    TextField nameField, shopTelField, shopMailField, idLineField ;
    @FXML
    TextArea adsTextArea ;
    @FXML
    CheckBox sunCheck, monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck;
    @FXML
    ComboBox openTimeCombo, closeTimeCombo ;
    @FXML
    TableView employeeTable ;

    public void onClickSave(){

    }

    public void onClickEditEmp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/employeeView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
