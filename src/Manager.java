import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    private  int id;
    Task task;
    SubTask subtask;
    Epic epic;
    HashMap <Integer, Task> tasks = new HashMap<>();
    HashMap <Integer, SubTask> subtasks = new HashMap<>();
    HashMap <Integer, Epic> epics = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void putTask(Task task){
    if (task.getId()==0){
        int x=getId();
        setId(x+1);
        if (task instanceof SubTask) {
            task.setId(getId());
            subtasks.put(task.getId(), (SubTask) task);
        }else if(task instanceof Epic){
            task.setId(getId());
            epics.put(task.getId(), (Epic) task);
        }else{
            task.setId(getId());
            tasks.put(task.getId(), task);
        }
    }else{
        return;
    }
}

    public ArrayList getListOfTasks (){
        ArrayList <Task>listOfTasks = new ArrayList (tasks.values());
    return listOfTasks;
   }
    public ArrayList getListOfSubTasks () {
        ArrayList<Task> listOfSubTasks = new ArrayList(subtasks.values());
        return listOfSubTasks;
    }
    public ArrayList getListOfEpics () {
        ArrayList<Task> listOfEpics = new ArrayList(epics.values());
        return listOfEpics;
    }


}
