package net.adriansergio.appmensajeria;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

public class VentanaNotifController {

    @FXML
    private ScrollPane friendList;

    @FXML
    private ScrollPane notifPanel;

    private VBox contenido;

    private CallbackClient cliente;

    @FXML
    public void initialize(){
        contenido = new VBox();
        notifPanel.setContent(contenido);
    }
    public void updateNotifPanel(String message){
        Label label = new Label(message);
        contenido.getChildren().add(label);
    }

    public void handleCloseRequest(WindowEvent event) {
        cliente.desconectar();
    }

    public void setCliente(CallbackClient cliente) {
        this.cliente = cliente;
    }
}

