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
        if (getSubTasks().isEmpty()) return;
        if (getSubTasks().size() == 1) {
            setStatus(Objects.requireNonNull(getSubTasks().getFirst()).getStatus());
            return;
        }
        for (int i = 0; i < getSubTasks().size() - 1; i++) {
            if (Objects.requireNonNull(getSubTasks().get(i)).getStatus()
                    != Objects.requireNonNull(getSubTasks().get(i + 1)).getStatus()
                    || Objects.requireNonNull(getSubTasks().get(i)).getStatus() == Status.IN_PROGRESS) {
                setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        setStatus(Objects.requireNonNull(getSubTasks().getFirst()).getStatus());
    }

    public void setOneSubTask(SubTask subTask) {
        if (!getSubTasks().contains(subTask)) getSubTasks().add(subTask);
        updateStatus();
    }

    private void setStatus(Status status) {
        this.status = status;
    }
}
