package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import th.ac.ku.mylaundry.model.Order;
import th.ac.ku.mylaundry.service.LaundryApiDataSource;
import th.ac.ku.mylaundry.service.OrderApiDataSource;
import th.ac.ku.mylaundry.service.ReportWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReportController extends Navigator {

    @FXML
    DatePicker fromDatePicker, toDatePicker ;
    @FXML
    TableView orderTable ;

    ArrayList<Order> orderArrayList ;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public void initialize() throws IOException {
        shopNameLabel.setText(LaundryApiDataSource.getLaundryName(1).toString());
        addColTable();
        fromDatePicker.setOnAction(date ->{
            toDatePicker.getEditor().clear();
            toDatePicker.setValue(null);
            initToPicker(fromDatePicker.getValue());
        });
    }

    public void onClickGetData() throws IOException {
        if(fromDatePicker.getValue() != null || toDatePicker.getValue() != null){
            orderArrayList = OrderApiDataSource.getReport(fromDatePicker.getValue().format(formatter),
                    toDatePicker.getValue().plusDays(1).format(formatter));
            intiTable();
        }
    }

    public void initToPicker(LocalDate fromDate){
        toDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || fromDate.compareTo(date) > 0 );
            }
        });
    }

    public void printReport() throws IOException {
        if(orderArrayList != null){
            ReportWriter.writeReport(fromDatePicker.getValue().format(formatter),
                    toDatePicker.getValue().format(formatter), orderArrayList);
        }
    }

    public void intiTable(){
        ObservableList<Order> observableList = FXCollections.observableList(orderArrayList);
        addColTable();
        orderTable.setItems(observableList);
    }

    public void addColTable(){
        orderTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
        TableColumn<Order,String> createdCol = new TableColumn<>("วันที่");
        TableColumn<Order, String> nameCol = new TableColumn<Order, String>("ชื่อ");
        TableColumn<Order, String> serviceCol = new TableColumn<Order, String>("บริการ");
        TableColumn<Order, String> statusCol = new TableColumn<Order, String>("สถานะ");
        TableColumn<Order, String> pickDateCol = new TableColumn<Order, String>("วันที่รับ");
        TableColumn<Order, String> pickTimeCol = new TableColumn<Order, String>("เวลาที่รับ");
        TableColumn<Order, String> deliDateCol = new TableColumn<Order, String>("วันที่ส่ง");
        TableColumn<Order, String> deliTimeCol = new TableColumn<Order, String>("เวลาที่ส่ง");
        TableColumn<Order, String> addressCol = new TableColumn<Order, String>("ที่อยู่");
        TableColumn<Order, String> responderCol = new TableColumn<Order, String>("ผู้รับผิดชอบ");
        TableColumn<Order, String> deliverCol = new TableColumn<Order, String>("ผู้ส่งผ้า");
        TableColumn<Order, String> payMethodCol = new TableColumn<Order, String>("รูปแบบการชำระ");
        TableColumn<Order, Boolean> payStatusCol = new TableColumn<Order, Boolean>("สถานะชำระ");
        TableColumn<Order, Double> pickSerChargeCol = new TableColumn<Order, Double>("ค่ารับ");
        TableColumn<Order, Double> deliSerChargeCol = new TableColumn<Order, Double>("ค่าส่ง");
        TableColumn<Order, Boolean> isMemCol = new TableColumn<Order, Boolean>("รายการสมาชิก");
        TableColumn<Order, Double> totalCol = new TableColumn<Order, Double>("ค่าบริการรวม");

        idCol.setCellValueFactory(new PropertyValueFactory<Order,Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Order,String>("name"));
        createdCol.setCellValueFactory(new PropertyValueFactory<Order,String>("created_at"));
        serviceCol.setCellValueFactory(new PropertyValueFactory<Order,String>("service"));
        statusCol.setCellValueFactory(new PropertyValueFactory<Order,String>("status"));
        pickDateCol.setCellValueFactory(new PropertyValueFactory<Order,String>("pickDate"));
        pickTimeCol.setCellValueFactory(new PropertyValueFactory<Order,String>("pickTime"));
        deliDateCol.setCellValueFactory(new PropertyValueFactory<Order,String>("deliDate"));
        deliTimeCol.setCellValueFactory(new PropertyValueFactory<Order,String>("deliTime"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Order,String>("address"));
        responderCol.setCellValueFactory(new PropertyValueFactory<Order,String>("responder"));
        deliverCol.setCellValueFactory(new PropertyValueFactory<Order,String>("deliver"));
        payMethodCol.setCellValueFactory(new PropertyValueFactory<Order,String>("payMethod"));
        payStatusCol.setCellValueFactory(new PropertyValueFactory<Order,Boolean>("payStatus"));
        pickSerChargeCol.setCellValueFactory(new PropertyValueFactory<Order,Double>("pickSerCharge"));
        deliSerChargeCol.setCellValueFactory(new PropertyValueFactory<Order,Double>("deliSerCharge"));
        isMemCol.setCellValueFactory(new PropertyValueFactory<Order,Boolean>("isMemOrder"));
        totalCol.setCellValueFactory(new PropertyValueFactory<Order,Double>("total"));

        orderTable.getColumns().addAll(idCol,createdCol,nameCol,serviceCol,statusCol,pickDateCol,pickTimeCol,deliDateCol,deliTimeCol,
                addressCol,responderCol,deliverCol,payMethodCol,payStatusCol,pickSerChargeCol,deliSerChargeCol,isMemCol,totalCol
        );
    }


}
