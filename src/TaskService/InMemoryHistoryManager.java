package TaskService;

import Tasks.Task;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private Node head;
    private Node tail;
    private int size;
    final HashMap<Integer, Node> taskViewsHashMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (taskViewsHashMap.containsKey(task.getId())) {
            Node node = taskViewsHashMap.get(task.getId());
            if (node != tail){
                removeNode(node);
                tail.next = node;
                node.next = null;
                node.prev = tail;
                tail = node;
            }
        } else {
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (taskViewsHashMap.containsKey(id)) {
            removeNode(taskViewsHashMap.get(id));
            taskViewsHashMap.remove(id);
        }
    }

    private void removeNode(Node node) {
        if (node != null && taskViewsHashMap.containsValue(node)) {
            if (node == head) {
                head = node.next;
            }
            if (node == tail) {
                tail = node.prev;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            if (node.prev != null) {
                node.prev.next = node.next;
            }
        }
    }

    private void linkLast(Task task) {
        if (taskViewsHashMap.isEmpty()) {
            head = new Node(task);
            tail = head;
        } else {
            tail.next = new Node(task);
            tail.next.prev = tail;
            tail = tail.next;
        }
        taskViewsHashMap.put(task.getId(), tail);
    }

    private List<Task> getTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>();
        Node node = head;
        while (node != null) {
            taskArrayList.add(node.data);
            node = node.next;
        }
        return taskArrayList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
