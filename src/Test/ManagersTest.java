package Test;

import static org.junit.jupiter.api.Assertions.*;

import TaskService.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    public void ManagersReturnNotNullObjects() {

        Assertions.assertNotNull(Managers.getDefault(), "Объект не найден.");
        assertEquals("TaskService.InMemoryTaskManager", Managers.getDefault().getClass().getName()
                , "Объект другого типа.");
        assertNotNull(Managers.getDefaultHistory(), "Объект не найден.");
        assertEquals("TaskService.InMemoryHistoryManager", Managers.getDefaultHistory().getClass().getName()
                , "Объект другого типа.");

    }
}