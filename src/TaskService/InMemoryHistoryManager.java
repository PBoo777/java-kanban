package TaskService;

import Tasks.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    ArrayList<Task> lastTenTaskViews = new ArrayList<>();

    @Override
    public void add(Task task) {
        lastTenTaskViews.add(task);
        if (lastTenTaskViews.size() > 10) lastTenTaskViews.removeFirst();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return lastTenTaskViews;
    }
}
