package com.dualion.meetup.service.mapper;

import com.dualion.meetup.domain.*;
import com.dualion.meetup.service.dto.HijoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Hijo and its DTO HijoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HijoMapper {

    @Mapping(source = "padre.id", target = "padreId")
    HijoDTO hijoToHijoDTO(Hijo hijo);

    List<HijoDTO> hijosToHijoDTOs(List<Hijo> hijos);

    @Mapping(source = "padreId", target = "padre")
    Hijo hijoDTOToHijo(HijoDTO hijoDTO);

    List<Hijo> hijoDTOsToHijos(List<HijoDTO> hijoDTOs);

    default Padre padreFromId(Long id) {
        if (id == null) {
            return null;
        }
        Padre padre = new Padre();
        padre.setId(id);
        return padre;
    }
}
