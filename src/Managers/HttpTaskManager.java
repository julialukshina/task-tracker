package Managers;

import Servers.KVTaskClient;
import Tasks.Task;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager{
    private KVTaskClient client;
    private Gson gson = new GsonBuilder()
            .setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    if(f.getName().equals("TYPE")){
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).create();
    private final String KEYS = "keys";
    private final String KEYS_HIST = "keysHist";

    public HttpTaskManager(String URL) {
        super(null);
        client = new KVTaskClient(URL);
    }
    @Override
    public void save(){ //сохранение состояния на сервер
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getListOfTasks());
        allTasks.addAll(getListOfEpics());
        allTasks.addAll(getListOfSubTasks());
        StringBuilder idToString = new StringBuilder();
        StringBuilder historyToString = new StringBuilder();
        if(!allTasks.isEmpty()){
            for(Task task : allTasks){
                idToString.append(task.getId());
                idToString.append(",");
try {
    client.put(String.valueOf(task.getId()), gson.toJson(task));
} catch (IOException | InterruptedException e) {
    e.printStackTrace();
}
            }
            try {
                client.put(KEYS, gson.toJson(idToString.toString()));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                client.put(KEYS, gson.toJson("Is Empty"));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!history().isEmpty()){
            for(Task task : history()){
                historyToString.append(task.getId());
                historyToString.append(",");
            }
            try {
                client.put(KEYS_HIST, gson.toJson(historyToString.toString()));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                client.put(KEYS_HIST, gson.toJson("History is Empty"));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
