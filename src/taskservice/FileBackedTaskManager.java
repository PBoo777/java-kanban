package taskservice;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.*;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try {
            String  content  = Files.readString(file.toPath());
            String[] lines = content.split("\n");
            HashMap<Integer, Task> newTaskHashMap = new HashMap<>();
            HashMap<Integer, Epic> newEpicHashMap = new HashMap<>();
            HashMap<Integer, SubTask> newSubTaskHashMap = new HashMap<>();
            int startLine = lines.length;
            for (int i = 0; i < lines.length; i++) {
                String[] fields = lines[i].split(",");
                boolean isValide;
                if (fields.length >= 5) {
                    try {
                        Integer.parseInt(fields[1]);
                        TaskTypes.valueOf(fields[0]);
                        Status.valueOf(fields[4]);
                        isValide = true;
                    } catch (NullPointerException | IllegalArgumentException e) {
                        isValide = false;
                    }
                    if (isValide) {
                        startLine = i;
                        break;
                    }
                }
            }
            for (int i = startLine; i < lines.length; i++) {
                String[] fields = lines[i].split(",");
                switch (fields[0]) {
                    case "EPIC":
                        Epic epic = new Epic(fields[2], fields[3]);
                        epic.setId(Integer.parseInt(fields[1]));
                        epic.setStatus(Status.valueOf(fields[4]));
                        if (!fields[5].equals("null")) {
                            ArrayList<Integer> subTaskIds = new ArrayList<>();
                            String[] ids = fields[5].split(":");
                            for (String id : ids) {
                                subTaskIds.add(Integer.parseInt(id));
                            }
                            epic.setSubTaskIds(subTaskIds);
                        }
                        epic.setStartTime(LocalDateTime.parse(fields[6]));
                        epic.setDuration(Duration.parse(fields[7]));
                        if ((epic.getStartTime() != null) && (epic.getDuration() != null)) {
                            epic.setEndTime(epic.getStartTime().plus(epic.getDuration()));
                        }
                        newEpicHashMap.put(epic.getId(), epic);
                        break;
                    case "SUBTASK":
                        SubTask subTask = new SubTask(fields[2], fields[3], Status.valueOf(fields[4]),
                                Integer.parseInt(fields[5]), LocalDateTime.parse(fields[6]), Duration.parse(fields[7]));
                        subTask.setId(Integer.parseInt(fields[1]));
                        newSubTaskHashMap.put(subTask.getId(), subTask);
                        break;
                    case "TASK":
                        Task task = new Task(fields[2], fields[3], Status.valueOf(fields[4]),
                                LocalDateTime.parse(fields[5]), Duration.parse(fields[6]));
                        task.setId(Integer.parseInt(fields[1]));
                        newTaskHashMap.put(task.getId(), task);
                        break;
                    default:
                        break;
                }
            }
            if (lines.length > 4) {
                String[] fields = lines[lines.length - 1].split(",");
                fileBackedTaskManager.id = Integer.parseInt(fields[1]) + 1;
            }
            fileBackedTaskManager.taskHashMap = newTaskHashMap;
            fileBackedTaskManager.epicHashMap = newEpicHashMap;
            fileBackedTaskManager.subTaskHashMap = newSubTaskHashMap;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return fileBackedTaskManager;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getName()))) {
            writer.write("Task: type,id,name,description,status,startTime,duration\n");
            writer.write("Epic: type,id,name,description,status,subTaskIds,startTime,duration\n");
            writer.write("Subtask: type,id,name,description,status,ownerId,startTime,duration\n");
            writer.write("------------------------------------------\n");
            int iteratedId = 1;
            while (true) {
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
            StringBuilder subTaskIds = new StringBuilder();
            if (!epic.getSubTaskIds().isEmpty()) {
                epic.getSubTaskIds().forEach(id -> subTaskIds.append(id).append(":"));
                subTaskIds.deleteCharAt(subTaskIds.length() - 1);
            } else {
                subTaskIds.append("null");
            }
            return String.format("%s,%d,%s,%s,%s,%s," + epic.getStartTime() + "," + epic.getDuration() + "\n",
                    TaskTypes.EPIC, epic.getId(), epic.getName(), epic.getDescription(), epic.getStatus(), subTaskIds);
        } else if (task instanceof SubTask subTask) {
            return String.format("%s,%d,%s,%s,%s,%d," + subTask.getStartTime() + "," + subTask.getDuration() + "\n",
                    TaskTypes.SUBTASK, subTask.getId(), subTask.getName(), subTask.getDescription(),
                    subTask.getStatus(), subTask.getOwnerId());
        } else {
            return String.format("%s,%d,%s,%s,%s," + task.getStartTime() + "," + task.getDuration() + "\n",
                    TaskTypes.TASK, task.getId(), task.getName(), task.getDescription(), task.getStatus());
        }
    }

    @Override
    public int createTask(Task task) {
        int result = super.createTask(task);
        save();
        return result;
    }

    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        save();
    }

    @Override
    public void removeAllTasks(TaskTypes type) {
        super.removeAllTasks(type);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    public void setFile(File file) {
        this.file = file;
    }
}
