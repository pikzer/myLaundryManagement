module th.ac.ku.mylaundry {
    requires javafx.controls;
    requires javafx.fxml;

    //needed for HTTP
    requires unirest.java;

    //needed for JSON
    requires gson;
    requires java.sql;
    requires json;


    opens th.ac.ku.mylaundry to javafx.fxml;
    exports th.ac.ku.mylaundry;
    exports th.ac.ku.mylaundry.controller;
    exports th.ac.ku.mylaundry.model;
    exports th.ac.ku.mylaundry.service;
    opens th.ac.ku.mylaundry.controller to javafx.fxml;
    opens th.ac.ku.mylaundry.model to javafx.fxml;
    opens th.ac.ku.mylaundry.service to javafx.fxml;

}