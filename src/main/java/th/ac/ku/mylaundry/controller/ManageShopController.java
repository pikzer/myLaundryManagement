package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Employee;
import th.ac.ku.mylaundry.model.Laundry;
import th.ac.ku.mylaundry.service.EmployeeApiDataSource;
import th.ac.ku.mylaundry.service.LaundryApiDataSource;

import java.io.IOException;
import java.util.ArrayList;

public class ManageShopController extends Navigator  {

    @FXML
    TextField nameField, shopTelField, shopMailField, idLineField ;
    @FXML
    TextArea adsTextArea ;
    @FXML
    CheckBox sunCheck, monCheck, tueCheck, wedCheck, thuCheck, friCheck, satCheck;
    @FXML
    ComboBox openTimeCombo, closeTimeCombo ;
    @FXML
    TableView employeeTable ;

    Laundry laundry ;
    ArrayList<Employee> employeeArrayList ;
    ArrayList<Boolean> day ;

    String timeTable[] = {"06:00","07:00","08:00", "09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00",
            "17:00", "18:00","19:00","20.00","21:00","22.00","23:00","00:00","01:00","02:00","03:00","04:00","05:00",} ;

    public void initialize() throws IOException {
        employeeArrayList = EmployeeApiDataSource.getEmployees() ;
        showEmployeeTable(employeeArrayList);
        laundry = LaundryApiDataSource.getShop();
        nameField.setText(laundry.getName());
        shopTelField.setText(laundry.getPhone());
        shopMailField.setText(laundry.getEmail());
        adsTextArea.setText(laundry.getAddress());
        idLineField.setText(laundry.getLineID());
        openTimeCombo.getItems().addAll(timeTable);
        closeTimeCombo.getItems().addAll(timeTable);
        openTimeCombo.getSelectionModel().select(laundry.getOpentime());
        closeTimeCombo.getSelectionModel().select(laundry.getClosetime());
        decodeDay(laundry.getWorkDay());
        sunCheck.setSelected(day.get(0));
        monCheck.setSelected(day.get(1));
        tueCheck.setSelected(day.get(2));
        wedCheck.setSelected(day.get(3));
        thuCheck.setSelected(day.get(4));
        friCheck.setSelected(day.get(5));
        satCheck.setSelected(day.get(6));
    }

    public void showEmployeeTable(ArrayList<Employee> employees){
        ObservableList<Employee> employeeObservableList = FXCollections.observableList(employees);
        employeeTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Employee, String> nameCol = new TableColumn<Employee, String>("ชื่อ");
        TableColumn<Employee, String> roleCol = new TableColumn<Employee, String>("หน้าที่");
        TableColumn<Employee, String> telCol = new TableColumn<Employee, String>("เบอร์โทร");
        TableColumn<Employee, String> emailCol = new TableColumn<Employee, String>("อีเมลล์");
        idCol.setCellValueFactory(new PropertyValueFactory<Employee,Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("name"));
        roleCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("role"));
        telCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("email"));
        employeeTable.getColumns().addAll(idCol,nameCol,roleCol,telCol,emailCol) ;
        employeeTable.setItems(employeeObservableList);
    }
    public void onClickSave(){

    }



    public void onClickEditEmp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/employeeView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void decodeDay(String code){
        day = new ArrayList<>();
        for(int i = 0 ; i < code.length();i++){
            if(code.charAt(i)=='1'){
                day.add(true);
            }
            else{
                day.add(false);
            }
        }
    }
}
