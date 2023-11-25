package net.adriansergio.appmensajeria;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class VentanaChatController {


    @FXML
    private TextArea chatPane;

    @FXML
    private TextField messageTx;

    @FXML
    private Button sendButton;

    private CallbackClientInterface amigo;
    private CallbackClientImpl client;

    @FXML
    void sendMessage(ActionEvent event) {
        try {
            if (messageTx != null) {
                amigo.mensajeCliente(client.getUsername(), messageTx.getText());
                chatPane.appendText(client.getUsername() + ": " + messageTx.getText() + "\n");
                messageTx.setText(null);
            }
        }
        catch (Exception e){};
    }

    public void updateChat(String amigoName, String message){
        Platform.runLater(() -> updateChatPane(amigoName, message));
    }

    private void updateChatPane(String amigoName, String message){
        chatPane.appendText(amigoName + ": " + message + "\n");
    }

    /*
     * Función que actua como setter para que este controlador tenga las referencias adecuadas
     * */
    public void setupChat(CallbackClientImpl cliente, CallbackClientInterface amigo, String amigoName) {
        try {
            this.client = cliente;
            this.amigo = amigo;
            this.client.addChat(amigoName, this);
        }
        catch(Exception e){
            System.out.println("Error en setupChat");
            e.printStackTrace();
        };
    }
}

