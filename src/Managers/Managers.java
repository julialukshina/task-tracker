package Managers;

public class Managers { //утилитарный класс
    public static TaskManager GetDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryTaskManager();
    }


}
