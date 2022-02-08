import java.util.ArrayList;

public interface TaskManager {
    public ArrayList<Task> getHistory();
        public Integer getId();
        public void setId(Integer id);
        public Integer getNewId();
        public void putTask(Task task);
        public ArrayList<Task> getListOfTasks();
        public ArrayList<SubTask> getListOfSubTasks();
        public ArrayList<Epic> getListOfEpics();
        public ArrayList<SubTask> getListOfSubtasksOfEpics(Integer id);
        public void deleteAllTasks();
        public void deleteAllSubtasks();
        public void deleteAllEpics();
       public Object getTaskByID(Integer id);
        public void updateTask(Task task, Integer id);
        public void deleteTaskById(Integer id);
}


