import java.util.Objects;
public class Task { //класс задач
    private String name;
    private String description;
    private Status status;
    private int id;

    public Task(String name, String description) { //конструктор класса задач
        this.name = name;
        this.description = description;
        status = status.NEW;
        id=0;
    }

    public String getName() {
        return name;
    } //геттеры и сеттеры полей класса

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override //переопределение toString()
    public String toString(){
        return "{ name' = " + name + '\'' + ", description = '" + description
                + '\'' + ". status = '" + status + '\'' + ", id = '"+ id + '}';
    }
}
