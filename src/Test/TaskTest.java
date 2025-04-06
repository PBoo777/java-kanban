package Test;

import TaskService.Managers;
import TaskService.Status;
import TaskService.TaskManager;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;

import Tasks.Task;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    public void TasksCorrectSavedAndReturnById() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task = new Task("Name1", "Description1", Status.NEW);
        int TaskId = inMemoryTaskManager.createTask(task);
        Task savedTask = inMemoryTaskManager.getTaskById(TaskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals("Tasks.Task", savedTask.getClass().getName(), "Задача другого типа.");
        assertEquals(task.getId(), savedTask.getId(), "ID задач не совпадают.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
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

        assertEquals(inMemoryTaskManager.getTaskById(idArray[index]).getId(), tasks.get(index).getId()
                , "ID задач совпадают");
        assertEquals(inMemoryTaskManager.getTaskById(idArray[index]), tasks.get(index), "Задачи совпадают");
    }
}