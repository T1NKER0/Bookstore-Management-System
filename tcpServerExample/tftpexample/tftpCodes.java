package tftpexample;

public class tftpCodes {

	//Buffer size
	public static final int BUFFER_SIZE=1024;
	
	
	//Data transfer codes--------------------------------------------
	//OK 
	public static final int OK=1;
	
	//Get
	public static final int GET=2;
	
	//Put
	public static final int PUT=3;
	
	//Close connection
	public static final int CLOSECONNECTION=4;

	//Menu codes--------------------------------------------------------
	//Add genre
	public static final int ADDGENRE = 5;
	
	//Add book
	public static final int ADDBOOK = 6;

	//Modify a book in stock
	public static final int MODIFYBOOK = 7;

	//List all genres
	public static final int LISTALLGENRE = 8;

	//List all book by genre
	public static final int LISTALLBOOKBYGENRE = 9;

	//List all book for a particular genre
	public static final int LISTALLBOOKPARTGENRE = 10;

	//Buy a book
	public static final int BUYBOOK = 11;

	public static final int SEARCHBOOK = 12;


	//Error messages----------------------------------------------------
	//Item not found
	public static final int ITEMNOTFOUND=20;
	
	//The intended item already exists
	public static final int EXISTINGITEM=21;

	//No genres
	public static final int GENRENOTFOUND = 22;
	
	//No valid command
	public static final int WRONGCOMMAND=30;
	
}
