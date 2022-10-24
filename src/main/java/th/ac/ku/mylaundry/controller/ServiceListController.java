package th.ac.ku.mylaundry.controller;

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
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Category;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.model.ServiceRate;
import th.ac.ku.mylaundry.service.ApiCall;
import th.ac.ku.mylaundry.service.ServiceRateApiDataSource;
import th.ac.ku.mylaundry.service.Validator;

import java.io.IOException;
import java.util.ArrayList;

public class ServiceListController extends Navigator{

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    @FXML
    Button delBtn, confirmBtn ;

    @FXML
    TableView cleanTable, cleanIronTable, dryCleanTable, ironTable ;

    @FXML
    TextField cleanBPField, cleanIronBPField, dryCleanBPField, ironBPField
            , cleanSearchField, cleanIronSearchField, dryCleanSearchField, ironSearchField
            , categoryField, priceField ;
    @FXML
    ComboBox serviceCombo ;



    ArrayList<ServiceRate> serviceRates = new ArrayList<>() ;
    ArrayList<Category> categoryArrayList ;
    ArrayList<Category> cleanCatArr ;
    ObservableList<Category> cleanCatObserve ;
    ArrayList<Category> cleanIronCatArr ;
    ObservableList<Category> cleanIronCatObserve;
    ArrayList<Category> dryCleanCatArr ;
    ObservableList<Category> dryCleanCatObserve;
    ArrayList<Category> ironCatArr ;
    ObservableList<Category> ironObserve ;

    Category selectedCat ;

    String serviceType[] = {"ซักอบ","ซักรีด","ซักแห้ง","รีด"} ;

    public void initialize() throws IOException {
        serviceCombo.getItems().clear();
        serviceCombo.getItems().addAll(serviceType) ;
        serviceRates = ServiceRateApiDataSource.getServiceRate() ;
        categoryArrayList = ServiceRateApiDataSource.getCategory();
        showBasePrice();
        categorySplitService();
        delBtn.setDisable(true);

        cleanCatObserve = FXCollections.observableList(cleanCatArr);
        FilteredList<Category> cleanFiltered = new FilteredList<>(cleanCatObserve);
        cleanSearchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            cleanFiltered.setPredicate(category -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(category.getClothType().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Category> cleanSortedList = new SortedList<>(cleanFiltered);
        cleanSortedList.comparatorProperty().bind(cleanTable.comparatorProperty());
        showTableClean(cleanSortedList);


//
//
        cleanIronCatObserve = FXCollections.observableList(cleanIronCatArr);
        FilteredList<Category> cleanIronFiltered = new FilteredList<>(cleanIronCatObserve);
        cleanIronSearchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
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
        SortedList<Category> cleanIronSortedList = new SortedList<>(cleanIronFiltered);
        cleanIronSortedList.comparatorProperty().bind(cleanIronTable.comparatorProperty());
        showTableIronClean(cleanIronSortedList);
//
        dryCleanCatObserve = FXCollections.observableList(dryCleanCatArr);
        FilteredList<Category> dryCleanFiltered = new FilteredList<>(dryCleanCatObserve);
        dryCleanSearchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            dryCleanFiltered.setPredicate(category -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(category.getClothType().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Category> dryCleanSortedList = new SortedList<>(dryCleanFiltered);
        dryCleanSortedList.comparatorProperty().bind(dryCleanTable.comparatorProperty());
        showTableDryClean(dryCleanSortedList);

        ironObserve = FXCollections.observableList(ironCatArr);
        FilteredList<Category> ironFiltered = new FilteredList<>(ironObserve);
        ironSearchField.textProperty().addListener((observableValue, newValue, oldValue) ->{
            ironFiltered.setPredicate(category -> {
                if(newValue == null || newValue.isEmpty() || oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                else if(category.getClothType().contains(newValue)){
                    return true;
                }
                return false;
            });
        });
        SortedList<Category> ironSortedList = new SortedList<>(ironFiltered);
        ironSortedList.comparatorProperty().bind(dryCleanTable.comparatorProperty());
        showTableIron(ironSortedList);

        cleanTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
//                clearTableSelect();
                showSelectedCategory((Category) newValue) ;
            }
        });
        cleanIronTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
//                clearTableSelect();
                showSelectedCategory((Category) newValue) ;
            }
        });
        dryCleanTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
//                clearTableSelect();
                showSelectedCategory((Category) newValue) ;
            }
        });
        ironTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
//                clearTableSelect();
                showSelectedCategory((Category) newValue) ;
            }
        });
    }


    public void showSelectedCategory(Category category){
        selectedCat = category ;
        delBtn.setDisable(false);
        categoryField.setText(category.getClothType());
        priceField.setText(String.valueOf(category.getAddOnPrice() +
                serviceRates.get(category.getService_rate_id()-1).getBasePrice()));
        serviceCombo.getSelectionModel().select(category.getService_rate_id()-1);
    }

    public void onClickAnchor(){
        cleanTable.getSelectionModel().clearSelection();
        cleanIronTable.getSelectionModel().clearSelection();
        dryCleanTable.getSelectionModel().clearSelection();
        ironTable.getSelectionModel().clearSelection();
        selectedCat = null ;
        categoryField.clear();
        priceField.clear();
        delBtn.setDisable(true);
        serviceCombo.getSelectionModel().clearSelection();
    }

    public void showBasePrice(){
        for (ServiceRate s:serviceRates) {
            if(s.getService().equals("ซักอบ")){
                cleanBPField.setText(String.valueOf(s.getBasePrice()));
            }
            else if(s.getService().equals("ซักรีด")){
                cleanIronBPField.setText(String.valueOf(s.getBasePrice()));
            }
            else if(s.getService().equals("ซักแห้ง")){
                dryCleanBPField.setText(String.valueOf(s.getBasePrice()));
            }
            else if(s.getService().equals("รีด")){
                ironBPField.setText(String.valueOf(s.getBasePrice()));
            }
        }
    }



    public void showTableClean(ObservableList<Category> categorySortedList){
        cleanTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Customer, String> cateCol = new TableColumn<Customer, String>("ประเภทผ้า");
        TableColumn<Customer, Double> addOnPrice = new TableColumn<Customer, Double>("ราคาเพิ่มเติม");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        cateCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("clothType"));
        addOnPrice.setCellValueFactory(new PropertyValueFactory<Customer,Double>("addOnPrice"));


        cleanTable.getColumns().addAll(idCol,cateCol,addOnPrice) ;
        cleanTable.setItems(categorySortedList);
    }

    public void showTableIronClean(SortedList<Category> SortedList){
        cleanIronTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Customer, String> cateCol = new TableColumn<Customer, String>("ประเภทผ้า");
        TableColumn<Customer, Double> addOnPrice = new TableColumn<Customer, Double>("ราคาเพิ่มเติม");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        cateCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("clothType"));
        addOnPrice.setCellValueFactory(new PropertyValueFactory<Customer,Double>("addOnPrice"));

        cleanIronTable.getColumns().addAll(idCol,cateCol,addOnPrice) ;
        cleanIronTable.setItems(SortedList);
    }

    public void showTableDryClean(SortedList<Category> SortedList){
        dryCleanTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Customer, String> cateCol = new TableColumn<Customer, String>("ประเภทผ้า");
        TableColumn<Customer, Double> addOnPrice = new TableColumn<Customer, Double>("ราคาเพิ่มเติม");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        cateCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("clothType"));
        addOnPrice.setCellValueFactory(new PropertyValueFactory<Customer,Double>("addOnPrice"));

        dryCleanTable.getColumns().addAll(idCol,cateCol,addOnPrice) ;
        dryCleanTable.setItems(SortedList);
    }

    public void showTableIron(SortedList<Category> SortedList){
        ironTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Customer, String> cateCol = new TableColumn<Customer, String>("ประเภทผ้า");
        TableColumn<Customer, Double> addOnPrice = new TableColumn<Customer, Double>("ราคาเพิ่มเติม");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        cateCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("clothType"));
        addOnPrice.setCellValueFactory(new PropertyValueFactory<Customer,Double>("addOnPrice"));

        ironTable.getColumns().addAll(idCol,cateCol,addOnPrice) ;
        ironTable.setItems(SortedList);
    }

    public void categorySplitService() {
        cleanCatArr = new ArrayList<>();
        cleanIronCatArr = new ArrayList<>();
        dryCleanCatArr = new ArrayList<>();
        ironCatArr = new ArrayList<>();
        for (Category c : categoryArrayList) {
            if (c.getService_rate_id() == 1) {
                cleanCatArr.add(c);
            } else if (c.getService_rate_id() == 2) {
                cleanIronCatArr.add(c);
            } else if (c.getService_rate_id() == 3) {
                dryCleanCatArr.add(c);
            } else if (c.getService_rate_id() == 4) {
                ironCatArr.add(c);
            }
        }
    }


    public void onClickConfirm() throws IOException {
        if(selectedCat == null){
            if(serviceCombo.getSelectionModel().isEmpty() || categoryField.getText() == null || categoryField.getText().isEmpty()
                    || priceField.getText().isEmpty() || priceField.getText() == null){
                pushAlert("กรุณากรอกข้อมูลให้ครบถ้วน", Alert.AlertType.WARNING);
            }
            else if(!Validator.isDoubleAndPositive(priceField.getText())){
                pushAlert("กรุณากรอกราคาให้ถูกต้อง", Alert.AlertType.WARNING);
            }
            else{
                Category cat = new Category(1,serviceCombo.getSelectionModel().getSelectedIndex()+1,
                        categoryField.getText(),Double.parseDouble(priceField.getText()) -
                        serviceRates.get(serviceCombo.getSelectionModel().getSelectedIndex()).getBasePrice() );
                if( ServiceRateApiDataSource.addNewCategory(cat)){
                    pushAlert("เพิ่มประเภทผ้าสำเร็จ", Alert.AlertType.INFORMATION);
                    onClickAnchor();
                    initialize();
                }
                else{
                    pushAlert("เพิ่มประเภทผ้าไม่สำเร็จ", Alert.AlertType.ERROR);
                    onClickAnchor();
                    initialize();
                }
            }
        }
        else if(selectedCat != null){

        }
    }

    public void onClickDel() throws IOException {
        if(selectedCat != null){
            if( ServiceRateApiDataSource.deleteCategory(selectedCat.getId())){
                pushAlert("ลบประเภทผ้าสำเร็จ", Alert.AlertType.INFORMATION);
                onClickAnchor();
                initialize();
            }
            else{
                pushAlert("ลบประเภทผ้าไม่สำเร็จ", Alert.AlertType.ERROR);
            }
        }
    }
    public void pushAlert(String message,Alert.AlertType alertType){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(alertType);
        a.setContentText(message);
        a.show();
    }

    public void onClickEditClean() throws IOException {
        if(Validator.isDoubleAndPositive(cleanBPField.getText())){
            ServiceRateApiDataSource.patchServiceRate(1,Double.parseDouble(cleanBPField.getText()));
            onClickAnchor();
            initialize();
        }
    }

    public void onClickEditCleanIronBP() throws IOException {
        if(Validator.isDoubleAndPositive(cleanIronBPField.getText())){
            ServiceRateApiDataSource.patchServiceRate(2,Double.parseDouble(cleanIronBPField.getText()));
            onClickAnchor();
            initialize();
        }
    }

    public void onClickEditDryCleanBP() throws IOException {
        if (Validator.isDoubleAndPositive(dryCleanBPField.getText())) {
            ServiceRateApiDataSource.patchServiceRate(3, Double.parseDouble(dryCleanBPField.getText()));
            onClickAnchor();
            initialize();
        }
    }

    public void onClickEditIronBP() throws IOException {
            if(Validator.isDoubleAndPositive(ironBPField.getText())) {
                ServiceRateApiDataSource.patchServiceRate(4, Double.parseDouble(ironBPField.getText()));
                onClickAnchor();
                initialize();
            }
    }
        public void onClickMemSerRate(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/serviceMemRateView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
