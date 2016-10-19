package com.dualion.meetup.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Padre entity.
 */
public class PadreDTO implements Serializable {

    private Long id;

    private String nombre;

    private String apellidos;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PadreDTO padreDTO = (PadreDTO) o;

        if ( ! Objects.equals(id, padreDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PadreDTO{" +
            "id=" + id +
            ", nombre='" + nombre + "'" +
            ", apellidos='" + apellidos + "'" +
            '}';
    }
}
