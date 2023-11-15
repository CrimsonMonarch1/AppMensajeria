package net.adriansergio.appmensajeria;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {

   private final VentanaNotifController controlador;

   private ArrayList<CallbackClientInterface> amigos;

   public CallbackClientImpl(VentanaNotifController controlador) throws RemoteException {
      super();
      this.controlador = controlador;
      amigos = new ArrayList<>();
   }

   public void mensajeServidor(String message) {
      System.out.println(message);
      if(message != null)
         controlador.updateNotifPanel(message);
   }

   public void actualizarAmigos(ArrayList<CallbackClientInterface> amigos){
      if(amigos != null){
         this.amigos = amigos;
         this.controlador.updateFriendCounter(this.amigos.size());
      }
   }

   private void mostrarMensaje(String message){

   }

   public void mensajeCliente(String message){}

}