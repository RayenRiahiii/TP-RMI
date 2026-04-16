package server;

import interfaces.Vector2D;
import interfaces.VectorService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class VectorServiceImpl extends UnicastRemoteObject implements VectorService {
    public VectorServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public double magnitude(Vector2D v) throws RemoteException {
        Vector2D vector = requireVector(v, "v");
        log("magnitude", vector);
        return Math.hypot(vector.x, vector.y);
    }

    @Override
    public Vector2D normalize(Vector2D v) throws RemoteException {
        Vector2D vector = requireVector(v, "v");
        log("normalize", vector);
        double magnitude = Math.hypot(vector.x, vector.y);
        if (magnitude == 0.0) {
            throw new RemoteException("Cannot normalize a zero vector.");
        }

        // This mutates only the deserialized server-side copy.
        vector.x /= magnitude;
        vector.y /= magnitude;
        return vector;
    }

    @Override
    public Vector2D add(Vector2D a, Vector2D b) throws RemoteException {
        Vector2D left = requireVector(a, "a");
        Vector2D right = requireVector(b, "b");
        log("add", left, right);
        return new Vector2D(left.x + right.x, left.y + right.y);
    }

    private Vector2D requireVector(Vector2D vector, String name) throws RemoteException {
        if (vector == null) {
            throw new RemoteException("Vector '" + name + "' must not be null.");
        }
        return vector;
    }

    private void log(String operation, Vector2D... vectors) {
        StringBuilder builder = new StringBuilder("[VectorService] ").append(operation).append(" ");
        for (int index = 0; index < vectors.length; index++) {
            if (index > 0) {
                builder.append(" | ");
            }
            builder.append(vectors[index]);
        }
        System.out.println(builder);
    }
}
