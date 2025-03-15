import java.util.Objects;

public class Task {

    String name;
    String description;
    int id;
    Status status;

    public Task(String name, String description, Status status) {
        this.description = description;
        this.name = name;
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        if (name != null) {
            hash += name.hashCode();
        }
        hash *= 17;
        if (description != null) {
            hash += description.hashCode();
        }
        hash *= 31;
        if (status != null) {
            hash += status.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public String toString() {
        return "Task {" +
                "description = '" + description + '\'' +
                ", name = '" + name + '\'' +
                ", id = " + id +
                ", status = " + status +
                '}';
    }
}
