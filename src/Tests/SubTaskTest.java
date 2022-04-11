package Tests;

import Managers.Managers;
import Managers.TaskManager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    TaskManager manager;

    @BeforeEach
        //перед каждым тестом создается новый менеджер
    void createTaskManager() {
        manager = Managers.GetDefault();
    }

    @Test
        //тест по добавлению сабтаски с существующим эпиком
    void shouldBeAddedSubtask() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        assertFalse(((Epic) epic1).getSubTasksOfEpic().isEmpty());
        assertEquals(1, ((Epic) epic1).getSubTasksOfEpic().size());
    }

    @Test
        //тест ловит ошибку, так как нет эпика с id 1 и не может быть с id 0
    void shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
            manager.putTask(subTask1);
        });
        assertThrows(NullPointerException.class, () -> {
            Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 0);
            manager.putTask(subTask1);
        });
    }
}