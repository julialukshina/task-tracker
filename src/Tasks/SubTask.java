package Tasks;

import java.util.Objects;

public class SubTask extends Task { // класс подзадач наследует класс задач
    private final int epic;

    public SubTask(String name, String description, int epic) { // конструктор класса подзадач
        super(name, description);
        this.epic = epic;
    }

    public int getEpic() {
        return epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return Objects.equals(epic, subTask.epic);
    }

    @Override
    public String toString() {
        return getId() + "," + TypeOfTasks.SUBTASK + "," + getName() + "," + getStatus() + "," + getDescription() + "," + getEpic();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epic);
    }
}
