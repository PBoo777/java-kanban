package TaskService;

import Tasks.Epic;
import Tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    int createTask(Task task);

    void updateTask(Task task, int id);

    void printAllTasks(String className);

    void removeAllTasks(String className);

    Task getTaskById(int id);

    void removeTaskById(int id);

    void printAllTasksByEpic(Epic epic);

    ArrayList<Task> getHistory();

}
