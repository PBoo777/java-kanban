package tasks;

import taskservice.Status;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private int ownerId;

    public SubTask(String name, String description, Status status, int ownerId, LocalDateTime localDateTime,
                   Duration duration) {
        super(name, description, status, localDateTime, duration);
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int id) {
        ownerId = id;
    }

    public SubTask selfClone() {
        SubTask subTask = new SubTask(getName(), getDescription(), getStatus(), getOwnerId(), getStartTime(),
                getDuration());
        subTask.endTime = getEndTime();
        subTask.setId(getId());
        return subTask;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", ownerId=" + ownerId +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
                '}';
    }
}

