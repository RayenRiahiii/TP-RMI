package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VectorService extends Remote {
    double magnitude(Vector2D v) throws RemoteException;

    Vector2D normalize(Vector2D v) throws RemoteException;

    Vector2D add(Vector2D a, Vector2D b) throws RemoteException;
}
