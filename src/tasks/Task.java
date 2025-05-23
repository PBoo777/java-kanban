package tasks;

import taskservice.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    int id;
    String name;
    String description;
    Status status;
    Duration duration;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.description = description;
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        if ((this.startTime != null) && (this.duration != null)) {
            endTime = this.startTime.plus(this.duration);
        }
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
        hash *= 43;
        if (startTime != null) {
            hash += startTime.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(status, task.status) && id == task.id && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
