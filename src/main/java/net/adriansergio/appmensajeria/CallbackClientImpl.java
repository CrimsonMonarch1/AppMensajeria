package net.adriansergio.appmensajeria;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {

   private final VentanaNotifController controlador;

   private HashMap<CallbackClientInterface, String> amigos;

   public CallbackClientImpl(VentanaNotifController controlador) throws RemoteException {
      super();
      this.controlador = controlador;
      amigos = new HashMap<>();
   }

   public void mensajeServidor(String message) {
      System.out.println(message);
      if(message != null)
         controlador.updateNotifPanel(message);
   }

   public void actualizarAmigos(HashMap<CallbackClientInterface, String> amigos){
      if(amigos != null){
         this.amigos = amigos;
         this.controlador.updateFriendCounter(this.amigos.size() - 1);
      }
   }

   private void mostrarMensaje(String message){

   }

   public void mensajeCliente(String message){}

}