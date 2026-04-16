package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameCallback extends Remote {
    void onPlayerJoined(String playerName) throws RemoteException;

    void onGameEvent(String eventMessage) throws RemoteException;

    void onGameOver(String winner) throws RemoteException;
}
