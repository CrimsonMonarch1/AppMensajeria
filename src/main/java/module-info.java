module net.adriansergio.appmensajeria {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.rmi;
    requires java.sql;

    opens net.adriansergio.appmensajeria to javafx.fxml;
    exports net.adriansergio.appmensajeria;
}