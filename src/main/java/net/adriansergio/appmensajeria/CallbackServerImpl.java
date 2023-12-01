package net.adriansergio.appmensajeria;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;



public class CallbackServerImpl extends UnicastRemoteObject implements CallbackServerInterface {

   private HashMap<String, CallbackClientInterface> usuariosOnline;

   private ConexionBDD conexion;
   public CallbackServerImpl() throws RemoteException {
      super( );
     usuariosOnline = new HashMap<>();
     conexion = new ConexionBDD();
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

    public boolean inicioSesion(String usuario, String cont) throws RemoteException{

        try {
            // Consulta SQL
            String consultaSQL = "SELECT nome, contrasinal FROM usuarios";
            // Preparar la consulta
            PreparedStatement statement = conexion.conexion.prepareStatement(consultaSQL);
            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();
            // Procesar los resultados
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String contrasinal = resultSet.getString("contrasinal");
                if (nome.equals(usuario) && contrasinal.equals(cont)){
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean usuarioNoExistente(String usuario) throws RemoteException{

        try {
            // Consulta SQL
            String consultaSQL = "SELECT nome, contrasinal FROM usuarios";
            // Preparar la consulta
            PreparedStatement statement = conexion.conexion.prepareStatement(consultaSQL);
            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();
            // Procesar los resultados
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String contrasinal = resultSet.getString("contrasinal");
                if (nome.equals(usuario)){
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
    public boolean introducirUsuarios(String nome, String contrasinal) throws RemoteException, SQLException{
        if(usuarioNoExistente(nome) ){
            System.out.println("contraseña:'"+contrasinal+"'");
            // Consulta SQL para insertar un nuevo usuario
            String consultaSQL = "INSERT INTO usuarios (nome, contrasinal) VALUES (?, ?)";

            try (PreparedStatement statement = conexion.conexion.prepareStatement(consultaSQL)) {
                // Establecer los valores de los parámetros
                statement.setString(1, nome);
                statement.setString(2, contrasinal);

                // Ejecutar la consulta de inserción
                int filasAfectadas = statement.executeUpdate();

                /*// Comprobar si la inserción fue exitosa
                if (filasAfectadas > 0) {
                    System.out.println("Usuario agregado exitosamente.");
                } else {
                    System.out.println("Error al agregar el usuario.");
                }*/
            }
            return true;
        }
        return false;
    }

    ArrayList<String> consultarAmigos(String nome1) throws RemoteException, SQLException{
       ArrayList<String> amigos= new ArrayList<>();
        // Consulta SQL
        String consultaSQL = "SELECT nome1 , nome2 FROM amigos WHERE (nome1= ?) OR (nome2 = ?)";
        // Preparar la consulta
        PreparedStatement statement = conexion.conexion.prepareStatement(consultaSQL);
        // Ejecutar la consulta

        statement.setString(1, nome1);
        statement.setString(2, nome1);

        ResultSet resultSet = statement.executeQuery();
        // Procesar los resultados
        while (resultSet.next()) {
            String nomeAux = resultSet.getString("nome1");
            String nome2 = resultSet.getString("nome2");
            if(nomeAux.equals(nome1)){
                amigos.add(nome2);
            }
            else{
                amigos.add(nomeAux);
            }
        }
        return amigos;
    }

    void aceptarAmistad(String usuario, String solicitante) throws RemoteException, SQLException{
       String c1= null;
       String c2=null;
        // Consulta SQL
        String consultaSQL = "SELECT nome, contrasinal FROM usuarios";
        // Preparar la consulta
        PreparedStatement statement = conexion.conexion.prepareStatement(consultaSQL);
        // Ejecutar la consulta
        ResultSet resultSet = statement.executeQuery();
        // Procesar los resultados
        while (resultSet.next()) {
            String nome = resultSet.getString("nome");
            String contrasinal = resultSet.getString("contrasinal");
            if (usuario.equals(nome)){
                c1=contrasinal;
            }
            else if(solicitante.equals(nome)){
                c2=contrasinal;
            }

        }

        // Consulta SQL para insertar un nuevo usuario
        String consultaSQL2 = "INSERT INTO amigos (nome1,contrasinal1,nome2,contrasinal2) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement2 = conexion.conexion.prepareStatement(consultaSQL2)) {
            // Establecer los valores de los parámetros
            statement2.setString(1, usuario);
            statement2.setString(2, c1);
            statement2.setString(3, solicitante);
            statement2.setString(4, c2);

            // Ejecutar la consulta de inserción
            int filasAfectadas = statement2.executeUpdate();

            // Comprobar si la inserción fue exitosa
            if (filasAfectadas > 0) {
                System.out.println("Nuevos amigos conseguidos.");
            } else {
                System.out.println("Error al agregar hacer amigos.");
            }
        }
    }

}
