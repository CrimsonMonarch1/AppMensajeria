package net.adriansergio.appmensajeria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

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

    @FXML
    private Pane panel_login;

    private Image backgroundImage;

    private CallbackClient cliente;
    private CallbackClientImpl clientObj;


    public void initialize(){
        cargarImagen();
    }

    private void cargarImagen(){
        try {
            String[] imagenes = {"blood_moon_yasuo.png", "championship_zed.png", "cosmic_paladin_nautilus.jpeg", "cosmic_paladin_sion.jpeg",
                    "gwen_star_guardian_2.jpeg", "immortal_journey_kayle.jpeg", "immortal_journey_soraka.jpeg",
                    "project_zed.jpg", "coven_akali.jpeg", "project_fiora.png", "cosmic_bel_veth.jpeg", "winterblessed_thresh.jpeg", "crystalis_motus_ashe.jpeg"};
            Random generador = new Random();
            int imag_index = generador.nextInt(imagenes.length);
            String imagen = imagenes[imag_index];
            String path = "/net/adriansergio/appmensajeria/" + imagen;
            backgroundImage = new Image(path);
            panel_login.setStyle("-fx-background-image: url('" + backgroundImage.getUrl() + "');");
        }
        catch (Exception e){
            System.out.println("Error al cargar imagen de fondo");
            e.printStackTrace();
        }
    }

    @FXML
    void exit(ActionEvent event) {
    }

    /*
    * La función de login crea una nueva ventana para el menú principal e inicia el cliente, luego esconde la ventana actual
    *
    * */
    @FXML
    void login(ActionEvent event) {
        if(cliente != null){
            try {
                //Cargamos el archivo xml de la segunda ventana
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("ventana_notif.fxml"));
                Parent root = loader.load();

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

                //El cliente comienza
                cliente.start();

                //Mostramos el menu principal
                secondStage.show();

                //Escondemos la ventana actual
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * La función de register crea un nuevo cliente y lo registra en la base de datos del servidor
     * Sin un registro, el login no funciona, porque no habría cliente
     * */
    @FXML
    void register(ActionEvent event){
        if(usernameTx.getText() != null && passwordTx.getText() != null){
            cliente = new CallbackClient(usernameTx.getText(), passwordTx.getText());
            usernameTx.setText(null);
            passwordTx.setText(null);
        }
    }
}
