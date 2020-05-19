package ro.ubb.springjpa.domain.model;

import javax.persistence.Entity;

/**
 * The type Client.
 */
@Entity
public class Client extends BaseEntity<Integer>
{
    private String firstName;
    private String lastName;
    private String address;

    public Client(){}

    /**
     * Instantiates a new Client.
     *
     * @param firstName String representing the first name of the client
     * @param lastName  String representing the the last name of the client
     * @param address   String representing the the address of the client
     */
    public Client(String firstName, String lastName, String address)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }


    /**
     * Gets first name.
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets address.
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        return this.address.equals(((Client) object).address) && this.firstName.equals(((Client) object).firstName) && this.lastName.equals(((Client) object).lastName);
    }

    @Override
    public String toString()
    {
        return "Client{" + super.toString() + " " +
                "firstName='" + this.firstName + '\'' +
                ", lastName='" + this.lastName + '\'' +
                ", address='" + this.address + '\''  +
                "}";
    }
}
