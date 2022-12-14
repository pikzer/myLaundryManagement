package th.ac.ku.mylaundry.controller;

import com.github.pheerathach.ThaiQRPromptPay;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import th.ac.ku.mylaundry.model.ClothList;
import th.ac.ku.mylaundry.model.MemberPackage;
import th.ac.ku.mylaundry.model.Order;
import th.ac.ku.mylaundry.service.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrderListViewController extends Navigator {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    TableView orderTable, clothListTable ;
    @FXML
    ComboBox statusCombo, serviceCombo, orderTypeCombo, rangeCombo, statusOrderCombo ;
    @FXML
    TextField searchField, subTotalField, deliField, vatField, totalField;
    @FXML
    TextArea adsArea ;
    @FXML
    CheckBox memCheck ;
    @FXML
    Label nameLabel, phoneLabel, serviceLabel, statusLabel, payMethodLabel, payStatusLabel;
    @FXML
    Button acceptBtn, cancelBtn, showQrBtn, invBtn, receiptBtn, tagBtn, makePayBtn,makePaperBtn,addDeliverBtn, addClothListBtn,changeStatusBtn ;


    String statusList[] = {"?????????????????????????????????", "??????????????????????????????????????????", "??????????????????" , "?????????????????????????????????????????????", "????????????????????????????????????????????????", "??????????????????",
            "???????????????????????????"};
    String serviceList[] = {"?????????????????????","???????????????","??????????????????","?????????????????????","?????????"};
    String serviceTypeList [] = {"?????????????????????","????????????????????????","?????????"} ;
    String rangeList[] = { "??????????????????", "??????????????????????????????","????????????????????????","???????????????","?????????????????????"};
    DecimalFormat f = new DecimalFormat("#0.00");


    ArrayList<Order> orderArrayList ;
    ObservableList<Order> orderObservableList;

    Order selectedOrder ;

    public void initialize() throws IOException {
        orderArrayList = OrderApiDataSource.getTodayOrder();
        serviceCombo.getItems().addAll(serviceList);
        statusCombo.getItems().addAll(statusList);
        statusOrderCombo.getItems().addAll(statusList);
        orderTypeCombo.getItems().addAll(serviceTypeList);
        rangeCombo.getItems().addAll(rangeList);
        serviceCombo.getSelectionModel().select(0);
        statusCombo.getSelectionModel().select(0);
        orderTypeCombo.getSelectionModel().select(0);
        rangeCombo.getSelectionModel().select(0);
        shopNameLabel.setText(LaundryApiDataSource.getLaundryName(1).toString());
        showOrderTable();
//        updateBtn.setDisable(true);
        acceptBtn.setDisable(true);
        addClothListBtn.setDisable(false);
        cancelBtn.setDisable(true);
        invBtn.setDisable(true);
        showQrBtn.setDisable(true);
        receiptBtn.setDisable(true);
//        tagBtn.setDisable(true);
        makePayBtn.setDisable(true);
        addDeliverBtn.setDisable(true);
//        makePaperBtn.setDisable(true);

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
//                if(serviceCombo.getSelectionModel().getSelectedItem().toString().equals("?????????????????????")){
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
//                if(statusCombo.getSelectionModel().getSelectedItem().toString().equals("?????????????????????")){
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
//                if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("?????????????????????")){
//                    return true;
//                }
//                else if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("????????????????????????")){
//                    if(order.getName().contains("ORS")){
//                        return true ;
//                    }
//                }
//                else if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("?????????")){
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
                try {
                    onSelectedOrder((Order) newValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                if(serviceCombo.getSelectionModel().getSelectedItem().toString().equals("?????????????????????")){
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
                if(statusCombo.getSelectionModel().getSelectedItem().toString().equals("?????????????????????")){
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
                if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("?????????????????????")){
                    return true;
                }
                else if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("????????????????????????")){
                    if(order.getName().contains("ORS")){
                        return true ;
                    }
                }
                else if(orderTypeCombo.getSelectionModel().getSelectedItem().toString().equals("?????????")){
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
        TableColumn idCol = new TableColumn("????????????");
        TableColumn<Order, String> nameCol = new TableColumn<Order, String>("????????????");
        TableColumn<Order, String> serviceCol = new TableColumn<Order, String>("??????????????????");
        TableColumn<Order, String> statusCol = new TableColumn<Order, String>("???????????????");
        TableColumn<Order, String> pickDateCol = new TableColumn<Order, String>("???????????????????????????");
        TableColumn<Order, String> pickTimeCol = new TableColumn<Order, String>("??????????????????????????????");
        TableColumn<Order, String> deliDateCol = new TableColumn<Order, String>("???????????????????????????");
        TableColumn<Order, String> deliTimeCol = new TableColumn<Order, String>("??????????????????????????????");
        TableColumn<Order, String> addressCol = new TableColumn<Order, String>("?????????????????????");
        TableColumn<Order, String> responderCol = new TableColumn<Order, String>("????????????????????????????????????");
        TableColumn<Order, String> deliverCol = new TableColumn<Order, String>("???????????????????????????");
        TableColumn<Order, String> payMethodCol = new TableColumn<Order, String>("???????????????????????????????????????");
        TableColumn<Order, Boolean> payStatusCol = new TableColumn<Order, Boolean>("???????????????????????????");
        TableColumn<Order, Double> pickSerChargeCol = new TableColumn<Order, Double>("??????????????????");
        TableColumn<Order, Double> deliSerChargeCol = new TableColumn<Order, Double>("??????????????????");
        TableColumn<Order, Boolean> isMemCol = new TableColumn<Order, Boolean>("????????????????????????????????????");
        TableColumn<Order, Double> totalCol = new TableColumn<Order, Double>("????????????????????????????????????");

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



    // TODO add col price

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
        TableColumn<ClothList, String> serviceCol = new TableColumn<>("??????????????????");
        TableColumn<ClothList, String> cateCol = new TableColumn<ClothList, String>("???????????????????????????");
        TableColumn<ClothList, Integer> quantityCol = new TableColumn<ClothList, Integer>("???????????????");
        cateCol.setCellValueFactory(new PropertyValueFactory<ClothList,String>("category"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<ClothList,Integer>("quantity"));
        serviceCol.setCellValueFactory(new PropertyValueFactory<ClothList,String >("service"));

        clothListTable.getColumns().addAll(numberCol,serviceCol,cateCol, quantityCol) ;
        clothListTable.setItems(clothLists);
    }

    public void onSelectedOrder(Order order) throws IOException {
        selectedOrder = order ;
        nameLabel.setText(order.getName());
        phoneLabel.setText(order.getCus_phone());
        adsArea.setText(order.getAddress().toString());
        serviceLabel.setText(order.getService());
        statusLabel.setText(order.getStatus());
        payMethodLabel.setText(order.getPayMethod());
//        tagBtn.setDisable(false);
//        makePaperBtn.setDisable(false);
        statusOrderCombo.setDisable(false);
        changeStatusBtn.setDisable(false);

        statusOrderCombo.getSelectionModel().select(order.getStatus());


        if (order.getPayMethod().equals("???????????????????????????")){
            showQrBtn.setDisable(false);
        }
        else{
            showQrBtn.setDisable(true);
        }

        if(order.getIsMemOrder()==1){
            makePayBtn.setDisable(true);
            invBtn.setDisable(true);
            receiptBtn.setDisable(true);
        }


        // finace
        else if(order.getPayStatus()==0){
            payStatusLabel.setText("??????????????????????????????");
            makePayBtn.setDisable(false);
            invBtn.setDisable(false);
            receiptBtn.setDisable(true);
        }
        else{
            payStatusLabel.setText("????????????????????????");
            receiptBtn.setDisable(false);
            makePayBtn.setDisable(true);
            invBtn.setDisable(true);
        }
        //order
        if(order.getResponder().equals("?????????????????????????????????????????????") && order.getStatus().equals("?????????????????????????????????????????????")){
            statusOrderCombo.setDisable(true);
            changeStatusBtn.setDisable(true);
            addDeliverBtn.setDisable(true);
            acceptBtn.setDisable(false);
            cancelBtn.setDisable(false);
            makePayBtn.setDisable(true);
//            makePaperBtn.setDisable(true);
//            tagBtn.setDisable(true);
            invBtn.setDisable(true);
        }
        else if(order.getStatus().equals("????????????????????????????????????????????????")){
//            updateBtn.setDisable(false);
//            statusOrderCombo.setDisable(true);
//            changeStatusBtn.setDisable(true);
            addDeliverBtn.setDisable(true);
            acceptBtn.setDisable(true);
            cancelBtn.setDisable(true);
            makePayBtn.setDisable(true);
//            makePaperBtn.setDisable(true);
//            tagBtn.setDisable(true);
            invBtn.setDisable(true);
        }
        else if(order.getStatus().equals("??????????????????")){
//            updateBtn.setDisable(true);
            addDeliverBtn.setDisable(true);
            acceptBtn.setDisable(true);
            cancelBtn.setDisable(true);
            addClothListBtn.setDisable(false);
            makePayBtn.setDisable(true);
//            makePaperBtn.setDisable(true);
//            tagBtn.setDisable(true);
            invBtn.setDisable(true);
        }
        else if(order.getStatus().equals("???????????????????????????") ||order.getStatus().equals("??????????????????") ){
//            updateBtn.setDisable(true);
            addDeliverBtn.setDisable(true);
            statusOrderCombo.setDisable(true);
            changeStatusBtn.setDisable(true);
            acceptBtn.setDisable(true);
            cancelBtn.setDisable(true);
            addClothListBtn.setDisable(true);
            makePayBtn.setDisable(true);
//            makePaperBtn.setDisable(true);
//            tagBtn.setDisable(true);
            invBtn.setDisable(true);
            receiptBtn.setDisable(false);
            makePayBtn.setDisable(true);
            if(order.getStatus().equals("??????????????????")){
                receiptBtn.setDisable(true);
            }
        }
        else{
//            updateBtn.setDisable(false);
            if(order.getDeliver().equals("?????????????????????????????????????????????")){
                addDeliverBtn.setDisable(false);
            }
            else{
                addDeliverBtn.setDisable(true);
            }
            acceptBtn.setDisable(true);
            cancelBtn.setDisable(true);
            addClothListBtn.setDisable(false);
//            makePaperBtn.setDisable(false);
//            tagBtn.setDisable(false);
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

//    public void onClickUpdate(ActionEvent event) throws IOException {
//        if(selectedOrder != null){
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("????????????????????????????????????????????????");
//            alert.setHeaderText("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
//            ButtonType cancel = new ButtonType("??????????????????");
//            alert.getButtonTypes().add(cancel);
//            Optional<ButtonType> option = alert.showAndWait();
//            if(option.get() == null){
//
//            }
//            else if (option.get() == ButtonType.OK) {
//                OrderApiDataSource.updateOrderStatus(selectedOrder.getId());
//                root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/OrderListView.fxml"));
//                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//                scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//            } else if (option.get() == cancel) {
//
//            } else {
//            }
//        }
//    }

    public void onClickAccept(ActionEvent event) throws IOException {
        if(selectedOrder != null){
            if(OrderApiDataSource.confirmOrder(selectedOrder.getId())){
                pushAlertWarning("??????????????????????????????????????????????????????", Alert.AlertType.INFORMATION);
                root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/OrderListView.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else{
                pushAlertWarning("???????????????????????????????????????????????????????????????", Alert.AlertType.INFORMATION);
            }
        }
    }

    public void onClickCancel(ActionEvent event) throws IOException {
        if(selectedOrder != null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("????????????????????????????????????");
            alert.setHeaderText("????????????????????????????????????????????????????????????????????????????");
            ButtonType cancel = new ButtonType("??????????????????");
            alert.getButtonTypes().add(cancel);
            Optional<ButtonType> option = alert.showAndWait();
            if(option.get() == null){

            }
            else if (option.get() == ButtonType.OK) {
                if(selectedOrder.getStatus().equals("?????????????????????????????????????????????")){
                    if(OrderApiDataSource.cancelOrder(selectedOrder.getId())){
                        pushAlert("??????????????????????????????????????????????????????", Alert.AlertType.INFORMATION);
                        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/OrderListView.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                    else{
                        pushAlert("???????????????????????????????????????????????????????????????", Alert.AlertType.ERROR);
                    }
                }
//                        onClickAnchor();
            } else if (option.get() == ButtonType.CANCEL) {
            } else {
            }
        }
    }

    public void onClickShowQr() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().dynamicQR().creditTransfer().mobileNumber(EmployeeApiDataSource.getOwnerQR()).amount(new BigDecimal(totalField.getText())).build();
        File file = new File("qr.png");
        qr.draw(500,500,file);
        StackPane secondaryLayout = new StackPane();
        Scene secondScene = new Scene(secondaryLayout, 500, 500);

        Image image = new Image(file.toURI().toString());
        secondaryLayout.getChildren().add(new ImageView(image));
        Stage newWindow = new Stage();
        newWindow.setTitle("QR-Promtpay");
        newWindow.setScene(secondScene);
        newWindow.show();

        newWindow.setOnHiding( aevent -> {
            file.delete();
        });
    }



    public void onClickInv() throws IOException, DocumentException {
        if(selectedOrder!=null){
            WriterPDF.createINVPDF(selectedOrder);
        }
    }

    public void onClickReceipt() throws DocumentException, IOException {
        if(selectedOrder.getPayStatus()==0){
            pushAlert("???????????????????????????????????????????????????????????????????????????", Alert.AlertType.WARNING);
        }
        else{
            WriterPDF.createReceipt(selectedOrder);
        }
    }

    public void onClickTag(){

    }

    public void onClickPaper(){

    }

    public void onClickChangeStatus(ActionEvent event) throws IOException {
        if(selectedOrder != null && statusOrderCombo.getSelectionModel() != null){
            if(statusOrderCombo.getSelectionModel().getSelectedItem().equals("???????????????????????????")){
                if(selectedOrder.getPayStatus() == 0){
                    pushAlertWarning("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????", Alert.AlertType.WARNING);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("??????????????????????????????????????????????????????");
                    alert.setHeaderText("??????????????????????????????????????????????????????????????????????????????????????? "+ selectedOrder.getStatus() + " ???????????? " + statusOrderCombo.getSelectionModel().getSelectedItem().toString() + " ??????????????????????");
                    ButtonType cancel = new ButtonType("??????????????????");
                    alert.getButtonTypes().add(cancel);
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get() == ButtonType.OK){
                        if(OrderApiDataSource.updateOrderStatus(selectedOrder.getId(),
                                statusOrderCombo.getSelectionModel().getSelectedItem().toString())){
                            pushAlertWarning("?????????????????????????????????????????? " + statusOrderCombo.getSelectionModel().getSelectedItem().toString()+" ??????????????????", Alert.AlertType.INFORMATION);
                            root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/OrderListView.fxml"));
                            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                            scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                        }
                        else{
                            pushAlertWarning("?????????????????????????????????????????????????????????", Alert.AlertType.ERROR);
                        }
                    }
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("??????????????????????????????????????????????????????");
                alert.setHeaderText("??????????????????????????????????????????????????????????????????????????????????????? "+ selectedOrder.getStatus() + " ???????????? " + statusOrderCombo.getSelectionModel().getSelectedItem().toString() + " ??????????????????????");
                ButtonType cancel = new ButtonType("??????????????????");
                alert.getButtonTypes().add(cancel);
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get() == ButtonType.OK){
                    if(OrderApiDataSource.updateOrderStatus(selectedOrder.getId(),
                            statusOrderCombo.getSelectionModel().getSelectedItem().toString())){
                        pushAlertWarning("?????????????????????????????????????????? " + statusOrderCombo.getSelectionModel().getSelectedItem().toString()+" ??????????????????", Alert.AlertType.INFORMATION);
                        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/OrderListView.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                    else{
                        pushAlertWarning("?????????????????????????????????????????????????????????", Alert.AlertType.ERROR);
                    }
                }
            }
        }
    }

    public void addClothList(ActionEvent event) throws IOException {
        if(selectedOrder == null){
        }
        else if(selectedOrder.getStatus().equals("??????????????????")){
            Button b = (Button) event.getSource();
            Stage stage = (Stage) b.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/th/ac/ku/mylaundry/AddClothListAppView.fxml"));
            stage.setScene(new Scene(loader.load(), 1366, 768));
            AddClothListAppController addClothListAppController = loader.getController();
            addClothListAppController.setOrder(selectedOrder);
            stage.show();
        }

//        else if(selectedOrder != null && selectedOrder.getStatus().equals("??????????????????")){
//            Button b = (Button) event.getSource();
//            Stage stage = (Stage) b.getScene().getWindow();
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/th/ac/ku/mylaundry/AddClothListAppView.fxml"));
//            stage.setScene(new Scene(loader.load(), 1366, 768));
//            AddClothListAppController addClothListAppController = loader.getController();
//            addClothListAppController.setOrder(selectedOrder);
//            stage.show();
//        }

//        else {
//            Button b = (Button) event.getSource();
//            Stage stage = (Stage) b.getScene().getWindow();
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/th/ac/ku/mylaundry/editOrderView.fxml"));
//            stage.setScene(new Scene(loader.load(), 1366, 768));
//            EditOrderController editOrderController = loader.getController();
//            editOrderController.setOrder(selectedOrder);
//            stage.show();
//        }

    }

    public void onClickAddDeliver(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/deliListView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onClickMakePay(ActionEvent event) throws IOException {
        if(selectedOrder != null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("????????????????????????");
            alert.setHeaderText("??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
            ButtonType cancel = new ButtonType("??????????????????");
            alert.getButtonTypes().add(cancel);
            Optional<ButtonType> option = alert.showAndWait();
            if(option.get() == null){

            }
            else if (option.get() == ButtonType.OK) {
                OrderApiDataSource.payMoney(selectedOrder.getId());
                pushAlertWarning("??????????????????????????????????????????", Alert.AlertType.CONFIRMATION);
                root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/OrderListView.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else if (option.get() == cancel) {

            } else {
            }
        }
    }


    public void pushAlertWarning(String message, Alert.AlertType alertType){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(alertType);
        a.setContentText(message);
        a.show();
    }

    public void onClickRefresh(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/OrderListView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
