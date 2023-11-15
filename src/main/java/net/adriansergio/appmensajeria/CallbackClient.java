package net.adriansergio.appmensajeria;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.concurrent.CountDownLatch;


public class CallbackClient extends Thread {

  private String nombreUsuario;
  private final VentanaLoginController controladorLogin;

  private CallbackClientInterface callbackObj = null;

  private CallbackServerInterface h = null;

  private VentanaNotifController controladorNotif;
  public CallbackClient(VentanaLoginController controlador) {
    this.controladorLogin = controlador;
  }

  public void run()
  {
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
      System.out.println(h.bienvenida());
      CountDownLatch lock = new CountDownLatch(1);
      //Creeo objeto cliente para interactuar con el servidor
      callbackObj = new CallbackClientImpl(lock, controladorNotif);
      //Conectarse al servicio
      h.conectarse(callbackObj, nombreUsuario);
      System.out.println("Cliente: conectado.");
      lock.await();
      h.desconectarse(callbackObj, nombreUsuario);
      System.out.println("Cliente: desconectado.");
    }
    catch (Exception e) {
      System.out.println("Exception in CallbackClient: " + e);
      e.printStackTrace();
    }
  }

  public void setNombreUsuario(String nombreUsuario){
    this.nombreUsuario = nombreUsuario;
  }

  public void setControladorNotificaciones(VentanaNotifController controladorNotif){
    this.controladorNotif = controladorNotif;
  }

  public void desconectar(){
    try {
      h.desconectarse(callbackObj, nombreUsuario);
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
}
