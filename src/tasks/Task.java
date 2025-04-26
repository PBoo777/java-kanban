package tasks;

import taskservice.Status;

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
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status && id == task.id;
    }

    @Override
    public String toString() {
        return "Tasks.Task {" +
                "description = '" + description + '\'' +
                ", name = '" + name + '\'' +
                ", id = " + id +
                ", status = " + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

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

}
