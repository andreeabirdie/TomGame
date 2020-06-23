package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Cities", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class City implements Serializable {
    String name;

    public City() {
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
