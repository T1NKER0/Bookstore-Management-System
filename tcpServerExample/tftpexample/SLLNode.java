package tftpexample;

public class SLLNode<T> {
    private T info;           // actual data
    private SLLNode<T> next; // Points to the next node in the list

    // Default Constructor
    public SLLNode() {
        // Initialize the 'next' pointer
        // to null to signify no next node initially
        next = null;
    }

    // Special Constructor:
    // Takes value of type T and pointer to next node as parameters
    public SLLNode(T i, SLLNode<T> n) {
        // Initialize 'info' with the provided data value 'i'
        info = i;
        // Initialize 'next' with the provided next node pointer 'n'
        next = n;
    }

    public T getInfo() {
        // Return data from node
        return info;
    }

    public SLLNode<T> getNext() {
        // Return Pointer to next node
        return next;
    }

    public void setInfo(T i) {
        // Set the data of the node to the provided value 'i'
        info = i;
    }

    public void setNext(SLLNode<T> n) {
        // Set the 'next' pointer to the provided pointer 'n'
        next = n;
    }
}
