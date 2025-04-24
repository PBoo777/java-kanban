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

        Epic originEpic = new Epic("Name1", "Description1");
        int epicId = inMemoryTaskManager.createTask(originEpic);
        Epic savedEpic1 = (Epic) inMemoryTaskManager.getTaskById(epicId);
        Epic savedEpic2 = (Epic) inMemoryTaskManager.getTaskById(epicId);

        assertNotNull(savedEpic1, "Задача не найдена.");
        assertEquals("Tasks.Epic", savedEpic1.getClass().getName(), "Задача другого типа.");
        assertEquals(savedEpic1.getId(), savedEpic2.getId(), "ID задач не совпадают.");
        assertEquals(savedEpic1.getSubTaskIds(), savedEpic2.getSubTaskIds(), "Списки подзадач не совпадают");
        assertEquals(savedEpic1.getSubTaskStatuses(), savedEpic2.getSubTaskStatuses(), "Списки подзадач не совпадают");
        assertEquals(savedEpic1, savedEpic2, "Задачи не совпадают.");
    }
}