package client;

import interfaces.GameServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GameSimulationMain {
    private static final String[] PLAYER_NAMES = {"Alice", "Bob", "Charlie"};
    private static final String[] ACTIONS = {"MOVE", "ATTACK", "JUMP", "DEFEND", "CAST", "HEAL"};

    private GameSimulationMain() {
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        Registry registry = LocateRegistry.getRegistry(host, port);
        GameServer gameServer = (GameServer) registry.lookup("GameServer");

        CountDownLatch startSignal = new CountDownLatch(1);
        Thread[] players = new Thread[PLAYER_NAMES.length];

        for (int index = 0; index < PLAYER_NAMES.length; index++) {
            String playerName = PLAYER_NAMES[index];
            players[index] = new Thread(() -> runPlayer(gameServer, playerName, startSignal), "Sim-" + playerName);
            players[index].start();
        }

        startSignal.countDown();

        for (Thread player : players) {
            player.join();
        }

        System.out.println("Simulation terminee.");
    }

    private static void runPlayer(GameServer gameServer, String playerName, CountDownLatch startSignal) {
        GameCallbackImpl callback = null;
        AtomicBoolean disconnected = new AtomicBoolean(false);
        String playerId = null;

        try {
            startSignal.await();
            callback = new GameCallbackImpl(playerName);
            playerId = gameServer.joinGame(playerName, callback);
            System.out.println(playerName + " connecte avec l'id " + playerId);

            sendRandomAction(gameServer, playerId);
            Thread.sleep(ThreadLocalRandom.current().nextLong(250L, 700L));
            sendRandomAction(gameServer, playerId);
            Thread.sleep(2000L);
        } catch (Exception exception) {
            System.err.println("Erreur pour " + playerName + " : " + exception.getMessage());
        } finally {
            disconnect(gameServer, callback, playerId, disconnected);
        }
    }

    private static void sendRandomAction(GameServer gameServer, String playerId) throws Exception {
        String action = ACTIONS[ThreadLocalRandom.current().nextInt(ACTIONS.length)];
        gameServer.sendAction(playerId, action);
    }

    private static void disconnect(
            GameServer gameServer,
            GameCallbackImpl callback,
            String playerId,
            AtomicBoolean disconnected) {
        if (callback == null || playerId == null || !disconnected.compareAndSet(false, true)) {
            return;
        }

        try {
            gameServer.leaveGame(playerId);
        } catch (Exception ignored) {
        }

        try {
            UnicastRemoteObject.unexportObject(callback, true);
        } catch (Exception ignored) {
        }
    }
}
