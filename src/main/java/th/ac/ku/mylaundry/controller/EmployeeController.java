package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeController extends HomeController{



    @FXML
    public void onBackBtn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/manageShopView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
