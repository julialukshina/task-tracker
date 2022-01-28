import java.util.Objects;
public class Task { //класс задач
    private String name;
    private String description;
    private String status;
    private int id;

    public Task(String name, String description) { //конструктор класса задач
        this.name = name;
        this.description = description;
        status = "NEW";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override //переопределение equals()
    public boolean equals (Object obj){
        if (this == obj) return true;
        if(obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return (Objects.equals(name, otherTask.name)&&
                Objects.equals(description, otherTask.description)&&
                id==otherTask.id);
    }

    @Override //переопределение toString()
    public String toString(){
        String result = " { name' = " + name + '\'' + ", description = '" + description
                + '\'' + ". status = '" + status + '\'' + "id = '"+ id + '\'';
        return result + '}';
    }
}
