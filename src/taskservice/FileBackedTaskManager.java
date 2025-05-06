package taskservice;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.*;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    String fileName;

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.getName());
        try {
            String  content  = Files.readString(file.toPath());
            String[] lines = content.split("\n");
            HashMap<Integer, Task> newTaskHashMap = new HashMap<>();
            HashMap<Integer, Epic> newEpicHashMap = new HashMap<>();
            HashMap<Integer, SubTask> newSubTaskHashMap = new HashMap<>();
            for (int i = 4; i < lines.length; i++) {
                String[] fields = lines[i].split(",");
                switch (fields[1]) {
                    case "EPIC":
                        Epic epic = new Epic(fields[2], fields[3]);
                        epic.setId(Integer.parseInt(fields[0]));
                        epic.setStatus(Status.valueOf(fields[4]));
                        if (!fields[5].equals("null")) {
                            ArrayList<Status> subTaskStatuses = new ArrayList<>();
                            ArrayList<Integer> subTaskIds = new ArrayList<>();
                            String[] statuses = fields[5].split(":");
                            for (String status : statuses) {
                                subTaskStatuses.add(Status.valueOf(status));
                            }
                            String[] ids = fields[6].split(":");
                            for (String id : ids) {
                                subTaskIds.add(Integer.parseInt(id));
                            }
                            epic.setSubTaskStatuses(subTaskStatuses);
                            epic.setSubTaskIds(subTaskIds);
                        }
                        newEpicHashMap.put(epic.getId(), epic);
                        break;
                    case "SUBTASK":
                        SubTask subTask = new SubTask(fields[2], fields[3], Status.valueOf(fields[4]),
                                Integer.parseInt(fields[5]));
                        subTask.setId(Integer.parseInt(fields[0]));
                        newSubTaskHashMap.put(subTask.getId(), subTask);
                        break;
                    case "TASK":
                        Task task = new Task(fields[2], fields[3], Status.valueOf(fields[4]));
                        task.setId(Integer.parseInt(fields[0]));
                        newTaskHashMap.put(task.getId(), task);
                        break;
                    default:
                        break;
                }
            }
            if (lines.length > 4) {
                String[] fields = lines[lines.length - 1].split(",");
                fileBackedTaskManager.id = Integer.parseInt(fields[0]) + 1;
            }
            fileBackedTaskManager.taskHashMap = newTaskHashMap;
            fileBackedTaskManager.epicHashMap = newEpicHashMap;
            fileBackedTaskManager.subTaskHashMap = newSubTaskHashMap;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return fileBackedTaskManager;
    }

    public void save(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Task: id,name,description,status\n");
            writer.write("Epic: id,name,description,status,subTaskStatuses,subTaskIds\n");
            writer.write("Subtask: id,name,description,status,ownerId\n");
            writer.write("------------------------------------------\n");
            int iteratedId = 1;
            while (true){
                if (taskHashMap.containsKey(iteratedId)) {
                    writer.write(taskToString(taskHashMap.get(iteratedId)));
                } else if (epicHashMap.containsKey(iteratedId)) {
                    writer.write(taskToString(epicHashMap.get(iteratedId)));
                } else if (subTaskHashMap.containsKey(iteratedId)) {
                    writer.write(taskToString(subTaskHashMap.get(iteratedId)));
                } else {
                    break;
                }
                iteratedId++;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла задач");
        }
    }

    public String taskToString(Task task) {
        if (task instanceof Epic epic) {
            StringBuilder subTaskStatuses = new StringBuilder();
            StringBuilder subTaskIds = new StringBuilder();
            if (!epic.getSubTaskStatuses().isEmpty()) {
                for (Status status : epic.getSubTaskStatuses()) {
                    subTaskStatuses.append(status.name()).append(":");
                }
                subTaskStatuses.deleteCharAt(subTaskStatuses.length() - 1);
                for (int id : epic.getSubTaskIds()) {
                    subTaskIds.append(id).append(":");
                }
                subTaskIds.deleteCharAt(subTaskIds.length() - 1);
            } else {
                subTaskStatuses.append("null");
                subTaskIds.append("null");
            }
            return String.format("%d,%s,%s,%s,%s,%s,%s\n", epic.getId(), TaskTypes.EPIC, epic.getName(),
                    epic.getDescription(), epic.getStatus(), subTaskStatuses, subTaskIds);
        } else if (task instanceof SubTask subTask) {
            return String.format("%d,%s,%s,%s,%s,%d\n", subTask.getId(), TaskTypes.SUBTASK, subTask.getName(),
                    subTask.getDescription(), subTask.getStatus(), subTask.getOwnerId());
        } else {
            return String.format("%d,%s,%s,%s,%s\n", task.getId(), TaskTypes.TASK, task.getName(), task.getDescription(),
                    task.getStatus());
        }
    }

    @Override
    public int createTask(Task task) {
        int result = super.createTask(task);
        save(fileName);
        return result;
    }

    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        save(fileName);
    }

    @Override
    public void removeAllTasks(TaskTypes type) {
        super.removeAllTasks(type);
        save(fileName);
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save(fileName);
    }
}
