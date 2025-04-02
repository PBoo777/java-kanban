import TaskService.InMemoryTaskManager;
import TaskService.Status;
import TaskService.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Учёба", "Пройти тему в Практикуме", Status.IN_PROGRESS);
        Task task2 = new Task("Учёба", "Позаниматься на других материалах", Status.DONE);
        Task task3 = new Task("Учёба", "Поучить английский", Status.NEW);
        Epic epic1 = new Epic("Дела по хозяйству", "Ремонт");
        Epic epic2 = new Epic("Работа", "Задачи по текущей работе");

        SubTask subTask1 = new SubTask("Обои", "Найти и купить обои", Status.NEW, epic1);
        SubTask subTask2 = new SubTask("Потолок", "Покрасить потолок", Status.NEW, epic1);
        SubTask subTask3 = new SubTask("На работе", "Работать", Status.NEW, epic2);
        SubTask subTask4 = new SubTask("Санузел", "Поклеить плитку", Status.NEW, epic1);

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.createTask(epic1);
        inMemoryTaskManager.createTask(epic2);
        inMemoryTaskManager.createTask(subTask1);
        inMemoryTaskManager.createTask(subTask2);
        inMemoryTaskManager.createTask(subTask3);
        inMemoryTaskManager.createTask(subTask4);

        inMemoryTaskManager.printAllTasks("Tasks.Task");
        inMemoryTaskManager.printAllTasks("Tasks.Epic");
        inMemoryTaskManager.printAllTasks("Tasks.SubTask");

        System.out.println(inMemoryTaskManager.getTaskById(8));

    }
}