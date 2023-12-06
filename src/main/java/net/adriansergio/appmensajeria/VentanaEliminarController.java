package net.adriansergio.appmensajeria;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class VentanaEliminarController {

    private CallbackClient cliente;

    @FXML
    private Button botonEliminar;

    @FXML
    private TextField textoUsuario;

    @FXML
    private Pane panel;

    public void setUpEnviar(CallbackClient cliente){
        this.cliente=cliente;
    }
    @FXML
    void eliminarAmigo(ActionEvent event) {
        cliente.eliminarAmigo(textoUsuario.getText());
        textoUsuario.setText(null);
    }

}
