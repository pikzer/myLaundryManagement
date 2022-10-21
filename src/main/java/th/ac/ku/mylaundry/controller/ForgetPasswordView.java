package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgetPasswordView {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    public void onClickCheck(ActionEvent event){

    }

    public void onClickEditPwd(ActionEvent event){

    }

    public void onClickBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/landingView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
