package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TaskManager extends Remote {
    TaskHandle createTask(TaskData data) throws RemoteException;

    void subscribe(TaskCallback cb) throws RemoteException;

    List<String> listPendingTasks() throws RemoteException;
}
