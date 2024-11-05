import java.util.ArrayList;
import java.util.List;

public class DLList {

    Node head, prev = null;

    // Convert binary tree to doubly linked list using in-order traversal
    public void convertBinaryTree(Node root) {
        if (root == null) return;

        // Process the left subtree
        convertBinaryTree(root.leftChild);

        // Link the current node
        if (prev == null) {
            head = root; // Set head if it's the first node
        } else {
            prev.rightChild = root;
            root.leftChild = prev;
        }
        prev = root; // Move previous to current

        // Process the right subtree
        convertBinaryTree(root.rightChild);
    }

    public Node find(String value) {
        Node current = head;

        while (current != null)
        {
            if (current.name == value)
            {
                return current;
            }
            else
            {
                current = current.rightChild;
            }
        }

        return null;
    }

    public List<String> display() {
        List<String> temp = new ArrayList<>();

        Node current = head;

        while (current != null)
        {
            temp.add(current.name);
            current = current.rightChild;
        }

        return temp;
    }

    public String displaySingle() {
        String temp = "";

        Node current = head;

        while (current != null)
        {
            temp += current.name + "\n";
            current = current.rightChild;
        }

        return temp;
    }
}

class DLNode {
    String value;
    DLNode prev, next;

    public DLNode() {
        value = null;
        prev = null;
        next = null;
    }

    public DLNode(String dataValue) {
        value = dataValue;
        prev = null;
        next = null;
    }
}
