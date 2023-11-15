package net.adriansergio.appmensajeria;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface CallbackServerInterface extends Remote {

  public String bienvenida() throws RemoteException;


  public void conectarse(CallbackClientInterface callbackClientObject, String nombreUsuario) throws RemoteException;


  public void desconectarse(CallbackClientInterface callbackClientObject, String nombreUsuario) throws RemoteException;
}
