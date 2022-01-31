import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    private Integer id = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNewId() { //выделила в отдельный метод по рекомендации
        id++;
        return id;
    }

    public void putTask(Task task) { // записывает соответсвующую задачу в нужную мапу
        if (task instanceof SubTask) {
            task.setId(getNewId());
            subtasks.put(task.getId(), (SubTask) task);
            ((SubTask) task).getEpic().addSubTask((SubTask) task); //сабтаск записывается в список эпика
            ((SubTask) task).getEpic().checkStatus(); // дополнила проверкой статуса эпика
        } else if (task instanceof Epic) {
            task.setId(getNewId());
            epics.put(task.getId(), (Epic) task);
        } else {
            task.setId(getNewId());
            tasks.put(task.getId(), task);
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

    public ArrayList<SubTask> getListOfSubtasksOfEpics(Integer id) { //возвращает список сабтасков эпика, исправила параметр метода
        return new ArrayList<>(epics.get(id).getSubTasksOfEpic());
    }

    public void deleteAllTasks() { // разделила на три отдельных метода, каждый из которых удаляет свой тип задач
        tasks.clear();
    } //предусмотрела удаление всех задач для каждого типа задач

    public void deleteAllSubtasks() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (SubTask subTask : subtasks.values()) {
            epics.add(subTask.getEpic());
        }
        subtasks.clear();
        for (Epic epic : epics) {
            epic.deleteSubtasks();
            epic.checkStatus();
        }
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public Object getTaskByID(Integer id) { // метод возвращает объект всех видов задач по его id
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

    public void updateTask(Task task, Integer id) { //метод обновляет задачи всех видов
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

    public void deleteTaskById(Integer id) { //метод удаляет задачу по id
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
