package Managers;

import Enams.Status;
import Enams.TypeOfTasks;
import Exсeptions.ManagerSaveException;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
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
            if (taskElements[5].equals("null")) {
                task.setStartTime(null);
            } else {
                task.setStartTime(LocalDateTime.parse(taskElements[5]));
            }
            if (taskElements[6].equals("null")) {
                task.setDuration(null);
            } else {
                task.setDuration(Duration.parse(taskElements[6]));
            }
            return task;
        } else if (taskElements[1].equals(TypeOfTasks.EPIC.toString())) {
            Task epic = new Epic(taskElements[2], taskElements[4]);
            epic.setId(Integer.parseInt(taskElements[0]));
            epic.setStatus(statusFromString(taskElements[3]));
            return epic;
        } else {
            Task subtask = new SubTask(taskElements[2], taskElements[4], Integer.parseInt(taskElements[7]));
            subtask.setId(Integer.parseInt(taskElements[0]));
            subtask.setStatus(statusFromString(taskElements[3]));
            if (taskElements[5].equals("null")) {
                subtask.setStartTime(null);
            } else {
                subtask.setStartTime(LocalDateTime.parse(taskElements[5]));
            }
            if (taskElements[6].equals("null")) {
                subtask.setDuration(null);
            } else {
                subtask.setDuration(Duration.parse(taskElements[6]));
            }
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

    public void save() { // метод сохранения данных в файл
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getListOfTasks());
        allTasks.addAll(getListOfEpics());
        allTasks.addAll(getListOfSubTasks());
        try (FileWriter writer = new FileWriter(file);) {
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write("id,type,name,status,description,startTime,duration,epic" + "\n"); //добавлены поля по времени, скорректирован порядок
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
}
