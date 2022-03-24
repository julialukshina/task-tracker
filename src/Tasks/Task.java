package Tasks;

import java.util.Objects;

public class Task { //класс задач
    private String name;
    private String description;
    private Status status;
    private Integer id;

    public Task(String name, String description) { //конструктор класса задач
        this.name = name;
        this.description = description;
        status = status.NEW;
        id = 0;
    }

    public String getName() {
        return name;
    } //геттеры и сеттеры полей класса

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override //переопределение toString()
    public String toString() {
        return id + "," + TypeOfTasks.TASK + "," + name + "," + status + "," + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }
}
