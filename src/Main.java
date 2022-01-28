public class Main {
    public static void main(String[] args) {
Manager manager = new Manager();
        Epic test1 = new Epic("проверка", "что может быть лучше");
        manager.putTask(test1);
        Epic test2 = new Epic("проверка2", "что может быть лучше2");
        manager.putTask(test2);
        Epic test3 = new Epic("проверка3", "что может быть лучше12");
        manager.putTask(test3);
        System.out.println("Список эпиков состоит из: " + manager.getListOfEpics());
        Task test4=new Task("this is cool", "eee");
        manager.putTask(test4);
            System.out.println("Список задач состоит из: " + manager.getListOfTasks());
    }
}
