package test;

import TaskService.InMemoryTaskManager;
import TaskService.Managers;
import TaskService.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest {

    @Test
    void correctRemovedAllTasks() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Epic epic = new Epic("EpicName", "EpicDescription");
        int epicId = inMemoryTaskManager.createTask(epic);
        SubTask subTask = new SubTask("SubName", "SubDescription", Status.NEW, epicId);
        inMemoryTaskManager.createTask(new Task("TaskName", "TaskDescription", Status.IN_PROGRESS));
        inMemoryTaskManager.createTask(subTask);
        inMemoryTaskManager.removeAllTasks("Tasks.Task");
        inMemoryTaskManager.removeAllTasks("Tasks.Epic");
        inMemoryTaskManager.removeAllTasks("Tasks.SubTask");

        assertTrue(inMemoryTaskManager.getTaskHashMap().isEmpty(), "Список задач не очищен");
        assertTrue(inMemoryTaskManager.getEpicHashMap().isEmpty(), "Список эпиков не очищен");
        assertTrue(inMemoryTaskManager.getSubTaskHashMap().isEmpty(), "Список подзадач не очищен");
    }

    @Test
    void removeTaskById() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        Epic epic = new Epic("EpicName", "EpicDescription");
        int epicId = inMemoryTaskManager.createTask(epic);
        SubTask subTask = new SubTask("SubName", "SubDescription", Status.NEW, epicId);
        int taskId = inMemoryTaskManager.createTask(new Task("TaskName", "TaskDescription",
                Status.IN_PROGRESS));
        inMemoryTaskManager.createTask(subTask);
        inMemoryTaskManager.removeTaskById(taskId);

        assertNull(inMemoryTaskManager.getTaskById(taskId));
    }
}