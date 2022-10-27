package th.ac.ku.mylaundry.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Category;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.model.MemberPackage;
import th.ac.ku.mylaundry.service.MemberPackageApiDataSource;
import th.ac.ku.mylaundry.service.Validator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MemberPackageController {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    @FXML
    TableView packageTable ;
    @FXML
    ComboBox serviceCombo ;
    @FXML
    TextField numOfClothField, priceField ;
    @FXML
    Label pricePerPieceLabel ;
    @FXML
    Button addEditBtn, delBtn ;

    ArrayList<MemberPackage> memberPackages ;

    String serviceList[] = {"ซักอบ&ซักรีด","ซักรีด", "ซักอบ", "ซักแห้ง", "รีด"} ;
    DecimalFormat f = new DecimalFormat("#0.00");
    MemberPackage selectedPackage ;


    public void initialize() throws IOException {
        serviceCombo.getSelectionModel().clearSelection();
        serviceCombo.getItems().clear();
        memberPackages = MemberPackageApiDataSource.getMemberPackage() ;
        serviceCombo.getItems().addAll(serviceList);
        delBtn.setDisable(true);
        pricePerPieceLabel.setText(f.format(0.00));
        showTable();
        packageTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                onSelectPackage((MemberPackage) newValue); ;
            }
        });
        numOfClothField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numOfClothField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        priceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if(newValue != null){
                    if(!numOfClothField.getText().isEmpty()){
                        if(Validator.isDoubleAndPositive(newValue)){
                            pricePerPieceLabel.setText(String.valueOf(Double.parseDouble(newValue)/Integer.parseInt(numOfClothField.getText())));
                        }
                    }
                }
            }
        });
    }

    public void showTable(){
        packageTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<MemberPackage, String> serviceCol = new TableColumn<MemberPackage, String>("บริการ");
        TableColumn<MemberPackage, Integer> quantityCol = new TableColumn<MemberPackage, Integer>("จำนวน");
        TableColumn<MemberPackage, Double> priceCol = new TableColumn<MemberPackage, Double>("ราคา");


        idCol.setCellValueFactory(new PropertyValueFactory<MemberPackage,Integer>("id"));
        serviceCol.setCellValueFactory(new PropertyValueFactory<MemberPackage,String>("service"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<MemberPackage,Integer>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<MemberPackage,Double>("price"));

        packageTable.getColumns().addAll(idCol,serviceCol,quantityCol,priceCol) ;
        packageTable.setItems(FXCollections.observableList(memberPackages));
    }

    public void onSelectPackage(MemberPackage memberPackage){
        selectedPackage = memberPackage;
        delBtn.setDisable(false);
        serviceCombo.getSelectionModel().select(memberPackage.getService());
        numOfClothField.setText(String.valueOf(memberPackage.getQuantity()));
        priceField.setText(String.valueOf(memberPackage.getPrice()));
        pricePerPieceLabel.setText(f.format(memberPackage.getPrice()/memberPackage.getQuantity()));
    }

    public void onClickAddEdit() throws IOException {
        if(selectedPackage == null){
            if(serviceCombo.getSelectionModel().isEmpty() || numOfClothField.getText().isEmpty()
                    || priceField.getText().isEmpty()){
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("กรุณากรอกข้อมูลให้ครบถ้วน");
                a.show();
            } else if (!Validator.isDoubleAndPositive(priceField.getText())) {
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("กรุณากรอกราคาให้ถูกต้อง");
                a.show();
            }else {
                if(MemberPackageApiDataSource.addMemberPackage(serviceCombo.getSelectionModel().getSelectedItem().toString(),
                        Integer.parseInt(numOfClothField.getText()),Double.parseDouble(priceField.getText()))){
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("เพิ่มสำเร็จ");
                    a.show();
                    onClickClear();
                    initialize();
                }else{
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.WARNING);
                    a.setContentText("เพิ่มไม่สำเร็จ");
                    a.show();
                }
                onClickClear();
                initialize();
            }
        }
        else{
            if(serviceCombo.getSelectionModel().isEmpty() || numOfClothField.getText().isEmpty()
                    || priceField.getText().isEmpty()){
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("กรุณากรอกข้อมูลให้ครบถ้วน");
                a.show();
            } else if (!Validator.isDoubleAndPositive(priceField.getText())) {
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.WARNING);
                a.setContentText("กรุณากรอกราคาให้ถูกต้อง");
                a.show();
            }else {
//                MemberPackageApiDataSource.addMemberPackage(serviceCombo.getSelectionModel().getSelectedItem().toString(),
//                        Integer.parseInt(numOfClothField.getText()),Double.parseDouble(priceField.getText()));
                if(MemberPackageApiDataSource.patchMemberPackage(selectedPackage.getId(),
                        serviceCombo.getSelectionModel().getSelectedItem().toString(),
                        Integer.parseInt(numOfClothField.getText()),
                        Double.parseDouble(priceField.getText()))){
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("แก้ไขสำเร็จ");
                    a.show();
                    onClickClear();
                    initialize();
                }
                else{
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("แก้ไขไม่สำเร็จ");
                    a.show();
                }
            }
        }
    }

    public void onClickDel() throws IOException {
        if(MemberPackageApiDataSource.deleteMemberPackage(selectedPackage.getId())){
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("ลบสำเร็จ");
            a.show();
            onClickClear();
            initialize();
        }
        else{
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("ลบไม่สำเร็จ");
            a.show();
        }
    }

    public void onClickClear() throws IOException {
        selectedPackage = null ;
        packageTable.getSelectionModel().clearSelection();
        serviceCombo.getSelectionModel().clearSelection();
        serviceCombo.getItems().clear();
        numOfClothField.clear();
        priceField.clear();
        pricePerPieceLabel.setText(f.format(0.00));
        initialize();
    }

    public void onClickBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/serviceListView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
