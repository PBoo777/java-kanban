package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import taskservice.FileBackedTaskManager;
import taskservice.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class FileBackedTaskManagerTest {

    @Test
    void correctSavedAndLoadEmptyFile() {
        File file = new File("TestSave.txt");
        File newFile = new File("ControlTestSave.txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.getName());
        fileBackedTaskManager.save(file.getName());
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
        newFileBackedTaskManager.save(newFile.getName());
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
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.getName());
        Task task = new Task("Учёба", "Пройти тему в Практикуме", Status.IN_PROGRESS);
        Epic epic = new Epic("Дела по хозяйству", "Ремонт");
        int epicId = fileBackedTaskManager.createTask(epic); // задача#1
        SubTask subTask1 = new SubTask("Обои", "Найти и купить обои", Status.DONE, epicId);
        SubTask subTask2 = new SubTask("Потолок", "Покрасить потолок", Status.DONE, epicId);
        int taskId = fileBackedTaskManager.createTask(task);// задача#2
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
        newFileBackedTaskManager.save(newFile.getName());
        String newContent;
        try {
            newContent  = Files.readString(newFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertTrue(newFile.exists(), "Файл нового экземпляра менеджера не был создан");
        assertEquals(content, newContent, "Контрольный файл отличается от исходного");

        epic = (Epic) fileBackedTaskManager.getTaskById(epicId);
        task = fileBackedTaskManager.getTaskById(taskId);
        subTask1 = (SubTask) fileBackedTaskManager.getTaskById(subTask1Id);
        subTask2 = (SubTask) fileBackedTaskManager.getTaskById(subTask2Id);
        Epic newEpic = (Epic) newFileBackedTaskManager.getTaskById(epicId);
        Task newTask = newFileBackedTaskManager.getTaskById(taskId);
        SubTask newSubTask1 = (SubTask) newFileBackedTaskManager.getTaskById(subTask1Id);
        SubTask newSubTask2 = (SubTask) newFileBackedTaskManager.getTaskById(subTask2Id);

        assertEquals(epic, newEpic, "Контрольный эпик отличается от исходного");
        assertEquals(task, newTask, "Контрольный таск отличается от исходного");
        assertEquals(subTask1, newSubTask1, "Контрольный сабтаск1 отличается от исходного");
        assertEquals(subTask2, newSubTask2, "Контрольный сабтаск2 отличается от исходного");
    }
}