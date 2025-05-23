package test;

import taskservice.InMemoryTaskManager;
import taskservice.Managers;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    void correctRemovedAllTasks() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        removeAllTasksTest(inMemoryTaskManager);
        assertTrue(inMemoryTaskManager.getTaskHashMap().isEmpty(), "Список задач не очищен");
        assertTrue(inMemoryTaskManager.getEpicHashMap().isEmpty(), "Список эпиков не очищен");
        assertTrue(inMemoryTaskManager.getSubTaskHashMap().isEmpty(), "Список подзадач не очищен");
    }

    @Test
    public void correctRemoveTaskById() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        removeTaskByIdTest(inMemoryTaskManager);
    }

    @Test
    public void correctUpdateTask() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        updateTaskTest(inMemoryTaskManager);
    }

    @Test
    public void correctCreateAndGetByIdEpic() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        createAndGetByIdEpicTest(inMemoryTaskManager);
    }

    @Test
    public void correctCreateAndGetByIdTask() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        createAndGetByIdTaskTest(inMemoryTaskManager);
    }

    @Test
    public void correctCreateAndGetByIdSubTask() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        createAndGetByIdSubTaskTest(inMemoryTaskManager);
    }

    @Test
    public void correctEpicUpdateStatus() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        epicUpdateStatusTest(inMemoryTaskManager);
    }

    @Test
    public void correctSubTaskSetOwner() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        subTaskSetOwnerTest(inMemoryTaskManager);
    }

    @Test
    public void correctDisallowTaskByOverlappingTest() {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        disallowTaskByOverlappingTest(inMemoryTaskManager);
    }
}