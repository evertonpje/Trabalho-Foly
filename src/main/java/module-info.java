module dev.lucasm.pratice.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens dev.lucasm.pratice.demo1 to javafx.fxml;
    exports dev.lucasm.pratice.demo1;
}