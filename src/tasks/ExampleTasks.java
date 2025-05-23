package tasks;

import taskservice.Status;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class ExampleTasks {
    public static final Task defaultTask = new Task("DEFAULT", "DEFAULT", Status.NEW, null,
            null);

    private static final Duration tenDays = Duration.ofDays(10);
    private static final Duration halfDay = Duration.ofMinutes(60 * 12);
    private static final Duration twoDays = Duration.ofDays(2);
    private static final Duration oneDay = Duration.ofDays(1);

    public static final Task task1 = new Task("Учёба", "Пройти тему в Практикуме",Status.IN_PROGRESS,
            LocalDateTime.of(2020, Month.MAY, 15, 0, 0), tenDays);
    public static final Task task2 = new Task("Учёба", "Позаниматься на других материалах", Status.DONE,
            task1.getEndTime().plus(halfDay), twoDays);
    public static final Task task3 = new Task("Учёба", "Поучить английский", Status.NEW,
            task2.getEndTime().plus(halfDay), twoDays);

    public static final Epic epic1 = new Epic("Дела по хозяйству", "Ремонт");
    public static final Epic epic2 = new Epic("Работа", "Задачи по текущей работе");

    public static final SubTask subTask1 = new SubTask("Обои", "Найти и купить обои", Status.DONE,
            0, LocalDateTime.of(2025, Month.JUNE, 3, 9, 0), halfDay);
    public static final SubTask subTask2 = new SubTask("Потолок", "Покрасить потолок", Status.DONE,
            0, LocalDateTime.of(2025, Month.JUNE, 5, 9, 0), oneDay);
    public static final SubTask subTask3 = new SubTask("На работе", "Работать", Status.NEW,
            0, LocalDateTime.of(2025, Month.JUNE, 8, 8, 0), tenDays);
    public static final SubTask subTask4 = new SubTask("Санузел", "Поклеить плитку", Status.DONE,
            0, LocalDateTime.of(2025, Month.JUNE, 15, 9, 0), twoDays);
    public static final SubTask subTask5 = new SubTask("Вне работы", "Отдыхать", Status.IN_PROGRESS,
            0, LocalDateTime.of(2025, Month.JUNE, 19, 0, 0), tenDays);
}
