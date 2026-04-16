package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CounterService extends Remote {
    SharedCounter createCounter(String name) throws RemoteException;

    void atomicIncrement(SharedCounter c, int n) throws RemoteException;

    int sum(SharedCounter c1, SharedCounter c2) throws RemoteException;
}
