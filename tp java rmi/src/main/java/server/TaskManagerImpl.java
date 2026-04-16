package server;

import interfaces.TaskCallback;
import interfaces.TaskData;
import interfaces.TaskHandle;
import interfaces.TaskManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManagerImpl extends UnicastRemoteObject implements TaskManager {
    private final AtomicInteger nextTaskId = new AtomicInteger(1);
    private final Map<String, TaskHandleImpl> tasks = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<TaskCallback> subscribers = new CopyOnWriteArrayList<>();

    public TaskManagerImpl() throws RemoteException {
        super();
    }

    @Override
    public TaskHandle createTask(TaskData data) throws RemoteException {
        TaskData normalizedData = normalizeTaskData(data);
        String taskId = "T" + nextTaskId.getAndIncrement();
        TaskHandleImpl handle = new TaskHandleImpl(taskId, normalizedData, this);
        tasks.put(taskId, handle);
        notifyTaskCreated(normalizedData);
        return handle;
    }

    @Override
    public void subscribe(TaskCallback cb) throws RemoteException {
        if (cb == null) {
            throw new RemoteException("Task callback must not be null.");
        }
        subscribers.addIfAbsent(cb);
    }

    @Override
    public List<String> listPendingTasks() {
        List<String> pendingTasks = new ArrayList<>();
        for (TaskHandleImpl task : tasks.values()) {
            if (task.getStateInternal() != TaskState.DONE) {
                pendingTasks.add(task.toPendingDescription());
            }
        }
        return pendingTasks;
    }

    public void onTaskCompleted(String taskId, String result) {
        List<TaskCallback> unreachableCallbacks = new ArrayList<>();
        for (TaskCallback callback : subscribers) {
            try {
                callback.onTaskCompleted(taskId, result);
            } catch (RemoteException exception) {
                unreachableCallbacks.add(callback);
            }
        }
        subscribers.removeAll(unreachableCallbacks);
    }

    private TaskData normalizeTaskData(TaskData data) throws RemoteException {
        if (data == null) {
            throw new RemoteException("TaskData must not be null.");
        }

        String title = data.getTitle() == null ? "" : data.getTitle().trim();
        String description = data.getDescription() == null ? "" : data.getDescription().trim();
        int priority = data.getPriority();

        if (title.isEmpty()) {
            throw new RemoteException("Task title must not be blank.");
        }
        if (description.isEmpty()) {
            throw new RemoteException("Task description must not be blank.");
        }
        if (priority < 1 || priority > 3) {
            throw new RemoteException("Task priority must be 1, 2 or 3.");
        }

        return new TaskData(title, description, priority);
    }

    private void notifyTaskCreated(TaskData data) {
        List<TaskCallback> unreachableCallbacks = new ArrayList<>();
        for (TaskCallback callback : subscribers) {
            try {
                callback.onTaskCreated(data);
            } catch (RemoteException exception) {
                unreachableCallbacks.add(callback);
            }
        }
        subscribers.removeAll(unreachableCallbacks);
    }
}
