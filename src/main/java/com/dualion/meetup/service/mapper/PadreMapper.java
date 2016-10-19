package com.dualion.meetup.service.mapper;

import com.dualion.meetup.domain.*;
import com.dualion.meetup.service.dto.PadreDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Padre and its DTO PadreDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PadreMapper {

    PadreDTO padreToPadreDTO(Padre padre);

    List<PadreDTO> padresToPadreDTOs(List<Padre> padres);

    @Mapping(target = "hijos", ignore = true)
    Padre padreDTOToPadre(PadreDTO padreDTO);

    List<Padre> padreDTOsToPadres(List<PadreDTO> padreDTOs);
}
