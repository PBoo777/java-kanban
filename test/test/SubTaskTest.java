package test;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.ExampleTasks.*;
import tasks.SubTask;
import org.junit.jupiter.api.Test;

class SubTaskTest {

    @Test
    public void subTasksCorrectEqualOverride() {
        subTask1.setId(122);
        subTask1.setOwnerId(332);
        SubTask subTask = new SubTask(subTask1.getName(), subTask1.getDescription(), subTask1.getStatus(),
                subTask1.getOwnerId(), subTask1.getStartTime(), subTask1.getDuration());
        subTask.setId(subTask1.getId());
        assertEquals(subTask1, subTask, "Метод equals в подзадаче реализован некорректно");
    }
}