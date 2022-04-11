package Managers;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HistoryManager historyManager = Managers.getDefaultHistory();
    TreeSet<Task> taskTreeSet = new TreeSet<>((Comparator) (task1, task2) -> { //новое поле - отсортированное дерево
        if (((Task) task2).getStartTime() == null && ((Task) task1).getStartTime() != null) {
            return -1;
        } else if (((Task) task2).getStartTime() == null) {
            return 1;
        } else if (((Task) task1).getStartTime() == null && ((Task) task2).getStartTime() != null) {
            return 1;
        } else if (((Task) task1).getStartTime().isBefore(((Task) task2).getStartTime())) {
            return -1;
        } else if (((Task) task1).getStartTime().equals(((Task) task2).getStartTime())) {
            return 0;
        }
        return 1;
    });

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    @Override
    public Integer getNewId() { //выделила в отдельный метод по рекомендации
        id++;
        return id;
    }

    public TreeSet getTreeSet() { //вспомогательный метод для возвращения дерева
        return taskTreeSet;
    }

    @Override
    public void putTask(Task task) { // записывает соответсвующую задачу в нужную мапу
        if (task instanceof Epic) {
            task.setId(getNewId());
            epics.put(task.getId(), (Epic) task);
        } else if (canSaveTask(task)) {
            if (task instanceof SubTask) {
                task.setId(getNewId());
                subtasks.put(task.getId(), (SubTask) task);
                epics.get(((SubTask) task).getEpic()).addSubTask((SubTask) task); //сабтаск записывается в список эпика
                epics.get(((SubTask) task).getEpic()).checkStatus(); // дополнила проверкой статуса эпика
                taskTreeSet.add(task);
            } else {
                task.setId(getNewId());
                tasks.put(task.getId(), task);
                taskTreeSet.add(task);
            }
        } else {
            throw new IllegalArgumentException("Задача не может быть добавлена, на это время запланирована другая задача");
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
            taskTreeSet.remove(task);
            if (historyManager.getHistory().contains(task)) {
                historyManager.remove(task);
            }
        }// разделила на три отдельных метода, каждый из которых удаляет свой тип задач
        tasks.clear();
    } //предусмотрела удаление всех задач для каждого типа задач

    @Override
    public void deleteAllSubtasks() {
        ArrayList<Integer> epics1 = new ArrayList<>();
        for (SubTask subTask : subtasks.values()) {
            epics1.add(subTask.getEpic());
            taskTreeSet.remove(subTask);
            if (historyManager.getHistory().contains(subTask)) {
                historyManager.remove(subTask);
            }
        }
        subtasks.clear();
        for (Integer epic : epics1) {
            epics.get(epic).deleteSubtasks();
            epics.get(epic).checkStatus();
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            if (historyManager.getHistory().contains(epic)) {
                historyManager.remove(epic);
            }
        }
        for (SubTask subTask : subtasks.values()) {
            taskTreeSet.remove(subTask);
            if (historyManager.getHistory().contains(subTask)) {
                historyManager.remove(subTask);
            }
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Object getTaskByID(Integer id) { // метод возвращает объект всех видов задач по его id
        Task result = null;
        if (tasks.containsKey(id)) {
            result = tasks.get(id);
            historyManager.add(result);
        } else if (subtasks.containsKey(id)) {
            result = subtasks.get(id);
            historyManager.add(result);
        } else if (epics.containsKey(id)) {
            result = epics.get(id);
            historyManager.add(result);
        } else {
            System.out.print("Такой id отсутствует.");
        }
        return result;
    }

    @Override
    public void updateTask(Task task, Integer id) { //метод обновляет задачи всех видов
        if (task instanceof SubTask) {
            if (task.getId() == id) {
                taskTreeSet.remove(getTaskByID(id));
                subtasks.put(id, (SubTask) task);
                epics.get(((SubTask) task).getEpic()).checkStatus();
                taskTreeSet.add(task);
            } else {
                System.out.println("Вы пытаетесь записать подзадачу под другим id");
            }
        } else if (task instanceof Epic) {
            if (task.getId() == id) {
                epics.put(id, (Epic) task);
            } else {
                System.out.println("Вы пытаетесь записать эпик под другим id");
            }
        } else {
            if (tasks.get(id) != null) {
                if (task.getId() == id) {
                    taskTreeSet.remove(getTaskByID(id));
                    tasks.put(id, task);
                    taskTreeSet.add(task);
                } else {
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
            taskTreeSet.remove(getTaskByID(id));
            if (historyManager.getHistory().contains(tasks.get(id))) {
                historyManager.remove(tasks.get(id));
            }
            tasks.remove(id);
        } else if (subtasks.containsKey(id)) {
            taskTreeSet.remove(getTaskByID(id));
            Epic epic = epics.get(subtasks.get(id).getEpic());
            SubTask subTask = subtasks.get(id);
            subtasks.remove(id);
            if (historyManager.getHistory().contains(subTask)) {
                historyManager.remove(subTask);
            }
            epic.getSubTasksOfEpic().remove(subTask);
            epic.checkStatus();
        } else if (epics.containsKey(id)) {
            ArrayList<SubTask> subTasks = epics.get(id).getSubTasksOfEpic();
            if (historyManager.getHistory().contains(epics.get(id))) {
                historyManager.remove(epics.get(id));
            }
            epics.remove(id);
            for (SubTask subTask : subTasks) {
                taskTreeSet.remove(subTask);
                subtasks.remove(subTask.getId());
                if (historyManager.getHistory().contains(subTask)) {
                    historyManager.remove(subTask);
                }
            }
        } else {
            System.out.print("Нельзя удалить то, чего нет");
        }
    }

    public boolean canSaveTask(Task task) { /* отдельный метод, определяющий, может ли быть добавлена задача или
    сабтаск, в зависимости от указанного в ней времени*/
        if (taskTreeSet.isEmpty()) {
            return true;
        }
        if (task.getStartTime() == null) {
            return true;
        } else {
            for (Task task1 : taskTreeSet) {
                if (task1.getStartTime() == null) {
                    continue;
                }
                if (task.getStartTime().equals(task1.getStartTime()) || task.getEndTime().equals(task1.getEndTime())) {
                    return false;
                }
                if (task1.getStartTime().isAfter(task.getStartTime())
                        && task1.getEndTime().isBefore(task.getEndTime())
                        || task.getEndTime().isAfter(task1.getStartTime())
                        && task.getEndTime().isBefore(task1.getEndTime())
                        || task.getStartTime().isAfter(task1.getStartTime())
                        && task.getStartTime().isBefore(task1.getEndTime())) {
                    return false;
                }
            }
        }
        return true;
    }
}