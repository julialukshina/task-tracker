package Tasks;

import Enams.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Task { //класс задач
    private String name;
    private String description;
    private Status status;
    private Integer id;
    protected   Duration duration;
    protected ZonedDateTime startTime;
    protected ZonedDateTime endTime;

    public Task(String name, String description) { //конструктор класса задач
        this.name = name;
        this.description = description;
        status = status.NEW;
        id = 0;
    }

    public Task(String name, String description, Duration duration, ZonedDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        status = status.NEW;
        id = 0;
        endTime = getEndTime();
    }

    public ZonedDateTime getEndTime() {
        if (startTime != null && duration != null) {
            endTime =  startTime.plusMinutes(duration.toMinutes());
        }else {
            endTime=null;
        }
        return endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
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
        return id + "," + TypeOfTasks.TASK + "," + name + "," + status + "," + description + "," + startTime + ","
                + duration;
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
