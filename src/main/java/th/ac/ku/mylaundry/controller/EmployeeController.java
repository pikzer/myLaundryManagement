package th.ac.ku.mylaundry.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import th.ac.ku.mylaundry.service.EmployeeApiDataSource;
import th.ac.ku.mylaundry.service.LaundryApiDataSource;
import th.ac.ku.mylaundry.service.Validator;

import java.io.IOException;
import java.util.ArrayList;

public class EmployeeController extends Navigator{

    @FXML
    TableView employeeTable ;
    @FXML
    TextField nameField, phoneField, idCardField, emailField, bankNumField, salaryField; ;
    @FXML
    ComboBox bankNameCombo, roleCombo;
    @FXML
    TextArea adsTextArea ;
    @FXML
    Button delBtn;

    ArrayList<Employee> employeeArrayList ;
    Employee selectedEmployee ;

    String bankList[] = {"กรุงเทพ","ไทยพาณิขณ์","กสิกรไทย","ออมสิน","ทหารไทย"} ;
    String roleList[] = {"EMPLOYEE","DELIVER"};

    public void initialize() throws IOException {
        employeeArrayList = EmployeeApiDataSource.getEmployees() ;
        bankNameCombo.getItems().addAll(bankList);
        shopNameLabel.setText(LaundryApiDataSource.getLaundryName(1).toString());
        roleCombo.getItems().addAll(roleList);
        showEmployeeTable(employeeArrayList);
        employeeTable.getSelectionModel().selectedItemProperty().addListener((observableValue, newValue, oldValue) -> {
            if(oldValue != null){
                showSelectedEmployee((Employee) oldValue);
            }
        });
        phoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    phoneField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(newValue.length() > 10){
                    phoneField.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        idCardField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    idCardField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(newValue.length() > 13){
                    idCardField.setText(newValue.substring(0, newValue.length() - 1));
                }
            }
        });

        bankNumField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    bankNumField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
//        devOnly();
    }


    public void showEmployeeTable(ArrayList<Employee> employees){
        employeeTable.getColumns().clear();
        ObservableList<Employee> employeeObservableList = FXCollections.observableList(employees);
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Employee, String> nameCol = new TableColumn<Employee, String>("ชื่อ");
        TableColumn<Employee, String> roleCol = new TableColumn<Employee, String>("หน้าที่");
        TableColumn<Employee, String> telCol = new TableColumn<Employee, String>("เบอร์โทร");
        TableColumn<Employee, String> emailCol = new TableColumn<Employee, String>("อีเมล");
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
        selectedEmployee = employee ;
        roleCombo.getItems().clear();
        roleCombo.getItems().addAll(roleList);
        nameField.setText(employee.getName());
        phoneField.setText(employee.getPhone());
        idCardField.setText(employee.getIdCard());
        emailField.setText(employee.getEmail());
        adsTextArea.setText(employee.getAddress());
        salaryField.setText(String.valueOf(employee.getSalary()));
        // Combo
        if(employee.getRole().equals("OWNER")){
            roleCombo.getItems().clear();
            roleCombo.getItems().add("OWNER") ;
            roleCombo.getSelectionModel().select(0);
            delBtn.setDisable(true);
        }
        else{
            roleCombo.getSelectionModel().select(employee.getRole());
            delBtn.setDisable(false);
        }
        bankNumField.setText(employee.getBankAccountNumber());
        bankNameCombo.getSelectionModel().select(employee.getBankName());
    }

    public void onClickAnchor() throws IOException {
        selectedEmployee = null ;
        employeeTable.getSelectionModel().clearSelection();
        delBtn.setDisable(true);
        nameField.clear();
        adsTextArea.clear();
        bankNumField.clear();
        phoneField.clear();
        idCardField.clear();
        emailField.clear();
        roleCombo.getItems().clear();
        roleCombo.getSelectionModel().clearSelection();
        bankNameCombo.getItems().clear();
        bankNameCombo.getEditor().clear();
        salaryField.clear();
        bankNameCombo.getSelectionModel().clearSelection();
        initialize();
    }

    public void onClickDel(ActionEvent event) throws IOException {
        EmployeeApiDataSource.delEmployee(selectedEmployee.getId());
        initialize();
    }
    public void onClickAddOrEdit() throws IOException {
        if(selectedEmployee == null){
            if(nameField.getText().isEmpty() || phoneField.getText().isEmpty() || idCardField.getText().isEmpty()
                    || emailField.getText().isEmpty() || roleCombo.getSelectionModel().isEmpty()
                    || adsTextArea.getText().isEmpty() || bankNumField.getText().isEmpty()
                    || bankNameCombo.getSelectionModel().isEmpty()) {
                pushAlertWarning("กรุณากรอกข้อมูลให้ครบถ้วน");
            }
            else if(!Validator.isPhoneNumber(phoneField.getText())){
                pushAlertWarning("หมายเลขโทรศัพท์ไม่ถูกต้อง");
            }
            else if(!Validator.isEmail(emailField.getText())){
                pushAlertWarning("อีเมลไม่ถูกต้อง");
            }

            // other Validation
            else if(bankNumField.getText().length() < 10 || bankNumField.getText().length() > 15){
                pushAlertWarning("หมายเลขบัญชีไม่ถูกต้อง");
            }
            else if(!Validator.isDoubleAndPositive(salaryField.getText())){
                pushAlertWarning("กรุณากรอกเงินเดือนให้ถูกต้อง");
            }
            else{
                Employee employee = new Employee(nameField.getText(),phoneField.getText(),emailField.getText(), roleCombo.getSelectionModel().getSelectedItem().toString(),
                        "",Double.parseDouble(salaryField.getText()),adsTextArea.getText(), idCardField.getText(),bankNumField.getText(),
                        bankNameCombo.getSelectionModel().getSelectedItem().toString()) ;
                if(EmployeeApiDataSource.addEmployee(employee)){
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("เพิ่มพนักงงานสำเร็จ");
                    a.show();
                    onClickAnchor();
                }
                else{
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("เพิ่มพนักงงานไม่สำเร็จ");
                    a.show();
                }
            }
        }
        else{
            // TODO
            Employee employee = new Employee(selectedEmployee.getId(),nameField.getText(),phoneField.getText(),emailField.getText(), roleCombo.getSelectionModel().getSelectedItem().toString(),
                        "",Double.parseDouble(salaryField.getText()),adsTextArea.getText(), idCardField.getText(),bankNumField.getText(),
                        bankNameCombo.getSelectionModel().getSelectedItem().toString()) ;
                if(EmployeeApiDataSource.patchEmployee(employee)){
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("แก้ไขสำเร็จ");
                    a.show();
                    onClickAnchor();
                }
                else{
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setContentText("แก้ไขไม่สำเร็จ");
                    a.show();
                }
//            if(nameField.getText().isEmpty() || phoneField.getText().isEmpty() || idCardField.getText().isEmpty()
//                    || emailField.getText().isEmpty() || roleCombo.getSelectionModel().isEmpty()
//                    || adsTextArea.getText().isEmpty() || bankNumField.getText().isEmpty()
//                    || bankNameCombo.getSelectionModel().isEmpty() ) {
//                pushAlertWarning("กรุณากรอกข้อมูลให้ครบถ้วน");
//            }
//            else if(!Validator.isPhoneNumber(phoneField.getText())){
//                pushAlertWarning("หมายเลขโทรศัพท์ไม่ถูกต้อง");
//            }
//            else if(!Validator.isEmail(emailField.getText())){
//                pushAlertWarning("อีเมลล์ไม่ถูกต้อง");
//            }
//            if(!pwdField.getText().isEmpty()){
//                if(pwdField.getText().length() <= 8 && pwdField.getText().length() >= 32){
//                    pushAlertWarning("รหัสจะต้องมีความยาว 8 ตัวอักษรขึ้นไป");
//                }
//                else if(pwdField.getText().length() >= 32){
//                    pushAlertWarning("รหัสมีความมากกว่า 32 ตัวอักษร");
//                }
//                else if(!pwdField.getText().equals(conPwdField.getText())){
//                    pushAlertWarning("รหัสยืนยันไม่ถูกต้อง");
//                }
//            }
//            // other Validation
//            else if(bankNumField.getText().length() < 10 || bankNumField.getText().length() > 15){
//                pushAlertWarning("หมายเลขบัญชีไม่ถูกต้อง");
//            }
//            else if(!Validator.isDoubleAndPositive(salaryField.getText())){
//                pushAlertWarning("กรุณากรอกเงินเดือนให้ถูกต้อง");
//            }
//            else{
//                Employee employee = new Employee(selectedEmployee.getId(),nameField.getText(),phoneField.getText(),emailField.getText(), roleCombo.getSelectionModel().getSelectedItem().toString(),
//                        pwdField.getText(),Double.parseDouble(salaryField.getText()),adsTextArea.getText(), idCardField.getText(),bankNumField.getText(),
//                        bankNameCombo.getSelectionModel().getSelectedItem().toString()) ;
//                if(EmployeeApiDataSource.patchEmployee(employee)){
//                    Alert a = new Alert(Alert.AlertType.NONE);
//                    a.setAlertType(Alert.AlertType.INFORMATION);
//                    a.setContentText("แก้ไขสำเร็จ");
//                    a.show();
//                    onClickAnchor();
//                }
//                else{
//                    Alert a = new Alert(Alert.AlertType.NONE);
//                    a.setAlertType(Alert.AlertType.INFORMATION);
//                    a.setContentText("แก้ไขไม่สำเร็จ");
//                    a.show();
//                }
//            }
        }
    }
    @FXML
    public void onBackBtn(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/manageShopView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void pushAlertWarning(String message){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.WARNING);
        a.setContentText(message);
        a.show();
    }

    public void devOnly(){
        employeeTable.getSelectionModel().clearSelection();
        delBtn.setDisable(true);
        nameField.setText("Rujipas");
        adsTextArea.setText("94 สุขาประชาสรรค์ 3 ตำบลบางพูด อำเภอปากเกร็ด นนทบุรี 11120");
        bankNumField.setText("5662514252");
        phoneField.setText("0255855585");
        idCardField.setText("1102522625258");
        emailField.setText("rujipas@mail.com");
        roleCombo.getSelectionModel().select(0);
        bankNameCombo.getSelectionModel().select(0);
    }
}
