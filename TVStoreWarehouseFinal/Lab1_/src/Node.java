/**
 * @author Obinna Elemuo	
 * CMSY265 - Fall 2023
 * 12/07/2023
 * Node for a binary tree holding a TVType object, with pointers to left and right child nodes.
 * Initialized with TVType data; left and right pointers are null by default.
 * @version 1.0
 */
public class Node {
	// The TVType object contained in the node
    private TVType data;
    // left subtree
    private Node left; 
    // right subtree
    private Node right; 
    // Constructs a Node with specified TVType object 
    public Node(TVType data) {
        this.data = data; 
        this.left = null; 
        this.right = null; 
    }
    // Getters and setters for Node
    public TVType getData() {
        return data;
    }
    
    public void setData(TVType data) {
        this.data = data; 
    }

    public Node getLeft() {
        return left;
    }
   
    public void setLeft(Node left) {
        this.left = left; 
    }
    
    public Node getRight() {
        return right;
    }
    
    public void setRight(Node right) {
        this.right = right; 
    }
}
