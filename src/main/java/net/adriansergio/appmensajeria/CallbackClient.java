package net.adriansergio.appmensajeria;

import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;


public class CallbackClient extends Thread {

  private String nombreUsuario;

  private String password;

  private ArrayList<String> solicitudesAmistad;

  private CallbackClientInterface callbackObj;

  private CallbackServerInterface h;

  private CountDownLatch lock;

  private VentanaNotifController controladorMenu;

  private VentanaLoginController controladorLogin;

  private VentanaSolicitudesController controladorSolicitudes;

  public CallbackClient(String nombreUsuario, String password, VentanaLoginController controladorLogin, boolean login){
    this.nombreUsuario = nombreUsuario;
    this.password = password;
    this.controladorLogin= controladorLogin;
    try {
      int RMIPort;
      String hostName;
      //Lector de input
      InputStreamReader is = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(is);
      //El enlace lo he hardcodeado porque en una app real el cliente simplemente se conecta y ya
      String registryURL = "rmi://localhost:6789/callback";
      //Busco el objeto servidor al que conectarme
      h = (CallbackServerInterface)Naming.lookup(registryURL);

      if(login){
        if(h.usuarioNoExistente(nombreUsuario)){
          //Ventanita error regitro
          controladorLogin.ventanaError("Usuario no registrado.");
        }
        else if(!h.inicioSesion(nombreUsuario, password)){
          //Ventanita error regitro
          controladorLogin.ventanaError("Contraseña del usuario mal introducidos.");
        }
      }
      else{
        if(!h.introducirUsuarios(nombreUsuario, password)){
          //Ventanita error regitro
          controladorLogin.ventanaError("Usuario ya registrado.");
        }
      }
    }
    catch (Exception e) {
      System.out.println("Exception in CallbackClient: " + e);
      e.printStackTrace();
    }

  }

  public void run()
  {
    try {
        //controladorLogin.logear();
        System.out.println(h.bienvenida());
        lock = new CountDownLatch(1);
        //Asignamos el controlador de menu al cliente
        controladorMenu.setupCliente(this);
        //Creeo objeto cliente para interactuar con el servidor
        callbackObj = new CallbackClientImpl(controladorMenu, nombreUsuario);
        controladorMenu.setupCliente(this);
        //Conectarse al servicio
        h.conectarse(callbackObj, nombreUsuario);
        System.out.println("Cliente: conectado.");
        lock.await();
        h.desconectarse(callbackObj, nombreUsuario);
        System.out.println("Cliente: desconectado.");
        System.exit(0);

    }
    catch (Exception e) {
      System.out.println("Exception in CallbackClient: " + e);
      e.printStackTrace();
    }
  }

  public void desconectar(){
    try {
      lock.countDown();
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

  public void logearCliente(ActionEvent event){
    this.controladorLogin.logear(event);
  }

  public CallbackClientImpl getCallbackObj(){
    return (CallbackClientImpl)this.callbackObj;
  }

  public void setControladorMenu(VentanaNotifController controladorMenu) {
    this.controladorMenu = controladorMenu;
  }

  public void setSolicitudesAmistad(VentanaSolicitudesController controladorSolicitudes){this.controladorSolicitudes= controladorSolicitudes;}

  public void enviarSolicitudAmistad(String username){
    try {
      if(h.usuarioNoExistente(username)) {
        controladorLogin.ventanaError("Usuario no existente");
      }
      else {
          //Añadir en base de datos solicitudes de amistad
        if(!h.enviarSolicitudAmistad(nombreUsuario, username)){
          controladorLogin.ventanaError("Este usuario ya es tu amigo.");
        }
        ArrayList<String> solicitudes = h.consultarSolicitudesAmistad(nombreUsuario);
        if(solicitudes.contains(username)){
          controladorLogin.ventanaError("Ya tienes una solicitud de amistad de este usuario. Acéptala");
        }
      }

    } catch (RemoteException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

  }

  public void eliminarAmigo(String username){
    try {
      ArrayList<String> amigos;
      if(h.usuarioNoExistente(username)) {
        controladorLogin.ventanaError("Usuario no existente");
      }
      else {
        amigos=h.consultarAmigos(nombreUsuario);
        if(amigos.contains(username)){
          CallbackClientInterface cliente = h.obtenerUsuario(username);
          h.eliminarAmigo(nombreUsuario, username);
          callbackObj.removeOnlineFriend(username, cliente);
          cliente.removeOnlineFriend(nombreUsuario, callbackObj);
        }
        else{
          controladorLogin.ventanaError("No tienes ningún amigo con ese nombre.");
        }
      }

    } catch (RemoteException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public void anadirSolicitudes(){
    try {
      solicitudesAmistad= h.consultarSolicitudesAmistad(nombreUsuario);
      for (String username: solicitudesAmistad){
        System.out.println(username);
        controladorSolicitudes.anadirSolicitudes(username);
      }
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public void aceptarSolicitudes(String amigo){
    try {
      CallbackClientInterface cliente = h.obtenerUsuario(amigo);
      h.aceptarAmistad(nombreUsuario, amigo);
      h.eliminarSolicitudAmistad(amigo, nombreUsuario);
      callbackObj.addOnlineFriend(amigo, cliente);
      cliente.addOnlineFriend(nombreUsuario, callbackObj);
      cliente.mensajeServidor(nombreUsuario+" aceptó tu solicitud de amistad");
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void rechazarSolicitudes(String amigo){
    try {
      h.eliminarSolicitudAmistad(amigo, nombreUsuario);
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void cambiarContrasena(String contrasena){
    try {
      if (password.equals(contrasena)) {

        controladorLogin.ventanaError("Contraseña nueva y actual iguales");
      }
      else{
        password=contrasena;
        h.cambiarContrasena(nombreUsuario, password);
      }
    } catch (RemoteException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
