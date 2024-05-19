package tftpexample;


public class SLList<T> {
    private SLLNode<T> head; // A pointer to the first node
    private SLLNode<T> tail; // A pointer to the last node

    // Default Constructor
    public SLList() {
        tail = head = null;
    }

    // Destructor
    public void finalize() {
        SLLNode<T> prtNode = head;
        while (prtNode != null) {
            prtNode = head.getNext();
            head = prtNode;
        }
    }

    // addToHead: creates a new node with val as info, and this new node is the new head
    public void addToHead(T val) {
        head = new SLLNode<T>(val, head);
        if (tail == null) {
            tail = head;
        }
    }

    // addToTail: creates a new node with val as info, and this new node is the new tail
    public void addToTail(T val) {
        SLLNode<T> newNode = new SLLNode<T>(val, null);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    // deleteFromHead: remove head from the list, the new head is the previous head-> next
    // if the list had only one node, head and tail point null
    public void deleteFromHead() {
        if (head != null) {
            SLLNode<T> tmp = head;
            head = head.getNext();
            if (head == null) {
                tail = null;
            }
            tmp = null;
        }
    }

    // deleteFromTail: removes the tail node from the list
    public void deleteFromTail() {
        if (head != null) {
            SLLNode<T> tmp = head;
            if (head != tail) {
                while (tmp.getNext() != tail) {
                    tmp = tmp.getNext();
                }
                tail = tmp;
                tmp = tmp.getNext();
                tail.setNext(null);
            } else {
                head = tail = null;
            }
            tmp = null;
        }
    }

    // isEmpty: returns true if the list is empty; otherwise, returns false
    public boolean isEmpty() {
        return head == null;
    }

    // printList: prints all nodes in the list
    public void printList() {
        SLLNode<T> current = head;
        while (current != null) {
            System.out.println(current.getInfo());
            current = current.getNext();
        }
        System.out.println();
    }

    // countNodes: returns the number of nodes in the list
    public int countNodes() {
        int count = 0;
        SLLNode<T> current = head;
        while (current != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }

    // getHead: returns the head of the list
    public SLLNode<T> getHead() {
        return head;
    }

    // sortInsert: creates a new node, and it is inserted sortedly
    public void sortInsert(T val) {
        SLLNode<T> newNode = new SLLNode<T>(val, null);

        if (isEmpty() || ((Comparable<T>) val).compareTo(head.getInfo()) < 0) {
            newNode.setNext(head);
            head = newNode;
            if (tail == null) {
                tail = newNode;
            }
        } else {
            SLLNode<T> current = head;
            SLLNode<T> prev = null;

            while (current != null && ((Comparable<T>) val).compareTo(current.getInfo()) >= 0) {
                prev = current;
                current = current.getNext();
            }

            prev.setNext(newNode);
            newNode.setNext(current);

            if (current == null) {
                tail = newNode;
            }
        }
    }
}