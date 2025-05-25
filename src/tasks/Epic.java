package tasks;

import taskservice.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW, null, null);
    }

    public Epic selfClone() {
        Epic epic = new Epic(getName(), getDescription());
        epic.setStatus(getStatus());
        epic.setSubTaskIds(getSubTaskIds());
        epic.setStartTime(getStartTime());
        epic.setDuration(getDuration());
        epic.setEndTime(getEndTime());
        epic.setId(getId());
        return epic;
    }

    public ArrayList<Integer> getSubTaskIds() {
        return new ArrayList<>(subTaskIds);
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    public void setNewSubTaskId(int id) {
        if (!subTaskIds.contains(id)) {
            subTaskIds.add(id);
        }
    }

    public void removeSubTaskId(int id) {
        Integer integer = id;
        subTaskIds.remove(integer);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subTaskIds=" + subTaskIds +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
                '}';
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
