module com.gui.hotel_management_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens com.gui.hotel_management_javafx to javafx.fxml;
    exports com.gui.hotel_management_javafx;
}