package test;

import TaskService.Managers;
import TaskService.Status;
import TaskService.TaskManager;
import TaskService.InMemoryTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {


    @Test
    public void tasksCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Task originTask = new Task("Name", "Description", Status.NEW);
        Task newTask = new Task("AnotherName", "AnotherDescription", Status.IN_PROGRESS);
        int taskId = inMemoryTaskManager.createTask(originTask);
        Task task = inMemoryTaskManager.getTaskById(taskId);
        inMemoryTaskManager.updateTask(newTask, taskId);
        assertEquals(task, inMemoryTaskManager.getHistory().getLast(),
                "Предыдущая версия задачи не сохраняется.");
    }

    @Test
    public void subTasksCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Epic epic1 = new Epic("E1Name", "E1Description");
        Epic epic2 = new Epic("E2Name", "E2Description");
        int epic1Id = inMemoryTaskManager.createTask(epic1);
        int epic2Id = inMemoryTaskManager.createTask(epic2);
        SubTask originSubTask = new SubTask("Name", "Description", Status.NEW, epic1Id);
        SubTask newSubTask = new SubTask("AnotherName", "AnotherDescription",
                Status.IN_PROGRESS, epic2Id);
        int subTaskId = inMemoryTaskManager.createTask(originSubTask);
        SubTask subTask = (SubTask) inMemoryTaskManager.getTaskById(subTaskId);
        inMemoryTaskManager.updateTask(newSubTask, subTaskId);
        SubTask historyTask = (SubTask) inMemoryTaskManager.getHistory().getLast();

        assertEquals(subTask.getOwnerId(), historyTask.getOwnerId(), "Эпики не совпадают");
        assertEquals(subTask, historyTask,"Предыдущая версия задачи не сохраняется.");
    }

    @Test
    public void epicCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        Epic originEpic = new Epic("Name", "Description");
        Epic newEpic = new Epic("AnotherName", "AnotherDescription");
        int epicId = inMemoryTaskManager.createTask(originEpic);
        int newEpicId = inMemoryTaskManager.createTask(newEpic);
        SubTask subTask1 = new SubTask("SName1", "SDescription1", Status.IN_PROGRESS, epicId);
        SubTask subTask2 = new SubTask("SName2", "SDescription2", Status.DONE, newEpicId);
        inMemoryTaskManager.createTask(subTask1);
        inMemoryTaskManager.createTask(subTask2);
        Epic epic = (Epic) inMemoryTaskManager.getTaskById(epicId);
        inMemoryTaskManager.updateTask(newEpic, epicId);
        Epic historyEpic = (Epic) inMemoryTaskManager.getHistory().getLast();

        assertEquals(epic.getSubTaskIds(), historyEpic.getSubTaskIds(), "Подзадачи не совпадают");
        assertEquals(epic, historyEpic, "Предыдущая версия задачи не сохраняется.");
    }

    @Test
    public void  historyExcludeDuplicationTasks() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        int taskId;
        int originTaskId = inMemoryTaskManager.createTask(new Task("OriginTaskName",
                "OriginTaskDescription", Status.DONE));
        inMemoryTaskManager.getTaskById(originTaskId);  // Просмотр 1, контрольный
        Epic epic = new Epic("Name", "Description");
        int epicId = inMemoryTaskManager.createTask(epic);
        inMemoryTaskManager.getTaskById(epicId);  // Просмотр 2
        SubTask subTask1 = new SubTask("SName1", "SDescription1", Status.IN_PROGRESS, epicId);
        taskId = inMemoryTaskManager.createTask(subTask1);
        inMemoryTaskManager.getTaskById(taskId);  // Просмотр 3
        taskId = inMemoryTaskManager.createTask(new Task("TaskName", "TaskDescription", Status.NEW));
        inMemoryTaskManager.getTaskById(taskId);  // Просмотр 4
        inMemoryTaskManager.getTaskById(taskId);  // Просмотр 4(второй раз)
        inMemoryTaskManager.getTaskById(taskId);  // Просмотр 4(третий раз)

        assertEquals(4, inMemoryTaskManager.getHistory().size(),
                 "Количество сохранённых задач в истории отлично от 4");
        assertNotEquals(inMemoryTaskManager.getHistory().get(inMemoryTaskManager.getHistory().size() - 2),
                inMemoryTaskManager.getHistory().getLast(), "Последняя задача дублирует предпоследнюю");

        inMemoryTaskManager.getTaskById(originTaskId);  // Просмотр 1(второй раз)

        assertEquals(4, inMemoryTaskManager.getHistory().size(),
                 "Количество сохранённых задач в истории отлично от 4");
        assertNotEquals(inMemoryTaskManager.getHistory().getFirst(), inMemoryTaskManager.getHistory().getLast(),
                "Последняя задача дублирует первую");
    }

    @Test
    public void taskCorrectRemovedFromHistory() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        int taskId;
        int originTaskId = inMemoryTaskManager.createTask(new Task("OriginTaskName",
                "OriginTaskDescription", Status.DONE));
        Task originTask = inMemoryTaskManager.getTaskById(originTaskId);  // Просмотр 1, контрольный
        Epic epic = new Epic("Name", "Description");
        int epicId = inMemoryTaskManager.createTask(epic);
        inMemoryTaskManager.getTaskById(epicId);  // Просмотр 2
        SubTask subTask = new SubTask("SName1", "SDescription1", Status.IN_PROGRESS, epicId);
        taskId = inMemoryTaskManager.createTask(subTask);
        inMemoryTaskManager.getTaskById(taskId);  // Просмотр 3
        taskId = inMemoryTaskManager.createTask(new Task("TaskName", "TaskDescription", Status.NEW));
        inMemoryTaskManager.getTaskById(taskId);  // Просмотр 4
        inMemoryTaskManager.getHistoryManager().remove(originTaskId); // Удаление просмотра 1

        assertEquals(3, inMemoryTaskManager.getHistory().size(),
                 "Количество сохранённых задач в истории отлично от 3");
        assertNotEquals(inMemoryTaskManager.getHistory().getFirst(), originTask,
                "Первая задача не удалена из истории");
    }
}