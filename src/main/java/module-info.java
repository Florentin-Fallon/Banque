module com.example.helloapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.helloapplication to javafx.fxml;
    exports com.example.helloapplication;
}