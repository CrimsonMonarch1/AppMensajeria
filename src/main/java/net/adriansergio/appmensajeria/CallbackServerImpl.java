package net.adriansergio.appmensajeria;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;



public class CallbackServerImpl extends UnicastRemoteObject implements CallbackServerInterface {

   private HashMap<CallbackClientInterface, String> usuariosOnline;

   public CallbackServerImpl() throws RemoteException {
      super( );
     usuariosOnline = new HashMap<>();
   }

  public String bienvenida( ) throws RemoteException {
      return("Bienvenido a la app de mensajería.");
  }

  public synchronized void conectarse(CallbackClientInterface callbackClientObject, String nombreUsuario) throws RemoteException{
      // store the callback object into the vector
      if (!(usuariosOnline.containsKey(callbackClientObject))) {
         usuariosOnline.put(callbackClientObject, nombreUsuario);
         notificarConexion(nombreUsuario);
    }
  }  

  public synchronized void desconectarse(CallbackClientInterface callbackClientObject, String nombreUsuario) throws RemoteException{
    if (usuariosOnline.containsKey(callbackClientObject)) {
        System.out.println("Servidor: " + nombreUsuario + " se ha desconectado.");
        usuariosOnline.remove(callbackClientObject);
        notificarDesconexion(nombreUsuario);
    } else {
       System.out.println("Servidor: el cliente que se quiso desconectar no estaba conectado en primer lugar.");
    }
  } 

  private void notificarConexion(String nombreUsuario) throws RemoteException{
    //Para cada cliente lo notificamos del que está online
      for (CallbackClientInterface cliente : usuariosOnline.keySet()) {
          //Le mandamos un mensaje si no es el mismo cliente
          if (!usuariosOnline.get(cliente).equals(nombreUsuario))
              cliente.mensajeServidor(nombreUsuario + " se ha conectado.");
      }
  }

    private void notificarDesconexion(String nombreUsuario) throws RemoteException{
        //Para cada cliente lo notificamos del que está online
            for (CallbackClientInterface cliente : usuariosOnline.keySet()) {
                //Le mandamos un mensaje si no es el mismo cliente
                cliente.mensajeServidor(nombreUsuario + " se ha desconectado.");
            }
    }

    private void usuariosOnline() throws RemoteException{
        for (CallbackClientInterface cliente : usuariosOnline.keySet()) {
            for (CallbackClientInterface cliente2 : usuariosOnline.keySet()){
                //Para cada cliente de la lista mandamos la lista con todos los clientes online que no sean él
            }
        }
    }
}
