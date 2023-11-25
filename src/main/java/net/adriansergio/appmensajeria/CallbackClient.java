package net.adriansergio.appmensajeria;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.concurrent.CountDownLatch;


public class CallbackClient extends Thread {

  private String nombreUsuario;

  private String password;

  private CallbackClientInterface callbackObj;

  private CallbackServerInterface h;

  private CountDownLatch lock;

  private VentanaNotifController controladorMenu;

  public CallbackClient(String nombreUsuario, String password){
    this.nombreUsuario = nombreUsuario;
    this.password = password;
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
      lock = new CountDownLatch(1);
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

  public CallbackClientImpl getCallbackObj(){
    return (CallbackClientImpl)this.callbackObj;
  }

  public void setControladorMenu(VentanaNotifController controladorMenu) {
    this.controladorMenu = controladorMenu;
  }
}
