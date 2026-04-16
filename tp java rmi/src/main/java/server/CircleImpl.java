package server;

import java.rmi.RemoteException;

public class CircleImpl extends AbstractShape {
    private final double radius;

    public CircleImpl(double radius) throws RemoteException {
        if (radius <= 0) {
            throw new RemoteException("Circle requires a positive radius.");
        }
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String describe() {
        return "Circle(radius=" + radius + ")";
    }
}
