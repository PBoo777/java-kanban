package TaskService;

import java.util.HashMap;
import java.util.List;

import Tasks.*;

public class InMemoryTaskManager implements TaskManager{

    final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    final HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private int id = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int createTask(Task task) {
        if (task instanceof Epic epic) {
            if (!epicHashMap.containsValue(epic)) {
                epic.setId(++id);
                epicHashMap.put(epic.getId(), epic);
                epic.updateStatus();
            }
            return epic.getId();
        } else if (task instanceof SubTask subTask) {
            if (!taskHashMap.containsValue(subTask)) {
                subTask.setId(++id);
                subTaskHashMap.put(subTask.getId(), subTask);
                if (!epicHashMap.containsValue(subTask.getOwner())) {
                    subTask.getOwner().setId(++id);
                    epicHashMap.put(subTask.getOwner().getId(), subTask.getOwner());
                }
                subTask.getOwner().setOneSubTask(subTask);
            }
            return subTask.getId();
        } else {
            if (!taskHashMap.containsValue(task)) {
                task.setId(++id);
                taskHashMap.put(task.getId(), task);
            }
            return task.getId();
        }
    }

    @Override
    public void updateTask(Task task, int id) {
        if (task instanceof Epic epic) {
            epicHashMap.put(id, epic);
            epic.updateStatus();
        } else if (task instanceof SubTask subTask) {
            subTaskHashMap.put(id, subTask);
            subTask.getOwner().setOneSubTask(subTask);
            if (!epicHashMap.containsValue(subTask.getOwner())) {
                subTask.getOwner().setId(++id);
                epicHashMap.put(subTask.getOwner().getId(), subTask.getOwner());
            }
        } else {
            taskHashMap.put(id, task);
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}