package Tasks;

import TaskService.Status;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        ArrayList<Integer> subTaskIds = new ArrayList<>();
        for (SubTask subTask : subTasks) {
            subTaskIds.add(subTask.getId());
        }
        return "Tasks.Epic {" +
                "subTaskIds = " + subTaskIds +
                ", description = '" + description + '\'' +
                ", id = " + id +
                ", name = '" + name + '\'' +
                ", status = " + status +
                '}';
    }

    public void setSubTasks(ArrayList<SubTask> SubTasks) {
        this.subTasks = SubTasks;
        updateStatus();
    }

    public void updateStatus() {
        if (subTasks.isEmpty()) return;
        if (subTasks.size() == 1) {
            setStatus(Objects.requireNonNull(subTasks.getFirst()).getStatus());
            return;
        }
        for (int i = 0; i < subTasks.size() - 1; i++) {
            if (Objects.requireNonNull(subTasks.get(i)).getStatus()
                    != Objects.requireNonNull(subTasks.get(i + 1)).getStatus()
                    || Objects.requireNonNull(subTasks.get(i)).getStatus() == Status.IN_PROGRESS) {
                setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        setStatus(Objects.requireNonNull(subTasks.getFirst()).getStatus());
    }

    public void setOneSubTask(SubTask newSubTask) {
        for (int i = 0; i < subTasks.size(); i++) {
            if (newSubTask.id == subTasks.get(i).id) {
                subTasks.set(i, newSubTask);
                updateStatus();
                return;
            }
        }
        subTasks.add(newSubTask);
        updateStatus();
    }

    private void setStatus(Status status) {
        this.status = status;
    }
}
