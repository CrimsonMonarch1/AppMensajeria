package net.adriansergio.appmensajeria;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class CallbackClientImpl extends UnicastRemoteObject implements CallbackClientInterface {

   private HashMap<String, CallbackClientInterface> amigosOnline;
   private HashMap<String, CallbackClientInterface> amigos;

   private VentanaNotifController controladorMenu;

   private HashMap<String, VentanaChatController> ventanasChat;

   private String username;

   private HashMap<String, String> conversations;

   public CallbackClientImpl(VentanaNotifController controladorMenu, String username) throws RemoteException {
      super();
      amigos = new HashMap<>();
      amigosOnline = new HashMap<>();
      this.controladorMenu = controladorMenu;
      this.username = username;
      ventanasChat = new HashMap<>();
   }

   public void mensajeServidor(String message) {
      System.out.println(message);
      controladorMenu.updateNotifPanel(message);
   }

   public void addOnlineFriend(String username, CallbackClientInterface amigo) throws java.rmi.RemoteException{
      amigosOnline.put(username, amigo);
      controladorMenu.updateFriendsList(username, true);
      controladorMenu.updateFriendCounter(amigosOnline.size());
   }

   public void removeOnlineFriend(String username, CallbackClientInterface amigo) throws java.rmi.RemoteException{
      if(amigosOnline.get(username) != null){
         amigosOnline.remove(username);
         controladorMenu.updateFriendCounter(amigosOnline.size());
         controladorMenu.updateFriendsList(username, false);
      }
   }

   private void mostrarMensaje(String message){

   }

   public void addChat(String amigo, VentanaChatController controlador){
      ventanasChat.put(amigo, controlador);
   }

   public CallbackClientInterface obtenerAmigo(String username){
      return this.amigosOnline.get(username);
   }

   public void mensajeCliente(String sender, String message) throws java.rmi.RemoteException {
      System.out.println(message);
      updateChat(sender, message);
   }

   private void updateChat(String amigoName, String message){
      if(ventanasChat.get(amigoName) != null)
         ventanasChat.get(amigoName).updateChat(message);
   }

   public String getUsername(){
      return this.username;
   }

   public String getConversation(String friendName){
      return conversations.get(friendName);
   }

   public void setConversation(String friendName, String chat){
      if(conversations.get(friendName) != null){
         conversations.replace(friendName, chat);
      }
      else{
         conversations.put(friendName,chat);
      }
   }
}