package server;

import interfaces.TaskData;
import interfaces.TaskHandle;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TaskHandleImpl extends UnicastRemoteObject implements TaskHandle {
    private final String taskId;
    private final TaskData data;
    private final TaskManagerImpl manager;

    private TaskState state = TaskState.PENDING;
    private int progress;
    private String result = "";
    private String assignee = "unassigned";

    public TaskHandleImpl(String taskId, TaskData data, TaskManagerImpl manager) throws RemoteException {
        super();
        this.taskId = taskId;
        this.data = data;
        this.manager = manager;
    }

    @Override
    public synchronized void assignTo(String assignee) throws RemoteException {
        if (assignee == null || assignee.isBlank()) {
            throw new RemoteException("Assignee must not be blank.");
        }
        if (state == TaskState.DONE) {
            throw new RemoteException("Task " + taskId + " is already DONE.");
        }
        this.assignee = assignee.trim();
    }

    @Override
    public synchronized void updateProgress(int percent) throws RemoteException {
        if (percent < 0 || percent > 100) {
            throw new RemoteException("Progress must be between 0 and 100.");
        }
        if (state == TaskState.DONE) {
            throw new RemoteException("Task " + taskId + " is already DONE.");
        }

        progress = percent;
        if (percent > 0) {
            state = TaskState.IN_PROGRESS;
        }
    }

    @Override
    public synchronized void complete(String result) throws RemoteException {
        if (state == TaskState.DONE) {
            throw new RemoteException("Task " + taskId + " is already DONE.");
        }
        if (result == null || result.isBlank()) {
            throw new RemoteException("Completion result must not be blank.");
        }

        progress = 100;
        state = TaskState.DONE;
        this.result = result.trim();
        manager.onTaskCompleted(taskId, this.result);
    }

    @Override
    public synchronized String getStatus() {
        StringBuilder builder = new StringBuilder()
                .append(taskId)
                .append(" | ")
                .append(data.getTitle())
                .append(" | assignee=")
                .append(assignee)
                .append(" | state=")
                .append(state)
                .append(" | progress=")
                .append(progress)
                .append("%");

        if (state == TaskState.DONE) {
            builder.append(" | result=").append(result);
        }

        return builder.toString();
    }

    public synchronized TaskState getStateInternal() {
        return state;
    }

    public synchronized String toPendingDescription() {
        return taskId
                + " | "
                + data.getTitle()
                + " | priority="
                + data.getPriority()
                + " | assignee="
                + assignee
                + " | state="
                + state;
    }
}
