package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import th.ac.ku.mylaundry.model.*;
import th.ac.ku.mylaundry.service.CustomerApiDataSource;
import th.ac.ku.mylaundry.service.DeliveryTimeApiDataSource;
import th.ac.ku.mylaundry.service.EmployeeApiDataSource;
import th.ac.ku.mylaundry.service.InputFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    ArrayList<DeliveryTime> deliveryTimes ;
    String jobList[] = {"ทั้งหมด","ส่งผ้า","รับผ้า"} ;
    String timeList[] = {"ช่่วงเช้า","ช่วงบ่าย","ช่วงเย็น"} ;
    DeliveryTime selectDeliveryTime ;

    public void initialize() throws IOException {
        deliveryTimes = DeliveryTimeApiDataSource.getDeliveryTime() ;
        ObservableList<DeliveryTime> observableList = FXCollections.observableList(deliveryTimes);
        FilteredList<DeliveryTime> deliveryTimes = new FilteredList<>(observableList);
        jobCombo.getItems().addAll(jobList);
        jobCombo.getSelectionModel().select(0);
        datePicker.setValue(LocalDate.now());
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
            ArrayList<Boolean> art = DeliveryTimeApiDataSource.getAvailableInDateTime(deliDatePicker.getValue().toString());
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
                showSelectedDeliveryTime((DeliveryTime) newValue) ;
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

        idCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,Integer>("id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("time"));
        orderCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("orderName"));
        deliverCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String >("deliver"));
        jobCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("job"));

        deliTable.getColumns().addAll(idCol,dateCol,timeCol,deliverCol,orderCol,jobCol) ;
        deliTable.setItems(sortedList);
    }

    public void showSelectedDeliveryTime(DeliveryTime deliveryTime){
        selectDeliveryTime = deliveryTime ;
        orderNameLabel.setText(deliveryTime.getOrderName());
    }

    public void onClickEdit(){

    }

    public void onClickAssign(){

    }
}
