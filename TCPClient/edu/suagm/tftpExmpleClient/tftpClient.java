package edu.suagm.tftpExmpleClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

//import tftpexample.Book;


/**************************************************************************
 * 
 * @author Idalides Vergara
 * CPEN 457 - Programming Languages 
 * This class is the Trivial FTP client
 * Fall 2017 
 *
 *************************************************************************/
public class tftpClient {

    // List of valid commands on the protocol
    private enum tftpValidCommands {
        exit, addGenre, addBook, modifyBook, listGenre, listGenreBook, 
        listInputGenreBook, searchBook
    }

    // Current command
    private int currentCommand;

    // Data stream and output streams for data transfer
    private DataInputStream socketInputStream;
    private DataOutputStream socketOutputStream;

    // Connection parameters
    private String serverAddressStr;
    private int serverPort;

    public static void main(String args[]) {
        // Extract port and IP address from the arguments
        String serverAddressStr = args[0];
        int serverPort = Integer.parseInt(args[1]);
        System.out.println("Connecting to " + serverAddressStr + " Through " + serverPort);

        // Create the client object
        tftpClient clientObj = new tftpClient(serverAddressStr, serverPort);

        // Perform data transfer
        clientObj.dataTransfer();
    }

    public tftpClient(String serverAddressStr, int serverPort) {
        this.serverAddressStr = serverAddressStr;
        this.serverPort = serverPort;
    }


    private static String readKeyboard(Scanner reader){
        return reader.nextLine();
    }

    private void dataTransfer() {
        String arguments = "";
    
        // Scanner for reading commands from the keyboard
        Scanner reader = new Scanner(System.in);
    
        try {
    
            // Converting canonical IP address into InetAddress
            InetAddress serverAddress = InetAddress.getByName(serverAddressStr);
    
            // Create a client socket and connect to the Server
            Socket socket = new Socket(serverAddress, serverPort);
    
            // Create the Data Stream for data transfer
            socketInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            socketOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            System.out.println("Connected to the server");
    
            int command; // declare command variable here
            do {
                //Print the client menu
                String menu = "\n--BOOKSTORE MENU--\n" +
                        "(Please select a valid option)\n" +
                        "1. Add Genre.\n" +
                        "2. Add Book.\n" +
                        "3. Modify a Book in stock.\n" +
                        "4. List All Genres.\n" +
                        "5. List all Books by genre.\n" +
                        "6. List all Books for a particular genre.\n" +
                        "7. Search for a Book.\n" +
                        "8. Buy a Book.\n" +
                        "9. Exit.\n" +
                        "Option: ";
    
                System.out.println(menu);
    
                // Read next command
                command = readCommands(reader);
    
                //Execute commands
                switch (command) {
    
                    // option 1: add genre
                    //the user enters the title of the genre they wish to add
                    case 1:
                        System.out.print("Enter genre to the bookstore: ");
                        String genre = readKeyboard(reader);
                        addGenre(genre);
                        break;
    
                    // option 2: add book
                    //the user enters the information of the book they wish to add
                    case 2:
                        String bookDetails = "";
                        System.out.print("Enter book title: ");
                        bookDetails += readKeyboard(reader);
                        System.out.print("Select one of the following genres in the bookstore: ");
                        listGenre();
                        System.out.print("Enter genre: ");
                        bookDetails += "," + readKeyboard(reader);
                        System.out.print("Enter book's synopsis: ");
                        bookDetails += "," + readKeyboard(reader);
                        System.out.print("Enter author name: ");
                        bookDetails += "," + readKeyboard(reader);
                        System.out.print("Enter release year: ");
                        bookDetails += "," + readKeyboard(reader);
                        System.out.print("Enter price of book: $");
                        bookDetails += "," + readKeyboard(reader);
                        System.out.print("Enter quantity in stock: ");
                        bookDetails += "," + readKeyboard(reader);
                        addBook(bookDetails);
                        break;
    
                    // option 3: modify book
                    //the user enters the title of the book they wish to modify
                    //they can only modify stock quantity and price
                    case 3:
                        modifyBook();
                        break;
    
                    // option 4: list all genres
                    //all added genres are printed in alphabetical order
                    case 4:
                        listGenre();
                        break;
    
                    // option 5: list all books by genre
                    //all genres and all books associated with each genre are listed in alphabetical order
                    case 5:
                        listGenreBook();
                        break;
    
                    // option 6: list all books for selected genre
                    //all books associated with the selected genre are printed in alphabetical order
                    case 6:
                        System.out.print("Enter genre title: ");
                        String genreTitle = reader.nextLine();
                        listInputGenreBook(genreTitle);
                        break;
    
                    //option 7: search for a book
                    //the user enters the book title and the method returns the information of that book
                    case 7:
                        System.out.print("Enter book title: ");
                        String bookTitle = readKeyboard(reader);
                        searchBook(bookTitle);
                        break;
                    //option 8: buy a book
                    //the user enters the book title, and if they confirm the purchase, the book stock quantity
                    //is reduced by 1 
                    case 8:
                        buyBook();
                    break;
    
                    // option 9: exit command
                    //this option terminates the connection
                    case 9:
                        exitCommand();
                        break;
    
                    // Wrong command
                    default:
                        System.out.println("Invalid option. Please select a valid option.");
                        break;
                }
    
            } while (command != 9);
    
            // Close scanner
            reader.close();
            reader = null;
    
            // Close socket and connections
            socketOutputStream.close();
            socketInputStream.close();
            socket.close();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method gets the clients genre title input and sends it to the server
     * It then receives the server confirmation specifying if the genre was succesfully
     * added or if the genre insertion failed 
     * @param genre //this is the genre title
     */
private synchronized void addGenre(String genre) {
        try {
            //reads information from the client
            int read;

            // Sends the addGenre commmand
            socketOutputStream.writeInt(tftpCodes.ADDGENRE);
            socketOutputStream.flush();

            // Server sends OK to Client
            read = socketInputStream.readInt();

            //if OK is received, then the client sends the genre title to the server
            if (read == tftpCodes.OK) {
                socketOutputStream.write(genre.getBytes());
                socketOutputStream.flush();
                System.out.print("Adding " + genre + " to bookstore");
                System.out.println("\nWaiting for confirmation");

                // Server sends OK to Client
                read = socketInputStream.readInt();

                //if OK is received, we confirm that the genre has been added
                if (read == tftpCodes.OK) {
                    System.out.print(genre + " added.\n");
                }
                // Server sends EXISTINGITEM to Client
                else if (read == tftpCodes.EXISTINGITEM) {
                    System.out.print("The genre exists already in the bookstore.\n");
                } else { //if transaction failed then:
                    System.out.print("Error adding genre.\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**This method receives the book information from the user and sends it
     * to the server
     * 
     * @param bookDetails //book information 
     */
   private synchronized void addBook(String bookDetails) {
        try {
            int read;
    
            // Sends the ADDBOOK command to the server
            socketOutputStream.writeInt(tftpCodes.ADDBOOK);
            socketOutputStream.flush();
    
            // Server sends OK to Client
            read = socketInputStream.readInt();
            if (read == tftpCodes.OK) {
                // Send the book details to the server
                socketOutputStream.writeBytes(bookDetails); // Use writeUTF to send string data
                socketOutputStream.flush();
    
                // Wait for confirmation from the server
                read = socketInputStream.readInt();
                if (read == tftpCodes.OK) {
                    System.out.println("Book added successfully.");
                } else {
                    System.out.println("Error adding book.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method sends the book title to the client and receives the information
     * of that book. It then asks the user to enter a new price and quantity in stock
     * and sends them to the server. finally it receives a response that confirms
     * whether the book update was succesful or not. 
     */
    private synchronized void modifyBook(){
        try{
            //Send MODIFYBOOK command to the server
            socketOutputStream.writeInt(tftpCodes.MODIFYBOOK);
            socketOutputStream.flush();

            //Read response from the server 
            int read = socketInputStream.readInt();
            if(read == tftpCodes.OK){
                Scanner reader = new Scanner(System.in);
                System.out.print("Enter the book's title: ");
                String title = readKeyboard(reader);

                // Send the book title to the server
                socketOutputStream.write(title.getBytes());
                socketOutputStream.flush();

                //server sends book information to the client 
                byte[] bookBuffer = new byte[tftpCodes.BUFFER_SIZE];
                socketInputStream.read(bookBuffer);
                
                //read book information
                String bookInfo = new String(bookBuffer);
                System.out.println(title + " book information:");
                System.out.println(bookInfo);

                // Ask if the user if they want to modify the book
                System.out.println("Do you want to modify the book? (y/n)");
                String answer = readKeyboard(reader);

                //Write OK to client
                socketOutputStream.writeInt(tftpCodes.OK);
                socketOutputStream.flush();

                // Validate if the user wants to modify the book
                if(answer.equals("y")){
                //Update book price and quantity in stock
                    System.out.println("Modifying " + title);
                    System.out.print("New Price: $");
                    String modifyDetails = readKeyboard(reader);
                    System.out.print("New Quantity: ");
                    modifyDetails += ", " + readKeyboard(reader);
                    
                    // Send the new book information to the server
                    socketOutputStream.write(modifyDetails.getBytes());
                    socketOutputStream.flush();

                    //read server response
                    read = socketInputStream.readInt();

                    // Server confirmation
                    if(read  == tftpCodes.OK){
                        System.out.println("Book modified successfully!");
                    }
                    else{
                        System.out.println("Book not modified!");
                    }
                //User does not want to modify the book
                }else{
                    System.out.println("Book not modified!");
                    System.out.println("Returning to bookstore menu");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method to delete a book from the server. It sends the book title to the client
     * and recieves the information of that book. The server then sends the confirmation
     * based on whether the purchase was succesful or not.
     */
    private synchronized void buyBook(){
        try{
            //Send BUYBOOK command to the server
            socketOutputStream.writeInt(tftpCodes.BUYBOOK);
            socketOutputStream.flush();

            //Read OK and send the book title to the client
            int read = socketInputStream.readInt();
            if(read == tftpCodes.OK){
                Scanner reader = new Scanner(System.in);
                System.out.print("Enter the book's title: ");
                String title = readKeyboard(reader);

                // alternative use getBytes
                socketOutputStream.write(title.getBytes());
                socketOutputStream.flush();

                //server sends book information to the client 
                byte[] bookBuffer = new byte[tftpCodes.BUFFER_SIZE];
                socketInputStream.read(bookBuffer);
                
                //read book information
                String bookInfo = new String(bookBuffer);
                System.out.println(title + " book information:");
                System.out.println(bookInfo);

                // Ask if the user if they want to modify the book
                System.out.println("Do you want to buy the book? (y/n)");
                String answer = readKeyboard(reader);

                //Write OK to server
                socketOutputStream.writeInt(tftpCodes.OK);
                socketOutputStream.flush();

                // Validate if the user wants to modify the book
                if(answer.equals("y")){

                    System.out.println("Transaction in progress... ");
                    
                    read = socketInputStream.readInt();

                    // Read OK from server
                    if(read  == tftpCodes.OK){
                        System.out.println("Book purchased successfully!");
                    }
                    else{
                        System.out.println("Purchase failed!");
                    }
                //User does not want to modify the book
                }else{
                    System.out.println("Purchase cancelled");
                    System.out.println("Returning to bookstore menu");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method sends the LISTALLGENRE command to the server and prints the 
     * genre list after receiving it after the server sends it
     */
    private void listGenre() {
        try {
            // send list genre command
            socketOutputStream.writeInt(tftpCodes.LISTALLGENRE);
            socketOutputStream.flush();
    
            // Clear out the buffer
            socketInputStream.skip(socketInputStream.available());
    
            // Read the list from the server
            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            int read = socketInputStream.read(buffer);
            String genres = new String(buffer, 0, read).trim();
            System.out.println(genres);
    
            //Write OK to server
            socketOutputStream.writeInt(tftpCodes.OK);
            socketOutputStream.flush();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
/**
 * this method sends the LISTALLBOOKSBGENRE command to the server and receives the book list 
 * in every genre.
 */
    private synchronized void listGenreBook() {
        try {
            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            int read;
    
            // send list genre command
            socketOutputStream.writeInt(tftpCodes.LISTALLBOOKBYGENRE);
            socketOutputStream.flush();
            
            //Read genre and book list from server
            read = socketInputStream.read(buffer);
    
            //print every genre and all books in each genre
            String genresAndBooks = new String(buffer, 0, read).trim();
            System.out.println(genresAndBooks);
    
            //write OK to server
            socketOutputStream.writeInt(tftpCodes.OK);
            socketOutputStream.flush();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/**
 * this method sends the genre title to the server and receives the book list
 * associated with that specific genre
 * @param genre //genre title
 */
    private synchronized void listInputGenreBook(String genre) {
        try {
            byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
            int read;
    
            // send list genre command with genre input
            socketOutputStream.writeInt(tftpCodes.LISTALLBOOKPARTGENRE);
            socketOutputStream.writeUTF(genre); // send genre input
            socketOutputStream.flush();
    
            //read book list from server
            read = socketInputStream.read(buffer);
    
            //print the book list
            String genresAndBooks = new String(buffer, 0, read).trim();
            System.out.println(genresAndBooks);
    
            //Write OK to server
            socketOutputStream.writeInt(tftpCodes.OK);
            socketOutputStream.flush();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method sends the book title to the server and receives the information
     * associated with that book
     * @param bookTitle //title of the book
     */
    public void searchBook(String bookTitle) {
        try {
            // Send the searchBook command to the server
            socketOutputStream.writeInt(tftpCodes.SEARCHBOOK);
            socketOutputStream.writeUTF(bookTitle); // send the book title
            socketOutputStream.flush();

            // Wait for the server's response
            int read = socketInputStream.readInt();
            if (read == tftpCodes.OK) {
                // Read the book information from the server
                byte[] buffer = new byte[tftpCodes.BUFFER_SIZE];
                read = socketInputStream.read(buffer);

                //print book details
                String bookDetails = new String(buffer, 0, read).trim();
                System.out.println(bookDetails);

                //Write OK to the server
                socketOutputStream.writeInt(tftpCodes.OK);
                socketOutputStream.flush();
            }else {
                System.out.println("Book not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method closes the connection with the server
     */
    private void exitCommand() {
        try {
            // Send CloseConnection command
            socketOutputStream.writeInt(tftpCodes.CLOSECONNECTION);
            socketOutputStream.flush();

            // Wait for OK code
            int read = socketInputStream.readInt();
            System.out.println("Goodbye!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int readCommands(Scanner reader) {
        // Print the prompt
        System.out.print(">");
    
        // Read the next command
        String sText = reader.nextLine();
        int results = -1;
    
        // Parse the sentence
        StringTokenizer st = new StringTokenizer(sText);
        if (st.hasMoreTokens()) {
            String command = st.nextToken();
            try {
                results = Integer.parseInt(command);
            } catch (NumberFormatException e) {
                // If the command is not a number, set results to -1
                results = -1;
            }
        }
    
        return results;
    }

   
} 