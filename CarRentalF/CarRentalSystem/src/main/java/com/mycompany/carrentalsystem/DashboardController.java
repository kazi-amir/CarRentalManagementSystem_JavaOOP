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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
public class DashboardController {
    
    @FXML
    private Button admin_availableCarsBtn;

    @FXML
    private TableColumn<carData, String> admin_col_Brand;

    @FXML
    private TableColumn<carData, String> admin_col_Model;

    @FXML
    private TableColumn<carData, String> admin_col_Price;

    @FXML
    private TableColumn<carData, String> admin_col_Status;

    @FXML
    private TableColumn<carData, String> admin_col_carID;

    @FXML
    private TextField admin_dashCarBrand;

    @FXML
    private Button admin_dashCarClear;

    @FXML
    private TextField admin_dashCarID;

    @FXML
    private TextField admin_dashCarPrice;

    @FXML
    private Button admin_dashCarInsert;

    @FXML
    private TextField admin_dashCarModel;

    @FXML
    private TextField admin_dashCarSearch;

    @FXML
    private ComboBox<?> admin_dashCarStatus;

    @FXML
    private TableView<carData> admin_dashCarTableView;

    @FXML
    private Button admin_dashCarUpdate;

    @FXML
    private Text admin_dashCustomer;

    @FXML
    private Text admin_dashEarning;

    @FXML
    private Text admin_dashavailableCars;

    @FXML
    private Button admin_dashboardBtn;

    @FXML
    private Button admin_signOutBtn;

    @FXML
    private Label username;

    @FXML
    private AnchorPane availableCar_form;

    @FXML
    private Button close;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;
    
    @FXML
    private double x = 0;
    
    @FXML
    private double y = 0;
    

    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;  
    
    @FXML
        @SuppressWarnings("empty-statement")
    public void availableCarAdd(){
        
        String sql = "INSERT INTO car (car_id, brand, model, price, status, date) " 
                + "VALUES (?, ?, ?, ?, ?, ?)";
        
        connect = database.connectDB();
        
        try{
            
            Alert alert;
            if(admin_dashCarID.getText().isEmpty()
                || admin_dashCarBrand.getText().isEmpty()
                || admin_dashCarModel.getText().isEmpty()
                || admin_dashCarPrice.getText().isEmpty()
                || admin_dashCarStatus.getSelectionModel().getSelectedItem()==null){
                
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, admin_dashCarID.getText());
                prepare.setString(2, admin_dashCarBrand.getText());
                prepare.setString(3, admin_dashCarModel.getText());
                prepare.setString(4, admin_dashCarPrice.getText());
                prepare.setString(5, (String)admin_dashCarStatus.getSelectionModel().getSelectedItem());
                
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                
                prepare.setString(6, String.valueOf(sqlDate));
                
                prepare.executeUpdate();
                
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Car Data Added Successfully.");
                alert.showAndWait();
                
                availableCarShowListData();
                availableCarClear();
            }
        }catch(SQLException e){};
    }

    public void availableCarUpdate() {
        int op = 0;
        String sql = "UPDATE car SET";
        if(admin_dashCarBrand.getText().isEmpty()==false){
            if(op>0){ sql+=","; }
            sql+= " brand = '" + admin_dashCarBrand.getText()+"'";
            op++;
        }
        if(admin_dashCarModel.getText().isEmpty()==false){
            if(op>0){ sql+=","; }
            sql+= " model = '" + admin_dashCarModel.getText()+"'"; op++;
        }
        if(admin_dashCarStatus.getSelectionModel().getSelectedItem()!=null){
            if(op>0){ sql+=","; }
            sql+= " status = '" + admin_dashCarStatus.getSelectionModel().getSelectedItem()+"'"; op++;
        }
        if(admin_dashCarPrice.getText().isEmpty()==false){
            if(op>0){ sql+=","; }
            sql+= " price = '" + admin_dashCarPrice.getText()+"'"; op++;
        }
        sql+= " WHERE car_id = '" + admin_dashCarID.getText() + "'";

        connect = database.connectDB();

        try {
            Alert alert;

            if (admin_dashCarID.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please provide Car ID");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Car ID: " + admin_dashCarID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    availableCarShowListData();
                    availableCarClear();
                }

            }
        } catch (SQLException e) {}

    }
    
    public void availableCarClear(){
        admin_dashCarID.setText("");
        admin_dashCarBrand.setText("");
        admin_dashCarModel.setText("");
        admin_dashCarPrice.setText("");
        admin_dashCarStatus.getSelectionModel().clearSelection();
    }

    public void availableCarDelete() {

        String sql = "DELETE FROM car WHERE car_id = '" + admin_dashCarID.getText() + "'";

        connect = database.connectDB();

        try {
            Alert alert;
            if (admin_dashCarID.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Car ID: " + admin_dashCarID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    availableCarShowListData();
                    availableCarClear();
                }

            }
        } catch (SQLException e) {}
    }
    
    private ObservableList<carData> availableCarList;
    
    public void availableCarShowListData(){
        
        availableCarList = availableCarListData();
        
        admin_col_carID.setCellValueFactory(new PropertyValueFactory<>("carId"));
        admin_col_Brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        admin_col_Model.setCellValueFactory(new PropertyValueFactory<>("model"));
        admin_col_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        admin_col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        admin_dashCarTableView.setItems(availableCarList);
    }
    
    public void availableCarSelect(){
        
        carData carD = admin_dashCarTableView.getSelectionModel().getSelectedItem();
        
        int num = admin_dashCarTableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < - 1) {
            return;
        }

        admin_dashCarID.setText(String.valueOf(carD.getCarId()));
        admin_dashCarBrand.setText(carD.getBrand());
        admin_dashCarModel.setText(carD.getModel());
        admin_dashCarPrice.setText(String.valueOf(carD.getPrice()));

    }

    public void availableCarSearch() {

        FilteredList<carData> filter = new FilteredList<>(availableCarList, e -> true);

        admin_dashCarSearch.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateCarData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateCarData.getCarIDstring().contains(searchKey)) {
                    return true;
                } else if (predicateCarData.getBrand().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCarData.getModel().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCarData.getPrice().toString().contains(searchKey)) {
                    return true;
                } else if (predicateCarData.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<carData> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(admin_dashCarTableView.comparatorProperty());
        admin_dashCarTableView.setItems(sortList);

    }
    
    private String[] listStatus = {"Available", "Not Available"};
    
    public void availableCarStatusList(){
        List<String> listSt = new ArrayList<>();
        
        for(String data: listStatus){
            listSt.add(data);
        }
        ObservableList listData = FXCollections.observableArrayList(listSt);
        admin_dashCarStatus.setItems(listData);
    }
    
    public ObservableList<carData> availableCarListData(){

        ObservableList<carData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM car";

        connect = database.connectDB();

        try{

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            carData carD;

            while(result.next()){

                carD = new carData(result.getInt("car_id"), result.getString("brand"), 
                        result.getString("model"), result.getDouble("price"), 
                        result.getString("status"), result.getDate("date"));

                listData.add(carD);
            }



        }catch(SQLException e){}int name = 9;

        return listData;
    }
    
    public void displayUsername(){

        String user = getData.username;
        username.setText(user);

    }
    
    public void switchForm(ActionEvent event){

        if(event.getSource() == admin_dashboardBtn){
            dashboard_form.setVisible(true);
            availableCar_form.setVisible(false);

            admin_dashboardBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #727272, #dadada");
            admin_availableCarsBtn.setStyle("-fx-background-color: transparent");

        }else if(event.getSource() == admin_availableCarsBtn){
            dashboard_form.setVisible(false);
            availableCar_form.setVisible(true);

            admin_dashboardBtn.setStyle("-fx-background-color: transparent");
            admin_availableCarsBtn.setStyle("-fx-background-color: linear-gradient(to bottom right, #727272, #dadada");

            availableCarShowListData();
            availableCarStatusList();
            availableCarSearch();

        }
    }
    
    public void logOut() throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to log out?");
        Optional<ButtonType> option = alert.showAndWait();
        try{
            if(option.get().equals(ButtonType.OK)){

                admin_signOutBtn.getScene().getWindow().hide();

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
        }catch(IOException e){}
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
        
        //Available Car Form
        availableCarShowListData();
        availableCarStatusList();
        availableCarSearch();
        
    }

}
