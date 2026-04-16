package client;

import interfaces.Shape;
import interfaces.ShapeFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public final class ShapeClientMain {
    private ShapeClientMain() {
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        Registry registry = LocateRegistry.getRegistry(host, port);
        ShapeFactory factory = (ShapeFactory) registry.lookup("ShapeFactory");

        System.out.println("Available shapes: " + Arrays.toString(factory.availableTypes()));
        printShape(factory.createShape("circle", 5.0));
        printShape(factory.createShape("rectangle", 4.0, 6.0));
        printShape(factory.createShape("triangle", 3.0, 4.0, 5.0));
        printShape(factory.createShape("pentagon", 2.0));
    }

    private static void printShape(Shape shape) throws Exception {
        System.out.printf(
                "%s | area=%.2f | perimeter=%.2f%n",
                shape.describe(),
                shape.area(),
                shape.perimeter());
    }
}
