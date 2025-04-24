package Tasks;

import TaskService.Status;

public class SubTask extends Task {

    private int ownerId;

    public SubTask(String name, String description, Status status, int ownerId) {
        super(name, description, status);
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int id) {
        ownerId = id;
    }

    @Override
    public String toString() {
        return "Tasks.SubTask {" +
                "owner id = " + ownerId +
                ", description = '" + description + '\'' +
                ", id = " + id +
                ", name = '" + name + '\'' +
                ", status = " + status +
                '}';
    }
}

