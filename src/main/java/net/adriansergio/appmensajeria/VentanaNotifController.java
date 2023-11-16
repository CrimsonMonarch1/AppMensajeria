package net.adriansergio.appmensajeria;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.util.HashMap;

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

    public void updateFriendsList(HashMap<CallbackClientInterface, String> amigos){
        Platform.runLater(() -> updateFriends(amigos));
    }

    private void updateNotifications(String message){
        log.appendText(message + "\n");
    }

    private void updateCounter(Integer number){
        friendsCounter.setText(number.toString());
    }

    private void updateFriends(HashMap<CallbackClientInterface, String> amigos){

    }

    public void handleCloseRequest(WindowEvent event) {
        cliente.desconectar();
    }

    public void setCliente(CallbackClient cliente) {
        this.cliente = cliente;
    }
}

