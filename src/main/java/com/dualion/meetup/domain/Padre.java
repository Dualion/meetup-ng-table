package com.dualion.meetup.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Padre.
 */
@Entity
@Table(name = "padre")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Padre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @OneToMany(mappedBy = "padre")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Hijo> hijos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Padre nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Padre apellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Set<Hijo> getHijos() {
        return hijos;
    }

    public Padre hijos(Set<Hijo> hijos) {
        this.hijos = hijos;
        return this;
    }

    public Padre addHijo(Hijo hijo) {
        hijos.add(hijo);
        hijo.setPadre(this);
        return this;
    }

    public Padre removeHijo(Hijo hijo) {
        hijos.remove(hijo);
        hijo.setPadre(null);
        return this;
    }

    public void setHijos(Set<Hijo> hijos) {
        this.hijos = hijos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Padre padre = (Padre) o;
        if(padre.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, padre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Padre{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", apellidos='" + apellidos + "'" +
            '}';
    }
}
