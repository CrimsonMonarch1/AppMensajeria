package net.adriansergio.appmensajeria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class VentanaLoginController {

    @FXML
    private Button exitButton;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordTx;

    @FXML
    private TextField usernameTx;

    private CallbackClient cliente;

    @FXML
    public void initialize(){
        cliente = new CallbackClient(this);
    }

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void login(ActionEvent event) {
        if(usernameTx.getText() != null){
            try {
                //Cargamos el archivo xml de la segunda ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ventana_notif.fxml"));
                Parent root = loader.load();

                //Creamos un stage nuevo
                Stage secondStage = new Stage();
                secondStage.setScene(new Scene(root, 700, 500));
                secondStage.setTitle("Menu principal");

                //Mandamos el username al cliente
                cliente.setNombreUsuario(usernameTx.getText());

                //Reseteamos la caja
                usernameTx.setText(null);

                //Creamos un nuevo controller para pasárselo al cliente
                VentanaNotifController controladorNotif = new VentanaNotifController();

                controladorNotif.setCliente(cliente);

                secondStage.setOnCloseRequest(CloseEvent -> controladorNotif.handleCloseRequest(CloseEvent));

                //Le pasamos el nuevo controller también
                cliente.setControladorNotificaciones(controladorNotif);

                //El cliente comienza
                cliente.start();

                //Lo mostramos
                secondStage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
