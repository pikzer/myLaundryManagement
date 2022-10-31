package th.ac.ku.mylaundry.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import th.ac.ku.mylaundry.model.ClothList;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.model.Employee;
import th.ac.ku.mylaundry.model.Order;
import th.ac.ku.mylaundry.service.OrderApiDataSource;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderListViewController extends Navigator {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    TableView orderTable, clothListTable ;
    @FXML
    ComboBox statusCombo, serviceCombo, orderTypeCombo, rangeCombo;
    @FXML
    TextField searchField, subTotalField, deliField, vatField, totalField;
    @FXML
    TextArea adsArea ;
    @FXML
    CheckBox memCheck ;
    @FXML
    Label nameLabel, phoneLabel, serviceLabel, statusLabel, payMethodLabel, payStatusLabel;
    @FXML
    Button updateBtn, acceptBtn, cancelBtn, showQrBtn, invBtn, receiptBtn, tagBtn, makePayBtn,makePaperBtn,addDeliverBtn, addClothListBtn ;


    String statusList[] = {"ทั้งหมด","order in", "order add", "order-confirm" , "waiting for pick up", "pick up", "in progress",
            "finish laundry", "go out delivery", "delivery complete", "complete"};
    String serviceList[] = {"ทั้งหมด","ซักอบ","ซักรีด","ซักแห้ง","รีด"};
    String serviceTypeList [] = {"ทั้งหมด","หน้าร้าน","แอป"} ;
    String rangeList[] = { "วันนี้", "สัปดาห์นี้","เดือนนี้","ปีนี้","ทั้งหมด"};
    DecimalFormat f = new DecimalFormat("#0.00");


    ArrayList<Order> orderArrayList ;
    ObservableList<Order> orderObservableList;

    Order selectedOrder ;

    public void initialize(){
        orderArrayList = OrderApiDataSource.getTodayOrder();
        serviceCombo.getItems().addAll(serviceList);
        statusCombo.getItems().addAll(statusList);
        orderTypeCombo.getItems().addAll(serviceTypeList);
        rangeCombo.getItems().addAll(rangeList);
        serviceCombo.getSelectionModel().select(0);
        statusCombo.getSelectionModel().select(0);
        orderTypeCombo.getSelectionModel().select(0);
        rangeCombo.getSelectionModel().select(0);
        showOrderTable();
        updateBtn.setDisable(true);
        acceptBtn.setDisable(true);
        addClothListBtn.setDisable(true);
        cancelBtn.setDisable(true);
        invBtn.setDisable(true);
        showQrBtn.setDisable(true);
        receiptBtn.setDisable(true);
        tagBtn.setDisable(true);
        makePayBtn.setDisable(true);
        addDeliverBtn.setDisable(true);
        makePaperBtn.setDisable(true);

        rangeCombo.setOnAction(event -> {
            if(rangeCombo.getSelectionModel().getSelectedIndex()==0){
                orderArrayList = OrderApiDataSource.getTodayOrder();
                showOrderTable();

            }
            else if(rangeCombo.getSelectionModel().getSelectedIndex()==1){
                orderArrayList = OrderApiDataSource.getWeekOrder();
                showOrderTable();


            }
            else if(rangeCombo.getSelectionModel().getSelectedIndex()==2){
                orderArrayList = OrderApiDataSource.getMonthOrder();
                showOrderTable();


            }
            else if(rangeCombo.getSelectionModel().getSelectedIndex()==3){
                orderArrayList = OrderApiDataSource.getYearOrder();
                showOrderTable();

            }
            else{
                orderArrayList = OrderApiDataSource.getOrderList();
                showOrderTable();

            }
        });

//        orderObservableList = FXCollections.observableList(orderArrayList) ;
//        FilteredList<Order> filteredList  = new FilteredList<>(orderObservableList, p-> true) ;
//
//        orderTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if(newValue != null){
//                onSelectedOrder((Order) newValue);
//                try {
//                    showClothListTable((Order) newValue);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//        searchField.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
//                filteredList.setPredicate(order -> {
//                    if(newValue == null || newValue.isEmpty()|| oldValue == null || oldValue.isEmpty()){
//                        return true ;
//                    }
//                    String lower = newValue.toLowerCase() ;
//                    if(order.getCus_phone().contains(newValue)){
//                        return true;
//                    }
//                    if(order.getName().contains(lower))
//                        return true ;
//                    return false ;
//                });
//            }
//        });
//
//        serviceCombo.setOnAction(event -> {
//            filteredList.setPredicate(order -> {
//                if(serviceCombo.getSelectionModel().getSelectedItem().toString().equals("ทั้งหมด")){
//                    return true;
//                }
//                else if(order.getService().equals(serviceCombo.getSelectionModel().getSelectedItem().toString())){
//                    return true ;
//                }
//                return false ;
//            });
//        });
//
//
//
//       statusCombo.setOnAction(event -> {
//            filteredList.setPredicate(order -> {
//                if(statusCombo.getSelectionModel().getSelectedItem().toString().equals("ทั้งหมด")){
//                    return true;
//                }
//                else if(order.getStatus().equals(statusCombo.getSelectionModel().getSelectedItem().toString())){
//                    return true ;
//                }
//                return false ;
//            });
//        });
//
//        orderTypeCombo.setOnAction(event -> {
//            filteredList.setPredicate(order -> {
//                if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("ทั้งหมด")){
//                    return true;
//                }
//                else if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("หน้าร้าน")){
//                    if(order.getName().contains("ORS")){
//                        return true ;
//                    }
//                }
//                else if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("แอป")){
//                    if(order.getName().contains("ORA")){
//                        return true ;
//                    }
//                }
//                return false ;
//            });
//        });
//
//        memCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
//                filteredList.setPredicate(order -> {
//                    if(newValue){
//                        if(order.getIsMemOrder() == 1){
//                            return true;
//                        }
//                        else{
//                            return false;
//                        }
//                    }
//                    else if(!newValue){
//                        if(order.getIsMemOrder() == 0){
//                            return true;
//                        }
//                        else{
//                            return false;
//                        }
//                    }
//                    return false ;
//                });
//            }
//        });
//
//        SortedList<Order> orderSortedList =new SortedList<>(filteredList) ;
//        orderSortedList.comparatorProperty().bind(orderTable.comparatorProperty());
    }

    public void showOrderTable(){
        orderTable.getColumns().clear();
        orderObservableList = FXCollections.observableList(orderArrayList) ;
        FilteredList<Order> filteredList  = new FilteredList<>(orderObservableList, p-> true) ;

        orderTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                onSelectedOrder((Order) newValue);
                try {
                    showClothListTable((Order) newValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                filteredList.setPredicate(order -> {
                    if(newValue == null || newValue.isEmpty()|| oldValue == null || oldValue.isEmpty()){
                        return true ;
                    }
                    String lower = newValue.toLowerCase() ;
                    if(order.getCus_phone().contains(newValue)){
                        return true;
                    }
                    if(order.getName().contains(lower))
                        return true ;
                    return false ;
                });
            }
        });

        serviceCombo.setOnAction(event -> {
            filteredList.setPredicate(order -> {
                if(serviceCombo.getSelectionModel().getSelectedItem().toString().equals("ทั้งหมด")){
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
                if(statusCombo.getSelectionModel().getSelectedItem().toString().equals("ทั้งหมด")){
                    return true;
                }
                else if(order.getStatus().equals(statusCombo.getSelectionModel().getSelectedItem().toString())){
                    return true ;
                }
                return false ;
            });
        });

        orderTypeCombo.setOnAction(event -> {
            filteredList.setPredicate(order -> {
                if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("ทั้งหมด")){
                    return true;
                }
                else if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("หน้าร้าน")){
                    if(order.getName().contains("ORS")){
                        return true ;
                    }
                }
                else if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("แอป")){
                    if(order.getName().contains("ORA")){
                        return true ;
                    }
                }
                return false ;
            });
        });

        memCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                filteredList.setPredicate(order -> {
                    if(newValue){
                        if(order.getIsMemOrder() == 1){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    else if(!newValue){
                        if(order.getIsMemOrder() == 0){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    return false ;
                });
            }
        });

        SortedList<Order> orderSortedList =new SortedList<>(filteredList) ;
        orderSortedList.comparatorProperty().bind(orderTable.comparatorProperty());
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
        orderTable.setItems(orderSortedList);
    }

    public void showClothListTable(Order order) throws IOException {
        clothListTable.getColumns().clear();
        ObservableList<ClothList> clothLists  = FXCollections.observableList(OrderApiDataSource.getOrderClothList(order.getId()));
        TableColumn numberCol = new TableColumn("#");
        numberCol.setMinWidth(20);
        numberCol.setMaxWidth(30);
        numberCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClothList, ClothList>, ObservableValue<ClothList>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<ClothList, ClothList> p) {
                return new ReadOnlyObjectWrapper(p.getValue());
            }
        });

        numberCol.setCellFactory(new Callback<TableColumn<ClothList, ClothList>, TableCell<ClothList, ClothList>>() {
            @Override public TableCell<ClothList, ClothList> call(TableColumn<ClothList, ClothList> param) {
                return new TableCell<ClothList, ClothList>() {
                    @Override protected void updateItem(ClothList item, boolean empty) {
                        super.updateItem(item, empty);

                        if (this.getTableRow() != null && item != null) {
                            setText(this.getTableRow().getIndex()+1+"");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        numberCol.setSortable(false);
        TableColumn<ClothList, String> serviceCol = new TableColumn<>("บริการ");
        TableColumn<ClothList, String> cateCol = new TableColumn<ClothList, String>("ประเภทผ้า");
        TableColumn<ClothList, Integer> quantityCol = new TableColumn<ClothList, Integer>("จำนวน");
        cateCol.setCellValueFactory(new PropertyValueFactory<ClothList,String>("category"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<ClothList,Integer>("quantity"));
        serviceCol.setCellValueFactory(new PropertyValueFactory<ClothList,String >("service"));

        clothListTable.getColumns().addAll(numberCol,serviceCol,cateCol, quantityCol) ;
        clothListTable.setItems(clothLists);
    }

    public void onSelectedOrder(Order order){
        selectedOrder = order ;
        nameLabel.setText(order.getName());
        phoneLabel.setText(order.getCus_phone());
        adsArea.setText(order.getAddress().toString());
        serviceLabel.setText(order.getService());
        statusLabel.setText(order.getStatus());
        payMethodLabel.setText(order.getPayMethod());
        tagBtn.setDisable(false);
        makePaperBtn.setDisable(false);
        if(order.getPayStatus()==0){
            payStatusLabel.setText("ยังไม่ชำระ");
            makePayBtn.setDisable(false);
            invBtn.setDisable(false);
            receiptBtn.setDisable(true);
        }
        else{
            payStatusLabel.setText("ชำระแล้ว");
            receiptBtn.setDisable(false);
            makePayBtn.setDisable(true);
            invBtn.setDisable(true);
        }
        if(order.getStatus().equals("order add") || order.getStatus().equals("complete")){
            updateBtn.setDisable(true);
            if(order.getStatus().equals("order add")){
                acceptBtn.setDisable(false);
                cancelBtn.setDisable(false);
            }
        }
        if(order.getStatus().equals("pick up")){
            addClothListBtn.setDisable(false);
        }
        else{
            updateBtn.setDisable(false);
            addClothListBtn.setDisable(true);
            acceptBtn.setDisable(true);
            cancelBtn.setDisable(true);
        }
        if(order.getAddress().equals("null") && order.getResponder().equals("ยังไม่ลงทะเบียน")){
            addDeliverBtn.setDisable(true);
        }
        else{
            addDeliverBtn.setDisable(false);
        }
        double tax = order.getTotal() * 0.07 ;
        vatField.setText(f.format(tax));
        subTotalField.setText(f.format(order.getTotal()-tax-order.getDeliSerCharge()-order.getPickSerCharge()));
        totalField.setText(f.format(order.getTotal()));
        deliField.setText(f.format(order.getDeliSerCharge()+order.getPickSerCharge()));
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

    public void onClickPaper(){

    }

    public void onClickAddDeliver(){

    }

    public void onClickMakePay(){
        if(selectedOrder != null){
            OrderApiDataSource.payMoney(selectedOrder.getId());
        }
    }
}
