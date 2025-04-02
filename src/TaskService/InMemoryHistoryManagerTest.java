package TaskService;

import static org.junit.jupiter.api.Assertions.*;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {


    @Test
    public void TasksCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task task = new Task("Name", "Description", Status.NEW);
        Task newTask = new Task("AnotherName", "AnotherDescription", Status.IN_PROGRESS);
        int taskId = inMemoryTaskManager.createTask(task);

        inMemoryTaskManager.getTaskById(taskId);
        inMemoryTaskManager.updateTask(newTask, taskId);
        assertEquals(task, inMemoryTaskManager.getHistory().getLast()
                ,"Предыдущая версия задачи не сохраняется.");
    }

    @Test
    public void subTasksCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Epic epic1 = new Epic("E1Name", "E1Description");
        Epic epic2 = new Epic("E2Name", "E2Description");
        SubTask subTask = new SubTask("Name", "Description", Status.NEW, epic1);
        SubTask newSubTask = new SubTask("AnotherName", "AnotherDescription", Status.IN_PROGRESS, epic2);
        int subTaskId = inMemoryTaskManager.createTask(subTask);

        inMemoryTaskManager.getTaskById(subTaskId);
        inMemoryTaskManager.updateTask(newSubTask, subTaskId);

        SubTask historyTask = (SubTask) inMemoryTaskManager.getHistory().getLast();

        assertEquals(subTask.getOwner(), historyTask.getOwner(), "Эпики не совпадают");
        assertEquals(subTask, historyTask,"Предыдущая версия задачи не сохраняется.");
    }

    @Test
    public void epicCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Epic epic = new Epic("Name", "Description");
        Epic newEpic = new Epic("AnotherName", "AnotherDescription");
        SubTask subTask1 = new SubTask("SName1", "SDescription1", Status.IN_PROGRESS, epic);
        SubTask subTask2 = new SubTask("SName2", "SDescription2", Status.DONE, newEpic);
        inMemoryTaskManager.createTask(subTask1);
        inMemoryTaskManager.createTask(subTask2);
        int epicId = inMemoryTaskManager.createTask(epic);

        inMemoryTaskManager.getTaskById(epicId);
        inMemoryTaskManager.updateTask(newEpic, epicId);

        Epic historyEpic = (Epic) inMemoryTaskManager.getHistory().getLast();

        assertEquals(epic.getSubTasks(), historyEpic.getSubTasks(), "Подзадачи не совпадают");
        assertEquals(epic, historyEpic, "Предыдущая версия задачи не сохраняется.");
    }
}