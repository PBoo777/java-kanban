import taskservice.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("Save.txt");
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

        System.out.printf("%d, %d, %d\n", id1, id2, id3);
        fileBackedTaskManager.printAllTasks(TaskTypes.TASK);
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();

        File file = new File("Save.txt");
        FileBackedTaskManager f2 = FileBackedTaskManager.loadFromFile(file);
        System.out.println("------------------------------------");
        f2.printAllTasks(TaskTypes.TASK);
        System.out.println("------------------------------------");

        f2.printAllTasks(TaskTypes.EPIC);
        System.out.println("------------------------------------");

        f2.printAllTasks(TaskTypes.SUBTASK);
    }
}