package th.ac.ku.mylaundry.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class NewOrderController extends Navigator {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button showAllBtn, showCleanBtn, showCleanIronBtn, showDryCleanBtn, showIronBtn,
            addCateBtn, delCateBtn, showQrBtn, makeInvBtn, makeReceiptBtn, makeTagBtn, makePayBtn, makeOrderBtn ;
    @FXML
    TableView categoryTable, clothListTable;
    @FXML
    ComboBox telCombo, orderCombo, payCombo;
    @FXML
    TextField cateSearchField, subTotalField, deliveryPriceField, taxField, totalField ;
    @FXML
    Label serviceLabel, cateLabel, pricePerUnitLabel, priceCateLabel, serviceTypeLabel, memBalanceLabel, quantityLabel ;
    @FXML
    Spinner quantitySpinner ;
    @FXML
    CheckBox deliCheck ;
    @FXML
    TextArea adsArea;
    @FXML
    DatePicker receiveDatePicker, deliDatePicker;

    public void initialize(){

    }


    // Button OnClick
    public void showAllService(){

    }

    public void showCleanService(){

    }

    public void showCleanIronService(){

    }

    public void dryCleanService(){

    }

    public void showIronService(){

    }

    public void addToClothList(){

    }

    public void delClothList(){

    }

    public void showQr(){

    }

    public void newCustomer(ActionEvent event) throws IOException {
        onClickCus(event);
    }

    public void registerMember(ActionEvent event) throws IOException {
        onClickCus(event);
    }

    public void makeOrder(){

    }

    public void printInv(){

    }

    public void printReceipt(){

    }

    public void printTag(){

    }

    public void payMoney(){

    }
}
