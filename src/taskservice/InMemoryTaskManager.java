package taskservice;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import static tasks.ExampleTasks.defaultTask;
import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskHashMap = new HashMap<>();
    int id = 1;
    final HistoryManager historyManager = Managers.getDefaultHistory();

    Set<Task> prioritizedTaskSet = new TreeSet<>((t1, t2) -> {
        if (t1.getStartTime().isAfter(t2.getStartTime())) {
            return 1;
        } else if (t1.getStartTime().isBefore(t2.getStartTime())) {
            return -1;
        } else {
            return 0;
        }
    });

    @Override
    public int createTask(Task task) {
        if (task instanceof Epic epic) {
            if (!epicHashMap.containsValue(epic)) {
                Epic newEpic = new Epic(epic.getName(), epic.getDescription());
                newEpic.setSubTaskIds(epic.getSubTaskIds());
                newEpic.setId(id++);
                epicUpdate(newEpic);
                epicHashMap.put(newEpic.getId(), newEpic);
                return newEpic.getId();
            }
        } else if (task instanceof SubTask subTask) {
            if (disallowTaskByOverlapping(subTask)) return -1;
            if (!subTaskHashMap.containsValue(subTask)) {
                SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                        epicHashMap.containsKey(subTask.getOwnerId()) ? subTask.getOwnerId() : 0,
                        subTask.getStartTime(), subTask.getDuration());
                newSubTask.setId(id++);
                subTaskHashMap.put(newSubTask.getId(), newSubTask);
                if (newSubTask.getOwnerId() != 0) {
                    setOneSubTaskToEpic(newSubTask, epicHashMap.get(newSubTask.getOwnerId()));
                }
                if (newSubTask.getStartTime() != null) {
                    prioritizedTaskSet.add(newSubTask);
                }
                return newSubTask.getId();
            }
        } else {
            if (disallowTaskByOverlapping(task)) return -1;
            if (!taskHashMap.containsValue(task)) {
                Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus(), task.getStartTime(),
                        task.getDuration());
                newTask.setId(id++);
                taskHashMap.put(newTask.getId(), newTask);
                if (newTask.getStartTime() != null) {
                    prioritizedTaskSet.add(newTask);
                }
                return newTask.getId();
            }
        }
        return -2;
    }

    @Override
    public void updateTask(Task task, int id) {
        if (subTaskHashMap.containsKey(id)) {
            if (epicHashMap.containsKey(subTaskHashMap.get(id).getOwnerId())) {
                removeOneSubTaskFromEpic(subTaskHashMap.get(id), epicHashMap.get(subTaskHashMap.get(id).getOwnerId()));
            }
        }
        if (epicHashMap.containsKey(id)) {
            epicHashMap.get(id).getSubTaskIds().forEach(subTaskId ->
                    subTaskHashMap.get(subTaskId).setOwnerId(0));
        }
        if ((task.getStartTime() != null) && (task.getDuration() != null)) {
            prioritizedTaskSet.remove(getTaskFromHashMap(id));
        }
        if (task instanceof Epic epic) {
            Epic newEpic = new Epic(epic.getName(), epic.getDescription());
            newEpic.setSubTaskIds(epic.getSubTaskIds());
            if ((id <= 0) || (this.id <= id)) {
                newEpic.setId(this.id++);
            } else {
                newEpic.setId(id);
            }
            epicUpdate(newEpic);
            epicHashMap.put(newEpic.getId(), newEpic);
        } else if (task instanceof SubTask subTask) {
            if (disallowTaskByOverlapping(subTask)) return;
            SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getStatus(),
                    epicHashMap.containsKey(subTask.getOwnerId()) ? subTask.getOwnerId() : 0,
                    subTask.getStartTime(), subTask.getDuration());
            if ((id <= 0) || (this.id <= id)) {
                newSubTask.setId(this.id++);
            } else {
                newSubTask.setId(id);
            }
            subTaskHashMap.put(newSubTask.getId(), newSubTask);
            if (newSubTask.getOwnerId() != 0) {
                setOneSubTaskToEpic(newSubTask, epicHashMap.get(newSubTask.getOwnerId()));
            }
        } else {
            if (disallowTaskByOverlapping(task)) return;
            Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus(), task.getStartTime(),
                    task.getDuration());
            taskHashMap.put(id, newTask);
        }
    }

    @Override
    public void printAllTasks(TaskTypes type) {
        String s = getString();
        switch (type) {
            case TASK:
                taskHashMap.values().forEach(task -> {
                    String sId = task.getId() + ",";
                    String name = task.getName() + "',";
                    String ds = task.getDescription() + "',";
                    String sType = TaskTypes.TASK + ":";
                    System.out.printf(s, sType, sId, name, ds, task.getStatus());
                    System.out.println();
                });
                break;
            case EPIC:
                epicHashMap.values().forEach(epic -> {
                    String sId = epic.getId() + ",";
                    String name = epic.getName() + "',";
                    String ds = epic.getDescription() + "',";
                    String sType = TaskTypes.EPIC + ":";
                    String st = epic.getStatus() + ",";
                    System.out.printf(s, sType, sId, name, ds, st);
                    System.out.println("subTaskIds = " + epic.getSubTaskIds());
                });
                break;
            case SUBTASK:
                subTaskHashMap.values().forEach(subTask -> {
                    String sId = subTask.getId() + ",";
                    String name = subTask.getName() + "',";
                    String ds = subTask.getDescription() + "',";
                    String sType = TaskTypes.SUBTASK + ":";
                    String st = subTask.getStatus() + ",";
                    System.out.printf(s, sType, sId, name, ds, st);
                    System.out.println("owner id = " + subTask.getOwnerId());
                });
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
        Task task = getTaskFromHashMap(id);
        if (!task.equals(defaultTask)) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void removeTaskById(int id) {
        prioritizedTaskSet.remove(getTaskFromHashMap(id));
        if (subTaskHashMap.containsKey(id)) {
            if (subTaskHashMap.get(id).getOwnerId() != 0) {
                removeOneSubTaskFromEpic(subTaskHashMap.get(id), epicHashMap.get(subTaskHashMap.get(id).getOwnerId()));
            }
            subTaskHashMap.remove(id);
        }
        if (epicHashMap.containsKey(id)) {
            epicHashMap.get(id).getSubTaskIds().forEach(subId -> subTaskHashMap.get(subId).setOwnerId(0));
            epicHashMap.remove(id);
        }
        taskHashMap.remove(id);
    }

    @Override
    public void printAllTasksByEpic(Epic epic) {
        epic.getSubTaskIds().forEach(id -> System.out.println(subTaskHashMap.get(id)));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void clear() {
        taskHashMap.clear();
        epicHashMap.clear();
        subTaskHashMap.clear();
        prioritizedTaskSet.clear();
        ((InMemoryHistoryManager) historyManager).clear();
        id = 1;
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

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTaskSet;
    }

    private void updateStatus(Epic epic) {
        if (epic.getSubTaskIds().isEmpty()) return;
        if (epic.getSubTaskIds().size() == 1) {
            epic.setStatus(Objects.requireNonNull(subTaskHashMap.get(epic.getSubTaskIds().getFirst())).getStatus());
            return;
        }
        for (int i = 0; i < epic.getSubTaskIds().size() - 1; i++) {
            if (Objects.requireNonNull(subTaskHashMap.get(epic.getSubTaskIds().get(i)).getStatus())
                    != Objects.requireNonNull(subTaskHashMap.get(epic.getSubTaskIds().get(i + 1)).getStatus())
                    || Objects.requireNonNull(subTaskHashMap.get(epic.getSubTaskIds().get(i)).getStatus()) ==
                    Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        epic.setStatus(Objects.requireNonNull(subTaskHashMap.get(epic.getSubTaskIds().getFirst())).getStatus());
    }

    private void setOneSubTaskToEpic(SubTask newSubTask, Epic epic) {
        epic.setNewSubTaskId(newSubTask.getId());
        epicUpdate(epic);
    }

    private void removeOneSubTaskFromEpic(SubTask removedSubTask, Epic epic) {
        epic.removeSubTaskId(removedSubTask.getId());
        epicUpdate(epic);
    }

    private void updateTime(Epic epic) {
        if (epic.getSubTaskIds().isEmpty()) return;
        if (epic.getSubTaskIds().size() == 1) {
            epic.setStartTime(subTaskHashMap.get(epic.getSubTaskIds().getFirst()).getStartTime());
            epic.setDuration(subTaskHashMap.get(epic.getSubTaskIds().getFirst()).getDuration());
            if ((epic.getStartTime() != null) && (epic.getDuration() != null)) {
                epic.setEndTime(epic.getStartTime().plus(epic.getDuration()));
            }
            return;
        }
        Comparator<LocalDateTime> comparator = ((t1, t2) -> {
            if (t1.isAfter(t2)) {
                return 1;
            } else if (t1.isBefore(t2)) {
                return -1;
            } else {
                return 0;
            }
        });
        if (epic.getSubTaskIds().stream().anyMatch(subTaskId -> subTaskHashMap.get(subTaskId).getStartTime() !=
                null)) {
            epic.setStartTime(epic.getSubTaskIds().stream().map(subTaskId ->
                subTaskHashMap.get(subTaskId).getStartTime()).filter(Objects::nonNull).min(comparator).get());
        }
        if (epic.getSubTaskIds().stream().anyMatch(subTaskId -> subTaskHashMap.get(subTaskId).getEndTime() !=
                null)) {
            epic.setEndTime(epic.getSubTaskIds().stream().map(subTaskId ->
                subTaskHashMap.get(subTaskId).getEndTime()).filter(Objects::nonNull).max(comparator).get());
        }
        if (epic.getSubTaskIds().stream().anyMatch(subTaskId -> subTaskHashMap.get(subTaskId).getDuration() !=
                null)) {
            epic.setDuration(epic.getSubTaskIds().stream().map(subTaskId ->
                subTaskHashMap.get(subTaskId).getDuration()).filter(Objects::nonNull).reduce(Duration::plus).get());
        }
    }

    private void epicUpdate(Epic epic) {
        updateStatus(epic);
        updateTime(epic);
//        if ((epic.getStartTime() != null) && (epic.getDuration() != null)) {
//            Epic newEpic = new Epic(epic.getName(), epic.getDescription());
//            newEpic.setSubTaskIds(epic.getSubTaskIds());
//            newEpic.setId(epic.getId());
//            newEpic.setStartTime(epic.getStartTime());
//            newEpic.setDuration(epic.getDuration());
//            newEpic.setEndTime(epic.getEndTime());
//            prioritizedTaskSet.add(epic);
//        } else {
//            Optional<Task> o = prioritizedTaskSet.stream().filter(ep -> ep.getId() == epic.getId()).findFirst();
//            o.ifPresent(task -> prioritizedTaskSet.remove(task));
//        }
    }

    private Task getTaskFromHashMap(int id) {
        if (taskHashMap.containsKey(id)) {
            Task newTask = new Task(taskHashMap.get(id).getName(), taskHashMap.get(id).getDescription(),
                    taskHashMap.get(id).getStatus(), taskHashMap.get(id).getStartTime(),
                    taskHashMap.get(id).getDuration());
            newTask.setId(id);
            return newTask;
        }
        if (epicHashMap.containsKey(id)) {
            Epic newEpic = new Epic(epicHashMap.get(id).getName(), epicHashMap.get(id).getDescription());
            newEpic.setSubTaskIds(epicHashMap.get(id).getSubTaskIds());
            updateStatus(newEpic);
            updateTime(newEpic);
            newEpic.setId(id);
            return newEpic;
        }
        if (subTaskHashMap.containsKey(id)) {
            SubTask newSubTask = new SubTask(subTaskHashMap.get(id).getName(), subTaskHashMap.get(id).getDescription(),
                    subTaskHashMap.get(id).getStatus(), subTaskHashMap.get(id).getOwnerId(),
                    subTaskHashMap.get(id).getStartTime(), subTaskHashMap.get(id).getDuration());
            newSubTask.setId(id);
            return newSubTask;
        }
        return defaultTask;
    }

    private String getString() {
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
        return "%-9s id = %-4s name = '%" + (nameMaxLength * (-1) - 4) + "s description " +
                "= '%" + (descriptionMaxLength * (-1) - 4) + "s status = %-15s";
    }

    private boolean notOverlapping(Task task1, Task task2) {
        if ((task2.getStartTime() != null) && (task2.getDuration() != null)) {
            return task1.getStartTime().isAfter(task2.getEndTime()) || task2.getStartTime().isAfter(task1.getEndTime());
        } else return true;
    }

    private boolean disallowTaskByOverlapping(Task task) {
        return prioritizedTaskSet.stream().anyMatch(prioritizedTask -> !notOverlapping(prioritizedTask, task));
    }
}