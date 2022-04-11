package Tests;

import Managers.HistoryManager;
import Managers.Managers;
import Managers.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager manager;
    TaskManager taskManager;

    @BeforeEach //перед каждым тестом создается два новых менеждера
    void createHistoryManager() {
        manager = Managers.getDefaultHistory();
        taskManager = Managers.GetDefault();
    }

    @Test //тест на пустую историю у нового менеджера
    void getHistory() {
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test //тест метода по добавлению задач и заполнения истории
    void add() {
        Task task1 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        taskManager.putTask(task1);
        manager.add(task1);
        assertEquals(1, manager.getHistory().size());

        Task epic1 = new Epic("Ремонт", "квартира в центре");
        taskManager.putTask(epic1);
        manager.add(epic1);
        assertEquals(2, manager.getHistory().size());

        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 2);
        taskManager.putTask(subTask1);
        manager.add(subTask1);
        manager.add(task1);
        assertEquals(3, manager.getHistory().size());
    }

    @Test //тест ловит исключение, если пытаться добавить пустую задачу
    void shouldBeNullPointerExceptionInRemove() {
        assertThrows(NullPointerException.class, () -> manager.remove(null));
    }

    @Test //тест метода remove()
    void remove() {
        Task task1 = new Task("Поход в кино", "Сибирский цирюльник");
        taskManager.putTask(task1);
        Task task2 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        taskManager.putTask(task2);
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        taskManager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 3);
        taskManager.putTask(subTask1);
        Task subTask2 = new SubTask("Бригада", "Не позднее апреля", 3);
        taskManager.putTask(subTask2);
        Task subTask3 = new SubTask("Согласование проекта", "Не позднее конца марта", 3);
        taskManager.putTask(subTask3);
        Task epic2 = new Epic("Поход к врачу", "до 15 февраля");
        taskManager.putTask(epic2);
        taskManager.getTaskByID(1);
        manager.add(task1);
        taskManager.getTaskByID(3);
        manager.add(epic1);
        taskManager.getTaskByID(6);
        manager.add(subTask3);
        taskManager.getTaskByID(7);
        manager.add(epic2);
        taskManager.getTaskByID(7);
        manager.add(epic2);
        taskManager.getTaskByID(1);
        manager.add(task1);
        taskManager.getTaskByID(1);
        manager.add(task1);
        taskManager.getTaskByID(2);
        manager.add(task2);
        taskManager.getTaskByID(4);
        manager.add(subTask1);
        taskManager.getTaskByID(1);
        manager.add(task1);
        taskManager.getTaskByID(5);
        manager.add(subTask2);
        assertEquals(7, manager.getHistory().size());
        manager.remove(epic1);
        assertEquals(6, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(epic1));
        manager.remove(task2);
        assertEquals(5, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(task2));
        manager.remove(subTask2);
        assertEquals(4, manager.getHistory().size());
        assertFalse(manager.getHistory().contains(subTask2));
    }
}