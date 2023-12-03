package net.adriansergio.appmensajeria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

public class VentanaLoginController {

    @FXML
    private Button exitButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField passwordTx;

    @FXML
    private TextField usernameTx;

    private CallbackClient cliente;
    private CallbackClientImpl clientObj;

    @FXML
    void exit(ActionEvent event) {
    }

    /*
    * La función de login crea una nueva ventana para el menú principal e inicia el cliente, luego esconde la ventana actual
    *
    * */
    @FXML
    void login(ActionEvent event) {
        if(usernameTx.getText() != null && passwordTx.getText() != null){

                cliente = new CallbackClient(usernameTx.getText(), passwordTx.getText(), this, true);

                passwordTx.setText(null);

        }
        else{
            ventanaError("Falta usuario o contraseña");
        }
    }

    void logear(){
        try {

            //Cargamos el archivo xml de la segunda ventana
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ventana_notif.fxml"));
            Parent root = loader.load();
            Platform.runLater(()->{
                //Creamos un stage nuevo
                Stage secondStage = new Stage();
                secondStage.setScene(new Scene(root, 700, 500));
                if(usernameTx != null)
                    secondStage.setTitle("Menu principal - " + usernameTx.getText());

                //Reseteamos la caja
                usernameTx.setText(null);
                //Creamos un nuevo controller y le pasamos el cliente
                VentanaNotifController controladorMenu= loader.getController();

                //Gestionar qué pasa cuando apagamos la ventana
                secondStage.setOnCloseRequest(CloseEvent -> controladorMenu.handleCloseRequest(CloseEvent));

                //Pasamos el controlador del menu principal al cliente
                cliente.setControladorMenu(controladorMenu);

                cliente.start();

                //Mostramos el menu principal
                secondStage.show();

            });

            //Escondemos la ventana actual
            //((Node)(event.getSource())).getScene().getWindow().hide();---------------------------------------------------
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * La función de register crea un nuevo cliente y lo registra en la base de datos del servidor
     * Sin un registro, el login no funciona, porque no habría cliente
     * */
    @FXML
    void register(ActionEvent event){
        if(usernameTx.getText() != null && passwordTx.getText().length()>4){

            cliente = new CallbackClient(usernameTx.getText(), passwordTx.getText(), this);
            usernameTx.setText(null);
            passwordTx.setText(null);
        }
        else if (usernameTx.getText()==null){
            ventanaError("Se debe introducir nombre de usuario");
        }
        else{
            ventanaError("Contraseña no válida. Mínimo 5 caracteres.");
        }
    }

    public void ventanaError(String mensaje) {
        Platform.runLater(()->{
            usernameTx.setText(null);
            passwordTx.setText(null);
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText(null);
            alerta.setContentText(mensaje);

            alerta.showAndWait();
        });
    }
}
