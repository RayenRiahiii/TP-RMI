package server;

import java.rmi.RemoteException;

public class RectangleImpl extends AbstractShape {
    private final double width;
    private final double height;

    public RectangleImpl(double width, double height) throws RemoteException {
        if (width <= 0 || height <= 0) {
            throw new RemoteException("Rectangle requires positive width and height.");
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }

    @Override
    public String describe() {
        return "Rectangle(width=" + width + ", height=" + height + ")";
    }
}
