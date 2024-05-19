package tftpexample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**************************************************************************
 * 
 * @author Idalides Vergara
 * CPEN 457 - Programming Languages 
 * This class is design for handling client connections to the 
 * Trivial FTP server
 *
 *************************************************************************/
public class tftpHandler extends Thread {
    // Data stream and output streams for data transfer
    private DataInputStream clientInputStream;
    private DataOutputStream clientOutputStream;

    // Client socket for maintaining connection with the client
    private Socket clientSocket;

    // Data Structures
    private static final BSTree genreTree = new BSTree();

    public tftpHandler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            clientInputStream = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            clientOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        dataTransfer();
    }

    public void dataTransfer() {
        try {

            int readCommand;

            do {
                // Wait for command
                System.out.println("waiting for a command");
                readCommand = clientInputStream.readInt();
                //print command when received
                System.out.println("Received Command: " + readCommand);

                // option 9: exit command
                    //this option terminates the connection
                switch (readCommand) {
                    case tftpCodes.CLOSECONNECTION:
                        exitCommand();
                        break;

                        // option 1: add genre
                    //reads genre title from user and adds that genre to the genre tree
                    case tftpCodes.ADDGENRE:
                        addGenre();
                        break;

                    //option 3: modify book
                    //reads book title from user, sends book information, reads new price and stock quantity
                    //from user and updates the book with those parameters
                    case tftpCodes.MODIFYBOOK:
                        modifyBook();
                        break;

                        //option 4: 
                        //reads listGenre command from user, then sends all added genres to the client
                        //in alphabetical order
                    case tftpCodes.LISTALLGENRE:
                        listGenre();
                        break;

                        //reads book information from the client, then creates a book object with that
                        //infomation as parameters and adds the book to the circular doubly linked list
                        //located in the node of the specified genre
                    case tftpCodes.ADDBOOK:
                        addBook();
                        break;

                        //reads command from the user, then prints the content of the book list
                        //from each node
                    case tftpCodes.LISTALLBOOKBYGENRE:
                    listGenreBook();
                    break;

                        //reads genre title from the user, then prints the content of book list
                        //of the node with the same genre title 
                    case tftpCodes.LISTALLBOOKPARTGENRE:
                    listInputGenreBook();
                    break;

                    //reads book title from the user, then sends the information of the book with
                    //the same title as the user input
                    case tftpCodes.SEARCHBOOK:
                    searchBook();
                    break;

                    //reads book title from the user, then decrements the stock quantity
                    //of the book with the same title
                    case tftpCodes.BUYBOOK:
                    buyBook();
                    break;
    }
                }
             while (readCommand != tftpCodes.CLOSECONNECTION);

            // Close connection and socket
            clientInputStream.close();
            clientOutputStream.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method closes the connection with the client
     */
    private void exitCommand() {
        try {
            clientOutputStream.writeInt(tftpCodes.OK);
            clientOutputStream.flush();
            System.out.println("The connection has been closed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method reads the title of the genre that the user wishes to add from the client
     * and creates a node in the genreTree using the client's title as a parameter
     */
    public synchronized void addGenre() {

        byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
    
        System.out.println("addGenre Command");
    
        try {
    
            int read;
    
            // Write the OK: make acknowledgment of addGenre
            clientOutputStream.writeInt(tftpCodes.OK);
            clientOutputStream.flush();
    
            // Wait for genre title
            System.out.println("Waiting...");
            read = clientInputStream.read(buffer);
    
            // Print the genre title
            String genreTitle = new String(buffer).trim();
            System.out.println("Adding genre: " + genreTitle + " to bookstore...");
    
           

                // Insert genre into binary search tree
                // Create new DLList for genre
                DLList bookList = new DLList();
                genreTree.insert(genreTitle, bookList);
            
            //Send Ok to client
            clientOutputStream.writeInt(tftpCodes.OK);
            clientOutputStream.flush();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method receives the book details from the client and proceeds
     * to insert the book inside the book list in the node of the specified genre
     */
    public synchronized void addBook() {
        byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
        System.out.println("addBook Command");
    
        try {
            int read;
    
            // Write the OK: make acknowledgment of addBook
            clientOutputStream.writeInt(tftpCodes.OK);
            clientOutputStream.flush();
    
            // Wait for book details
            System.out.println("Waiting for book details...");
            read = clientInputStream.read(buffer);
    
            // Extract the book details received from the client
            String bookDetails = new String(buffer, 0, read).trim();
    
            // Parse the book details recieved from the client into their classifications
            String[] parts = bookDetails.split(",");
            if (parts.length >= 7) {
                String title = parts[0].trim(); //book title
                String genre = parts[1].trim(); //book genre
                String plot = parts[2].trim(); //book plot
                String bookAuthor = parts[3].trim(); //book author
                String publicationYear = parts[4].trim(); //book publication year
                String price = parts[5].trim(); //book price
                String quantity = parts[6].trim(); // quantity in stock
    
                // Search for the genre node in the binary search tree starting from the root
                BSTNode genreNode = genreTree.searchGenre(genreTree.root, genre);
    
                if (genreNode != null) { // Check if genre node exists and matches the genre
                    // Add the book to the DLList associated with the genre
                    Book book = new Book(title, plot, bookAuthor, Integer.parseInt(publicationYear), Double.parseDouble(price), Integer.parseInt(quantity));
                    genreNode.getBookList().insertSorted(book);
                    // Print the book added to the genre
                    System.out.print("Book: " + genreNode.getBookList().getBook(title) + " added to " + genreNode.getGenre());
                    System.out.println(" Book '" + book.getTitle() + "' has been successfully added to genre '" + genre + "'.");
                    genreNode.getBookList().printList(); // Print the list of books associated with the genre
                } else {
                    // Genre not found in the tree
                    System.out.println("Genre '" + genre + "' not found in the bookstore.");
                }
    
                // Send acknowledgment to the client
                clientOutputStream.writeInt(tftpCodes.OK);
                clientOutputStream.flush();
            } else {
                System.out.println("Invalid book details provided.");
                // Send error message to the client
                clientOutputStream.writeInt(tftpCodes.WRONGCOMMAND);
                clientOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method receives the book title from the server and proceeds 
     * to modify the book with the matching title
     */
    private synchronized void modifyBook(){
        try{
            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            int read;

            //Write OK to the client
            clientOutputStream.writeInt(tftpCodes.OK);
            clientOutputStream.flush();

            //Wait for client to specify which book to modify
            read = clientInputStream.read(buffer);
            String bookName = new String(buffer);
            bookName = bookName.trim();
            System.out.println("Client is requesting to modify book: " + bookName);
            
            /*
             *  Send the book details to the client
             */

            String bookDetails = genreTree.findTheBook(bookName).toString();

            System.out.println("Sending book information to client..");
            clientOutputStream.write(bookDetails.getBytes());
            clientOutputStream.flush();

            // Wait for client to confirm that he/she is ready to modify the book
            read = clientInputStream.readInt();

            // Wait for client confimation
            if(read == tftpCodes.OK){
                    System.out.println("Client is ready to modify book");

                    //read the new book information from the client
                    read = clientInputStream.read(buffer);
                    String modifyDetails = new String(buffer, 0, read);
                    modifyDetails.trim();

                    // Split the string to get the new price and new quantity
                    String[] modifyDetailsArray = modifyDetails.split(",");
                    double newPrice = Double.parseDouble(modifyDetailsArray[0]);
                    int newQuantity = Integer.parseInt(modifyDetailsArray[1].trim());

                    //Modify the book
                    System.out.println("Modifying book..");
                    Boolean modifyBook = genreTree.modifyBook(bookName, newPrice, newQuantity);

                    //If the modification was succesful, write OK to the client
                    if (modifyBook) {
                        // Write the OK: make acknowledgment of modifyBook
                        clientOutputStream.writeInt(tftpCodes.OK);
                        clientOutputStream.flush();
                        System.out.println("Book modified successfully");
                    }
                    else{ //if modification fails
                        // Write the ERROR: make acknowledgment of modifyBook
                        clientOutputStream.writeInt(tftpCodes.ITEMNOTFOUND);
                        clientOutputStream.flush();
                        System.out.println("Book modification failed");
                    }
                }else{ //if client denies modification prompt
                    System.out.println("Client cancelled modification");
                }
        }catch(Exception e){
            System.out.println("Error in modifyBook");
            e.printStackTrace();
        }
    }

    /**
     * This method receives a book title. The server then proceeds to decrement
     * the quantity in stock for that book.
     */
    private synchronized void buyBook(){
        try{

            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            int read;

            //Write Ok to the client
            clientOutputStream.writeInt(tftpCodes.OK);
            clientOutputStream.flush();

            //Wait for client to specify which book to modify
            read = clientInputStream.read(buffer);
            String bookName = new String(buffer);
            bookName = bookName.trim();
            System.out.println("Client is requesting to buy book: " + bookName);
            
            /*
             *  Use the tree to get the book details
             *  Send the book details to the client
             */

             //Search for book in the tree
           String bookDetails = genreTree.findTheBook(bookName).toString();

            //Send book information to the client
                System.out.println("Sending book information to client..");
                clientOutputStream.write(bookDetails.getBytes());
                clientOutputStream.flush();

                //Read OK from client
                read = clientInputStream.readInt();

                // Wait for client confimation
                if(read == tftpCodes.OK){
                    System.out.println("Client is ready to buy book");

                    //Write OK to client
                    clientOutputStream.writeInt(tftpCodes.OK);
                    clientOutputStream.flush();
            
                    System.out.println("Buying book..");

                    //Buy the book
                    Boolean modifyBook = genreTree.buyBook(bookName);

                    //if book purchased succesfully
                    if (modifyBook) {
                        // Write the OK: make acknowledgment of modifyBook
                        clientOutputStream.writeInt(tftpCodes.OK);
                        clientOutputStream.flush();
                        System.out.println("Book purchased successfully");
                    }
                    else{ //if purchased failed
                        // Write the ERROR: make acknowledgment of modifyBook
                        clientOutputStream.writeInt(tftpCodes.ITEMNOTFOUND);
                        clientOutputStream.flush();
                        System.out.println("Book purchase failed");
                    }
                }else{ //client denies purchase prompt
                    System.out.println("Client cancelled purchase");
                }
        }catch(Exception e){
            System.out.println("Error in modifyBook");
            e.printStackTrace();
        }
    }
    
    /**
     * This method receives the LISTALLGENRE command from the client and proceeds
     * to send the genre title in each node 
     */
    private synchronized void listGenre() {
        try {
            int read;
            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            // Write the OK: make acknowledgment of listGenre
             //clientOutputStream.writeInt(tftpCodes.OK);
            //clientOutputStream.flush();

            System.out.println("Listing All Genres: ");

            
                // get genre list and store it in a string
                String genreList = genreTree.getGenreList();
                System.out.println(genreList);

                buffer = genreList.getBytes();

                //Send list to client
                clientOutputStream.write(buffer);
                clientOutputStream.flush();
            
            //Read OK from client
            read = clientInputStream.readInt();
            if (read == tftpCodes.OK) {
                System.out.println("Genres listed to Client.");
            } else {
                System.out.println("Genres weren't listed to Client.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads the genre title from the client and sends the book list
     * information.
     */
    private synchronized void listGenreBook() {
        try {
            int read;
            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            // Write the OK: make acknowledgment of listGenre
             //clientOutputStream.writeInt(tftpCodes.OK);
             //clientOutputStream.flush();
    
            System.out.println("Listing All Genres and Books: \n ");
    
            
                // get genrelist and store it in a string
                String genreBookList = genreTree.getGenreListWithBooks();
                System.out.println(genreBookList);
    
                buffer = genreBookList.getBytes();
    
                //Send book list to client
                clientOutputStream.write(buffer);
                clientOutputStream.flush();
            
            //Read OK from client
            read = clientInputStream.readInt();
            if (read == tftpCodes.OK) {
                System.out.println("Genres and books listed to Client.");
            } else {
                System.out.println("Genres and books weren't listed to Client.");
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/**
 * this method receives a genre title from the client and sends back
 * the book list associated with that genre.
 */
 private synchronized void listInputGenreBook() {
        try {
            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            // Read genre title from client
            int read = clientInputStream.read(buffer);
            String genreTitle = new String(buffer).trim();
            System.out.println("Listing books in genre: " + genreTitle);
    
            // Search for books in genre
            String booksByGenre = genreTree.getBooksByGenre(genreTitle);
    
            // Send list of books back to client
            if (!booksByGenre.equals("Genre not found or no books available in this genre")) {
                buffer = booksByGenre.getBytes();
                clientOutputStream.writeInt(buffer.length);
                clientOutputStream.write(buffer);
                clientOutputStream.flush();
            }
    
            //clientOutputStream.writeInt(tftpCodes.OK);
           // clientOutputStream.flush();

           //Read OK from client
           read = clientInputStream.readInt();
           if (read == tftpCodes.OK) {
               System.out.println("Genre books listed to Client.");
           } else {
               System.out.println("Genre books weren't listed to Client.");
           }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * this method receives a book title from the client and sends back the 
 * information associated with that book
 */
    public synchronized void searchBook() {
        try {
            // Send OK to client
            clientOutputStream.writeInt(tftpCodes.OK);
            clientOutputStream.flush();
    
            // Read book title from client
            System.out.println("Waiting for book title...");
            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            int read = clientInputStream.read(buffer);
            System.out.println("Bytes read: " + read);
            String bookName = new String(buffer,0,read).trim();
            System.out.println("Book title received: " + bookName);
    
            //Get book information
            String bookDetails = genreTree.findTheBook(bookName);
            if (bookDetails.isEmpty())
                System.out.println("Book not found.");
    
            //if (bookDetails != null) {
                // Print book details in server
                System.out.println("Book details: " + bookDetails);
    
                // Convert book details to bytes and send to client
                clientOutputStream.writeUTF(bookDetails);
                clientOutputStream.flush();
                bookDetails = null;
    
                //Read OK from client
                read = clientInputStream.readInt();
                if (read == tftpCodes.OK) {
                    System.out.println("Book found");
                    // Send OK to client
                clientOutputStream.writeInt(tftpCodes.OK);
                clientOutputStream.flush();

                } else {
                    System.out.println("Book not found.");
                }
    
                // Send OK to client
                //clientOutputStream.writeInt(tftpCodes.OK);
                //clientOutputStream.flush();
           // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}