package net.adriansergio.appmensajeria;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

public class VentanaChatController {


    @FXML
    private TextArea chatPane;

    @FXML
    private TextField messageTx;

    @FXML
    private Button sendButton;

    @FXML
    private Pane chat_pane;

    private CallbackClientInterface amigo;
    private CallbackClientImpl client;

    private String conversation;

    @FXML
    void sendMessage(ActionEvent event) {
        try {
            if (messageTx != null) {
                amigo.mensajeCliente(client.getUsername(), client.getUsername() + ": " + messageTx.getText() + "\n");
                chatPane.appendText(client.getUsername() + ": " + messageTx.getText() + "\n");
                messageTx.setText(null);
                conversation = messageTx.getText();
            }
        }
        catch (Exception e){};
    }

    public void updateChat(String message){
        Platform.runLater(() -> updateChatPane(message));
    }

    private void updateChatPane(String message){
        chatPane.appendText(message);
    }

    /*
     * Funcion que actua como setter para que este controlador tenga las referencias adecuadas
     * */
    public void setupChat(CallbackClientImpl cliente, CallbackClientInterface amigo, String amigoName) {
        try {
            this.client = cliente;
            this.amigo = amigo;
            this.client.addChat(amigoName, this);
            cargarChat(amigoName);
        }
        catch(Exception e){
            System.out.println("Error en setupChat");
            e.printStackTrace();
        };
    }

    public void cargarChat(String friendName){
        chatPane.setText(this.client.getConversation(friendName));
    }

    public void handleCloseRequest(WindowEvent closeEvent, String friendName) {
        conversation = chatPane.getText();
        client.setConversation(friendName, chatPane.getText());
    }
}

