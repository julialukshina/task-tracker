package Managers;

import java.io.File;

public class Managers { //утилитарный класс
    public static File file = new File("src/file.csv");

    public static TaskManager GetDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return new FileBackedTasksManager(file);
    }
}
