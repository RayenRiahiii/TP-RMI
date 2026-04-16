package server;

import interfaces.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class AbstractShape extends UnicastRemoteObject implements Shape {
    protected AbstractShape() throws RemoteException {
        super();
    }
}
