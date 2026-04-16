package client;

import interfaces.CounterService;
import interfaces.SharedCounter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class CounterClientMain {
    private CounterClientMain() {
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        Registry registry = LocateRegistry.getRegistry(host, port);
        CounterService counterService = (CounterService) registry.lookup("CounterService");

        SharedCounter c1 = counterService.createCounter("counterA");
        SharedCounter c2 = counterService.createCounter("counterB");

        c1.reset();
        c2.reset();

        counterService.atomicIncrement(c1, 5);
        counterService.atomicIncrement(c2, 3);

        System.out.println("Valeur de counterA apres atomicIncrement = " + c1.getValue());
        System.out.println("Valeur de counterB apres atomicIncrement = " + c2.getValue());

        c1.increment();
        c2.decrement();

        System.out.println("Valeur de counterA apres increment via stub = " + c1.getValue());
        System.out.println("Valeur de counterB apres decrement via stub = " + c2.getValue());
        System.out.println("Somme retournee par CounterService.sum = " + counterService.sum(c1, c2));
        System.out.println("Observation : les modifications via le stub sont visibles en temps reel.");
    }
}
