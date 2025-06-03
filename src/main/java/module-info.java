module com.example.helloapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.helloapplication to javafx.fxml;
    exports com.example.helloapplication;
    opens com.example.helloapplication.components to javafx.fxml;
    exports com.example.helloapplication.components;
}
