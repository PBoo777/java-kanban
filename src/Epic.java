import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Integer> subTaskIds = new ArrayList<>();

    Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public void updateStatus() {
        if (subTaskIds.isEmpty()) return;
        if (subTaskIds.size() == 1) {
            status = Manager.getInstance(subTaskIds.getFirst()).status;
            return;
        }
        for (int i = 0; i < subTaskIds.size() - 1; i++) {
            if (Manager.getInstance(subTaskIds.get(i)).status != Manager.getInstance(subTaskIds.get(i+1)).status
                    || Manager.getInstance(subTaskIds.get(i)).status == Status.IN_PROGRESS) {
                status = Status.IN_PROGRESS;
                return;
            }
        }
        status = Manager.getInstance(subTaskIds.getFirst()).status;
    }

    public void setOneSubTask(int id) {
        if (subTaskIds.isEmpty() || !(subTaskIds.contains(id))) subTaskIds.add(id);
        updateStatus();
    }

    @Override
    public String toString() {
        return "Epic {" +
                "subTaskIds = " + subTaskIds +
                ", description = '" + description + '\'' +
                ", id = " + id +
                ", name = '" + name + '\'' +
                ", status = " + status +
                '}';
    }
}
