package client;

import interfaces.GameCallback;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GameCallbackImpl extends UnicastRemoteObject implements GameCallback {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final String playerName;

    public GameCallbackImpl(String playerName) throws RemoteException {
        super();
        this.playerName = playerName;
    }

    @Override
    public void onPlayerJoined(String playerName) {
        log("joueur connecte : " + playerName);
    }

    @Override
    public void onGameEvent(String eventMessage) {
        log(eventMessage);
    }

    @Override
    public void onGameOver(String winner) {
        log("partie terminee, gagnant : " + winner);
    }

    private void log(String message) {
        String timestamp = LocalTime.now().format(TIME_FORMAT);
        System.out.println("[" + timestamp + "] NOTIF [" + playerName + "] : " + message);
    }
}
