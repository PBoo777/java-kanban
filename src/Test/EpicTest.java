package Test;

import TaskService.Managers;
import TaskService.TaskManager;
import static org.junit.jupiter.api.Assertions.*;

import Tasks.Epic;
import org.junit.jupiter.api.Test;

class EpicTest {
    TaskManager inMemoryTaskManager = Managers.getDefault();

    @Test
    public void EpicsCorrectSavedAndReturnById() {

        Epic epic = new Epic("Name1", "Description1");
        int epicId = inMemoryTaskManager.createTask(epic);
        Epic savedEpic = (Epic) inMemoryTaskManager.getTaskById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals("Tasks.Epic", savedEpic.getClass().getName(), "Задача другого типа.");
        assertEquals(epic.getId(), savedEpic.getId(), "ID задач не совпадают.");
        assertEquals(epic.getSubTasks(), savedEpic.getSubTasks(), "Списки подзадач не совпадают");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }
}