package tftpexample;

public class Book {
    private String title;
    private String plot;
    private String authorName;
    private SLList<String> authors;
    private int releaseYear;
    private double price;
    private int quantityInStock;

    // Constructor
    public Book(String title, String plot, String authorName, int releaseYear, double price, int quantityInStock) {
        this.title = title;
        this.plot = plot;
        this.authorName = authorName;
        this.authors = new SLList<>(); // Initialize the authors list
        this.authors.addToHead(authorName); // Add the author name to the list
        this.releaseYear = releaseYear;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

     // Constructor with only title
     public Book(String title) {
        this.title = title;
        this.plot = "";
        this.authors = new SLList<>();
        this.releaseYear = 0;
        this.price = 0.0;
        this.quantityInStock = 0;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public SLList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(SLList<String> authors) {
        this.authors = authors;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }


    @Override
    public String toString() {
        return "Title: " + title +
                "\nPlot: " + plot +
                "\nAuthors: " + authorsToString() +
                "\nRelease Year: " + releaseYear +
                "\nPrice: $" + price +
                "\nQuantity in Stock: " + quantityInStock;
    }

    // Helper method to convert authors to string
    private String authorsToString() {
        StringBuilder stringBuilder = new StringBuilder();
        SLLNode<String> current = authors.getHead();
        while (current != null) {
            stringBuilder.append(current.getInfo()).append(", ");
            current = current.getNext();
        }
        // Removing the trailing comma and space if there are authors
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        return stringBuilder.toString();
    }
}