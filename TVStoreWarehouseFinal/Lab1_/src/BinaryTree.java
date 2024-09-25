/**
 * @author Obinna Elemuo	
 * CMSY256 - Fall 2023
 * 11/2/2023
 * BinaryTree class manages the operations on a binary tree, including insertion, traversal, and search.
 * The binary tree is organized based on the price of TVType objects, which is to be utilized in the driver class. 
 * @version 1.0
 */
public class BinaryTree {
	// Root node of the binary tree
	private Node root; 
	// Flag used during search operations
    private static boolean found; 

    // default constructor initializing the root to null
    public BinaryTree() {
        root = null; 
    }

    // Getter and Setter for root node
    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
    // reset boolean flag to prepare for a new search
    public void resetFlag() {
        found = false; // Reset the flag before search
    }
    
    // inserts a new node into binary tree based on price of TV
    public void insert(TVType tvType) {
        root = insertRecursive(root, tvType);
    }
    // method recursively inserts
    private Node insertRecursive(Node current, TVType tvType) {
        if (current == null) {
            return new Node(tvType);
        }

        if (tvType.getPrice() < current.getData().getPrice()) {
            current.setLeft(insertRecursive(current.getLeft(), tvType));
        } else if (tvType.getPrice() > current.getData().getPrice()) {
            current.setRight(insertRecursive(current.getRight(), tvType));
        }

        return current;
    }
    // performs an inorder traversal of binary tree, displays TV types
    public void inorderTraversal() {
        inorderRecursive(root);
    }
    // method preforms an inorder traversal to node the current node
    private void inorderRecursive(Node node) {
        if (node != null) {
            inorderRecursive(node.getLeft());
            System.out.println(node.getData());
            inorderRecursive(node.getRight());
        }
    }
    // searches the tree with a node for the given TV brand and node
    public TVType search(String brand, String model) {
        return searchRecursive(root, brand, model);
    }
    
    // recursively search for the node with the given TV brand and model.
    private TVType searchRecursive(Node current, String brand, String model) {
        if (current == null) {
            return null;
        }

        TVType tvType = current.getData();
        if (brand.equals(tvType.getBrand()) && model.equals(tvType.getModel())) {
            return tvType;
        }

        TVType leftSearch = searchRecursive(current.getLeft(), brand, model);
        if (leftSearch != null) return leftSearch;

        return searchRecursive(current.getRight(), brand, model);
    }

    
}
