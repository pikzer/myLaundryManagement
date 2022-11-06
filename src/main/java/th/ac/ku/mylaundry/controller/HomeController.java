package th.ac.ku.mylaundry.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.model.Laundry;
import th.ac.ku.mylaundry.model.Order;
import th.ac.ku.mylaundry.service.ApiCall;
import th.ac.ku.mylaundry.service.CustomerApiDataSource;
import th.ac.ku.mylaundry.service.LaundryApiDataSource;
import th.ac.ku.mylaundry.service.OrderApiDataSource;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class HomeController extends Navigator {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;



    @FXML
    Text shopNameText ;
    @FXML
    Label orderLabel, inprogressLabel, finishLabel, notPayLabel, incomeLabel, allCusLabel, memberLabel ;
    @FXML
    TableView orderTable ;

    @FXML
    ComboBox shopStatusCombo;

    String status[] = {"ปิด", "เปิด"} ;

    public Integer order = 0, inprogress = 0, finish = 0, notpay = 0, allCus = 0, member = 0 ;
    Double income = 0.00 ;

    @FXML
    public void initialize() throws IOException {
        ApiCall.role = ApiCall.getRole() ;
        refresh() ;
    }

    private void refresh() throws IOException {
        showOrderTable();
        ArrayList<String> dash = ApiCall.getDashboardData();
        order = OrderApiDataSource.getTodayOrder().size();
        orderLabel.setText(order.toString());
        inprogressLabel.setText(dash.get(0));
        shopNameLabel.setText(LaundryApiDataSource.getLaundryName(1).toString());
        finishLabel.setText(dash.get(1));
        notPayLabel.setText(dash.get(2));
        DecimalFormat df = new DecimalFormat("#0.00");
        incomeLabel.setText("฿"+dash.get(3));
        allCusLabel.setText(dash.get(4));
        memberLabel.setText(dash.get(5));
        shopStatusCombo.getItems().addAll(status);
        shopStatusCombo.getSelectionModel().select(LaundryApiDataSource.getStatusShop());
        shopStatusCombo.setOnAction(event -> {
            if(shopStatusCombo.getSelectionModel().getSelectedItem().equals("เปิด")){
                try {
                    LaundryApiDataSource.openShop();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(shopStatusCombo.getSelectionModel().getSelectedItem().equals("ปิด")){
                try {
                    LaundryApiDataSource.closeShop();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void onClickRefresh(ActionEvent event) throws IOException {
        refresh();
//        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/homeView.fxml"));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
    }

    public void onClickNewOrder(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/th/ac/ku/mylaundry/newOrderView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void showOrderTable(){
        ObservableList<Order> orderObservableList = FXCollections.observableList(OrderApiDataSource.getTodayOrder());
        orderTable.getColumns().clear();
        TableColumn idCol = new TableColumn("ไอดี");
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

        orderTable.getColumns().addAll(idCol,nameCol,serviceCol,statusCol,pickDateCol,pickTimeCol,deliDateCol,deliTimeCol,
                addressCol,responderCol,deliverCol,payMethodCol,payStatusCol,pickSerChargeCol,deliSerChargeCol,isMemCol,totalCol
        );
        orderTable.setItems(orderObservableList);
    }



}
