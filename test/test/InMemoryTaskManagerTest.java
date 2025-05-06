package test;

import taskservice.InMemoryTaskManager;
import taskservice.Managers;
import taskservice.Status;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import org.junit.jupiter.api.Test;
import taskservice.TaskTypes;

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
        inMemoryTaskManager.removeAllTasks(TaskTypes.TASK);
        inMemoryTaskManager.removeAllTasks(TaskTypes.EPIC);
        inMemoryTaskManager.removeAllTasks(TaskTypes.SUBTASK);

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
        Task task = new Task("Default", "Default", Status.NEW);

        assertEquals(task, inMemoryTaskManager.getTaskById(taskId), "Не является дефолтным объектом задачи");
    }
}