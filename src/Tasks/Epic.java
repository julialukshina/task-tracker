package Tasks;

import Enums.Status;
import Enums.TypeOfTasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {
    private final ArrayList<SubTask> subTasksOfEpic;



    public Epic(String name, String description) {
        super(name, description);
        subTasksOfEpic = new ArrayList<>();
    }

    public void addSubTask(SubTask subtask) { //метод записи сабтасков в лист эпика
        subTasksOfEpic.add(subtask);
        checkTime();
    }


    @Override //переопределила методы времени для эпика
    public LocalDateTime getEndTime() {
        if (!getSubTasksOfEpic().isEmpty()) {
            LocalDateTime maxEndTime = subTasksOfEpic.get(0).getEndTime();
            for (SubTask subtask : subTasksOfEpic) {
                if (maxEndTime != null) {
                    if(subtask.getEndTime()!= null) {
                        if (maxEndTime.isBefore(subtask.getEndTime())) {
                            maxEndTime = subtask.getEndTime();
                        }
                    }
                }
                if (maxEndTime == null && subtask.getEndTime()!= null){
                    maxEndTime = subtask.getEndTime();
                }
            }
            endTime = maxEndTime;
        } else{
            endTime=null;
        }
        return endTime;
    }

    @Override
    public Duration getDuration() {
        if (getStartTime() != null && getEndTime() != null) {
            setDuration(Duration.between(getStartTime(), getEndTime()));
            return Duration.between(getStartTime(), getEndTime());
        }
        return Duration.ZERO;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (!getSubTasksOfEpic().isEmpty()) {
            LocalDateTime minStartTime = subTasksOfEpic.get(0).getStartTime();
            for (SubTask subtask : subTasksOfEpic) {
                if (minStartTime != null) {
                    if(subtask.getStartTime()!= null) {
                        if (minStartTime.isAfter(subtask.getStartTime())) {
                            minStartTime = subtask.getStartTime();
                        }
                    }
                }
                if(minStartTime==null && subtask.getStartTime()!= null){
                    minStartTime = subtask.getStartTime();
                }
            }
            startTime = minStartTime;
        }else{
            startTime=null;
        }
        return startTime;
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

    public void checkTime() {
        getStartTime();
        getEndTime();
        getDuration();
    }


    @Override // переопределение метода setStatus(Tasks.Status status) для эпиков
    public void setStatus(Status status) {
        System.out.println("Данный метод недоступен для задач этого класса Tasks.Epic");
    }

    @Override //переопределение toString()
    public String toString() {
        return getId() + "," + TypeOfTasks.EPIC + "," + getName() + "," + getStatus() + "," + super.getDescription() + "," + startTime + "," + duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksOfEpic, epic.subTasksOfEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksOfEpic);
    }
}
