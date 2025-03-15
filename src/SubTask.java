public class SubTask extends Task {

    private final Epic owner;

    SubTask(String name, String description, Status status, Epic owner) {
        super(name, description, status);
        this.owner = owner;
    }

    Epic getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "SubTask {" +
                "owner id = " + owner.id +
                ", description = '" + description + '\'' +
                ", id = " + id +
                ", name = '" + name + '\'' +
                ", status = " + status +
                '}';
    }
}
