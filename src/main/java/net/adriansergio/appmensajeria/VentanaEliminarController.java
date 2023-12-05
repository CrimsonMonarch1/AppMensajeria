package net.adriansergio.appmensajeria;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class VentanaEliminarController {

    private CallbackClient cliente;

    @FXML
    private Button botonEliminar;

    @FXML
    private TextField textoUsuario;

    public void setUpEnviar(CallbackClient cliente){
        this.cliente=cliente;
    }
    @FXML
    void eliminarAmigo(ActionEvent event) {
        cliente.eliminarAmigo(textoUsuario.getText());
        textoUsuario.setText(null);
    }

}
