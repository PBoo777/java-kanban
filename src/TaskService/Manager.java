package TaskService;

import java.util.HashMap;
import Tasks.*;

public class Manager {

    static final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    static final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    static final HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private static int id = 0;

    public static void createTask(Task task) {
        switch (task.getClass().getName()) {
            case "Tasks.Task":
                if (taskHashMap.containsValue(task)) return;
                task.setId(++id);
                taskHashMap.put(task.getId(), task);
                return;
            case "Tasks.Epic":
                Epic epic = (Epic) task;
                if (epicHashMap.containsValue(epic)) return;
                epic.setId(++id);
                epicHashMap.put(epic.getId(), epic);
                return;
            case "Tasks.SubTask":
                SubTask subTask = (SubTask) task;
                if (taskHashMap.containsValue(subTask)) return;
                subTask.setId(++id);
                subTaskHashMap.put(subTask.getId(), subTask);
                if (epicHashMap.containsValue(subTask.getOwner())) {
                    subTask.getOwner().setOneSubTask(subTask.getId());
                } else {
                    Epic newE = new Epic(subTask.getName(), subTask.getDescription());
                    newE.setId(++id);
                    newE.setOneSubTask(subTask.getId());
                    epicHashMap.put(newE.getId(), newE);
                }
                return;
            default:
                System.out.println("Такой тип задач не предусмотрен");
        }
    }

    public static void updateTask(Task task) {
        switch (task.getClass().getName()) {
            case "Tasks.Task":
                taskHashMap.put(task.getId(), task);
                return;
            case "Tasks.Epic":
                Epic epic = (Epic) task;
                epicHashMap.put(epic.getId(), epic);
                epic.updateStatus();
                return;
            case "Tasks.SubTask":
                SubTask subTask = (SubTask) task;
                subTaskHashMap.put(subTask.getId(), subTask);
                subTask.getOwner().setOneSubTask(subTask.getId());
                return;
            default:
                System.out.println("Такой тип задач не предусмотрен");
        }
    }

    public static void printAllTasks(String className) {
        switch (className) {
            case "Tasks.Task":
                for (Task task : taskHashMap.values()) {
                    System.out.println(task);
                }
                break;
            case "Tasks.Epic":
                for (Epic epic : epicHashMap.values()) {
                    System.out.println(epic);
                }
                break;
            case "Tasks.SubTask":
                for (SubTask subTask : subTaskHashMap.values()) {
                    System.out.println(subTask);
                }
                break;
            default:
                System.out.println("Такой тип задач не предусмотрен");
        }
    }

    public static void removeAllTasks(String className) {
        switch (className) {
            case "Tasks.Task":
                taskHashMap.clear();
                break;
            case "Tasks.Epic":
                epicHashMap.clear();
                break;
            case "Tasks.SubTask":
                subTaskHashMap.clear();
                break;
            default:
                System.out.println("Такой тип задач не предусмотрен");
        }
    }

    public static Task getTaskById(int id) {
        if (taskHashMap.containsKey(id)) return taskHashMap.get(id);
        if (epicHashMap.containsKey(id)) return epicHashMap.get(id);
        if (subTaskHashMap.containsKey(id)) return subTaskHashMap.get(id);
        return null;
    }

    public static void removeTaskById(int id) {
        taskHashMap.remove(id);
        epicHashMap.remove(id);
        subTaskHashMap.remove(id);
    }

    public static void printAllTasksByEpic(Epic epic) {
        SubTask subTask;
        for (Integer subTaskId : epic.getSubTaskIds()) {
            subTask = (SubTask) getTaskById(subTaskId);
            System.out.println(subTask);
        }
    }
}
