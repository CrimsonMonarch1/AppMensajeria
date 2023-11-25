package net.adriansergio.appmensajeria;

public interface CallbackClientInterface extends java.rmi.Remote{
    public void mensajeServidor(String message) throws java.rmi.RemoteException;

    public void addOnlineFriend(String username, CallbackClientInterface amigo) throws java.rmi.RemoteException;

    public void removeOnlineFriend(String username, CallbackClientInterface amigo) throws java.rmi.RemoteException;

    public void mensajeCliente(String sender, String message) throws java.rmi.RemoteException;

}