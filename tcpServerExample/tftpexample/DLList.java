package tftpexample;

/**
 * DLList class: Represents a doubly linked list of books.
 */
public class DLList {
    private Node head; // Head of the doubly linked list
    private Node tail; // Tail of the doubly linked list

    // Constructor to initialize an empty doubly linked list
    public DLList() {
        head = null;
        tail = null;
    }

    // Inner class representing a node in the doubly linked list
    private class Node {
        Book book; // Book stored in the node
        Node prev; // Reference to the previous node
        Node next; // Reference to the next node

        // Constructor to initialize a node with a book
        public Node(Book book) {
            this.book = book;
        }
    }

    // Method to check if the doubly linked list is empty
    public boolean isEmpty() {
        return head == null;
    }

    // Method to insert a new book in sorted order based on the title
    public void insertSorted(Book myBook) {
        Node newNode = new Node(myBook);
        if (head == null) { // If the list is empty
            head = tail = newNode;
            newNode.next = newNode.prev = newNode; // Circular link
        } else if (myBook.getTitle().compareTo(head.book.getTitle()) <= 0) { // Insert before head
            newNode.next = head;
            newNode.prev = head.prev;
            head.prev.next = newNode;
            head.prev = newNode;
            head = newNode; // Update head
        } else if (myBook.getTitle().compareTo(tail.book.getTitle()) >= 0) {
            newNode.next = head;  // Point to the head to maintain circular nature
            newNode.prev = tail;
            tail.next = newNode;
            head.prev = newNode;  // Update head's previous to new node
            tail = newNode;       // Update tail to the new node
        } else { // Insert in the middle
            Node current = head;
            while (current.next != head && myBook.getTitle().compareTo(current.next.book.getTitle()) > 0) {
                current = current.next;
            }
            newNode.next = current.next;
            newNode.prev = current;
            current.next.prev = newNode;
            current.next = newNode;
        }
    }

    // Method to retrieve a book by its title
    public Book getBook(String title) {
        Node current = head;
        while (current != null) {
            if (current.book.getTitle().equals(title)) {
                return current.book;
            }
            current = current.next;
        }
        return null;
    }

    // Method to find the index of a specific book in the list
    public int findBook(Book book) {
        Node current = head;
        int index = 0;
        while (current != null) {
            if (current.book.equals(book)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    // Method to update a book at a specific index in the list
    public void setBook(int index, Book book) {
        Node current = head;
        int i = 0;
        while (current != null && i < index) {
            current = current.next;
            i++;
        }
        if (current != null) {
            current.book = book;
        }
    }

    // Method to convert the linked list of books to a string
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        int count = 0;
        final int BUFFER_SIZE = 1024; // Set the buffer size to 1024 bytes
        while (current != null && count < BUFFER_SIZE) {
            sb.append(current.book.toString()).append("\n");
            current = current.next;
            count++;
            if (current == head) { // Stop printing when reaching the head again
                break;
            }
        }
        return sb.toString();
    }

    // Method to print the contents of the doubly linked list
    public void printList() {
        if (head == null) {
            System.out.println("Empty list");
        } else {
            Node current = head;
            do {
                System.out.println(current.book);
                current = current.next;
            } while (current != head);
        }
    }

    // Method to search for a book by title
    public Book searchBook(String title) {
        Node current = head;
        do {
            if (current.book.getTitle().equals(title)) {
                return current.book;
            }
            current = current.next;
        } while (current != head);
        return null;
    }

    /**
     * getBookTitle: Gets the book by its title.
     *
     * @param title The title of the book to get.
     * @return The book with the given title, or null if no such book exists.
     */
    public String getBookTitle(String title) {
        if (isEmpty()) {
            return null;
        }
        // Set the current node to the head of the list
        Node current = head;

        // Iterate through the list until the end or until the book is found
        while (current != null) {
            // Check if the current book has the same title as the given title
            if (current.book.getTitle().equals(title)) {
                // Return the book if it is found
                return current.book.toString();
            }
            // Set the current node to the next node in the list
            current = current.next;
        }
        // Return null if the book is not found
        return null;
    }
}
