package TaskService;

import java.util.ArrayList;
import java.util.HashMap;

import Tasks.*;

public class InMemoryTaskManager implements TaskManager{

    final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    final HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private int id = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int createTask(Task task) {
        switch (task.getClass().getName()) {
            case "Tasks.Task":
                if (taskHashMap.containsValue(task)) return task.getId();
                task.setId(++id);
                taskHashMap.put(task.getId(), task);
                return task.getId();
            case "Tasks.Epic":
                Epic epic = (Epic) task;
                if (epicHashMap.containsValue(epic)) return epic.getId();
                epic.setId(++id);
                epicHashMap.put(epic.getId(), epic);
                epic.updateStatus();
                return epic.getId();
            case "Tasks.SubTask":
                SubTask subTask = (SubTask) task;
                if (taskHashMap.containsValue(subTask)) return subTask.getId();
                subTask.setId(++id);
                subTaskHashMap.put(subTask.getId(), subTask);
                if (epicHashMap.containsValue(subTask.getOwner())) {
                    subTask.getOwner().setOneSubTask(subTask);
                } else {
                    Epic newE = subTask.getOwner();
                    newE.setId(++id);
                    newE.setOneSubTask(subTask);
                    epicHashMap.put(newE.getId(), newE);
                }
                return subTask.getId();
            default:
                System.out.println("Такой тип задач не предусмотрен");
        }
        return -1;
    }

    @Override
    public void updateTask(Task task, int Id) {
        switch (task.getClass().getName()) {
            case "Tasks.Task":
                taskHashMap.put(Id, task);
                return;
            case "Tasks.Epic":
                Epic epic = (Epic) task;
                epicHashMap.put(Id, epic);
                epic.updateStatus();
                return;
            case "Tasks.SubTask":
                SubTask subTask = (SubTask) task;
                subTaskHashMap.put(Id, subTask);
                subTask.getOwner().setOneSubTask(subTask);
                return;
            default:
                System.out.println("Такой тип задач не предусмотрен");
        }
    }

    @Override
    public void printAllTasks(String className) {
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

    @Override
    public void removeAllTasks(String className) {
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

    @Override
    public Task getTaskById(int id) {
        if (taskHashMap.containsKey(id)) {
            historyManager.add(taskHashMap.get(id));
            return taskHashMap.get(id);
        }
        if (epicHashMap.containsKey(id)) {
            historyManager.add(epicHashMap.get(id));
            return epicHashMap.get(id);
        }
        if (subTaskHashMap.containsKey(id)) {
            historyManager.add(subTaskHashMap.get(id));
            return subTaskHashMap.get(id);
        }
        return null;
    }

    @Override
    public void removeTaskById(int id) {
        taskHashMap.remove(id);
        epicHashMap.remove(id);
        subTaskHashMap.remove(id);
    }

    @Override
    public void printAllTasksByEpic(Epic epic) {
        for (SubTask subTask : epic.getSubTasks()) {
            System.out.println(subTask);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

}