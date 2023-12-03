package net.adriansergio.appmensajeria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class VentanaAnadirController {

    private CallbackClient cliente;

    @FXML
    private Button botonEnviar;

    @FXML
    private TextField textoUsuario;

    public void setUpEnviar(CallbackClient cliente){
        this.cliente=cliente;
    }
    @FXML
    void enviarSolicitud(ActionEvent event) {

        cliente.enviarSolicitudAmistad(textoUsuario.getText());
        textoUsuario.setText(null);
    }



}
