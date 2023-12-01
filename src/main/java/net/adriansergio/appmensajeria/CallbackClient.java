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

  private VentanaLoginController controladorLogin;

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

      if(h.usuarioNoExistente(nombreUsuario)){
        //Ventanita error regitro
        controladorLogin.ventanaError("Usuario no registrado.");
      }
      else if(!h.inicioSesion(nombreUsuario, password)){
        //Ventanita error regitro
        controladorLogin.ventanaError("Contraseña del usuario mal introducidos.");
      }
      else{
        controladorLogin.logear();
      }
    }
    catch (Exception e) {
      System.out.println("Exception in CallbackClient: " + e);
      e.printStackTrace();
    }

  }

  public CallbackClient(String nombreUsuario, String password, VentanaLoginController controladorLogin){
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

      if(!h.introducirUsuarios(nombreUsuario, password)){
        //Ventanita error regitro
        controladorLogin.ventanaError("Usuario ya registrado.");
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
