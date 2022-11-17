package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.*;
import th.ac.ku.mylaundry.service.*;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class DeliListController extends Navigator {
    @FXML
    DatePicker datePicker, deliDatePicker;
    @FXML
    ComboBox jobCombo, deliverCombo, timeCombo;
    @FXML
    Label orderNameLabel ;
    @FXML
    TextField searchField ;
    @FXML
    TableView deliTable ;
    @FXML
    Button editBtn, assignBtn;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    ArrayList<DeliveryTime> deliveryTimes ;
    String jobList[] = {"ทั้งหมด","ส่งผ้า","รับผ้า"} ;
    String timeList[] = {"ช่่วงเช้า","ช่วงบ่าย","ช่วงเย็น"} ;
    DeliveryTime selectDeliveryTime ;

    public void initialize() throws IOException {
        deliveryTimes = DeliveryTimeApiDataSource.getDeliveryTime() ;
        ObservableList<DeliveryTime> observableList = FXCollections.observableList(deliveryTimes);
        FilteredList<DeliveryTime> deliveryTimes = new FilteredList<>(observableList);
        editBtn.setDisable(true);
        assignBtn.setDisable(true);
        shopNameLabel.setText(LaundryApiDataSource.getLaundryName(1).toString());
        jobCombo.getItems().addAll(jobList);
        jobCombo.getSelectionModel().select(0);

        datePicker.setOnAction(event -> {
            deliveryTimes.setPredicate(deliveryTime -> {
                if(datePicker.getValue() != null){
                    if(datePicker.getValue().format(formatter).equals(deliveryTime.getDate())){
                        return true;
                    }
                }
                return false;
            });
        });

        searchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            deliveryTimes.setPredicate(deliveryTime -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(deliveryTime.getDeliver().contains(newValue)){
                    return true;
                }
                return false;
            });
        });

        jobCombo.setOnAction(event -> {
            deliveryTimes.setPredicate(deliveryTime -> {
                if(jobCombo.getSelectionModel().getSelectedItem().toString().equals("ทั้งหมด")){
                    return true;
                }
                else if(deliveryTime.getJob().equals(jobCombo.getSelectionModel().getSelectedItem())){
                    return true ;
                }
                return false ;
            });
        });
        SortedList<DeliveryTime> sortedList = new SortedList<>(deliveryTimes);
        sortedList.comparatorProperty().bind(deliTable.comparatorProperty());
        showDeliTable(sortedList);
        deliDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
        deliDatePicker.setOnAction(event -> {
            ArrayList<Boolean> art = DeliveryTimeApiDataSource.getAvailableInDateTime(deliDatePicker.getValue().format(formatter));
            timeCombo.getItems().clear();
            if(art.get(0)){
                timeCombo.getItems().add("ช่วงเช้า");
            }
            if(art.get(1)){
                timeCombo.getItems().add("ช่วงบ่าย");

            }
            if(art.get(2)){
                timeCombo.getItems().add("ช่วงเย็น");
            }
        });

        ObservableList<String> customers = FXCollections.observableList(getEmployeeNeme());
        FilteredList<String> filteredItems = new FilteredList<String>(customers);
        deliverCombo.getEditor().textProperty().addListener(new InputFilter(deliverCombo,filteredItems,false));
        deliverCombo.setItems(filteredItems);

        deliTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                try {
                    showSelectedDeliveryTime((DeliveryTime) newValue) ;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public ArrayList<String> getEmployeeNeme() throws IOException {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Employee> customers = EmployeeApiDataSource.getEmployees();
        for (Employee c:customers) {
            names.add(c.getName());
        }
        return names;
    }

    public void showDeliTable(SortedList sortedList){
        deliTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<DeliveryTime, String> dateCol = new TableColumn<DeliveryTime, String>("วันที่");
        TableColumn<DeliveryTime, String> timeCol = new TableColumn<DeliveryTime, String>("ช่วงเวลา");
        TableColumn<DeliveryTime, String> deliverCol = new TableColumn<DeliveryTime, String>("ผู้รับส่ง");
        TableColumn<DeliveryTime, String> orderCol = new TableColumn<DeliveryTime, String>("รายการ");
        TableColumn<DeliveryTime, String> jobCol = new TableColumn<DeliveryTime, String>("งาน");
        TableColumn<DeliveryTime, String> adsCol = new TableColumn<DeliveryTime, String>("ที่อยู่รับส่ง");


        idCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,Integer>("id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("time"));
        orderCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("orderName"));
        adsCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("u_code"));
        deliverCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String >("deliver"));
        jobCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("job"));

        deliTable.getColumns().addAll(idCol,dateCol,timeCol,adsCol,deliverCol,orderCol,jobCol) ;
        deliTable.setItems(sortedList);
    }

    public void showSelectedDeliveryTime(DeliveryTime deliveryTime) throws IOException {
        selectDeliveryTime = deliveryTime ;
        orderNameLabel.setText(deliveryTime.getOrderName());
        if(selectDeliveryTime.getJob().equals("cancel")){
            editBtn.setDisable(true);
            assignBtn.setDisable(true);
        }
        else{
            editBtn.setDisable(false);
        }
        if(selectDeliveryTime.getDeliver().equals("ยังไม่ลงทะเบียน")){
            assignBtn.setDisable(false);
        }
    }

    public void onClickEdit() throws IOException {
        if(selectDeliveryTime != null  && !timeCombo.getSelectionModel().isEmpty()){
            if(DeliveryTimeApiDataSource.addDeliveryTime(new DeliveryTime(deliDatePicker.getValue().format(formatter),
                    timeCombo.getSelectionModel().getSelectedItem().toString(),selectDeliveryTime.getOrderName(),
                    selectDeliveryTime.getJob(),selectDeliveryTime.getU_code()))){
                DeliveryTimeApiDataSource.cancelDelivery(selectDeliveryTime.getId());
                pushAlertWarning("แก้ไขเวลารับส่งสำเร็จ", Alert.AlertType.INFORMATION);
                onClear();
            }
            else {
                pushAlertWarning("แก้ไขเวลารับส่งไม่สำเร็จ", Alert.AlertType.ERROR);
                onClear();
            }
        }
    }

    public void onClickAssign() throws IOException {
        if(!deliverCombo.getSelectionModel().isEmpty()){
            if(DeliveryTimeApiDataSource.addDeliver(selectDeliveryTime.getId(),
                    deliverCombo.getSelectionModel().getSelectedItem().toString(),selectDeliveryTime.getOrderName())){
                pushAlertWarning("เพิ่มผู้รับส่งสำเร็จ", Alert.AlertType.INFORMATION);
                onClear();
            }
            else{
                pushAlertWarning("เพิ่มผู้รับส่งไม่สำเร็จสำเร็จ", Alert.AlertType.ERROR);
                onClear();
            }
        }
    }

    public void onClear() throws IOException {
        selectDeliveryTime = null ;
        deliTable.getSelectionModel().clearSelection();
        deliTable.refresh();
        datePicker.getEditor().clear();
        deliDatePicker.getEditor().clear();
        deliverCombo.getSelectionModel().clearSelection();
        timeCombo.getSelectionModel().clearSelection();
        orderNameLabel.setText("-");
        initialize();

    }

    public void onClickAnchor() throws IOException {
//        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/deliListView.fxml"));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
    }

    public void onClickCancel(){
//        DeliveryTimeApiDataSource.cancelDelivery(selectDeliveryTime.getId());

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ยกเลิกรายการ");
        alert.setHeaderText("คุณแน่จะที่จะทำการยกเลิกรายการหรือไม่");
        ButtonType cancel = new ButtonType("ยกเลิก");
        alert.getButtonTypes().add(cancel);
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == null){

        }
        else if (option.get() == ButtonType.OK) {
            DeliveryTimeApiDataSource.cancelDelivery(selectDeliveryTime.getId());
        } else if (option.get() == cancel) {

        } else {
        }
    }

    public void pushAlertWarning(String message, Alert.AlertType alertType){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(alertType);
        a.setContentText(message);
        a.show();
    }
}
