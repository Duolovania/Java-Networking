import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BinaryTree.java
 *
 * This class handles the features and functionality of the binary tree.
 *
 * Version 1.00.
 * Author: Ryhan Khan.
 */
public class BinaryTree
{
    Node root;
    HashMap<String, List<String>> hashMap = new HashMap<>();

    /**
     * Generates a hashmap using the binary tree.
     *
     * @param focusNode the root of the hashmap.
     */
    public void genHashMap(Node focusNode)
    {
        if (focusNode == null) return;

        List<String> children = new ArrayList<String>();

        if (focusNode.leftChild != null) children.add(focusNode.leftChild.name); // Gets the left node.
        if (focusNode.rightChild != null) children.add(focusNode.rightChild.name); // Gets the right node.

        hashMap.put(focusNode.name, children); // Inserts binary tree nodes into the hashmap.

        // Recursively traverse the left and right subtrees.
        genHashMap(focusNode.leftChild);
        genHashMap(focusNode.rightChild);
    }

    /**
     * Displays the hashmap.
     *
     * @return a string containing all items.
     */
    public String hashDisplay() {
        // Loops through each item of the hashmap.
        String result = hashMap.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                .collect(Collectors.joining("\n"));

        return result;
    }

    /**
     * Displays the binary tree.
     *
     * @return a string containing all items.
     */
    public String display() {
        String temp = "";

        Node current = root; // Start at the root.

        // Loop while an entry exists.
        while (current != null)
        {
            temp += current.name + "\n"; // Adds the value of the node.
            current = current.rightChild; // Moves to the next node.
        }

        return temp;
    }

    /**
     * Adds an item to the binary tree.
     *
     * @param key the key of the new node.
     * @param name the value of the new node.
     */
    public void addNode(int key, String name) {
        // Create a new Node and initialize it
        Node newNode = new Node(key, name);

        // Checks if there is no node.
        if (root == null)
        {
            root = newNode; // Sets the new node as the root.
        }
        else
        {
            Node focusNode = root; // Starts at the root.
            Node parent; // Future parent for our new Node

            while (true)
            {
                // root is the top parent so we start
                // there
                parent = focusNode;

                // Check if the new node should go on
                // the left side of the parent node
                if (key < focusNode.key)
                {
                    // Switch focus to the left child
                    focusNode = focusNode.leftChild;

                    // If the left child has no children
                    if (focusNode == null)
                    {
                        // then place the new node on the left of it
                        parent.leftChild = newNode;
                        return; // All Done
                    }

                }
                else
                { // If we get here put the node on the right
                    focusNode = focusNode.rightChild;

                    // If the right child has no children
                    if (focusNode == null) {

                        // then place the new node on the right of it
                        parent.rightChild = newNode;
                        return; // All Done
                    }
                }
            }
        }
    }

    /**
     * Outputs each item using an in-order traversal.
     *
     * @param focusNode the root node.
     * @param output the text area that will be output to.
     */
    public void inOrderTraverseTree(Node focusNode, JTextArea output) {
        if (focusNode != null) {
            // Traverse the left node
            inOrderTraverseTree(focusNode.leftChild, output);
            output.append(focusNode.name + "\n");
            // Traverse the right node
            inOrderTraverseTree(focusNode.rightChild, output);
        }

    }

    /**
     * Outputs each item using a pre-order traversal.
     *
     * @param focusNode the root node.
     * @param output the text area that will be output to.
     */
    public void preorderTraverseTree(Node focusNode, JTextArea output) {
        if (focusNode != null) {
            output.append(focusNode.name + "\n");
            preorderTraverseTree(focusNode.leftChild, output);
            preorderTraverseTree(focusNode.rightChild, output);
        }
    }

    /**
     * Outputs each item using a post-order traversal.
     *
     * @param focusNode the root node.
     * @param output the text area that will be output to.
     */
    public void postOrderTraverseTree(Node focusNode, JTextArea output) {
        if (focusNode != null) {
            postOrderTraverseTree(focusNode.leftChild, output);
            postOrderTraverseTree(focusNode.rightChild, output);
            output.append(focusNode.name + "\n");
        }
    }

    /**
     * Searches the binary tree for a value
     *
     * @param focusNode the root node.
     * @param value the search term.
     * @return the result.
     */
    public Node findNode(Node focusNode, String value) {
        if (focusNode == null || focusNode.name.equals(value))
        {
            return focusNode;
        }

        // Recursively search left and right subtrees.
        Node foundNode = findNode(focusNode.leftChild, value);

        if (foundNode == null)
        {
            foundNode = findNode(focusNode.rightChild, value);
        }

        return foundNode;
    }
}

class Node {

    int key;
    String name;

    Node leftChild;
    Node rightChild;

    Node(int key, String name) {
        this.key = key;
        this.name = name;
    }

    Node(String name) {
        this.name = name;
    }

    public String toString() {

        return name + " has the key " + key;
    }
}
