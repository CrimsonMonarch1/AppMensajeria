package net.adriansergio.appmensajeria;

import java.util.ArrayList;

public interface CallbackClientInterface
  extends java.rmi.Remote{
    public void mensajeServidor(String message) throws java.rmi.RemoteException;

    public void actualizarAmigos(ArrayList<CallbackClientInterface> amigos) throws java.rmi.RemoteException;

    public void mensajeCliente(String message) throws java.rmi.RemoteException;
}