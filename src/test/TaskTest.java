package test;

import taskService.Managers;
import taskService.Status;
import taskService.TaskManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;

import tasks.Task;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    public void TasksCorrectSavedAndReturnById() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task = new Task("Name1", "Description1", Status.NEW);
        int TaskId = inMemoryTaskManager.createTask(task);
        Task savedTask1 = inMemoryTaskManager.getTaskById(TaskId);
        Task savedTask2 = inMemoryTaskManager.getTaskById(TaskId);

        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals("Tasks.Task", savedTask1.getClass().getName(), "Задача другого типа.");
        assertEquals(savedTask1.getId(), savedTask2.getId(), "ID задач не совпадают.");
        assertEquals(savedTask1, savedTask2, "Задачи не совпадают.");
    }

    @Test
    public void TasksEqualsIfIdEquals() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        int[] idArray = new int[20];
        ArrayList<Task> tasks = new ArrayList<>();
        String name;
        String description;
        Status status;
        for (int i = 0; i < 20; i++) {
            name = "Name" + i;
            description = "Description" + i;
            if (i % 3 == 0) {
                status = Status.NEW;
            } else if (i % 2 == 0) {
                status = Status.IN_PROGRESS;
            } else {
                status = Status.DONE;
            }
            Task task = new Task(name, description, status);
            idArray[i] = inMemoryTaskManager.createTask(task);
            tasks.add(task);
        }
        Random rnd = new Random();
        int index = rnd.nextInt(20);
        Task task1 = inMemoryTaskManager.getTaskById(idArray[index]);
        Task task2 = inMemoryTaskManager.getTaskById(idArray[index]);

        assertEquals(task1.getId(), task2.getId()
                , "ID задач совпадают");
        assertEquals(task1, task2, "Задачи совпадают");
    }
}