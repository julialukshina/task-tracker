import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    private int id;
    Task task;
    SubTask subtask;
    Epic epic;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void putTask(Task task) { // записывает соответсвующую задачу в нужную мапу
        if (task.getId() == 0) {
            int x = getId();
            setId(x + 1);
            if (task instanceof SubTask) {
                task.setId(getId());
                subtasks.put(task.getId(), (SubTask) task);
            } else if (task instanceof Epic) {
                task.setId(getId());
                epics.put(task.getId(), (Epic) task);
            } else {
                task.setId(getId());
                tasks.put(task.getId(), task);
            }
        } else {
            return;
        }
    }

    public ArrayList getListOfTasks() { // возвращает список задач
        ArrayList<Task> listOfTasks = new ArrayList(tasks.values());
        return listOfTasks;
    }

    public ArrayList getListOfSubTasks() { // возвращает список подзадач
        ArrayList<Task> listOfSubTasks = new ArrayList(subtasks.values());
        return listOfSubTasks;
    }

    public ArrayList getListOfEpics() { // // возвращает список эпиков
        ArrayList<Task> listOfEpics = new ArrayList(epics.values());
        return listOfEpics;
    }

    public void deleteAllTasks(HashMap map) { // метод удаляет все задачи
        map.clear();
    }

    public Object getTaskByID(int id) { // метод возвращает объект по его id
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.print("Такой id отсутствует. ");
        }
        return task;
    }

    public void updateTask(Task task, int id) {
        if (task instanceof SubTask) {
            subtasks.put(id, (SubTask) task);
            epic.checkStatus();
        } else if (task instanceof Epic) {
            epics.put(id, (Epic) task);
        } else {
            if (tasks.get(id) != null) {
                tasks.put(id, task);
            } else {
                System.out.println("Такой задачи нет в списках");
            }
        }
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            epic.checkStatus();
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        } else {
            System.out.print("Нельзя удалить то, чего нет");
        }
    }

}
