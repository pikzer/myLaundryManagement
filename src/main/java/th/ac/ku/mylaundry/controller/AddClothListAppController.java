package th.ac.ku.mylaundry.controller;

import javafx.application.Platform;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import th.ac.ku.mylaundry.model.*;
import th.ac.ku.mylaundry.service.*;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class AddClothListAppController extends Navigator{


    @FXML
    Button showCleanBtn, showCleanIronBtn, showDryCleanBtn, showIronBtn,
            addCateBtn, delCateBtn, makeInvBtn, makeReceiptBtn, makeTagBtn, makePayBtn, makeOrderBtn ;
    @FXML
    TableView categoryTable, clothListTable;

    @FXML
    ComboBox timeCombo;

    @FXML
    TextField cateSearchField, subTotalField, deliveryPriceField, taxField, totalField ;

    @FXML
    Label serviceLabel, cateLabel, pricePerUnitLabel, priceCateLabel, serviceTypeLabel, memBalanceLabel, quantityLabel, nameLabel, orderNameLabel,orderServiceLabel,phoneLabel,payMethodLabel ;

    @FXML
    Spinner quantitySpinner ;

    @FXML
    TextArea adsArea;

    @FXML
    DatePicker  deliDatePicker;

    @FXML
    Pane pane;

    double total ;
    double vat ;
    double deliCharge ;
    int orderId ;

    int selectService ;
    DecimalFormat f = new DecimalFormat("#0.00");
    Cloth selectCloth ;
    Customer selectCustomer ;
    String selectPayMethod ;
    ClothList selectClothList ;
    boolean makeOrder ;

    Order order ;
    Customer customer ;


    String timeList[] =  {"ช่วงเช้า","ช่วงบ่าย","ช่วงเย็น"} ;

    ArrayList<Cloth> onShowClothList ;
    ArrayList<ClothList> cartClothList ;
    ObservableList<Cloth> onShowClothObserve ;
    ObservableList<ClothList> clothListObservableList ;
    ArrayList<ServiceRate> serviceRatesData ;

    public void setOrder(Order selectOrder){
        this.order = selectOrder ;
        customer = CustomerApiDataSource.searchCustomer(order.getCus_phone());
        selectCustomer = customer ;
    }

    public void initialize() throws IOException {
        serviceRatesData = ServiceRateApiDataSource.getServiceRate();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                total = 0 ;
                orderId = 0 ;
                deliCharge = 0;
                vat = 0;
                makeOrder = false ;
                cartClothList = new ArrayList<>();
                showCleanBtn.setDisable(true);
                showCleanService();
                quantitySpinner.setEditable(true);
                quantitySpinner.setDisable(true);

                initSpinner();
//        timeCombo.getItems().addAll(timeList);
                adsArea.setDisable(true);
                deliDatePicker.setDisable(false);
                addCateBtn.setDisable(true);
                delCateBtn.setDisable(true);
                makeInvBtn.setDisable(true);
                timeCombo.setDisable(false);
                makeReceiptBtn.setDisable(true);
                makeTagBtn.setDisable(true);
                makePayBtn.setDisable(true);
                quantitySpinner.setDisable(true);
                initClothList();
                selectPayMethod = order.getPayMethod();
                deliDatePicker.setDayCellFactory(picker -> new DateCell() {
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        LocalDate today = LocalDate.now();
                        setDisable(empty || date.compareTo(LocalDate.parse(order.getPickDate()).plusDays(1)) < 0 );
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


                categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue != null){
                        showSelectedCategory((Cloth) newValue) ;
                    }
                });
                clothListTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue != null){
                        showSelectClothList((ClothList) newValue);
                    }
                });


                quantitySpinner.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
                    if(newValue != null && selectCloth != null){
                        priceCateLabel.setText(String.valueOf(selectCloth.getPrice() * Integer.parseInt(newValue.toString())));
                    } else if (selectClothList != null && selectCloth == null) {
                        priceCateLabel.setText(String.valueOf(selectClothList.getPricePerUnit() * Integer.parseInt(newValue.toString())));
                    }
                }));
                showOrder();

            }
        });


    }

    public void initSpinner(){
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0));
    }
    public void updateSpinnerValue(int n){
        quantitySpinner.getValueFactory().setValue(n);
    }

    public void initClothList(){
        clothListTable.getColumns().clear();
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
//        clothListTable.setItems(sortedList);
    }

    public void showSelectedCategory(Cloth cloth){
        selectClothList = null ;
        clothListTable.getSelectionModel().clearSelection();
        priceCateLabel.setText("0.00");
        quantitySpinner.setDisable(false);
        cateLabel.setText(cloth.getClothType());
        addCateBtn.setDisable(false);
        updateSpinnerValue(0);
        selectCloth = cloth ;
        if(selectService == 1){
            serviceLabel.setText("ซักอบ");
        } else if(selectService == 2){
            serviceLabel.setText("ซักรีด");
        } else if(selectService == 3){
            serviceLabel.setText("ซักแห้ง");
        }else if(selectService == 4){
            serviceLabel.setText("รีด");
        }
        pricePerUnitLabel.setText(String.valueOf(cloth.getPrice()));
    }

    public void showOrder(){
        orderNameLabel.setText(order.getName());
        orderServiceLabel.setText(order.getService());
        phoneLabel.setText(order.getCus_phone());
        nameLabel.setText(customer.getName());
        payMethodLabel.setText(order.getPayMethod());
        if(customer.getIsMembership() ==1 ){
            serviceTypeLabel.setText(customer.getMemService());
            memBalanceLabel.setText(String.valueOf(customer.getMemCredit()));
        }
        adsArea.setText(order.getAddress());
    }

    public void showSelectClothList(ClothList clothList){
        if(makeOrder == true){

        }
        else{
            selectCloth = null ;
            selectClothList = clothList ;
            delCateBtn.setDisable(false);
            addCateBtn.setDisable(false);
            categoryTable.getSelectionModel().clearSelection();
            quantitySpinner.setDisable(false);
            updateSpinnerValue(clothList.getQuantity());
            cateLabel.setText(clothList.getCategory());
            serviceLabel.setText(clothList.getService());
            pricePerUnitLabel.setText(f.format(clothList.getPricePerUnit()));
        }

    }

    public void updatePrice(){
        addDeliCharge();
        double subtotal = total + deliCharge ;
        double tax = subtotal * 0.07 ;
        subtotal-=tax ;
        taxField.setText(f.format(tax));
        subTotalField.setText(f.format(subtotal));
        deliveryPriceField.setText(f.format(deliCharge));
        totalField.setText(f.format(subtotal+tax));
    }

    public void addDeliCharge(){
        int n = showCartQuantity();
        if(n <= 15){
            deliCharge = 30 ;
        }
        else if(n > 15 && n <= 30){
            deliCharge = 50 ;
        }
        else if(n > 30 && n <= 50){
            deliCharge = 60  ;
        }
        else if(n > 50){
            deliCharge = 0 ;
        }
    }


    public void showCategoryTable(SortedList<Cloth> sortedList){
        categoryTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Customer, String> cateCol = new TableColumn<Customer, String>("ประเภทผ้า");
        TableColumn<Customer, Double> addOnPrice = new TableColumn<Customer, Double>("ราคา");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        cateCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("clothType"));
        addOnPrice.setCellValueFactory(new PropertyValueFactory<Customer,Double>("price"));


        categoryTable.getColumns().addAll(idCol,cateCol,addOnPrice) ;
        categoryTable.setItems(sortedList);
    }
    public int showCartQuantity(){
        int q = 0 ;
        for (ClothList c:cartClothList) {
            q+=c.getQuantity();
        }
        quantityLabel.setText(String.valueOf(q));
        return q;
    }


    public void showCleanService(){
        if(selectPayMethod == null || selectPayMethod.equals("เงินสด") || selectPayMethod.equals("พร้อมเพย์")){
            showCleanBtn.setDisable(true);
            showCleanIronBtn.setDisable(false);
            showDryCleanBtn.setDisable(false);
            showIronBtn.setDisable(false);
        }
        else if (selectPayMethod.equals("สมาชิก") && selectCustomer.getMemService().equals("ซักอบ&ซักรีด")){
            showCleanBtn.setDisable(true);
            showCleanIronBtn.setDisable(false);
            showDryCleanBtn.setDisable(true);
            showIronBtn.setDisable(true);
        }
        pane.setStyle("-fx-background-color: chocolate;");
        selectService = 1 ;
        onShowClothList = OrderApiDataSource.getCloth(1);
        onShowClothObserve = FXCollections.observableList(onShowClothList);
        FilteredList<Cloth> cleanIronFiltered = new FilteredList<>(onShowClothObserve);
        cateSearchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            cleanIronFiltered.setPredicate(category -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(category.getClothType().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Cloth> cateSortedList = new SortedList<>(cleanIronFiltered);
        cateSortedList.comparatorProperty().bind(categoryTable.comparatorProperty());
        showCategoryTable(cateSortedList);
    }

    public void showCleanIronService(){
        if( selectPayMethod.equals("เงินสด") || selectPayMethod.equals("พร้อมเพย์")){
            showCleanBtn.setDisable(false);
            showCleanIronBtn.setDisable(true);
            showDryCleanBtn.setDisable(false);
            showIronBtn.setDisable(false);
        }
        else if (order.getPayMethod().equals("สมาชิก") && customer.getMemService().equals("ซักอบ&ซักรีด")){
            showCleanBtn.setDisable(false);
            showCleanIronBtn.setDisable(true);
            showDryCleanBtn.setDisable(true);
            showIronBtn.setDisable(true);
        }
        subTotalField.setText("0.00");
        deliveryPriceField.setText("0.00");
        taxField.setText("0.00");
        totalField.setText("0.00");
        pane.setStyle("-fx-background-color: darkseagreen;");
        selectService = 2 ;
        onShowClothList = OrderApiDataSource.getCloth(2);
        onShowClothObserve = FXCollections.observableList(onShowClothList);
        FilteredList<Cloth> cleanIronFiltered = new FilteredList<>(onShowClothObserve);
        cateSearchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            cleanIronFiltered.setPredicate(category -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(category.getClothType().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Cloth> cateSortedList = new SortedList<>(cleanIronFiltered);
        cateSortedList.comparatorProperty().bind(categoryTable.comparatorProperty());
        showCategoryTable(cateSortedList);
    }

    public void dryCleanService(){
        showCleanBtn.setDisable(false);
        showCleanIronBtn.setDisable(false);
        showDryCleanBtn.setDisable(true);
        showIronBtn.setDisable(false);
        pane.setStyle("-fx-background-color:  lavender;");
        selectService = 3;
        onShowClothList = OrderApiDataSource.getCloth(3);
        onShowClothObserve = FXCollections.observableList(onShowClothList);
        FilteredList<Cloth> cleanIronFiltered = new FilteredList<>(onShowClothObserve);
        cateSearchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            cleanIronFiltered.setPredicate(category -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(category.getClothType().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Cloth> cateSortedList = new SortedList<>(cleanIronFiltered);
        cateSortedList.comparatorProperty().bind(categoryTable.comparatorProperty());
        showCategoryTable(cateSortedList);
    }

    public void showIronService(){
        showCleanBtn.setDisable(false);
        showCleanIronBtn.setDisable(false);
        showDryCleanBtn.setDisable(false);
        showIronBtn.setDisable(true);
        pane.setStyle("-fx-background-color: orangered  ;");
        selectService = 4 ;
        onShowClothList = OrderApiDataSource.getCloth(4);
        onShowClothObserve = FXCollections.observableList(onShowClothList);
        FilteredList<Cloth> cleanIronFiltered = new FilteredList<>(onShowClothObserve);
        cateSearchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            cleanIronFiltered.setPredicate(category -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(category.getClothType().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Cloth> cateSortedList = new SortedList<>(cleanIronFiltered);
        cateSortedList.comparatorProperty().bind(categoryTable.comparatorProperty());
        showCategoryTable(cateSortedList);
    }


    public void addToClothList(){
        if(selectCloth == null && selectClothList != null){
            for (int i = 0; i < cartClothList.size(); i++) {
                if(cartClothList.get(i).getService().equals(selectClothList.getService()) &&
                        cartClothList.get(i).getCategory().equals(selectClothList.getCategory())){
                    total -= selectClothList.getPricePerUnit() * cartClothList.get(i).getQuantity();
                    cartClothList.get(i).setQuantity(Integer.parseInt(quantitySpinner.getValue().toString()));
                    total += selectClothList.getPricePerUnit() * cartClothList.get(i).getQuantity();
                    showCartQuantity();
                    updatePrice();
                    clothListTable.refresh();
                    return;
                }
            }
        }
        else if(selectPayMethod == null){
            pushAlertWarning("กรุณาเลือกรูปแบบการชำระเงิน", Alert.AlertType.WARNING);
        }
        else if(selectCloth != null && selectClothList == null){
            String service = "" ;
            if(!quantitySpinner.getValue().toString().equals("0")){
                if(selectService == 1){
                    service = "ซักอบ" ;
                }
                else if(selectService == 2){
                    service = "ซักรีด" ;
                }
                else if(selectService == 3){
                    service = "ซักแห้ง" ;
                }
                else if(selectService == 4){
                    service = "รีด" ;
                }
                boolean isDuplicate = false;

                for(int i = 0 ; i < cartClothList.size();i++){
                    if(cartClothList.get(i).getCategory().equals(selectCloth.getClothType())
                            && (cartClothList.get(i).getService().equals(service))){
                        total -= selectCloth.getPrice() * cartClothList.get(i).getQuantity();
                        cartClothList.get(i).setQuantity(cartClothList.get(i).getQuantity()+
                                Integer.parseInt(quantitySpinner.getValue().toString()));
                        total += selectCloth.getPrice() * cartClothList.get(i).getQuantity();
                        isDuplicate = true ;
                        break;
                    }
                }
                if(!isDuplicate){
//                    cartClothList.add(new ClothList(service,selectCloth.getClothType(),
//                            Integer.valueOf(quantitySpinner.getValue().toString())),selectCloth.getPrice());
                    cartClothList.add(new ClothList(service,selectCloth.getClothType(),
                            Integer.valueOf(quantitySpinner.getValue().toString()),selectCloth.getPrice()));
                    total += selectCloth.getPrice() * Integer.valueOf(quantitySpinner.getValue().toString());
                }
                updateClothListTable();
            }
        }else{
            System.out.println(0);
        }
    }

    public void clearCategorySelect(){
        categoryTable.getSelectionModel().clearSelection();
        quantitySpinner.setDisable(true);
        cateLabel.setText("-");
        serviceLabel.setText("-");
        pricePerUnitLabel.setText("0.00");
        priceCateLabel.setText("0.00");
        updateSpinnerValue(0);
    }

    public void updateClothListTable(){
        addCateBtn.setDisable(true);
        clothListObservableList = FXCollections.observableList(cartClothList) ;
        clothListTable.setItems(clothListObservableList);
        clothListTable.refresh();
        clearCategorySelect();
        showCartQuantity();
        updatePrice();
    }
    public void delClothList(){
        if(selectClothList != null && selectCloth == null){
            for (int i = 0; i < cartClothList.size(); i++) {
                if(cartClothList.get(i).getService().equals(selectClothList.getService()) &&
                        cartClothList.get(i).getCategory().equals(selectClothList.getCategory())){
                    total -= selectClothList.getPricePerUnit() * cartClothList.get(i).getQuantity();
                    cartClothList.remove(i);
                    showCartQuantity();
                    updatePrice();
                    clothListTable.refresh();
                    onClickPane();
                    return;
                }
            }
        }
    }

    public void payMoney(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ยืนยันการชำระเงิน");
        alert.setHeaderText("คุณต้องการชำระเงินเลยหรือไม่");
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == null){

        }
        else if (option.get() == ButtonType.OK) {
            if(OrderApiDataSource.payMoney(order.getId())){
                makePayBtn.setDisable(true);
                pushAlertWarning("ชำระเงินสำเร็จ", Alert.AlertType.INFORMATION);
            }
            else{
                pushAlertWarning("ชำระเงินไม่สำเร็จ", Alert.AlertType.ERROR);
            }
        }
        else {

        }
    }

    public void printReceipt(){

    }

    public void printInv(){

    }

    public void addClothList() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(cartClothList.size() <= 0){
            pushAlertWarning("กรุณาเพิ่มรายการผ้า", Alert.AlertType.WARNING);
        }
        else if(deliDatePicker.getValue()!=null && !timeCombo.getSelectionModel().isEmpty()){
            if(OrderApiDataSource.addClothListApp(order,cartClothList,deliDatePicker.getValue().format(formatter),timeCombo.getSelectionModel().getSelectedItem().toString())){
                pushAlertWarning("เพิ่มรายการผ้าสำเร็จ", Alert.AlertType.INFORMATION);
                onMakeOrderComplete();
            }
            else {
                pushAlertWarning("เพิ่มรายการผ้าไม่สำเร็จ", Alert.AlertType.ERROR);
            }
        }
    }

    public void onMakeOrderComplete(){
        makePayBtn.setDisable(false);
        makeTagBtn.setDisable(false);
        makeInvBtn.setDisable(false);
        makeReceiptBtn.setDisable(false);
        deliDatePicker.setDisable(true);
        timeCombo.setDisable(true);
        makeOrderBtn.setDisable(true);
        categoryTable.setDisable(true);
        addCateBtn.setDisable(true);

    }

    public String getMostService(){
        if(selectCustomer != null){
            List<String> service = new ArrayList<>();
            for (ClothList c:cartClothList) {
                service.add(c.getService());
            }
            String mostRepeatedWord
                    = service.stream()
                    .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .max(Comparator.comparing(Map.Entry::getValue))
                    .get()
                    .getKey();
            return mostRepeatedWord;
        }
        return null;
    }

    public void printTag(){

    }


    public void onClickPane(){
        clearCategorySelect();
    }

    public void onClickBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/orderListView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void pushAlertWarning(String message, Alert.AlertType alertType){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(alertType);
        a.setContentText(message);
        a.show();
    }


}