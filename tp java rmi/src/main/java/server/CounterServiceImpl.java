package server;

import interfaces.CounterService;
import interfaces.SharedCounter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CounterServiceImpl extends UnicastRemoteObject implements CounterService {
    private final Map<String, SharedCounter> counters = new ConcurrentHashMap<>();

    public CounterServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized SharedCounter createCounter(String name) throws RemoteException {
        if (name == null || name.isBlank()) {
            throw new RemoteException("Counter name must not be null or blank.");
        }

        String normalizedName = name.trim();
        SharedCounter existing = counters.get(normalizedName);
        if (existing != null) {
            return existing;
        }

        SharedCounter counter = new SharedCounterImpl(normalizedName);
        counters.put(normalizedName, counter);
        return counter;
    }

    @Override
    public void atomicIncrement(SharedCounter c, int n) throws RemoteException {
        if (c == null) {
            throw new RemoteException("SharedCounter must not be null.");
        }
        if (n < 0) {
            throw new RemoteException("Increment count must be non-negative.");
        }

        for (int index = 0; index < n; index++) {
            c.increment();
        }
    }

    @Override
    public int sum(SharedCounter c1, SharedCounter c2) throws RemoteException {
        if (c1 == null || c2 == null) {
            throw new RemoteException("SharedCounter arguments must not be null.");
        }
        return c1.getValue() + c2.getValue();
    }
}
