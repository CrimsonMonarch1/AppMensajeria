package net.adriansergio.appmensajeria;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.WindowEvent;

public class VentanaNotifController {

    @FXML
    private ScrollPane friendList;

    @FXML
    private TextArea log;

    @FXML
    private Label friendsCounter;

    private CallbackClient cliente;

    public VentanaNotifController(){
        log = new TextArea();
        friendsCounter = new Label("0");
        friendList = new ScrollPane();
    }
    public void updateNotifPanel(String message){
        //Actualizamos las notificaciones del sistema fuera del hilo de JavaFX, hace falta runLater
        Platform.runLater(() -> updateNotifications(message));
    }

    public void updateFriendCounter(Integer number){
        //Actualizamos las notificaciones del sistema fuera del hilo de JavaFX, hace falta runLater
        Platform.runLater(() -> updateCounter(number));
    }

    private void updateNotifications(String message){
        log.appendText(message + "\n");
    }

    private void updateCounter(Integer number){
        friendsCounter.setText(number.toString());
    }

    public void handleCloseRequest(WindowEvent event) {
        cliente.desconectar();
    }

    public void setCliente(CallbackClient cliente) {
        this.cliente = cliente;
    }
}

