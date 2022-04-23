package Tests;

import Managers.FileBackedTasksManager;
import Managers.Managers;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File fileWithHistory = new File("src/testWithHistory.csv");
    File fileWithoutHistory = new File("src/testWithHistory.csv");
    FileBackedTasksManager managerWithHistory;
    FileBackedTasksManager managerWithoutHistory;

    public FileBackedTasksManagerTest() {
        super((FileBackedTasksManager) Managers.getDefaultFileBacked());
    }


    @Test //тест метода loadFromFile()
    void loadFromFile() {
        File file = new File("src/test.csv");
        managerWithHistory = FileBackedTasksManager.loadFromFile(file);
        assertEquals(3, managerWithHistory.getListOfTasks().size());
        assertEquals(3, managerWithHistory.getListOfSubTasks().size());
        assertEquals(2, managerWithHistory.getListOfEpics().size());
        assertEquals(6, managerWithHistory.history().size());

        File fileWithoutHistory = new File("src/test1.csv");
        managerWithoutHistory = FileBackedTasksManager.loadFromFile(fileWithoutHistory);
        assertEquals(3, managerWithoutHistory.getListOfTasks().size());
        assertEquals(3, managerWithoutHistory.getListOfSubTasks().size());
        assertEquals(2, managerWithoutHistory.getListOfEpics().size());
        assertTrue(managerWithoutHistory.history().isEmpty());

        File emptyFile = new File("src/testEmpty.csv");
        FileBackedTasksManager emptyManager = FileBackedTasksManager.loadFromFile(emptyFile);
        assertTrue(emptyManager.getListOfTasks().isEmpty());
        assertTrue(emptyManager.getListOfSubTasks().isEmpty());
        assertTrue(emptyManager.getListOfEpics().isEmpty());
        assertTrue(emptyManager.history().isEmpty());
    }

    @Test //тест метода save()
    void save() {
        File file = new File("src/file.csv");
        FileBackedTasksManager emptyManager = new FileBackedTasksManager(file);
        managerWithHistory = new FileBackedTasksManager(fileWithHistory);
        managerWithoutHistory = new FileBackedTasksManager(fileWithoutHistory);
        emptyManager.save();
        File emptyFile = new File("src/testEmpty.csv");
        compare(emptyFile, file);


        Task task1 = new Task("Поход в кино", "Сибирский цирюльник");
        managerWithHistory.putTask(task1);
        Task task2 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        managerWithHistory.putTask(task2);
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        managerWithHistory.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 3);
        managerWithHistory.putTask(subTask1);
        Task subTask2 = new SubTask("Бригада", "Не позднее апреля", 3);
        managerWithHistory.putTask(subTask2);
        Task subTask3 = new SubTask("Согласование проекта", "Не позднее конца марта", 3);
        managerWithHistory.putTask(subTask3);
        Task epic2 = new Epic("Поход к врачу", "до 15 февраля");
        managerWithHistory.putTask(epic2);
        managerWithHistory.getTaskByID(1); //вызываем задачи через getTaskByID, чтобы они отобразились в истории задач
        managerWithHistory.getTaskByID(3);
        managerWithHistory.getTaskByID(6);
        managerWithHistory.getTaskByID(7);
        managerWithHistory.getTaskByID(7);
        managerWithHistory.getTaskByID(1);
        managerWithHistory.getTaskByID(1);
        managerWithHistory.getTaskByID(2);
        managerWithHistory.getTaskByID(4);
        managerWithHistory.getTaskByID(1);
        managerWithHistory.getTaskByID(5);
        managerWithHistory.deleteTaskById(1);
        LocalDateTime now = LocalDateTime.parse("2022-04-10T17:50");
        Duration duration = Duration.ofMinutes(30);
        Task taskz = new Task("Купить подарок брату", "Скрин подарка в ВК");
        managerWithHistory.putTask(taskz);
        Task tasky = new Task("Поход к врачу", "до 15 февраля", duration, now);
        managerWithHistory.putTask(tasky);
        File file1 = new File("src/test.csv");
        compare(file1, fileWithHistory);

        managerWithoutHistory.putTask(task1);
        managerWithoutHistory.putTask(task2);
        managerWithoutHistory.putTask(epic1);
        managerWithoutHistory.putTask(subTask1);
        managerWithoutHistory.putTask(subTask2);
        managerWithoutHistory.putTask(subTask3);
        managerWithoutHistory.putTask(epic2);
        managerWithoutHistory.deleteTaskById(1);
        managerWithoutHistory.putTask(taskz);
        managerWithoutHistory.putTask(tasky);
        File file2 = new File("src/test1.csv");
        compare(file2, fileWithoutHistory);
    }

    void compare(File file1, File file2) { //дополнительный метод, сравнивающий файлы для проверки правильности работы метода save()
        ArrayList<String> fromFile1 = new ArrayList<>();
        ArrayList<String> fromFile2 = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file1);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                fromFile1.add(bufferedReader.readLine());
            }
            FileReader fileReader1 = new FileReader(file2);
            BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
            bufferedReader1.readLine();
            while (bufferedReader1.ready()) {
                fromFile2.add(bufferedReader1.readLine());
            }
            for (int i = 0; i < fromFile1.size(); i++) {
                assertEquals(fromFile1.get(i), fromFile2.get(i));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не обнаружен");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}