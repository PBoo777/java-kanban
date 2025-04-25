package test;

import TaskService.Managers;
import TaskService.Status;
import TaskService.TaskManager;
import static org.junit.jupiter.api.Assertions.*;

import tasks.Epic;
import tasks.SubTask;
import org.junit.jupiter.api.Test;

class SubTaskTest {
    TaskManager inMemoryTaskManager = Managers.getDefault();

    @Test
    public void SubTasksCorrectSavedAndReturnById() {
        Epic epic = new Epic("EName", "EDescription1");
        int epicId = inMemoryTaskManager.createTask(epic);
        SubTask subTask = new SubTask("Name1", "Description1", Status.NEW, epicId);
        int subTaskId = inMemoryTaskManager.createTask(subTask);
        SubTask savedSubTask1 = (SubTask) inMemoryTaskManager.getTaskById(subTaskId);
        SubTask savedSubTask2 = (SubTask) inMemoryTaskManager.getTaskById(subTaskId);

        assertNotNull(savedSubTask1, "Задача не найдена.");
        assertEquals("Tasks.SubTask", savedSubTask1.getClass().getName(), "Задача другого типа.");
        assertEquals(savedSubTask1.getId(), savedSubTask2.getId(), "ID задач не совпадают.");
        assertEquals(savedSubTask1.getOwnerId(), savedSubTask2.getOwnerId(), "Эпик не совпадает.");
        assertEquals(savedSubTask1, savedSubTask2, "Задачи не совпадают.");
    }
}