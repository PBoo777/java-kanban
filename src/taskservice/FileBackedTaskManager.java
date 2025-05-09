package taskservice;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

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
            int startLine = lines.length;
            for (int i = 0; i < lines.length; i++) {
                String[] fields = lines[i].split(",");
                boolean isValide;
                if (fields.length >= 5) {
                    try {
                        Integer.parseInt(fields[0]);
                        TaskTypes.valueOf(fields[1]);
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

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Task: id,name,description,status\n");
            writer.write("Epic: id,name,description,status,subTaskStatuses,subTaskIds\n");
            writer.write("Subtask: id,name,description,status,ownerId\n");
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        File file = new File("Save.txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.getName());
        Task task1 = new Task("Учёба", "Пройти тему в Практикуме", Status.IN_PROGRESS);
        Task task2 = new Task("Учёба", "Позаниматься на других материалах", Status.DONE);
        Task task3 = new Task("Учёба", "Поучить английский", Status.NEW);

        int id1 = fileBackedTaskManager.createTask(task1);
        int id2 = fileBackedTaskManager.createTask(task2);
        int id3 = fileBackedTaskManager.createTask(task3);
        Epic epic1 = new Epic("Дела по хозяйству", "Ремонт");
        Epic epic2 = new Epic("Работа", "Задачи по текущей работе");

        int epic1Id = fileBackedTaskManager.createTask(epic1);
        int epic2Id = fileBackedTaskManager.createTask(epic2);

        SubTask subTask1 = new SubTask("Обои", "Найти и купить обои", Status.DONE, epic1Id);
        SubTask subTask2 = new SubTask("Потолок", "Покрасить потолок", Status.DONE, epic1Id);
        SubTask subTask3 = new SubTask("На работе", "Работать", Status.NEW, epic2Id);
        SubTask subTask4 = new SubTask("Санузел", "Поклеить плитку", Status.DONE, epic1Id);
        SubTask subTask5 = new SubTask("Вне работы", "Отдыхать", Status.IN_PROGRESS, epic2Id);

        fileBackedTaskManager.createTask(subTask1);
        fileBackedTaskManager.createTask(subTask2);
        fileBackedTaskManager.createTask(subTask3);
        fileBackedTaskManager.createTask(subTask4);
        fileBackedTaskManager.createTask(subTask5);

        System.out.println("Оригинальный fileBackedTaskManager:");
        System.out.println();
        fileBackedTaskManager.printAllTasks(TaskTypes.TASK);
        fileBackedTaskManager.printAllTasks(TaskTypes.EPIC);
        fileBackedTaskManager.printAllTasks(TaskTypes.SUBTASK);

        System.out.println();
        System.out.println("Копия fileBackedTaskManager:");
        System.out.println();
        FileBackedTaskManager f2 = FileBackedTaskManager.loadFromFile(file);
        f2.printAllTasks(TaskTypes.TASK);
        f2.printAllTasks(TaskTypes.EPIC);
        f2.printAllTasks(TaskTypes.SUBTASK);
    }
}
