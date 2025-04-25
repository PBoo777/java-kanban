import taskService.InMemoryTaskManager;
import taskService.Status;
import taskService.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Учёба", "Пройти тему в Практикуме", Status.IN_PROGRESS);
        Task task2 = new Task("Учёба", "Позаниматься на других материалах", Status.DONE);
        Task task3 = new Task("Учёба", "Поучить английский", Status.NEW);
        Epic epic1 = new Epic("Дела по хозяйству", "Ремонт");
        Epic epic2 = new Epic("Работа", "Задачи по текущей работе");

        int epic1Id = inMemoryTaskManager.createTask(epic1);
        int epic2Id = inMemoryTaskManager.createTask(epic2);

        SubTask subTask1 = new SubTask("Обои", "Найти и купить обои", Status.NEW, epic1Id);
        SubTask subTask2 = new SubTask("Потолок", "Покрасить потолок", Status.NEW, epic1Id);
        SubTask subTask3 = new SubTask("На работе", "Работать", Status.NEW, epic2Id);
        SubTask subTask4 = new SubTask("Санузел", "Поклеить плитку", Status.NEW, epic1Id);

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);

        inMemoryTaskManager.createTask(subTask1);
        inMemoryTaskManager.createTask(subTask2);
        inMemoryTaskManager.createTask(subTask3);
        inMemoryTaskManager.createTask(subTask4);

        inMemoryTaskManager.printAllTasks("Tasks.Task");
        System.out.println("    ----   Это были Таски  ---- ");
        inMemoryTaskManager.printAllTasks("Tasks.Epic");
        System.out.println("    ----   Это были Эпики  ----  ");
        inMemoryTaskManager.printAllTasks("Tasks.SubTask");
        System.out.println("    ----   Это были Сабтаски  ----  ");


        System.out.println(inMemoryTaskManager.getTaskById(8));
        System.out.println("    ----   Это был Таск #Id 8 ");

        inMemoryTaskManager.printAllTasksByEpic(epic1);
        System.out.println("    ----   Это были Сабтаски Эпика1  ---- ");

        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getTaskById(5);
        inMemoryTaskManager.getTaskById(7);
        inMemoryTaskManager.getTaskById(3);
        inMemoryTaskManager.getTaskById(1);

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("    ----   Это была история  ---- ");

        inMemoryTaskManager.removeTaskById(8);
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("    ----   Это была история после remove #Id 8  ----  ");

        inMemoryTaskManager.removeAllTasks("Tasks.Task");

        inMemoryTaskManager.printAllTasks("Tasks.Task");
        System.out.println("    ----    ");

        inMemoryTaskManager.printAllTasks("Tasks.Epic");
        System.out.println("    ----    ");

        inMemoryTaskManager.printAllTasks("Tasks.SubTask");
        System.out.println("    ----    ");



    }
}