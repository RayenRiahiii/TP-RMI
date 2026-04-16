package client;

import interfaces.GameServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GameClientMain {
    private GameClientMain() {
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;
        String playerName = args.length > 2 ? args[2] : "Player" + System.currentTimeMillis() % 1000;

        Registry registry = LocateRegistry.getRegistry(host, port);
        GameServer gameServer = (GameServer) registry.lookup("GameServer");

        GameCallbackImpl callback = new GameCallbackImpl(playerName);
        AtomicBoolean disconnected = new AtomicBoolean(false);
        String playerId = gameServer.joinGame(playerName, callback);
        System.out.println("Connected as " + playerName + " with id " + playerId);
        System.out.println("Players connected: " + gameServer.getPlayerCount());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (disconnected.compareAndSet(false, true)) {
                try {
                    gameServer.leaveGame(playerId);
                } catch (Exception ignored) {
                }
                try {
                    UnicastRemoteObject.unexportObject(callback, true);
                } catch (Exception ignored) {
                }
            }
        }));

        if (args.length > 3) {
            for (int index = 3; index < args.length; index++) {
                sendAction(gameServer, playerId, args[index]);
            }
        } else {
            sendAction(gameServer, playerId, "MOVE");
            sendAction(gameServer, playerId, "ATTACK");
        }

        Thread.sleep(2000L);
        if (disconnected.compareAndSet(false, true)) {
            gameServer.leaveGame(playerId);
            UnicastRemoteObject.unexportObject(callback, true);
        }
        Thread.sleep(200L);
    }

    private static void sendAction(GameServer gameServer, String playerId, String action) throws Exception {
        gameServer.sendAction(playerId, action);
        Thread.sleep(300L);
    }
}
