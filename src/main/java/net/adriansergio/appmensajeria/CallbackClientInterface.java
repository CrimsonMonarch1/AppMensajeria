package net.adriansergio.appmensajeria;

import java.util.ArrayList;
import java.util.HashMap;

public interface CallbackClientInterface
  extends java.rmi.Remote{
    public void mensajeServidor(String message) throws java.rmi.RemoteException;

    public void actualizarAmigos(HashMap<CallbackClientInterface, String> amigos) throws java.rmi.RemoteException;

    public void mensajeCliente(String message) throws java.rmi.RemoteException;
}