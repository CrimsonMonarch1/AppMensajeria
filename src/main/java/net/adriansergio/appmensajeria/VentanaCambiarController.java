package net.adriansergio.appmensajeria;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class VentanaCambiarController {

    private CallbackClient cliente;
    @FXML
    private Button botonNuevaContrasena;

    @FXML
    private TextField confirmarContrasena;

    @FXML
    private TextField contrasenaNueva;

    @FXML
    void aceptarContrasena(ActionEvent event) {
        if(contrasenaNueva.getText().equals(confirmarContrasena.getText())){
            cliente.cambiarContrasena(contrasenaNueva.getText());
        }
        else{
            ventanaError("La contraseña y su confirmación son diferentes");
        }
        contrasenaNueva.setText(null);
        confirmarContrasena.setText(null);
    }

    public void setUpEnviar(CallbackClient cliente){
        this.cliente=cliente;
    }

    void ventanaError(String mensaje) {
        Platform.runLater(()->{
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText(null);
            alerta.setContentText(mensaje);

            alerta.showAndWait();
        });
    }

}