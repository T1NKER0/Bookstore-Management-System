package tftpexample;

import java.util.Stack;

/**
 * BSTree class: Represents a binary search tree for organizing genres and their associated book lists.
 */
public class BSTree {

    BSTNode root;

    // Constructor that initializes the tree
    public BSTree() {
        root = null;
    }

    /*
     * isEmpty method: this method checks if the tree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /*
     * insert method: this method inserts a genre into the tree
     */
    public void insert(String genre, DLList bookList) {
        root = recursiveInsert(root, genre, bookList);
    }

    private BSTNode recursiveInsert(BSTNode node, String genre, DLList bookList) {

        if (node == null) {
            // Create a new node if the current node is null
            BSTNode newNode = new BSTNode(genre, bookList);
            return newNode;
        }

        // Genre already exists in the tree
        if (genre.equals(node.genreTitle)) {
            node.bookList = bookList;
            return node;
        }
        // Insert the genre into the left subtree
        if (genre.compareTo(node.genreTitle) < 0) {
            // Recursively insert into the left subtree
            node.leftChild = recursiveInsert(node.leftChild, genre, bookList);
        } else {
            // Recursively insert into the right subtree
            node.rightChild = recursiveInsert(node.rightChild, genre, bookList);
        }

        // Return the current node
        return node;
    }

    /*
     * getGenreList method: this method gets all the genres in the bookstore
     */
    public String getGenreList() {

        if (isEmpty()) {
            return "No genres in bookstore";
        }
        return getGenreRecursive(root);
    }

    /*
     * getGenreRecursive method: this method gets the genres from the bookstore
     */
    public String getGenreRecursive(BSTNode root) {

        String genre = "";
        if (root != null) {
            genre += getGenreRecursive(root.leftChild);
            genre += root.genreTitle + " ";
            genre += getGenreRecursive(root.rightChild);
        }

        return genre;
    }

    /*
     * getGenreListWithBooks method: This method retrieves all the genres in the bookstore along with their associated books.
     */
    public String getGenreListWithBooks() {
        // Check if the tree is empty
        if (isEmpty()) {
            return "No genres in the bookstore";
        }

        // Create a StringBuilder to construct the output string
        StringBuilder sb = new StringBuilder();

        // Start traversal from the root
        BSTNode node = root;

        // Create a stack for iterative traversal
        Stack<BSTNode> stack = new Stack<>();

        // Traverse the tree in inorder
        while (!stack.empty() || node != null) {
            if (node != null) {
                // Push current node and move to its left child
                stack.push(node);
                node = node.leftChild;
            } else {
                // Pop the node and process it
                node = stack.pop();

                // Append genre title to the output
                sb.append(node.genreTitle).append(":\n");

                // Check if the book list associated with the genre is not empty
                if (node.bookList != null) {
                    sb.append(node.bookList.toString()).append("\n");
                } else {
                    // If the book list is empty, indicate that
                    sb.append("Empty list\n");
                }

                // Move to the right child
                node = node.rightChild;
            }
        }

        // Return the constructed string
        return sb.toString();
    }

    /**
     * Modify the book price and quantity in stock
     *
     * @param title       //book title
     * @param newPrice    //new book price
     * @param newQuantity //new stock quantity
     * @return //modification success status
     */
    public boolean modifyBook(String title, double newPrice, int newQuantity) {
        // Start traversal from the root
        BSTNode current = root;

        // Traverse the tree until the desired node is found or until the end of the tree
        while (current != null) {
            // Compare the title with the title of the current node's book list
            int compare = title.compareTo(current.bookList.getBook(title).getTitle());

            // If title is less than current node's title, move to the left child
            if (compare < 0) {
                current = current.leftChild;
            }
            // If title is greater than current node's title, move to the right child
            else if (compare > 0) {
                current = current.rightChild;
            }
            // If titles match, modify the book
            else {
                // Get the book to modify from the current node's book list
                Book modifyBook = current.bookList.getBook(title);

                // If the book exists in the book list
                if (modifyBook != null) {
                    // Find the index of the book in the book list
                    int index = current.bookList.findBook(modifyBook);

                    // If the book is found in the book list
                    if (index != -1) {
                        // Update the book's price and quantity
                        modifyBook.setPrice(newPrice);
                        modifyBook.setQuantityInStock(newQuantity);

                        // Replace the modified book in the book list
                        current.bookList.setBook(index, modifyBook);

                        // Return true to indicate successful modification
                        return true;
                    }
                }
                // If the book is not found in the book list, return false
                return false;
            }
        }
        // If the title is not found in the tree, return false
        return false;
    }

    /**
     * buyBook method: This method attempts to purchase a book with the given title from the bookstore.
     * If the book is found and its quantity is greater than 0, it decreases the quantity by 1 and returns true, indicating successful purchase.
     * If the book is not found or its quantity is already 0, it returns false.
     */
    public boolean buyBook(String title) {
        BSTNode current = root;
        while (current != null) {
            int compare = title.compareTo(current.bookList.getBook(title).getTitle());
            if (compare < 0) {
                current = current.leftChild;
            } else if (compare > 0) {
                current = current.rightChild;
            } else {
                Book modifyBook = current.bookList.getBook(title);
                if (modifyBook != null) {
                    int index = current.bookList.findBook(modifyBook);
                    if (index != -1) {
                        int newQuantity = modifyBook.getQuantityInStock() - 1;

                        modifyBook.setQuantityInStock(newQuantity);
                        current.bookList.setBook(index, modifyBook);
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * searchGenre method: This method searches for a genre node with the given title in the binary search tree.
     * If the genre node is found, it returns the node. If not found, it creates a new node for the genre and returns it.
     */
    public BSTNode searchGenre(BSTNode node, String genre) {
        if (node == null) {
            // Create a new node for the genre
            DLList list = new DLList();
            return new BSTNode(genre, list);
        }

        int comparison = genre.compareTo(node.genreTitle);
        if (comparison == 0) {
            return node; // found
        }

        if (comparison < 0) {
            if (node.leftChild == null) {
                // Create a new node for the genre
                DLList list = new DLList();
                node.leftChild = new BSTNode(genre, list);
                return node.leftChild;
            } else {
                return searchGenre(node.leftChild, genre);
            }
        } else {
            if (node.rightChild == null) {
                // Create a new node for the genre
                DLList list = new DLList();
                node.rightChild = new BSTNode(genre, list);
                return node.rightChild;
            } else {
                return searchGenre(node.rightChild, genre);
            }
        }
    }

    /**
     * getBooksByGenre method: This method retrieves all the books belonging to a specific genre.
     * It searches for the genre node with the given title and returns the list of books in that genre.
     * If the genre is not found or there are no books available in that genre, it returns an appropriate message.
     */
    public String getBooksByGenre(String genreTitle) {
        BSTNode genreNode = searchGenre(root, genreTitle); // Search for the genre node
        if (genreNode == null || genreNode.bookList == null) {
            return "Genre not found or no books available in this genre";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(genreTitle).append(":\n");
        sb.append(genreNode.bookList.toString()).append("\n");

        return sb.toString();
    }

    /**
     * findTheBook method: This method searches for a book with the given title in the bookstore.
     * It performs a preorder traversal of the binary search tree and returns information about the book if found.
     * If the book is not found, it returns null.
     */
    public String findTheBook(String title) {
        return findTheBookRecursive(root, title);
    }

    /**
     * findTheBookRecursive method: This is a helper method for findTheBook method.
     * It performs a preorder traversal of the binary search tree and searches for the book with the given title.
     * If the book is found, it returns information about the book. Otherwise, it returns null.
     */
    private String findTheBookRecursive(BSTNode currentNode, String title) {
        if (currentNode == null) {
            return null;
        }

        Book book = currentNode.getBookList().getBook(title);
        String bookInfo = book.toString();
        if (bookInfo != null) {
            return bookInfo;
        }

        bookInfo = findTheBookRecursive(currentNode.leftChild, title);
        if (bookInfo != null) {
            return bookInfo;
        }

        return findTheBookRecursive(currentNode.rightChild, title);
    }
}
