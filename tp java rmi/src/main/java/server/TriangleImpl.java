package server;

import java.rmi.RemoteException;

public class TriangleImpl extends AbstractShape {
    private final double a;
    private final double b;
    private final double c;

    public TriangleImpl(double a, double b, double c) throws RemoteException {
        if (a <= 0 || b <= 0 || c <= 0) {
            throw new RemoteException("Triangle requires three positive sides.");
        }
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new RemoteException("Triangle sides do not satisfy the triangle inequality.");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double area() {
        double semiPerimeter = perimeter() / 2;
        return Math.sqrt(semiPerimeter * (semiPerimeter - a) * (semiPerimeter - b) * (semiPerimeter - c));
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }

    @Override
    public String describe() {
        return "Triangle(a=" + a + ", b=" + b + ", c=" + c + ")";
    }
}
