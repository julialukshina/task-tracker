package Tests;

import Managers.Managers;
import Managers.TaskManager;
import Tasks.*;
import Enams.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    TaskManager manager;

    @BeforeEach
        //перед каждым тестом создается новый мернеджер
    void createTaskManager() {
        manager = Managers.GetDefault();
    }

    @Test
//тест статуса эпика без подзадач
    void checkStatusSubtasksEmpty() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
        //тест статуса эпика с двумя подзадачами со статусом new
    void checkStatusSubtasksStatusNew() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        Task subTask2 = new SubTask("Бригада", "Не позднее апреля", 1);
        manager.putTask(subTask2);
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
//тест статуса эпика с двумя подзадачами со статусом done
    void checkStatusSubtasksStatusDone() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        Task subTask2 = new SubTask("Бригада", "Не позднее апреля", 1);
        manager.putTask(subTask2);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        manager.updateTask(subTask1, 2);
        manager.updateTask(subTask2, 3);
        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
        //тест статуса эпика с двумя подзадачами со статусом new и done
    void checkStatusSubtasksStatusNewAndDone() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        Task subTask2 = new SubTask("Бригада", "Не позднее апреля", 1);
        manager.putTask(subTask2);
        subTask1.setStatus(Status.DONE);
        manager.updateTask(subTask1, 2);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
//тест статуса эпика с двумя подзадачами со статусом in progress
    void checkStatusSubtasksStatusInProgress() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        Task subTask2 = new SubTask("Бригада", "Не позднее апреля", 1);
        manager.putTask(subTask2);
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        manager.updateTask(subTask1, 2);
        manager.updateTask(subTask2, 3);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }
}