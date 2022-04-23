package Managers;

import Servers.KVServer;

import java.io.File;
import java.io.IOException;

public class Managers { //утилитарный класс
    public static File file = new File("src/file.csv");

    public static KVServer KVserver;

    static {
        try {
            KVserver = new KVServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TaskManager GetDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked() {
        return new FileBackedTasksManager(file);
    }

    public static HttpTaskManager getDefaultHttpTask() {
        try{
            KVserver.start();
        }catch (IllegalStateException e){
            System.out.println("Сервер уже запущен");
        }
        return new HttpTaskManager("http://localhost:8078");
    }
}
