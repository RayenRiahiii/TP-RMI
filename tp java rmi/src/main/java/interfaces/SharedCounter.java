package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SharedCounter extends Remote {
    void increment() throws RemoteException;

    void decrement() throws RemoteException;

    int getValue() throws RemoteException;

    void reset() throws RemoteException;
}
