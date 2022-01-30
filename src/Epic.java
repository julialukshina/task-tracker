import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasksOfEpic;

    public Epic(String name, String description) {
        super(name, description);
        subTasksOfEpic = new ArrayList<>();
    }

    public void addSubTask(SubTask subtask) { //метод записи сабтасков в лист эпика
        subTasksOfEpic.add(subtask);
    }

    public ArrayList<SubTask> getSubTasksOfEpic() { //метод возвращает список подзадач определенного эпика
        return subTasksOfEpic;
    }

    public void deleteSubtasks() { //метод удаления сабтасков из списка
        subTasksOfEpic.clear();
    }

    public void checkStatus() { //метод проверки статуса для эпика
        if (subTasksOfEpic.size() == 0) {
            super.setStatus(Status.NEW);
            return;
        }
        boolean isNew = true;
        boolean isDone = true;
        for (SubTask subtask : subTasksOfEpic) {
            if (subtask.getStatus() != Status.NEW) {
                isNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                isDone = false;
            }
        }
        if (isNew) {
            super.setStatus(Status.NEW);
        } else if (isDone) {
            super.setStatus(Status.DONE);
        } else {
            super.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override // переопределение метода setStatus(Status status) для эпиков
    public void setStatus(Status status) {
        System.out.println("Данный метод недоступен для этого задач класса Epic");
    }

    @Override //переопределение toString()
    public String toString() {
        return super.toString() +
                ", subTasksOfEpic=" + subTasksOfEpic +
                '}';
    }
}
