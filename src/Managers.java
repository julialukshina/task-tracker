public class Managers { //утилитарный метод
    public static TaskManager GetDefault(){
return new InMemoryTaskManager();
    }
}
