package net.adriansergio.appmensajeria;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;



public class CallbackServerImpl extends UnicastRemoteObject implements CallbackServerInterface {

   private HashMap<String, CallbackClientInterface> usuariosOnline;

   public CallbackServerImpl() throws RemoteException {
      super( );
     usuariosOnline = new HashMap<>();
   }

  public String bienvenida( ) throws RemoteException {
      return("Bienvenido a la app de mensajería.");
  }

  public synchronized void conectarse(CallbackClientInterface callbackClientObject, String nombreUsuario) throws RemoteException{
      // store the callback object into the vector
      if (!(usuariosOnline.containsKey(nombreUsuario))) {
          System.out.println("Servidor: " + nombreUsuario + " se ha conectado.");
          usuariosOnline.put(nombreUsuario, callbackClientObject);
          notificarConexion(nombreUsuario);
          addOnlineFriends(nombreUsuario);
    }
  }  

  public synchronized void desconectarse(CallbackClientInterface callbackClientObject, String nombreUsuario) throws RemoteException{
    if (usuariosOnline.containsKey(nombreUsuario)) {
        System.out.println("Servidor: " + nombreUsuario + " se ha desconectado.");
        removeOnlineFriends(nombreUsuario);
        usuariosOnline.remove(nombreUsuario);
        notificarDesconexion(nombreUsuario);
    } else {
       System.out.println("Servidor: el cliente que se quiso desconectar no estaba conectado en primer lugar.");
    }
  } 

  private void notificarConexion(String nombreUsuario) throws RemoteException{
    //Para cada cliente lo notificamos del que está online
      for (String username : usuariosOnline.keySet()) {
          //Le mandamos un mensaje si no es el mismo cliente
          if (!username.equals(nombreUsuario)) {
              usuariosOnline.get(username).mensajeServidor(nombreUsuario + " se ha conectado.");
          }
      }
  }

    private void notificarDesconexion(String nombreUsuario) throws RemoteException{
        //Para cada cliente lo notificamos del que está online
        for (String username : usuariosOnline.keySet()) {
            //Le mandamos un mensaje si no es el mismo cliente
            usuariosOnline.get(username).mensajeServidor(nombreUsuario + " se ha desconectado.");
        }
    }

    private void addOnlineFriends(String nombreUsuario) throws RemoteException{
       //Pasamos por todos los clientes
        for (String username : usuariosOnline.keySet()) {
            //Si el cliente es diferente de si mismo, se añade como amigo
            if (!username.equals(nombreUsuario)) {
                //Cada cliente añade al cliente actual y el cliente actual añade a cada cliente como amigos
                usuariosOnline.get(nombreUsuario).addOnlineFriend(username, usuariosOnline.get(username));
                usuariosOnline.get(username).addOnlineFriend(nombreUsuario, usuariosOnline.get(nombreUsuario));
            }
        }
    }

    private void removeOnlineFriends(String nombreUsuario) throws RemoteException{
        //Pasamos por todos los clientes
        for (String username : usuariosOnline.keySet()) {
            //Hay que quitar al cliente que se quiere desconectar de las listas de amigos online de todos los clientes
            if (!username.equals(nombreUsuario)) {
                //Cada cliente elimina al que se acaba de desconectar
                usuariosOnline.get(username).removeOnlineFriend(nombreUsuario, usuariosOnline.get(nombreUsuario));
            }
        }
    }
}
