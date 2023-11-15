package net.adriansergio.appmensajeria;

public interface CallbackClientInterface 
  extends java.rmi.Remote{
    public void mensajeServidor(String message) throws java.rmi.RemoteException;

    public void mensajeCliente(String message) throws java.rmi.RemoteException;
}