package th.ac.ku.mylaundry.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.model.Employee;
import th.ac.ku.mylaundry.model.Order;
import th.ac.ku.mylaundry.service.OrderApiDataSource;

import java.util.ArrayList;

public class OrderListViewController extends Navigator {
    @FXML
    TableView orderTable ;
    @FXML
    ComboBox statusCombo, serviceCombo, deliverCombo;
    @FXML
    TextField searchField;
    @FXML
    CheckBox onsiteCheck, appCheck;
    @FXML
    Button updateBtn, acceptBtn, cancelBtn, showQrBtn, invBtn, receiptBtn, tagBtn, makePayBtn;


    String statusList[] = {"order in", "order add", "order-confirm" , "waiting for pick up", "pick up", "in progress",
            "finish laundry", "go out delivery", "delivery complete", "complete"};
    String serviceList[] = {"ซักอบ","ซักรีด","ซักแห้ง","รีด"};
    ArrayList<Order> orderArrayList ;
    ObservableList<Order> orderObservableList;

    Order selectedOrder ;

    public void initialize(){
        orderArrayList = OrderApiDataSource.getOrderList();
        serviceCombo.getItems().addAll(serviceList);
        statusCombo.getItems().addAll(statusList);

        orderObservableList = FXCollections.observableList(orderArrayList) ;
        FilteredList<Order> filteredList  = new FilteredList<>(orderObservableList, p-> true) ;

        orderTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                onSelectedOrder((Order) newValue);
            }
        });
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
//                filteredList.setPredicate(order -> {
//                    if(newValue == null || newValue.isEmpty()|| oldValue == null || oldValue.isEmpty()){
//                        return true ;
//                    }
////                    String lower = newValue.toLowerCase() ;
//                    if(order.getP){
//                        return true;
//                    }
//                    if(customer.getName().contains(lower))
//                        return true ;
//                    return false ;
//                });
            }
        });

        serviceCombo.setOnAction(event -> {
            filteredList.setPredicate(order -> {
                if(serviceCombo.getSelectionModel().getSelectedItem() == null){
                    return true;
                }
                else if(order.getService().equals(serviceCombo.getSelectionModel().getSelectedItem().toString())){
                    return true ;
                }
                return false ;
            });
        });

       statusCombo.setOnAction(event -> {
            filteredList.setPredicate(order -> {
                if(statusCombo.getSelectionModel().getSelectedItem() == null){
                    return true;
                }
                else if(order.getStatus().equals(statusCombo.getSelectionModel().getSelectedItem().toString())){
                    return true ;
                }
                return false ;
            });
        });

        onsiteCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                filteredList.setPredicate(order -> {
                    if(newValue){
                        if(order.getName().contains("ORS")){
                            return true;
                        }
                    }
                    return false;
                });
            }
        });
        appCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                filteredList.setPredicate(order -> {
                    if(newValue){
                        if(order.getName().contains("ORA")){
                            return true;
                        }
                    }
                    return false;
                });
            }
        });

        SortedList<Order> orderSortedList =new SortedList<>(filteredList) ;
        orderSortedList.comparatorProperty().bind(orderTable.comparatorProperty());
        showOrderTable(orderSortedList);

    }

    public void showOrderTable(SortedList<Order> orderSortedList){
        orderTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Order, String> nameCol = new TableColumn<Order, String>("ชื่อ");
        TableColumn<Order, String> serviceCol = new TableColumn<Order, String>("บริการ");
        TableColumn<Order, String> statusCol = new TableColumn<Order, String>("สถานะ");
        TableColumn<Order, String> pickDateCol = new TableColumn<Order, String>("วันที่รับ");
        TableColumn<Order, String> pickTimeCol = new TableColumn<Order, String>("เวลาที่รับ");
        TableColumn<Order, String> deliDateCol = new TableColumn<Order, String>("วันที่ส่ง");
        TableColumn<Order, String> deliTimeCol = new TableColumn<Order, String>("เวลาที่ส่ง");
        TableColumn<Order, String> addressCol = new TableColumn<Order, String>("ที่อยู่");
        TableColumn<Order, String> responderCol = new TableColumn<Order, String>("ผู้รับผิดชอบ");
        TableColumn<Order, String> deliverCol = new TableColumn<Order, String>("ผู้ส่งผ้า");
        TableColumn<Order, String> payMethodCol = new TableColumn<Order, String>("รูปแบบการชำระ");
        TableColumn<Order, Boolean> payStatusCol = new TableColumn<Order, Boolean>("สถานะชำระ");
        TableColumn<Order, Double> pickSerChargeCol = new TableColumn<Order, Double>("ค่ารับ");
        TableColumn<Order, Double> deliSerChargeCol = new TableColumn<Order, Double>("ค่าส่ง");
        TableColumn<Order, Boolean> isMemCol = new TableColumn<Order, Boolean>("รายการสมาชิก");
        TableColumn<Order, Double> totalCol = new TableColumn<Order, Double>("ค่าบริการรวม");

        idCol.setCellValueFactory(new PropertyValueFactory<Order,Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Order,String>("name"));
        serviceCol.setCellValueFactory(new PropertyValueFactory<Order,String>("service"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Order,String>("status"));
        pickDateCol.setCellValueFactory(new PropertyValueFactory<Order,String>("pickDate"));
        pickTimeCol.setCellValueFactory(new PropertyValueFactory<Order,String>("pickTime"));
        deliDateCol.setCellValueFactory(new PropertyValueFactory<Order,String>("deliDate"));
        deliTimeCol.setCellValueFactory(new PropertyValueFactory<Order,String>("deliTime"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Order,String>("address"));
        responderCol.setCellValueFactory(new PropertyValueFactory<Order,String>("responder"));
        deliverCol.setCellValueFactory(new PropertyValueFactory<Order,String>("deliver"));
        payMethodCol.setCellValueFactory(new PropertyValueFactory<Order,String>("payMethod"));
        payStatusCol.setCellValueFactory(new PropertyValueFactory<Order,Boolean>("payStatus"));
        pickSerChargeCol.setCellValueFactory(new PropertyValueFactory<Order,Double>("pickSerCharge"));
        deliSerChargeCol.setCellValueFactory(new PropertyValueFactory<Order,Double>("deliSerCharge"));
        isMemCol.setCellValueFactory(new PropertyValueFactory<Order,Boolean>("isMemOrder"));
        totalCol.setCellValueFactory(new PropertyValueFactory<Order,Double>("total"));

        orderTable.getColumns().addAll(idCol,nameCol,serviceCol,statusCol,pickDateCol,pickTimeCol,deliDateCol,deliTimeCol,
                addressCol,responderCol,deliverCol,payMethodCol,payStatusCol,pickSerChargeCol,deliSerChargeCol,isMemCol,totalCol
        );
    }

    public void onSelectedOrder(Order order){
        selectedOrder = order ;
    }

    public void onClickAnchor(){
        orderTable.getSelectionModel().clearSelection();
    }

    public void onClickUpdate(){
        if(selectedOrder != null){
            OrderApiDataSource.updateOrderStatus(selectedOrder.getId());
        }
    }

    public void onClickAccept(){
        if(selectedOrder != null){
            if(selectedOrder.getStatus().equals("order in")){
                OrderApiDataSource.updateOrderStatus(selectedOrder.getId());
            }
            else{
                // some alert
            }
        }
    }

    public void onClickCancel(){
        if(selectedOrder != null){
            if(selectedOrder.getStatus().equals("order in")){
                // TODO confirm alert
                OrderApiDataSource.updateOrderStatus(selectedOrder.getId());
            }
            else{
                // some alert
            }
        }
    }

    public void onClickShowQr(){

    }

    public void onClickInv(){

    }

    public void onClickReceipt(){

    }

    public void onClickTag(){

    }

    public void onClickMakePay(){
        if(selectedOrder != null){
            OrderApiDataSource.payMoney(selectedOrder.getId());
        }
    }
}
