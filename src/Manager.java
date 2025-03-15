import java.util.HashMap;

public class Manager {

    static final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    static final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    static final HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    private static int id = 0;

    static void createSomeTask(Task task) {
        switch (task.getClass().getName()) {
            case "Task":
                if (taskHashMap.containsValue(task)) return;
                task.id = ++id;
                taskHashMap.put(task.id, task);
                return;
            case "Epic":
                Epic epic = (Epic) task;
                if (epicHashMap.containsValue(epic)) return;
                epic.id = ++id;
                epicHashMap.put(epic.id, epic);
                return;
            case "SubTask":
                SubTask subTask = (SubTask) task;
                if (taskHashMap.containsValue(subTask)) return;
                subTask.id = ++id;
                subTaskHashMap.put(subTask.id, subTask);
                if (epicHashMap.containsValue(subTask.getOwner())) {
                    subTask.getOwner().setOneSubTask(subTask.id);
                } else {
                    Epic newE = new Epic(subTask.name, subTask.description);
                    newE.id = ++id;
                    newE.setOneSubTask(subTask.id);
                    epicHashMap.put(newE.id, newE);
                }
        }
    }

    static void updateSomeTask(Task task) {
        switch (task.getClass().getName()) {
            case "Task":
                taskHashMap.put(task.id, task);
                return;
            case "Epic":
                Epic epic = (Epic) task;
                epicHashMap.put(epic.id, epic);
                epic.updateStatus();
                return;
            case "SubTask":
                SubTask subTask = (SubTask) task;
                subTaskHashMap.put(subTask.id, subTask);
                subTask.getOwner().setOneSubTask(subTask.id);
        }
    }

    static void printAllTasks(String className) {
        switch (className) {
            case "Task":
                for (Task task : taskHashMap.values()) {
                    System.out.println(task);
                }
                break;
            case "Epic":
                for (Epic epic : epicHashMap.values()) {
                    System.out.println(epic);
                }
                break;
            case "SubTask":
                for (SubTask subTask : subTaskHashMap.values()) {
                    System.out.println(subTask);
                }
                break;
        }
    }

    static void removeAllTasks(String className) {
        switch (className) {
            case "Task":
                taskHashMap.clear();
                break;
            case "Epic":
                epicHashMap.clear();
                break;
            case "SubTask":
                subTaskHashMap.clear();
                break;
        }
    }

    static Task getInstance(int id) {
        if (taskHashMap.containsKey(id)) return taskHashMap.get(id);
        if (epicHashMap.containsKey(id)) return epicHashMap.get(id);
        if (subTaskHashMap.containsKey(id)) return subTaskHashMap.get(id);
        return null;
    }

    static void removeOneTask(int id) {
        taskHashMap.remove(id);
        epicHashMap.remove(id);
        subTaskHashMap.remove(id);
    }

    static void printAllTasksByEpic(Epic epic) {
        SubTask subTask;
        for (Integer subTaskId : epic.subTaskIds) {
            subTask = (SubTask) getInstance(subTaskId);
            System.out.println(subTask);
        }
    }
}
