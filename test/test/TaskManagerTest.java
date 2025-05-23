package test;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import taskservice.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;

import static tasks.ExampleTasks.*;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    void updateTaskTest(T manager) {
        int taskId = manager.createTask(task1);
        int epicId = manager.createTask(epic1);
        int subTaskId = manager.createTask(subTask1);

        manager.updateTask(task2, taskId);
        manager.updateTask(epic2, epicId);
        manager.updateTask(subTask2, subTaskId);

        task2.setId(taskId);
        epic2.setId(epicId);
        subTask2.setId(subTaskId);

        assertEquals(task2, manager.getTaskById(taskId), "Апдейт задачи выполнен некорректно");
        assertEquals(epic2, manager.getTaskById(epicId), "Апдейт эпика выполнен некорректно");
        assertEquals(subTask2, manager.getTaskById(subTaskId), "Апдейт подзадачи выполнен некорректно");
    }

    void removeAllTasksTest(T manager) {
        int epicId = manager.createTask(epic1);
        subTask1.setOwnerId(epicId);
        manager.createTask(task1);
        manager.createTask(subTask1);
        manager.removeAllTasks(TaskTypes.TASK);
        manager.removeAllTasks(TaskTypes.EPIC);
        manager.removeAllTasks(TaskTypes.SUBTASK);
    }

    void removeTaskByIdTest(T manager) {
        int epicId = manager.createTask(epic1);
        subTask1.setOwnerId(epicId);
        int taskId = manager.createTask(task1);
        manager.createTask(subTask1);
        manager.removeTaskById(taskId);

        assertEquals(defaultTask, manager.getTaskById(taskId), "Не является дефолтным объектом задачи");
    }

    void createAndGetByIdEpicTest(T manager) {
        int epicId = manager.createTask(epic1);
        Epic savedEpic1 = (Epic) manager.getTaskById(epicId);
        Epic savedEpic2 = (Epic) manager.getTaskById(epicId);

        assertNotNull(savedEpic1, "Задача не найдена.");
        assertEquals(savedEpic1.getId(), savedEpic2.getId(), "ID задач не совпадают.");
        assertEquals(savedEpic1.getSubTaskIds(), savedEpic2.getSubTaskIds(), "Списки подзадач не совпадают");
        assertEquals(savedEpic1, savedEpic2, "Задачи не совпадают.");
    }

    void createAndGetByIdTaskTest(T manager) {
        int taskId = manager.createTask(task1);
        Task savedTask1 = manager.getTaskById(taskId);
        Task savedTask2 = manager.getTaskById(taskId);

        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(savedTask1.getId(), savedTask2.getId(), "ID задач не совпадают.");
        assertEquals(savedTask1, savedTask2, "Задачи не совпадают.");
    }

    void createAndGetByIdSubTaskTest(T manager) {
        int epicId = manager.createTask(epic1);
        subTask1.setOwnerId(epicId);
        int subTaskId = manager.createTask(subTask1);
        SubTask savedSubTask1 = (SubTask) manager.getTaskById(subTaskId);
        SubTask savedSubTask2 = (SubTask) manager.getTaskById(subTaskId);

        assertNotNull(savedSubTask1, "Задача не найдена.");
        assertEquals(savedSubTask1.getId(), savedSubTask2.getId(), "ID задач не совпадают.");
        assertEquals(savedSubTask1.getOwnerId(), savedSubTask2.getOwnerId(), "Эпик не совпадает.");
        assertEquals(savedSubTask1, savedSubTask2, "Задачи не совпадают.");
    }

    private void createSubTasksForEpic(Status status, int ownerId, LocalDateTime start, T manager) {
        Duration dur = Duration.ofDays(1);
        Duration durPlus = Duration.ofHours(2);
        LocalDateTime moment = start;
        for (int i = 1; i < 10; i++) {
            moment = moment.plus(dur);
            SubTask subTask = new SubTask("Some name", "Some description", status, ownerId,
                    moment, durPlus);
            manager.createTask(subTask);
        }
    }

    void epicUpdateStatusTest(T manager) {
        LocalDateTime start = LocalDateTime.of(2010, Month.MAY, 15, 0, 0);
        int epicId = manager.createTask(epic1);
        createSubTasksForEpic(Status.NEW, epicId, start, manager);

        assertEquals(Status.NEW, manager.getTaskById(epicId).getStatus(),
                "Статус эпика в случае всех подзадач 'NEW' вычисляется некорректно");

        int subTaskId = ((Epic) manager.getTaskById(epicId)).getSubTaskIds().getFirst();
        subTask1.setStatus(Status.DONE);
        subTask1.setOwnerId(epicId);
        manager.updateTask(subTask1, subTaskId);

        assertEquals(Status.IN_PROGRESS, manager.getTaskById(epicId).getStatus(),
                "Статус эпика в случае изменения статуса подзадач вычисляется некорректно1");

        manager.clear();
        epicId = manager.createTask(epic1);
        createSubTasksForEpic(Status.DONE, epicId, start, manager);
        assertEquals(Status.DONE, manager.getTaskById(epicId).getStatus(),
                "Статус эпика в случае всех подзадач 'DONE' вычисляется некорректно");
        manager.printAllTasks(TaskTypes.SUBTASK);
        manager.printAllTasks(TaskTypes.EPIC);


        subTaskId = ((Epic) manager.getTaskById(epicId)).getSubTaskIds().getFirst();
        subTask1.setStatus(Status.NEW);
        subTask1.setOwnerId(epicId);
        manager.updateTask(subTask1, subTaskId);
        manager.printAllTasks(TaskTypes.SUBTASK);
        manager.printAllTasks(TaskTypes.EPIC);

        assertEquals(Status.IN_PROGRESS, manager.getTaskById(epicId).getStatus(),
                "Статус эпика в случае изменения статуса подзадач вычисляется некорректно2");

        manager.clear();
        epicId = manager.createTask(epic1);
        createSubTasksForEpic(Status.IN_PROGRESS, epicId, start, manager);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(epicId).getStatus(),
                "Статус эпика в случае всех подзадач 'IN_PROGRESS' вычисляется некорректно");

        manager.clear();
        epicId = manager.createTask(epic1);
        createSubTasksForEpic(Status.DONE, epicId, start, manager);
        createSubTasksForEpic(Status.NEW, epicId, start.plus(Period.ofYears(2)), manager);
        assertEquals(Status.IN_PROGRESS, manager.getTaskById(epicId).getStatus(),
                "Статус эпика в случае всех подзадач 'DONE' и 'NEW' вычисляется некорректно");
    }

    void subTaskSetOwnerTest(T manager) {
        LocalDateTime start = LocalDateTime.of(2010, Month.MAY, 15, 0, 0);
        int epicId = manager.createTask(epic1);
        createSubTasksForEpic(Status.NEW, epicId, start, manager);
        boolean noneMatch = ((Epic) manager.getTaskById(epicId)).getSubTaskIds().stream().noneMatch(subTaskId ->
                ((SubTask) manager.getTaskById(subTaskId)).getOwnerId() != epicId);
        assertTrue(noneMatch, "Не все подзадачи связаны с эпиком");
    }

    void disallowTaskByOverlappingTest(T manager) {
        LocalDateTime start = LocalDateTime.of(2010, Month.MAY, 15, 0, 0);
        Duration fiveDay = Duration.ofDays(5);
        Duration twoDay = Duration.ofDays(2);
        Duration oneDay = Duration.ofDays(1);
        Duration halfDay = Duration.ofHours(12);
        Task task1 = new Task("Some name", "Some description", Status.DONE, start, oneDay);
        Task task2 = new Task("Some name", "Some description", Status.NEW, start.plus(twoDay), oneDay);
        Task task3 = new Task("Some name", "Some description", Status.IN_PROGRESS, start.plus(fiveDay),
                oneDay);
        Task incorrectTimeTask = new Task("Some name", "Some description", Status.DONE,
                task1.getStartTime().plus(halfDay), oneDay);
        int id1 = manager.createTask(task1);
        int id2 = manager.createTask(task2);
        int id3 = manager.createTask(task3);
        int idX = manager.createTask(incorrectTimeTask);
        assertTrue((id1 * id2 * id3 > 0), "Задачи с корректными временными интервалами не проходят проверку");
        assertTrue((idX < 0), "Задача с некорректным временным интервалом проходит проверку");
    }
}