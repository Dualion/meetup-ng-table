package com.dualion.meetup.service;

import com.dualion.meetup.service.dto.HijoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Hijo.
 */
public interface HijoService {

    /**
     * Save a hijo.
     *
     * @param hijoDTO the entity to save
     * @return the persisted entity
     */
    HijoDTO save(HijoDTO hijoDTO);

    /**
     *  Get all the hijos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<HijoDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" hijo.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    HijoDTO findOne(Long id);

    /**
     *  Delete the "id" hijo.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
