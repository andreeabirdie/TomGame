package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Countries", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Country implements Serializable {
    String name;

    public Country() {
    }

    @Id
    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
