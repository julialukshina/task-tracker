import Managers.Managers;
import Managers.*;
import Tasks.*;

import java.time.Duration;
import java.time.ZonedDateTime;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.GetDefault();
        ZonedDateTime now = ZonedDateTime.now();
        Duration duration = Duration.ofMinutes(30);
        ZonedDateTime now2 = now.plusMinutes(60);
Task epic1=new Epic("Поход к врачу", "до 15 февраля");
manager.putTask(epic1);
        Task subtask1 = new SubTask("Поход к врачу", "до 15 февраля", 1/*, duration, now*/);
        manager.putTask(subtask1);
        System.out.println(epic1.getStartTime());
        System.out.println(epic1.getEndTime());
        System.out.println(epic1.getDuration());
        Task subtask4 = new SubTask("Поход к врачу", "до 15 февраля", 1, duration, now);
       manager.putTask(subtask4);
        System.out.println(epic1.getStartTime());
        System.out.println(epic1.getEndTime());
        System.out.println(epic1.getDuration());
        Task subtask3 = new SubTask("Купить подарок брату", "Скрин подарка в ВК", 1);
        manager.putTask(subtask3);
        System.out.println(epic1.getStartTime());
        System.out.println(epic1.getEndTime());
        System.out.println(epic1.getDuration());
        Task subtask2 = new SubTask("Купить подарок брату", "Скрин подарка в ВК", 1, duration, now2);
       manager.putTask(subtask2);
        System.out.println(epic1.getStartTime());
        System.out.println(epic1.getEndTime());
        System.out.println(epic1.getDuration());
        System.out.println(epic1);
//        try{
//            Task task3 = new Task("Купить подарок брату", "Скрин подарка", duration, now);
//            manager.putTask(task3);
//        } catch (IllegalArgumentException e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println(((InMemoryTaskManager)manager).getTreeSet());
//
//        manager.deleteTaskById(1);
//        System.out.println(((InMemoryTaskManager)manager).getTreeSet());
//
//        task2.setName("no");
//        manager.updateTask(task2, 2);
//        System.out.println(((InMemoryTaskManager)manager).getTreeSet());
//
//
//        manager.deleteAllTasks();
//        System.out.println(((InMemoryTaskManager)manager).getTreeSet());



        //далее создаются объекты: 2 задачи, эпик с тремя подзадачами, эпик без задач
//        Task task1 = new Task("Поход в кино", "Сибирский цирюльник");
//        manager.putTask(task1);
//        Task task2 = new Task("Купить подарок брату", "Скрин подарка в ВК");
//        manager.putTask(task2);
//        Task epic1 = new Epic("Ремонт", "квартира в центре");
//        manager.putTask(epic1);
//        Task subTask1 = new SubTask("Дизайнер", "Не позднее марта", 3);
//        manager.putTask(subTask1);
//        Task subTask2 = new SubTask("Бригада", "Не позднее апреля", 3);
//        manager.putTask(subTask2);
//        Task subTask3 = new SubTask("Согласование проекта", "Не позднее конца марта", 3);
//        manager.putTask(subTask3);
//        Task epic2 = new Epic("Поход к врачу", "до 15 февраля");
//        manager.putTask(epic2);
//        System.out.println("История задач состоит из: " + manager.history());

//        manager.getTaskByID(1); //вызываем задачи через getTaskByID, чтобы они отобразились в истории задач
//        manager.getTaskByID(3);
//        manager.getTaskByID(6);
//        manager.getTaskByID(7);
//        manager.getTaskByID(7);
//        manager.getTaskByID(1);
//        manager.getTaskByID(1);
//        manager.getTaskByID(2);
//        manager.getTaskByID(4);
//        manager.getTaskByID(1);
//        manager.getTaskByID(5);
//        System.out.println("История задач состоит из: " + manager.history());
//        manager.deleteTaskById(2);
//        System.out.println("История задач состоит из: " + manager.history());

    }
}
