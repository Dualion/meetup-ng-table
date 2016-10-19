package com.dualion.meetup.service;

import com.dualion.meetup.service.dto.PadreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Padre.
 */
public interface PadreService {

    /**
     * Save a padre.
     *
     * @param padreDTO the entity to save
     * @return the persisted entity
     */
    PadreDTO save(PadreDTO padreDTO);

    /**
     *  Get all the padres.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PadreDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" padre.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PadreDTO findOne(Long id);

    /**
     *  Delete the "id" padre.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
