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

import java.util.ArrayList;
import java.util.Date;

public class DeliListController extends Navigator {
    @FXML
    DatePicker datePicker;
    @FXML
    CheckBox pickCheck, deliCheck;
    @FXML
    TextField searchField ;
    @FXML
    TableView deliTable ;

    ArrayList<DeliveryTime> deliveryTimes ;

    public void initialize(){
        deliveryTimes = DeliveryTimeApiDataSource.getDeliveryTime() ;

        // TODO
        ObservableList<DeliveryTime> observableList = FXCollections.observableList(deliveryTimes);
        FilteredList<DeliveryTime> deliveryTimes = new FilteredList<>(observableList);
        searchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            deliveryTimes.setPredicate(category -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(true){
                    return true;
                }
                return false;
            });
        });
        SortedList<DeliveryTime> sortedList = new SortedList<>(deliveryTimes);
        sortedList.comparatorProperty().bind(deliTable.comparatorProperty());
        showDeliTable(sortedList);
    }

    public void showDeliTable(SortedList sortedList){
        deliTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<DeliveryTime, Date> dateCol = new TableColumn<DeliveryTime, Date>("รายการ");
        TableColumn<DeliveryTime, String> timeCol = new TableColumn<DeliveryTime, String>("ช่วงเวลา");
        TableColumn<DeliveryTime, String> orderCol = new TableColumn<DeliveryTime, String>("รายการ");
        TableColumn<DeliveryTime, String> jobCol = new TableColumn<DeliveryTime, String>("งาน");

        idCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,Integer>("id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,Date>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("time"));
        orderCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("orderName"));
        jobCol.setCellValueFactory(new PropertyValueFactory<DeliveryTime,String>("job"));

        deliTable.getColumns().addAll(idCol,dateCol,timeCol,orderCol,jobCol) ;
        deliTable.setItems(sortedList);
    }
}
