package taskservice;

import tasks.Epic;
import tasks.Task;
import java.util.List;

public interface TaskManager {

    int createTask(Task task);

    void updateTask(Task task, int id);

    void printAllTasks(TaskTypes type);

    void removeAllTasks(TaskTypes type);

    Task getTaskById(int id);

    void removeTaskById(int id);

    void printAllTasksByEpic(Epic epic);

    List<Task> getHistory();

}
