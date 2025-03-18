import TaskService.Manager;
import TaskService.Status;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Учёба", "Пройти тему в Практикуме", Status.IN_PROGRESS);
        Task task2 = new Task("Учёба", "Позаниматься на других материалах", Status.DONE);
        Task task3 = new Task("Учёба", "Поучить английский", Status.NEW);
        Epic epic1 = new Epic("Дела по хозяйству", "Ремонт");
        Epic epic2 = new Epic("Работа", "Задачи по текущей работе");

        SubTask subTask1 = new SubTask("Обои", "Найти и купить обои", Status.NEW, epic1);
        SubTask subTask2 = new SubTask("Потолок", "Покрасить потолок", Status.NEW, epic1);
        SubTask subTask3 = new SubTask("На работе", "Работать", Status.NEW, epic2);
        SubTask subTask4 = new SubTask("Санузел", "Поклеить плитку", Status.NEW, epic1);

        Manager.createTask(task1);
        Manager.createTask(task2);
        Manager.createTask(task3);
        Manager.createTask(epic1);
        Manager.createTask(epic2);
        Manager.createTask(subTask1);
        Manager.createTask(subTask2);
        Manager.createTask(subTask3);
        Manager.createTask(subTask4);

        Manager.printAllTasks("Tasks.Task");
        Manager.printAllTasks("Tasks.Epic");
        Manager.printAllTasks("Tasks.SubTask");

        System.out.println(Manager.getTaskById(8));

    }
}