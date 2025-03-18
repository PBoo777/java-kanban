package Tasks;

import TaskService.Status;

public class SubTask extends Task {

    private final Epic owner;

    public SubTask(String name, String description, Status status, Epic owner) {
        super(name, description, status);
        this.owner = owner;
    }

    public Epic getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Tasks.SubTask {" +
                "owner id = " + owner.id +
                ", description = '" + description + '\'' +
                ", id = " + id +
                ", name = '" + name + '\'' +
                ", status = " + status +
                '}';
    }
}
