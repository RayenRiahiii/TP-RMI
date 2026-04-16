package client;

import interfaces.Vector2D;
import interfaces.VectorService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class VectorClientMain {
    private VectorClientMain() {
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        Registry registry = LocateRegistry.getRegistry(host, port);
        VectorService vectorService = (VectorService) registry.lookup("VectorService");

        Vector2D v = new Vector2D(3.0, 4.0);
        Vector2D w = new Vector2D(1.0, -2.0);

        System.out.println("Vecteur local initial v = " + v);
        System.out.println("Magnitude(v) = " + vectorService.magnitude(v));

        Vector2D normalized = vectorService.normalize(v);
        System.out.println("Vecteur retourne par normalize(v) = " + normalized);
        System.out.println("Vecteur local v apres l'appel = " + v);

        Vector2D sum = vectorService.add(v, w);
        System.out.println("add(v, w) = " + sum);

        v.x = 99.0;
        System.out.println("Vecteur local v modifie apres appel distant = " + v);
        System.out.println("Observation : le serveur a travaille sur une copie serialisee, pas sur l'objet local.");
    }
}
