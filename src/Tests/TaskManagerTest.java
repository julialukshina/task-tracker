package Tests;

import Managers.Managers;
import Managers.TaskManager;
import Tasks.*;
import Enams.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }

    @Test
        //тест на пустую историю у пустого менеджера
    void shouldBeEmptyHistory() {
        assertTrue(manager.history().isEmpty());
    }

    @Test
//тест на пустую историю из-за некорректного id
    void shouldBeEmptyHistoryBecauseIDDoesNotExist() {
        manager.getTaskByID(10);
        assertTrue(manager.history().isEmpty());
    }

    @Test
        //тест на не пустую историю
    void shouldBeNotEmptyHistory() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        manager.getTaskByID(1);
        assertFalse(manager.history().isEmpty());
        assertEquals(1, manager.history().size());
    }

    @Test
        //тест на добавление нового id
    void shouldGetNewId1() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        assertEquals(1, epic1.getId());
    }

    @Test
        //тест на корректное добавление задач
    void shouldBeCorrectlyPutTask() {
        Task task1 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        manager.putTask(task1);
        System.out.println(task1.getEndTime());
        assertFalse(manager.getListOfTasks().isEmpty());
        assertEquals(1, manager.getListOfTasks().size());

        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        assertFalse(manager.getListOfEpics().isEmpty());
        assertEquals(1, manager.getListOfEpics().size());

        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 2);
        manager.putTask(subTask1);
        assertFalse(manager.getListOfSubTasks().isEmpty());
        assertEquals(1, manager.getListOfSubTasks().size());
    }

    @Test
        //тест на работу метода по добавлению задачи с нулевым значением
    void shouldThrowNullPointerExceptionPutTask() {
        assertThrows(NullPointerException.class, () -> manager.putTask(null));
    }

    @Test
        //серия тестов на возвращение листа всех задач конкретного типа и подзадач эпика
    void getListOfTasks() {
        assertTrue(manager.getListOfTasks().isEmpty());
        Task task1 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        manager.putTask(task1);
        assertFalse(manager.getListOfTasks().isEmpty());
        assertEquals(1, manager.getListOfTasks().size());

    }

    @Test
    void getListOfSubTasks() {
        assertTrue(manager.getListOfSubTasks().isEmpty());
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        assertFalse(manager.getListOfSubTasks().isEmpty());
        assertEquals(1, manager.getListOfSubTasks().size());

    }

    @Test
    void getListOfEpics() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        assertTrue(manager.getListOfSubtasksOfEpics(1).isEmpty());
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        assertFalse(manager.getListOfSubtasksOfEpics(1).isEmpty());
        assertEquals(1, manager.getListOfSubtasksOfEpics(1).size());
    }

    @Test
    void getListOfSubtasksOfEpics() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        assertFalse(manager.getListOfSubTasks().isEmpty());
        assertEquals(1, manager.getListOfSubTasks().size());
    }

    @Test
        //серия тестов на удаление всех задач одного типа
    void deleteAllTasks() {
        Task task1 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        manager.putTask(task1);
        assertFalse(manager.getListOfTasks().isEmpty());
        assertEquals(1, manager.getListOfTasks().size());
        manager.deleteAllTasks();
        assertTrue(manager.getListOfTasks().isEmpty());
    }

    @Test
    void deleteAllSubtasks() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        assertFalse(manager.getListOfSubTasks().isEmpty());
        assertEquals(1, manager.getListOfSubTasks().size());
        manager.deleteAllSubtasks();
        assertTrue(manager.getListOfSubTasks().isEmpty());
        assertTrue(manager.getListOfSubtasksOfEpics(1).isEmpty());
    }

    @Test
    void deleteAllEpics() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 1);
        manager.putTask(subTask1);
        assertFalse(manager.getListOfEpics().isEmpty());
        assertEquals(1, manager.getListOfEpics().size());
        manager.deleteAllEpics();
        assertTrue(manager.getListOfEpics().isEmpty());
        assertTrue(manager.getListOfSubTasks().isEmpty());
    }

    @Test
        //тест на метод getTaskByID()
    void getTaskByID() {
        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        assertNull(manager.getTaskByID(10));
        assertNotNull(manager.getTaskByID(1));
    }


    @Test
        //тест на метод updateTask()
    void updateTask() {
        Task task1 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        manager.putTask(task1);
        task1.setName("Купить подарок сестре");
        manager.updateTask(task1, 1);
        assertEquals("Купить подарок сестре", ((Task) manager.getTaskByID(1)).getName());


        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        epic1.setDescription("квартира");
        manager.updateTask(epic1, 2);
        assertEquals("квартира", ((Epic) manager.getTaskByID(2)).getDescription());

        Task epic3 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic3);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 3);
        manager.putTask(subTask1);
        subTask1.setStatus(Status.DONE);
        manager.updateTask(subTask1, 4);
        assertEquals(Status.DONE, ((SubTask) manager.getTaskByID(4)).getStatus());
    }

    @Test
        //тест на метод deleteTaskById()
    void deleteTaskById() {
        Task task1 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        manager.putTask(task1);
        assertFalse(manager.getListOfTasks().isEmpty());
        assertEquals(1, manager.getListOfTasks().size());
        manager.deleteTaskById(1);
        assertFalse(manager.getListOfTasks().contains(task1));

        Task epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        assertFalse(manager.getListOfEpics().isEmpty());
        assertEquals(1, manager.getListOfEpics().size());
        manager.deleteTaskById(2);
        assertFalse(manager.getListOfEpics().contains(epic1));

        Task epic2 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic2);
        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 3);
        manager.putTask(subTask1);
        assertFalse(manager.getListOfSubTasks().isEmpty());
        assertEquals(1, manager.getListOfSubTasks().size());
        manager.deleteTaskById(4);
        assertFalse(manager.getListOfEpics().contains(subTask1));
    }

    @Test
        //тест на метод booleanCanSaveTask()
    void booleanCanSaveTask() {
        Task task1 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        assertTrue(manager.canSaveTask(task1));
        manager.putTask(task1);

        TaskManager manager = Managers.GetDefault();
        ZonedDateTime now = ZonedDateTime.now();
        Duration duration = Duration.ofMinutes(30);
        ZonedDateTime now2 = now.plusMinutes(60);

        Task task2 = new Task("Поход к врачу", "до 15 февраля", duration, now);
        assertTrue(manager.canSaveTask(task2));
        manager.putTask(task2);


        Task task3 = new Task("Купить подарок брату", "Скрин подарка в ВК", duration, now2);
        assertTrue(manager.canSaveTask(task3));
        manager.putTask(task3);

        Task task4 = new Task("Купить подарок брату", "Скрин подарка", duration, now);
        assertFalse(manager.canSaveTask(task4));
    }
}