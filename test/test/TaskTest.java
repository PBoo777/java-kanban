package test;

import taskservice.Managers;
import taskservice.Status;
import taskservice.TaskManager;
import static org.junit.jupiter.api.Assertions.*;
import static tasks.ExampleTasks.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Random;
import tasks.Task;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    public void tasksCorrectEqualOverride() {
        task1.setId(122);
        Task task = new Task(task1.getName(), task1.getDescription(), task1.getStatus(), task1.getStartTime(),
                task1.getDuration());
        task.setId(task1.getId());
        assertEquals(task1, task, "Метод equals в задаче реализован некорректно");
    }

    @Test
    public void tasksEqualsIfIdEqualsByRandomSample() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        int[] idArray = new int[20];
        String name;
        String description;
        Status status;
        LocalDateTime start = LocalDateTime.of(2025, Month.MAY, 15, 0, 0);
        Duration dur = Duration.ofDays(1);
        Duration durPlus = Duration.ofMinutes(1);
        for (int i = 0; i < 20; i++) {
            name = "Name" + i;
            description = "Description" + i;
            start = start.plus(dur).plus(dur);
            dur = dur.plus(durPlus);
            if (i % 3 == 0) {
                status = Status.NEW;
            } else if (i % 2 == 0) {
                status = Status.IN_PROGRESS;
            } else {
                status = Status.DONE;
            }
            Task task = new Task(name, description, status, start, dur);
            idArray[i] = inMemoryTaskManager.createTask(task);
        }
        Random rnd = new Random();
        int index = rnd.nextInt(20);
        Task task1 = inMemoryTaskManager.getTaskById(idArray[index]);
        Task task2 = inMemoryTaskManager.getTaskById(idArray[index]);

        assertEquals(task1.getId(), task2.getId(), "ID задач не совпадают");
        assertEquals(task1, task2, "Задачи не совпадают");
    }
}