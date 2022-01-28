import java.util.ArrayList;
public class Epic extends Task {
   ArrayList<SubTask>subTasksOfEpic;

   public Epic(String name, String description) {
      super(name, description);
      ArrayList<SubTask>subTasksOfEpic = new ArrayList<>();
   }

   @Override
   public void setStatus(String status) {
      System.out.println("Данный метод недоступен для этого задач класса Epic");
   }
}
