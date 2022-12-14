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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import th.ac.ku.mylaundry.model.*;
import th.ac.ku.mylaundry.service.*;

import javax.imageio.ImageIO;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;




public class NewOrderController extends Navigator {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button  showCleanBtn, showCleanIronBtn, showDryCleanBtn, showIronBtn,
            addCateBtn, delCateBtn, showQrBtn, makeInvBtn, makeReceiptBtn, makeTagBtn, makePayBtn, makeOrderBtn ;
    @FXML
    TableView categoryTable, clothListTable;
    @FXML
    ComboBox telCombo, payCombo, timeCombo;
    @FXML
    TextField cateSearchField, subTotalField, deliveryPriceField, taxField, totalField ;
    @FXML
    Label serviceLabel, cateLabel, pricePerUnitLabel, priceCateLabel, serviceTypeLabel, memBalanceLabel, quantityLabel, nameLabel ;
    @FXML
    Spinner quantitySpinner ;
    @FXML
    CheckBox deliCheck ;
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

    ArrayList<Cloth> onShowClothList ;
    ArrayList<ClothList> cartClothList ;
    ObservableList<Cloth> onShowClothObserve ;
    ObservableList<ClothList> clothListObservableList ;
    ArrayList<ServiceRate> serviceRatesData ;

    Integer selectService ;
    DecimalFormat f = new DecimalFormat("#0.00");
    Cloth selectCloth ;
    Customer selectCustomer ;
    String selectPayMethod ;
    ClothList selectClothList ;
    boolean makeOrder ;

    String payMethodList[] = {"??????????????????","???????????????????????????"};
    String timeList[] =  {"????????????????????????","????????????????????????","????????????????????????"} ;

    public void initialize() throws IOException {
        serviceRatesData = ServiceRateApiDataSource.getServiceRate();
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
        showQrBtn.setDisable(true);
        shopNameLabel.setText(LaundryApiDataSource.getLaundryName(1).toString());
        initSpinner();
//        timeCombo.getItems().addAll(timeList);
        adsArea.setDisable(true);
        deliDatePicker.setDisable(true);
        addCateBtn.setDisable(true);
        delCateBtn.setDisable(true);
        makeInvBtn.setDisable(true);
        showQrBtn.setDisable(true);
        timeCombo.setDisable(true);
        makeReceiptBtn.setDisable(true);
//        makeTagBtn.setDisable(true);
        makePayBtn.setDisable(true);
        quantitySpinner.setDisable(true);
        payCombo.getItems().addAll(payMethodList);
        initClothList();
        ObservableList<String> customers = FXCollections.observableList(getCusPhone());
        FilteredList<String> filteredItems = new FilteredList<String>(customers);
        telCombo.getEditor().textProperty().addListener(new InputFilter(telCombo,filteredItems,false));
        telCombo.setItems(filteredItems);
        deliDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today.plusDays(1)) < 0 );
            }
        });
        deliDatePicker.setOnAction(event -> {
            ArrayList<Boolean> art = DeliveryTimeApiDataSource.getAvailableInDateTime(deliDatePicker.getValue().toString());
            timeCombo.getItems().clear();
            if(art.get(0)){
                timeCombo.getItems().add("????????????????????????");
            }
            if(art.get(1)){
                timeCombo.getItems().add("????????????????????????");

            }
            if(art.get(2)){
                timeCombo.getItems().add("????????????????????????");
            }
        });



        deliCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    addDeliCharge();
                    updatePrice();
                    adsArea.setDisable(false);
                    deliDatePicker.setDisable(false);
                    timeCombo.setDisable(false);
                }
                else{
                    removeDelCharge();
                    updatePrice();
                    adsArea.setDisable(true);
                    timeCombo.setDisable(true);
                    deliDatePicker.setDisable(true);
                }
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
        telCombo.setOnAction(event -> {
            if(telCombo.getSelectionModel().isEmpty()){

            }
            if(!telCombo.getSelectionModel().isEmpty()){
                if(Validator.isPhoneNumber(telCombo.getSelectionModel().getSelectedItem().toString())){
                    selectCustomer = CustomerApiDataSource.searchCustomer(telCombo.getSelectionModel().getSelectedItem().toString());
                    if(selectCustomer != null){
                        showCustomerData(selectCustomer) ;
                    }
                }
            }
        });
        payCombo.setOnAction(event -> {
            if(payCombo.getSelectionModel().isEmpty()){

            }
            else if(payCombo.getSelectionModel().getSelectedItem().toString().equals("??????????????????")){
                selectPayMethod = "??????????????????" ;
                cartClothList = new ArrayList<>();
                total = 0;
                updateClothListTable();
                clothListTable.refresh();
                if(selectCustomer.getMemService().equals("???????????????&??????????????????")) {
                    showCleanService();
                }
                else if(selectCustomer.getMemService().equals("???????????????")){
                    showCleanService();
                    showDryCleanBtn.setDisable(true);
                    showIronBtn.setDisable(true);
                    showCleanIronBtn.setDisable(true);
                }
                else if(selectCustomer.getMemService().equals("??????????????????")){
                    showCleanIronService();
                    showDryCleanBtn.setDisable(true);
                    showIronBtn.setDisable(true);
                    showCleanBtn.setDisable(true);
                }
                else if(selectCustomer.getMemService().equals("?????????")){
                    showIronService();
                    showDryCleanBtn.setDisable(true);
                    showCleanIronBtn.setDisable(true);
                    showCleanBtn.setDisable(true);
                }
                else if(selectCustomer.getMemService().equals("?????????????????????")){
                    dryCleanService();
                    showCleanBtn.setDisable(true);
                    showCleanIronBtn.setDisable(true);
                    showCleanIronBtn.setDisable(true);
                }
            }
            else if(payCombo.getSelectionModel().getSelectedItem().toString().equals("??????????????????")){
                selectPayMethod = "??????????????????" ;
                showCleanBtn.setDisable(false);
                showDryCleanBtn.setDisable(false);
                showCleanIronBtn.setDisable(false);
                showIronBtn.setDisable(false);
                showQrBtn.setDisable(true);
                showCleanService();

            }
            else if(payCombo.getSelectionModel().getSelectedItem().toString().equals("???????????????????????????")){
                selectPayMethod = "???????????????????????????" ;
                showCleanBtn.setDisable(false);
                showDryCleanBtn.setDisable(false);
                showCleanIronBtn.setDisable(false);
                showIronBtn.setDisable(false);
                showQrBtn.setDisable(false);
                showCleanService();
            }
        });
    }


    public void addDeliCharge(){
        int n = showCartQuantity();
        if(n <= 15){
            deliCharge = 15 ;
        }
        else if(n > 15 && n <= 30){
            deliCharge = 25 ;
        }
        else if(n > 30 && n <= 50){
            deliCharge = 30  ;
        }
        else if(n > 50){
           deliCharge = 0 ;
        }
    }
    public void removeDelCharge(){
        deliCharge = 0 ;
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
        TableColumn<ClothList, String> serviceCol = new TableColumn<>("??????????????????");
        TableColumn<ClothList, String> cateCol = new TableColumn<ClothList, String>("???????????????????????????");
        TableColumn<ClothList, Integer> quantityCol = new TableColumn<ClothList, Integer>("???????????????");
        cateCol.setCellValueFactory(new PropertyValueFactory<ClothList,String>("category"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<ClothList,Integer>("quantity"));
        serviceCol.setCellValueFactory(new PropertyValueFactory<ClothList,String >("service"));

        clothListTable.getColumns().addAll(numberCol,serviceCol,cateCol, quantityCol) ;
//        clothListTable.setItems(sortedList);
    }
    public void initSpinner(){
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0));
    }
    public void updateSpinnerValue(int n){
        quantitySpinner.getValueFactory().setValue(n);
    }

    public ArrayList<String> getCusPhone() throws IOException {
        ArrayList<String> phones = new ArrayList<>();
        ArrayList<Customer> customers = CustomerApiDataSource.getCustomers();
        for (Customer c:customers) {
            phones.add(c.getPhone());
        }
        return phones;
    }

    public void showCustomerData(Customer customer){
        nameLabel.setText(customer.getName());
        if(customer.getIsMembership() == 0){
            serviceTypeLabel.setText("-");
            memBalanceLabel.setText("-");
            payCombo.getItems().clear();
            payCombo.getItems().addAll(payMethodList);
            if(CustomerApiDataSource.getCustomerAddress(customer.getId()) != null){
                adsArea.setText(CustomerApiDataSource.getCustomerAddress(customer.getId()).getuCode());
            }
        }
        else{
            payCombo.getItems().add("??????????????????") ;
            serviceTypeLabel.setText(customer.getMemService());
            memBalanceLabel.setText(customer.getMemCredit().toString());
            if(CustomerApiDataSource.getCustomerAddress(customer.getId()) != null){
                adsArea.setText(CustomerApiDataSource.getCustomerAddress(customer.getId()).getuCode());
            }
        }
    }




    public void showCategoryTable(SortedList<Cloth> sortedList){
        categoryTable.getColumns().clear();
        TableColumn idCol = new TableColumn("????????????");
        TableColumn<Customer, String> cateCol = new TableColumn<Customer, String>("???????????????????????????");
        TableColumn<Customer, Double> addOnPrice = new TableColumn<Customer, Double>("????????????");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        cateCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("clothType"));
        addOnPrice.setCellValueFactory(new PropertyValueFactory<Customer,Double>("price"));


        categoryTable.getColumns().addAll(idCol,cateCol,addOnPrice) ;
        categoryTable.setItems(sortedList);
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
            serviceLabel.setText("???????????????");
        } else if(selectService == 2){
            serviceLabel.setText("??????????????????");
        } else if(selectService == 3){
            serviceLabel.setText("?????????????????????");
        }else if(selectService == 4){
            serviceLabel.setText("?????????");
        }
        pricePerUnitLabel.setText(String.valueOf(cloth.getPrice()));
    }


    // Button OnClick


    public void showCleanService(){
        if(quantitySpinner.getValue() != null){
            onClickPane();

        }
        if(selectPayMethod == null || selectPayMethod.equals("??????????????????") || selectPayMethod.equals("???????????????????????????")){
            showCleanBtn.setDisable(true);
            showCleanIronBtn.setDisable(false);
            showDryCleanBtn.setDisable(false);
            showIronBtn.setDisable(false);
        }
        else if (selectPayMethod.equals("??????????????????") && selectCustomer.getMemService().equals("???????????????&??????????????????")){
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
        onClickPane();

        if(selectPayMethod == null || selectPayMethod.equals("??????????????????") || selectPayMethod.equals("???????????????????????????")){
            showCleanBtn.setDisable(false);
            showCleanIronBtn.setDisable(true);
            showDryCleanBtn.setDisable(false);
            showIronBtn.setDisable(false);
        }
        else if (selectPayMethod.equals("??????????????????") && selectCustomer.getMemService().equals("???????????????&??????????????????")){
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
        onClickPane();

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
        onClickPane();

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
        else if(selectCustomer == null && selectClothList == null){
            pushAlertWarning("????????????????????????????????????????????????", Alert.AlertType.WARNING);
        }
        else if(selectPayMethod == null){
            pushAlertWarning("?????????????????????????????????????????????????????????????????????????????????", Alert.AlertType.WARNING);
        }
        else if(selectCloth != null && selectClothList == null){
            String service = "" ;
            if(!quantitySpinner.getValue().toString().equals("0")){
                if(selectService == 1){
                    service = "???????????????" ;
                }
                else if(selectService == 2){
                    service = "??????????????????" ;
                }
                else if(selectService == 3){
                    service = "?????????????????????" ;
                }
                else if(selectService == 4){
                    service = "?????????" ;
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

    public void onClickPane(){
        clearCategorySelect();
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

    public void updatePrice(){
        if(deliCheck.isSelected()){
            addDeliCharge();
        }
        double subtotal = total + deliCharge ;
        double tax = subtotal * 0.07 ;
        subtotal-=tax ;
        taxField.setText(f.format(tax));
        subTotalField.setText(f.format(subtotal));
        deliveryPriceField.setText(f.format(deliCharge));
        totalField.setText(f.format(subtotal+tax));
    }

    public int showCartQuantity(){
        int q = 0 ;
        for (ClothList c:cartClothList) {
            q+=c.getQuantity();
        }
        quantityLabel.setText(String.valueOf(q));
        return q;
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

    Scene start,game;

    public void showQr(ActionEvent event) throws IOException, WriterException {

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

    public void newCustomer(ActionEvent event) throws IOException {
        onClickCus(event);
    }

    public void registerMember(ActionEvent event) throws IOException {
        onClickCus(event);
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

    public void makeOrder() throws IOException {
        if(cartClothList.size() <= 0){
            pushAlertWarning("?????????????????????????????????????????????????????????", Alert.AlertType.WARNING);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("????????????????????????????????????");
            alert.setHeaderText("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
            ButtonType cancel = new ButtonType("??????????????????");
            alert.getButtonTypes().add(cancel);
            Optional<ButtonType> option = alert.showAndWait();
            if(option.get() == ButtonType.OK){
                String mainService = getMostService() ;
                // Order With Member Ship
                if(selectPayMethod.equals("??????????????????")){
                    if(showCartQuantity() > selectCustomer.getMemCredit()){
                        pushAlertWarning("???????????????????????????????????????????????????????????? ????????????????????????????????????????????????????????????????????????????????????????????????", Alert.AlertType.ERROR);
                    }
                    else{
                        if(!deliCheck.isSelected()){
                            if(OrderApiDataSource.payMember(selectCustomer.getId(),showCartQuantity())){
                                orderId = OrderApiDataSource.addOrderWithNoDeli(selectCustomer.getPhone(),new Order(mainService,selectPayMethod,
                                        "order in",1,1), cartClothList);
                            }
                            if(orderId != 0){
                                pushAlertWarning("??????????????????????????????????????????", Alert.AlertType.INFORMATION);
                                onMakeOrderComplete();
                                updateBalance();
                                makeOrder = true ;
                            }
                            else{
                                pushAlertWarning("???????????????????????????????????????????????????", Alert.AlertType.ERROR);
                            }
                        }
                        else{
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            if(adsArea.getText() != null && deliDatePicker.getValue() != null && timeCombo.getSelectionModel() != null){
                                if(OrderApiDataSource.payMember(selectCustomer.getId(),showCartQuantity())){
                                    orderId = OrderApiDataSource.addOrderWithDeli(selectCustomer.getPhone(),new Order(mainService,
                                            deliDatePicker.getValue().format(formatter),timeCombo.getSelectionModel().getSelectedItem().toString(),
                                            adsArea.getText(),1,payCombo.getSelectionModel().getSelectedItem().toString(),
                                            1),cartClothList);
                                }
                                if(orderId != 0){
                                    pushAlertWarning("??????????????????????????????????????????", Alert.AlertType.INFORMATION);
                                    onMakeOrderComplete();
                                    updateBalance();
                                    makeOrder = true ;
                                }
                                else{
                                    pushAlertWarning("???????????????????????????????????????????????????", Alert.AlertType.ERROR);
                                }
                            }
                            else{
                                pushAlertWarning("???????????????????????????????????????????????????????????????????????????", Alert.AlertType.WARNING);
                            }
                        }
                    }
                }
                // Order no MemberShip
                else{
                    // if deli
                    if(!deliCheck.isSelected()){
                        orderId = OrderApiDataSource.addOrderWithNoDeli(selectCustomer.getPhone(),new Order(mainService,selectPayMethod,
                                "order in",0,0), cartClothList);
                        if(orderId != 0){
                            pushAlertWarning("??????????????????????????????????????????", Alert.AlertType.INFORMATION);
                            onMakeOrderComplete();
                            makeOrder = true ;
                        }
                        else{
                            pushAlertWarning("???????????????????????????????????????????????????", Alert.AlertType.ERROR);
                        }
                    }
                    else{
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        if(!adsArea.getText().equals("") && deliDatePicker.getValue() != null && timeCombo.getSelectionModel().getSelectedItem() != null){
                            orderId = OrderApiDataSource.addOrderWithDeli(selectCustomer.getPhone(),new Order(mainService,
                                    deliDatePicker.getValue().format(formatter),timeCombo.getSelectionModel().getSelectedItem().toString(),
                                    adsArea.getText(),0,payCombo.getSelectionModel().getSelectedItem().toString(),
                                    0),cartClothList);
                            if(orderId != 0){
                                pushAlertWarning("??????????????????????????????????????????", Alert.AlertType.INFORMATION);
                                onMakeOrderComplete();
                                makeOrder = true ;
                            }
                            else{
                                pushAlertWarning("???????????????????????????????????????????????????", Alert.AlertType.ERROR);
                            }
                        }
                        else{
                            pushAlertWarning("???????????????????????????????????????????????????????????????????????????", Alert.AlertType.WARNING);
                        }
                    }
                }
            }
        }
    }

    public void updateBalance(){
        selectCustomer = CustomerApiDataSource.searchCustomer(telCombo.getSelectionModel().getSelectedItem().toString());
        memBalanceLabel.setText(String.valueOf(selectCustomer.getMemCredit()));
    }

    public void onMakeOrderComplete(){
        makePayBtn.setDisable(false);
//        makeTagBtn.setDisable(false);
        makeInvBtn.setDisable(false);
        makeReceiptBtn.setDisable(false);
        if(OrderApiDataSource.getOrder(orderId).getIsMemOrder() == 1){
            makePayBtn.setDisable(true);
//            makeTagBtn.setDisable(true);
            makeInvBtn.setDisable(true);
            makeReceiptBtn.setDisable(true);
        }
        telCombo.setDisable(true);
        payCombo.setDisable(true);
        deliCheck.setDisable(true);
        deliDatePicker.setDisable(true);
        timeCombo.setDisable(true);
        makeOrderBtn.setDisable(true);
        categoryTable.setDisable(true);
        addCateBtn.setDisable(true);
        if(payCombo.getSelectionModel().getSelectedItem().toString().equals("???????????????????????????")){
            showQrBtn.setDisable(false);
        }
    }

    public void printInv() throws DocumentException, IOException {
        if(makeOrder){
            if(!makePayBtn.isDisable()){
                WriterPDF.createINVPDF(OrderApiDataSource.getOrder(orderId));
            }
        }
    }

    public void printReceipt() throws DocumentException, IOException {
        if(makeOrder){
            if(makePayBtn.isDisable()){
                if(orderId != 0){
                    WriterPDF.createReceipt(OrderApiDataSource.getOrder(orderId));
                }
            }
        }
    }

    public void printTag(){

    }

    public void payMoney(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("???????????????????????????????????????????????????");
        alert.setHeaderText("????????????????????????????????????????????????????????????????????????????????????");
        ButtonType cancel = new ButtonType("??????????????????");
        alert.getButtonTypes().add(cancel);
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == ButtonType.OK){
            if(OrderApiDataSource.payMoney(orderId)){
                makePayBtn.setDisable(true);
                pushAlertWarning("??????????????????????????????????????????", Alert.AlertType.INFORMATION);
            }
            else{
                pushAlertWarning("???????????????????????????????????????????????????", Alert.AlertType.ERROR);
            }
        }
    }


    public void onReset(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/newOrderView.fxml"));
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
