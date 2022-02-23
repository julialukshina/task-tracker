package Managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import Tasks.*;

public class InMemoryHistoryManager implements HistoryManager { //класс, работающий с историей задач
    //List<Task> history = new ArrayList<>();
    LinkedList history = new LinkedList();

    @Override
    public void add(Task task) { //в целях устранения дублирования кода в отдельный метод вынесла заполнение списка истории задач
//        if (history.size() < 10) {
//            history.add(task);
//        } else {
//            history.remove(0);
//            history.add(task);
//        }
        history.linkLast(task);
    }

    @Override
    public void remove(Task task) { //в целях устранения дублирования кода в отдельный метод вынесла удаление из списка истории задач
//        if (history.contains(task)) {
//            history.removeIf(task1 -> task1.equals(task));
//        }
        Node removingNode = history.nodes.get(task.getId());
        history.removeNode(removingNode);
    }

    @Override
    public ArrayList<Task> getHistory() { //метод возвращает список историй задач
//        return (ArrayList<Task>) history;
        return history.getTasks(history);
    }

    class LinkedList { //класс собственной реализации связанного листа
        Map<Integer, Node> nodes = new HashMap<>();
        public Node headNode;
//        ArrayList<Task> tasks = new ArrayList<>();

        void linkLast(Task task) { //метод, добавляющий ноду в конец списка
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

        ArrayList<Task> getTasks(LinkedList taskList) {
            ArrayList<Task> tasks = new ArrayList<>();
            Node head = taskList.headNode;
            if (head == null) {
                System.out.println("История задач пуста");
            } else if (head.nextNode == head) {
                if (!tasks.contains(head.task)) {
                    tasks.add(head.task);
                }
            } else {
                if (!tasks.contains(head.task)) {
                    tasks.add(head.task);
                }
                while (head.nextNode != taskList.headNode) {
                    head = head.nextNode;
                    if (!tasks.contains(head.task)) {
                        tasks.add(head.task);
                    }
                }

            }
            return tasks;
        }

        void removeNode(Node node) {
            if (nodes.containsKey(node.task.getId())) {
                nodes.remove(node.task.getId());
//                tasks.remove(node.task);
                if (nodes.size() == 0) {
                    headNode = null;
                }
                if (nodes.size() != 0 /*&& tasks.size() != 0*/) {
                    Node previousNode = node.prevNode;
                    Node nextNode = node.nextNode;
                    previousNode.nextNode = nextNode;
                    nextNode.prevNode = previousNode;
                    if (node == headNode) {
//                     if (nodes.size() == 0) {
//                         headNode = null;
//                     }
                        headNode = nextNode;
                    }
//             }
//         if (node==headNode){
//            headNode.prevNode = null;
//            headNode.nextNode = null;
//            headNode.task = null;
//         }
//         Node previousNode=node.prevNode;
//         Node nextNode = node.nextNode;
//         previousNode.nextNode=nextNode;
//         nextNode.prevNode=previousNode;
                } else {
                    return;
                }
            }
        }
    }
}