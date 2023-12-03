package net.adriansergio.appmensajeria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class VentanaSiNoController{

    private CallbackClient cliente;

    private VentanaSolicitudesController solicitudes;

    private String amigo;

    @FXML
    private Button BotonAceptar;

    @FXML
    private Button botonRechazar;

    public void setUpSolicitud(CallbackClient cliente){
        this.cliente=cliente;
    }

    public void setUpAmistad(VentanaSolicitudesController solicitudes){
        this.solicitudes=solicitudes;
    }

    public void setAmigo(String amigo){
        this.amigo=amigo;
    }
    @FXML
    void aceptarSolicitud(ActionEvent event) {
        cliente.aceptarSolicitudes(amigo);
        solicitudes.eliminarSolitud(amigo);
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    void rechazarSolicitud(ActionEvent event) {
        cliente.rechazarSolicitudes(amigo);
        solicitudes.eliminarSolitud(amigo);
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

}

