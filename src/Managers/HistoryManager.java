package Managers;

import Tasks.Task;

import java.util.ArrayList;

public interface HistoryManager extends TaskManager {

    ArrayList<Task> getHistory();

    void add(Task task);

    void deleteTaskFromHistory(Task task);
}
