import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BinaryTree
{
    Node root;
    HashMap<String, List<String>> hashMap = new HashMap<>();

    public void genHashMap(Node focusNode)
    {
        if (focusNode == null) return;

        List<String> children = new ArrayList<String>();
        if (focusNode.leftChild != null) children.add(focusNode.leftChild.name);
        if (focusNode.rightChild != null) children.add(focusNode.rightChild.name);

        hashMap.put(focusNode.name, children);

        // Recursively traverse the left and right subtrees
        genHashMap(focusNode.leftChild);
        genHashMap(focusNode.rightChild);
    }

    public String hashDisplay() {
        String result = hashMap.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                .collect(Collectors.joining("\n"));

        return result;
    }

    public String display() {
        String temp = "";

        Node current = root;

        while (current != null)
        {
            temp += current.name + "\n";
            current = current.rightChild;
        }

        return temp;
    }


    public void addNode(int key, String name) {

        // Create a new Node and initialize it
        Node newNode = new Node(key, name);

        // If there is no root this becomes root
        if (root == null)
        {
            root = newNode;
        }
        else
        {
            // Set root as the Node we will start
            // with as we traverse the tree
            Node focusNode = root;

            // Future parent for our new Node
            Node parent;

            while (true) {
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

    // All nodes are visited in ascending order
    // Recursion is used to go to one node and
    // then go to its child nodes and so forth
    public void inOrderTraverseTree(Node focusNode, JTextArea output) {
        if (focusNode != null) {
            // Traverse the left node
            inOrderTraverseTree(focusNode.leftChild, output);
            output.append(focusNode.name + "\n");
            // Traverse the right node
            inOrderTraverseTree(focusNode.rightChild, output);
        }

    }

    public void preorderTraverseTree(Node focusNode, JTextArea output) {
        if (focusNode != null) {
            output.append(focusNode.name + "\n");
            preorderTraverseTree(focusNode.leftChild, output);
            preorderTraverseTree(focusNode.rightChild, output);
        }
    }

    public void postOrderTraverseTree(Node focusNode, JTextArea output) {
        if (focusNode != null) {
            postOrderTraverseTree(focusNode.leftChild, output);
            postOrderTraverseTree(focusNode.rightChild, output);
            output.append(focusNode.name + "\n");
        }
    }

    public Node findNode(int key) {
        // Start at the top of the tree
        Node focusNode = root;

        // While we haven't found the Node
        // keep looking
        while (focusNode.key != key) {
            // If we should search to the left
            if (key < focusNode.key) {
                // Shift the focus Node to the left child
                focusNode = focusNode.leftChild;
            } else {
                // Shift the focus Node to the right child
                focusNode = focusNode.rightChild;
            }

            // The node wasn't found
            if (focusNode == null)
                return null;
        }

        return focusNode;
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

        /*
         * return name + " has the key " + key + "\nLeft Child: " + leftChild +
         * "\nRight Child: " + rightChild + "\n";
         */

    }
}
