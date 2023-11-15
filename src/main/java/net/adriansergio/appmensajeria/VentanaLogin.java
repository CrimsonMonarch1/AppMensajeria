package net.adriansergio.appmensajeria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class VentanaLogin extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VentanaLogin.class.getResource("menu_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        stage.setTitle("Login");
        stage.setScene(scene);
        VentanaLoginController controller = fxmlLoader.getController();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}