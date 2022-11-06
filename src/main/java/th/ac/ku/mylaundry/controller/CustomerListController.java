package th.ac.ku.mylaundry.controller;

import com.itextpdf.text.DocumentException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.model.MemberPackage;
import th.ac.ku.mylaundry.service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

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

    ObservableList<Customer> customerObservableList ;

    Customer selectedCus ;

    String serviceType[] = {"ทั้งหมด","ซักอบ","ซักรีด","ซักแห้ง","รีด"} ;

    @FXML
    public void initialize() throws IOException {
        customerArrayList = CustomerApiDataSource.getCustomers();
        memberPackageArrayList = MemberPackageApiDataSource.getMemberPackage();
        totalLabel.setText("0.00");
        piecesCombo.setDisable(true);
        serviceAddCombo.setDisable(true);
        addMemBtn.setDisable(true);
        shopNameLabel.setText(LaundryApiDataSource.getLaundryName(1).toString());
//        serviceAddCombo.getItems().clear();
//        piecesCombo.getItems().clear();
        serviceTypeCombo.getItems().addAll(serviceType);
        serviceTypeCombo.getSelectionModel().select(0);
//        piecesCombo.getItems().addAll(piecesCombo);
        serviceAddCombo.getItems().addAll(serviceTypeFill());
//        showTableCustomer(FXCollections.observableList(customerArrayList));
        cusTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                showOnSelect((Customer) newValue) ;

                selectedCus = (Customer) newValue;
            }
        });
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
        customerObservableList = FXCollections.observableList(customerArrayList) ;
        FilteredList<Customer> filteredList  = new FilteredList<>(customerObservableList,p-> true) ;
        searchField.textProperty().addListener((observableValue, newValue, oldValue) -> {
            filteredList.setPredicate(customer -> {
                if(newValue == null || newValue.isEmpty()|| oldValue == null || oldValue.isEmpty()){
                    return true ;
                }
                String lower = newValue.toLowerCase() ;
                if(customer.getPhone().contains(lower)){
                    return true;
                }
                if(customer.getName().contains(lower))
                    return true ;
                return false ;
            });
        });

        serviceTypeCombo.setOnAction(event -> {
            filteredList.setPredicate(customer -> {
                if(serviceTypeCombo.getSelectionModel().getSelectedItem() == null){
                    return true ;
                }
                else if(serviceTypeCombo.getSelectionModel().getSelectedItem().equals("ทั้งหมด")){
                    return true ;
                }
                else if(serviceTypeCombo.getSelectionModel().getSelectedItem().equals("ซักอบ")){
                    if(customer.getMemService().equals("ซักอบ")){
                        return true;
                    }
                }
                else if(serviceTypeCombo.getSelectionModel().getSelectedItem().equals("ซักรีด")){
                    if(customer.getMemService().equals("ซักรีด")){
                        return true;
                    }
                }
                else if(serviceTypeCombo.getSelectionModel().getSelectedItem().equals("ซักแห้ง")){
                    if(customer.getMemService().equals("ซักแห้ง")){
                        return true;
                    }
                }
                else if(serviceTypeCombo.getSelectionModel().getSelectedItem().equals("รีด")){
                    if(customer.getMemService().equals("รีด")){
                        return true;
                    }
                }
                return false;
            });
        });

        memCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if(newValue == true){
                    filteredList.setPredicate(customer -> {
                        if(customer.getIsMembership() == 1){
                            return true ;
                        }
                        return false ;
                    });
                }
                else if(newValue == false){
                    filteredList.setPredicate(customer -> {
                        return true ;
                    });
                }
            }
        });

        SortedList<Customer> cusSortedList =new SortedList<>(filteredList) ;
        cusSortedList.comparatorProperty().bind(cusTable.comparatorProperty());
        showTableCustomer(cusSortedList);
    }

    public void showTableCustomer(ObservableList<Customer> customerSortedList){
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
        cusTable.setItems(customerSortedList);
    }

    public void showOnSelect(Customer newValue){
        adsTextArea.clear();
        emailField.clear();
        nameField.setText(newValue.getName());
        telField.setText(newValue.getPhone());
        if(!newValue.getEmail().equals("null")){
            emailField.setText(newValue.getEmail());
        }
        if(CustomerApiDataSource.getCustomerAddress(newValue.getId()) != null){
            adsTextArea.setText(CustomerApiDataSource.getCustomerAddress(newValue.getId()).getuCode());
        }
        piecesCombo.setDisable(false);
        serviceAddCombo.setDisable(false);
        addMemBtn.setDisable(false);
    }

    public void onClickAddEditCus(ActionEvent event) throws IOException {
        if (cusTable.getSelectionModel().isEmpty()) {
            // ADD
            if (!nameField.getText().isEmpty() && Validator.isPhoneNumber(telField.getText()) && !adsTextArea.getText().isEmpty() && emailField.getText().isEmpty()) {
                if (CustomerApiDataSource.addNewCustomer(nameField.getText(), telField.getText(), adsTextArea.getText())) {
                    pushAlert("เพิ่มลูกค้าสำเร็จ", Alert.AlertType.INFORMATION);
                    clearAll();
                } else {
                    pushAlert("เพิ่มลูกค้าไม่สำเร็จ", Alert.AlertType.ERROR);
                    clearAll();
                }
            }
            else{
                if (CustomerApiDataSource.addNewCustomer(nameField.getText(), telField.getText(), adsTextArea.getText(), emailField.getText())) {
                    pushAlert("เพิ่มลูกค้าสำเร็จ", Alert.AlertType.INFORMATION);
                    clearAll();
                } else {
                    pushAlert("เพิ่มลูกค้าไม่สำเร็จ", Alert.AlertType.ERROR);
                    clearAll();
                }
            }
        }
        // EDIT
        else {
            if(!nameField.getText().isEmpty() && !telField.getText().isEmpty() && !adsTextArea.getText().isEmpty()){
                if(emailField.getText().isEmpty()){
                    if(Validator.isPhoneNumber(telField.getText())){
                       if(CustomerApiDataSource.updateCustomer(selectedCus.getId(),nameField.getText(),telField.getText(),null,adsTextArea.getText())){
                           pushAlert("แก้ไขลูกค้าสำเร็จ", Alert.AlertType.INFORMATION);
                           clearAll();
                       }
                       else {
                           pushAlert("แก้ไขลูกค้าไม่สำเร็จ", Alert.AlertType.ERROR);
                           clearAll();
                       }
                    }
                    else{
                        pushAlert("กรุณากรอกข้อมูลให้ถูกต้อง", Alert.AlertType.WARNING);
                        clearAll();
                    }
                }
                else{
                    if(Validator.isEmail(emailField.getText())){
                        if(CustomerApiDataSource.updateCustomer(selectedCus.getId(),nameField.getText(),telField.getText(),emailField.getText(),adsTextArea.getText())){
                            pushAlert("แก้ไขลูกค้าสำเร็จ", Alert.AlertType.INFORMATION);
                            clearAll();
                        }
                        else{
                            pushAlert("แก้ไขลูกค้าไม่สำเร็จ", Alert.AlertType.ERROR);
                            clearAll();
                        }
                    }
                    else{
                        pushAlert("กรุณากรอกข้อมูลให้ถูกต้อง", Alert.AlertType.WARNING);
                        clearAll();
                    }
                }
            }
            else{
                pushAlert("กรุณากรอกข้อมูลให้ครบถ้วน", Alert.AlertType.WARNING);
                clearAll();
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
        adsTextArea.clear();
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

    public void onClickAddMem() throws IOException, DocumentException {
        if(serviceAddCombo.getSelectionModel().getSelectedItem() != null && piecesCombo.getSelectionModel().getSelectedItem() != null){
            if(selectedCus.getMemCredit() == 0){
                selectedCus.setMemCredit(piecesCombo.getSelectionModel().getSelectedItem());
                selectedCus.setMemService(serviceAddCombo.getSelectionModel().getSelectedItem().toString());
                CustomerApiDataSource.addMembership(selectedCus.getId(),serviceAddCombo.getSelectionModel().getSelectedItem(),selectedCus.getMemCredit());
                cusTable.refresh();
                pushAlert("เพิ่มสมาชิกสำเร็จ", Alert.AlertType.INFORMATION);
                getReceipt(selectedCus,new MemberPackage(0,serviceAddCombo.getSelectionModel().getSelectedItem().toString(),piecesCombo.getSelectionModel().getSelectedItem(),Double.parseDouble(totalLabel.getText())));
            }
            else if(selectedCus.getMemCredit() != 0){
                if(selectedCus.getMemService().equals(serviceAddCombo.getSelectionModel().getSelectedItem())){
                    selectedCus.setMemCredit(selectedCus.getMemCredit()+piecesCombo.getSelectionModel().getSelectedItem());
                    CustomerApiDataSource.addMembership(selectedCus.getId(),serviceAddCombo.getSelectionModel().getSelectedItem(),selectedCus.getMemCredit());
                    pushAlert("เพิ่มสมาชิกสำเร็จ", Alert.AlertType.INFORMATION);
                    getReceipt(selectedCus,new MemberPackage(0,serviceAddCombo.getSelectionModel().getSelectedItem().toString(),piecesCombo.getSelectionModel().getSelectedItem(),Double.parseDouble(totalLabel.getText())));
                    cusTable.refresh();
                    onClickAnchor();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("แทนที่สมาชิกแบบเหมาเก่า");
                    alert.setHeaderText("คุณแน่ใจที่จะทำการแทนที่สมาชิกแบบเหมาเก่า?");
                    Optional<ButtonType> option = alert.showAndWait();
                    ButtonType cancel = new ButtonType("ยกเลิก");
                    alert.getButtonTypes().add(cancel);
                    if(option.get() == null){

                    }
                    else if (option.get() == ButtonType.OK) {
                        selectedCus.setMemCredit(piecesCombo.getSelectionModel().getSelectedItem());
                        selectedCus.setMemService(serviceAddCombo.getSelectionModel().getSelectedItem());
                        CustomerApiDataSource.addMembership(selectedCus.getId(),selectedCus.getMemService(),selectedCus.getMemCredit());
                        pushAlert("เพิ่มสมาชิกสำเร็จ", Alert.AlertType.INFORMATION);
                        getReceipt(selectedCus,new MemberPackage(0,serviceAddCombo.getSelectionModel().getSelectedItem().toString(),piecesCombo.getSelectionModel().getSelectedItem(),Double.parseDouble(totalLabel.getText())));
                        cusTable.refresh();
//                        onClickAnchor();
                    } else if (option.get() == ButtonType.CANCEL) {
                    } else {
                    }
                }
            }
        }
    }

    public void getReceipt(Customer customer, MemberPackage memberPackage) throws DocumentException, IOException {
        if(customer != null || memberPackage != null){
            WriterPDF.packageReceipt(customer,memberPackage);
        }
    }

    public void pushAlert(String message,Alert.AlertType alertType){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(alertType);
        a.setContentText(message);
        a.show();
    }

}
