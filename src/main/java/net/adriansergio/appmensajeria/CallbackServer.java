package net.adriansergio.appmensajeria;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CallbackServer  {
  public static void main(String args[]) {
    InputStreamReader is = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(is);
    String hostName, portNum, registryURL;
    try{
      System.out.println("Introduzca el nombre del host:");
      hostName = br.readLine();
      System.out.println("Introduzca el puerto:");
      portNum = (br.readLine()).trim();
      int RMIPortNum = Integer.parseInt(portNum);
      startRegistry(RMIPortNum);
      //Creamos objeto servidor que publicamos en el registro
      CallbackServerImpl exportedObj = new CallbackServerImpl();
      registryURL = "rmi://"+ hostName + ":" + portNum + "/callback";
      Naming.rebind(registryURL, exportedObj);
      System.out.println("Servidor preparado.");
    }
    catch (Exception re) {
      System.out.println("Exception in HelloServer.main: " + re);
    }
  }

  //This method starts a RMI registry on the local host, if
  //it does not already exists at the specified port number.
  private static void startRegistry(int RMIPortNum)
    throws RemoteException{
    try {
      Registry registry = LocateRegistry.getRegistry(RMIPortNum);
      registry.list();
    }
    catch (RemoteException e) {
      Registry registry = LocateRegistry.createRegistry(RMIPortNum);
    }
  }
}
