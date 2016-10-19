package com.dualion.meetup.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Hijo.
 */
@Entity
@Table(name = "hijo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hijo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "apellidos")
    private String apellidos;

    @ManyToOne
    private Padre padre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Hijo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Hijo apellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Padre getPadre() {
        return padre;
    }

    public Hijo padre(Padre padre) {
        this.padre = padre;
        return this;
    }

    public void setPadre(Padre padre) {
        this.padre = padre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hijo hijo = (Hijo) o;
        if(hijo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, hijo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Hijo{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", apellidos='" + apellidos + "'" +
            '}';
    }
}
