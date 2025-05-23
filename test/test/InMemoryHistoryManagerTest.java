package test;

import taskservice.Managers;
import taskservice.TaskManager;
import taskservice.InMemoryTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import static tasks.ExampleTasks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {


    @Test
    public void tasksCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        int taskId = inMemoryTaskManager.createTask(task1);
        Task task = inMemoryTaskManager.getTaskById(taskId);
        inMemoryTaskManager.updateTask(task2, taskId);
        assertEquals(task, inMemoryTaskManager.getHistory().getLast(),
                "Предыдущая версия задачи не сохраняется.");
    }

    @Test
    public void subTasksCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        int epic1Id = inMemoryTaskManager.createTask(epic1);
        int epic2Id = inMemoryTaskManager.createTask(epic2);
        subTask1.setOwnerId(epic1Id);
        subTask2.setOwnerId(epic2Id);
        int subTaskId = inMemoryTaskManager.createTask(subTask1);
        SubTask subTask = (SubTask) inMemoryTaskManager.getTaskById(subTaskId);
        inMemoryTaskManager.updateTask(subTask2, subTaskId);
        SubTask historyTask = (SubTask) inMemoryTaskManager.getHistory().getLast();

        assertEquals(subTask.getOwnerId(), historyTask.getOwnerId(), "Эпики не совпадают");
        assertEquals(subTask, historyTask,"Предыдущая версия задачи не сохраняется.");
    }

    @Test
    public void epicCorrectSavedToHistory() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        int epicId = inMemoryTaskManager.createTask(epic1);
        int newEpicId = inMemoryTaskManager.createTask(epic2);
        subTask1.setOwnerId(epicId);
        subTask2.setOwnerId(newEpicId);
        inMemoryTaskManager.createTask(subTask1);
        inMemoryTaskManager.createTask(subTask2);
        Epic epic = (Epic) inMemoryTaskManager.getTaskById(epicId);
        inMemoryTaskManager.updateTask(epic2, epicId);
        Epic historyEpic = (Epic) inMemoryTaskManager.getHistory().getLast();

        assertEquals(epic.getSubTaskIds(), historyEpic.getSubTaskIds(), "Подзадачи не совпадают");
        assertEquals(epic, historyEpic, "Предыдущая версия задачи не сохраняется.");
    }

    @Test
    public void  historyExcludeDuplicationTasks() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        int taskId;
        int originTaskId = inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.getTaskById(originTaskId);  // Просмотр 1, контрольный
        int epicId = inMemoryTaskManager.createTask(epic1);
        inMemoryTaskManager.getTaskById(epicId);  // Просмотр 2
        subTask1.setOwnerId(epicId);
        taskId = inMemoryTaskManager.createTask(subTask1);
        inMemoryTaskManager.getTaskById(taskId);  // Просмотр 3
        taskId = inMemoryTaskManager.createTask(task2);
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
        int lastTaskId;
        int firstTaskId = inMemoryTaskManager.createTask(task1);
        Task firstTask = inMemoryTaskManager.getTaskById(firstTaskId);  // Просмотр 1
        int middleEpicId = inMemoryTaskManager.createTask(epic1);
        inMemoryTaskManager.getTaskById(middleEpicId);  // Просмотр 2
        subTask1.setOwnerId(middleEpicId);
        int middleSubTaskId = inMemoryTaskManager.createTask(subTask1);
        SubTask middleSubTask = (SubTask) inMemoryTaskManager.getTaskById(middleSubTaskId);  // Просмотр 3
        lastTaskId = inMemoryTaskManager.createTask(task2);
        Task lastTask = inMemoryTaskManager.getTaskById(lastTaskId);  // Просмотр 4
        inMemoryTaskManager.getHistoryManager().remove(firstTaskId); // Удаление просмотра 1

        assertEquals(3, inMemoryTaskManager.getHistory().size(),
                 "Количество сохранённых задач в истории отлично от 3");
        assertNotEquals(inMemoryTaskManager.getHistory().getFirst(), firstTask,
                "Первая задача не удалена из истории");

        inMemoryTaskManager.getHistoryManager().remove(middleSubTaskId); // Удаление просмотра 3

        assertEquals(2, inMemoryTaskManager.getHistory().size(),
                "Количество сохранённых задач в истории отлично от 2");
        assertNotEquals(inMemoryTaskManager.getHistory().get(1), middleSubTask,
                "Третья задача не удалена из истории");

        inMemoryTaskManager.getHistoryManager().remove(lastTaskId); // Удаление просмотра 1

        assertEquals(1, inMemoryTaskManager.getHistory().size(),
                "Количество сохранённых задач в истории отлично от 1");
        assertNotEquals(inMemoryTaskManager.getHistory().getLast(), lastTask,
                "Последняя задача не удалена из истории");
    }
}