// Here's the continuation of the BSTNode class with additional comments:

package tftpexample;

/**
 * BSTNode class: Represents a node in a binary search tree, storing genre information and a linked list of books.
 */
public class BSTNode {
    String genreTitle; // Genre title
    BSTNode leftChild; // Left child node
    BSTNode rightChild; // Right child node
    DLList bookList; // Linked list of books

    /**
     * Constructor to initialize a BSTNode with genre and book list.
     * @param genre Genre title
     * @param list Linked list of books
     */
    public BSTNode(String genre, DLList list) {
        this.genreTitle = genre;
        this.bookList = list;
        leftChild = null;
        rightChild = null;
    }

    /**
     * Getter method to retrieve the genre title.
     * @return The genre title
     */
    public String getGenre() {
        return genreTitle;
    }

    /**
     * Setter method to set the genre title.
     * @param genre The genre title to set
     */
    public void setGenre(String genre) {
        this.genreTitle = genre;
    }

    /**
     * Getter method to retrieve the left child node.
     * @return The left child node
     */
    public BSTNode getLeftChild() {
        return leftChild;
    }

    /**
     * Setter method to set the left child node.
     * @param leftChild The left child node to set
     */
    public void setLeftChild(BSTNode leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Getter method to retrieve the right child node.
     * @return The right child node
     */
    public BSTNode getRightChild() {
        return rightChild;
    }

    /**
     * Setter method to set the right child node.
     * @param rightChild The right child node to set
     */
    public void setRightChild(BSTNode rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * Getter method to retrieve the linked list of books.
     * @return The linked list of books
     */
    public DLList getBookList() {
        return bookList;
    }
}
