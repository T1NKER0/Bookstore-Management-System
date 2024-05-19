# Bookstore-Management-System

## Trivial FTP bookstore management system based on binary search trees.

# Usability

To add a genre, the program reads the genre title (e.g., Action, Comedy, Thriller, etc.).

To add a Book, the program reads the following: Title, genre, plot, authors, release year, price, and quantity in stock. For the Book’s genre, the program shows a list with all genres in the database, and the user selects a genre from the list. For each author, the program reads the last and first name.

To modify a Book in stock, the program reads the title, shows all information about the Book, asks if the user really wants to modify it, and for an affirmative answer, reads the new price and the quantity in stock.

When listing all genres, the program shows them in alphabetical order.

When listing all Books by genre, the program prints the genre title and all Books for that genre in alphabetical order by title. For each Book, the program shows the title, release year, authors, quantity in stock, and price.

When listing all Books for a particular genre, the program reads the genre and shows a list with all Books for the selected genre with the title, release year, and authors.

For buying a Book, the program reads the title and shows all information about the Book. The authors are alphabetically sorted by last name, quantity in stock, and price. Once a book is bought, the quantity in stock is updated.

# Project Architecture

The list of genres is implemented using a binary search tree sorted based on the genre’s title. Each node in this tree contains two attributes: the genre’s title and a sorted double circular list with Books’ information. For each Book, the authors list is implemented using a singly linked list ordered by the author’s last name.

The project defines a multi-thread server where the data is stored, and a client that implements the user interface.









