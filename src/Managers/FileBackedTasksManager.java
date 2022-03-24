package Managers;

import Exсeptions.ManagerSaveException;
import Tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager { // создан новый класс для записи информации файла и восстановления менеджера из файла
    private final File file;

    public FileBackedTasksManager(File file) { //конструктор для объектов нового FileBackedTasksManager
        super();
        this.file = file;
    }

    private static Task fromString(String value) { //формирует объект типа Task из строки
        String[] taskElements = value.split(",");
        if (taskElements[1].equals(TypeOfTasks.TASK.toString())) {
            Task task = new Task(taskElements[2], taskElements[4]);
            task.setId(Integer.parseInt(taskElements[0]));
            task.setStatus(statusFromString(taskElements[3]));
            return task;
        } else if (taskElements[1].equals(TypeOfTasks.EPIC.toString())) {
            Task epic = new Epic(taskElements[2], taskElements[4]);
            epic.setId(Integer.parseInt(taskElements[0]));
            epic.setStatus(statusFromString(taskElements[3]));
            return epic;
        } else {
            Task subtask = new SubTask(taskElements[2], taskElements[4], Integer.parseInt(taskElements[5]));
            subtask.setId(Integer.parseInt(taskElements[0]));
            subtask.setStatus(statusFromString(taskElements[3]));
            return subtask;
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) { //создает объект типа FileBackedTasksManager из данных, хранящихся в переданном файле
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.equals("")) {
                    fileBackedTasksManager.addTask(fromString(line));
                } else {
                    String[] splitLine = bufferedReader.readLine().split(",");
                    for (String s : splitLine) {
                        fileBackedTasksManager.getTaskByID(Integer.parseInt(s));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не обнаружен");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка записи"); //выбрасывает наше исключение ManagerSaveException
        }
        return fileBackedTasksManager;
    }

    private static Status statusFromString(String element) { //возвращает элемент перечисления после преобразования строки
        if (element.equals(Status.NEW.toString())) {
            return Status.NEW;
        } else if (element.equals(Status.IN_PROGRESS.toString())) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }

    private void save() { // метод сохранения данных в файл
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getListOfTasks());
        allTasks.addAll(getListOfEpics());
        allTasks.addAll(getListOfSubTasks());
        try (FileWriter writer = new FileWriter(file);) {
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write("id,type,name,status,description,epic" + "\n");
            for (Task task : allTasks) {
                bufferWriter.write(task.toString() + "\n");
            }
            if (!history().isEmpty()) {
                bufferWriter.write("\n");
                for (Task task : history()) {
                    bufferWriter.write(task.getId() + ",");
                }
            }
            bufferWriter.flush();
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка чтения"); //выбрасывает наше исключение ManagerSaveException
        }
    }

    public void addTask(Task task) { // метод для добавления задачи без присваивания нового ID
        if (task instanceof SubTask) {
            subtasks.put(task.getId(), (SubTask) task);
            epics.get(((SubTask) task).getEpic()).addSubTask((SubTask) task); //сабтаск записывается в список эпика
            epics.get(((SubTask) task).getEpic()).checkStatus(); // дополнила проверкой статуса эпика
        } else if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        } else {
            tasks.put(task.getId(), task);
        }
    }

    //далее: переопределение методов родительского класса InMemoryTaskManager, в которых треюуется вызов метода save();

    @Override
    public void putTask(Task task) {
        super.putTask(task);
        save();
    }

    @Override
    public Object getTaskByID(Integer id) {
        Object object = super.getTaskByID(id);
        save();
        return object;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();

    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void updateTask(Task task, Integer id) {
        super.updateTask(task, id);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    public static void main(String[] args) { // main для проверки нового менеджера FileBackedTasksManager
        File file = new File("src/file.csv");
        TaskManager manager = new FileBackedTasksManager(file);

        Task task1 = new Task("Поход в кино", "Сибирский цирюльник");
        manager.putTask(task1);
        Task task2 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        manager.putTask(task2);
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 3);
        manager.putTask(subTask1);
        Task subTask2 = new SubTask("Бригада", "Не позднее апреля", 3);
        manager.putTask(subTask2);
        Task subTask3 = new SubTask("Согласование проекта", "Не позднее конца марта", 3);
        manager.putTask(subTask3);
        Task epic2 = new Epic("Поход к врачу", "до 15 февраля");
        manager.putTask(epic2);

        manager.getTaskByID(1); //вызываем задачи через getTaskByID, чтобы они отобразились в истории задач
        manager.getTaskByID(3);
        manager.getTaskByID(6);
        manager.getTaskByID(7);
        manager.getTaskByID(7);
        manager.getTaskByID(1);
        manager.getTaskByID(1);
        manager.getTaskByID(2);
        manager.getTaskByID(4);
        manager.getTaskByID(1);
        manager.getTaskByID(5);
        System.out.println("История задач состоит из: " + manager.history());
        manager.deleteTaskById(2);
        System.out.println("История задач состоит из: " + manager.history());

        TaskManager manager1 = FileBackedTasksManager.loadFromFile(file);
    }
}
