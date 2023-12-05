package net.adriansergio.appmensajeria;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;

public class VentanaSolicitudesController {

    private CallbackClient cliente;
    private VBox caja;
    @FXML
    private ScrollPane panelSolicitudes;

    /*
     * En el initialize creamos la caja donde se guardan los amigos actualmente conectados
     * */
    public void initialize(){
        caja = new VBox();
        panelSolicitudes.setContent(caja);
    }

    public void setUpSolicitud(CallbackClient cliente){
        this.cliente=cliente;
    }

    public void anadirSolicitudes(String username){
        Platform.runLater(()-> {
            Button button = new Button(username);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //Cargamos el archivo xml de la segunda ventana

                    try {
                        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ventana_sino.fxml"));
                        Parent root = loader.load();
                        Stage solicitudStage = new Stage();
                        solicitudStage.setScene(new Scene(root, 303, 64));


                        //Creamos un nuevo controller y le pasamos el cliente
                        VentanaSiNoController controladorSolicitud= loader.getController();
                        controladorSolicitud.setUpSolicitud(cliente);
                        controladorSolicitud.setUpAmistad(VentanaSolicitudesController.this);
                        controladorSolicitud.setAmigo(button.getText());

                        //Mostramos el menu principal
                        solicitudStage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            caja.getChildren().add(button);
        });
    }

    public void eliminarSolitud(String username){
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
