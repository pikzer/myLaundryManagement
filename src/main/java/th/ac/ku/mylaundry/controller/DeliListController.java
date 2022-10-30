package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import th.ac.ku.mylaundry.model.Category;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.model.DeliveryTime;
import th.ac.ku.mylaundry.service.DeliveryTimeApiDataSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DeliListController extends Navigator {
    @FXML
    DatePicker datePicker, pickDatePicker, deliDatePicker;
    @FXML
    ComboBox jobCombo,pickTimeCombo,deliTimeCombo, deliverCombo;
    @FXML
    TextField searchField ;
    @FXML
    TableView deliTable ;

    ArrayList<DeliveryTime> deliveryTimes ;
    String jobList[] = {"ทั้งหมด","ส่งผ้า","รับผ้า"} ;
    String timeList[] = {"ช่่วงเช้า","ช่วงบ่าย","ช่วงเย็น"} ;

    public void initialize(){
        deliveryTimes = DeliveryTimeApiDataSource.getDeliveryTime() ;
        ObservableList<DeliveryTime> observableList = FXCollections.observableList(deliveryTimes);
        FilteredList<DeliveryTime> deliveryTimes = new FilteredList<>(observableList);
        jobCombo.getItems().addAll(jobList);
        jobCombo.getSelectionModel().select(0);
        pickTimeCombo.getItems().addAll(timeList);
        deliTimeCombo.getItems().addAll(timeList);
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

    public void onClickEdit(){

    }

    public void onClickAssign(){

    }
}
