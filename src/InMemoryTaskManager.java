import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    List <Task> history = new ArrayList<>();


    private void putTaskInHistory(Task task){ //в целях устранения дублирования кода в отдельный метод вынесла заполнение списка истории задач
        if (history.size()<10){
            history.add(task);
        }else{
            history.remove(0);
            history.add(task);
        }
    }

    private void deleteTaskFromHistory(Task task){ //в целях устранения дублирования кода в отдельный метод вынесла удаление из списка истории задач
               if (history.contains(task)){
                   history.removeIf(task1 -> task1.equals(task));
       }
    }

    @Override
    public ArrayList<Task> getHistory(){ //метод возвращает список историй задач
        return (ArrayList<Task>) history;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getNewId() { //выделила в отдельный метод по рекомендации
        id++;
        return id;
    }

    @Override
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

    @Override
    public ArrayList<Task> getListOfTasks() { // возвращает список задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getListOfSubTasks() { // возвращает список подзадач
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getListOfEpics() { // // возвращает список эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getListOfSubtasksOfEpics(Integer id) { //возвращает список сабтасков эпика, исправила параметр метода
        return new ArrayList<>(epics.get(id).getSubTasksOfEpic());
    }

    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
             deleteTaskFromHistory(task);
        }// разделила на три отдельных метода, каждый из которых удаляет свой тип задач
        tasks.clear();
    } //предусмотрела удаление всех задач для каждого типа задач

    @Override
    public void deleteAllSubtasks() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (SubTask subTask : subtasks.values()) {
            epics.add(subTask.getEpic());
            deleteTaskFromHistory(subTask);
        }
        subtasks.clear();
        for (Epic epic : epics) {
            epic.deleteSubtasks();
            epic.checkStatus();
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            deleteTaskFromHistory(epic);
        }
        for (SubTask subTask : subtasks.values()) {
            deleteTaskFromHistory(subTask);
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Object getTaskByID(Integer id) { // метод возвращает объект всех видов задач по его id
        Task result = null;
        if (tasks.containsKey(id)) {
            result = tasks.get(id);
            putTaskInHistory(result);
        } else if (subtasks.containsKey(id)) {
            result = subtasks.get(id);
            putTaskInHistory(result);
        } else if (epics.containsKey(id)) {
            result = epics.get(id);
            putTaskInHistory(result);
        } else {
            System.out.print("Такой id отсутствует. ");
        }
        return result;
    }

    @Override
    public void updateTask(Task task, Integer id) { //метод обновляет задачи всех видов
        if (task instanceof SubTask) {
            if (task.getId()==id){
            subtasks.put(id, (SubTask) task);
            ((SubTask) task).getEpic().checkStatus();
            }else{
                System.out.println("Вы пытаетесь записать подзадачу под другим id");
            }
        } else if (task instanceof Epic) {
            if (task.getId()==id){
                epics.put(id, (Epic) task);
            }else{
                System.out.println("Вы пытаетесь записать эпик под другим id");
            }
        } else {
            if (tasks.get(id) != null) {
                if (task.getId()==id){
                    tasks.put(id, task);
                }else{
                    System.out.println("Вы пытаетесь записать задачу под другим id");
                }
            } else {
                System.out.println("Такой задачи нет в списках");
            }
        }
    }

    @Override
    public void deleteTaskById(Integer id) { //метод удаляет задачу по id
            if (tasks.containsKey(id)) {
                deleteTaskFromHistory(tasks.get(id));
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            SubTask subTask= subtasks.get(id);
            subtasks.remove(id);
            deleteTaskFromHistory(subTask);
            epic.getSubTasksOfEpic().remove(subTask);
            epic.checkStatus();
        } else if (epics.containsKey(id)) {
                ArrayList<SubTask> subTasks = epics.get(id).getSubTasksOfEpic();
                deleteTaskFromHistory(epics.get(id));
            epics.remove(id);
            for (SubTask subTask : subTasks) {
                deleteTaskFromHistory(subTask);
            }
        } else {
            System.out.print("Нельзя удалить то, чего нет");
        }
    }
}
