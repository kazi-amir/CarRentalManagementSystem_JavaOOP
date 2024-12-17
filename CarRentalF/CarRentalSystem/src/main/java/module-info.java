module com.mycompany.carrentalsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.carrentalsystem to javafx.fxml;
    exports com.mycompany.carrentalsystem;
    requires mysql.connector.java;
    requires javafx.base;
    requires javafx.graphics;
    requires java.base;
    requires jopt.simple;
}
