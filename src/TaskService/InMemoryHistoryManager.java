package TaskService;

import Tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    List<Task> lastTenTaskViews = new ArrayList<>();

    @Override
    public void add(Task task) {
        lastTenTaskViews.add(task);
        if (lastTenTaskViews.size() > 10) lastTenTaskViews.removeFirst();
    }

    @Override
    public List<Task> getHistory() {
        return lastTenTaskViews;
    }
}
