package net.adriansergio.appmensajeria;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;


public interface CallbackServerInterface extends Remote {

  public String bienvenida() throws RemoteException;


  public void conectarse(CallbackClientInterface callbackClientObject, String nombreUsuario) throws RemoteException;


  public void desconectarse(CallbackClientInterface callbackClientObject, String nombreUsuario) throws RemoteException;

  public boolean introducirUsuarios(String nome, String contrasinal) throws RemoteException, SQLException;
  public boolean inicioSesion(String usuario, String cont) throws RemoteException;

  public CallbackClientInterface obtenerUsuario(String usuario)  throws RemoteException;

  public boolean usuarioNoExistente(String usuario) throws RemoteException;

  public boolean enviarSolicitudAmistad(String solicitante, String receptor) throws RemoteException, SQLException;

  public ArrayList<String> consultarSolicitudesAmistad(String nome1) throws RemoteException, SQLException;

  public void aceptarAmistad(String usuario, String solicitante) throws RemoteException, SQLException;

  public void eliminarAmigo(String usuario, String amigo) throws RemoteException, SQLException;

  public void eliminarSolicitudAmistad(String nome1, String nome2) throws RemoteException, SQLException;

  public ArrayList<String> consultarAmigos(String nome1) throws RemoteException, SQLException;
}
