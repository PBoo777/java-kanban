package Tasks;

import TaskService.Manager;
import TaskService.Status;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public void updateStatus() {
        if (subTaskIds.isEmpty()) return;
        if (subTaskIds.size() == 1) {
                status = Objects.requireNonNull(Manager.getTaskById(subTaskIds.getFirst())).status;
                return;
        }
        for (int i = 0; i < subTaskIds.size() - 1; i++) {
            if (Objects.requireNonNull(Manager.getTaskById(subTaskIds.get(i))).status
                    != Objects.requireNonNull(Manager.getTaskById(subTaskIds.get(i + 1))).status
                    || Objects.requireNonNull(Manager.getTaskById(subTaskIds.get(i))).status == Status.IN_PROGRESS) {
                status = Status.IN_PROGRESS;
                return;
            }
        }
        status = Objects.requireNonNull(Manager.getTaskById(subTaskIds.getFirst())).status;
    }

    public void setOneSubTask(int id) {
        if (subTaskIds.isEmpty() || !(subTaskIds.contains(id))) subTaskIds.add(id);
        updateStatus();
    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
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
}
