package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameServer extends Remote {
    String joinGame(String playerName, GameCallback cb) throws RemoteException;

    void sendAction(String playerId, String action) throws RemoteException;

    void leaveGame(String playerId) throws RemoteException;

    int getPlayerCount() throws RemoteException;
}
