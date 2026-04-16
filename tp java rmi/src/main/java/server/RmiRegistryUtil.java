package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class RmiRegistryUtil {
    private RmiRegistryUtil() {
    }

    public static Registry ensureRegistry(int port) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(port);
        try {
            registry.list();
            return registry;
        } catch (RemoteException ignored) {
            return LocateRegistry.createRegistry(port);
        }
    }
}
