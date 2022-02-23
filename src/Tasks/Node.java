package Tasks;

import java.util.Objects;

public class Node {// отдельный класс для нод
    public Task task;
    public Node nextNode;
    public Node prevNode;

    public Node(Task task) {
        this.task = task;
        this.nextNode = null;
        this.prevNode = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(task, node.task) && Objects.equals(nextNode, node.nextNode) && Objects.equals(prevNode, node.prevNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, nextNode, prevNode);
    }
}
