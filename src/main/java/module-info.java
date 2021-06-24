module com.shamilsdq.socketfiles {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.shamilsdq.socketfiles to javafx.fxml;
    exports com.shamilsdq.socketfiles;
}
