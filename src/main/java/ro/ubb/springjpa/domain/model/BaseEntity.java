package ro.ubb.springjpa.domain.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * The type BaseEntity.
 *
 * @param <ID> the type parameter
 */
@MappedSuperclass
public class BaseEntity<ID extends Serializable> implements Serializable
{
    @Id
    @GeneratedValue
    private ID id;

    /**
     * Gets id.
     * @return the id which is of type T
     */
    public ID getId()
    {
        return this.id;
    }

    /**
     * Sets id.
     * @param newId the new id of type T
     */
    public void setId(ID newId)
    {
        this.id = newId;
    }

    @Override
    public String toString()
    {
        return "BaseEntity{"+
                "id=" +
                this.id.toString() +
                "}";
    }
}
