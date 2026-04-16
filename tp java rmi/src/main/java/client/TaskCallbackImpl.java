package client;

import interfaces.TaskCallback;
import interfaces.TaskData;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TaskCallbackImpl extends UnicastRemoteObject implements TaskCallback {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String clientName;

    public TaskCallbackImpl(String clientName) throws RemoteException {
        super();
        this.clientName = clientName;
    }

    @Override
    public void onTaskCreated(TaskData data) {
        log("task created: " + data);
    }

    @Override
    public void onTaskCompleted(String taskId, String result) {
        log("task completed: " + taskId + " -> " + result);
    }

    private void log(String message) {
        String timestamp = LocalTime.now().format(TIME_FORMAT);
        System.out.println("[" + timestamp + "] TASK [" + clientName + "] : " + message);
    }
}
