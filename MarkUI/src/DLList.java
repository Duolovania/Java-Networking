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
        }
        else {
            prev.rightChild = root;
            root.leftChild = prev;
        }
        prev = root; // Move previous to current

        // Process the right subtree
        convertBinaryTree(root.rightChild);
    }

    public void insertAtEnd(String newData) {
        Node newNode = new Node(newData);

        if (head == null)
        {
            head = newNode;
        }
        else
        {
            Node current = head;

            while (current.rightChild != null)
            {
                current = current.rightChild;
            }

            current.rightChild = newNode;
            newNode.leftChild = current;
        }
    }

    public Node find(String value) {
        Node current = head;

        while (current != null)
        {
            if (current.name.equals(value))
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

    public String display() {
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