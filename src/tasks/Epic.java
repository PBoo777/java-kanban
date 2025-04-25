package tasks;

import taskService.Status;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Status> subTaskStatuses = new ArrayList<>();
    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public ArrayList<Integer> getSubTaskIds() {
        return new ArrayList<>(subTaskIds);
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    @Override
    public String toString() {
        return "Tasks.Epic {" +
                "subTaskIds = " + subTaskIds +
                ", description = '" + description + '\'' +
                ", id = " + id +
                ", name = '" + name + '\'' +
                ", status = " + status +
                '}';
    }

    public void updateStatus() {
        if (subTaskStatuses.isEmpty()) return;
        if (subTaskStatuses.size() == 1) {
            setStatus(Objects.requireNonNull(subTaskStatuses.getFirst()));
            return;
        }
        for (int i = 0; i < subTaskStatuses.size() - 1; i++) {
            if (Objects.requireNonNull(subTaskStatuses.get(i))
                    != Objects.requireNonNull(subTaskStatuses.get(i + 1))
                    || Objects.requireNonNull(subTaskStatuses.get(i)) == Status.IN_PROGRESS) {
                setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        setStatus(Objects.requireNonNull(subTaskStatuses.getFirst()));
    }

    public void setOneSubTask(SubTask newSubTask) {
        for (int i = 0; i < subTaskIds.size(); i++) {
            if (newSubTask.id == subTaskIds.get(i)) {
                subTaskStatuses.set(i, newSubTask.status);
                updateStatus();
                return;
            }
        }
        subTaskIds.add(newSubTask.id);
        subTaskStatuses.add(newSubTask.status);
        updateStatus();
    }

    public void removeOneSubTask(SubTask removedSubTask) {
        for (int i = 0; i < subTaskIds.size(); i++) {
            if (removedSubTask.id == subTaskIds.get(i)) {
                subTaskStatuses.remove(i);
                updateStatus();
                break;
            }
        }
        Integer i = removedSubTask.id;
        subTaskIds.remove(i);
    }

    public ArrayList<Status> getSubTaskStatuses() {
        return new ArrayList<>(subTaskStatuses);
    }

    public void setSubTaskStatuses(ArrayList<Status> subTaskStatuses) {
        this.subTaskStatuses = subTaskStatuses;
    }
}
