package com.mycompany.carrentalsystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;                    
import javafx.scene.input.MouseEvent;

public class PrimaryController {

    @FXML
    private AnchorPane LogInForm;

    @FXML
    private Button close;    
    
    @FXML
    private CheckBox isAdmin;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField password;

    @FXML
    private Button primaryButton;

    @FXML
    private TextField username;
    
    //Database tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    
    private double x = 0;
    private double y = 0;
    
    public void loginAdmin(){

        if(isAdmin.isSelected()){
            String sql = "SELECT * FROM admin WHERE username = ? and password = ?";
            
            connect = database.connectDB();

            try{

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, username.getText());
                prepare.setString(2, password.getText());

                connect = database.connectDB();

                result = prepare.executeQuery();

                Alert alert;

                if(username.getText().isEmpty()||password.getText().isEmpty()){

                    alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all fields!");
                    alert.showAndWait();

                }
                else{

                    if(result.next()){

                        getData.username = username.getText();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Welcome Admin..");
                        alert.showAndWait();

                        loginBtn.getScene().getWindow().hide();

                        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
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
                    else{

                        alert = new Alert(AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong Username/Password.");
                        alert.showAndWait();
                    }
                }

            }catch(IOException | SQLException e){}
        }
        else{
            
            String sql = "SELECT * FROM customer WHERE username = ? and password = ?";
            
            connect = database.connectDB();

            try{

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, username.getText());
                prepare.setString(2, password.getText());

                connect = database.connectDB();

                result = prepare.executeQuery();

                Alert alert;

                if(username.getText().isEmpty()||password.getText().isEmpty()){

                    alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all fields!");
                    alert.showAndWait();

                }
                else{

                    if(result.next()){

                        getData.username = username.getText();

                        alert = new Alert(AlertType.INFORMATION);
                        alert.setHeaderText(null);
                        alert.setContentText("Logged in Successfully!");
                        alert.showAndWait();

                        loginBtn.getScene().getWindow().hide();

                        Parent root = FXMLLoader.load(getClass().getResource("customerDashboard.fxml"));
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
                    else{

                        alert = new Alert(AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong Username/Password.");
                        alert.showAndWait();
                    }
                }

            }catch(IOException | SQLException e){}
        }
        
    }
    @FXML
    public void close(){
        System.exit(0);
    }
       
    @FXML
    private void switchToSecondary() throws IOException {
         loginBtn.getScene().getWindow().hide();
                    
        //link dashboard
        Parent root = FXMLLoader.load(getClass().getResource("secondary.fxml"));
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
}
