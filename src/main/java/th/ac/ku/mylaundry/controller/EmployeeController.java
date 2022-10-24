package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Employee;
import th.ac.ku.mylaundry.service.EmployeeApiDataSource;

import java.io.IOException;
import java.util.ArrayList;

public class EmployeeController extends Navigator{

    @FXML
    TableView employeeTable ;
    @FXML
    TextField nameField, phoneField, idCardField, emailField, bankNumField ;
    @FXML
    ComboBox bankNameCombo;

    ArrayList<Employee> employeeArrayList ;

    public void initialize() throws IOException {
        employeeArrayList = EmployeeApiDataSource.getEmployees() ;
        showEmployeeTable(employeeArrayList);
        employeeTable.getSelectionModel().selectedItemProperty().addListener((observableValue, newValue, oldValue) -> {
            showSelectedEmployee((Employee) newValue);
        });
    }


    public void showEmployeeTable(ArrayList<Employee> employees){
        ObservableList<Employee> employeeObservableList = FXCollections.observableList(employees);
        employeeTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Employee, String> nameCol = new TableColumn<Employee, String>("ชื่อ");
        TableColumn<Employee, String> roleCol = new TableColumn<Employee, String>("หน้าที่");
        TableColumn<Employee, String> telCol = new TableColumn<Employee, String>("เบอร์โทร");
        TableColumn<Employee, String> emailCol = new TableColumn<Employee, String>("อีเมลล์");
        TableColumn<Employee, String> addressCol = new TableColumn<Employee, String>("ที่อยู่");
        TableColumn<Employee, String> salaryCol = new TableColumn<Employee, String>("เงินเดือน");


        idCol.setCellValueFactory(new PropertyValueFactory<Employee,Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("name"));
        roleCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("role"));
        telCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("email"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("address"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<Employee,String>("salary"));
        employeeTable.getColumns().addAll(idCol,nameCol,roleCol,telCol,emailCol,addressCol,salaryCol) ;
        employeeTable.setItems(employeeObservableList);
    }

    public void showSelectedEmployee(Employee employee){
        nameField.setText(employee.getName());
        phoneField.setText(employee.getPhone());
        idCardField.setText(employee.getIdCard());
        emailField.setText(employee.getEmail());
        // Combo

    }

    @FXML
    public void onBackBtn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/manageShopView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
