package domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Seas", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Sea implements Serializable {
    String name;

    public Sea() {
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
