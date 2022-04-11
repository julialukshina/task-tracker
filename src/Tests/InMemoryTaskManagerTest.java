package Tests;

import Managers.InMemoryTaskManager;
import Managers.Managers;
import org.junit.jupiter.api.BeforeEach;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public InMemoryTaskManagerTest() {
        super((InMemoryTaskManager) Managers.GetDefault());
    }

    @BeforeEach
        //перед каждым тестом сождается новый менеджер
    void createTaskManager() {
        manager = (InMemoryTaskManager) Managers.GetDefault();
    }

}