package net.adriansergio.appmensajeria;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Iterator;

public class VentanaNotifController {

    @FXML
    private ScrollPane friendList;
    private VBox caja;

    @FXML
    private TextArea log;

    @FXML
    private Label friendsCounter;

    @FXML
    private Button botonEnviarSolicitud;

    @FXML
    private Button botonSolicitud;

    @FXML
    private Button botonEliminar;

    @FXML
    private Pane panel_notif;

    @FXML
    private Button botonCambiarContrasena;

    private CallbackClient cliente;

    private CallbackClientImpl clientObj;

    /*
    * En el initialize creamos la caja donde se guardan los amigos actualmente conectados
    * */
    public void initialize(){
        caja = new VBox();
        friendList.setContent(caja);
    }
    /*
    * Función que el objeto cliente llama para actualizar el panel de mensajes del sistema
    * */
    public void updateNotifPanel(String message){
        //Actualizamos las notificaciones del sistema fuera del hilo de JavaFX, hace falta runLater
        Platform.runLater(() -> updateNotifications(message));
    }

    /*
    * Función que actualiza el panel de mensajes del sistema, añadiendo mensajes sobre usuarios
    * */
    private void updateNotifications(String message){
        log.appendText(message + "\n");
    }

    /*
    * Función que el objeto cliente llama para actualizar el contador de amigos en linea
    * */
    public void updateFriendCounter(Integer number){
        //Actualizamos las notificaciones del sistema fuera del hilo de JavaFX, hace falta runLater
        Platform.runLater(() -> updateCounter(number));
    }

    /*
    * Función que actualiza el contador de amigos en línea
    * */
    private void updateCounter(Integer number){
        friendsCounter.setText(number.toString());
    }

    /*
    * Función que el objeto cliente llama para actualizar la lista de amigos en línea
    * */
    public void updateFriendsList(String username, boolean add){
        Platform.runLater(() -> updateFriends(username, add));
    }

    /*
    * Función que añade un botón para todos los amigos que están conectados, elimina su botón correspondiente cuando se desconecta
    * */
    private void updateFriends(String username, boolean add){
        if(add){
            Button button = new Button(username);
            button.setStyle(
                    "-fx-background-color: #000000;" +
                            "-fx-text-fill: white;" +
                    "-fx-min-width: 50px");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Call the existing function when the button is clicked
                    openChatWindow(username, clientObj.obtenerAmigo(username));
                }
            });
            caja.getChildren().add(button);
        }
        else{
            //Tengo que ver todos los botones en la caja
            Iterator<Node> iterator = caja.getChildren().iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                //Si ese boton tiene el nombre del amigo que se ha desconectado, elimino botón
                if (node instanceof Button && ((Button) node).getText().equals(username)) {
                    iterator.remove();
                    //Puedo hacer break porque los nombres van a ser únicos, en el peor de los casos es O(n)
                    break;
                }
            }
        }
    }

    private void openChatWindow(String amigoName, CallbackClientInterface amigo){
        try{
            //Cargamos el archivo xml de la ventana de chat
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ventana_chat.fxml"));
            Parent root = loader.load();

            //Creamos un stage nuevo
            Stage chatStage = new Stage();
            chatStage.setScene(new Scene(root, 504, 608));
            chatStage.setTitle("Chat con " + amigoName);

            //Creamos un nuevo controller y le pasamos el cliente
            VentanaChatController controladorChat= loader.getController();
            controladorChat.setupChat(clientObj, amigo, amigoName);

            chatStage.setOnCloseRequest(CloseEvent -> controladorChat.handleCloseRequest(CloseEvent, amigoName));

            //Mostramos el menu principal
            chatStage.show();
        }
        catch (Exception e){
            System.out.println("Error al abrir chat");
            e.printStackTrace();
        }
    }

    /*
    * Función que desconecta al cliente de manera limpia al cerrar la ventana
    * */
    public void handleCloseRequest(WindowEvent event) {
        cliente.desconectar();
    }

    /*
    * Función que actua como setter para que este controlador tenga las referencias adecuadas al cliente y al objeto
    * */
    public void setupCliente(CallbackClient cliente) {
        this.cliente = cliente;
        this.clientObj = cliente.getCallbackObj();
    }

    @FXML
    void eliminarAmigo(ActionEvent event) {
        try{
            //Cargamos el archivo xml de la ventana de chat
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ventana_eliminar.fxml"));
            Parent root = loader.load();

            //Creamos un stage nuevo
            Stage eliminarStage = new Stage();
            eliminarStage.setScene(new Scene(root, 403, 98));


            //Creamos un nuevo controller y le pasamos el cliente
            VentanaEliminarController controladorEliminar= loader.getController();

            controladorEliminar.setUpEnviar(cliente);

            //Mostramos el menu principal
            eliminarStage.show();
        }
        catch (Exception e){}
    }

    @FXML
    void solicitudesAmistad(ActionEvent event) {
        try{
            //Cargamos el archivo xml de la ventana de chat
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ventana_solicitudes.fxml"));
            Parent root = loader.load();

            //Creamos un stage nuevo
            Stage solicitudesStage = new Stage();
            solicitudesStage.setScene(new Scene(root, 223, 303));


            //Creamos un nuevo controller y le pasamos el cliente
            VentanaSolicitudesController controladorSolicitudes= loader.getController();

            controladorSolicitudes.setUpSolicitud(cliente);

            cliente.setSolicitudesAmistad(controladorSolicitudes);

            cliente.anadirSolicitudes();

            //Mostramos el menu principal
            solicitudesStage.show();
        }
        catch (Exception e){
            System.out.println("Error en crear ventana solicitud");
            e.printStackTrace();
        }
    }

    @FXML
    void enviarSolicitud(ActionEvent event) {
        try{
            //Cargamos el archivo xml de la ventana de chat
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ventana_enviar.fxml"));
            Parent root = loader.load();

            //Creamos un stage nuevo
            Stage solicitudesStage = new Stage();
            solicitudesStage.setScene(new Scene(root, 403, 98));


            //Creamos un nuevo controller y le pasamos el cliente
            VentanaAnadirController controladorAnadir= loader.getController();

            controladorAnadir.setUpEnviar(cliente);

            //Mostramos el menu principal
            solicitudesStage.show();
        }
        catch (Exception e){}
    }

    @FXML
    void cambiarContrasena(ActionEvent event){
        try{
            //Cargamos el archivo xml de la ventana de chat
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ventana_cambiar.fxml"));
            Parent root = loader.load();

            //Creamos un stage nuevo
            Stage cambiarStage = new Stage();
            cambiarStage.setScene(new Scene(root, 240, 211));


            //Creamos un nuevo controller y le pasamos el cliente
            VentanaCambiarController controladorCambiar= loader.getController();

            controladorCambiar.setUpEnviar(cliente);

            //Mostramos el menu principal
            cambiarStage.show();
        }
        catch (Exception e){}
    }

}

