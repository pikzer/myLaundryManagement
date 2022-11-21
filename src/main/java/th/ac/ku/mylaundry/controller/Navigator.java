package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.service.ApiCall;
import th.ac.ku.mylaundry.service.LaundryApiDataSource;

import java.io.File;
import java.io.IOException;

public class Navigator {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    @FXML
    Label shopNameLabel;

    public void onClickHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/homeView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickOrder(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/orderListView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickCus(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/customerlistView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickDeli(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/deliListView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickServiceRate(ActionEvent event) throws IOException {
        if(ApiCall.role.equals("EMPLOYEE")){
            pushAlert("คุณไม่มีสิทธฺิในการเข้าถึงฟังชั่นดังกล่าว", Alert.AlertType.WARNING);
        }
        else if (LaundryApiDataSource.getStatusShop()==1){
            pushAlert("ไม่สามารถเพิ่มหรือแก้ไขในขณะที่ร้านเปิดอยู่", Alert.AlertType.WARNING);

        } else {
            root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/serviceListView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onClickNoti(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/notificationView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickReport(ActionEvent event) throws IOException {
        if(ApiCall.role.equals("EMPLOYEE")){
            pushAlert("คุณไม่มีสิทธฺิในการเข้าถึงฟังชั่นดังกล่าว", Alert.AlertType.WARNING);
        }else{
            root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/reportView.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onClickManage(ActionEvent event) throws IOException {
        if(ApiCall.role.equals("EMPLOYEE")){
            pushAlert("คุณไม่มีสิทธฺิในการเข้าถึงฟังชั่นดังกล่าว", Alert.AlertType.WARNING);
        }else{
            root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/manageShopView.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onClickLogout(ActionEvent event) throws IOException {
        if(ApiCall.logout()){
            root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/landingView.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            File dir = new File("inv");
            if(dir.exists()){
                for (File file: dir.listFiles()) {
                    if(!file.isDirectory()){
                        file.delete();
                    }
                }
            }
            dir = new File("receipt");
            if(dir.exists()){
                for (File file: dir.listFiles()) {
                    if(!file.isDirectory()){
                        file.delete();
                    }
                }
            }
            dir = new File("report");
            if(dir.exists()){
                for (File file: dir.listFiles()) {
                    if(!file.isDirectory()){
                        file.delete();
                    }
                }
            }

            dir = new File("delivery");
            if(dir.exists()){
                for (File file: dir.listFiles()) {
                    if(!file.isDirectory()){
                        file.delete();
                    }
                }
            }
        }
    }

    public void pushAlert(String message, Alert.AlertType alertType){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(alertType);
        a.setContentText(message);
        a.show();
    }


}
