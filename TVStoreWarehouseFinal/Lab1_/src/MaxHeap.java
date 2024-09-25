/**
 * @author Obinna Elemuo	
 * CMSY256 - Fall 2023
 * 12/07/2023
 * Class which holds all of the methods used to create, insert, and remove elements from the heap
 * Uses instance variables for MaxHeaps objects such as an int to hold maximum and actual heap
 * size.
 * @version 1.0
 */
public class MaxHeap {
    // Array to store the heap elements
    private DelInfo[] heap;

    // Maximum size of the heap
    private int maxSize;

    // Current number of elements in the heap
    private int size;

    // Constructor to initialize the heap with a maximum size
    public MaxHeap(int maxSize) {
        this.maxSize = maxSize;
        this.size = 0;
        heap = new DelInfo[this.maxSize + 1]; 
    }

    // Getter method to return the current size of the heap
    public int getSize() {
        return size;
    }

    // Helper method to get the parent's position for a given position
    private int parent(int pos) {
        return pos / 2;
    }

    // Helper method to get the left child's position for a given position
    private int leftChild(int pos) {
        return (2 * pos);
    }

    // Helper method to get the right child's position for a given position
    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }

    // Helper method to check if a given position is a leaf node
    private boolean isLeaf(int pos) {
        return pos > (size / 2) && pos <= size;
    }

    // Method to swap two nodes in the heap
    private void swap(int fpos, int spos) {
        DelInfo tmp = heap[fpos];
        heap[fpos] = heap[spos];
        heap[spos] = tmp;
    }

    // Method to insert a new element into the heap
    public void insert(DelInfo element) {
        heap[++size] = element;
        int current = size;

        // Bubble up the new element as long as it's larger than its parent
        while (current > 1 && heap[current].getNumberOfTvs() > heap[parent(current)].getNumberOfTvs()) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Method to maintain the max heap property from a given position
    public void maxHeapify(int pos) {
        if (!isLeaf(pos)) {
            // Check if the current node is smaller than either of its children
            if (heap[pos].getNumberOfTvs() < heap[leftChild(pos)].getNumberOfTvs() || 
                heap[pos].getNumberOfTvs() < heap[rightChild(pos)].getNumberOfTvs()) {

                // Swap with the larger child and continue heapifying down
                if (heap[leftChild(pos)].getNumberOfTvs() > heap[rightChild(pos)].getNumberOfTvs()) {
                    swap(pos, leftChild(pos));
                    maxHeapify(leftChild(pos));
                } else {
                    swap(pos, rightChild(pos));
                    maxHeapify(rightChild(pos));
                }
            }
        }
    }

    // Method to remove and return the root element of the heap
    public DelInfo remove() {
        DelInfo popped = heap[1];
        heap[1] = heap[size--];
        maxHeapify(1);
        return popped;
    }

    // Method to balance the entire heap
    public void maxHeap() {
        // Start from the middle of the heap and heapify each node
        for (int pos = (size / 2); pos >= 1; pos--) {
            maxHeapify(pos);
        }
    }
}

