import java.util.ArrayList;
import java.util.List;

/**
 * DLList.java
 *
 * This class handles the features and functionality of the doubly linked list.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
public class DLList {

    Node head;

    /**
     * Adds an item to the end of the doubly linked list.
     *
     * @param newData the data that will be inserted.
     */
    public void insertAtEnd(String newData) {
        Node newNode = new Node(newData); // Creates a new node.

        // Checks if the root exists.
        if (head == null)
        {
            head = newNode; // Sets the new node as the root.
        }
        else
        {
            Node current = head; // Starts at the root.

            // Loops through each node until the end.
            while (current.rightChild != null)
            {
                current = current.rightChild; // Moves to the next node.
            }

            current.rightChild = newNode; // Inserts the new node to the end.
            newNode.leftChild = current; // Sets the previous node to the node right before.
        }
    }

    /**
     * Searches the doubly linked list for an entry.
     *
     * @param value the entry that will be searched for.
     */
    public Node find(String value) {
        Node current = head; // Starts at the root node.

        // Loops while an entry exists.
        while (current != null)
        {
            // Checks if the node matches.
            if (current.name.equals(value))
            {
                return current; // Returns the node.
            }
            else
            {
                current = current.rightChild; // Moves to the next node.
            }
        }

        return null;
    }

    /**
     * Generates the doubly linked list as a single string.
     *
     * @return each node of the doubly linked list.
     */
    public String display() {
        String temp = "";

        Node current = head; // Starts at the root.

        // Loops while an entry exists.
        while (current != null)
        {
            temp += current.name + "\n"; // Adds the value of the node.
            current = current.rightChild; // Moves to the next node.
        }

        return temp;
    }
}