package com.mycompany.carrentalsystem;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SecondaryController {

    @FXML
    private AnchorPane SignInForm;

    @FXML
    private Button close;

    @FXML
    private Button registerBTN;

    @FXML
    private TextArea register_address;

    @FXML
    private TextField register_email;

    @FXML
    private TextField register_fname;

    @FXML
    private TextField register_lname;

    @FXML
    private TextField register_mobile;

    
    @FXML
    private ComboBox<?> register_gender;
    
    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_username;

    @FXML
    private Button signInBtn;

    private double x = 0, y = 0;
    
    @FXML
    public void close(){
        System.exit(0);
    }
    
    @FXML
    private void switchToPrimary() throws IOException {
         signInBtn.getScene().getWindow().hide();
                    
        //link dashboard
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
    
    
    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    
//    private Connection connect2;
//    private PreparedStatement prepare2;
//    private ResultSet result2;
    
    private String[] genderList = {"Male", "Female"};
    public void userGender() {
        List<String> listG = new ArrayList<>();

        listG.addAll(Arrays.asList(genderList));
        
        ObservableList listData = FXCollections.observableArrayList(listG);

        register_gender.setItems(listData);
    }
    
    public void addNewUser() throws IOException, SQLException{
        String sq2 = "SELECT customerid FROM customer";
        connect = database.connectDB();
        int totalcustomer = 0;
        try{
            prepare = connect.prepareStatement(sq2);
            result = prepare.executeQuery();
            while(result.next()){
                totalcustomer++;
            }
        }catch(SQLException e){
            System.out.println("error paisi reh");
        }
        totalcustomer += 1;
        String sql = "INSERT INTO customer (customerid, firstName, lastName, username, password, email, gender, mobile, address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        connect = database.connectDB();
        
        try{
            Alert alert;
            if(register_fname.getText().isEmpty() 
                    || register_lname.getText().isEmpty()
                    || register_email.getText().isEmpty()
                    || register_username.getText().isEmpty()
                    || register_mobile.getText().isEmpty()
                    || register_password.getText().isEmpty()
                    || register_password.getText().isEmpty()
                    || register_address.getText().isEmpty()
                    || register_gender.getSelectionModel().getSelectedItem()==null){
                
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else{
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, Integer.toString(totalcustomer));
                prepare.setString(2, register_fname.getText());
                prepare.setString(3, register_lname.getText());
                prepare.setString(4, register_username.getText());
                prepare.setString(5, register_password.getText());
                prepare.setString(6, register_email.getText());
                prepare.setString(7,  (String)register_gender.getSelectionModel().getSelectedItem());
                prepare.setString(8, register_mobile.getText());
                prepare.setString(9, register_address.getText());
                
                prepare.executeUpdate();
                
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("User account created. Please Log-in now.");
                alert.showAndWait();
                
                switchToPrimary();
            }
        }catch(SQLException e){};
    }
    
    
}