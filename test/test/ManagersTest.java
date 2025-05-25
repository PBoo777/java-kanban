package test;

import static org.junit.jupiter.api.Assertions.*;
import taskservice.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    public void managersReturnNotNullObjects() {
        Assertions.assertNotNull(Managers.getDefault(), "Объект не найден.");
        assertEquals("taskservice.InMemoryTaskManager", Managers.getDefault().getClass().getName(),
                 "Объект другого типа.");
        assertNotNull(Managers.getDefaultHistory(), "Объект не найден.");
        assertEquals("taskservice.InMemoryHistoryManager", Managers.getDefaultHistory().getClass().getName(),
                 "Объект другого типа.");
    }
}