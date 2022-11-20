package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Customer;
import th.ac.ku.mylaundry.service.CustomerApiDataSource;
import th.ac.ku.mylaundry.service.InputFilter;
import th.ac.ku.mylaundry.service.LaundryApiDataSource;
import th.ac.ku.mylaundry.service.SendEmail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;

public class NotificationViewController extends Navigator {

    @FXML
    ComboBox cusCombo ;
    @FXML
    TextField subjectMailField ;
    @FXML
    TextArea textArea ;
    @FXML
    TableView cusTable;
    @FXML
    Button addBtn, allBtn, delBtn;

    ArrayList<Customer> customerArrayList ;



    public void initialize() throws IOException {
        customerArrayList = new ArrayList<>();
        shopNameLabel.setText(LaundryApiDataSource.getLaundryName(1).toString());
        initTable();
        ObservableList<String> customers = FXCollections.observableList(getCusPhone());
        FilteredList<String> filteredItems = new FilteredList<String>(customers);
        cusCombo.getEditor().textProperty().addListener(new InputFilter(cusCombo,filteredItems,false));
        cusCombo.setItems(filteredItems);
    }

    public void initTable(){
        cusTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Customer, String> nameCol = new TableColumn<Customer, String>("ชื่อ");
        TableColumn<Customer, String> phoneCol = new TableColumn<Customer, String>("เบอร์โทร");
        TableColumn<Customer, String> emailCol = new TableColumn<Customer, String>("อีเมล");

        idCol.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Customer,String>("email"));

        cusTable.getColumns().addAll(idCol,nameCol,phoneCol,emailCol) ;
    }

    public void updateTable(){
        initTable();
        ObservableList<Customer> customerObservableList = FXCollections.observableList(customerArrayList);
        cusTable.setItems(customerObservableList);
    }

    public void onClickAll() throws IOException {
        addBtn.setDisable(true);
        allBtn.setDisable(true);
        delBtn.setDisable(true);
        customerArrayList = new ArrayList<>() ;
        customerArrayList = CustomerApiDataSource.getCustomers();
        customerArrayList.removeIf(c -> c.getEmail().equals("null"));
        cusCombo.setDisable(true);
        updateTable();
    }




    public void onClickAdd(){
        if(!cusCombo.getSelectionModel().isEmpty()){
            Customer customer = CustomerApiDataSource.searchCustomer(cusCombo.getSelectionModel().getSelectedItem().toString());
            if(customer.getEmail().equals("null")){
                pushAlert("ลูกค้าไม่มีข้อมูลที่อยู่อีเมล", Alert.AlertType.WARNING);
                cusCombo.getSelectionModel().clearSelection();
                cusCombo.getEditor().clear();
                return;
            }
            for (Customer c:customerArrayList) {
                if(c.getPhone().equals(customer.getPhone())){
                    pushAlert("ลูกค้ามีอยู่ในรายการอยู่เลย", Alert.AlertType.WARNING);
                    return;
                }
            }
            customerArrayList.add(customer);
            cusCombo.getSelectionModel().clearSelection();
            cusCombo.getEditor().clear();
            updateTable();
        }
    }

    public void onClickDel(){
        if(!cusTable.getSelectionModel().isEmpty()){
            customerArrayList.remove(cusTable.getSelectionModel().getSelectedItem());
            updateTable();
        }
        else{
            pushAlert("กรุณาเลือกลูกค้า", Alert.AlertType.WARNING);
        }
    }

    public void onSendEmail(ActionEvent event) throws MessagingException, IOException {
        if(subjectMailField.getText().isEmpty() || textArea.getText().isEmpty()){
            pushAlert("กรุณากรอกหัวเรื่องหรือเนื้อเรื่อง", Alert.AlertType.WARNING);
        }
        else{
            if(customerArrayList.size() <= 0){
                pushAlert("กรุณาเพิ่มลูกค้า", Alert.AlertType.WARNING);
            }
            else{
                for (Customer c:customerArrayList) {
                    SendEmail.sendEmail(subjectMailField.getText(),textArea.getText(),c.getEmail());
                }
                root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/notificationView.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    public void onClickReset(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/notificationView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public ArrayList<String> getCusPhone() throws IOException {
        ArrayList<String> phones = new ArrayList<>();
        ArrayList<Customer> customers = CustomerApiDataSource.getCustomers();
        for (Customer c:customers) {
            phones.add(c.getPhone());
        }
        return phones;
    }
}
