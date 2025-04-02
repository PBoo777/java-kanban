package Tasks;

import TaskService.Managers;
import TaskService.Status;
import TaskService.TaskManager;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SubTaskTest {
    TaskManager inMemoryTaskManager = Managers.getDefault();

    @Test
    public void SubTasksCorrectSavedAndReturnById() {
        Epic epic = new Epic("EName", "EDescription1");
        SubTask subTask = new SubTask("Name1", "Description1", Status.NEW, epic);
        int subTaskId = inMemoryTaskManager.createTask(subTask);
        SubTask savedSubTask = (SubTask) inMemoryTaskManager.getTaskById(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals("Tasks.SubTask", savedSubTask.getClass().getName(), "Задача другого типа.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");
        assertEquals(subTask.getOwner(), savedSubTask.getOwner(), "Эпик не совпадает.");
    }
}