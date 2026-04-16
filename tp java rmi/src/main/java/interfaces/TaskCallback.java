package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TaskCallback extends Remote {
    void onTaskCreated(TaskData data) throws RemoteException;

    void onTaskCompleted(String taskId, String result) throws RemoteException;
}
