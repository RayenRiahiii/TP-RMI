package server;

import interfaces.GameCallback;
import interfaces.GameServer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameServerImpl extends UnicastRemoteObject implements GameServer {
    private final Map<String, GameCallback> players = new ConcurrentHashMap<>();
    private final Map<String, String> playerNames = new ConcurrentHashMap<>();
    private final AtomicInteger nextPlayerId = new AtomicInteger(1);

    public GameServerImpl() throws RemoteException {
        super();
    }

    @Override
    public String joinGame(String playerName, GameCallback cb) throws RemoteException {
        if (playerName == null || playerName.isBlank()) {
            throw new RemoteException("Player name must not be null or blank.");
        }
        if (cb == null) {
            throw new RemoteException("Game callback must not be null.");
        }

        String playerId = "P" + nextPlayerId.getAndIncrement();
        String normalizedPlayerName = playerName.trim();
        players.put(playerId, cb);
        playerNames.put(playerId, normalizedPlayerName);

        notifyOthers(playerId, callback -> callback.onPlayerJoined(normalizedPlayerName));
        return playerId;
    }

    @Override
    public void sendAction(String playerId, String action) throws RemoteException {
        String playerName = playerNames.get(playerId);
        if (playerName == null) {
            throw new RemoteException("Unknown player id: " + playerId);
        }
        if (action == null || action.isBlank()) {
            throw new RemoteException("Action must not be null or blank.");
        }

        String normalizedAction = action.trim();
        notifyAllPlayers(callback -> callback.onGameEvent(playerName + " (" + playerId + ") : " + normalizedAction));
        if (isWinningAction(normalizedAction)) {
            notifyAllPlayers(callback -> callback.onGameOver(playerName));
        }
    }

    @Override
    public void leaveGame(String playerId) throws RemoteException {
        GameCallback removed = players.remove(playerId);
        String removedName = playerNames.remove(playerId);
        if (removed == null || removedName == null) {
            throw new RemoteException("Unknown player id: " + playerId);
        }
        notifyAllPlayers(callback -> callback.onGameEvent(removedName + " (" + playerId + ") a quitte la partie."));
    }

    @Override
    public int getPlayerCount() {
        return players.size();
    }

    private boolean isWinningAction(String action) {
        return "win".equalsIgnoreCase(action)
                || "victory".equalsIgnoreCase(action)
                || "gagne".equalsIgnoreCase(action);
    }

    private void notifyOthers(String excludedPlayerId, CallbackAction action) {
        notifyCallbacks(excludedPlayerId, action);
    }

    private void notifyAllPlayers(CallbackAction action) {
        notifyCallbacks(null, action);
    }

    private void notifyCallbacks(String excludedPlayerId, CallbackAction action) {
        List<String> unreachablePlayers = new ArrayList<>();
        for (Entry<String, GameCallback> entry : players.entrySet()) {
            String playerId = entry.getKey();
            if (playerId.equals(excludedPlayerId)) {
                continue;
            }
            try {
                action.call(entry.getValue());
            } catch (RemoteException exception) {
                unreachablePlayers.add(playerId);
            }
        }

        for (String playerId : unreachablePlayers) {
            players.remove(playerId);
            playerNames.remove(playerId);
        }
    }

    @FunctionalInterface
    private interface CallbackAction {
        void call(GameCallback callback) throws RemoteException;
    }
}
