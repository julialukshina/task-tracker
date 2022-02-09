//Привет, Наталья! Прости, я просто запуталась в количестве InMemory менеджеров и в моей голове они слились в один)
package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager { //класс, работающий с историей задач
    List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) { //в целях устранения дублирования кода в отдельный метод вынесла заполнение списка истории задач
        if (history.size() < 10) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public void deleteTaskFromHistory(Task task) { //в целях устранения дублирования кода в отдельный метод вынесла удаление из списка истории задач
        if (history.contains(task)) {
            history.removeIf(task1 -> task1.equals(task));
        }
    }

    @Override
    public ArrayList<Task> getHistory() { //метод возвращает список историй задач
        return (ArrayList<Task>) history;
    }
}
