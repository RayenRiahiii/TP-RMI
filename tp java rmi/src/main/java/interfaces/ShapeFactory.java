package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ShapeFactory extends Remote {
    Shape createShape(String type, double... params) throws RemoteException;

    String[] availableTypes() throws RemoteException;
}
