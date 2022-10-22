package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.util.ArrayList;

public class CustomerListController extends Navigator {

    @FXML
    TableView cusTable;
    @FXML
    ComboBox serviceTypeCombo, serviceAddCombo, piecesCombo ;
    @FXML
    TextField searchField, nameField, telField, emailField ;
    @FXML
    CheckBox memCheck ;
    @FXML
    TextArea adsTextArea ;



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
        serviceTypeCombo.getItems().addAll(serviceType);
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

    }

    public void showTableCustomer(ObservableList<Customer> customerObservableList){
        cusTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ID");
        TableColumn phoneCol = new TableColumn("Tel");
        TableColumn emailCol = new TableColumn("Email");
        TableColumn isMemberCol = new TableColumn("Status");
        TableColumn memServiceCol = new TableColumn("Service Member");
        TableColumn memCreditCol = new TableColumn("Balance");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("email"));
        isMemberCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("isMembership"));
        memServiceCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("memService"));
        memCreditCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("memCredit"));

        cusTable.getColumns().addAll(idCol,phoneCol,emailCol,isMemberCol,memServiceCol,memCreditCol) ;
        cusTable.setItems(customerObservableList);
    }

    public void showOnSelect(Customer newValue){
        nameField.setText(newValue.getName());
        telField.setText(newValue.getPhone());
        emailField.setText(newValue.getEmail());
    }

    public void onClickAnchor() throws IOException {
        clearAll();
    }
    public void clearAll() throws IOException {
        cusTable.getSelectionModel().clearSelection();
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
