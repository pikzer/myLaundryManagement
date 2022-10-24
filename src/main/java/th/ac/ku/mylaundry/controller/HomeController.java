package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import th.ac.ku.mylaundry.service.LaundryApiDataSource;

import java.io.IOException;
import java.text.DecimalFormat;

public class HomeController extends Navigator {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    @FXML
    Label shopNameLabel;

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
    public void initialize()  {
        refresh() ;
    }

    private void refresh(){
        orderLabel.setText(order.toString());
        inprogressLabel.setText(inprogress.toString());
        finishLabel.setText(finish.toString());
        notPayLabel.setText(notpay.toString());
        DecimalFormat df = new DecimalFormat("#0.00");
        incomeLabel.setText("฿"+df.format(income));
        allCusLabel.setText(allCus.toString());
        memberLabel.setText(member.toString());
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


}
