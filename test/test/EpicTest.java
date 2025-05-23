package test;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.ExampleTasks.*;
import tasks.Epic;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

class EpicTest {

    @Test
    public void epicsCorrectEqualOverride() {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(4);
        epic1.setId(122);
        epic1.setSubTaskIds(ids);
        Epic epic = new Epic(epic1.getName(), epic1.getDescription());
        epic.setSubTaskIds(epic1.getSubTaskIds());
        epic.setStartTime(epic1.getStartTime());
        epic.setDuration(epic1.getDuration());
        epic.setEndTime(epic1.getEndTime());
        epic.setId(epic1.getId());
        assertEquals(epic1, epic, "Метод equals в эпике реализован некорректно");
    }
}