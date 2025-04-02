package Tasks;

import TaskService.Managers;
import TaskService.Status;
import TaskService.TaskManager;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TaskTest {
    TaskManager inMemoryTaskManager = Managers.getDefault();

    @Test
    public void TasksCorrectSavedAndReturnById() {

        Task task = new Task("Name1", "Description1", Status.NEW);
        int TaskId = inMemoryTaskManager.createTask(task);
        Task savedTask = inMemoryTaskManager.getTaskById(TaskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals("Tasks.Task", savedTask.getClass().getName(), "Задача другого типа.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }
}