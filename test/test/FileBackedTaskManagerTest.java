package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import taskservice.FileBackedTaskManager;
import taskservice.InMemoryTaskManager;
import static tasks.ExampleTasks.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class FileBackedTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    void correctRemovedAllTasks() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        removeAllTasksTest(fbtm);
        assertTrue(fbtm.getTaskHashMap().isEmpty(), "Список задач не очищен");
        assertTrue(fbtm.getEpicHashMap().isEmpty(), "Список эпиков не очищен");
        assertTrue(fbtm.getSubTaskHashMap().isEmpty(), "Список подзадач не очищен");
    }

    @Test
    public void correctRemoveTaskById() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        removeTaskByIdTest(fbtm);
    }

    @Test
    public void correctUpdateTask() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        updateTaskTest(fbtm);
    }

    @Test
    public void correctCreateAndGetByIdEpic() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        createAndGetByIdEpicTest(fbtm);
    }

    @Test
    public void correctCreateAndGetByIdTask() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        createAndGetByIdTaskTest(fbtm);
    }

    @Test
    public void correctCreateAndGetByIdSubTask() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        createAndGetByIdSubTaskTest(fbtm);
    }

    @Test
    public void correctEpicUpdateStatus() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        epicUpdateStatusTest(fbtm);
    }

    @Test
    public void correctSubTaskSetOwner() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        subTaskSetOwnerTest(fbtm);
    }

    @Test
    public void correctDisallowTaskByOverlappingTest() {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(new File("TestSave.txt"));
        disallowTaskByOverlappingTest(fbtm);
    }

    @Test
    void correctSavedAndLoadEmptyFile() {
        File file = new File("TestSave.txt");
        File newFile = new File("ControlTestSave.txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.save();
        String content;
        try {
            content  = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] lines = content.split("\n");
        assertTrue(file.exists(), "Файл не был создан");
        assertEquals(4, lines.length, "Число строк в файле не соответствует начальному состоянию");
        FileBackedTaskManager newFileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        newFileBackedTaskManager.setFile(newFile);
        newFileBackedTaskManager.save();
        String newContent;
        try {
            newContent  = Files.readString(newFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(newFile.exists(), "Файл нового экземпляра менеджера не был создан");
        assertEquals(content, newContent, "Контрольный файл отличается от исходного");
    }

    @Test
    void correctSavedAndLoadNotEmptyFile() {
        File file = new File("TestSave.txt");
        File newFile = new File("ControlTestSave.txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        int epicId = fileBackedTaskManager.createTask(epic1); // задача#1
        int taskId = fileBackedTaskManager.createTask(task1);// задача#2
        subTask1.setOwnerId(epicId);
        subTask2.setOwnerId(epicId);
        int subTask1Id = fileBackedTaskManager.createTask(subTask1);// задача#3
        int subTask2Id = fileBackedTaskManager.createTask(subTask2);// задача#4
        String content;
        try {
            content  = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] lines = content.split("\n");

        assertTrue(file.exists(), "Файл не был создан");
        assertEquals(8, lines.length, "Число строк в файле не соответствует ожидаемому");

        FileBackedTaskManager newFileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        newFileBackedTaskManager.setFile(newFile);
        newFileBackedTaskManager.save();
        String newContent;
        try {
            newContent  = Files.readString(newFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertTrue(newFile.exists(), "Файл нового экземпляра менеджера не был создан");
        assertEquals(content, newContent, "Контрольный файл отличается от исходного");

        Epic epic = (Epic) fileBackedTaskManager.getTaskById(epicId);
        Task task = fileBackedTaskManager.getTaskById(taskId);
        SubTask originSubTask1 = (SubTask) fileBackedTaskManager.getTaskById(subTask1Id);
        SubTask originSubTask2 = (SubTask) fileBackedTaskManager.getTaskById(subTask2Id);
        Epic newEpic = (Epic) newFileBackedTaskManager.getTaskById(epicId);
        Task newTask = newFileBackedTaskManager.getTaskById(taskId);
        SubTask newSubTask1 = (SubTask) newFileBackedTaskManager.getTaskById(subTask1Id);
        SubTask newSubTask2 = (SubTask) newFileBackedTaskManager.getTaskById(subTask2Id);

        assertEquals(epic, newEpic, "Контрольный эпик отличается от исходного");
        assertEquals(task, newTask, "Контрольный таск отличается от исходного");
        assertEquals(originSubTask1, newSubTask1, "Контрольный сабтаск1 отличается от исходного");
        assertEquals(originSubTask2, newSubTask2, "Контрольный сабтаск2 отличается от исходного");
    }

    @Test
    public void testException() {
        assertThrows(RuntimeException.class, () -> {
            FileBackedTaskManager.loadFromFile(new File("AnySave.txt"));
        }, "Загрузка из пустого файла должна приводить к исключению");
    }
}