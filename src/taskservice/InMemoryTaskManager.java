package taskservice;

import java.util.HashMap;
import java.util.List;

import tasks.*;

public class InMemoryTaskManager implements TaskManager{

    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private int id = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int createTask(Task task) {
        if (task instanceof Epic epic) {
            if (!epicHashMap.containsValue(epic)) {
                Epic newEpic = new Epic(epic.getName(), epic.getDescription());
                newEpic.setSubTaskIds(epic.getSubTaskIds());
                newEpic.setSubTaskStatuses(epic.getSubTaskStatuses());
                newEpic.setId(++id);
                newEpic.updateStatus();
                epicHashMap.put(newEpic.getId(), newEpic);
                return newEpic.getId();
            }
        } else if (task instanceof SubTask subTask) {
            if (!subTaskHashMap.containsValue(subTask)) {
                SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                        epicHashMap.containsKey(subTask.getOwnerId()) ? subTask.getOwnerId() : 0);
                newSubTask.setId(++id);
                subTaskHashMap.put(newSubTask.getId(), newSubTask);
                if (newSubTask.getOwnerId() != 0) {
                    epicHashMap.get(newSubTask.getOwnerId()).setOneSubTask(newSubTask);
                }
                return newSubTask.getId();
            }
        } else {
            if (!taskHashMap.containsValue(task)) {
                Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
                newTask.setId(++id);
                taskHashMap.put(newTask.getId(), newTask);
                return newTask.getId();
            }
        }
        return -1;
    }

    @Override
    public void updateTask(Task task, int id) {
        if (task instanceof Epic epic) {
            Epic newEpic = new Epic(epic.getName(), epic.getDescription());
            newEpic.setSubTaskIds(epic.getSubTaskIds());
            newEpic.setSubTaskStatuses(epic.getSubTaskStatuses());
            newEpic.updateStatus();
            epicHashMap.put(id, newEpic);
        } else if (task instanceof SubTask subTask) {
            SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                    epicHashMap.containsKey(subTask.getOwnerId()) ? subTask.getOwnerId() : 0);
            subTaskHashMap.put(id, newSubTask);
            if (newSubTask.getOwnerId() != 0) {
                epicHashMap.get(newSubTask.getOwnerId()).setOneSubTask(newSubTask);
            }
        } else {
            Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
            taskHashMap.put(id, newTask);
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
            Task newTask = new Task(taskHashMap.get(id).getName(), taskHashMap.get(id).getDescription(),
                    taskHashMap.get(id).getStatus());
            newTask.setId(id);
            historyManager.add(newTask);
            return newTask;
        }
        if (epicHashMap.containsKey(id)) {
            Epic newEpic = new Epic(epicHashMap.get(id).getName(), epicHashMap.get(id).getDescription());
            newEpic.setSubTaskIds(epicHashMap.get(id).getSubTaskIds());
            newEpic.setSubTaskStatuses(epicHashMap.get(id).getSubTaskStatuses());
            newEpic.updateStatus();
            newEpic.setId(id);
            historyManager.add(newEpic);
            return newEpic;
        }
        if (subTaskHashMap.containsKey(id)) {
            SubTask newSubTask = new SubTask(subTaskHashMap.get(id).getName(), subTaskHashMap.get(id).getDescription(),
                    subTaskHashMap.get(id).getStatus(), subTaskHashMap.get(id).getOwnerId());
            newSubTask.setId(id);
            historyManager.add(newSubTask);
            return newSubTask;
        }
        return null;
    }

    @Override
    public void removeTaskById(int id) {
        if (subTaskHashMap.containsKey(id)) {
            if (subTaskHashMap.get(id).getOwnerId() != 0) {
                epicHashMap.get(subTaskHashMap.get(id).getOwnerId()).removeOneSubTask(subTaskHashMap.get(id));
            }
            subTaskHashMap.remove(id);
        }
        if (epicHashMap.containsKey(id)) {
            for (Integer subTaskId : epicHashMap.get(id).getSubTaskIds()) {
                subTaskHashMap.get(subTaskId).setOwnerId(0);
            }
            epicHashMap.remove(id);
        }
        taskHashMap.remove(id);
    }

    @Override
    public void printAllTasksByEpic(Epic epic) {
        for (Integer subTaskId : epicHashMap.get(id).getSubTaskIds()) {
            System.out.println(subTaskHashMap.get(subTaskId));
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, SubTask> getSubTaskHashMap() {
        return subTaskHashMap;
    }

}