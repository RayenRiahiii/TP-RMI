package server;

import java.rmi.registry.Registry;
import java.util.concurrent.CountDownLatch;

public final class ServerMain {
    public static final int DEFAULT_PORT = 1099;

    private ServerMain() {
    }

    public static void main(String[] args) throws Exception {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        Registry registry = RmiRegistryUtil.ensureRegistry(port);
        registry.rebind("ShapeFactory", new ShapeFactoryImpl());
        registry.rebind("GameServer", new GameServerImpl());
        registry.rebind("VectorService", new VectorServiceImpl());
        registry.rebind("CounterService", new CounterServiceImpl());
        registry.rebind("TaskManager", new TaskManagerImpl());

        System.out.println("RMI registry ready on port " + port);
        new CountDownLatch(1).await();
    }
}
