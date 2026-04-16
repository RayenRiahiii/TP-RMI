package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Shape extends Remote {
    double area() throws RemoteException;

    double perimeter() throws RemoteException;

    String describe() throws RemoteException;
}
