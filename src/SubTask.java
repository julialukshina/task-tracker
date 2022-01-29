
public class SubTask extends Task { // класс подзадач наследует класс задач
  private  Epic epic;
    public SubTask(String name, String description, Epic epic) { // конструктор класса подзадач
        super(name, description);
        this.epic=epic;
        this.epic.addSubTask(this);
    }

}
