package net.adriansergio.appmensajeria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class VentanaAnadirController {

    private CallbackClient cliente;

    @FXML
    private Button botonEnviar;

    @FXML
    private TextField textoUsuario;

    @FXML
    private Pane panel;

    public void setUpEnviar(CallbackClient cliente){
        this.cliente=cliente;
    }
    @FXML
    void enviarSolicitud(ActionEvent event) {

        cliente.enviarSolicitudAmistad(textoUsuario.getText());
        textoUsuario.setText(null);
    }



}
