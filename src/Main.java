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

        Manager.createSomeTask(task1);
        Manager.createSomeTask(task2);
        Manager.createSomeTask(task3);
        Manager.createSomeTask(epic1);
        Manager.createSomeTask(epic2);
        Manager.createSomeTask(subTask1);
        Manager.createSomeTask(subTask2);
        Manager.createSomeTask(subTask3);
        Manager.createSomeTask(subTask4);

        Manager.printAllTasks("Task");
        Manager.printAllTasks("Epic");
        Manager.printAllTasks("SubTask");

        System.out.println(Manager.getInstance(8));

    }
}