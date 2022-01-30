import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    private int id;
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

    public ArrayList<Task> getListOfTasks() { // возвращает список задач
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getListOfSubTasks() { // возвращает список подзадач
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getListOfEpics() { // // возвращает список эпиков
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getListOfSubtasksOfEpics(Epic epic) { // // возвращает список сабтасков эпика
        return new ArrayList<>(epic.getSubTasksOfEpic());
    }

    public void deleteAllTasks(HashMap map) { // метод удаляет все задачи (для всех видов)
        if (map == subtasks) {
            ArrayList<Epic> epics = new ArrayList<>();
            for (SubTask subTask : subtasks.values()) {
                epics.add(subTask.getEpic());
            }
            map.clear();
            for (Epic epic : epics) {
                epic.deleteSubtasks();
                epic.checkStatus();
            }
        } else {
            map.clear();
        }
    }

    public Object getTaskByID(int id) { // метод возвращает объект всех видов задач по его id
        Task result = null;
        if (tasks.containsKey(id)) {
            result = tasks.get(id);
        } else if (subtasks.containsKey(id)) {
            result = subtasks.get(id);
        } else if (epics.containsKey(id)) {
            result = epics.get(id);
        } else {
            System.out.print("Такой id отсутствует. ");
        }
        return result;
    }

    public void updateTask(Task task, int id) { //метод обновляет задачи всех видов
        if (task instanceof SubTask) {
            subtasks.put(id, (SubTask) task);
            ((SubTask) task).getEpic().checkStatus();
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

    public void deleteTaskById(int id) { //метод удаляет задачу по id
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            subtasks.remove(id);
            epic.checkStatus();
        } else if (epics.containsKey(id)) {
            epics.remove(id);
        } else {
            System.out.print("Нельзя удалить то, чего нет");
        }
    }
}
