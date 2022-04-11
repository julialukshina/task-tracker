package Managers;

import java.util.ArrayList;
import java.util.List;

import Tasks.*;

public interface TaskManager {

    List<Task> history();

    Integer getNewId();

    void putTask(Task task);

    ArrayList<Task> getListOfTasks();

    ArrayList<SubTask> getListOfSubTasks();

    ArrayList<Epic> getListOfEpics();

    ArrayList<SubTask> getListOfSubtasksOfEpics(Integer id);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    Object getTaskByID(Integer id);

    void updateTask(Task task, Integer id);

    void deleteTaskById(Integer id);

    boolean canSaveTask(Task task);
}

