package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.model.MemberPackage;
import th.ac.ku.mylaundry.service.CustomerApiDataSource;
import th.ac.ku.mylaundry.service.MemberPackageDataSource;
import th.ac.ku.mylaundry.service.Validator;

import java.io.IOException;
import java.util.ArrayList;

public class CustomerListController extends Navigator {

    @FXML
    TableView cusTable;
    @FXML
    Button addMemBtn, recBtn, invBtn;
    @FXML
    ComboBox<String> serviceTypeCombo;
    @FXML
    ComboBox<String> serviceAddCombo;
    @FXML
    ComboBox<Integer> piecesCombo ;
    @FXML
    TextField searchField, nameField, telField, emailField ;
    @FXML
    CheckBox memCheck ;
    @FXML
    TextArea adsTextArea ;
    @FXML
    Label totalLabel ;



    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    ArrayList<Customer> customerArrayList;
    ArrayList<MemberPackage> memberPackageArrayList ;

    String serviceType[] = {"ทั้งหมด","ซักอบ","ซักรีด","ซักแห้ง","รีด"} ;

    @FXML
    public void initialize() throws IOException {
        customerArrayList = CustomerApiDataSource.getCustomers();
        memberPackageArrayList = MemberPackageDataSource.getMemberPackage();
        totalLabel.setText("0.00");
        piecesCombo.setDisable(true);
        serviceAddCombo.setDisable(true);
        addMemBtn.setDisable(true);
        recBtn.setDisable(true);
        invBtn.setDisable(true);
//        serviceAddCombo.getItems().clear();
//        piecesCombo.getItems().clear();
        serviceTypeCombo.getItems().addAll(serviceType);
        serviceTypeCombo.getSelectionModel().select(0);
//        piecesCombo.getItems().addAll(piecesCombo);
        serviceAddCombo.getItems().addAll(serviceTypeFill());
        showTableCustomer(FXCollections.observableList(customerArrayList));
        cusTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                showOnSelect((Customer) newValue) ;
            }
        });
        // TODO Listener Service ADD
//        serviceTypeCombo.getSelectionModel().selectedItemProperty().
        serviceAddCombo.setOnAction((event -> {
//            System.out.println(serviceAddCombo.getSelectionModel().getSelectedItem().toString());
            piecesCombo.getItems().addAll(piecesFill(serviceAddCombo.getSelectionModel().getSelectedItem()));
        }));
        piecesCombo.setOnAction((event -> {
//            System.out.println(serviceAddCombo.getSelectionModel().getSelectedItem().toString());
            for (MemberPackage p:memberPackageArrayList) {
//                if(p.getService().equals(serviceAddCombo.getSelectionModel().getSelectedItem().toString()) &&
//                p.getQuantity().equals(piecesCombo.getSelectionModel().getSelectedItem().toString())){
//                    totalLabel.setText(p.getPrice().toString());
//                }
                if(p.getService().equals(serviceAddCombo.getSelectionModel().getSelectedItem())){
                    if(p.getQuantity() == piecesCombo.getSelectionModel().getSelectedItem()){
                        totalLabel.setText(p.getPrice().toString());
                    }
                }
            }
        }));
    }

    public void showTableCustomer(ObservableList<Customer> customerObservableList){
        cusTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Customer, String> nameCol = new TableColumn<Customer, String>("ชื่อ");
        TableColumn<Customer, String> phoneCol = new TableColumn<Customer, String>("เบอร์โทร");
        TableColumn<Customer, String> emailCol = new TableColumn<Customer, String>("อีเมลล์");
        TableColumn<Customer, Integer> isMemberCol = new TableColumn<Customer, Integer>("สถานะสมาชิก");
        TableColumn<Customer, String> memServiceCol = new TableColumn<Customer, String>("ประเภท");
        TableColumn<Customer, Integer> memCreditCol = new TableColumn<Customer, Integer>("คงเหลือ");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("email"));
        isMemberCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("isMembership"));
        memServiceCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("memService"));
        memCreditCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("memCredit"));

        cusTable.getColumns().addAll(idCol,nameCol,phoneCol,emailCol,isMemberCol,memServiceCol,memCreditCol) ;
        cusTable.setItems(customerObservableList);
    }

    public void showOnSelect(Customer newValue){
        nameField.setText(newValue.getName());
        telField.setText(newValue.getPhone());
        emailField.setText(newValue.getEmail());
        piecesCombo.setDisable(false);
        serviceAddCombo.setDisable(false);
        addMemBtn.setDisable(false);
        recBtn.setDisable(false);
        invBtn.setDisable(false);
    }

    public void onClickAddEditCus(ActionEvent event) throws IOException {
        if(cusTable.getSelectionModel().isEmpty()){
            // ADD
            if(!nameField.getText().isEmpty() && Validator.isPhoneNumber(telField.getText())){
                CustomerApiDataSource.addNewCustomer(nameField.getText(),telField.getText());
                clearAll();
            }
            // UPDATE
            else{

            }
        }
    }
    public void onClickAnchor() throws IOException {
        clearAll();
    }
    public void clearAll() throws IOException {
        cusTable.getSelectionModel().clearSelection();
        piecesCombo.getItems().clear();
//        serviceAddCombo.getItems().clear();
        serviceTypeCombo.getItems().clear();
        nameField.clear();
        telField.clear();
        emailField.clear();
        initialize();
    }

    public String[] serviceTypeFill(){
        serviceAddCombo.getItems().clear();
        ArrayList<String> arr = new ArrayList<>() ;
        for (MemberPackage m:memberPackageArrayList){
            if(!arr.contains(m.getService())){
                arr.add(m.getService());
            }
        }
        return arr.toArray(new String[0]);
    }
    public Integer[] piecesFill(String service){
        piecesCombo.getItems().clear();
        ArrayList<Integer> arr = new ArrayList<>() ;
        for (MemberPackage m:memberPackageArrayList){
            if(m.getService().equals(service)){
                arr.add(m.getQuantity());
            }
        }
        return arr.toArray(new Integer[0]);
    }
}
