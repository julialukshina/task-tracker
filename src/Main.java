public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
//далее создаются объекты: 2 задачи, эпик с двуся подзадачами, эпик с одной подзадачей
        Task task1 = new Task("Поход в кино", "Сибирский цирюльник");
        manager.putTask(task1);
        Task task2 = new Task("Купить подарок брату", "Скрин подарка в ВК");
        manager.putTask(task2);
        Epic epic1 = new Epic("Ремонт", "квартира в центре");
        manager.putTask(epic1);
        SubTask subTask1 = new SubTask("Дизайнер", "Не позднее марта", epic1);
        manager.putTask(subTask1);
        SubTask subTask2 = new SubTask("Бригада", "Не позднее апреля", epic1);
        manager.putTask(subTask2);
        Epic epic2 = new Epic("Поход к врачу", "до 15 февраля");
        manager.putTask(epic2);
        SubTask subTask3 = new SubTask("Записьк врачу", "Не позднее 5 февраля", epic2);
        manager.putTask(subTask3);
//далее идет печать списков эпиков, задач и подзадач
        System.out.println("Список эпиков состоит из: " + manager.getListOfEpics());
        System.out.println("Список задач состоит из: " + manager.getListOfTasks());
        System.out.println("Список подзадач состоит из: " + manager.getListOfSubTasks());
//далее происходит изменение статусов объектов и update объектов
        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.DONE);
        epic1.setStatus(Status.IN_PROGRESS);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        epic2.setStatus(Status.DONE);
        subTask3.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1, 1);
        manager.updateTask(task1, 2);
        manager.updateTask(epic1, 3);
        manager.updateTask(subTask1, 4);
        manager.updateTask(subTask2, 5);
        manager.updateTask(epic2, 6);
        manager.updateTask(subTask3, 7);
//далее идет печать списков эпиков, задач и подзадач с учетом внесенныъх изменений
        System.out.println("Список эпиков состоит из: " + manager.getListOfEpics());
        System.out.println("Список задач состоит из: " + manager.getListOfTasks());
        System.out.println("Список подзадач состоит из: " + manager.getListOfSubTasks());
    }
}
