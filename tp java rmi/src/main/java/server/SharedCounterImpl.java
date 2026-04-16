package server;

import interfaces.SharedCounter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicInteger;

public class SharedCounterImpl extends UnicastRemoteObject implements SharedCounter {
    private final String name;
    private final AtomicInteger value = new AtomicInteger();

    public SharedCounterImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public void increment() {
        value.incrementAndGet();
    }

    @Override
    public void decrement() {
        value.decrementAndGet();
    }

    @Override
    public int getValue() {
        return value.get();
    }

    @Override
    public void reset() {
        value.set(0);
    }

    @Override
    public String toString() {
        return "SharedCounterImpl{name='" + name + "', value=" + value.get() + "}";
    }
}
