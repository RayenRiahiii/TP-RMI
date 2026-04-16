package server;

import java.rmi.RemoteException;

public class PentagonImpl extends AbstractShape {
    private final double side;

    public PentagonImpl(double side) throws RemoteException {
        if (side <= 0) {
            throw new RemoteException("Pentagon requires a positive side.");
        }
        this.side = side;
    }

    @Override
    public double area() {
        return 0.25 * Math.sqrt(5 * (5 + 2 * Math.sqrt(5))) * side * side;
    }

    @Override
    public double perimeter() {
        return 5 * side;
    }

    @Override
    public String describe() {
        return "Pentagon(side=" + side + ")";
    }
}
