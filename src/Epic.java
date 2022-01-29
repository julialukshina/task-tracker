import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasksOfEpic;
    private Status status;

    public Epic(String name, String description) {
        super(name, description);
                subTasksOfEpic = new ArrayList<>();
    }

    public void addSubTask(SubTask subtask) {
        subTasksOfEpic.add(subtask);
    }

    public void checkStatus() {
        if (subTasksOfEpic.size() == 0) {
            status = Status.NEW;
            return;
        }
        boolean isNew = true;
        boolean isDone = true;
        for (SubTask subtask : subTasksOfEpic){
                if (subtask.getStatus() != Status.NEW){
                    isNew = false;
                } else if (subtask.getStatus() != Status.DONE){
                    isDone = false;
                }
        }
        if (isNew) {
            status = Status.NEW;
        } else if (isDone) {
            status = Status.DONE;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    @Override
    public void setStatus(Status status) {
        System.out.println("Данный метод недоступен для этого задач класса Epic");
    }

    @Override
    public String toString() {

        return super.toString()  +
                ", subTasksOfEpic=" + subTasksOfEpic +
                '}';
    }
}
