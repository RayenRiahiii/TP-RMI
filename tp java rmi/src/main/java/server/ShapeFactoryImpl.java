package server;

import interfaces.Shape;
import interfaces.ShapeFactory;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;

public class ShapeFactoryImpl extends UnicastRemoteObject implements ShapeFactory {
    public ShapeFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public Shape createShape(String type, double... params) throws RemoteException {
        if (type == null || type.isBlank()) {
            throw new RemoteException("Shape type must not be null or blank.");
        }

        String normalizedType = type.toLowerCase(Locale.ROOT);
        return switch (normalizedType) {
            case "circle" -> {
                requireParamCount(normalizedType, params, 1);
                yield new CircleImpl(params[0]);
            }
            case "rectangle" -> {
                requireParamCount(normalizedType, params, 2);
                yield new RectangleImpl(params[0], params[1]);
            }
            case "triangle" -> {
                requireParamCount(normalizedType, params, 3);
                yield new TriangleImpl(params[0], params[1], params[2]);
            }
            case "pentagon" -> {
                requireParamCount(normalizedType, params, 1);
                yield new PentagonImpl(params[0]);
            }
            default -> throw new RemoteException(
                    "Unknown shape type: " + type + ". Available types: " + String.join(", ", availableTypes()));
        };
    }

    @Override
    public String[] availableTypes() {
        return new String[]{"circle", "rectangle", "triangle", "pentagon"};
    }

    private void requireParamCount(String type, double[] params, int expectedCount) throws RemoteException {
        int actualCount = params == null ? 0 : params.length;
        if (actualCount != expectedCount) {
            throw new RemoteException(
                    "Shape type '" + type + "' expects " + expectedCount + " parameter(s), but received " + actualCount + ".");
        }
    }
}
