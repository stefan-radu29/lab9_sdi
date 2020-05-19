package ro.ubb.springjpa.domain.model;

import javax.persistence.Entity;

/**
 * The type Book.
 */
@Entity
public class Book extends BaseEntity<Integer>
{
    private String title;
    private String author;
    private String publisher;
    private int publicationYear;
    private float price;

    public Book(){}

    /**
     * Instantiates a new Book.
     *
     * @param title           String representing the title of the book
     * @param author          String representing the author of the book
     * @param publisher       String representing the publisher of the book
     * @param publicationYear int representing the publication year of the book
     * @param price           float representing the price of the book
     */
    public Book(String title, String author, String publisher, int publicationYear, float price)
    {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.price = price;
    }

    /**
     * Gets title.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets publisher.
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets publisher.
     * @param publisher the publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets author.
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets publication year.
     * @return the publication year
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * Sets publication year.
     * @param publicationYear the publication year
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * Gets price.
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * Sets price.
     * @param price the price
     */
    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object object)
    {
        if(this == object)
            return true;
        if(object ==  null || getClass() != object.getClass())
            return false;

        return this.author.equals(((Book) object).author) &&
                this.title.equals(((Book) object).title) &&
                this.publisher.equals(((Book) object).publisher) &&
                this.publicationYear == ((Book) object).publicationYear;
    }

    @Override
    public String toString()
    {
        return "Book{" + super.toString() + " " +
                "title='" + this.title + '\'' +
                ", author='" + this.author + '\'' +
                ", publisher='" + this.publisher + '\'' +
                ", publicationYear=" + this.publicationYear +
                ", price=" + this.price +
                "}";
    }

}
