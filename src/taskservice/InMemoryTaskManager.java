package taskservice;

import java.util.HashMap;
import java.util.List;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {

    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    int id = 1;
    final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public int createTask(Task task) {
        if (task instanceof Epic epic) {
            if (!epicHashMap.containsValue(epic)) {
                Epic newEpic = new Epic(epic.getName(), epic.getDescription());
                newEpic.setSubTaskIds(epic.getSubTaskIds());
                newEpic.setSubTaskStatuses(epic.getSubTaskStatuses());
                newEpic.setId(id++);
                newEpic.updateStatus();
                epicHashMap.put(newEpic.getId(), newEpic);
                return newEpic.getId();
            }
        } else if (task instanceof SubTask subTask) {
            if (!subTaskHashMap.containsValue(subTask)) {
                SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                        epicHashMap.containsKey(subTask.getOwnerId()) ? subTask.getOwnerId() : 0);
                newSubTask.setId(id++);
                subTaskHashMap.put(newSubTask.getId(), newSubTask);
                if (newSubTask.getOwnerId() != 0) {
                    epicHashMap.get(newSubTask.getOwnerId()).setOneSubTask(newSubTask);
                }
                return newSubTask.getId();
            }
        } else {
            if (!taskHashMap.containsValue(task)) {
                Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
                newTask.setId(id++);
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
    public void printAllTasks(TaskTypes type) {
        int nameMaxLength = 0;
        int descriptionMaxLength = 0;
        for (Task task : taskHashMap.values()) {
            if (task.getName().length() > nameMaxLength) {
                nameMaxLength = task.getName().length();
            }
            if (task.getDescription().length() > descriptionMaxLength) {
                descriptionMaxLength = task.getDescription().length();
            }
        }
        for (Epic epic : epicHashMap.values()) {
            if (epic.getName().length() > nameMaxLength) {
                nameMaxLength = epic.getName().length();
            }
            if (epic.getDescription().length() > descriptionMaxLength) {
                descriptionMaxLength = epic.getDescription().length();
            }
        }
        for (SubTask subTask : subTaskHashMap.values()) {
            if (subTask.getName().length() > nameMaxLength) {
                nameMaxLength = subTask.getName().length();
            }
            if (subTask.getDescription().length() > descriptionMaxLength) {
                descriptionMaxLength = subTask.getDescription().length();
            }
        }
        String s = "%-9s id = %-4s name = '%" + (nameMaxLength * (-1) - 4) + "s description " +
                "= '%" + (descriptionMaxLength * (-1) - 4) + "s status = %-15s";
        switch (type) {
            case TASK:
                for (Task task : taskHashMap.values()) {
                    String sId = task.getId() + ",";
                    String name = task.getName() + "',";
                    String ds = task.getDescription() + "',";
                    String sType = TaskTypes.TASK + ":";
                    System.out.printf(s, sType, sId, name, ds, task.getStatus());
                    System.out.println();
                }
                break;
            case EPIC:
                for (Epic epic : epicHashMap.values()) {
                    String sId = epic.getId() + ",";
                    String name = epic.getName() + "',";
                    String ds = epic.getDescription() + "',";
                    String sType = TaskTypes.EPIC + ":";
                    String st = epic.getStatus() + ",";
                    System.out.printf(s, sType, sId, name, ds, st);
                    System.out.println("subTaskIds = " + epic.getSubTaskIds());
                }
                break;
            case SUBTASK:
                for (SubTask subTask : subTaskHashMap.values()) {
                    String sId = subTask.getId() + ",";
                    String name = subTask.getName() + "',";
                    String ds = subTask.getDescription() + "',";
                    String sType = TaskTypes.SUBTASK + ":";
                    String st = subTask.getStatus() + ",";
                    System.out.printf(s, sType, sId, name, ds, st);
                    System.out.println("owner id = " + subTask.getOwnerId());
                }
                break;
            default:
                System.out.println("Такой тип задач не предусмотрен");
        }
    }

    @Override
    public void removeAllTasks(TaskTypes type) {
        switch (type) {
            case TASK:
                taskHashMap.clear();
                break;
            case EPIC:
                epicHashMap.clear();
                break;
            case SUBTASK:
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
        return new Task("Default", "Default", Status.NEW);
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