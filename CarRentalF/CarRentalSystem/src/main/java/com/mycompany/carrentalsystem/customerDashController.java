/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carrentalsystem;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author amir
 */
public class customerDashController {
    
    @FXML
    private Button close;

    @FXML
    private TableColumn<carData, String> customer_col_Brand;

    @FXML
    private TableColumn<carData, String> customer_col_Model;

    @FXML
    private TableColumn<carData, String> customer_col_Price;

    @FXML
    private TableColumn<carData, String> customer_col_Status;

    @FXML
    private TableColumn<carData, String> customer_col_carID;

    @FXML
    private Text customer_currentBalance;

    @FXML
    private Text customer_dashAvailableCar;

    @FXML
    private Text customer_dashBalance;

    @FXML
    private Button customer_dashboardBtn;

    @FXML
    private Text customer_newBalance;

    @FXML
    private Button customer_rechargeBtn;

    @FXML
    private Text customer_recharge_currentBalance;

    @FXML
    private TextField customer_recharge_rechargeAmount;

    @FXML
    private Text customer_rentAmount;

    @FXML
    private Button customer_rentCarBtn;
    
    @FXML
    private Button customer_doRechargeBtn;

    @FXML
    private ComboBox<String> customer_rent_carID;

    @FXML
    private DatePicker customer_rent_dateRented;

    @FXML
    private DatePicker customer_rent_dateReturned;

    @FXML
    private TextField customer_rent_searchCar;

    @FXML
    private TableView<carData> customer_rent_tableView;

    @FXML
    private Button customer_signOutBtn;

    @FXML
    private AnchorPane dashboard_form;
    
    @FXML
    private AnchorPane recharge_form;
    
    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private AnchorPane rentCar_form;

    @FXML
    private Label username;
    
    @FXML
    private double x = 0;
    
    @FXML
    private double y = 0;
    
    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;  
    
    private double totalBalance;
    public void rentDisplayTotalBalance(){
        String sql = "SELECT balance FROM customer WHERE username = '"
                +getData.username+"'";
        
        connect = database.connectDB();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()){
                totalBalance = result.getDouble("balance");
            }
            customer_recharge_currentBalance.setText("$" + String.valueOf(totalBalance));
            customer_dashBalance.setText("$" + String.valueOf(totalBalance));
            customer_currentBalance.setText("$" + String.valueOf(totalBalance));
            
        }catch(SQLException e){}
        
    }
    
    private int countDate;
    public void rentDate(){
        Alert alert;
        if(customer_rent_carID.getSelectionModel().getSelectedItem() == null){
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a Car to rent");
            alert.showAndWait();
            
            customer_rent_dateRented.setValue(null);
            customer_rent_dateReturned.setValue(null);
            
        }else{
            
            if(customer_rent_dateReturned.getValue().isAfter(customer_rent_dateRented.getValue())){
                countDate = customer_rent_dateReturned.getValue().compareTo(customer_rent_dateRented.getValue());
            }
            countDate++;
        }
    }
    
    private double totalP;
    private double newBalance;
    public void rentDisplayTotal(){
        rentDate();
        double price = 0;
        String sql = "SELECT price FROM car WHERE car_id = '"
                +customer_rent_carID.getSelectionModel().getSelectedItem()+"'";
        
        connect = database.connectDB();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()){
                price = result.getDouble("price");
            }
            totalP = (price * countDate);
                        
        }catch(SQLException e){}
    }
    
    public void rentDisplayNewBalance(){
        rentDisplayTotal();
        customer_rentAmount.setText("$" + String.valueOf(totalP));
        newBalance = totalBalance - totalP;
        customer_newBalance.setText("$" + String.valueOf(newBalance));
        
    }
    
    private int availableCarsToRent = 0;
    public ObservableList<carData> rentCarListData(){
        ObservableList<carData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM car";
        
        connect  = database.connectDB();
        
        try{            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            carData carD;
//            availableCarsToRent = 0;
            while(result.next()){
                carD = new carData(result.getInt("car_id"), result.getString("brand"), 
                        result.getString("model"), result.getDouble("price"), 
                        result.getString("status"), result.getDate("date"));
                
                if((result.getString("status").equals("Available"))){
                    listData.add(carD);
                    availableCarsToRent++;
                }
            }
            customer_dashAvailableCar.setText(String.valueOf(availableCarsToRent));
            
        }catch(Exception e){e.printStackTrace();}
        
        return listData;
    }
    
    public void rentCarClear(){
        customer_rentAmount.setText("$0.00");
        customer_newBalance.setText("$0.00");
        customer_rent_dateReturned.setValue(null);
        customer_rent_dateRented.setValue(null);
        customer_rent_carID.getSelectionModel().clearSelection();
    }
    private void makeUnavailable(String selectedItem) throws SQLException {
        String sql = "UPDATE car SET status = 'Not Available' WHERE car_id = '" + String.valueOf(selectedItem)+"'";
        connect = database.connectDB();
        statement = connect.createStatement();
        statement.executeUpdate(sql);
        
    }
    
    private double curBalancePay;
    public void payRent() throws SQLException{
        Alert alert;
        if(totalBalance < totalP){
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Insufficient Balance. Please recharge.");
            alert.showAndWait();
                        
        }else{
            
            String sql = "UPDATE customer SET balance = ";
           
            connect  = database.connectDB();
            
            Double newB = totalBalance-totalP;

            sql+=newB;
            sql+= " WHERE username = '"+getData.username+"'";

            statement = connect.createStatement();
            statement.executeUpdate(sql);
            
            
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Car rented successfully");
            alert.showAndWait();
            
            customer_newBalance.setText(String.valueOf(newBalance));
            customer_currentBalance.setText("$" + String.valueOf(totalBalance));
            
            makeUnavailable(customer_rent_carID.getSelectionModel().getSelectedItem());
            
            rentCarClear();
            rentCarShowListData();
            rentDisplayTotalBalance();
            rentCarShowList();
        }
    }

    private double curBalanceDoRecharge;
    public void doRecharge() throws SQLException{
        
        String sql = "SELECT balance FROM customer WHERE username = '"
                +getData.username+"'";
        
        connect = database.connectDB();
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if(result.next()){
                curBalanceDoRecharge = result.getDouble("balance");
            }
            
        }catch(Exception e){e.printStackTrace();}
        
        sql = "UPDATE customer SET balance = ";
           
        connect  = database.connectDB();
        String nb = customer_recharge_rechargeAmount.getText();
        Double newB = Double.valueOf(nb);
        curBalanceDoRecharge += newB;
        
        sql+=curBalanceDoRecharge;
        sql+= " WHERE username = '"+getData.username+"'";
         
        statement = connect.createStatement();
        statement.executeUpdate(sql);
        
        try {
            Alert alert;

            if (customer_recharge_rechargeAmount.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please enter amount to recharge.");
                alert.showAndWait();
            } else {
                
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                statement = connect.createStatement();
                statement.executeUpdate(sql);

                rentDisplayTotalBalance();
                customer_recharge_rechargeAmount.setText("$0.00");
            }
        }catch(Exception e){e.printStackTrace();}
        
    }
    
    public void rentCarShowList(){
               
        ObservableList listData = FXCollections.observableArrayList();
        
        String sql = "SELECT car_id FROM car WHERE status = 'Available'";
        
        connect = database.connectDB();
        try{
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                listData.add(result.getString("car_id"));
            }
            
            customer_rent_carID.setItems(listData);
            
        }catch(SQLException e){}
    }
    
    private ObservableList<carData> rentCarList;
    public void rentCarShowListData(){
                
        rentCarList = rentCarListData();
        
        customer_col_carID.setCellValueFactory(new PropertyValueFactory<>("carId"));
        customer_col_Brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        customer_col_Model.setCellValueFactory(new PropertyValueFactory<>("model"));
        customer_col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        customer_col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        customer_rent_tableView.setItems(rentCarList);
        
    }  

    @FXML
    public void logOut() throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to log out?");
        Optional<ButtonType> option = alert.showAndWait();
        try{
            if(option.get().equals(ButtonType.OK)){

                customer_signOutBtn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event)->{
                   x = event.getSceneX();
                   y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event)->{
                   stage.setX(event.getScreenX() - x);                       
                   stage.setY(event.getScreenY() - y);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        }catch(Exception e){e.printStackTrace();}
    }

    public void switchForm(ActionEvent event){

        if(event.getSource() == customer_dashboardBtn){
            dashboard_form.setVisible(true);
            rentCar_form.setVisible(false);
            recharge_form.setVisible(false);
            
//            customer_dashboardBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #727272, #dadada");
//            customer_rentCarBtn.setStyle("-fx-background-color: transparent");
//            customer_rechargeBtn.setStyle("-fx-background-color: transparent");
        
            rentDisplayTotalBalance();
            displayUsername();
        
        }else if(event.getSource() == customer_rentCarBtn){
            dashboard_form.setVisible(false);
            rentCar_form.setVisible(true);
            recharge_form.setVisible(false);
            
//            customer_dashboardBtn.setStyle("-fx-background-color: transparent");
//            customer_rentCarBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #727272, #dadada");
//            customer_rechargeBtn.setStyle("-fx-background-color: transparent");
            
            rentCarShowListData();
            rentDisplayTotalBalance();
            rentCarShowList();
            displayUsername();
            customer_dashAvailableCar.setText(String.valueOf(availableCarsToRent));
            
        }else if(event.getSource() == customer_rechargeBtn){
            dashboard_form.setVisible(false);
            rentCar_form.setVisible(false);
            recharge_form.setVisible(true);
            
//            customer_dashboardBtn.setStyle("-fx-background-color: transparent");
//            customer_rentCarBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #727272, #dadada");
//            customer_rechargeBtn.setStyle("-fx-background-color: transparent");
            
            rentDisplayTotalBalance();
            displayUsername();
        }
    }

    public void displayUsername(){
        
        rentDisplayTotalBalance();
        username.setText(getData.username);

    }
    
    @FXML
    public void close() {
        System.exit(0);
    }

    @FXML
    public void minimize(){
        Stage stage = (Stage)main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    
    public void initialize(URL location, ResourceBundle resources) {
        //Display username
        displayUsername();
        
        rentCarShowListData();
        rentDisplayTotalBalance();
        rentCarShowList();
        customer_dashAvailableCar.setText(String.valueOf(availableCarsToRent));
        
    }
}
