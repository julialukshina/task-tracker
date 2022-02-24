package Managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import Tasks.*;

public class InMemoryHistoryManager implements HistoryManager { //класс, работающий с историей задач
    LinkedList history = new LinkedList();

    @Override
    public void add(Task task) { //в целях устранения дублирования кода в отдельный метод вынесла заполнение списка истории задач
        history.linkLast(task);
    }

    @Override
    public void remove(Task task) { //в целях устранения дублирования кода в отдельный метод вынесла удаление из списка истории задач
        Node removingNode = history.nodes.get(task.getId());
        history.removeNode(removingNode);
    }

    @Override
    public List<Task> getHistory() { //метод возвращает список историй задач
        return history.getTasks(history);
    }

    static class LinkedList { //класс собственной реализации связанного листа
        final private Map<Integer, Node> nodes = new HashMap<>();
        private Node headNode;

        void linkLast(Task task) { //метод, добавляющий ноду в конец списка и избегающий повторов в нем
            if (headNode == null) {
                headNode = new Node(task);
                headNode.prevNode = headNode;
                headNode.nextNode = headNode;
                nodes.put(task.getId(), headNode);
            } else {
                Node callingNode = nodes.get(task.getId());
                if (callingNode != null) {
                    Node previousNode = callingNode.prevNode;
                    Node nextNode = callingNode.nextNode;
                    previousNode.nextNode = nextNode;
                    nextNode.prevNode = previousNode;
                    if (callingNode == headNode) {
                        headNode = nextNode;
                    }
                }
                Node newNode = new Node(task);
                Node lastNode = headNode.prevNode;
                lastNode.nextNode = newNode;
                headNode.prevNode = newNode;
                newNode.nextNode = headNode;
                newNode.prevNode = lastNode;
                nodes.put(task.getId(), newNode);
            }
        }

        List<Task> getTasks(LinkedList taskList) { //метод, возвращающий список с историей задач
            List<Task> tasks = new ArrayList<>();
            Node head = taskList.headNode;
            if (head == null) {
                System.out.println("История задач пуста");
            } else if (head.nextNode == head) {
                tasks.add(head.task);
            } else {
                tasks.add(head.task);
                while (head.nextNode != taskList.headNode) {
                    head = head.nextNode;
                    if (!tasks.contains(head.task)) {
                        tasks.add(head.task);
                    }
                }

            }
            return tasks;
        }

        void removeNode(Node node) { //метод, удаляющий ноду из хэш-таблицы
            if (nodes.containsKey(node.task.getId())) {
                nodes.remove(node.task.getId());
                if (nodes.size() == 0) {
                    headNode = null;
                }
                if (nodes.size() != 0) {
                    Node previousNode = node.prevNode;
                    Node nextNode = node.nextNode;
                    previousNode.nextNode = nextNode;
                    nextNode.prevNode = previousNode;
                    if (node == headNode) {
                        headNode = nextNode;
                    }
                }
            }
        }
    }
}