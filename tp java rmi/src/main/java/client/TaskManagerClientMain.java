package client;

import interfaces.TaskData;
import interfaces.TaskHandle;
import interfaces.TaskManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public final class TaskManagerClientMain {
    private TaskManagerClientMain() {
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        Registry registry = LocateRegistry.getRegistry(host, port);
        TaskManager taskManager = (TaskManager) registry.lookup("TaskManager");

        TaskCallbackImpl callback = new TaskCallbackImpl("Observer-1");
        TaskCallbackImpl secondCallback = new TaskCallbackImpl("Observer-2");

        try {
            taskManager.subscribe(callback);
            taskManager.subscribe(secondCallback);

            TaskHandle task1 = taskManager.createTask(new TaskData("Compiler le projet", "Construire la version Java RMI", 3));
            TaskHandle task2 = taskManager.createTask(new TaskData("Ecrire le rapport", "Resumer les observations RMI", 2));

            task1.assignTo("Rayen");
            task2.assignTo("Equipe QA");

            printPendingTasks(taskManager.listPendingTasks());

            task1.updateProgress(25);
            System.out.println(task1.getStatus());
            task1.updateProgress(75);
            System.out.println(task1.getStatus());

            task2.updateProgress(40);
            System.out.println(task2.getStatus());

            task1.complete("Build et verification termines");
            System.out.println(task1.getStatus());

            task2.complete("Rapport redige");
            System.out.println(task2.getStatus());

            printPendingTasks(taskManager.listPendingTasks());
            Thread.sleep(400L);
        } finally {
            UnicastRemoteObject.unexportObject(callback, true);
            UnicastRemoteObject.unexportObject(secondCallback, true);
        }
    }

    private static void printPendingTasks(List<String> pendingTasks) {
        System.out.println("Tasks pending/in progress:");
        if (pendingTasks.isEmpty()) {
            System.out.println("- aucune");
            return;
        }

        for (String task : pendingTasks) {
            System.out.println("- " + task);
        }
    }
}
