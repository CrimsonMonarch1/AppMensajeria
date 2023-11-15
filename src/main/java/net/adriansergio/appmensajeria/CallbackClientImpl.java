package net.adriansergio.appmensajeria;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CountDownLatch;


public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {

   private CountDownLatch lock;

   private VentanaNotifController controlador;

   public CallbackClientImpl(CountDownLatch lock, VentanaNotifController controlador) throws RemoteException {
      super();
      this.lock = lock;
      this.controlador = controlador;
   }

   public void mensajeServidor(String message) {
      System.out.println(message);
      //this.controlador.updateNotifPanel(message);
   }

   private void mostrarMensaje(String message){

   }

   public void mensajeCliente(String message){}

}